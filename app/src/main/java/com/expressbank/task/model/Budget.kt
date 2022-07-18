package com.expressbank.task.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets_table")
data class Budget(
    val incoming: Int?,
    @ColumnInfo(name = "total_expence")
    val totalExpence: Float,
    val cashback:Int?,
    @ColumnInfo(name = "card_id")
    val cardID:Int,
    @ColumnInfo(name = "year_id")
    val yearId:Int,
    @ColumnInfo(name = "month_id")
    val monthId:Int,
    @PrimaryKey(autoGenerate = true)
    val id:Int=0
)