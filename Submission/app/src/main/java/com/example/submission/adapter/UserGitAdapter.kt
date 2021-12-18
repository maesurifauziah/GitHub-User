package com.example.submission.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission.UserGitDetailActivity
import com.example.submission.data.UserGit
import com.example.submission.databinding.ListUserBinding
import java.util.*
import kotlin.collections.ArrayList

lateinit var v_context: Context

class UserGitAdapter(private var listUserGit: ArrayList<UserGit>) : RecyclerView.Adapter<UserGitAdapter.ListDataHolder>(),
    Filterable {

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListDataHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        v_context = viewGroup.context
        return ListDataHolder(binding)
    }

    override fun getItemCount(): Int {
        return listUserGit.size
    }

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
        holder.itemView.setOnClickListener {
            val dataUser = UserGit(
                data.username,
                data.name,
                data.avatar,
                data.company,
                data.location,
                data.repository,
                data.followers,
                data.following
            )
            val intentDetail = Intent(v_context, UserGitDetailActivity::class.java)
            intentDetail.putExtra(UserGitDetailActivity.EXTRA_FAVORITE, dataUser)
            intentDetail.putExtra(UserGitDetailActivity.EXTRA_DATA, dataUser)
            v_context.startActivity(intentDetail)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: UserGit)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch = constraint.toString()
                listUserGit = if (charSearch.isEmpty()) {
                    listUserGit
                } else {
                    val resultList = ArrayList<UserGit>()
                    for (row in listUserGit) {
                        if ((row.username.toString().lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT)))
                        ) {
                            resultList.add(
                                UserGit(
                                    row.username,
                                    row.name,
                                    row.avatar,
                                    row.company,
                                    row.location,
                                    row.repository,
                                    row.followers,
                                    row.following
                                )
                            )
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = listUserGit
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                listUserGit = results.values as ArrayList<UserGit>
                notifyDataSetChanged()
            }
        }
    }


}