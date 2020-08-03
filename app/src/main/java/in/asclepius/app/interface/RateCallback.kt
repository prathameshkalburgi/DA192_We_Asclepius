package `in`.asclepius.app.`interface`

import `in`.asclepius.app.models.Doctors
import `in`.asclepius.app.models.ModelRating

interface RateCallback {
    fun rateDoctor(doctor: Doctors, rating: ModelRating)
}