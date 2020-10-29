package com.matiasilveiro.automastichome.main.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matiasilveiro.automastichome.databinding.ItemRecyclerRemoteSensorBinding
import com.matiasilveiro.automastichome.main.ui.models.RemoteSensorUI
import kotlin.properties.Delegates

class RemoteSensorsAdapter : RecyclerView.Adapter<RemoteSensorsAdapter.ViewHolder>() {

    var items: List<RemoteSensorUI> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }
    var onClickListener : ((RemoteSensorUI) -> Unit )? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerRemoteSensorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onClickListener)
    }

    fun setData(data: MutableList<RemoteSensorUI>){
        this.items = data
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(private val binding: ItemRecyclerRemoteSensorBinding) : RecyclerView.ViewHolder(binding.cardLayout) {

        internal fun bind(value: RemoteSensorUI, listener: ((RemoteSensorUI) -> Unit)?) {
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