package com.expressbank.task.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.expressbank.task.databinding.DropdownItemViewBinding
import com.expressbank.task.model.Month

class MonthDropdownAdapter(context: Context, private val items: List<Month>) :
    ArrayAdapter<Month>(context, android.R.layout.simple_list_item_1, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            DropdownItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.dropdownItemText.text = items[position].month
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            DropdownItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.dropdownItemText.text = items[position].month
        return binding.root
    }




}