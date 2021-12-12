package com.example.mysubmission1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListDeveloperAdapter(private val listdeveloper: ArrayList<Developer>) : RecyclerView.Adapter<ListDeveloperAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_developer, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username,name,company,location,repository,followers,following,avatar) = listdeveloper[position]
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.imgAvatar)
        holder.tvName.text = name
        holder.tvUsername.text = username
        holder.tvCompany.text = company
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listdeveloper[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int = listdeveloper.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_item_avatar)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_item_username)
        var tvCompany: TextView = itemView.findViewById(R.id.tv_item_company)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Developer)
    }

}


