package br.cin.ufpe.wakey

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import kotlin.collections.MutableList

const val PREFS_FILENAME = "br.cin.ufpe.wakey.prefs"
const val NUMBER_OF_WAKEYS = "number_of_wakeys"
const val WAKEY_LAT = "wakey_lat_"
const val WAKEY_LNG = "wakey_lng_"
const val WAKEY_RADIUS = "wakey_radius_"
const val WAKEY_NAME = "wakey_name_"

fun backupWakeys(activity: Activity, listOfWakeys: MutableList<Wakey>){

    val prefs = activity.getSharedPreferences(PREFS_FILENAME, 0)
    val editor = prefs.edit()
    val numberOfWakeys = listOfWakeys.size
    if (numberOfWakeys > 0){
        editor.putInt(NUMBER_OF_WAKEYS, numberOfWakeys)
        for (index in 0 until listOfWakeys.size){
            val wakey = listOfWakeys.get(index)
            editor.putFloat("""$WAKEY_LAT$index""", wakey.latitude.toFloat())
            editor.putFloat("""$WAKEY_LNG$index""", wakey.longitude.toFloat())
            editor.putFloat("""$WAKEY_RADIUS$index""", wakey.radius)
            editor.putString("""$WAKEY_NAME$index""", wakey.name)
        }
    }
    editor.apply()
}

fun restoreWakeys(activity: Activity): MutableList<Wakey> {
    val prefs = activity.getSharedPreferences(PREFS_FILENAME, 0)
    val numberOfWakeys = prefs.getInt(NUMBER_OF_WAKEYS, 0)
    val listOfWakeys: MutableList<Wakey> = mutableListOf<Wakey>()

    if (numberOfWakeys > 0){
        for (index in 0 until numberOfWakeys){
            val latitude = prefs.getFloat("""$WAKEY_LAT$index""", 0.toFloat()).toDouble()
            val longitude = prefs.getFloat("""$WAKEY_LNG$index""", 0.toFloat()).toDouble()
            val radius = prefs.getFloat("""$WAKEY_RADIUS$index""", 0.toFloat())
            val name = prefs.getString("""$WAKEY_NAME$index""", null)

            if ((radius == 0.toFloat()) || (name == null)){
                continue
            } else {
                val wakey = Wakey(latitude, longitude, radius, name)
                listOfWakeys.add(wakey)
            }
        }
    }

    return listOfWakeys
}

fun wakeyListToGeofenceList(wakeyList: MutableList<Wakey>): MutableList<Geofence>{
    val numberOfWakeys = wakeyList.size
    val geofenceList: MutableList<Geofence> = mutableListOf<Geofence>()
    if (numberOfWakeys > 0){
        for (wakey in wakeyList){
            geofenceList.add(wakey.buildGeofence())
        }
    }
    return geofenceList
}

fun getGeofencingRequest(geofenceList: MutableList<Geofence>): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
        setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        addGeofences(geofenceList)
    }.build()
}

internal fun setupPermissions(activity: Activity) {
    val locationPermission = ContextCompat.checkSelfPermission(activity,
        Manifest.permission.ACCESS_FINE_LOCATION)
    val vibratePermission = ContextCompat.checkSelfPermission(activity,
        Manifest.permission.ACCESS_FINE_LOCATION)

    if (locationPermission != PackageManager.PERMISSION_GRANTED || vibratePermission != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(activity)
    }
}

private fun requestPermissions(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.VIBRATE),
        194
    )
}