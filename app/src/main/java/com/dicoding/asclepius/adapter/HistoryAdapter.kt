package com.dicoding.asclepius.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.view.history.HistoryItemActivity
import com.dicoding.asclepius.view.history.HistoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HistoryAdapter(
    private val viewModel: HistoryViewModel
) : ListAdapter<History, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)

        holder.bind(user)
        holder.itemView.setOnClickListener {
            val intentHistoryItem = Intent(holder.itemView.context, HistoryItemActivity::class.java)
            intentHistoryItem.putExtra(HistoryItemActivity.HISTORY_DESCRIPTION, user.description)
            intentHistoryItem.putExtra(HistoryItemActivity.HISTORY_IMAGE, user.avatarUri)
            intentHistoryItem.putExtra(HistoryItemActivity.HISTORY_DATE, user.date)
            holder.itemView.context.startActivity(intentHistoryItem)
        }

        holder.deleteButton.setOnClickListener {
            viewModel.delete(user)
        }
    }

    class MyViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var deleteButton: FloatingActionButton = binding.deleteHistory
        fun bind(user: History) {
            binding.tvItemDescription.text = user.description
            binding.tvItemDate.text = user.date
            Glide.with(itemView.context)
                .load(user.avatarUri)
                .into(binding.imgItemPhoto)
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}