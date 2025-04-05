package com.dilarasagirli.routineapp.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dilarasagirli.routineapp.model.Routine
import com.dilarasagirli.routineapp.model.Tasks
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface RoutineDAO {
    @Query("SELECT * FROM routine")
    fun getAllRoutine(): Flowable<List<Routine>>

    @Query("SELECT * FROM routine WHERE routineId IN (:routineId)")
    fun loadAllRoutineByIds(routineId: IntArray): Flowable<List<Routine>>

    @Query("SELECT * FROM routine WHERE name LIKE :first LIMIT 1")
    fun getRoutineByName(first: String): Flowable<Routine>

    @Query("SELECT * FROM tasks WHERE routineId=:routineId ORDER BY taskOrder ASC")
    fun getAllTask(routineId:Int): Flowable<List<Tasks>>

    @Query("SELECT taskOrder FROM tasks WHERE routineId=:routineId ORDER BY taskOrder DESC LIMIT 1")
    fun getTaskOrder(routineId: Int): Maybe<Long>

    @Insert
    fun insertRoutine(routine: Routine) : Single<Long>

    @Insert
    fun insertTask(tasks: Tasks):Completable

    @Delete
    fun deleteRoutine(routine: Routine):Completable

    @Delete
    fun deleteTask(tasks: Tasks):Completable

    @Query ("DELETE FROM routine")
    fun deleteRoutines():Completable

    @Query ("DELETE FROM tasks")
    fun deleteTasks():Completable

    @Query ("UPDATE tasks SET taskOrder=:order WHERE routineId=:routineId AND taskId=:taskId")
    fun updateTaskOrder(order:Int,routineId: Int,taskId:Int): Completable
}