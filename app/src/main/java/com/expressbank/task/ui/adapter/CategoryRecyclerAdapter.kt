package com.expressbank.task.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expressbank.task.databinding.CategoryItemLayoutBinding
import com.expressbank.task.model.databaseview.CategoryWithExpance

class CategoryRecyclerAdapter(private val listener: OnItemClickListener) :
    ListAdapter<CategoryWithExpance, CategoryRecyclerAdapter.TasksViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding =
            CategoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TasksViewHolder(private val binding: CategoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
            }
        }

        fun bind(   task: CategoryWithExpance) {
            binding.apply {
                categoryIcon.apply {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            categoryIcon.context,
                            task.categoryDrawable
                        )
                    )
                    setColorFilter(
                        ContextCompat.getColor(
                            categoryIcon.context,
                            task.categoryColor
                        )
                    )
                }

                categoryPercentIndicator.apply {
                    progress = task.expansePercent.toInt()
                    setIndicatorColor(ContextCompat.getColor(this.context,task.categoryColor))
                }

                categoryName.text = task.categoryName

                categoryPercent.text=String.format("%.1f", task.expansePercent).plus( " %")

                dot.setTextColor(ContextCompat.getColor(dot.context, task.categoryColor))

                categoryExpenceAmount.text = task.expense.toString().plus(" AZN")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(task: CategoryWithExpance)
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryWithExpance>() {
        override fun areItemsTheSame(oldItem: CategoryWithExpance, newItem: CategoryWithExpance) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CategoryWithExpance, newItem: CategoryWithExpance) =
            oldItem == newItem
    }
}
