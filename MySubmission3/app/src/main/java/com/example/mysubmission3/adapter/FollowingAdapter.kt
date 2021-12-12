package com.example.mysubmission3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission3.data.DataUser
import com.example.mysubmission3.databinding.ItemUserBinding

class FollowingAdapter(private var listUser: ArrayList<DataUser>) : RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {

    private lateinit var binding: ItemUserBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        binding = ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val sch = ListViewHolder(binding)
        _context = viewGroup.context
        return sch
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listUser[position]
        Glide.with(holder.itemView.context)
            .load(data.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.binding.imgAvatar)
        holder.binding.txtUsername.text = data.username
        holder.binding.txtName.text = data.name
        holder.binding.txtCompany.text = data.company
        holder.binding.txtLocation.text = data.location
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

}