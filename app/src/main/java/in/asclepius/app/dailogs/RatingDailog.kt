package `in`.asclepius.app.dailogs

import `in`.asclepius.app.R
import `in`.asclepius.app.`interface`.RateCallback
import `in`.asclepius.app.databinding.RatingDailougeBinding
import `in`.asclepius.app.models.Doctors
import `in`.asclepius.app.models.ModelRating
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View

class RatingDailog(
    context: Context,
    private val doctor: Doctors,
    private val callback: RateCallback
) : Dialog(context, R.style.Theme_Transparent) {

    lateinit var binding: RatingDailougeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RatingDailougeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submit.setOnClickListener(View.OnClickListener {
            if (binding.descriptionText.editText?.text.toString()
                    .isBlank() || binding.ratingView.rating == 0.0f
            ) {
                with(binding.descriptionText)
                {
                    isErrorEnabled = true
                    error = "Please Select Rating And Please Enter your experience"
                }
            } else {

                callback.rateDoctor(
                    doctor,
                    ModelRating(
                        binding.ratingView.rating.toDouble(),
                        null,
                        null,
                        binding.descriptionText.editText?.text.toString()
                    )
                )


            }
        })
    }
}