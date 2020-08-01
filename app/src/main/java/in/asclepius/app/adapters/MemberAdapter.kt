package `in`.asclepius.app.adapters

import `in`.asclepius.app.R
import `in`.asclepius.app.databinding.MemberCardBinding
import `in`.asclepius.app.models.AppUser
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(
    private val members: List<AppUser>,
    private val context: Context
) :
    RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

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
    }
}