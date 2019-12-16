package br.cin.ufpe.wakey

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
const val WAKEY_ID = "wakey_id_"
const val CHANNEL_ID = "br.cin.ufpe.wakey-WAKEY"

fun backupWakeys(context: Context, listOfWakeys: MutableList<Wakey>){

    val prefs = context.getSharedPreferences(PREFS_FILENAME, 0)
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
            editor.putString("""$WAKEY_ID$index""", wakey.id)
        }
    }
    editor.apply()
}

fun restoreWakeys(context: Context): MutableList<Wakey> {
    val prefs = context.getSharedPreferences(PREFS_FILENAME, 0)
    val numberOfWakeys = prefs.getInt(NUMBER_OF_WAKEYS, 0)
    val listOfWakeys: MutableList<Wakey> = mutableListOf<Wakey>()

    if (numberOfWakeys > 0){
        for (index in 0 until numberOfWakeys){
            val latitude = prefs.getFloat("""$WAKEY_LAT$index""", 0.toFloat()).toDouble()
            val longitude = prefs.getFloat("""$WAKEY_LNG$index""", 0.toFloat()).toDouble()
            val radius = prefs.getFloat("""$WAKEY_RADIUS$index""", 0.toFloat())
            val name = prefs.getString("""$WAKEY_NAME$index""", null)
            val id = prefs.getString("""$WAKEY_ID$index""", null)

            if ((radius == 0.toFloat()) || (name == null) || id === null){
                continue
            } else {
                val wakey = Wakey(latitude, longitude, radius, name, id)
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

private fun addNewGeofence(wakey: Wakey, geofenceList: MutableList<Geofence>): MutableList<Geofence> {
    val geofence = wakey.buildGeofence()
    geofenceList.add(geofence)
    return geofenceList
}

fun getGeofenceTransitionDetails(listOfGeofences: List<Geofence>): String{
//    var message = "Wakey-wakey! You've entered "
//    var addendum = ""
//    for (geofence in listOfGeofences){
//        val name = geofence.requestId
//        val messageAdd = """"$addendum$name" region"""
//        message += messageAdd
//        if (listOfGeofences.size > 1 && addendum == ""){
//           addendum = ", and "
//        }
//    }
//    message += "."
    val name = listOfGeofences.get(0).requestId
    val message = """You've entered "$name" region."""
    return message
}

fun sendNotification(context: Context, message: String){

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Wakey-wakey!")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(1994, builder.build())

}

fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "WAKEY"
        val descriptionText = "Wakey notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = descriptionText

        // Register the channel with the system

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}