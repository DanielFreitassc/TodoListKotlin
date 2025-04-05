package br.edu.satc.todolistcompose.repositories

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import br.edu.satc.todolistcompose.TaskData

@Dao
interface TaskDao {
    @Query("SELECT * FROM taskData")
    fun getAll(): List<TaskData>

    @Query("SELECT * FROM taskData WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<TaskData>

    @Update
    fun updateAll(vararg  taks: TaskData)

    @Insert
    fun insertAll(vararg tasks: TaskData)

    @Delete
    fun delete(tasks: TaskData);
}

@Database(entities = [TaskData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}