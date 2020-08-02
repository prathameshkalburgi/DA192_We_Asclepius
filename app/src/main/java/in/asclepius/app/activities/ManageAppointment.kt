package `in`.asclepius.app.activities

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.AppointmentActions
import `in`.asclepius.app.adapters.AppointmentsAdapter
import `in`.asclepius.app.dailogs.ConfirmCancellationDailog
import `in`.asclepius.app.dailogs.LoadingDialog
import `in`.asclepius.app.databinding.ActivityManageAppointmentBinding
import `in`.asclepius.app.models.ModelAppointment
import `in`.asclepius.app.others.Constants
import `in`.codeworld.spinnerdatepicker.MaterialSpinnerDatePicker
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ManageAppointment : AppCompatActivity() {

    private var selectedDateString: String? = null
    private var datePicker: MaterialSpinnerDatePicker? = null
    private lateinit var loadingDailog: LoadingDialog
    lateinit var binding: ActivityManageAppointmentBinding
    private val dbReference = FirebaseDatabase.getInstance().getReference(Constants.APPOINTMENTS)
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    var appointmentList = mutableListOf<ModelAppointment>()
    lateinit var confirmDailog: ConfirmCancellationDailog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadingView.root.visibility = View.VISIBLE
        getAndSetAppointments()
    }

    private fun getAndSetAppointments() {

        appointmentList.clear()

        firebaseUser?.uid?.let {
            dbReference.child(it).addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    Log.d("ManageAppointment", "Appointments : " + snapshot.getValue())
                    try {
                        appointmentList.add(snapshot.getValue(ModelAppointment::class.java)!!)
                        setAdapter()
                    } catch (e: Exception) {
                        showNoAppointments()
                    }

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }
            })
        }


    }

    private fun setAdapter() {
        appointmentList.reverse()
        val rvAdapter = AppointmentsAdapter(
            appointmentList as ArrayList<ModelAppointment>,
            this,
            object : AppointmentActions {
                override fun onCancel(appointment: ModelAppointment) {
                    confirmDailog = ConfirmCancellationDailog(
                        this@ManageAppointment,
                        object : AppointmentActions {
                            override fun onCancel(appointment: ModelAppointment) {
                                cancelAppointment(appointment)
                            }

                            override fun onReschedule(appointment: ModelAppointment) {
                                showDatePicker(appointment)
                            }
                        },
                        appointment
                    )

                    confirmDailog.show()
                }

                override fun onReschedule(appointment: ModelAppointment) {

                }
            })
        binding.loadingView.root.visibility = View.GONE
        with(binding.appointmentsRV)
        {
            layoutManager = LinearLayoutManager(this@ManageAppointment)
            adapter = rvAdapter
        }
    }

    fun showDatePicker(appointment: ModelAppointment) {
        datePicker = MaterialSpinnerDatePicker(this)
            .setDividerColor(`in`.asclepius.app.R.color.colorPrimary)
            .setNextButtonColor(`in`.asclepius.app.R.color.colorPrimary)
            .setNextButtonTextColor(`in`.asclepius.app.R.color.white)
            .setTopBarBGColor(`in`.asclepius.app.R.color.colorAccent)
            .setNextButtonText("Next")
            .setDefaultDateToToday()
            .setCloseOnTouchOutSide(true)
            .setTitle(getString(`in`.asclepius.app.R.string.select_appointment_date))
            .setTitleTextColor(`in`.asclepius.app.R.color.black) // for customizing text color of the title
            .setTopBarTextColor(`in`.asclepius.app.R.color.white) // For custom top bar text color
            .setOnDateSelectedListener(object :
                MaterialSpinnerDatePicker.MaterialDatePickerListener {
                override fun onDateSelected(
                    dateString: String?,
                    rawDateString: String?,
                    dateObject: Date?
                ) {
                    if (dateObject?.day?.minus(1) == -1) {
                        Toast.makeText(
                            this@ManageAppointment,
                            "Please Don't Select Sunday. Sunday is working day",
                            Toast.LENGTH_LONG
                        ).show()
                        datePicker?.show()
                    } else {
                        selectedDateString = dateString
                        selectedDateString?.let { rescheduleAppointement(appointment, it) }

                    }

                }
            })

        datePicker?.show()
    }

    private fun rescheduleAppointement(appointment: ModelAppointment, rescheduledDate: String) {

        loadingDailog = LoadingDialog(this)
        loadingDailog.show()
        loadingDailog.setTitle(getString(R.string.rescheduling))

        firebaseUser?.uid?.let {
            dbReference.child(it).child(appointment.appointmentId.toString()).child("status")
                .setValue("Cancelled").addOnSuccessListener {
                    loadingDailog.dismiss()
                    getAndSetAppointments()
                    Toast.makeText(
                        this@ManageAppointment,
                        getString(R.string.rescheduled),
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    loadingDailog.dismiss()
                    Toast.makeText(
                        this@ManageAppointment,
                        getString(R.string.something_went_wrong_try_again_later),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun cancelAppointment(appointment: ModelAppointment) {

        loadingDailog = LoadingDialog(this)
        loadingDailog.show()
        loadingDailog.setTitle(getString(R.string.cancelling_please_wait))

        firebaseUser?.uid?.let {
            dbReference.child(it).child(appointment.appointmentId.toString()).child("status")
                .setValue("Cancelled").addOnSuccessListener {
                    loadingDailog.dismiss()
                    getAndSetAppointments()
                    Toast.makeText(
                        this@ManageAppointment,
                        getString(R.string.cancelled),
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    loadingDailog.dismiss()
                    Toast.makeText(
                        this@ManageAppointment,
                        getString(R.string.something_went_wrong_try_again_later),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }

    private fun showNoAppointments() {
        binding.noAppointments.root.visibility = View.VISIBLE
        binding.loadingView.root.visibility = View.GONE
    }


}