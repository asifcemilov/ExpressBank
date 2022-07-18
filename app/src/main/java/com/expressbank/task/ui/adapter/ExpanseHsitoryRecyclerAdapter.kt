package com.expressbank.task.ui.adapter

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expressbank.task.databinding.ExpenceItemLayoutBinding
import com.expressbank.task.model.ExpenseHistory
import com.expressbank.task.model.databaseview.CategoryWithExpance

class ExpanseHsitoryRecyclerAdapter :
    ListAdapter<ExpenseHistory, ExpanseHsitoryRecyclerAdapter.TasksViewHolder>(DiffCallback()) {

    lateinit var categoryWithExpance: CategoryWithExpance
    fun setCategoryWithExpanse(categoryWithExpance: CategoryWithExpance) {
        this.categoryWithExpance = categoryWithExpance
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding =
            ExpenceItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.i("aaa", "onCreateViewHolder: ")

        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TasksViewHolder(private val binding: ExpenceItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        getItem(position)
                    }
                }
            }
        }

        fun bind(task: ExpenseHistory) {
            Log.i("aaaa", "bind: ")

            binding.apply {
                categoryIcon.apply {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            this.context,
                            categoryWithExpance.categoryDrawable
                        )
                    )
                    setColorFilter(
                        ContextCompat.getColor(
                            this.context,
                            categoryWithExpance.categoryColor
                        )
                    )
                }

                iconBackground.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        iconBackground.context,
                        categoryWithExpance.categoryColor
                    )
                )

                expenseName.text = task.costDetail
                expenceDate.text = task.costDate
                expenceAmount.text = task.cost.toInt().toString()
            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<ExpenseHistory>() {
        override fun areItemsTheSame(oldItem: ExpenseHistory, newItem: ExpenseHistory) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ExpenseHistory, newItem: ExpenseHistory) =
            oldItem == newItem
    }
}
