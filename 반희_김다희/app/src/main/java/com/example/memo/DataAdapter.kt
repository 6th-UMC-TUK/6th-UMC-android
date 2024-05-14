package com.example.memo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(val DataList: ArrayList<Data>): RecyclerView.Adapter<DataAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataAdapter.CustomViewHolder, position: Int) {
        val data = DataList[position]
        holder.memo.text = data.memo
        holder.itemView.setOnClickListener {
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return DataList.size
    }
    private fun removeItem(position: Int) {
        if (position in 0 until DataList.size) {
            DataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memo = itemView.findViewById<TextView>(R.id.tv_memo)
    }
}