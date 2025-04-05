package com.dilarasagirli.routineapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine")
data class Routine (
    @PrimaryKey(autoGenerate = true) val routineId: Int = 0,
    @ColumnInfo(name = "name")var name: String,
    @ColumnInfo(name = "time")var totalTime: Int?,
    @ColumnInfo(name = "taskNumber")var taskCount: Int?,
    @ColumnInfo(name = "check")var Completed: Boolean
    ) {

}