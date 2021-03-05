package com.eahm.learn.framework.presentation.providerproductlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eahm.learn.R
import com.eahm.learn.business.domain.model.Product
import kotlinx.android.synthetic.main.provider_product_list_item_layout.view.*

class UserProductListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.provider_product_list_item_layout,
                parent,
                false
        )
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ProductViewHolder ->{
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(newProductList : List<Product>){
        differ.submitList(newProductList)
    }

    class ProductViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        lateinit var currentProduct : Product

        fun bind(Product: Product) = with(itemView){
            currentProduct = Product
            product_name.text = "${currentProduct.title} - ${currentProduct.id}"
            product_amount.text = "${currentProduct.amountAvailable}"
        }
    }


}