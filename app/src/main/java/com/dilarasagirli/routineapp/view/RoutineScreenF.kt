package com.dilarasagirli.routineapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.dilarasagirli.routineapp.adapter.RoutineScreenAdapter
import com.dilarasagirli.routineapp.classes.Tasks
import com.dilarasagirli.routineapp.databinding.FragmentRoutineScreenBinding
import com.dilarasagirli.routineapp.roomdb.RoutineDAO
import com.dilarasagirli.routineapp.roomdb.Routinedb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RoutineScreenF : Fragment() {
    private var _binding: FragmentRoutineScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var tasklist : ArrayList<Tasks>

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
        _binding = FragmentRoutineScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val id = RoutineScreenFArgs.fromBundle(it).routineId
            mDisposable.add(
                routineDAO.getAllTask(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)
            )
            binding.addbtn.setOnClickListener {
                val action=RoutineScreenFDirections.actionRoutineScreenFToAddTask(routineId = id)
                findNavController().navigate(action)
            }
            binding.editbtn.setOnClickListener {
                val action = RoutineScreenFDirections.actionRoutineScreenFToEditRoutine(routineId = id)
                findNavController().navigate(action)
            }

        }
    }

    private fun handleResponse(routine:List<com.dilarasagirli.routineapp.model.Tasks>) {
        val adapter = RoutineScreenAdapter(routine)
        binding.routinescreenrv.layoutManager= LinearLayoutManager(this.context)
        binding.routinescreenrv.adapter =adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}