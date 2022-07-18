package com.expressbank.task.model.databaseview

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT " +
            " expenses_table.id AS id, " +
            " budgets_table.id AS budgetId, " +
            " categories_table.id AS categoryId, " +
            " categories_table.name AS categoryName, " +
            " categories_table.color AS categoryColor," +
            " categories_table.drawable AS categoryDrawable," +
            " ((expenses_table.expense*100)/budgets_table.total_expence) AS expansePercent ," +
            "expenses_table.expense" +

            " FROM expenses_table  " +
            "INNER JOIN categories_table ON categories_table.id = expenses_table.category_id " +
            "INNER JOIN budgets_table ON budgets_table.id = expenses_table.budget_id"
)

data class CategoryWithExpance(
    val id: Int,
    val budgetId: Int,
    val categoryId: Int,
    val categoryName: String,
    val categoryColor: Int,
    val categoryDrawable: Int,
    val expense: Int,
    val expansePercent: Float
)