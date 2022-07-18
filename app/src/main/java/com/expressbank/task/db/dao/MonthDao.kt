package com.expressbank.task.db.dao

import androidx.room.*
import com.expressbank.task.model.Month
import kotlinx.coroutines.flow.Flow
@Dao
interface MonthDao {

    @Query("SELECT * FROM months_table")
    fun getMonthsAsFlow(): Flow<List<Month>>

    @Query("SELECT * FROM months_table")
    fun getMonths(): List<Month>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(month: Month)

    @Update
    suspend fun update(month: Month)

    @Delete
    suspend fun delete(month: Month)

}