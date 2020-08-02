package `in`.asclepius.app.adapters

import `in`.asclepius.app.R
import `in`.asclepius.app.databinding.DoctorsCardBinding
import `in`.asclepius.app.models.Doctors
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DoctorsAdapter(private val context: Context, private val doctors: Array<Doctors>) :
    RecyclerView.Adapter<DoctorsAdapter.ViewHolder>() {

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

    }
}