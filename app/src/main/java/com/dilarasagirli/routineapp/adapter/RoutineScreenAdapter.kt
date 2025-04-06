package com.dilarasagirli.routineapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dilarasagirli.routineapp.databinding.RecyclerRowBinding

class RoutineScreenAdapter (val tasklist:List<com.dilarasagirli.routineapp.model.Tasks>): RecyclerView.Adapter<RoutineScreenAdapter.RoutineScreenViewHolder>() {

    class RoutineScreenViewHolder (val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineScreenViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RoutineScreenViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasklist.size
    }

    override fun onBindViewHolder(holder: RoutineScreenViewHolder, position: Int) {
        holder.binding.textView.text=tasklist[position].name
    }
}