package com.whim.assignment.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.whim.assignment.BaseApplication
import com.whim.assignment.R
import com.whim.assignment.common.ui.observeNonNull
import com.whim.assignment.ui.ArticleMapComponent
import com.whim.assignment.ui.ArticleViewModel
import com.whim.assignment.ui.NearByArticleViewState
import com.whim.assignment.util.Constants.Companion.DEFAULT_ZOOM
import kotlinx.android.synthetic.main.activity_article_map.*
import javax.inject.Inject


class ArticleMapActivity : AppCompatActivity(), OnMapReadyCallback {
    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory
    // Stores an instance of NearByArticleComponent so that its Fragments can access it
    lateinit var articleMapComponent: ArticleMapComponent

    lateinit var articleViewModel: ArticleViewModel

    private val REQUEST_CODE_LOCATION_PERMISSION = 1001;

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private  var googleMap: GoogleMap? = null

    private var mLocationPermissionGranted = false

    private  var mLastKnownLocation : Location? = null

    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        articleMapComponent = (application as BaseApplication).appComponent.articleMapComponent().create()
        articleMapComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_map)
        configureBackdrop()
        initializeMap()
        checkLocationPermission()
        articleViewModel = ViewModelProvider(this,viewModelProviderFactory).get(ArticleViewModel::class.java)
        articleViewModel.getNearByArticleLiveData().observeNonNull(this){ state ->
                drawMarker(state)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)





    }

    private fun initializeMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.article_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        getDeviceLocation()
    }


    /**
     * This method will use to get current device location
     */
    private fun getDeviceLocation() {
        if (mLocationPermissionGranted) {
            fusedLocationClient.lastLocation.addOnCompleteListener {task ->

                if(task.isSuccessful){
                    mLastKnownLocation = task.result
                    mLastKnownLocation?.let { location ->
                        googleMap?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    location.getLatitude(),
                                    location.getLongitude()
                                ), DEFAULT_ZOOM
                            )
                        )
                        findNearByArticles(location)
                    }

                }
            }

            /*googleMap?.isMyLocationEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = true*/
        }
    }


    /**
     * This method will use to draw marker
     */
    private fun drawMarker(state : NearByArticleViewState){


        state.data?.let { articleList ->
            for (articleGeoData in articleList){
                val latLng = LatLng(articleGeoData.lat,articleGeoData.lng)
                var marker  = googleMap?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(articleGeoData.title)
                )
                marker?.tag = articleGeoData.id
                googleMap?.setOnMarkerClickListener(onMarkerClick)

            }
        }

    }

    /**
     * This method will use to check the location permission if permission not set it will prompt user to allow permission
     */

    private fun checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        } else {
            mLocationPermissionGranted = true
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION){
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true
                getDeviceLocation()
            }else{
                mLocationPermissionGranted = false
                Toast.makeText(this,"Permission Denied", Toast.LENGTH_LONG).show()
            }

        }

    }




    private fun findNearByArticles(location : Location){
        var requestData = "${location.latitude}|${location.longitude}"
        articleViewModel.getNearByArticles(requestData)
    }


    private  val onMarkerClick = GoogleMap.OnMarkerClickListener {marker ->
    // mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        showBottomSheetDialog()
        articleViewModel.getArticleDetail(marker.tag as Int)
        return@OnMarkerClickListener true
    }

    private fun showBottomSheetDialog()
    {
        var articleDetailDialog =  BottomSheetDialog(this)
        var detailView = layoutInflater.inflate(R.layout.fragment_article_detail,null)

        articleDetailDialog.setContentView(detailView)
        articleDetailDialog.show()

    }

    private fun configureBackdrop() {
        // Get the fragment reference
        val fragment = supportFragmentManager.findFragmentById(R.id.bottom_sheet)

        fragment?.let {
            // Get the BottomSheetBehavior from the fragment view
            it.view?.let { it1 ->
                BottomSheetBehavior.from(it1)?.let { bsb ->
                    // Set the initial state of the BottomSheetBehavior to HIDDEN
                    bsb.state = BottomSheetBehavior.STATE_COLLAPSED
                    bsb.isFitToContents = true
                    mBottomSheetBehavior = bsb

                }
            }
        }
    }

}
