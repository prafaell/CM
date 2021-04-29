package com.example.cm

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.cm.API.EndPoints
import com.example.cm.API.Problema
import com.example.cm.API.ServiceBuilder
import com.example.cm.API.User
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var problemas: List<Problema>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallBack: LocationCallback

    //added to implement location periodic updates
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        //location callback
        locationCallBack = object: LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                if (p0 != null) {
                    lastLocation = p0.lastLocation
                }
                var loc = LatLng(lastLocation.latitude,lastLocation.longitude)
                if (ActivityCompat.checkSelfPermission(
                        this@MapsActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@MapsActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mMap.isMyLocationEnabled = true
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12f))

                with(sharedPref.edit()) {
                    putString(com.example.cm.R.string.lat.toString(),lastLocation.latitude.toString())
                    putString(com.example.cm.R.string.lon.toString(),lastLocation.longitude.toString())
                    commit()
                }
            }
        }

        createLocationRequest()


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var position: LatLng


        val userid:Int? = sharedPref.getInt(R.string.userid.toString(), 1);

        call.enqueue(object : Callback<List<Problema>> {
            override fun onResponse(call: Call<List<Problema>>, response: Response<List<Problema>>) {
                if (response.isSuccessful){
                    problemas = response.body()!!
                    for (problema in problemas){
                        position = LatLng(problema.lat.toString().toDouble(), problema.lon.toString().toDouble())
                        if(problema.utilizador_id == userid){
                            mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setTag(problema.id.toString())
                        }else{
                            mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)).setTag(problema.id.toString())
                        }

                    }

                }
            }
            override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })


        btn100.setOnClickListener{
            mMap.clear()
            val call = request.getReports()
            call.enqueue(object : Callback<List<Problema>> {
                override fun onResponse(call: Call<List<Problema>>, response:Response<List<Problema>>){
                    if (response.isSuccessful){
                        problemas = response.body()!!
                        for (problema in problemas){
                            position = LatLng(problema.lat.toString().toDouble(), problema.lon.toString().toDouble())
                            val dist =  calcularDist(problema.lat.toString().toDouble(),problema.lon.toString().toDouble(),
                                            sharedPref.getString(R.string.lat.toString(),"lat")!!.toDouble() ,sharedPref.getString(R.string.lon.toString(), "lon")!!.toDouble() )

                          if(dist <= 100){
                              if(problema.utilizador_id == userid){
                                  mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)
                                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setTag(problema.id.toString())
                              }else{
                                  mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)).setTag(problema.id.toString())
                              }
                          }
                        }

                    }
                }
                override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        btn500.setOnClickListener{
            mMap.clear()
            val call = request.getReports()
            call.enqueue(object : Callback<List<Problema>> {
                override fun onResponse(call: Call<List<Problema>>, response:Response<List<Problema>>){
                    if (response.isSuccessful){
                        problemas = response.body()!!
                        for (problema in problemas){
                            position = LatLng(problema.lat.toString().toDouble(), problema.lon.toString().toDouble())
                            val dist =  calcularDist(problema.lat.toString().toDouble(),problema.lon.toString().toDouble(),
                                    sharedPref.getString(R.string.lat.toString(),"lat")!!.toDouble() ,sharedPref.getString(R.string.lon.toString(), "lon")!!.toDouble() )

                            if(dist <= 500){
                                if(problema.utilizador_id == userid){
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setTag(problema.id.toString())
                                }else{
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)).setTag(problema.id.toString())
                                }
                            }
                        }

                    }
                }
                override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        btn1000.setOnClickListener{
            mMap.clear()
            val call = request.getReports()
            call.enqueue(object : Callback<List<Problema>> {
                override fun onResponse(call: Call<List<Problema>>, response:Response<List<Problema>>){
                    if (response.isSuccessful){
                        problemas = response.body()!!
                        for (problema in problemas){
                            position = LatLng(problema.lat.toString().toDouble(), problema.lon.toString().toDouble())
                            val dist =  calcularDist(problema.lat.toString().toDouble(),problema.lon.toString().toDouble(),
                                    sharedPref.getString(R.string.lat.toString(),"lat")!!.toDouble() ,sharedPref.getString(R.string.lon.toString(), "lon")!!.toDouble() )

                            if(dist <= 1000){
                                if(problema.utilizador_id == userid){
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setTag(problema.id.toString())
                                }else{
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)).setTag(problema.id.toString())
                                }
                            }
                        }

                    }
                }
                override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        btnall.setOnClickListener {
            mMap.clear()
            val call = request.getReports()
            call.enqueue(object : Callback<List<Problema>> {
                override fun onResponse(call: Call<List<Problema>>, response:Response<List<Problema>>){
                    if (response.isSuccessful){
                        problemas = response.body()!!
                        for (problema in problemas){
                            position = LatLng(problema.lat.toString().toDouble(), problema.lon.toString().toDouble())
                                if(problema.utilizador_id == userid){
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setTag(problema.id.toString())
                                }else{
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)).setTag(problema.id.toString())
                                }
                        }

                    }
                }
                override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will onlya be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private fun createLocationRequest(){
        locationRequest = LocationRequest()

        locationRequest.interval = 1000
        locationRequest.priority =  LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause(){
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallBack)
    }

    public override fun onResume(){
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallBack,null)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnInfoWindowClickListener(this)

    }

    override fun onInfoWindowClick(marker: Marker){
        val intent = Intent(this, OcorrAlterar::class.java).apply {
            putExtra(EXTRA_REPLY_ID, marker.tag.toString())
        }
        startActivity(intent)
    }

    companion object {
        // add to implement last known location
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        //added to implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2
        const val EXTRA_REPLY_ID = "ID"
    }


    fun calcularDist(lat1:Double,lng1:Double, lat2:Double, lng2:Double): Float{
        var results = FloatArray(1)
        Location.distanceBetween(lat1,lng1,lat2,lng2,results)

        return results[0];
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menuobras->{
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val sharedPref: SharedPreferences = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)

                val userid:Int? = sharedPref.getInt(R.string.userid.toString(), 1);

                mMap.clear()
                val call = request.getProblemasPorTipo("Obras")
                call.enqueue(object : Callback<List<Problema>> {
                    override fun onResponse(call: Call<List<Problema>>, response:Response<List<Problema>>){
                        if (response.isSuccessful){
                            problemas = response.body()!!
                            for (problema in problemas){
                                val position = LatLng(problema.lat.toString().toDouble(), problema.lon.toString().toDouble())
                                if(problema.utilizador_id == userid){
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setTag(problema.id.toString())
                                }else{
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)).setTag(problema.id.toString())
                                }
                            }

                        }
                    }
                    override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
                    }
                })

                true
            }
            R.id.menuacidente->{
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val sharedPref: SharedPreferences = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)

                val userid:Int? = sharedPref.getInt(R.string.userid.toString(), 1);

                mMap.clear()
                val call = request.getProblemasPorTipo("Acidente")
                call.enqueue(object : Callback<List<Problema>> {
                    override fun onResponse(call: Call<List<Problema>>, response:Response<List<Problema>>){
                        if (response.isSuccessful){
                            problemas = response.body()!!
                            for (problema in problemas){
                                val position = LatLng(problema.lat.toString().toDouble(), problema.lon.toString().toDouble())
                                if(problema.utilizador_id == userid){
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setTag(problema.id.toString())
                                }else{
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)).setTag(problema.id.toString())
                                }
                            }

                        }
                    }
                    override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
                    }
                })

                true
            }
            R.id.menuperigo->{
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val sharedPref: SharedPreferences = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)

                val userid:Int? = sharedPref.getInt(R.string.userid.toString(), 1);

                mMap.clear()
                val call = request.getProblemasPorTipo("Perigo")
                call.enqueue(object : Callback<List<Problema>> {
                    override fun onResponse(call: Call<List<Problema>>, response:Response<List<Problema>>){
                        if (response.isSuccessful){
                            problemas = response.body()!!
                            for (problema in problemas){
                                val position = LatLng(problema.lat.toString().toDouble(), problema.lon.toString().toDouble())
                                if(problema.utilizador_id == userid){
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setTag(problema.id.toString())
                                }else{
                                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo).snippet(problema.tipo)).setTag(problema.id.toString())
                                }
                            }

                        }
                    }
                    override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
                    }
                })

                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }
}