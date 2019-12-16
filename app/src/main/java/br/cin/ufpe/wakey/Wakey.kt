package br.cin.ufpe.wakey

import com.google.android.gms.location.Geofence
import java.util.*

class Wakey(
    var latitude: Double,
    var longitude: Double,
    var radius: Float,
    var name: String,
    val id: String = UUID.randomUUID().toString())
{

    fun buildGeofence(): Geofence {
        return Geofence.Builder()
            // Set the request ID of the geofence.
            // This is a string to identify this geofence.
            .setRequestId(this.id)

            // Set the circular region of this geofence.
            .setCircularRegion(
                this.latitude,
                this.longitude,
                this.radius
            )

            // Set the expiration duration of the geofence. This geofence gets automatically
            // removed after this period of time.
            .setExpirationDuration(Geofence.NEVER_EXPIRE)

            // Set the transition types of interest. Alerts are only generated for these transition.
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)

            // Create the geofence.
            .build()
    }

}
