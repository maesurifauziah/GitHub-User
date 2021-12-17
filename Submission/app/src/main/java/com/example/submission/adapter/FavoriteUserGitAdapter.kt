package com.example.submission.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission.CustomOnItemClickListener
import com.example.submission.R
import com.example.submission.UserGitDetailActivity
import com.example.submission.data.FavoriteUserGit
import kotlinx.android.synthetic.main.list_user.view.*
import java.util.ArrayList

class FavoriteUserGitAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteUserGitAdapter.FavoriteUserViewHolder>() {
    var listFavoriteUser = ArrayList<FavoriteUserGit>()
    set(listFavorite) {
        if (listFavorite.size > 0) {
            this.listFavoriteUser.clear()
        }
        this.listFavoriteUser.addAll(listFavorite)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val view = LayoutInflater.from(viewgroup.context).inflate(R.layout.list_user, viewgroup, false)
        return FavoriteUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, i: Int) {
        holder.bind(listFavoriteUser[i])
    }

    override fun getItemCount(): Int = this.listFavoriteUser.size

    inner class FavoriteUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: FavoriteUserGit) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(favorite.avatar)
                    .apply(RequestOptions().override(250, 250))
                    .into(itemView.avatar_img)
                text_username.text = favorite.username
                text_name.text = favorite.name
                text_company.text = favorite.company.toString()
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, UserGitDetailActivity::class.java)
                                intent.putExtra(UserGitDetailActivity.EXTRA_NOTE, favorite)
                                intent.putExtra(UserGitDetailActivity.EXTRA_POSITION, position)
                                activity.startActivity(intent)
                            }
                        }
                    )
                )
            }
        }
    }


}