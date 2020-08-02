package `in`.asclepius.app.adapters

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.PatientSelectedCallback
import `in`.asclepius.app.databinding.MemberCardBinding
import `in`.asclepius.app.models.AppUser
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(
    private val members: List<AppUser>,
    private val context: Context,
    private val callback: PatientSelectedCallback
) :
    RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    public var selectedPatient = AppUser()
        set(value) {
            field = value
        }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = MemberCardBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemberAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.member_card, parent, false)
        return MemberAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: MemberAdapter.ViewHolder, position: Int) {
        val member = members.get(position)
        holder.binding.memberName.text = member.fullName
        holder.binding.realtionText.text =
            context.getString(R.string.yours) + " " + member.relationType
        holder.binding.memberAge.text = context.getString(R.string.onlyAge) + " " + member.age
        if (member.gender == "Male") {
            holder.binding.genderIcon.setImageDrawable(context.getDrawable(R.drawable.male_icon))
        } else {
            holder.binding.genderIcon.setImageDrawable(context.getDrawable(R.drawable.female_icon))
        }

        if (member.aadharNumber == selectedPatient.aadharNumber) {
            holder.binding.root.strokeWidth = 2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.binding.root.strokeColor = context.getColor(R.color.colorAccent)
            }
        } else {
            holder.binding.root.strokeWidth = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.binding.root.strokeColor = context.getColor(R.color.colorAccent)
            }
        }

        if (member.relationType == null) {
            holder.binding.realtionText.text = "Your self"
            holder.binding.memberAgeCard.visibility = View.GONE
        }

        if (member.age <= 0) {
            holder.binding.memberAgeCard.visibility = View.GONE
        }


        holder.binding.root.setOnClickListener(View.OnClickListener {
            callback.onPatientSelected(member)
        })
    }
}