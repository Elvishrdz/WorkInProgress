package com.eahm.learn.framework.presentation.shoppingcart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.R
import com.eahm.learn.framework.presentation.common.CURRENCY
import com.eahm.learn.utils.logD
import kotlinx.android.synthetic.main.layout_shopping_cart_item.view.*

class ShoppingCartAdapter(
    private val lifecycleOwner : LifecycleOwner,
    private val interactor : Interaction
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //region variables
    private val TAG = "ShoppingCartAdapter"

    private var currentList : MutableList<ShoppingCart> = mutableListOf()

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShoppingCart>(){
        override fun areItemsTheSame(oldItem: ShoppingCart, newItem: ShoppingCart): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingCart, newItem: ShoppingCart): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    //endregion variables

    //region override recyclerview adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShoppingCartViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_shopping_cart_item,
                    parent,
                    false
            ),
            interactor
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ShoppingCartViewHolder ->{
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
    //endregion override recyclerview adapter

    fun submitList(list : List<ShoppingCart>){
        currentList = list.toMutableList()
        val commitCallback = Runnable {
            // is executed when the List is committed, if it is committed.
            // if process died must restore list position
            //interaction?.restoreListPosition()
        }
        differ.submitList(currentList, commitCallback)
    }

    class ShoppingCartViewHolder constructor(
        view : View,
        private val interactor: Interaction?
    ) : RecyclerView.ViewHolder(view){
        private lateinit var currentProductItem : ShoppingCart

        fun bind(shoppingCart: ShoppingCart) = with(itemView) {

            currentProductItem = shoppingCart

            //region listeners
            layout_cart_item.setOnClickListener{
                /* See product Detail*/
                interactor?.onItemClicked(currentProductItem.id)
            }

            item_delete.setOnClickListener{
                interactor?.onDeleteItem(currentProductItem.id)
            }

            item_decrement_amount.setOnClickListener{
                interactor?.onDecrementAmount(currentProductItem)
            }

            item_increment_amount.setOnClickListener{
                interactor?.onIncrementAmount(currentProductItem)
            }
            //endregion listeners

            Glide.with(this)
                    .load(currentProductItem.product.photos[0].photoUrl)
                    .into(item_image)

            item_title.text = currentProductItem.product.title
            item_price.text = "${currentProductItem.product.price} $CURRENCY"

            item_amount_num.text = currentProductItem.amount.toString()

            val totalToPay =  currentProductItem.product.price * currentProductItem.amount
            item_total_num.text = totalToPay.toString()

        }
    }

    interface Interaction{
        fun onItemClicked(itemId : String)
        fun onDeleteItem(productId: String)
        fun onIncrementAmount(productInCart : ShoppingCart)
        fun onDecrementAmount(productInCart : ShoppingCart)
    }
}