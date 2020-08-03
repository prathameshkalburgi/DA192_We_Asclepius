package `in`.asclepius.app.adapters

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.OnDoctorSelectedCallback
import `in`.asclepius.app.databinding.DoctorsCardBinding
import `in`.asclepius.app.databinding.UserRatingCardBinding
import `in`.asclepius.app.models.Doctors
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DoctorsAdapter(
    private val context: Context,
    private val doctors: Array<Doctors>,
    private val callback: OnDoctorSelectedCallback,
    private val isFromSearchDoctors: Boolean
) :
    RecyclerView.Adapter<DoctorsAdapter.ViewHolder>() {

    private var selectedDoctor: Doctors? = null
        set(value) {
            field = value
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DoctorsCardBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.doctors_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return doctors.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = doctors.get(position)
        holder.binding.doctorName.text = doctor.name
        holder.binding.doctorRating.text = "Rating : 4.2"
        holder.binding.experienceText.text = "Experience : 3 Years"
        holder.binding.root.setOnClickListener(View.OnClickListener {
            callback.onDoctorSelected(doctor)
        })

        if (doctor.name == selectedDoctor?.name) {
            with(holder.binding.root)
            {
                strokeColor = context.resources.getColor(R.color.colorAccent)
                strokeWidth = 3
            }
        }

        if (isFromSearchDoctors) {
            holder.binding.speciality.text = doctor.speciality
            holder.binding.speciality.visibility = View.VISIBLE
            if (doctor.rating <= 0) {
                holder.binding.doctorRating.text = "No Ratings Yet"
            }

            holder.binding.locationCard.visibility = View.VISIBLE
            holder.binding.distanceCard.visibility = View.VISIBLE
            holder.binding.distance.text = doctor.distance.toString() + " km "
            holder.binding.locationText.text = doctor.hospital


            if (doctor.ratings != null) {
                for (rating in doctor.ratings) {
                    val inflater =
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val ratingView = inflater.inflate(
                        R.layout.user_rating_card,
                        holder.binding.ratingsContiner,
                        true
                    )
                    val bindedView = UserRatingCardBinding.bind(ratingView)

                    bindedView.ratedBy.text = rating.ratedBy.fullName
                    bindedView.ratingView.rating = rating.rating.toFloat()
                    holder.binding.ratingsContiner.addView(bindedView.root)
                }

                holder.binding.ratingLayout.visibility = View.VISIBLE
            }


        } else {
            holder.binding.locationCard.visibility = View.GONE
            holder.binding.distanceCard.visibility = View.GONE
        }

    }
}