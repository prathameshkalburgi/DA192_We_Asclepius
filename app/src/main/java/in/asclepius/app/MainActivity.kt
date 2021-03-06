package `in`.asclepius.app

import `in`.asclepius.app.activities.BookAppointment
import `in`.asclepius.app.activities.BookTeleconsultation
import `in`.asclepius.app.activities.ManageAppointment
import `in`.asclepius.app.activities.SearchDoctors
import `in`.asclepius.app.dailogs.LoadingDialog
import `in`.asclepius.app.databinding.ActivityMainBinding
import `in`.asclepius.app.databinding.AppointmentCardBinding
import `in`.asclepius.app.models.AppUser
import `in`.asclepius.app.models.ModelAppointment
import `in`.asclepius.app.others.Constants
import `in`.asclepius.app.others.SharedPrefsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {


    private lateinit var signedInUser: AppUser

    private val userReference =
        FirebaseDatabase.getInstance().getReference(Constants.USER_DATABASE_REFERENCE)
    var firebaseUser: FirebaseUser? = null
    lateinit var binding: ActivityMainBinding
    lateinit var mContext: Context
    var roleOfUser: String? = null
    var appointmentList = mutableListOf<ModelAppointment>()

    lateinit var loadingDailog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this

        binding.bookAppointmentCard.setOnClickListener(View.OnClickListener {
            startActivity(Intent(mContext, BookAppointment::class.java))
        })

        roleOfUser = intent.getStringExtra(Constants.USER_TYPE)

        if (roleOfUser != null) {
            binding.normalUserLayout.visibility = View.GONE
            binding.doctorLayout.visibility = View.VISIBLE
            setUpComingAppointments()
        } else {
            SharedPrefsManager(this@MainActivity).setString(Constants.USER_TYPE, "DOCTOR");
            binding.normalUserLayout.visibility = View.VISIBLE
            binding.doctorLayout.visibility = View.GONE
        }

        binding.manageAppointments.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ManageAppointment::class.java))
        })

        binding.bookTeleconsultation.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, BookTeleconsultation::class.java))
        })

        binding.searchDoctors.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SearchDoctors::class.java))
        })

        binding.toolbarCard.toolbar.setNavigationIcon(null)

        /* RatingDailog(this, Doctors(), object : RateCallback {
             override fun rateDoctor(doctor: Doctors, rating: ModelRating) {
                 addRating(doctor, rating)
             }
         }).show() */



        setSelfCard()

    }



    private fun setUpComingAppointments() {

        loadingDailog = LoadingDialog(this)
        loadingDailog.show()
        loadingDailog.setTitle(getString(R.string.getting_appointments))

        val databaseReference = FirebaseDatabase.getInstance().getReference(Constants.APPOINTMENTS)
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                Log.d("Child", "Child - > " + snapshot.value);
                val appointment = snapshot.getValue(ModelAppointment::class.java)!!
                appointmentList.add(appointment)
                setAppointmentAdapter()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setAppointmentAdapter() {

        loadingDailog.dismiss()

        for (appointment in appointmentList) {
            val view =
                AppointmentCardBinding.inflate(layoutInflater, binding.appointmentContainer, false)
            view.patientDetails.text = appointment.data
            binding.appointmentContainer.addView(view.root)
        }

    }

    private fun setSelfCard() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUser?.uid?.let {
            userReference.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.d("UserData", "User Dataerror :  " + error);
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("UserData", "User Data got : ");
                    signedInUser = snapshot.getValue(AppUser::class.java)!!
                    signedInUser?.let { it1 ->
                    }
                }
            })
        }
    }


}