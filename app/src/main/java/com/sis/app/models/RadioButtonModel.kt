package com.sis.app.models

/**
 * tidak dipakai
 */
class RadioButtonModel(val title:String)

//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.survey.app.R
//
//class ParentAdapter(private val parents: List<ParentModel>) : RecyclerView.Adapter<ParentAdapter.ViewHolder>() {
//    private val viewPool = RecyclerView.RecycledViewPool()
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.parent_recycler, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun getItemCount(): Int {
//        return parents.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val parent = parents[position]
//        holder.textView.text = parent.title
//        val childLayoutManager = LinearLayoutManager(holder.recyclerView.context, LinearLayout.HORIZONTAL, false)
//        childLayoutManager.initialPrefetchItemCount = 4
//        holder.recyclerView.apply {
//            layoutManager = childLayoutManager
//            adapter = ChildAdapter(parent.children)
//            setRecycledViewPool(viewPool)
//        }
//
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val recyclerView: RecyclerView = itemView.rv_child
//        val textView: TextView = itemView.textView
//    }
//}
//
//class ChildAdapter(private val children: List<ChildModel>) : RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//        val v = LayoutInflater.from(parent.context)
//            .inflate(R.layout.child_recycler, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun getItemCount(): Int {
//        return children.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val child = children[position]
//        holder.imageView.setImageResource(child.image)
//        holder.textView.text = child.title
//    }
//
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textView: TextView = itemView.child_textView
//        val imageView: ImageView = itemView.child_imageView
//    }
//}