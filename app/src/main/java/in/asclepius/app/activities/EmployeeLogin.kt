package `in`.asclepius.app.activities

import `in`.asclepius.app.MainActivity
import `in`.asclepius.app.R
import `in`.asclepius.app.dailogs.LoadingDialog
import `in`.asclepius.app.databinding.ActivityEmployeeLoginBinding
import `in`.asclepius.app.others.Constants
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class EmployeeLogin : AppCompatActivity() {

    lateinit var binding: ActivityEmployeeLoginBinding
    var roleSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener(View.OnClickListener {
            if (validateFields()) {
                val dialog = LoadingDialog(this@EmployeeLogin)
                dialog.show()
                dialog.setTitle(getString(R.string.authenticating_user))
                Handler().postDelayed(Runnable {
                    startActivity(
                        Intent(this@EmployeeLogin, MainActivity::class.java).putExtra(
                            Constants.USER_TYPE,
                            "DOCTOR"
                        )
                    )
                }, 8000)
            }
        })

        binding.registerAsDoctor.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@EmployeeLogin, RegisterAsDoctor::class.java))
        })

        binding.toolbarCard.toolbar.title = getString(R.string.organization_employee_login)

    }



    private fun validateFields(): Boolean {

        binding.emailTextField.isErrorEnabled = false
        binding.passwordTextField.isErrorEnabled = false

        if (binding.emailTextField.editText?.text.toString().trim()
                .isBlank() || !binding.emailTextField.editText?.text.toString().contains("@")
        ) {
            binding.emailTextField.error = getString(R.string.please_enter_correct_email)
            binding.emailTextField.isErrorEnabled = true
            return false
        }

        if (binding.passwordTextField.editText?.text.toString().trim()
                .isBlank() || binding.passwordTextField.editText?.text.toString().length < 6
        ) {
            binding.passwordTextField.error = getString(R.string.please_make_sure_password_is_valid)
            binding.passwordTextField.isErrorEnabled = true
            return false
        }

        /*if (!roleSelected) {
            binding.pleaseSelectRole.visibility = View.VISIBLE
            return false
        }*/
        return true
    }
}