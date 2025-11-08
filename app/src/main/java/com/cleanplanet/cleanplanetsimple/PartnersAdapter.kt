package com.cleanplanet.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PartnersAdapter(private val onPartnerClick: (Partner) -> Unit) :
    ListAdapter<Partner, PartnersAdapter.PartnerViewHolder>(PartnerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_partner, parent, false)
        return PartnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartnerViewHolder, position: Int) {
        val partner = getItem(position)
        holder.bind(partner)
        holder.itemView.setOnClickListener { onPartnerClick(partner) }
    }

    class PartnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textPartnerName)
        private val textType: TextView = itemView.findViewById(R.id.textPartnerType)
        private val textDirector: TextView = itemView.findViewById(R.id.textPartnerDirector)
        private val textPhone: TextView = itemView.findViewById(R.id.textPartnerPhone)
        private val textRating: TextView = itemView.findViewById(R.id.textPartnerRating)
        private val textEmail: TextView = itemView.findViewById(R.id.textPartnerEmail)

        fun bind(partner: Partner) {
            textName.text = partner.name
            textType.text = partner.type
            textDirector.text = "Директор: ${partner.director}"
            textPhone.text = partner.phone
            textEmail.text = partner.email
            textRating.text = "Рейтинг: ${partner.rating}/10"

            // Цвет рейтинга в зависимости от значения
            val ratingColor = when (partner.rating) {
                in 8..10 -> android.R.color.holo_green_dark
                in 5..7 -> android.R.color.holo_orange_dark
                else -> android.R.color.holo_red_dark
            }
            textRating.setTextColor(ContextCompat.getColor(itemView.context, ratingColor))
        }
    }
}

class PartnerDiffCallback : DiffUtil.ItemCallback<Partner>() {
    override fun areItemsTheSame(oldItem: Partner, newItem: Partner): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Partner, newItem: Partner): Boolean {
        return oldItem == newItem
    }
}