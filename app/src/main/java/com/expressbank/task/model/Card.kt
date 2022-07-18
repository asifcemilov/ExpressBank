package com.expressbank.task.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards_table")
data class Card(

    val cardName: String,
    val cardNumber: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0


) {
    override fun toString(): String {
        return cardName.plus("       ** ${cardNumber.substring(cardNumber.length - 4)}")
    }
}
