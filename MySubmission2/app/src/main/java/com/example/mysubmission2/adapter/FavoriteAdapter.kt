package com.example.mysubmission2.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission2.data.Favorite
import com.example.mysubmission2.CustomOnItemClickListener
import com.example.mysubmission2.R
import com.example.mysubmission2.DetailUser
import kotlinx.android.synthetic.main.item_user.view.*
import java.util.ArrayList

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.NoteViewHolder>() {
    var listFavorite = ArrayList<Favorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fav: Favorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(fav.avatar)
                    .apply(RequestOptions().override(250, 250))
                    .into(itemView.img_avatar)
                txt_username.text = fav.username
                txt_name.text = fav.name
                txt_company.text = fav.company.toString()
                txt_location.text = fav.location.toString()
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailUser::class.java)
                                intent.putExtra(DetailUser.EXTRA_POSITION, position)
                                intent.putExtra(DetailUser.EXTRA_NOTE, fav)
                                activity.startActivity(intent)
                            }
                        }
                    )
                )
            }
        }
    }
}