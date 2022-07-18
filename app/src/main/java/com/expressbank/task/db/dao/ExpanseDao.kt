package com.expressbank.task.db.dao

import androidx.room.*
import com.expressbank.task.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpanseDao {

    @Query("SELECT * FROM expenses_table")
    fun getExpencesAsFlow(): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

}