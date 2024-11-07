package com.example.todolistwithsharedpreference.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistwithsharedpreference.Data.Task
import com.example.todolistwithsharedpreference.R

class TaskAdaptor(
    private val tasks: MutableList<Task>,
    private val listener: TaskClickListener
) : RecyclerView.Adapter<TaskAdaptor.TaskViewHolder>() {

    interface TaskClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.idtxt)
        val titleTextView: TextView = itemView.findViewById(R.id.titletxt)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBx)
        val descriptionTextView: TextView = itemView.findViewById(R.id.desctxt)
        val editButton: ImageButton = itemView.findViewById(R.id.addBtn)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteBtn)

        fun bind(task: Task) {
            idTextView.text = task.id.toString() // Display task ID
            titleTextView.text = task.title
            descriptionTextView.text = task.description // Display task description
            checkBox.setOnCheckedChangeListener(null) // Clear listener before setting new state
            checkBox.isChecked = task.isCompleted

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                // Optionally, notify the listener about the change
            }

            editButton.setOnClickListener {
                listener.onEditClick(adapterPosition)
            }

            deleteButton.setOnClickListener {
                listener.onDeleteClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun removeItem(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }
}
