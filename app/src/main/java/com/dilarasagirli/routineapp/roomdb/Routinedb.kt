package com.dilarasagirli.routineapp.roomdb

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dilarasagirli.routineapp.model.Routine
import com.dilarasagirli.routineapp.model.Tasks

@Database (entities = [Routine::class,Tasks::class], version = 9,exportSchema = true,
        autoMigrations = [
    AutoMigration (from = 1, to = 2),
        AutoMigration(from = 2, to =3),
        AutoMigration(from = 4,to=5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7)
])
abstract class Routinedb : RoomDatabase(){
    abstract fun routineDao():RoutineDAO
    companion object {
        private val MIGRATION_3_4 = object : Migration(3,4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE Tasks ADD COLUMN routineId INTEGER DEFAULT 0")

                db.execSQL("UPDATE Tasks SET routineId = 0 WHERE routineId IS NULL")

                db.execSQL("PRAGMA foreign_keys=off;")
                db.execSQL("CREATE TABLE new_Tasks AS SELECT * FROM Tasks")
                db.execSQL("DROP TABLE Tasks")
                db.execSQL("ALTER TABLE new_Tasks RENAME TO Tasks")
                db.execSQL("PRAGMA foreign_keys=on;")

            }
    }
        private val MIGRATION_7_8 = object : Migration(7,8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE Tasks ADD COLUMN task_order INTEGER NOT NULL DEFAULT 0")
                db.execSQL("UPDATE Tasks SET task_order = 0 WHERE task_order IS NULL")

            }

        }
        //changing the column name without underscores
        private val MIGRATION_8_9 = object :Migration(8,9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                //closing the foreign keys in order to not interfere with the referencing table
                db.execSQL("PRAGMA foreign_keys=off;")

                db.execSQL("""CREATE TABLE new_tasks (
                taskId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                routineId INTEGER NOT NULL DEFAULT 0,
                name TEXT NOT NULL,
                duration INTEGER NOT NULL,
                completed INTEGER NOT NULL DEFAULT 0,
                taskOrder INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(routineId) REFERENCES routine(routineId) ON DELETE CASCADE
                )""")
                //copying the data from the old table
                db.execSQL("""
                    INSERT INTO new_tasks(taskId,routineId,name,duration,completed,taskOrder) 
                    SELECT taskId,routineId,name,duration,completed,task_order FROM Tasks
                    """.trimIndent())
                db.execSQL("DROP TABLE Tasks")
                db.execSQL("ALTER TABLE new_tasks RENAME TO Tasks")

                db.execSQL("PRAGMA foreign_keys=on;")
            }
        }
        @Volatile
        private var instance: Routinedb? = null

        fun getDatabase(context: Context): Routinedb {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    Routinedb::class.java,
                    "routine_db"
                )
                    .addMigrations(MIGRATION_3_4, MIGRATION_7_8, MIGRATION_8_9) // Add migration here
                    .build().also { instance = it }
            }
        }
    }
}