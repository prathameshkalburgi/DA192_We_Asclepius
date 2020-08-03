package `in`.asclepius.app.activities

import `in`.asclepius.app.`interface`.OnDoctorSelectedCallback
import `in`.asclepius.app.adapters.DoctorsAdapter
import `in`.asclepius.app.databinding.ActivitySearchDoctorsBinding
import `in`.asclepius.app.models.Doctors
import `in`.asclepius.app.models.ModelDoctorFirebase
import android.os.Bundle
import android.util.Log
import android.view.View
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

    }


    private fun showNoDoctors() {
        TODO("Not yet implemented")
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

    private fun setAdapter() {

        modelList.clear()

        if (doctorsList.size > 0) {
            for (doctor in doctorsList) {
                val tempDoc = Doctors(doctor)
                modelList.add(tempDoc)
            }

            val rvAdapter =
                DoctorsAdapter(this, modelList.toTypedArray(), object : OnDoctorSelectedCallback {
                    override fun onDoctorSelected(doctor: Doctors) {

                    }
                })

            binding.noAppointments.root.visibility = View.GONE
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