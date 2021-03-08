package app.com.firebaseauthentication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {


   lateinit var text1 : TextInputEditText
   lateinit var text2 : TextInputEditText
   lateinit var button : Button
   lateinit var text3 : TextView


    var auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        text1 = findViewById(R.id.textInputEditText5)
        text2 = findViewById(R.id.textInputEditText6)
        button = findViewById(R.id.button)
        text3 = findViewById(R.id.textView3)





        val user = auth.currentUser
        if(user != null)
        {
            startActivity(Intent(this,ProfileActivity::class.java))
            finish() // PRESSING BACK BUTTON FROM PROFILE ACTIVITY WILL NOT TAKE IT BACK TO THIS LOGIN ACTIVITY
        }

        userLogin()
    }

    private fun userLogin() {

         button.setOnClickListener {

             if (text1.text.toString() == "") {
                 text1.error = "Please enter email"
                 return@setOnClickListener
             }

                 if (text2.text.toString() == "") {
                     text2.error = "Please enter password"
                     return@setOnClickListener
                 }


             auth.signInWithEmailAndPassword(text1.text.toString() ,text2.text.toString() )
                 .addOnCompleteListener {
                     if (it.isSuccessful)
                     {
                         db.collection("users").document("${auth.currentUser?.uid}")
                        .get().addOnSuccessListener {
                            val user = it.toObject(UserDetail::class.java)
                            if (user?.photo_Url == "") {
                                startActivity(Intent(this, PhotoUploadActivity::class.java))
                                finish()
                            }
                            else{
                                startActivity(Intent(this, ProfileActivity::class.java))
                                finish() // PRESSING BACK BUTTON FROM PROFILE ACTIVITY WILL NOT TAKE IT BACK TO THIS LOGIN ACTIVITY
                           }
                        }
                     }
                     else
                     {
                         Toast.makeText(this,"Login failed Please try again", Toast.LENGTH_SHORT).show()
                     }
                 }
         }

        text3.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }



    }
}