package com.matiasilveiro.automastichome.main.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matiasilveiro.automastichome.databinding.ItemRecyclerCentralNodeBinding
import com.matiasilveiro.automastichome.main.domain.CentralNode
import kotlin.properties.Delegates

class CentralNodesAdapter : RecyclerView.Adapter<CentralNodesAdapter.ViewHolder>() {

    var items: List<CentralNode> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }
    var onClickListener : ((CentralNode) -> Unit )? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerCentralNodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onClickListener)
    }

    fun setData(data: MutableList<CentralNode>){
        this.items = data
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(private val binding: ItemRecyclerCentralNodeBinding) : RecyclerView.ViewHolder(binding.cardLayout) {

        internal fun bind(value: CentralNode, listener: ((CentralNode) -> Unit)?) {
            binding.textView.text = value.name
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