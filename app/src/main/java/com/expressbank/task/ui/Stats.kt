package com.expressbank.task.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.expressbank.task.R
import com.expressbank.task.databinding.FragmentStatsBinding
import com.expressbank.task.model.Card
import com.expressbank.task.model.Month
import com.expressbank.task.model.Year
import com.expressbank.task.model.databaseview.CategoryWithExpance
import com.expressbank.task.ui.adapter.CardsDropdownAdapter
import com.expressbank.task.ui.adapter.CategoryRecyclerAdapter
import com.expressbank.task.ui.adapter.MonthDropdownAdapter
import com.expressbank.task.ui.adapter.YearsDropdownAdapter
import com.expressbank.task.viewmodel.StatsViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Stats : Fragment(),
    CategoryRecyclerAdapter.OnItemClickListener,
    OnChartValueSelectedListener {
    private val TAG = "Stats"

    private val viewModel: StatsViewModel by viewModels()

    private lateinit var binding: FragmentStatsBinding

    private lateinit var categoryList: ArrayList<CategoryWithExpance>
    private lateinit var cardList: ArrayList<Card>
    private lateinit var yearList: ArrayList<Year>
    private lateinit var monthList: ArrayList<Month>


    private lateinit var categoryAdapter: CategoryRecyclerAdapter
    private lateinit var cardAdapter: CardsDropdownAdapter
    private lateinit var yearAdapter: YearsDropdownAdapter
    private lateinit var monthAdapter: MonthDropdownAdapter

    private lateinit var pieEntry: PieEntry
    private var cardId = 1
    private var yearId = 1
    private var monthId = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initilize()
        setSpinners()
        setSpinnersItemClick()
        setBudget(1, 1, 1)


    }

    private fun initilize() {

        categoryList = ArrayList()
        cardList = ArrayList()
        yearList = ArrayList()
        monthList = ArrayList()

        categoryAdapter = CategoryRecyclerAdapter(this)
        cardAdapter = CardsDropdownAdapter(requireContext(), cardList)
        yearAdapter = YearsDropdownAdapter(requireContext(), yearList)
        monthAdapter = MonthDropdownAdapter(requireContext(), monthList)


        binding.apply {
            categoryRecycler.layoutManager = LinearLayoutManager(requireContext())
            categoryRecycler.adapter = categoryAdapter

            ddCards.setAdapter(cardAdapter)
            ddYears.setAdapter(yearAdapter)
            ddMonths.setAdapter(monthAdapter)
        }

        //PieChart
        binding.pieChart.apply {
            isDrawHoleEnabled = true
            description.isEnabled = false
            legend.isEnabled = false
            holeRadius = 84f
            setHoleColor(ContextCompat.getColor(requireContext(), R.color.transparent1))
            isRotationEnabled = false
            setNoDataText("")
            setOnChartValueSelectedListener(this@Stats)
        }

    }


    private fun setSpinners() {
        lifecycle.coroutineScope.launch {

            viewModel.getYears().collect {
                if (it.size == 3) {
                    binding.ddYears.setText(it[0].toString(), false)
                    yearList.clear()
                    yearList.addAll(it)
                    yearAdapter.notifyDataSetChanged()
                }
            }
        }
        lifecycle.coroutineScope.launch {
            viewModel.getMonths().collect {
                if (it.size == 12) {
                    binding.ddMonths.setText(it[0].toString(), false)
                    monthList.clear()
                    monthList.addAll(it)
                    monthAdapter.notifyDataSetChanged()
                }
            }
        }
        lifecycle.coroutineScope.launch {
            viewModel.getCards().collect {
                if (it.size == 3) {
                    binding.ddCards.setText(it[0].toString(), false)
                    cardList.clear()
                    cardList.addAll(it)
                    cardAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setSpinnersItemClick() {
        binding.apply {
            ddCards.setOnItemClickListener { adapterView, view, i, l ->
                val card = adapterView.adapter.getItem(i) as Card
                cardId = card.id
                resetBudget()
            }
            ddYears.setOnItemClickListener { adapterView, view, i, l ->
                val year = adapterView.adapter.getItem(i) as Year
                yearId = year.id
                resetBudget()
            }
            ddMonths.setOnItemClickListener { adapterView, view, i, l ->
                val month = adapterView.adapter.getItem(i) as Month
                monthId = month.id
                resetBudget()
            }
        }
    }

    private fun resetBudget() {
        setBudget(cardId, yearId, monthId)
    }


    private fun setBudget(cardId: Int, yearId: Int, monthId: Int) {
        lifecycle.coroutineScope.launch {
            binding.apply {
                viewModel.getBudget(cardId, yearId, monthId).cancellable().collect {
                    if (it != null) {
                        expencesValue.text = it.totalExpence.toInt().toString()
                        incomingsValue.text = it.incoming.toString()
                        cashbackValue.text = it.cashback.toString()
                        setRecycler(it.id)
                        cancel()
                    }
                }
            }
        }
    }


    private fun setRecycler(budgetId: Int) {
        lifecycle.coroutineScope.launch {
            viewModel.getCategoriesWithExpences(budgetId).cancellable().collect { list ->
                if (list != null && list.size == 23) {
                    categoryList.clear()
                    categoryList.addAll(list as ArrayList<CategoryWithExpance>)
                    val job = lifecycle.coroutineScope.launch {
                        categoryAdapter.submitList(list)
                        delay(20)
                    }
                    job.join()
                    loadPieChartData()
                    binding.pieChartHoleView.root.visibility = View.VISIBLE
                    viewModel.deleteAllFromExpanseHistory()
                    list.forEach {
                        viewModel.addExpanceHistory(
                            it.categoryName,
                            it.id,
                            it.expense,
                            yearList[0],
                            monthList[0]
                        )
                    }
                    binding.pieChart.highlightValue(0f, 0, true)
                    binding.pieChart.invalidate()
                    cancel()
                }
            }
        }
    }


    private fun loadPieChartData() {
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()
        categoryList.forEach {
            entries.add(PieEntry(it.expansePercent, it.categoryId))
            colors.add(ContextCompat.getColor(requireContext(), it.categoryColor))
        }

        val pieDataSet = PieDataSet(entries, "pie")
        pieDataSet.colors = colors
        pieDataSet.sliceSpace = 3f
        pieDataSet.selectionShift = 15f
        pieDataSet.isHighlightEnabled = true

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(false)

        binding.pieChart.data = pieData
        binding.pieChart.invalidate()
        binding.pieChart.animateY(1000, Easing.EaseInCirc)
    }


    private fun setPieChartHoleView(category: CategoryWithExpance) {
        binding.pieChartHoleView.apply {
            holeTextLayout.visibility = View.VISIBLE
            holeCategoryName.text =
                category.categoryName.plus(" ").plus(String.format("%.1f", category.expansePercent))
                    .plus("%")
            holeExpanse.text = category.expense.toString()
            holeExpanse.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    category.categoryColor
                )
            )
            holeIcon.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        category.categoryDrawable
                    )
                )
                setColorFilter(ContextCompat.getColor(requireContext(), category.categoryColor))
                alpha = 0.13f
            }

            val date = monthList[0].month + " " + yearList[0].year

            viewStatement.setOnClickListener {
                BottomSheetController(
                    requireContext(),
                    category, lifecycle, viewLifecycleOwner, viewModel, date
                )
            }
        }
    }


    private fun PieEntry.getCategoryData(): CategoryWithExpance? {
        var categoryWithExpance: CategoryWithExpance? = null
        for (i in categoryList) {
            if (i.categoryId.toString() == this.data.toString()) {
                categoryWithExpance = i
                break
            }
        }
        return categoryWithExpance
    }

    override fun onItemClick(task: CategoryWithExpance) {
        var index = -1
        for (i in 0 until categoryList.size) {
            if (categoryList[i].categoryId == task.categoryId && categoryList[i].expense != 0) {
                index = 0
                for (j in 0..i) {
                    if (categoryList[i].expense == 0)
                        index++
                }
                index = i - index
                break
            }
        }
        binding.nestedScrollView.smoothScrollTo(
            binding.nestedScrollView.getChildAt(0).height,
            0,
            2000
        )
        if (index >= 0)
            binding.pieChart.highlightValue(index.toFloat(), 0, false)
        else
            binding.pieChart.highlightValue(null)
        setPieChartHoleView(task)
        binding.pieChart.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        binding.pieChartHoleView.holeTextLayout.visibility = View.VISIBLE
        pieEntry = e as PieEntry
        pieEntry.getCategoryData()?.let { setPieChartHoleView(it) }

    }

    override fun onNothingSelected() {
        binding.pieChartHoleView.apply {
            holeTextLayout.visibility = View.GONE
            holeIcon.clearColorFilter()
            holeIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.mipmap.expressbank_logo_foreground
                )
            )
            holeIcon.alpha = 1f
        }
    }


}


