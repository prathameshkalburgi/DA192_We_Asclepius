package `in`.asclepius.app.activities

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.OnDoctorSelectedCallback
import `in`.asclepius.app.`interface`.RateCallback
import `in`.asclepius.app.adapters.DoctorsAdapter
import `in`.asclepius.app.dailogs.LoadingDialog
import `in`.asclepius.app.dailogs.RatingDailog
import `in`.asclepius.app.databinding.ActivitySearchDoctorsBinding
import `in`.asclepius.app.models.*
import `in`.asclepius.app.others.Constants
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchDoctors : AppCompatActivity() {
    private val userReference =
        FirebaseDatabase.getInstance().getReference(Constants.USER_DATABASE_REFERENCE)
    private lateinit var ratingDailog: RatingDailog
    lateinit var loadingDailog: LoadingDialog
    private val dbReference = FirebaseDatabase.getInstance().getReference("app_doctors")
    private var firebaseUser = FirebaseAuth.getInstance().currentUser
    private val doctorsList: MutableList<ModelDoctorFirebase> = mutableListOf()
    private val modelList: MutableList<Doctors> = mutableListOf()
    lateinit var binding: ActivitySearchDoctorsBinding
    private lateinit var signedInUser: AppUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadingView.title.text = "Loading..."
        getAndSetDoctors()

        binding.search.setOnClickListener(View.OnClickListener {
            searchForDoctors()
        })

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchForDoctors()
            }

        })

        binding.cancelSearch.setOnClickListener(View.OnClickListener {
            binding.searchEditText.setText("")
            setAdapter()
        })

        setSelfCard()

    }

    private fun searchForDoctors() {
        if (modelList.size >= 0) {
            val doctorName = binding.searchEditText.text.toString()
            if (doctorName.isNotEmpty()) {
                val filteredByNameDocs = Doctors.filterDoctorsByName(modelList, doctorName)
                setAdapter(filteredByNameDocs, doctorName)
                binding.search.visibility = View.GONE
                binding.cancelSearch.visibility = View.VISIBLE

            } else {
                setAdapter()
                binding.showingResultText.visibility = View.GONE
                binding.search.visibility = View.VISIBLE
                binding.cancelSearch.visibility = View.GONE
                Toast.makeText(this, "Please Enter Doctor Name", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Please Wait...", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showNoDoctors() {
        binding.noResult.root.visibility = View.VISIBLE
        binding.noResult.emptyMessage.text =
            "No matching doctors found.Try changing search query or filters"
    }

    private fun getAndSetDoctors() {

        doctorsList.clear()

        firebaseUser?.uid?.let {
            dbReference.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (children in snapshot.children) {

                            Log.d("ManageAppointmentClass", "children - > " + children.getValue())
                            val appointment = children.getValue(ModelDoctorFirebase::class.java)!!
                            doctorsList.add(appointment)
                            setAdapter()

                    }
                }
            })

        }

    }

    private fun setAdapter(doctorsList: MutableList<Doctors>, name: String) {

        binding.showingResultText.visibility = View.VISIBLE


        if (doctorsList.size > 0) {

            binding.showingResultText.text =
                "Showing results for search query : $name\nMatching Results Found : " + doctorsList.size

            val rvAdapter =
                DoctorsAdapter(this, doctorsList.toTypedArray(), object : OnDoctorSelectedCallback {
                    override fun onDoctorSelected(doctor: Doctors) {

                    }

                    override fun onRateDoctor(doctor: Doctors) {
                        submitRecord(doctor)
                    }
                }, true)

            binding.showingResultText.text =
                "Showing results for search query : $name\nMatching Results Found : ${doctorsList.size}"

            binding.noResult.root.visibility = View.GONE
            binding.loadingView.root.visibility = View.GONE

            with(binding.doctorsRV)
            {
                layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this@SearchDoctors)
                adapter = rvAdapter
            }
        } else {
            showNoDoctors()
        }

    }

    private fun setAdapter() {

        modelList.clear()

        if (doctorsList.size > 0) {
            for (doctor in doctorsList) {
                val tempDoc = Doctors(doctor, LocationClass(15.3494861, 75.1080376))
                modelList.add(tempDoc)
            }

            val rvAdapter =
                DoctorsAdapter(this, modelList.toTypedArray(), object : OnDoctorSelectedCallback {
                    override fun onDoctorSelected(doctor: Doctors) {

                    }

                    override fun onRateDoctor(doctor: Doctors) {
                        submitRecord(doctor)
                    }
                }, true)

            binding.noResult.root.visibility = View.GONE
            binding.loadingView.root.visibility = View.GONE

            with(binding.doctorsRV)
            {
                layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this@SearchDoctors)
                adapter = rvAdapter
            }
        } else {
            showNoDoctors()
        }

    }

    private fun submitRecord(doctor: Doctors) {
        ratingDailog = RatingDailog(this, doctor, object : RateCallback {
            override fun rateDoctor(doctor: Doctors, rating: ModelRating) {
                addRating(doctor, rating)
            }
        })
        ratingDailog.show()
    }

    private fun addRating(doctor: Doctors, rating: ModelRating) {
        loadingDailog = LoadingDialog(this);
        loadingDailog.show()
        loadingDailog.setTitle("Posting rating...")
        val reference = FirebaseDatabase.getInstance().getReference(Constants.APP_DOCTORS_REF)
        rating.ratedBy = signedInUser

        firebaseUser?.uid?.let {
            reference.child(doctor.specialityId.toString()).child("ratings").child(
                it
            ).setValue(rating)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Posted Rating", Toast.LENGTH_SHORT).show()
                    loadingDailog.dismiss()
                }
                .addOnFailureListener {
                    loadingDailog.dismiss()
                    Toast.makeText(
                        this,
                        getString(R.string.something_went_wrong_try_again_later),
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
