package `in`.asclepius.app.`interface`

import `in`.asclepius.app.models.AppUser

interface PatientSelectedCallback {
    fun onPatientSelected(patient: AppUser)
}