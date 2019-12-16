package br.cin.ufpe.wakey

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_wakey_detail.*

class WakeyDetailActivity : AppCompatActivity(), OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    private lateinit var mMap: GoogleMap
    private lateinit var markerCenter: Marker
    private lateinit var radiusCircle: Circle
    private var selectedRadius: Double = 100.0

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

        // Configure view according to if editing or creating a wakey
        val wakey = intent.extras?.getString(EXTRA_WAKEY)
        if (wakey != null) {
            setTitle(R.string.title_edit_activity)
            nameEditText.setText(wakey)
            saveBtn.text = resources.getString(R.string.button_save)
            selectedRadius = 700.0
        } else {
            setTitle(R.string.title_create_activity)
            deleteBtn.visibility = View.GONE
            saveBtn.text = resources.getString(R.string.button_create)
            selectedRadius = (resources.getInteger(R.integer.default_radius) + 1) * 100.0
        }
        radiusTextView.text = "${selectedRadius.toInt()}m"

        // Config map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Config SeekBar
        radiusSeekBar.progress = ((selectedRadius / 100.0) - 1).toInt()
        radiusSeekBar.setOnSeekBarChangeListener(this)
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
            .radius(selectedRadius)
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

    override fun onStartTrackingTouch(p0: SeekBar?) {
        // Not implemented
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        // Not implemented
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        var newRadius = (progress + 1) * 100.0
        selectedRadius = newRadius
        radiusCircle.radius = newRadius
        radiusTextView.text = "${newRadius.toInt()}m"
    }
}
