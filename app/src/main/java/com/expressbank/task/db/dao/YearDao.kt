package com.expressbank.task.db.dao

import androidx.room.*
import com.expressbank.task.model.Year
import kotlinx.coroutines.flow.Flow
@Dao
interface YearDao {

    @Query("SELECT * FROM years_table")
    fun getYearsAsFlow(): Flow<List<Year>>

    @Query("SELECT * FROM years_table")
    fun getYears(): List<Year>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(year: Year)

    @Update
    suspend fun update(year: Year)

    @Delete
    suspend fun delete(year: Year)

}