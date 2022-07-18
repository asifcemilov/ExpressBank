package com.expressbank.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.expressbank.task.db.dao.*
import com.expressbank.task.model.*
import com.expressbank.task.model.databaseview.CategoryWithExpance
import com.expressbank.task.util.RandomGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private var categoryDao: CategoryDao,
    private var cardDao: CardDao,
    private var yearDao: YearDao,
    private var monthDao: MonthDao,
    private var budgetDao: BudgetDao,
    private var expenceHistoryDao: ExpenceHistoryDao
) : ViewModel() {

    fun getCards(): Flow<List<Card>> {
        return cardDao.getCardsAsFlow()
    }

    fun getYears(): Flow<List<Year>> {
        return yearDao.getYearsAsFlow()
    }

    fun getMonths(): Flow<List<Month>> {
        return monthDao.getMonthsAsFlow()
    }

    fun getBudget(cardId: Int, yearId: Int, monthId: Int): Flow<Budget> {
        return budgetDao.getBudgetWithConditionAsFlow(cardId, yearId, monthId)
    }

    fun getCategoriesWithExpences(budgetId: Int): Flow<List<CategoryWithExpance>> {
        return categoryDao.getCategoriesWithExpences(budgetId)
    }

    var expanseId: Int = 0

    val searchQuery = MutableStateFlow("")

    private val tasksFlow = searchQuery.flatMapLatest {
        expenceHistoryDao.getExpensesHistoryAsFlow(expanseId, it)
    }

    val tasks = tasksFlow.asLiveData()

    fun deleteAllFromExpanseHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            expenceHistoryDao.deleteAll()
        }
    }

    suspend fun addExpanceHistory(
        categoryName: String,
        expanseId: Int,
        expense: Int,
        year: Year,
        month: Month
    ) {

        val randCosts = if (expense != 0) {
            RandomGenerator.nRandomsThatSumToLimit(8, expense, 0)
        } else {
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0).toList()
        }

        val randDayIterator = RandomGenerator.randomDay().listIterator()
        if (randDayIterator.hasNext())
            randCosts.forEach {
                viewModelScope.launch(Dispatchers.IO) {
                    expenceHistoryDao.insert(
                        ExpenseHistory(
                            expanseId,
                            it.toFloat(),
                            " $categoryName Cost",
                            (10..23).random().toString()
                                .plus(":")
                                .plus((10..59).random())
                                .plus(
                                    "  ${randDayIterator.next()}." +
                                            (if (month.id < 10) "0${month.id}."
                                            else "0${month.id}.") +
                                            year.year
                                )
                        )
                    )
                    if (month.id < 10)
                        "0${month.id}"
                    else "0${month.id}"
                }

            }
    }


}

