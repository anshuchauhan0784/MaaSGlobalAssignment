package com.whim.assignment.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.whim.assignment.R
import javax.inject.Inject


class ArticleDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*(activity as NearByArticleActivity).nearByArticleComponent.inject(this)
        var model = ViewModelProvider(this,viewModelProviderFactory).get(NearByArticleViewModel::class.java)*/
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_detail, container, false)
    }


}
