package com.project.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val description: String,

    val dueDate: String,

    val reminder: String,

    var isCompleted: Boolean = false,

    var isStarred: Boolean = false
)
