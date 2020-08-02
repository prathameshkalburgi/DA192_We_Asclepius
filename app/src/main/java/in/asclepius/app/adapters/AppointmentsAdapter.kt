package `in`.asclepius.app.adapters

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.AppointmentActions
import `in`.asclepius.app.databinding.AppointmentCardBinding
import `in`.asclepius.app.models.ModelAppointment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AppointmentsAdapter(
    private val appointments: ArrayList<ModelAppointment>,
    private val context: Context,
    private val actions: AppointmentActions
) :

    RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AppointmentCardBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.appointment_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments.get(position)
        holder.binding.patientDetails.text = appointments.get(position).dataForUser

        holder.binding.cancelAppointment.setOnClickListener(View.OnClickListener {
            actions.onCancel(appointments[position])
        })

        holder.binding.rescheduleAppointment.setOnClickListener(View.OnClickListener {
            actions.onReschedule(appointments[position])
        })

        if (appointment.status == "Cancelled") {
            holder.binding.subActions.visibility = View.GONE
            holder.binding.cancelledCard.visibility = View.VISIBLE
            holder.binding.activeCard.visibility = View.GONE
        } else {
            holder.binding.cancelledCard.visibility = View.GONE
            holder.binding.activeCard.visibility = View.VISIBLE
        }

        if (appointment.isOnlineConsultation) {
            holder.binding.statusText.text = "Request Sent To Doctor"
        }


    }

}