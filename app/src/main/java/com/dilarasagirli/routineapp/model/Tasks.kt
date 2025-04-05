package com.dilarasagirli.routineapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
    (foreignKeys = [
    ForeignKey(
        entity = Routine::class,
        parentColumns = ["routineId"],
        childColumns = ["routineId"],
        onDelete = ForeignKey.CASCADE
        )
    ]
)
class Tasks (
    @PrimaryKey(autoGenerate = true) val taskId :Int = 0,
    @ColumnInfo(name = "routineId", defaultValue = "0") val routineId:Int = 0,
    var name: String,
    var duration:Int,
    var completed:Boolean,
    @ColumnInfo(name="taskOrder",defaultValue="0") var taskOrder:Int = 0
) {

}