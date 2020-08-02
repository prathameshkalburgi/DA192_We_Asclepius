package `in`.asclepius.app.adapters

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.OnDepartmentSelected
import `in`.asclepius.app.databinding.DepartmentCardBinding
import `in`.asclepius.app.databinding.SpecialityCardBinding
import `in`.asclepius.app.models.DepartmentModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DepartmentsAdapter(
    private val department: DepartmentModel,
    private val context: Context,
    private val onDepartmentSelected: OnDepartmentSelected
) :
    RecyclerView.Adapter<DepartmentsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DepartmentsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.department_card, parent, false)
        return DepartmentsAdapter.ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DepartmentCardBinding.bind(itemView)
    }


    override fun getItemCount(): Int {
        return department.opdDepartments.size
    }

    override fun onBindViewHolder(holder: DepartmentsAdapter.ViewHolder, position: Int) {
        val opdModel = department.getOPDDepartmentAt(position)
        holder.binding.departmentTitle.text = opdModel.departmentName
        holder.binding.specialityTotalText.text =
            context.getString(R.string.specialities) + " : " + opdModel.availabeSpeciality.size.toString()
        holder.binding.specialityContainer.removeAllViews()

        holder.binding.root.setOnClickListener(View.OnClickListener {
            holder.binding.chooseSpecialitySubLayout.visibility = View.VISIBLE
            holder.binding.specialityContainer.removeAllViews()
            for (speciality in opdModel.availabeSpeciality) {
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(
                    R.layout.speciality_card,
                    holder.binding.specialityContainer,
                    false
                )
                val binding = SpecialityCardBinding.bind(view)
                binding.speciality.text = speciality.specialityName
                binding.root.setOnClickListener(View.OnClickListener {
                    val tempOPD = opdModel
                    tempOPD.availabeSpeciality[0] = speciality
                    onDepartmentSelected.onOPDselected(tempOPD)

                })
                holder.binding.specialityContainer.addView(binding.root)
            }
        })
    }

}