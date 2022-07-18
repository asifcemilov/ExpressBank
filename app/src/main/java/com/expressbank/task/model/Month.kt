package com.expressbank.task.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "months_table")
data class Month(
    val month:String,
    @PrimaryKey(autoGenerate = true)
    val id:Int=0
){
    override fun toString(): String {
        return month
    }
}
