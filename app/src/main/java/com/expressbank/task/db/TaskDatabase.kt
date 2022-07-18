package com.expressbank.task.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.expressbank.task.R
import com.expressbank.task.db.dao.*
import com.expressbank.task.di.ApplicationScope
import com.expressbank.task.model.*
import com.expressbank.task.model.databaseview.CategoryWithExpance
import com.expressbank.task.util.RandomGenerator.Companion.nRandomsThatSumToLimit
import com.expressbank.task.util.RandomGenerator.Companion.randomBudgets
import com.expressbank.task.util.toList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [
        Card::class,
        Year::class,
        Month::class,
        Category::class,
        Budget::class,
        Expense::class,
        ExpenseHistory::class],
    views = [CategoryWithExpance::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase :RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun cardDao(): CardDao
    abstract fun yearDao(): YearDao
    abstract fun monthDao(): MonthDao
    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpanseDao
    abstract fun expenseHistoryDao(): ExpenceHistoryDao

    class Callback @Inject constructor(
        @ApplicationContext
        private val context: Context,
        private val database: Provider<TaskDatabase>,
        @ApplicationScope
        private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        private lateinit var categories: List<String>
        private lateinit var categoryColors: List<Int>
        private lateinit var categoryDrawables: List<Int>
        private lateinit var cards: List<String>
        private lateinit var cardNumbers: List<String>
        private lateinit var years: List<String>
        private lateinit var months: List<String>

        private lateinit var categoryDao: CategoryDao
        private lateinit var cardDao: CardDao
        private lateinit var yearDao: YearDao
        private lateinit var monthDao: MonthDao
        private lateinit var budgetDao: BudgetDao
        private lateinit var expanseDao: ExpanseDao
        private lateinit var expenceHistoryDao: ExpenceHistoryDao


        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            initilizeVarables()

            applicationScope.launch(Dispatchers.IO) {
                addCards()
                addYears()
                addMonths()
                addCategories()
                addBudgesAndExpencesAndHistory()
            }
        }


        private fun initilizeVarables() {
            val resources = context.applicationContext.resources

            categories = resources.getStringArray(R.array.categories).toList()
            categoryColors = resources.obtainTypedArray(R.array.catagoriesColor).toList()
            categoryDrawables =resources.obtainTypedArray(R.array.catagoriesDrawable).toList()
            cards = context.applicationContext.resources.getStringArray(R.array.cards).toList()
            cardNumbers = resources.getStringArray(R.array.cardNumbers).toList()
            years = resources.getStringArray(R.array.years).toList()
            months = resources.getStringArray(R.array.months).toList()

            categoryDao = database.get().categoryDao()
            cardDao = database.get().cardDao()
            yearDao = database.get().yearDao()
            monthDao = database.get().monthDao()
            budgetDao = database.get().budgetDao()
            expanseDao = database.get().expenseDao()
            expenceHistoryDao = database.get().expenseHistoryDao()

        }

        private suspend fun addCards() {
            for (i in 0..2)
                cardDao.insert(Card(cards[i],cardNumbers[i]))
        }

        private suspend fun addYears() {
            years.forEach {
                yearDao.insert(Year(it))
            }
        }

        private suspend fun addMonths() {
            months.forEach {
                monthDao.insert(Month(it))
            }
        }

        private suspend fun addCategories() {
            for (i in 0..22)
                categoryDao.insert(Category(categories[i], categoryColors[i], categoryDrawables[i]))
        }

        private suspend fun addBudgesAndExpencesAndHistory() {
            val cardsList = cardDao.getCards()
            val yearsList = yearDao.getYears()
            val monthsList = monthDao.getMonths()

            cardsList.forEach { card ->

                yearsList.forEach { year ->

                    monthsList.forEach { month ->
                        applicationScope.launch(Dispatchers.IO) {
                            addBudget(card, year, month)
                        }
                    }
                }
            }
        }

        private suspend fun addBudget(card: Card, year: Year, month: Month) {
            val randBudgets = randomBudgets()
            budgetDao.insert(
                Budget(
                    randBudgets[0],
                    randBudgets[1].toFloat(),
                    randBudgets[2],
                    card.id,
                    year.id,
                    month.id
                )
            )
            val budget = budgetDao.getBudgetWithCondition(card.id, year.id, month.id)
            applicationScope.launch(Dispatchers.IO) {
                addExpances(budget)
            }
        }

        private suspend fun addExpances(budget: Budget) {
            val categoryList = categoryDao.getCategories()
            val randExpences = nRandomsThatSumToLimit(
                categoryList.size,
                budget.totalExpence.toInt(),
                (5..10).random()
            )
            val randExpenseIterator = randExpences.listIterator()

            if (randExpenseIterator.hasNext())
                categoryList.forEach { category ->
                    expanseDao.insert(
                        Expense(
                            randExpenseIterator.next().toFloat(),
                            budget.id,
                            category.id
                        )
                    )
                }
        }
    }
}

