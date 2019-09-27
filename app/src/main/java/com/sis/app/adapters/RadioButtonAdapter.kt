package com.sis.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import com.sis.app.R

class RadioButtonAdapter(val context: Context, val list: ArrayList<String>) : BaseAdapter() {

    private val inflater: LayoutInflater
    var selectedPosition: Int = -1

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = null
        var holder = ViewHolder()

        if (convertView == null) {
            view = inflater.inflate(R.layout.abc_list_menu_item_radio, null, true)
            holder.radioButton = view.findViewById(R.id.radio)

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        holder.radioButton?.text = list[pos]
        holder.radioButton?.isChecked = (pos == selectedPosition)
        holder.radioButton?.tag = pos

        holder.radioButton?.setOnClickListener {
            selectedPosition = view?.tag as Int
            notifyDataSetChanged()
        }
        return view
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    class ViewHolder {
        var radioButton: RadioButton? = null
    }

}