package com.expressbank.task.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.expressbank.task.databinding.CardsItemViewBinding
import com.expressbank.task.model.Card
import java.util.*


class CardsDropdownAdapter(context: Context, private val items: ArrayList<Card>) :
    ArrayAdapter<Card>(context, 0, items) {

    override fun getFilter(): Filter {
        return countryFilter
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            CardsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.dropdownItemText1.text = items[position].cardName
        val cardNumber = items[position].cardNumber
        binding.dropdownItemText2.text = "**  ".plus(cardNumber.substring(cardNumber.length-4))


        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            CardsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.dropdownItemText1.text = items[position].cardName
        val cardNumber = items[position].cardNumber
        binding.dropdownItemText2.text = "**  ".plus(cardNumber.substring(cardNumber.length-4))
            binding.root.setPadding(0,16,16,16)

        return binding.root
    }

    private val countryFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            val suggestions: MutableList<Card> = ArrayList<Card>()
            if (constraint.isEmpty()) {
                suggestions.addAll(items)
            } else {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in items) {
                    if (item.cardName.lowercase().contains(filterPattern)) {
                        suggestions.add(item)
                    }
                }
            }
            results.values = suggestions
            results.count = suggestions.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            clear()
            addAll(results.values as ArrayList<Card>)
            notifyDataSetInvalidated()
        }

        override fun convertResultToString(resultValue: Any): CharSequence {
            val card = (resultValue as Card)
            return card.cardName.plus("       ** ${card.cardNumber.substring(card.cardNumber.length - 4)}")
        }
    }





}