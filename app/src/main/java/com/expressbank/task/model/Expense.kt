package com.expressbank.task.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses_table")
data class Expense(

    val expense:Float,
    @ColumnInfo(name = "budget_id")
    val budgetId:Int,
    @ColumnInfo(name = "category_id")
    val categoryId:Int,

    @PrimaryKey(autoGenerate = true)
    val id:Int=0
)
