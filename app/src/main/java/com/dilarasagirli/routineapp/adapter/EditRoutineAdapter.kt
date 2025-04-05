package com.dilarasagirli.routineapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dilarasagirli.routineapp.databinding.RecyclerRowBinding
import com.dilarasagirli.routineapp.model.Tasks
import java.util.Collections

class EditRoutineAdapter (val tasklist: MutableList<Tasks>,private val itemClickListener: ((Tasks) -> Unit)? = null ): RecyclerView.Adapter<EditRoutineAdapter.EditRoutineVh>(){

    class EditRoutineVh (val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditRoutineVh {
        val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EditRoutineVh(binding)
    }

    override fun getItemCount(): Int {
        return tasklist.size
    }

    override fun onBindViewHolder(holder: EditRoutineVh, position: Int) {
       holder.binding.textView.text= tasklist[position].name
    }

    fun getUpdatedList(): List<Tasks> {
        return tasklist
    }

    fun updateItemMoved(fromPos:Int, toPos: Int) {
        Collections.swap(tasklist, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }
}