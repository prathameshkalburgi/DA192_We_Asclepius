package `in`.asclepius.app.activities

import `in`.asclepius.app.R
import `in`.asclepius.app.dailogs.LoadingDialog
import `in`.asclepius.app.databinding.ActivityCreateAccountBinding
import `in`.asclepius.app.models.AppUser
import `in`.asclepius.app.others.Constants
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class CreateAccount : AppCompatActivity(), View.OnClickListener {

    private val SIGN_IN_CODE: Int = 2565
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    lateinit var binding: ActivityCreateAccountBinding
    lateinit var mContext: Context;
    private val TAG = "CreateAccount"
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDailog: LoadingDialog


    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference(Constants.USER_DATABASE_REFERENCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this

        binding.toolbarCard.toolbar.title = (mContext.resources.getString(R.string.createAccount))
        binding.toolbarCard.toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        binding.signUpUsingGoogle.setOnClickListener(View.OnClickListener {
            signUpUsingGoogle()
        })

        binding.createAccount.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

        loadingDailog = LoadingDialog(this)

    }

    private fun signUpUsingGoogle() {
        loadingDailog.show()
        loadingDailog.setTitle(getString(R.string.getting_google_accounts))
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Sign In was successful
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user,true)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    task.exception?.let {
                        googleSignInFailed(task.exception.toString())
                    } ?: kotlin.run {
                        googleSignInFailed("Something Went Wrong")
                    }

                }
            }
    }

    private fun updateUI(user: FirebaseUser?, isFromGoogleAuth: Boolean) {
        loadingDailog.show()
        loadingDailog.setTitle(getString(R.string.almost_done))

        if (isFromGoogleAuth) {
            val appUser = AppUser(user?.displayName, "--")
            user?.uid?.let {
                usersRef.child(it).setValue(appUser).addOnSuccessListener {
                    startActivity(Intent(mContext, VerifyMobileNumber::class.java))
                }.addOnFailureListener {
                }
            }
        } else {
            val appUser = AppUser(
                binding.fullNameTextField.editText?.text.toString(),
                binding.phoneNumber.editText?.text.toString()
            )
            user?.uid?.let {
                usersRef.child(it).setValue(appUser).addOnSuccessListener {
                    startActivity(Intent(mContext, VerifyMobileNumber::class.java).putExtra(Constants.INTENT_KEY_MOBILE_PHONE,appUser.mobileNumber))
                }.addOnFailureListener {
                }
            }
        }

    }

    private fun googleSignInFailed(error: String) {
        if (error.contains("The email address is already in use by another account.")) {
            binding.emailTextField.error = getString(R.string.email_exists)
            binding.emailTextField.isErrorEnabled = true
        } else {
            binding.errorCard.visibility = View.VISIBLE
            binding.errorText.text = getString(R.string.something_went_wrong_try_again_later)
        }

        loadingDailog.dismiss()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.createAccount -> createAccountWithCreds()
        }
    }

    private fun createAccountWithCreds() {
        if (validateFields()) {
            loadingDailog.show()
            loadingDailog.setTitle(getString(R.string.creatingAccount))
            auth.createUserWithEmailAndPassword(
                binding.emailTextField.editText?.text.toString().trim(),
                passwordTextField.editText?.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        updateUI(user,false)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        task.exception?.let {
                            googleSignInFailed(task.exception.toString())
                        } ?: kotlin.run {
                            googleSignInFailed("Something Went Wrong")
                        }
                    }
                }
        }
    }

    private fun validateFields(): Boolean {

        binding.fullNameTextField.isErrorEnabled = false
        binding.emailTextField.isErrorEnabled = false
        binding.phoneNumber.isErrorEnabled = false
        binding.passwordTextField.isErrorEnabled = false
        binding.confirmPasswordTextField.isErrorEnabled = false

        if (binding.fullNameTextField.editText?.text.toString().isBlank()) {
            binding.fullNameTextField.error = getString(R.string.please_enter_full_name)
            binding.fullNameTextField.isErrorEnabled = true
            return false
        }

        if (binding.emailTextField.editText?.text.toString()
                .isBlank() || !binding.emailTextField.editText?.text.toString().contains("@")
        ) {
            binding.emailTextField.error = getString(R.string.please_enter_correct_email)
            binding.emailTextField.isErrorEnabled = true
            return false
        }

        if (binding.phoneNumber.editText?.text.toString()
                .isBlank() || binding.phoneNumber.editText?.text.toString().length != 10
        ) {
            binding.phoneNumber.error = getString(R.string.please_enter_valid_mobile_number)
            binding.phoneNumber.isErrorEnabled = true
            return false
        }

        if (binding.passwordTextField.editText?.text.toString()
                .isBlank() || binding.passwordTextField.editText?.text.toString().length < 6
        ) {
            binding.passwordTextField.error = getString(R.string.please_make_sure_password_is_valid)
            binding.passwordTextField.isErrorEnabled = true
            return false
        }

        if (binding.passwordTextField.editText?.text.toString() != binding.confirmPasswordTextField.editText?.text.toString()) {
            binding.confirmPasswordTextField.error = getString(R.string.password_not_matching)
            binding.confirmPasswordTextField.isErrorEnabled = true
            return false
        }
        return true
    }

}