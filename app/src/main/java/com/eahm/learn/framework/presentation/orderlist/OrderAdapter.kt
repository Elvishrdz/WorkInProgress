package com.eahm.learn.framework.presentation.orderlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eahm.learn.R
import com.eahm.learn.business.domain.model.Order
import kotlinx.android.synthetic.main.order_list_item_layout.view.*

class OrderAdapter(
    private val interaction : OrderInteractions
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.order_list_item_layout,
                parent,
                false
        )
        return OrderViewHolder(interaction, view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is OrderViewHolder ->{
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(newOrderList : List<Order>){
        differ.submitList(newOrderList)
    }

    class OrderViewHolder(
            private val interaction : OrderInteractions,
            view : View
    ) : RecyclerView.ViewHolder(view) {

        lateinit var currentOrder : Order

        fun bind(order: Order) = with(itemView){
            currentOrder = order

            order_item_amount.text = "${currentOrder.products.size} items"
            order_placed_date.text = "Ordered on ${currentOrder.order_date}"

            root_order_item.setOnClickListener{
                interaction.onItemSelected(currentOrder)
            }

        }
    }


}

interface OrderInteractions {
    fun onItemSelected(order : Order)
}