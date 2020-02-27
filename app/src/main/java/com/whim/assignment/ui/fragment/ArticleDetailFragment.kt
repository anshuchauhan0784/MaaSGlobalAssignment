package com.whim.assignment.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.whim.assignment.R
import com.whim.assignment.common.Status
import com.whim.assignment.common.ui.observeNonNull
import com.whim.assignment.ui.ArticleDetailViewState
import com.whim.assignment.ui.ArticleViewModel
import com.whim.assignment.ui.GetRouteViewModel
import com.whim.assignment.ui.GetRouteViewState
import com.whim.assignment.ui.activity.ArticleMapActivity
import com.whim.assignment.ui.adapter.ImageViewerAdapter
import com.whim.assignment.ui.model.ArticleDetail
import com.whim.assignment.util.Constants
import kotlinx.android.synthetic.main.fragment_article_detail.*
import javax.inject.Inject


class ArticleDetailFragment : BottomSheetDialogFragment() {

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    @Inject
    internal  lateinit var requestManager: RequestManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as ArticleMapActivity).articleMapComponent.inject(this)
        var articleViewModel = ViewModelProvider(this,viewModelProviderFactory).get(ArticleViewModel::class.java)

        registerForLiveDataArticleDetail(articleViewModel)

        val pageID = arguments?.getInt(Constants.KEY_PAGE_ID)


        pageID?.let { id ->
            articleViewModel.getArticleDetail(id)
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_detail, container, false)
    }


    /**
     * This method will observe the live data for article detail
     */
    private fun registerForLiveDataArticleDetail(articleViewModel :ArticleViewModel){
        articleViewModel.getArticleDetailLiveData().observeNonNull(this){ state ->
            handleArticleDetailResponse(state)
        }
    }

    /**
     * This method will handle the response after make the request to get Article Detail
     */
    private fun handleArticleDetailResponse(state: ArticleDetailViewState){
        if(state.isLoading()){
            progressBar_detail.visibility = View.VISIBLE
        }else if(state.status == Status.SUCCESS){
            progressBar_detail.visibility = View.GONE
            updateUI(state.getArticleDetail())
        }else if(state.status == Status.ERROR){
            progressBar_detail.visibility = View.GONE
            (activity as ArticleMapActivity).showErrorToast(state.getErrorMessage())
        }
    }

    /**
     * This method will use to update article detail ui
     */

    private fun updateUI(detail: ArticleDetail?){

            tv_title.visibility = View.VISIBLE
            tv_title.text = detail?.title ?: getString(R.string.title_not_available)
            tv_description.visibility = View.VISIBLE
            tv_description.text = detail?.detail ?: getString(R.string.desc_not_available)
            rv_image_gallery.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false )
            detail?.imageUrl?.let {
                rv_image_gallery.adapter = ImageViewerAdapter(it,requestManager )
            }
            tv_wiki_link.visibility = View.VISIBLE
            tv_wiki_link.text = detail?.fullUrl ?: getString(R.string.wikipedia)
            divider.visibility = View.VISIBLE
            btn_direction.visibility = View.VISIBLE

            btn_direction.setOnClickListener {
                requestForRouteDetail()
            }
    }


    /**
     * On Direction Button click this method will be use to fetch all the route
     */
    private fun requestForRouteDetail(){
        val currentLatLng = arguments?.getString(Constants.CURRENT_LAT_LNG)
        val destinationLatLng = arguments?.getString(Constants.PAGE_LAT_LNG)
        if(currentLatLng.isNullOrBlank() || destinationLatLng.isNullOrBlank()) return
        var getRouteViewModel = ViewModelProvider(this,viewModelProviderFactory).get(GetRouteViewModel::class.java)
        registerForLiveDataGetRoute(getRouteViewModel)
        getRouteViewModel.getRouteDetail(currentLatLng,destinationLatLng,resources.getString(R.string.api_key))
    }


    /**
     * Register live data of Suggested Route
     */
    private fun registerForLiveDataGetRoute(getRouteViewModel: GetRouteViewModel){
        getRouteViewModel.getRouteLiveData().observeNonNull(this){ state ->
            handleGetRouteResponse(state)
        }
    }

    /**
     * This method will handle the state of response during the get route request. once the request success it will send data
     * to activity
     */
    private fun handleGetRouteResponse(state: GetRouteViewState){
        if(state.isLoading()){
            progressBar_detail.visibility = View.VISIBLE
        }else if(state.status == Status.SUCCESS){
            progressBar_detail.visibility = View.GONE
            (activity as ArticleMapActivity).processRoute(state.getRouteData())
            dismiss()
        }else if(state.status == Status.ERROR){
            progressBar_detail.visibility = View.GONE
            (activity as ArticleMapActivity).showErrorToast(state.getErrorMessage())
        }
    }

    companion object {
        const val TAG = "ArticleDetailFragment"
    }


}
