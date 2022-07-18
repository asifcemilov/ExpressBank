package com.expressbank.task.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses_history_table")
data class ExpenseHistory(
    @ColumnInfo(name = "expance_id")
    val expanceId:Int,
    val cost:Float,
    @ColumnInfo(name = "cost_detail")
    val costDetail:String,
    @ColumnInfo(name = "cost_date")
    val costDate:String,

    @PrimaryKey(autoGenerate = true)
    val id:Int=0
)
