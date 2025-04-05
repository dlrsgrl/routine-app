package com.dilarasagirli.routineapp.view

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.dilarasagirli.routineapp.R
import com.dilarasagirli.routineapp.databinding.FragmentAddTaskBinding
import com.dilarasagirli.routineapp.databinding.FragmentEditRoutineBinding
import com.dilarasagirli.routineapp.model.Tasks
import com.dilarasagirli.routineapp.roomdb.RoutineDAO
import com.dilarasagirli.routineapp.roomdb.Routinedb
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddTask : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
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
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        var isEmpty = false
        var isTaskEmpty = false
        binding.editTextText.addTextChangedListener {
            isEmpty = it.toString().trim().isNotEmpty()
            binding.addbtn1.isEnabled =  isEmpty && isTaskEmpty
        }
        binding.taskTime.addTextChangedListener {
            isTaskEmpty = it.toString().trim().isNotEmpty()
            binding.addbtn1.isEnabled =  isEmpty && isTaskEmpty
        }
        arguments?.let {
            val routineId = AddTaskArgs.fromBundle(it).routineId
            binding.taskTime.setOnClickListener {
                val timePicker = MaterialTimePicker.Builder()
                    .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(0)
                    .setMinute(30)
                    .setTitleText("Select Time")
                    .build()

                timePicker.show(parentFragmentManager,"timepicker tag")
                timePicker.addOnPositiveButtonClickListener {
                    val selectedHour = timePicker.hour
                    val selectedMinute = timePicker.minute
                    binding.taskTime.setText("$selectedHour:$selectedMinute")
                }
            }
            binding.addbtn1.setOnClickListener {
                //Checking the last added task's order number
                mDisposable.add(
                    routineDAO.getTaskOrder(routineId)
                        .defaultIfEmpty(-1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )
            }
        }
        return view
    }

    private fun handleResponse(taskOrder:Long){
        val task_order=if (taskOrder.toInt() == -1) 0 else taskOrder.toInt() + 1
        val taskTime = binding.taskTime.text.toString().split(":")
        val hour =taskTime[0].toInt()
        val min = taskTime[1].toInt()
        val duration = hour * 60 + min
        val task = Tasks(routineId = AddTaskArgs.fromBundle(requireArguments()).routineId ,name = binding.editTextText.text.toString(), duration = duration,taskOrder=task_order, completed = false)
        mDisposable.add(
            routineDAO.insertTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    findNavController().navigateUp()
                }
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }

}