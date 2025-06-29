package com.dilarasagirli.routineapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilarasagirli.routineapp.R
import com.dilarasagirli.routineapp.adapter.Routineadapter
import com.dilarasagirli.routineapp.databinding.FragmentMainBinding
import com.dilarasagirli.routineapp.model.Routine
import com.dilarasagirli.routineapp.roomdb.RoutineDAO
import com.dilarasagirli.routineapp.roomdb.Routinedb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var routineDAO: RoutineDAO
    private val mDisposable=CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Routinedb.getDatabase(requireContext())
        routineDAO=db.routineDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAll()
        binding.routinrv.layoutManager = LinearLayoutManager(this.context)

        binding.addbtn2.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addRoutineFragment)
        }
    }

    private fun getAll() {
        mDisposable.add(
            routineDAO.getAllRoutine()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(routines: List<com.dilarasagirli.routineapp.model.Routine>) {
        val adapter = Routineadapter(routines, onEdit ={routine-> showEditDialog(routine)}, onDelete = {routine -> deleteRoutine(routine)} )
        binding.routinrv.adapter = adapter
    }

    private fun deleteRoutine(routine: Routine) {
        mDisposable.add(
            routineDAO.deleteRoutine(routine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    private fun updateRoutine(routine: Routine) {
        mDisposable.add(
            routineDAO.editRoutine(routine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    private fun showEditDialog(routine: Routine){
        val editText=EditText(requireContext())
        editText.setText(routine.name)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit routine name")
            .setView(editText)
            .setPositiveButton("Save") {dialog,which ->
                val newName=editText.text.toString()
                if(newName.isNotBlank()) {
                    val updatedRoutine = routine.copy(name = newName)
                    updateRoutine(updatedRoutine)
                }
            }
            .setNegativeButton("Cancel",null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }

}