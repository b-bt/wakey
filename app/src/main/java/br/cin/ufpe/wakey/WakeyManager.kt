package br.cin.ufpe.wakey

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val checkInstance = instance
        if (checkInstance != null) {
            return checkInstance
        }

        return synchronized(this) {
            val checkInstanceAgain = instance
            if (checkInstanceAgain != null) {
                checkInstanceAgain
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}

class WakeyManager private constructor(val context: Context) {
    var wakeyList = mutableListOf<Wakey>()
        private set

    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofencingRequest: GeofencingRequest
    private var geofenceList: MutableList<Geofence> = mutableListOf<Geofence>()
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object : SingletonHolder<WakeyManager, Context>(::WakeyManager)

    init {
        wakeyList = restoreWakeys(context)

        wakeyList = restoreWakeys(context)
//        wakeyList.add(Wakey(-8.049917, -34.905129, 150.toFloat(), "Fulano's House"))
        geofenceList = wakeyListToGeofenceList(wakeyList)
        geofencingClient = LocationServices.getGeofencingClient(context)
        if (!geofenceList.isEmpty()) {
            geofencingRequest = getGeofencingRequest(geofenceList)

            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                addOnSuccessListener {
                    Log.e("MainActivity", "Success!")
                }
                addOnFailureListener {
                    Log.e("MainActivity", """$it""")
                }
            }
        }
    }

}