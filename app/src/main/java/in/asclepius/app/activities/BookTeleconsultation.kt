package `in`.asclepius.app.activities

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.OnDepartmentSelected
import `in`.asclepius.app.`interface`.OnDoctorSelectedCallback
import `in`.asclepius.app.`interface`.PatientSelectedCallback
import `in`.asclepius.app.adapters.DepartmentsAdapter
import `in`.asclepius.app.adapters.DoctorsAdapter
import `in`.asclepius.app.adapters.MemberAdapter
import `in`.asclepius.app.dailogs.LoadingDialog
import `in`.asclepius.app.databinding.ActivityBookTeleconsultationBinding
import `in`.asclepius.app.models.*
import `in`.asclepius.app.others.CacheConstants
import `in`.asclepius.app.others.Constants
import `in`.asclepius.app.others.SharedPrefsManager
import `in`.codeworld.spinnerdatepicker.MaterialSpinnerDatePicker
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import java.util.*
import kotlin.random.Random

class BookTeleconsultation : AppCompatActivity(), View.OnClickListener {

    private var datePicker: MaterialSpinnerDatePicker? = null
    private lateinit var doctorsAdapter: DoctorsAdapter
    private var selectedDateString: String? = null
    private var selectedDate: Date? = null
    private lateinit var departmentAdapter: DepartmentsAdapter
    private lateinit var memberAdapter: MemberAdapter
    lateinit var binding: ActivityBookTeleconsultationBinding
    lateinit var mContext: Context
    lateinit var specializationSheet: BottomSheetBehavior<View>
    lateinit var selectPatientSheet: BottomSheetBehavior<View>
    lateinit var doctorSheetLayout: BottomSheetBehavior<View>
    lateinit var bookAppointmentSheet: BottomSheetBehavior<View>
    val database = FirebaseDatabase.getInstance()
    val userMembersReference = database.getReference(Constants.USER_MEMBERS_REFERENCE)
    val userReference = database.getReference(Constants.USER_DATABASE_REFERENCE)
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val userMembers = mutableListOf<AppUser>()
    lateinit var selectedPatient: AppUser
    lateinit var sharedPrefsManager: SharedPrefsManager
    lateinit var signedInUser: AppUser
    lateinit var opdDepartmentSelected: OpdDepartments
    var doctorSelected: Doctors? = null

    val databaseReference = FirebaseDatabase.getInstance().getReference(Constants.APPOINTMENTS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookTeleconsultationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this

        sharedPrefsManager = SharedPrefsManager(this)
        binding.toolbarCard.toolbar.title = getString(R.string.bookAppointmentNow)

        binding.selectPatient.loadingView.root.visibility = View.VISIBLE
        binding.selectPatient.loadingView.title.text = "Getting family members data"

        setSelfCard()
        setDepartmentsAdapter()
        specializationSheet = BottomSheetBehavior.from(binding.specialitySheet)
        selectPatientSheet = BottomSheetBehavior.from(binding.patientSheet)

        doctorSheetLayout = BottomSheetBehavior.from(binding.doctorSheet)
        bookAppointmentSheet = BottomSheetBehavior.from(binding.bookAppointmentSheet)

        doctorSheetLayout.state = BottomSheetBehavior.STATE_HIDDEN
        specializationSheet.state = BottomSheetBehavior.STATE_HIDDEN
        bookAppointmentSheet.state = BottomSheetBehavior.STATE_HIDDEN

        binding.specialitySheetLayout.visibility = View.VISIBLE
        binding.doctorSheetLayout.visibility = View.VISIBLE

        Handler().postDelayed(Runnable {
            selectPatientSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }, 500);

        binding.selectPatient.addNewMember.setOnClickListener(this)

        binding.toolbarCard.toolbar.title = getString(R.string.bookTeleConsultation)

    }

    private fun setDepartmentsAdapter() {
        val gson = Gson()
        val departmentModel =
            gson.fromJson<DepartmentModel>(CacheConstants.ORG_DETAILS, DepartmentModel::class.java)
        departmentAdapter =
            DepartmentsAdapter(departmentModel, this, object : OnDepartmentSelected {
                override fun onOPDselected(opdDepartment: OpdDepartments) {
                    opdDepartmentSelected = opdDepartment
                    setSpecialitySelected()
                }
            })
        binding.selectSpeciality.departmentRV.layoutManager = LinearLayoutManager(this)
        binding.selectSpeciality.departmentRV.adapter = departmentAdapter

    }


    private fun setPatientSelected() {
        binding.selectPatient.selectedCard.root.visibility = View.VISIBLE
        specializationSheet.state = BottomSheetBehavior.STATE_EXPANDED
        binding.specialitySheetLayout.visibility = View.VISIBLE
        selectPatientSheet.isHideable = false
        binding.selectPatient.selectedCard.selectedAttribute.text =
            getString(R.string.patient_selected)
        binding.selectPatient.selectedCard.selectedValue.text = selectedPatient.fullName

        binding.selectPatient.selectedCard.edit.setOnClickListener(View.OnClickListener {
            binding.selectPatient.selectedCard.root.visibility = View.GONE
            binding.selectPatient.patientsLayout.visibility = View.VISIBLE
            specializationSheet.state = BottomSheetBehavior.STATE_HIDDEN
            doctorSheetLayout.state = BottomSheetBehavior.STATE_HIDDEN
        })
    }

    private fun setSpecialitySelected() {
        binding.selectSpeciality.selectedCard.root.visibility = View.VISIBLE
        with(binding.selectSpeciality)
        {
            selectedCard.selectedAttribute.text =
                getString(R.string.department) + " : " + opdDepartmentSelected.departmentName
            selectedCard.selectedValue.text =
                opdDepartmentSelected.availabeSpeciality[0].specialityName
        }

        binding.selectSpeciality.selectedCard.edit.setOnClickListener(View.OnClickListener {
            specializationSheet.state = BottomSheetBehavior.STATE_EXPANDED
            binding.selectSpeciality.selectedCard.root.visibility = View.GONE
            doctorSheetLayout.state = BottomSheetBehavior.STATE_HIDDEN
            binding.selectSpeciality.specialityLayout.visibility = View.VISIBLE
        })

        selectPatientSheet.state = BottomSheetBehavior.STATE_EXPANDED
        selectPatientSheet.isDraggable = false
        specializationSheet.isDraggable = false
        selectPatientSheet.isHideable = false
        doctorSheetLayout.state = BottomSheetBehavior.STATE_EXPANDED
        binding.doctorSheetLayout.visibility = View.VISIBLE

        val doctorsAvailableOnDate = opdDepartmentSelected.availabeSpeciality[0].doctors

        doctorsAdapter =
            DoctorsAdapter(this, doctorsAvailableOnDate, object : OnDoctorSelectedCallback {
                override fun onDoctorSelected(doctor: Doctors) {
                    doctorSelected = doctor
                    setDoctorSelected()
                }
            }, false)

        binding.selectDoctor.doctorsRV.layoutManager = LinearLayoutManager(this)
        binding.selectDoctor.doctorsRV.adapter = doctorsAdapter


    }

    private fun setDoctorSelected() {

        binding.selectDoctor.selectedCard.root.visibility = View.VISIBLE

        with(binding.selectDoctor)
        {
            selectedCard.selectedAttribute.text =
                getString(R.string.doctor_selected)
            selectedCard.selectedValue.text = doctorSelected?.name
        }

        binding.selectDoctor.selectedCard.edit.setOnClickListener(View.OnClickListener {
            doctorSheetLayout.state = BottomSheetBehavior.STATE_EXPANDED
            binding.selectDoctor.selectedCard.root.visibility = View.GONE
        })

        bookAppointmentSheet.state = BottomSheetBehavior.STATE_EXPANDED

        binding.confirm.requestConsultation.setOnClickListener(View.OnClickListener {
            bookAppointment()
        })

    }


    private fun setPatientsRV() {
        setPatientsAdapter()

        firebaseUser?.uid?.let {
            userMembersReference.child(it).addChildEventListener(object : ChildEventListener {
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
                    val user = snapshot.getValue(AppUser::class.java)
                    user?.let { it1 -> userMembers.add(it1) }
                    setPatientsAdapter()
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun setPatientsAdapter() {
        binding.selectPatient.loadingView.root.visibility = View.GONE
        memberAdapter = MemberAdapter(userMembers, this, object : PatientSelectedCallback {
            override fun onPatientSelected(patient: AppUser) {
                selectedPatient = patient
                memberAdapter.selectedPatient = patient
                memberAdapter.notifyDataSetChanged()
                setPatientSelected()
            }
        })
        binding.selectPatient.patientsRV.layoutManager = LinearLayoutManager(this)
        binding.selectPatient.patientsRV.adapter = memberAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addNewMember -> addNewMember()
        }
    }

    private fun addNewMember() {
        startActivity(Intent(this, AddFamilyMember::class.java))
    }

    private fun setSelfCard() {
        firebaseUser?.uid?.let {
            userReference.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    signedInUser = snapshot.getValue(AppUser::class.java)!!
                    signedInUser?.let { it1 ->
                        userMembers.add(it1)
                        setPatientsRV()
                    }
                }
            })
        }
    }

    private fun bookAppointment() {
        val dialog = LoadingDialog(this@BookTeleconsultation)
        dialog.show()
        dialog.setTitle(getString(R.string.booking_appointment))
        var appointment = ModelAppointment(
            selectedPatient,
            selectedDateString,
            false,
            doctorSelected,
            opdDepartmentSelected.departmentName,
            "Active",
            signedInUser,
            Random.nextInt(0, 50000),
            true
        )
        firebaseUser?.uid?.let {
            databaseReference.child(it).child(appointment.appointmentId.toString())
                .setValue(appointment)
                .addOnSuccessListener {
                    binding.successLayout.root.visibility = View.VISIBLE
                    dialog.dismiss()
                    binding.successLayout.successAnim.playAnimation()
                    binding.activityLayout.visibility = View.GONE
                    binding.successLayout.idGeneratedText.text =
                        appointment.appointmentId.toString() + " is your appointment id. Please keep this for further assistance"
                    binding.successLayout.successAnim.repeatCount = 0
                }.addOnFailureListener {
                    dialog.dismiss()
                    Toast.makeText(
                        this,
                        getString(R.string.something_went_wrong_try_again_later),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }

}