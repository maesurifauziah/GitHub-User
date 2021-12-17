package com.example.submission.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission.data.UserGit
import com.example.submission.databinding.ListUserBinding

class FollowersUserAdapter (private var listUserGit: ArrayList<UserGit>) : RecyclerView.Adapter<FollowersUserAdapter.ListDataHolder>() {

    private lateinit var binding: ListUserBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListDataHolder {
        binding = ListUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        v_context = viewGroup.context
        return ListDataHolder(binding)
    }


    override fun getItemCount(): Int = listUserGit.size

    class ListDataHolder(var binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListDataHolder, i: Int) {
        val data = listUserGit[i]
        Glide.with(holder.itemView.context)
            .load(data.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.binding.avatarImg)
        holder.binding.textUsername.text = data.username
        holder.binding.textName.text = data.name
        holder.binding.textCompany.text = data.company
    }
}