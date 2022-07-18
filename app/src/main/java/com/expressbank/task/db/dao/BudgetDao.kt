package com.expressbank.task.db.dao

import androidx.room.*
import com.expressbank.task.model.Budget
import kotlinx.coroutines.flow.Flow
@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets_table WHERE card_id = :cardId AND year_id=:yearId AND month_id=:monthId")
    fun getBudgetWithConditionAsFlow(cardId:Int, yearId:Int, monthId:Int): Flow<Budget>

    @Query("SELECT * FROM budgets_table WHERE card_id = :cardId AND year_id=:yearId AND month_id=:monthId")
    fun getBudgetWithCondition(cardId:Int, yearId:Int, monthId:Int): Budget

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)

    @Update
    suspend fun update(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

}