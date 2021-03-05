package com.eahm.learn.framework.presentation.providerorderlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eahm.learn.R
import com.eahm.learn.business.domain.model.provider.OrderProvider
import kotlinx.android.synthetic.main.provider_order_list_item_layout.view.*

class ProviderOrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<OrderProvider>(){
        override fun areItemsTheSame(oldItem: OrderProvider, newItem: OrderProvider): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderProvider, newItem: OrderProvider): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.provider_order_list_item_layout,
                parent,
                false
        )
        return OrderProviderViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is OrderProviderViewHolder ->{
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(newOrderProviderList : List<OrderProvider>){
        differ.submitList(newOrderProviderList)
    }

    class OrderProviderViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        lateinit var currentOrder : OrderProvider

        fun bind(orderProvider: OrderProvider) = with(itemView){
            currentOrder = orderProvider

            order_product_name.text = currentOrder.productId.title
            order_address_description.text = currentOrder.address.description
        }
    }


}