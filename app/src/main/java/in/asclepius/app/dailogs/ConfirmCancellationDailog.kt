package `in`.asclepius.app.dailogs

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.AppointmentActions
import `in`.asclepius.app.databinding.ConfirmLayoutBinding
import `in`.asclepius.app.models.ModelAppointment
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View

class ConfirmCancellationDailog(
    context: Context,
    private val action: AppointmentActions,
    private val appointment: ModelAppointment
) : Dialog(context, R.style.Theme_Transparent) {


    lateinit var binding: ConfirmLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConfirmLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.yes.setOnClickListener(View.OnClickListener {
            action.onCancel(appointment)
            dismiss()
        })

        binding.no.setOnClickListener(View.OnClickListener {
            dismiss()
        })

    }
}