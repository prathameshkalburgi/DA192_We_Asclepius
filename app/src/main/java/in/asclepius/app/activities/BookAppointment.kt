package `in`.asclepius.app.activities

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.OnDepartmentSelected
import `in`.asclepius.app.`interface`.PatientSelectedCallback
import `in`.asclepius.app.adapters.DepartmentsAdapter
import `in`.asclepius.app.adapters.MemberAdapter
import `in`.asclepius.app.databinding.ActivityBookAppointmentBinding
import `in`.asclepius.app.models.AppUser
import `in`.asclepius.app.models.DepartmentModel
import `in`.asclepius.app.models.OpdDepartments
import `in`.asclepius.app.others.CacheConstants
import `in`.asclepius.app.others.Constants
import `in`.asclepius.app.others.SharedPrefsManager
import `in`.codeworld.spinnerdatepicker.MaterialSpinnerDatePicker
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import java.util.*


class BookAppointment : AppCompatActivity(), View.OnClickListener {

    private lateinit var departmentAdapter: DepartmentsAdapter
    private lateinit var memberAdapter: MemberAdapter
    lateinit var binding: ActivityBookAppointmentBinding
    lateinit var mContext: Context
    lateinit var specializationSheet: BottomSheetBehavior<View>
    lateinit var selectPatientSheet: BottomSheetBehavior<View>
    lateinit var dateSelectionSheet: BottomSheetBehavior<View>
    lateinit var timeSelectionSheet: BottomSheetBehavior<View>
    val database = FirebaseDatabase.getInstance()
    val userMembersReference = database.getReference(Constants.USER_MEMBERS_REFERENCE)
    val userReference = database.getReference(Constants.USER_DATABASE_REFERENCE)
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val userMembers = mutableListOf<AppUser>()
    lateinit var selectedPatient: AppUser
    lateinit var sharedPrefsManager: SharedPrefsManager
    lateinit var signedInUser: AppUser
    lateinit var opdDepartmentSelected: OpdDepartments


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this

        sharedPrefsManager = SharedPrefsManager(this)

        /* binding.selectPatient.firstPatientCard.setOnClickListener(View.OnClickListener {
            setPatientSelected()
        }) */

        /*  binding.selectSpeciality.firstSpecialityCard.setOnClickListener(View.OnClickListener {
              setSpecialitySelected()
          })*/

        binding.datePicker.dateNext.setOnClickListener(View.OnClickListener {
            setDateSelected()
        })

        binding.toolbarCard.toolbar.title = getString(R.string.bookAppointmentNow)

        binding.selectPatient.loadingView.root.visibility = View.VISIBLE
        binding.selectPatient.loadingView.title.text = "Getting family members data"

        setSelfCard()

        setDepartmentsAdapter()

        specializationSheet = BottomSheetBehavior.from(binding.specialitySheet)
        selectPatientSheet = BottomSheetBehavior.from(binding.patientSheet)
        dateSelectionSheet = BottomSheetBehavior.from(binding.dateAndTimeSheet)
        timeSelectionSheet = BottomSheetBehavior.from(binding.timeSheet)

        timeSelectionSheet.state = BottomSheetBehavior.STATE_HIDDEN
        specializationSheet.state = BottomSheetBehavior.STATE_HIDDEN
        dateSelectionSheet.state = BottomSheetBehavior.STATE_HIDDEN
        binding.dateAndTimeSheetLayout.visibility = View.VISIBLE
        binding.specialitySheetLayout.visibility = View.VISIBLE
        binding.timeSheetLayout.visibility = View.VISIBLE

        Handler().postDelayed(Runnable {
            selectPatientSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }, 500);

        binding.selectPatient.addNewMember.setOnClickListener(this)

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
        binding.selectPatient.patientsLayout.visibility = View.GONE
        specializationSheet.state = BottomSheetBehavior.STATE_EXPANDED
        binding.specialitySheetLayout.visibility = View.VISIBLE
        selectPatientSheet.isHideable = false
        binding.selectPatient.selectedCard.selectedAttribute.text =
            getString(R.string.patient_selected)
        binding.selectPatient.selectedCard.selectedValue.text = selectedPatient.fullName

        binding.selectPatient.selectedCard.edit.setOnClickListener(View.OnClickListener {
            binding.selectPatient.selectedCard.root.visibility = View.GONE
            binding.selectPatient.patientsLayout.visibility = View.VISIBLE
            dateSelectionSheet.state = BottomSheetBehavior.STATE_HIDDEN
            specializationSheet.state = BottomSheetBehavior.STATE_HIDDEN
            timeSelectionSheet.state = BottomSheetBehavior.STATE_HIDDEN
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
            dateSelectionSheet.state = BottomSheetBehavior.STATE_HIDDEN
            timeSelectionSheet.state = BottomSheetBehavior.STATE_HIDDEN
            binding.selectSpeciality.specialityLayout.visibility = View.VISIBLE
        })

        binding.selectSpeciality.specialityLayout.visibility = View.GONE
        dateSelectionSheet.state = BottomSheetBehavior.STATE_EXPANDED
        selectPatientSheet.state = BottomSheetBehavior.STATE_EXPANDED
        selectPatientSheet.isDraggable = false
        specializationSheet.isDraggable = false
        binding.dateAndTimeSheetLayout.visibility = View.VISIBLE
        selectPatientSheet.isHideable = false

        showDatePickingDailog()
    }

    private fun showDatePickingDailog() {
        binding.datePicker.chooseDate.setOnClickListener(View.OnClickListener {
            val datePicker = MaterialSpinnerDatePicker(this)
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

                    }
                })

            datePicker.show()
        })
    }

    private fun setDateSelected() {
        /* binding.dateAndTimeSheetLayout.selectedCard.visibility= View.VISIBLE
         binding.dateAndTimeSheetLayout.datePickerLayout.visibility= View.GONE*/
        timeSelectionSheet.state = BottomSheetBehavior.STATE_EXPANDED
        selectPatientSheet.state = BottomSheetBehavior.STATE_EXPANDED
        binding.dateAndTimeSheetLayout.visibility = View.VISIBLE
        dateSelectionSheet.isDraggable = false
        timeSelectionSheet.state = BottomSheetBehavior.STATE_EXPANDED
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

}