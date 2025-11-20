package com.example.docexpiry

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView

// Lightweight spinner adapter with easy selection callback
class SimpleSpinnerAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
    init { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    fun positionOf(item: String) = getPosition(item)
    var onItemSelected: OnItemSelected? = null
    interface OnItemSelected { fun onSelected(pos: Int, value: String) }
    // AdapterView selection hookup (set externally)
    fun attachListener(spinner: android.widget.Spinner) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                onItemSelected?.onSelected(position, getItem(position)!!)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}

