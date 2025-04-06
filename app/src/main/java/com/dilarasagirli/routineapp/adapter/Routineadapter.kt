package com.dilarasagirli.routineapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.dilarasagirli.routineapp.databinding.RecyclerRowBinding
import com.dilarasagirli.routineapp.view.MainFragmentDirections

class Routineadapter (val routinesList: List<com.dilarasagirli.routineapp.model.Routine>): RecyclerView.Adapter<Routineadapter.RoutineViewHolder>() {

    class RoutineViewHolder (val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RoutineViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return routinesList.size
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        holder.binding.textView.text = routinesList[position].name

        holder.itemView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToRoutineScreenF(routineId = routinesList[position].routineId)
            Navigation.findNavController(it).navigate(action)
        }
    }
}