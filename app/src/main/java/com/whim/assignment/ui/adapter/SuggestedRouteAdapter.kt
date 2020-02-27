package com.whim.assignment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.whim.assignment.R
import com.whim.assignment.ui.listener.RouteDirectionButtonListener
import com.whim.assignment.ui.model.RouteData
import kotlinx.android.synthetic.main.cell_suggested_route.view.*

class SuggestedRouteAdapter(private  val listRouteData: List<RouteData>,
                            private  val routeDirectionButtonListener: RouteDirectionButtonListener) : RecyclerView.Adapter<SuggestedRouteAdapter.ViewHolder>(){

    class ViewHolder( val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_suggested_route, parent, false)
        return SuggestedRouteAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  listRouteData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = listRouteData[position]
        holder.view.tv_distance.text = "${holder.view.tv_distance.text}${data.distance}"
        holder.view.tv_duration.text = "${holder.view.tv_duration.text}${data.duration}"
        holder.view.tv_route_title.text = "${holder.view.tv_route_title.text} ${position+1}"
        holder.view.btn_get_direction.setOnClickListener {
            routeDirectionButtonListener.onButtonClick(data)
        }

        }
    }
