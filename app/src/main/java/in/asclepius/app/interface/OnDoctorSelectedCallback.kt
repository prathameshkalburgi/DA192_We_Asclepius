package `in`.asclepius.app.`interface`

import `in`.asclepius.app.models.Doctors

interface OnDoctorSelectedCallback {
    fun onDoctorSelected(doctor: Doctors)
    fun onRateDoctor(doctor: Doctors)
}