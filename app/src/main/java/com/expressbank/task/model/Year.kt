package com.expressbank.task.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "years_table")
data class Year(
    val year:String,
    @PrimaryKey(autoGenerate = true)
    val id:Int=0
){
    override fun toString(): String {
        return year
    }
}
