package com.matiasilveiro.automastichome.main.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matiasilveiro.automastichome.databinding.ItemRecyclerRemoteActuatorBinding
import com.matiasilveiro.automastichome.databinding.ItemRecyclerRemoteNodeBinding
import com.matiasilveiro.automastichome.main.domain.RemoteActuator
import kotlin.properties.Delegates

class RemoteActuatorsAdapter : RecyclerView.Adapter<RemoteActuatorsAdapter.ViewHolder>() {

    var items: List<RemoteActuator> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }
    var onClickListener : ((RemoteActuator) -> Unit )? = null
    var onSwitchListener : ((RemoteActuator, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerRemoteActuatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onClickListener, onSwitchListener)
    }

    fun setData(data: ArrayList<RemoteActuator>){
        this.items = data.toList()
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(private val binding: ItemRecyclerRemoteActuatorBinding) : RecyclerView.ViewHolder(binding.cardLayout) {

        internal fun bind(value: RemoteActuator, clickListener: ((RemoteActuator) -> Unit)?, switchListener: ((RemoteActuator, Boolean) -> Unit)?) {
            binding.txtName.text = value.name
            Glide.with(binding.root)
                .load(value.imageUrl)
                .centerCrop()
                .into(binding.imageView)

            binding.cardLayout.setOnClickListener {
                clickListener?.invoke(value)
            }
            binding.switcher.setOnCheckedChangeListener { state ->
                switchListener?.invoke(value, state)
            }
        }
    }
}