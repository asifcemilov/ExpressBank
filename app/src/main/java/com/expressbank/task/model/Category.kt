package com.expressbank.task.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "categories_table")
data class Category(
    val name: String,
    val color: Int,
    val drawable: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int=0) {
}