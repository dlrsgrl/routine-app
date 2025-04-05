package com.dilarasagirli.routineapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dilarasagirli.routineapp.R
import com.dilarasagirli.routineapp.adapter.EditRoutineAdapter
import com.dilarasagirli.routineapp.classes.Routine
import com.dilarasagirli.routineapp.databinding.FragmentEditRoutineBinding
import com.dilarasagirli.routineapp.databinding.FragmentMainBinding
import com.dilarasagirli.routineapp.model.Tasks
import com.dilarasagirli.routineapp.roomdb.RoutineDAO
import com.dilarasagirli.routineapp.roomdb.Routinedb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class EditRoutine : Fragment() {
    private var _binding: FragmentEditRoutineBinding? = null
    private val binding get() = _binding!!

    private lateinit var routinesList : ArrayList<Routine>
    private lateinit var routineDAO: RoutineDAO
    private val mDisposable= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Routinedb.getDatabase(requireContext())
        routineDAO=db.routineDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditRoutineBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.editRoutinerv.layoutManager = LinearLayoutManager(this.context)

        arguments?.let{
            val id=EditRoutineArgs.fromBundle(it).routineId
            mDisposable.add(
                routineDAO.getAllTask(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)
            )
            binding.donebtn.setOnClickListener {
                val updated =(binding.editRoutinerv.adapter as EditRoutineAdapter).getUpdatedList()
                val tasks = updated.mapIndexed{index, task ->
                    routineDAO.updateTaskOrder(index,id,task.taskId)
                }
                Completable.merge(tasks)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

//                updated?.forEachIndexed { index, task ->
//                    mDisposable.add(
//                        routineDAO.updateTaskOrder(index,id,task.taskId)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe()
//                    )
//                }
                val action = EditRoutineDirections.actionEditRoutineToRoutineScreenF(routineId = id)
                val navOptions = navOptions {
                    popUpTo(R.id.routineScreenF) { inclusive = true }
                }
                findNavController().navigate(action,navOptions)

            }
            binding.addtaskbtn.setOnClickListener {
                val action=EditRoutineDirections.actionEditRoutineToAddTask(routineId = id)
                findNavController().navigate(action)
            }


        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }

    private fun handleResponse(taskslist:List<Tasks>){
        val adapter =EditRoutineAdapter(taskslist.toMutableList())
        binding.editRoutinerv.adapter=adapter

        val itemTouchHelper=ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,0)
            {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPos= viewHolder.adapterPosition
                    val toPos = target.adapterPosition
                    adapter.updateItemMoved(fromPos,toPos)
                    return true
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                }
            }
        )
        itemTouchHelper.attachToRecyclerView(binding.editRoutinerv)
    }

}