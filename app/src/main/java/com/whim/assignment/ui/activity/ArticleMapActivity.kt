package com.whim.assignment.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.whim.assignment.BaseApplication
import com.whim.assignment.R
import com.whim.assignment.common.Status
import com.whim.assignment.common.ui.observeNonNull
import com.whim.assignment.ui.ArticleMapComponent
import com.whim.assignment.ui.ArticleViewModel
import com.whim.assignment.ui.NearByArticleViewState
import com.whim.assignment.ui.fragment.ArticleDetailFragment
import com.whim.assignment.ui.model.RouteData
import com.whim.assignment.util.Constants
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

    private var locationPermissionGranted = false

    private  var lastKnownLocation : Location? = null

    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    private  val DEFAULT_ZOOM = 12f

    private var selectedMarkerPosition : LatLng? = null

    // Saving the list of marker on activity level so that after cancel on draw route we can show it again
    private var listOfMarker = mutableListOf<Marker?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        articleMapComponent = (application as BaseApplication).appComponent.articleMapComponent().create()
        articleMapComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_map)
        initializeMap()
        checkLocationPermission()
        articleViewModel = ViewModelProvider(this,viewModelProviderFactory).get(ArticleViewModel::class.java)
        registerForLiveDataNearByArticle()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    private fun registerForLiveDataNearByArticle(){
        articleViewModel.getNearByArticleLiveData().observeNonNull(this){ state ->
            handleNearbyArticleResponse(state)

        }
    }

    /**
     * This method will handle the response after make the request for near by article
     */
    private fun handleNearbyArticleResponse(nearByArticleViewState: NearByArticleViewState){
        if(nearByArticleViewState.isLoading()){
            progressBar.visibility = View.VISIBLE
        }else if(nearByArticleViewState.status == Status.SUCCESS){
            progressBar.visibility = View.GONE
            drawMarker(nearByArticleViewState)
        }else if(nearByArticleViewState.status == Status.ERROR){
            progressBar.visibility = View.GONE
            showErrorToast(nearByArticleViewState.getErrorMessage())
        }
    }

    /**
     * This method will use to show error Toast
     */
     fun showErrorToast(errorMessage : String?){
        progressBar.visibility = View.GONE
        Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show()
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
        if (locationPermissionGranted) {
            googleMap?.isMyLocationEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = true
            fusedLocationClient.lastLocation.addOnCompleteListener {task ->

                if(task.isSuccessful){
                    lastKnownLocation = task.result
                    lastKnownLocation?.let { location ->
                        googleMap?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                ), DEFAULT_ZOOM
                            )
                        )
                        findNearByArticles(location)
                    }

                }
            }


        }
    }


    /**
     * This method will use to draw marker
     */
    private fun drawMarker(state : NearByArticleViewState){


        state.getArticleList()?.let { articleList ->
            for (articleGeoData in articleList){
                val latLng = LatLng(articleGeoData.lat,articleGeoData.lng)
                var marker  = googleMap?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(articleGeoData.title)
                )
                marker?.tag = articleGeoData.id
                listOfMarker.add(marker)
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
            locationPermissionGranted = true
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
                locationPermissionGranted = true
                getDeviceLocation()
            }else{
                locationPermissionGranted = false
                Toast.makeText(this,"Permission Denied", Toast.LENGTH_LONG).show()
            }

        }

    }




    private fun findNearByArticles(location : Location){
        var requestData = "${location.latitude}|${location.longitude}"
        articleViewModel.getNearByArticles(requestData)
    }


    private  val onMarkerClick = GoogleMap.OnMarkerClickListener {marker ->
        selectedMarkerPosition =  marker.position
        showArticleDetail(marker.tag as Int,marker.position)
        return@OnMarkerClickListener true
    }

   private fun showArticleDetail(pageId : Int, latLng: LatLng) {
       var data  = Bundle()
       data.putInt(Constants.KEY_PAGE_ID,pageId)
       data.putString(Constants.PAGE_LAT_LNG,"${latLng.latitude},${latLng.longitude}")
       data.putString(Constants.CURRENT_LAT_LNG,"${lastKnownLocation?.latitude},${lastKnownLocation?.longitude}")

       val articleDetailFragment = ArticleDetailFragment()
       articleDetailFragment?.arguments = data
       articleDetailFragment.show(supportFragmentManager,ArticleDetailFragment.TAG)
    }

    fun drawRoute(routeData: RouteData?){
        googleMap?.clear()
        if(selectedMarkerPosition != null) {
            googleMap?.addMarker(
                MarkerOptions()
                    .position(selectedMarkerPosition!!)
            )
        }

        routeData?.routes?.let {
            googleMap?.addPolyline(PolylineOptions().addAll(it))
        }


    }


}



// Get the fragment reference
/* val fragment = supportFragmentManager.findFragmentById(R.id.bottom_sheet)
var data  = Bundle()
data.putInt("PageId",pageId)
fragment?.arguments = data
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
 }*/