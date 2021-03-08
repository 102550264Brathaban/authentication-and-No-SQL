package app.com.firebaseauthentication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StreamDownloadTask
import java.io.InputStream

class ProfileActivity : AppCompatActivity() {


   lateinit var db : FirebaseFirestore
   lateinit var text1 : TextView
   lateinit var button1 : Button
   lateinit var imageV : ImageView

    val auth = FirebaseAuth.getInstance()

   val imageRef = FirebaseStorage.getInstance().reference.child("${auth.currentUser?.uid}/pic.jpg")//YOU CAN ALSO TAKE IT FROM WEB URL YUP

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        db = FirebaseFirestore.getInstance()
        text1 = findViewById(R.id.textView4)
        button1 = findViewById(R.id.button2)
        imageV = findViewById(R.id.imageView2)


        val docRef = db.collection("users").document(auth.currentUser!!.uid)
        docRef.get().addOnSuccessListener {
            val user = it.toObject(UserDetail::class.java)
            text1.text = "email ---- ${user?.email} \n\n first name --- ${user?.firstName} \n last name --- ${user?.lastName}"
        }


       imageRef.getBytes(1024*1024)
           .addOnSuccessListener {
               val bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
               imageV.setImageBitmap(bitmap)
           }

        button1.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }



}