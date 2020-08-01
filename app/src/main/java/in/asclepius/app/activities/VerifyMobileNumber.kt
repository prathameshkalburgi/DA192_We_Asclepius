package `in`.asclepius.app.activities

import `in`.asclepius.app.MainActivity
import `in`.asclepius.app.R
import `in`.asclepius.app.databinding.ActivityVerifyMobileNumberBinding
import `in`.asclepius.app.models.AppUser
import `in`.asclepius.app.others.Constants
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerifyMobileNumber : AppCompatActivity() {

    private var otpNumber: Int = -1
    lateinit var binding: ActivityVerifyMobileNumberBinding;
    var cachedPhoneNumber: String? = "-1"
    val mContext = this@VerifyMobileNumber
    val database= FirebaseDatabase.getInstance()
    val userReference=database.getReference(Constants.USER_DATABASE_REFERENCE)
    lateinit var appUser: AppUser
    val firebaseUser= FirebaseAuth.getInstance().currentUser
    val TAG="VerifyMobileNumber"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyMobileNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cachedPhoneNumber=intent?.extras?.getString(Constants.INTENT_KEY_MOBILE_PHONE,"")

        initAppUser()

        if(cachedPhoneNumber!=null && cachedPhoneNumber!!.length==10) {
            sendOTP()
        }else{
            binding.numberLayout.visibility= View.VISIBLE
            binding.otpLayout.visibility= View.GONE
        }

        binding.sendOtpButton.setOnClickListener(View.OnClickListener {
            if(binding.phoneNumber.editText?.text.toString().isBlank() || binding.phoneNumber.editText?.text.toString().length!=10){
                binding.phoneNumber.error=getString(R.string.invalid_phone_number)
            }else{
                binding.sendOtpTxt.visibility= View.GONE
                binding.sendOtpLoading.visibility= View.VISIBLE
                sendOTP()
            }
        })

        binding.toolbarCard.toolbar.title=getString(R.string.verify_mobile_number)

    }

    private fun initAppUser() {

        firebaseUser?.uid?.let { userReference.child(it).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "initAppUser() : -> ${error.toString()}");
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                appUser=snapshot.getValue(AppUser::class.java)!!
                Log.d(TAG,"User details got are  : ${appUser.toString()}")
            }
        }) }

    }

    private fun sendOTP() {
        otpNumber = (100000..999999).random()
        cachedPhoneNumber = binding.phoneNumber.editText?.text.toString()
        binding.otpSentMessage.setText(String.format(resources.getString(R.string.otp_sent_confirmation), cachedPhoneNumber.toString()))
        binding.numberLayout.visibility= View.GONE
        binding.otpLayout.visibility= View.VISIBLE

        Handler().postDelayed(Runnable {
            binding.otpView.setOTP("123456")
            setNumberAsVerified()
        },8000);
    }


    private fun setNumberAsVerified() {
        binding.confirmOtpText.visibility= View.GONE
        binding.confirmOtpLoading.visibility= View.VISIBLE
        Handler().postDelayed(Runnable {
            showSuccess()
        },5000)
    }

    private fun showSuccess() {
        appUser.isNumberVerified=true
        appUser.mobileNumber=cachedPhoneNumber
        firebaseUser?.uid?.let { userReference.child(it).setValue(appUser).addOnSuccessListener {
            binding.otpLayout.visibility = View.GONE
            binding.numberLayout.visibility = View.GONE
            binding.successLayout.visibility = View.VISIBLE
            binding.successAnim.playAnimation()
            Handler().postDelayed(Runnable {
                startActivity(Intent(mContext, MainActivity::class.java))
            },2000);
        }.addOnFailureListener {
            showError()
        } }

    }

    private fun showError() {
        binding.invalidOTP.visibility = View.VISIBLE
        binding.invalidOTP.setText(getString(R.string.something_went_wrong_try_again_later))
        binding.otpLayout.visibility = View.VISIBLE
    }

    private fun showOTPLayout() {
        binding.otpLayout.visibility = View.VISIBLE
        binding.numberLayout.visibility = View.GONE
    }

}