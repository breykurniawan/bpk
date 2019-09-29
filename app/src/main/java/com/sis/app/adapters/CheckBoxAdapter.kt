package com.sis.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import com.sis.app.R
import com.sis.app.models.surveyQuestion.CheckboxModel

class CheckBoxAdapter(val context: Context, val list: ArrayList<CheckboxModel>) : BaseAdapter() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }


    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View?
        var holder = ViewHolder()

        if (convertView == null) {
            view = inflater.inflate(R.layout.abc_list_menu_item_checkbox, null, true)
            holder.checkBox = view.findViewById(R.id.checkbox)

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        holder.checkBox?.text = list[pos].title
        holder.checkBox?.isChecked = list[pos].checked
        holder.checkBox?.setTag(1, convertView)
        holder.checkBox?.tag = pos

        holder.checkBox?.setOnClickListener {
//            val tmpView = holder.checkBox?.getTag(1) as View

            list[pos].checked != list[pos].checked
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
        var checkBox: CheckBox? = null
    }
}

//class CheckBoxAdapter(val list: List<CheckboxModel>) : RecyclerView.Adapter<CheckBoxAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CheckBoxAdapter.ViewHolder {
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.abc_list_menu_item_checkbox, parent, true))
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onBindViewHolder(holder: CheckBoxAdapter.ViewHolder, position: Int) {
//        holder.bind(list[position], position)
//    }
//
//    class ViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
//        var radioButton: CheckBox? = null
//
//        init {
//            radioButton = view.findViewById(R.id.checkbox)
//        }
//
//        fun bind(model : CheckboxModel, position: Int) {
//            radioButton?.setText(model.title)
//            radioButton?.isChecked = model.checked
//            radioButton?.tag = position
//            radioButton?.setOnClickListener{
//                model.checked = !model.checked
//                Toast.makeText(view.context, "checkbox $position diklik", Toast.LENGTH_LONG).show()
//                Log.d(CheckBoxAdapter::class.java.simpleName, "checkbox $position diklik")
//
//            }
//        }
//    }
//
//}