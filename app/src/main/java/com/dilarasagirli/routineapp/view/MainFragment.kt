package com.dilarasagirli.routineapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.dilarasagirli.routineapp.R
import com.dilarasagirli.routineapp.classes.Routine
import com.dilarasagirli.routineapp.adapter.Routineadapter
import com.dilarasagirli.routineapp.classes.Tasks
import com.dilarasagirli.routineapp.databinding.FragmentMainBinding
import com.dilarasagirli.routineapp.roomdb.RoutineDAO
import com.dilarasagirli.routineapp.roomdb.Routinedb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var routinesList : ArrayList<Routine>
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

        binding.routinrv.layoutManager = LinearLayoutManager(this.context)

        binding.addbtn2.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addRoutineFragment)
        }
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAll()

    }

    private fun getAll() {
        mDisposable.add(
            routineDAO.getAllRoutine()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(routine: List<com.dilarasagirli.routineapp.model.Routine>) {
        val adapter = Routineadapter(routine)
        binding.routinrv.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }

}