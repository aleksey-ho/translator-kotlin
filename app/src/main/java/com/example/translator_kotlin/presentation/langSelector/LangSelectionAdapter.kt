package com.example.translator_kotlin.presentation.langSelector

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.translator_kotlin.R
import com.example.translator_kotlin.domain.model.Language

class LangSelectionAdapter(private val context: Context) : BaseAdapter() {
    private val items = ArrayList<Any>()
    private val sectionHeader = ArrayList<Int>()

    fun addItems(items: List<Language>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addSectionHeaderItem(item: String) {
        items.add(item)
        sectionHeader.add(items.size - 1)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (sectionHeader.contains(position)) LangSelectionAdapterType.TYPE_SEPARATOR.value else LangSelectionAdapterType.TYPE_ITEM.value
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (convertView == null)
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null)

        val text1 = convertView!!.findViewById<TextView>(android.R.id.text1)

        when (LangSelectionAdapterType.values()[getItemViewType(position)]) {
            LangSelectionAdapterType.TYPE_ITEM -> text1.text = (items[position] as Language).name
            LangSelectionAdapterType.TYPE_SEPARATOR -> {
                text1.text = (items[position] as String).toUpperCase()
                text1.setTextColor(ContextCompat.getColor(context, R.color.colorListViewTextSeparator))
            }
        }
        return convertView
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    internal enum class LangSelectionAdapterType private constructor(val value: Int) {
        TYPE_ITEM(0), TYPE_SEPARATOR(1)
    }

}
