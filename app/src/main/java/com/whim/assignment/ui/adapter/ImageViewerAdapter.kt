package com.whim.assignment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.whim.assignment.R
import kotlinx.android.synthetic.main.cell_image_layout.view.*


class ImageViewerAdapter(private val imageUrlList : List<String>, private val requestManager: RequestManager) : RecyclerView.Adapter<ImageViewerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_image_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return imageUrlList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageUrlList[position]
        requestManager
            .load(imageUrl)
            .into(holder.view.iv_article)

    }

    class ViewHolder( val view: View) : RecyclerView.ViewHolder(view)
}