package `in`.asclepius.app.activities

import `in`.asclepius.app.MainActivity
import `in`.asclepius.app.R
import `in`.asclepius.app.dailogs.LoadingDialog
import `in`.asclepius.app.databinding.ActivityLoginBinding
import `in`.asclepius.app.models.AppUser
import `in`.asclepius.app.others.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() , View.OnClickListener {

    private val SIGN_IN_CODE: Int=1545
    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var binding : ActivityLoginBinding
    lateinit var mContext : Context
    private val TAG="LoginActivity"
    private lateinit var loadingDailog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext=this;
        binding.toolbarCard.toolbar.title = mContext.getString(R.string.login)

        binding.createAccount.setOnClickListener(View.OnClickListener {
            startActivity(Intent(mContext,CreateAccount::class.java))
        })

        binding.toolbarCard.toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        binding.loginBtn.setOnClickListener(this)

        loadingDailog= LoadingDialog(this)
        loadingDailog.setCancelable(false)


        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signInUsingGoogle.setOnClickListener(View.OnClickListener {
            signUpUsingGoogle()
        })

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.loginBtn -> loginUser()
        }
    }

    private fun loginUser() {
        if(validateFields())
        {
            loadingDailog.show()
            loadingDailog.setTitle(getString(R.string.authenticating_user))

            auth.signInWithEmailAndPassword(binding.emailTextField.editText?.text.toString().trim(), binding.passwordTextField.editText?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Sign In With Email:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        Log.d(TAG, "signInWithEmail:failure"+ task.exception + " other messsage : "+task.exception!!.localizedMessage + task.exception?.message)
                        task.exception?.let {
                            showFailedMessage(task.exception.toString())
                        }?:kotlin.run {
                            showFailedMessage(null)
                        }
                    }
                }
        }
    }

    private fun showFailedMessage(message: String?) {

        binding.errorCard.visibility= View.VISIBLE

        if(message!=null) {
            if(message.contains("There is no user record corresponding to this identifier")){
                binding.errorText.text=getString(R.string.email_not_exist)
            }else if(message.contains("The password is invalid or the user does not have a password")){
                binding.errorText.text=getString(R.string.password_is_wrong_or_use_other_login)
            }else{
                binding.errorText.text=getString(R.string.unable_to_login_at_the_moment)
            }
        }else {
            binding.errorText.text=getString(R.string.unable_to_login_at_the_moment)
        }

        loadingDailog.setCancelable(true)
        loadingDailog.dismiss()
    }

    private fun updateUI(user: FirebaseUser?) {
        loadingDailog.show()
        loadingDailog.setTitle(getString(R.string.almost_done))
        initAppUser()
    }

    private fun validateFields(): Boolean {

        binding.emailTextField.isErrorEnabled=false
        binding.passwordTextField.isErrorEnabled=false

        if(binding.emailTextField.editText?.text.toString().trim().isBlank() || ! binding.emailTextField.editText?.text.toString().contains("@"))
        {
            binding.emailTextField.error=getString(R.string.please_enter_correct_email)
            binding.emailTextField.isErrorEnabled=true
            return false
        }

        if(binding.passwordTextField.editText?.text.toString().trim().isBlank() || binding.passwordTextField.editText?.text.toString().length<6)
        {
            binding.passwordTextField.error=getString(R.string.please_make_sure_password_is_valid)
            binding.passwordTextField.isErrorEnabled=true
            return false
        }

        return true

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
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    task.exception?.let {
                        showFailedMessage(task.exception.toString())
                    } ?: kotlin.run {
                        showFailedMessage("Something Went Wrong")
                    }

                }
            }
    }

    private fun initAppUser() {

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUser?.uid?.let {
            val userReference = FirebaseDatabase.getInstance().getReference(Constants.USER_DATABASE_REFERENCE)
            userReference.child(it).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "initAppUser() : -> ${error.toString()}");
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    try{
                        val appUser = snapshot.getValue(AppUser::class.java)!!
                        if(appUser.isNumberVerified)
                        {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }else{
                            startActivity(Intent(this@LoginActivity,VerifyMobileNumber::class.java).putExtra(Constants.INTENT_KEY_MOBILE_PHONE,appUser.mobileNumber))
                        }
                    }catch (e:Exception){
                        val appUser = AppUser(firebaseUser?.displayName, "--")
                        firebaseUser?.uid?.let {
                            userReference.child(it).setValue(appUser).addOnSuccessListener {
                                startActivity(Intent(mContext, VerifyMobileNumber::class.java))
                            }.addOnFailureListener {
                                showFailedMessage(null)
                            }
                        }

                    }

                }
            }) }
    }
}