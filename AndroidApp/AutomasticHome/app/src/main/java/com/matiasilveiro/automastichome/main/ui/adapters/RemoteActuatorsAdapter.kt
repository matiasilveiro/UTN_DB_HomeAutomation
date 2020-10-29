package com.matiasilveiro.automastichome.main.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matiasilveiro.automastichome.databinding.ItemRecyclerRemoteActuatorBinding
import com.matiasilveiro.automastichome.main.ui.models.RemoteActuatorUI
import kotlin.properties.Delegates

class RemoteActuatorsAdapter : RecyclerView.Adapter<RemoteActuatorsAdapter.ViewHolder>() {

    var items: List<RemoteActuatorUI> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }
    var onClickListener : ((RemoteActuatorUI) -> Unit )? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerRemoteActuatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onClickListener)
    }

    fun setData(data: MutableList<RemoteActuatorUI>){
        this.items = data
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(private val binding: ItemRecyclerRemoteActuatorBinding) : RecyclerView.ViewHolder(binding.cardLayout) {

        internal fun bind(value: RemoteActuatorUI, listener: ((RemoteActuatorUI) -> Unit)?) {
            binding.txtName.text = value.name
            Glide.with(binding.root)
                .load(value.imageUrl)
                .centerCrop()
                .into(binding.imageView)

            binding.cardLayout.setOnClickListener {
                listener?.invoke(value)
            }
        }
    }
}