package com.aman.urlbrowser.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aman.urlbrowser.data.local.UrlHistoryEntity
import com.aman.urlbrowser.databinding.ItemHistoryBinding
import com.aman.urlbrowser.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter : ListAdapter<UrlHistoryEntity, HistoryAdapter.HistoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(urlHistory: UrlHistoryEntity) {
            binding.tvUrl.text = urlHistory.url
            binding.tvTimestamp.text = formatTimestamp(urlHistory.timestamp)
        }

        private fun formatTimestamp(timestamp: Long): String {
            val date = Date(timestamp)
            val formatter = SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault())
            return formatter.format(date)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UrlHistoryEntity>() {
        override fun areItemsTheSame(oldItem: UrlHistoryEntity, newItem: UrlHistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UrlHistoryEntity, newItem: UrlHistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}
