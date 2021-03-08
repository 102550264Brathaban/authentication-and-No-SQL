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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PhotoUploadActivity : AppCompatActivity() {

    lateinit var button2 : Button
    lateinit var button3 : Button
    lateinit var button4 : Button
    lateinit var  filePath : Uri
    lateinit var imageV : ImageView
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_upload)

        button2 = findViewById(R.id.button3)
        button3 = findViewById(R.id.button4)
        button4 = findViewById(R.id.button5)

        imageV = findViewById(R.id.imageView)
        imageV.setImageResource(R.drawable.face)

        button2.setOnClickListener {

            startFileChooser()
        }
        button3.setOnClickListener {
            fileUpload()
        }

        button4.setOnClickListener {
             startActivity(Intent(this,ProfileActivity::class.java))
             finish()

        }
    }



    private fun startFileChooser() {
        var i = Intent()
        i.type = "image/*" //LET'S IT PICK IMAGES. YOU CAN PUT PDF IF YOU WANT TO PICK PDF YUP
        i.action = Intent.ACTION_GET_CONTENT// DEFINES THE ACTION TO PICK ITEMS OUT YUP
        startActivityForResult(Intent.createChooser(i,"choose picture"),111) // YUP CHOOSER ENABLES USER TO PICK AN ITEM YUP
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null)
        {
            filePath = data.data!!

            val inputStream = contentResolver.openInputStream(filePath)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            //var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)/// YOU CAN ALSO DO LIKE THIS YUP
            imageV.setImageBitmap(bitmap)


        }


    }


    private fun fileUpload() {
        if (filePath != null) {
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            val imageRef = FirebaseStorage.getInstance().reference.child("${auth.currentUser?.uid}/pic.jpg")
            imageRef.putFile(filePath)
                .addOnSuccessListener {
                    pd.dismiss()
                    Toast.makeText(this,"File uploaded", Toast.LENGTH_SHORT).show()


                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {
                    val progress : Double = (100.0 * it.bytesTransferred)/it.totalByteCount
                    pd.setMessage("uploaded ${progress.toInt()}%")
                }


           // SAVING THE Photo URL INSIDE USER'S DOCUMENT/////

            imageRef.downloadUrl.addOnSuccessListener {
                db.collection("users").document("${auth.currentUser?.uid}")
                    .update("photo_Url", it.toString())
            }




        }

    }


    }
