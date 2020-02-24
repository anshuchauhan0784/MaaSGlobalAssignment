package com.whim.assignment.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {
        articleMapComponent = (application as BaseApplication).appComponent.articleMapComponent().create()
        articleMapComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_map)

        articleViewModel = ViewModelProvider(this,viewModelProviderFactory).get(ArticleViewModel::class.java)
        articleViewModel.getNearByArticleLiveData().observeNonNull(this){
            Log.d("Result",""+it.data)
        }

        savedInstanceState.runIfNull {
            articleViewModel.getNearByArticles("60.1831906|24.92854")
        }
    }
}
