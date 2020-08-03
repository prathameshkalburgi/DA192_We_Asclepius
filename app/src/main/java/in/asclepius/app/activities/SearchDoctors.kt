package `in`.asclepius.app.activities

import `in`.asclepius.app.`interface`.OnDoctorSelectedCallback
import `in`.asclepius.app.adapters.DoctorsAdapter
import `in`.asclepius.app.databinding.ActivitySearchDoctorsBinding
import `in`.asclepius.app.models.Doctors
import `in`.asclepius.app.models.LocationClass
import `in`.asclepius.app.models.ModelDoctorFirebase
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

    private val dbReference = FirebaseDatabase.getInstance().getReference("app_doctors")
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val doctorsList: MutableList<ModelDoctorFirebase> = mutableListOf()
    private val modelList: MutableList<Doctors> = mutableListOf()
    lateinit var binding: ActivitySearchDoctorsBinding

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
                        try {
                            Log.d("ManageAppointmentClass", "children - > " + children.getValue())
                            val appointment = children.getValue(ModelDoctorFirebase::class.java)!!
                            doctorsList.add(appointment)
                            setAdapter()
                        } catch (e: Exception) {
                            Log.d("ManageAppointmentClass", "DBError : $e")
                            showNoDoctors()
                        }

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


}