package com.cleanplanet.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ServicesAdapter(private val onServiceClick: (Service) -> Unit) :
    ListAdapter<Service, ServicesAdapter.ServiceViewHolder>(ServiceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = getItem(position)
        holder.bind(service)
        holder.itemView.setOnClickListener { onServiceClick(service) }
    }

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textServiceName)
        private val textCode: TextView = itemView.findViewById(R.id.textServiceCode)
        private val textType: TextView = itemView.findViewById(R.id.textServiceType)
        private val textPrice: TextView = itemView.findViewById(R.id.textServicePrice)
        private val textDescription: TextView = itemView.findViewById(R.id.textServiceDescription)

        fun bind(service: Service) {
            textName.text = service.name
            textCode.text = "Код: ${service.code}"
            textType.text = "Тип: ${service.type}"
            textPrice.text = "Цена: ${service.price} руб."
            textDescription.text = service.description
        }
    }
}

class ServiceDiffCallback : DiffUtil.ItemCallback<Service>() {
    override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
        return oldItem == newItem
    }
}