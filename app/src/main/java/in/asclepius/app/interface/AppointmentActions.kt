package `in`.asclepius.app.`interface`

import `in`.asclepius.app.models.ModelAppointment

interface AppointmentActions {
    public fun onCancel(appointment: ModelAppointment)
    public fun onReschedule(appointment: ModelAppointment)
}