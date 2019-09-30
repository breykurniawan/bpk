package com.sis.app.adapters.Spinner

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.sis.app.R
import com.sis.app.models.identity.Stakeholder

//class SAdapter(var list: List<Stakeholder>) : BaseAdapter() {
//    override fun getView(position: Int, view: View, parent: ViewGroup): View {
//        //TODO
////        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
////            view = getLayoutInflater().inflate(
////                R.layout.
////                toolbar_spinner_item_actionbar, parent, false);
////            view.setTag("NON_DROPDOWN");
////        }
////        TextView textView = (TextView) view.findViewById(android.R.id.text1);
////        textView.setText(getTitle(position));
////        return view;
//    }
//
//    override fun getItem(pos: Int): Any = list.get(pos)
//
//    override fun getItemId(pos: Int): Long = list.get(pos).id_pemangku_kepentingan.toLong()
//
//    override fun getCount() = list.size
//
//
//}