package com.expressbank.task.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.expressbank.task.R
import com.expressbank.task.databinding.BottomSheetDialogBinding
import com.expressbank.task.model.databaseview.CategoryWithExpance
import com.expressbank.task.ui.adapter.ExpanseHsitoryRecyclerAdapter
import com.expressbank.task.util.onQueryTextChanged
import com.expressbank.task.viewmodel.StatsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class BottomSheetController(
    val context: Context,
    val categoryWithExpance: CategoryWithExpance,
    private val lifecycle: Lifecycle,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: StatsViewModel,
    private val date: String
) : DialogInterface.OnShowListener,
    DialogInterface.OnCancelListener {

    private var bindingBottomSheet: BottomSheetDialogBinding
    private var dialog: BottomSheetDialog
    private var expanseHistoryAdapter: ExpanseHsitoryRecyclerAdapter


    init {
        bindingBottomSheet =
            BottomSheetDialogBinding.inflate(LayoutInflater.from(context), null, false)
        expanseHistoryAdapter = ExpanseHsitoryRecyclerAdapter()
        dialog = BottomSheetDialog(context, R.style.BottomSheetMainStyle)
        dialog.setContentView(bindingBottomSheet.root)
        dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN
        dialog.setOnShowListener(this)
        dialog.setOnCancelListener(this)

        bindingBottomSheet.apply {
            historyRecycler.layoutManager = LinearLayoutManager(context)
            historyRecycler.adapter = expanseHistoryAdapter

        }

        setDialog()
    }

    @SuppressLint("SetTextI18n")
    private fun setDialog() {
        expanseHistoryAdapter.setCategoryWithExpanse(categoryWithExpance)
        viewModel.expanseId = categoryWithExpance.id
        lifecycle.coroutineScope.launch {
            viewModel.tasks.observe(lifecycleOwner) {
                Log.i("aaa", "setDialog: $it")
                expanseHistoryAdapter.submitList(it)
            }
        }
        bindingBottomSheet.apply {
            cornerIcon.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        categoryWithExpance.categoryDrawable
                    )
                )
                setColorFilter(ContextCompat.getColor(context, categoryWithExpance.categoryColor))
            }
            categoryName.text = categoryWithExpance.categoryName
            expensesDate.text = "Expense for $date"
            expencesCost.text = categoryWithExpance.expense.toString() + " AZN"
            expencesPercent.text =
                String.format("%.1f", categoryWithExpance.expansePercent) + "% of all"

            val layerDrawable =
                ContextCompat.getDrawable(context, R.drawable.bottomsheet_top_icon) as LayerDrawable
            val gradientDrawable =
                layerDrawable.findDrawableByLayerId(R.id.roundCorner) as GradientDrawable
            gradientDrawable.mutate()
            gradientDrawable.setStroke(
                9,
                ContextCompat.getColor(context, categoryWithExpance.categoryColor)
            )

            cornerIconLayout.background = layerDrawable

        }
        bindingBottomSheet.searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        bindingBottomSheet.searchView.setQuery("1", true)
        bindingBottomSheet.searchView.setQuery("", true)

        dialog.show()

    }


    override fun onShow(dialog: DialogInterface?) {
    }

    override fun onCancel(dialog: DialogInterface?) {
    }


}