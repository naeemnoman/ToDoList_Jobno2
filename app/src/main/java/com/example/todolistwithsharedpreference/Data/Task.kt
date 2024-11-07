package com.example.todolistwithsharedpreference.Data

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    var isCompleted: Boolean
)