package br.cin.ufpe.wakey

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_wakey_detail.*

class WakeyDetailActivity : AppCompatActivity(),
    OnMapReadyCallback, SeekBar.OnSeekBarChangeListener, TextWatcher {

    private lateinit var mMap: GoogleMap
    private lateinit var markerCenter: Marker
    private lateinit var radiusCircle: Circle
    private var selectedRadius: Double = 100.0
    private var selectedWakey: Wakey? = null

    companion object {
        const val EXTRA_WAKEY_ID: String = "wakey_id"

        fun newIntent(context: Context, wakeyId: String? = null): Intent {
            val detailIntent = Intent(context, WakeyDetailActivity::class.java)
            detailIntent.putExtra(EXTRA_WAKEY_ID, wakeyId)

            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wakey_detail)

        // Configure view according to if editing or creating a wakey
        val wakeyId = intent.extras?.getString(EXTRA_WAKEY_ID)
        selectedWakey = WakeyManager.getInstance(this).getWakeyById(wakeyId)

        if (selectedWakey != null) {
            setTitle(R.string.title_edit_activity)
            nameEditText.setText(selectedWakey!!.name)
            saveBtn.text = resources.getString(R.string.button_save)
            selectedRadius = selectedWakey!!.radius.toDouble()
        } else {
            setTitle(R.string.title_create_activity)
            deleteBtn.visibility = View.GONE
            saveBtn.text = resources.getString(R.string.button_create)
            selectedRadius = (resources.getInteger(R.integer.default_radius) + 1) * 100.0
        }
        radiusTextView.text = "${selectedRadius.toInt()}m"
        nameEditText.addTextChangedListener(this)
        updateSaveBtnState()

        // Config map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Config SeekBar
        radiusSeekBar.progress = ((selectedRadius / 100.0) - 1).toInt()
        radiusSeekBar.setOnSeekBarChangeListener(this)

        // Config buttons
        saveBtn.setOnClickListener {
            val location = mMap.cameraPosition.target
            val name = nameEditText.text.toString()

            if (selectedWakey != null) {
                // TODO: "Save the Wakey"
            } else {
                WakeyManager.getInstance(this).createWakie(
                    location.latitude,
                    location.longitude,
                    selectedRadius,
                    name
                    )
            }
            Toast.makeText(this,  resources.getText(R.string.success_save), Toast.LENGTH_SHORT).show()
            this.finish()
        }
        deleteBtn.setOnClickListener{
            val confirmDialog = AlertDialog.Builder(this)
            confirmDialog.setTitle(resources.getText(R.string.dialog_title_delete))
            confirmDialog.setMessage(resources.getText(R.string.dialog_desc_delete))
            confirmDialog.setNegativeButton(resources.getText(R.string.button_cancel), null)
            confirmDialog.setPositiveButton(resources.getText(R.string.button_delete)) {_, _ ->
                Toast.makeText(this, "Deletado", Toast.LENGTH_SHORT).show()
                this.finish()
            }
            confirmDialog.create().show()
        }
    }

    fun updateSaveBtnState() {
        val enabled = !this.nameEditText.text.isBlank()

        if (enabled == saveBtn.isEnabled) {
            return
        }

        if (enabled) {
            saveBtn.isEnabled = true
            saveBtn.isClickable = true
            saveBtn.setTextColor(ContextCompat.getColor(this, R.color.buttonText))
            saveBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        } else {
            saveBtn.isEnabled = false
            saveBtn.isClickable = false
            saveBtn.setTextColor(Color.WHITE)
            saveBtn.setBackgroundColor(Color.LTGRAY)
        }
    }

    // MARK: Map Stuff
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Move the camera to a certain location and zoom in
        var location = LatLng(-8.0557, -34.9516)
        if (selectedWakey != null) {
            location = LatLng(selectedWakey!!.latitude, selectedWakey!!.longitude)
        }
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

    // MARK: SeekBar Stuff
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

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // Not implemented
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // Not implemented
    }

    override fun afterTextChanged(p0: Editable?) {
        updateSaveBtnState()
    }

}
