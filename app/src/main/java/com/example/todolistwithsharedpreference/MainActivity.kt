package com.example.todolistwithsharedpreference

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistwithsharedpreference.Adaptor.TaskAdaptor
import com.example.todolistwithsharedpreference.Data.Task


class MainActivity : AppCompatActivity() {
    private lateinit var taskList: MutableList<Task>
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdaptor
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editText: EditText
    private lateinit var editText2: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)
        editText = findViewById(R.id.editTxt)
        editText2 = findViewById(R.id.editTxt2)
        addButton = findViewById(R.id.btn)
        recyclerView = findViewById(R.id.Rv)

        taskList = retrieveTasks()

        taskAdapter = TaskAdaptor(taskList, object : TaskAdaptor.TaskClickListener {
            override fun onEditClick(position: Int) {
                editText.setText(taskList[position].title)
                editText2.setText(taskList[position].description)
                taskList.removeAt(position)
                taskAdapter.notifyItemRemoved(position)
                saveTasks(taskList)
            }

            override fun onDeleteClick(position: Int) {
                taskList.removeAt(position)
                taskAdapter.notifyItemRemoved(position)
                saveTasks(taskList)
            }
        })

        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addButton.setOnClickListener {
            val taskText = editText.text.toString()
            val taskDescription = editText2.text.toString()
            if (taskText.isNotEmpty() || taskDescription.isNotEmpty()) {
                val task = Task(taskList.size + 1, taskText, taskDescription, false) // Create a new task with description
                taskList.add(task)
                saveTasks(taskList)
                taskAdapter.notifyItemInserted(taskList.size - 1)
                editText.text.clear()
                editText2.text.clear()
            } else {
                Toast.makeText(this, "Task title can't be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Attach ItemTouchHelper for swipe actions
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                taskAdapter.removeItem(position)  // Perform the swipe-to-delete action
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun saveTasks(taskList: MutableList<Task>) {
        val editor = sharedPreferences.edit()
        val taskSet = HashSet<String>()

        taskList.forEach { taskSet.add("${it.title}|${it.description}") }
        editor.putStringSet("tasks", taskSet)
        editor.apply()
    }

    private fun retrieveTasks(): MutableList<Task> {
        val tasks = sharedPreferences.getStringSet("tasks", HashSet()) ?: HashSet()
        return tasks.map {
            val parts = it.split("|")
            Task(0, parts[0], parts.getOrNull(1) ?: "", false)
        }.toMutableList()
    }
}
