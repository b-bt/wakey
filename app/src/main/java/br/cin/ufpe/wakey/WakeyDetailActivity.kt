package br.cin.ufpe.wakey

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_wakey_detail.*

class WakeyDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var markerCenter: Marker
    private lateinit var radiusCircle: Circle

    companion object {
        const val EXTRA_WAKEY: String = "wakey"

        fun newIntent(context: Context, wakey: String? = null): Intent {
            val detailIntent = Intent(context, WakeyDetailActivity::class.java)

            detailIntent.putExtra(EXTRA_WAKEY, wakey)

            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wakey_detail)

        // Check if editing or creating a wakey
        val wakey = intent.extras?.getString(EXTRA_WAKEY)
        if (wakey != null) {
            setTitle(R.string.title_edit_activity)
            nameEditText.setText(wakey)
            saveBtn.text = resources.getString(R.string.button_save)
        } else {
            setTitle(R.string.title_create_activity)
            deleteBtn.visibility = View.GONE
            saveBtn.text = resources.getString(R.string.button_create)
        }

        // Config map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Move the camera to a certain location and zoom in
        val location = LatLng(-8.0557, -34.9516)
        val newPosition = CameraPosition.Builder()
            .target(location)
            .zoom(15f)
            .build()

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(newPosition))

        // Add radius circle around marker
        val circleOptions = CircleOptions()
            .center(mMap.cameraPosition.target)
            .radius(100.0)
            .strokeColor(ContextCompat.getColor(this, R.color.radiusCircleBorder))
            .fillColor(ContextCompat.getColor(this, R.color.radiusCircleFill))
        radiusCircle = mMap.addCircle(circleOptions)

        // Handle marker position
        val markerOptions = MarkerOptions()
        markerOptions.position(mMap.cameraPosition.target)
        markerCenter = mMap.addMarker(markerOptions)
        mMap.setOnCameraMoveListener {
            val position = mMap.cameraPosition.target
            markerCenter.position = position
            radiusCircle.center = position
        }
    }
}
