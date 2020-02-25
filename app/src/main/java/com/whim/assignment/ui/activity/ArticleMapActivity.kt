package com.whim.assignment.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.whim.assignment.BaseApplication
import com.whim.assignment.R
import com.whim.assignment.common.ui.observeNonNull
import com.whim.assignment.common.ui.runIfNull
import com.whim.assignment.ui.ArticleMapComponent
import com.whim.assignment.ui.ArticleViewModel
import javax.inject.Inject

class ArticleMapActivity : AppCompatActivity() {
    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory
    // Stores an instance of NearByArticleComponent so that its Fragments can access it
    lateinit var articleMapComponent: ArticleMapComponent

    lateinit var articleViewModel: ArticleViewModel

    val REQUEST_CODE_LOCATION_PERMISSION = 1001;

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        articleMapComponent = (application as BaseApplication).appComponent.articleMapComponent().create()
        articleMapComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_map)

        articleViewModel = ViewModelProvider(this,viewModelProviderFactory).get(ArticleViewModel::class.java)
        articleViewModel.getNearByArticleLiveData().observeNonNull(this){
            Log.d("Result",""+it.data)
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

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

            fusedLocationClient.lastLocation
                .addOnSuccessListener {location ->
                    findNearByArticles(location)
                }
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
                fusedLocationClient.lastLocation
                    .addOnSuccessListener {location ->
                        findNearByArticles(location)
                    }
            }else{
                Toast.makeText(this,"Permission Denied", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun findNearByArticles(location : Location){
        var requestData = "${location.latitude}|${location.longitude}"
        articleViewModel.getNearByArticles(requestData)
    }

}
