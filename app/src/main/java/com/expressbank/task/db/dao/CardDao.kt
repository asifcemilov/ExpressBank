package com.expressbank.task.db.dao

import androidx.room.*
import com.expressbank.task.model.Card
import kotlinx.coroutines.flow.Flow
@Dao
interface CardDao {

    @Query("SELECT * FROM cards_table")
    fun getCardsAsFlow(): Flow<List<Card>>

    @Query("SELECT * FROM cards_table")
    fun getCards(): List<Card>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card)

    @Update
    suspend fun update(card: Card)

    @Delete
    suspend fun delete(card: Card)

}