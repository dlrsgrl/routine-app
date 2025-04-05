package com.dilarasagirli.routineapp.classes

import java.io.Serializable

class Routine (var name: String, var totalTime: Int?, var taskNumber:Int?, var isCompleted: Boolean,var tasks: ArrayList<Tasks>?): Serializable

