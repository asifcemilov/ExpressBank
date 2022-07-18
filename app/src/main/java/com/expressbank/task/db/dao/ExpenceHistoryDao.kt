package com.expressbank.task.db.dao

import androidx.room.*
import com.expressbank.task.model.ExpenseHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenceHistoryDao {

    @Query("SELECT * FROM expenses_history_table WHERE expance_id=:expeceId AND  cost LIKE '%' || :searchQuery ||  '%' ")
    fun getExpensesHistoryAsFlow(expeceId: Int, searchQuery: String): Flow<List<ExpenseHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseHistory: ExpenseHistory)

    @Update
    suspend fun update(expenseHistory: ExpenseHistory)

    @Delete
    suspend fun delete(expenseHistory: ExpenseHistory)

    @Query("DELETE FROM expenses_history_table")
    fun deleteAll()

}