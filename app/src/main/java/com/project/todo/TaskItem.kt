package com.project.todo

data class TaskItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    var isCompleted: Boolean,
    var isStarred: Boolean
)
