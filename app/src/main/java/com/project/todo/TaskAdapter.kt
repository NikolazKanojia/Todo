package com.project.todo

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onCheckToggle: (Int) -> Unit,
    private val onStarToggle: (Int) -> Unit,
    private val onTaskLongClick: (Task) -> Unit

) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {

        val cardTask: CardView = itemView.findViewById(R.id.cardTask)
        val cbTask: CheckBox = itemView.findViewById(R.id.cbTask)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvSub: TextView = itemView.findViewById(R.id.tvTaskSubtitle)
        val ivStar: ImageView = itemView.findViewById(R.id.ivStar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)

        return TaskViewHolder(view)
    }

    fun submitList(newList: List<Task>) {

        tasks.clear()
        tasks.addAll(newList)

        notifyDataSetChanged()
    }

    fun getTask(position: Int): Task {

        return tasks[position]
    }

    override fun getItemCount(): Int {
        Log.d("ITEM_COUNT", tasks.size.toString())
        return tasks.size
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {

        val task = tasks[position]
        Log.d("ITEM_BIND", "Position = $position")
        holder.tvTitle.text = task.title
        holder.tvSub.text = task.description

        // Remove previous listener (important for RecyclerView)
        holder.cbTask.setOnCheckedChangeListener(null)

        // Checkbox state
        holder.cbTask.isChecked = task.isCompleted

        holder.tvTitle.setTextColor(
            if (task.isCompleted)
                Color.parseColor("#AAAACC")
            else
                Color.parseColor("#1A1A2E")
        )

        holder.cbTask.buttonDrawable = null

        holder.cbTask.background =
            if (task.isCompleted)
                holder.itemView.context.getDrawable(
                    R.drawable.bg_checkbox_checked
                )
            else
                holder.itemView.context.getDrawable(
                    R.drawable.bg_checkbox_unchecked
                )

        // Star state
        holder.ivStar.setImageResource(
            if (task.isStarred)
                R.drawable.ic_star_filled
            else
                R.drawable.ic_star_outline
        )

        // Checkbox click
        holder.cbTask.setOnClickListener {

            val pos = holder.bindingAdapterPosition

            if (pos != RecyclerView.NO_POSITION) {

                tasks[pos].isCompleted = holder.cbTask.isChecked

                holder.cbTask.background =
                    if (tasks[pos].isCompleted)
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.bg_checkbox_checked
                        )
                    else
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.bg_checkbox_unchecked
                        )

                holder.tvTitle.setTextColor(
                    if (tasks[pos].isCompleted)
                        Color.parseColor("#AAAACC")
                    else
                        Color.parseColor("#1A1A2E")
                )

                onCheckToggle(pos)
            }
        }

        // Star click
        holder.ivStar.setOnClickListener {

            val pos = holder.bindingAdapterPosition

            if (pos != RecyclerView.NO_POSITION) {

                tasks[pos].isStarred = !tasks[pos].isStarred

                holder.ivStar.setImageResource(
                    if (tasks[pos].isStarred)
                        R.drawable.ic_star_filled
                    else
                        R.drawable.ic_star_outline
                )

                onStarToggle(pos)
            }
        }
        holder.itemView.setOnLongClickListener {

            val pos = holder.bindingAdapterPosition

            if (pos != RecyclerView.NO_POSITION) {
                onTaskLongClick(tasks[pos])
            }

            true
        }


    }
}