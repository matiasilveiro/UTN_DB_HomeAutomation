package com.matiasilveiro.automastichome.main.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matiasilveiro.automastichome.databinding.ItemRecyclerRemoteNodeBinding
import com.matiasilveiro.automastichome.main.domain.RemoteSensor
import kotlin.properties.Delegates

class RemoteSensorsAdapter : RecyclerView.Adapter<RemoteSensorsAdapter.ViewHolder>() {

    var items: List<RemoteSensor> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }
    var onClickListener : ((RemoteSensor) -> Unit )? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerRemoteNodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onClickListener)
    }

    fun setData(data: MutableList<RemoteSensor>){
        this.items = data
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(private val binding: ItemRecyclerRemoteNodeBinding) : RecyclerView.ViewHolder(binding.cardLayout) {

        internal fun bind(value: RemoteSensor, listener: ((RemoteSensor) -> Unit)?) {
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