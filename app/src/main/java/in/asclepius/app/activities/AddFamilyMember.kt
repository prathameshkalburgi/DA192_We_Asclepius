package `in`.asclepius.app.activities

import `in`.asclepius.app.R
import `in`.asclepius.app.dailogs.LoadingDialog
import `in`.asclepius.app.databinding.ActivityAddFamilyMemberBinding
import `in`.asclepius.app.models.AppUser
import `in`.asclepius.app.others.Constants
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddFamilyMember : AppCompatActivity() {

    lateinit var binding:ActivityAddFamilyMemberBinding
    lateinit var loadingDailog : LoadingDialog
    lateinit var mContext : Context
    val membersRef= FirebaseDatabase.getInstance().getReference(Constants.USER_MEMBERS_REFERENCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddFamilyMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRelationTypeAdapter()
        binding.toolbarCard.toolbar.title=getString(R.string.addMember)

        binding.addMember.setOnClickListener(View.OnClickListener {
            addMemberToDatabase()
        })

        loadingDailog= LoadingDialog(this)
        mContext=this@AddFamilyMember

    }

    private fun addMemberToDatabase() {

        if(validateFields()) {

            loadingDailog.show()
            loadingDailog.setTitle(getString(R.string.adding_member))

            val rootUser= FirebaseAuth.getInstance().currentUser
            rootUser?.uid?.let {
                val member= AppUser(binding.memberName.editText?.text.toString(),"NA")
                member.aadharNumber=binding.adharId.editText?.text.toString()
                val radioButton=findViewById<RadioButton>(binding.genderGroup.checkedRadioButtonId)
                member.gender= radioButton.text.toString()
                member.age=binding.memberAge.editText?.text.toString().trim().toInt()
                member.relationType=binding.relationType.text.toString()

                membersRef.child(rootUser.uid).child("u${member.aadharNumber}").setValue(member).addOnSuccessListener {
                    Toast.makeText(mContext, getString(R.string.member_added), Toast.LENGTH_LONG).show()
                    loadingDailog.dismiss()
                    onBackPressed()
                }.addOnFailureListener {
                    binding.addMember.visibility= View.VISIBLE
                    binding.addMemberLoading.visibility= View.GONE
                    Toast.makeText(mContext, getString(R.string.something_went_wrong_try_again_later), Toast.LENGTH_LONG).show()
                }

            }

        }

    }

    private fun validateFields(): Boolean {

        binding.memberAge.isErrorEnabled=false
        binding.memberName.isErrorEnabled=false
        binding.selectGender.visibility= View.GONE

        if(binding.memberName.editText?.text.toString().isBlank())
        {
            binding.memberName.error=getString(R.string.please_enter_full_name)
            binding.memberName.isErrorEnabled=true
            return false
        }

        if(binding.adharId.editText?.text.toString().isBlank() || binding.adharId.editText?.text.toString().length!=12) {
            binding.adharId.error=getString(R.string.enter_correct_adhar_number)
            binding.adharId.isErrorEnabled=true
            return false
        }

        if(binding.memberAge.editText?.text.toString().isBlank())
        {
            binding.memberAge.error=getString(R.string.please_enter_valid_age)
            binding.memberAge.isErrorEnabled=true
            return false
        }

        if(binding.genderGroup.checkedRadioButtonId<=0)
        {
            binding.selectGender.visibility= View.VISIBLE
            return false
        }

        if(binding.relationType.text.isBlank())
        {
            binding.realtionBox.error=getString(R.string.please_select_relation_type)
            binding.realtionBox.isErrorEnabled=true
            return false
        }

        return true;
    }

    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

    private fun setRelationTypeAdapter() {
        val relations = Constants.relations.map { it -> it.capitalizeWords() }
        val adapter = ArrayAdapter<Any?>(this, android.R.layout.simple_list_item_1,relations)
        binding.relationType.threshold=2
        binding.relationType.setAdapter(adapter)
        binding.relationType.dropDownHeight=200
    }

}