package com.example.mysubmission3.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission3.data.DataUser
import com.example.mysubmission3.DetailUser
import com.example.mysubmission3.databinding.ItemUserBinding
import java.util.*
import kotlin.collections.ArrayList

lateinit var _context: Context

class AdapterUser(private var listData: ArrayList<DataUser>) : RecyclerView.Adapter<AdapterUser.ListViewHolder>(),
    Filterable {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val sch = ListViewHolder(binding)
        _context = viewGroup.context
        return sch
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        Glide.with(holder.itemView.context)
            .load(data.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.binding.imgAvatar)
        holder.binding.txtUsername.text = data.username
        holder.binding.txtName.text = data.name
        holder.binding.txtCompany.text = data.company
        holder.binding.txtLocation.text = data.location
        holder.itemView.setOnClickListener {
            val dataUser = DataUser(
                data.username,
                data.name,
                data.avatar,
                data.company,
                data.location,
                data.repository,
                data.followers,
                data.following
            )
            val intentDetail = Intent(_context, DetailUser::class.java)
            intentDetail.putExtra(DetailUser.EXTRA_DATA, dataUser)
            _context.startActivity(intentDetail)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: DataUser)
    }

    override fun getItemCount(): Int = listData.size

    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch = constraint.toString()
                listData = if (charSearch.isEmpty()) {
                    listData
                } else {
                    val resultList = ArrayList<DataUser>()
                    for (row in listData) {
                        if ((row.username.toString().toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT)))
                        ) {
                            resultList.add(
                                DataUser(
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
                filterResults.values = listData
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                listData = results.values as ArrayList<DataUser>
                notifyDataSetChanged()
            }
        }
    }
}