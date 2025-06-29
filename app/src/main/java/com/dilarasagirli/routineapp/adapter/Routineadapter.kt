package com.dilarasagirli.routineapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.dilarasagirli.routineapp.R
import com.dilarasagirli.routineapp.databinding.RecyclerRowBinding
import com.dilarasagirli.routineapp.model.Routine
import com.dilarasagirli.routineapp.view.MainFragmentDirections

class Routineadapter (val routinesList: List<com.dilarasagirli.routineapp.model.Routine>,val onDelete: (Routine) -> Unit,val onEdit: (Routine) -> Unit): RecyclerView.Adapter<Routineadapter.RoutineViewHolder>() {

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
        holder.binding.imageButton.setOnClickListener {
            val popup=PopupMenu(holder.itemView.context,it)
            popup.menuInflater.inflate(R.menu.options_menu,popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_name -> {
                        onEdit(routinesList[position])
                        true
                    }
                    R.id.delete -> {
                        onDelete(routinesList[position])
                        true
                    }
                    else -> false
                }
            }
            popup.show()

        }
    }
}