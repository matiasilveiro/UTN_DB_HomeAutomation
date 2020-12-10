package com.matiasilveiro.automastichome.main.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.matiasilveiro.automastichome.core.utils.setVisible
import com.matiasilveiro.automastichome.databinding.ItemRecyclerControlBinding
import com.matiasilveiro.automastichome.main.domain.ControlFeedback
import kotlin.properties.Delegates

class ControlsAdapter : RecyclerView.Adapter<ControlsAdapter.ViewHolder>() {

    var items: List<ControlFeedback> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }
    var onClickListener : ((ControlFeedback) -> Unit )? = null
    var onEditListener : ((ControlFeedback) -> Unit )? = null
    var onDeleteListener : ((ControlFeedback) -> Unit )? = null
    private var role: Int = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerControlBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position],role)
        holder.onClickListener = onClickListener
        holder.onEditListener = onEditListener
        holder.onDeleteListener = onDeleteListener
    }

    fun setData(data: MutableList<ControlFeedback>){
        this.items = data
    }

    fun setRole(role: Int) {
        this.role = role
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(private val binding: ItemRecyclerControlBinding) : RecyclerView.ViewHolder(binding.cardLayout) {
        var onClickListener : ((ControlFeedback) -> Unit )? = null
        var onEditListener : ((ControlFeedback) -> Unit )? = null
        var onDeleteListener : ((ControlFeedback) -> Unit )? = null

        internal fun bind(value: ControlFeedback, role: Int) {
            binding.textView.text = value.name

            val action = when(value.actionTrue) {
                1 -> "ON"
                else -> "OFF"
            }
            binding.txtDescription.text = "Cuando el sensor ${value.condition} ${value.referenceValue}, el actuador estará ${action}"

            when(role) {
                0 -> {
                    binding.btnEdit.visibility = View.VISIBLE
                    binding.btnDelete.visibility = View.VISIBLE
                    binding.overlay.visibility = View.VISIBLE
                }
                else -> {
                    binding.btnEdit.visibility = View.GONE
                    binding.btnDelete.visibility = View.GONE
                    binding.overlay.visibility = View.GONE
                }
            }

            binding.cardLayout.setOnClickListener {
                onClickListener?.invoke(value)
            }
            binding.btnEdit.setOnClickListener {
                onEditListener?.invoke(value)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteListener?.invoke(value)
            }
        }
    }
}