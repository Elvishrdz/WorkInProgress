package com.eahm.learn.framework.presentation.productlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.R
import com.eahm.learn.framework.presentation.common.CURRENCY
import com.eahm.learn.framework.presentation.common.extensions.changeColor
import kotlinx.android.synthetic.main.layout_product_list_item.view.*
import java.lang.IndexOutOfBoundsException

class ProductListAdapter(
    private val interaction: Interaction? = null,
    private val lifecycleOwner: LifecycleOwner,
    private val selectedProducts: LiveData<ArrayList<Product>>,
    private val dateUtil: DateUtil
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_product_list_item,
                parent,
                false
            ),
            interaction,
            lifecycleOwner,
            selectedProducts,
            dateUtil
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Product>) {
        val commitCallback = Runnable {
            // if process died must restore list position
            interaction?.restoreListPosition()
        }
        //printLogD("listadapter", "size: ${list.size}")
        differ.submitList(list, commitCallback)
    }

    fun getProduct(index: Int): Product? {
        return try{
            differ.currentList[index]
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
            null
        }
    }

    class ProductViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val lifecycleOwner: LifecycleOwner,
        private val selectedProducts: LiveData<ArrayList<Product>>,
        private val dateUtil: DateUtil
    ) : RecyclerView.ViewHolder(itemView) {

        private val COLOR_GREY = R.color.app_background_color
        private val COLOR_PRIMARY = R.color.colorPrimary
        private lateinit var product: Product

        fun bind(item: Product) = with(itemView) {
            setOnClickListener {
                interaction?.onItemSelected(adapterPosition, product)
            }

            setOnLongClickListener {
                interaction?.activateMultiSelectionMode()
                interaction?.onItemSelected(adapterPosition, product)
                true
            }

            product = item
            product_title.text = item.title
            product_price.text = item.price.toString() + CURRENCY
            //product_timestamp.text = dateUtil.removeTimeFromDateString(item.updated_at)

            if(item.photos.isNotEmpty()){
                val url = item.photos[0].photoUrl

                Glide
                    .with(itemView)
                    .load(url)
                    .centerCrop()
                    .into(product_image)
            }

            // Observe changes in the product list
            selectedProducts.observe(lifecycleOwner, Observer { products ->
                if(products != null){
                    if(products.contains(product)){
                        changeColor(
                            newColor = COLOR_GREY
                        )
                    }
                    else{
                        changeColor(
                            newColor = COLOR_PRIMARY
                        )
                    }
                }
                else{
                    changeColor(
                        newColor = COLOR_PRIMARY
                    )
                }
            })
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Product)
        fun restoreListPosition()
        fun isMultiSelectionModeEnabled(): Boolean
        fun activateMultiSelectionMode()
        fun isProductSelected(product: Product): Boolean
    }
}