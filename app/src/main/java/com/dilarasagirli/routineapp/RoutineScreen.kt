//package com.dilarasagirli.routineapp
//
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.dilarasagirli.routineapp.databinding.ActivityRoutineScreenBinding
//
//class RoutineScreen : AppCompatActivity() {
//
//    private lateinit var binding: ActivityRoutineScreenBinding
//    private lateinit var tasklist : ArrayList<Tasks>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityRoutineScreenBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        val routine = intent.getSerializableExtra("routineName") as Routine
//        tasklist= routine.tasks
//
//        val adapter = RoutineScreenAdapter(tasklist)
//        binding.routinescreenrv.adapter=adapter
//        binding.routinescreenrv.layoutManager= LinearLayoutManager(this)
//
//
//    }
//}