package `in`.asclepius.app.activities

import `in`.asclepius.app.databinding.ActivityRegisterAsDoctorBinding
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException


class RegisterAsDoctor : AppCompatActivity() {

    private lateinit var filePath: Uri
    private val PICK_RESUME: Int = 1056
    lateinit var binding: ActivityRegisterAsDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAsDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.chooseResumeFile.setOnClickListener(View.OnClickListener {

        })

    }

    private fun SelectImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_RESUME
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_RESUME && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {

            try {
                filePath = data.data!!
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filePath
                    )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}