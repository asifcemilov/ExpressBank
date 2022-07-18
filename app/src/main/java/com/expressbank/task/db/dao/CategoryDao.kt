package com.expressbank.task.db.dao

import androidx.room.*
import com.expressbank.task.model.Category
import com.expressbank.task.model.databaseview.CategoryWithExpance
import kotlinx.coroutines.flow.Flow
@Dao
interface CategoryDao {


    @Query("SELECT * FROM categories_table")
    fun getCategories(): List<Category>

    @Query("SELECT * FROM categorywithexpance WHERE budgetId=:budgetId")
    fun getCategoriesWithExpences(budgetId :Int): Flow<List<CategoryWithExpance>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

}