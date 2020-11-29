package com.matiasilveiro.automastichome.main.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matiasilveiro.automastichome.databinding.ItemRecyclerCentralNodeBinding
import com.matiasilveiro.automastichome.main.domain.CentralNode
import kotlin.properties.Delegates

class CentralNodesAdapter : RecyclerView.Adapter<CentralNodesAdapter.ViewHolder>() {

    var items: List<CentralNode> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }
    var onClickListener : ((CentralNode) -> Unit )? = null
    var onEditListener : ((CentralNode) -> Unit )? = null
    var onDeleteListener : ((CentralNode) -> Unit )? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerCentralNodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.onClickListener = onClickListener
        holder.onEditListener = onEditListener
        holder.onDeleteListener = onDeleteListener
    }

    fun setData(data: MutableList<CentralNode>){
        this.items = data
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(private val binding: ItemRecyclerCentralNodeBinding) : RecyclerView.ViewHolder(binding.cardLayout) {
        var onClickListener : ((CentralNode) -> Unit )? = null
        var onEditListener : ((CentralNode) -> Unit )? = null
        var onDeleteListener : ((CentralNode) -> Unit )? = null

        internal fun bind(value: CentralNode) {
            binding.textView.text = value.name
            Glide.with(binding.root)
                .load(value.imageUrl)
                .centerCrop()
                .into(binding.imageView)

            binding.cardLayout.setOnClickListener {
                onClickListener?.invoke(value)
            }
            binding.btnEdit.setOnClickListener {
                onEditListener?.invoke(value)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteListener?.invoke(value)
            }

            if(value.role != 0) {
                binding.btnEdit.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE
            }
        }
    }
}