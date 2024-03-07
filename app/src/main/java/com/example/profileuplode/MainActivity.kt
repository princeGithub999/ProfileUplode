package com.example.profileuplode

import ModleClass.UserDataModle
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import java.util.UUID

class MainActivity : AppCompatActivity() {


    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var selectButton: AppCompatButton
    private lateinit var submitButton: AppCompatButton
    private lateinit var profileImage: ShapeableImageView
    private lateinit var getImageView: ShapeableImageView
    private lateinit var getNameTextView: TextView
    private lateinit var getEmailTextView: TextView
    private var db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        nameEditText = findViewById(R.id.enterName_EditText)
        emailEditText = findViewById(R.id.enterEmail_EditText)
        selectButton = findViewById(R.id.selectImage_Button)
        submitButton = findViewById(R.id.submit)
        profileImage = findViewById(R.id.image)
        getImageView = findViewById(R.id.getImage)
        getNameTextView = findViewById(R.id.getName)
        getEmailTextView = findViewById(R.id.getEmail)

        selectButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent, 10)

        }

        submitButton.setOnClickListener {
            get()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == RESULT_OK) {
            if (data != null) {

                val ref = Firebase.storage.reference.child("photo5888.png")
                ref.putFile(data.data!!).addOnSuccessListener {

                    val url = it.storage.downloadUrl
                    ref.downloadUrl.addOnSuccessListener { it ->
                        Picasso.get().load(it.toString()).into(profileImage)

                        val name = nameEditText.text.toString()
                        val email = emailEditText.text.toString()

                        val uid = UUID.randomUUID().toString()
                        val map = hashMapOf(
                            "id" to uid,
                            "userName" to name,
                            "userEmail" to email,
                            "image" to url
                        )
                        db.collection("User").document(uid).set(map)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Add Data success", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "data flied", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }

    fun get() {
        val stroage = FirebaseStorage.getInstance().reference
            .child("photo5888.png")
            .downloadUrl
            .addOnSuccessListener {
                Picasso.get().load(it.toString()).into(getImageView)

                Toast.makeText(this, "downloadUrl success", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                Toast.makeText(this, "downloadUrl Failure", Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }
    }
}