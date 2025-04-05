package com.dilarasagirli.routineapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.dilarasagirli.routineapp.R
import com.dilarasagirli.routineapp.classes.Routine
import com.dilarasagirli.routineapp.databinding.FragmentAddRoutineBinding
import com.dilarasagirli.routineapp.roomdb.RoutineDAO
import com.dilarasagirli.routineapp.roomdb.Routinedb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddRoutineFragment : Fragment() {
    private var _binding: FragmentAddRoutineBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentAddRoutineBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.routineNameEditT.addTextChangedListener {
            val isValid = it.toString().trim().isNotEmpty()
            binding.savebtn.isEnabled = isValid
        }
        binding.savebtn.setOnClickListener {
            val name=binding.routineNameEditT.text.toString()

            val routine= com.dilarasagirli.routineapp.model.Routine(name = name, totalTime = null, taskCount = null, Completed = false)
            mDisposable.add(
                routineDAO.insertRoutine(routine)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)
            )
        }
        return view
    }

    private fun handleResponse(routineId:Long){
        val action = AddRoutineFragmentDirections.actionAddRoutineFragmentToEditRoutine(routineId = routineId.toInt())
        findNavController().navigate(action)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }

}