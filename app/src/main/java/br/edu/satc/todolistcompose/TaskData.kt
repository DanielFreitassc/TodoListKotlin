package br.edu.satc.todolistcompose

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskData (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "complete")
    val complete: Boolean
)