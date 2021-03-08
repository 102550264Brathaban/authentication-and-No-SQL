package app.com.firebaseauthentication

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RegistrationActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val dbRef = db.collection("users")

    lateinit var text1 : TextInputEditText
    lateinit var text2 : TextInputEditText
    lateinit var text3 : TextInputEditText
    lateinit var text4 : TextInputEditText
    lateinit var button1 : Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         text1 = findViewById<TextInputEditText>(R.id.textInputEditText1)
         text2 = findViewById<TextInputEditText>(R.id.textInputEditText2)
         text3 = findViewById<TextInputEditText>(R.id.textInputEditText3)
         text4 = findViewById<TextInputEditText>(R.id.textInputEditText4)


         button1 = findViewById(R.id.button1)


        register()
    }

    private fun register() {
        button1.setOnClickListener {
            if (text1.text.toString() == "") {
                text1.error = "Please enter first name"
                return@setOnClickListener
            }

            if (text2.text.toString() == "") {
                text2.error = "Please enter last name"
                return@setOnClickListener
            }

            if (text3.text.toString() == "") {
                text3.error = "Please enter email"
                return@setOnClickListener
            }

            if (text4.text.toString() == ""){
                text4.error = "Please enter password"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(text3.text.toString(),text4.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        val user = auth.currentUser
                        val userDetail = UserDetail(text1.text.toString(),text2.text.toString(),text3.text.toString(),text4.text.toString(),"")
                        dbRef.document(user!!.uid).set(userDetail)
                        Toast.makeText(this,"Registration is Successful",Toast.LENGTH_SHORT).show()
                        finish()   //YUP CLOSES THE ACTIVITY AND GOES BACK TO THE ACTIVITY WHICH CALLED THIS ACTIVITY YUP

                    }
                   else
                        Toast.makeText(this,"Registration failed Please try again",Toast.LENGTH_SHORT).show()

                }

        }


    }




}

//YOU NEED TO ENABLE FIREBASE AUTHENTICATION FROM FIREBASE WEBSITE FIRST YUP