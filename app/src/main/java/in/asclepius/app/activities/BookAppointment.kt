package `in`.asclepius.app.activities

import `in`.asclepius.app.adapters.MemberAdapter
import `in`.asclepius.app.databinding.ActivityBookAppointmentBinding
import `in`.asclepius.app.models.AppUser
import `in`.asclepius.app.others.Constants
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class BookAppointment : AppCompatActivity() {

    lateinit var binding: ActivityBookAppointmentBinding
    lateinit var mContext: Context
    lateinit var specializationSheet: BottomSheetBehavior<View>
    lateinit var selectPatientSheet: BottomSheetBehavior<View>
    lateinit var dateSelectionSheet: BottomSheetBehavior<View>
    lateinit var timeSelectionSheet: BottomSheetBehavior<View>
    val database = FirebaseDatabase.getInstance()
    val userMembersReference = database.getReference(Constants.USER_MEMBERS_REFERENCE)
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val userMembers = mutableListOf<AppUser>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this

        /* binding.selectPatient.firstPatientCard.setOnClickListener(View.OnClickListener {
            setPatientSelected()
        }) */

        /*  binding.selectSpeciality.firstSpecialityCard.setOnClickListener(View.OnClickListener {
              setSpecialitySelected()
          })*/

        binding.datePicker.dateNext.setOnClickListener(View.OnClickListener {
            setDateSelected()
        })

        setPatientsRV()

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

    }


    private fun setPatientSelected() {
        binding.selectPatient.selectedCard.root.visibility = View.VISIBLE
        //binding.selectPatient.patientsLayout.visibility=View.GONE
        specializationSheet.state = BottomSheetBehavior.STATE_EXPANDED
        binding.specialitySheetLayout.visibility = View.VISIBLE
        selectPatientSheet.isHideable = false
    }

    private fun setSpecialitySelected() {
        binding.selectSpeciality.selectedCard.root.visibility = View.VISIBLE
        binding.selectSpeciality.specialityLayout.visibility = View.GONE
        dateSelectionSheet.state = BottomSheetBehavior.STATE_EXPANDED
        selectPatientSheet.state = BottomSheetBehavior.STATE_EXPANDED
        selectPatientSheet.isDraggable = false
        specializationSheet.isDraggable = false
        binding.dateAndTimeSheetLayout.visibility = View.VISIBLE
        selectPatientSheet.isHideable = false
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
        val memberAdapter = MemberAdapter(userMembers, this)
        binding.selectPatient.patientsRV.layoutManager = LinearLayoutManager(this)
        binding.selectPatient.patientsRV.adapter = memberAdapter
    }


}