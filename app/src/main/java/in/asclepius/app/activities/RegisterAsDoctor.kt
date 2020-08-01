package `in`.asclepius.app.activities

import `in`.asclepius.app.R
import `in`.asclepius.app.dailogs.LoadingDialog
import `in`.asclepius.app.databinding.ActivityRegisterAsDoctorBinding
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class RegisterAsDoctor : AppCompatActivity() {

    private var filePath: Uri? = null
    private val PICK_RESUME: Int = 1056
    lateinit var binding: ActivityRegisterAsDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAsDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.chooseResumeFile.setOnClickListener(View.OnClickListener {
            selectResume()
        })

        binding.upload.setOnClickListener(View.OnClickListener {
            if (binding.email.editText?.text.toString()
                    .isBlank() || !binding.email.editText?.text.toString()
                    .contains("@") || filePath == null
            ) {
                binding.email.error =
                    getString(R.string.please_enter_correct_email_and_choose_resume)
                binding.email.isErrorEnabled = true
            } else {
                val dailog = LoadingDialog(this@RegisterAsDoctor)
                dailog.show()
                dailog.setTitle(getString(R.string.uploading_details))

                Handler().postDelayed(Runnable {
                    dailog.dismiss()
                    showSuccess()
                }, 5000);
            }
        })

    }

    private fun showSuccess() {
        binding.successLayout.visibility = View.VISIBLE
        binding.normalLayout.visibility = View.GONE
    }

    private fun selectResume() {
        val intent = Intent()
        intent.type = "file/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Resume Via..."
            ),
            PICK_RESUME
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_RESUME) {
            if (resultCode == Activity.RESULT_OK) {
                val selectedFileURI: Uri? = data?.data
                val file = File(selectedFileURI?.path.toString())
                Log.d("ResumeFile", "File : " + file.getName())
                filePath = Uri.parse(file.getName().toString())
                Log.d("ResumeFile", "FilePath : " + filePath)
                binding.chooseText.text = file.name
            }
        }
    }
}