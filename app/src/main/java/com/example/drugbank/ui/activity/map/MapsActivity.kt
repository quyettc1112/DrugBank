package com.example.drugbank.ui.activity.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ConfirmDialog
import com.example.drugbank.common.constant.Constant

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.drugbank.databinding.ActivityMapsBinding
import com.example.drugbank.ui.activity.main.MainActivity
import com.example.drugbank.ui.activity.main.MainViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.checkerframework.checker.units.qual.Current
import java.net.URL

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment
    private lateinit var mapsViewModels: MapsViewModels
  //  private lateinit var backUpCurrentLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapsViewModels = ViewModelProvider(this)[MapsViewModels::class.java]

        Places.initialize(applicationContext, getString(R.string.API_KEy))
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.customToolbar.onStartIconClick = {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constant.IS_BACK_FROM_MAP, true)
            startActivity(intent)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)






    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (isLocationPermissionGranted()) {
            showCurrentLocation()
            findPlace()
           // placeNearBy()
            binding.hospital.setOnClickListener {
                mMap.clear()
                binding.layoutATM.setBackgroundResource(0)
                binding.layoutNhaThuoc.setBackgroundResource(0)
                binding.layoutBenhVienc.setBackgroundResource(R.drawable.background_text_button_iconclick)
                mapsViewModels.nearBy.value = "hospital"
                showCurrentLocation()
            }

            binding.ATM.setOnClickListener {
                mMap.clear()
                binding.layoutBenhVienc.setBackgroundResource(0)
                binding.layoutNhaThuoc.setBackgroundResource(0)
                binding.layoutATM.setBackgroundResource(R.drawable.background_text_button_iconclick)
                mapsViewModels.nearBy.value = "atm"
                showCurrentLocation()
            }
            binding.medicine.setOnClickListener {
                mMap.clear()
                binding.layoutATM.setBackgroundResource(0)
                binding.layoutBenhVienc.setBackgroundResource(0)
                binding.layoutNhaThuoc.setBackgroundResource(R.drawable.background_text_button_iconclick)
                mapsViewModels.nearBy.value = "drugstore"
                showCurrentLocation()
            }

        } else {
            requestLocationPermission()
        }
    }

    private fun findPlace() {
        autocompleteSupportFragment =
            supportFragmentManager.findFragmentById(R.id.autoComplete_fragment) as AutocompleteSupportFragment
        autocompleteSupportFragment.setPlaceFields(listOf(Field.ID, Field.ADDRESS, Field.LAT_LNG))
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Toast.makeText(this@MapsActivity, "Some Error", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        place.latLng,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
                mMap.addMarker(
                    MarkerOptions()
                        .position(place.latLng)
                        .title(place.name)
                )

            }
        })
    }
    private fun placeNearBy(currentLocation: LatLng, mMap: GoogleMap) {
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + "${currentLocation.latitude}" + "," + "${currentLocation.longitude}" +
                "&radius=5000" +
              //  "&types=pharmarcy" +
         "&types=" + "${mapsViewModels.nearBy.value}" +
                "&sensor=true" +
                "&key=" + "${resources.getString(R.string.API_KEy)}"
        val placeTask = PlaceTask(mMap)
        placeTask.execute(url)

        // PlaceTask class should handle network calls and update UI on main thread


    }

    class PlaceTask(private val mMap: GoogleMap) : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String {
            val url = params[0]
            val response = URL(url).readText()
            return response
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            val jsonObject = JsonParser.parseString(result).asJsonObject
            val resultsArray = jsonObject.getAsJsonArray("results")
            for (resultElement in resultsArray) {
                val resultObject = resultElement.asJsonObject
                extractLocationData(resultObject)
            }
        }

        private fun extractLocationData(resultObject: JsonObject?) {
            val geometryObject = resultObject?.getAsJsonObject("geometry")
            val locationObject = geometryObject?.getAsJsonObject("location")
            val lat = locationObject?.get("lat")?.asDouble
            val lng = locationObject?.get("lng")?.asDouble
            val name = resultObject?.get("name")?.asString
            // Sử dụng lat và lng cho các thao tác tiếp theo, ví dụ:
            if (lat != null && lng != null) {
                val locationLatLng = LatLng(lat, lng)
                mMap.addMarker(
                    MarkerOptions()
                        .position(locationLatLng)
                        .title(name)
                        .snippet(name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )
            }
        }
    }





    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCurrentLocation()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                val intentToSeeting = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val confirmDialog = ConfirmDialog(
                    this,
                    object  : ConfirmDialog.ConfirmCallback{
                        override fun negativeAction() {
                            startActivity(intent)
                        }
                        override fun positiveAction() {

                            val uri = Uri.fromParts("package", packageName, null)
                            intentToSeeting.data = uri
                            startActivity(intentToSeeting)
                        }
                    },
                    title = "Cannot get location",
                    message = "Grant permission to access location",
                    positiveButtonTitle = "Yes",
                    negativeButtonTitle = "No"
                )
                confirmDialog.show()

            }
        }
    }

    private fun showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
               val currentLatLng = LatLng(location.latitude, location.longitude)
                placeNearBy(currentLatLng, mMap)


                mMap.addMarker(
                    MarkerOptions()
                        .position(currentLatLng)
                        .title("Current Location")
                )
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLatLng,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
            }
        }



    }
    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val DEFAULT_ZOOM = 15
    }
}