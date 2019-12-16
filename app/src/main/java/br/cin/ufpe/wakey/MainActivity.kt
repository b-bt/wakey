package br.cin.ufpe.wakey

import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofencingRequest: GeofencingRequest
    private var geofenceList: MutableList<Geofence> = mutableListOf<Geofence>()
    private var wakeyList: MutableList<Wakey> = mutableListOf<Wakey>()
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermissions(this)
        createNotificationChannel(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        wakeyList = restoreWakeys(this)
//        wakeyList.add(Wakey(-8.049917, -34.905129, 150.toFloat(), "Fulano's House"))
        geofenceList = wakeyListToGeofenceList(wakeyList)
        geofencingClient = LocationServices.getGeofencingClient(this)
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

        // Config map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setPadding(0, 0, 0, 300)
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        // Move the camera to the users location and zoom in
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val location = LatLng(location.latitude, location.longitude)
                val newPosition = CameraPosition.Builder()
                    .target(location)
                    .zoom(16f)
                    .build()
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPosition))
            }
        }
    }

    override fun onStop() {
        super.onStop()
        backupWakeys(this, wakeyList)
    }
}
