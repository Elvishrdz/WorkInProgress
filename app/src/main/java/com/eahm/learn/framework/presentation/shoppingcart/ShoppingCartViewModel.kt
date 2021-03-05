package com.eahm.learn.framework.presentation.shoppingcart

import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.eahm.learn.business.domain.model.*
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.interactors.common.InsertProductInCart.Companion.SHOPPING_CART_LOCAL
import com.eahm.learn.business.interactors.shoppingcart.ShoppingCartInteractors
import com.eahm.learn.framework.presentation.common.BaseViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.shoppingcart.state.ShoppingCartStateEvent.*
import com.eahm.learn.framework.presentation.shoppingcart.state.ShoppingCartViewState
import com.eahm.learn.utils.logD
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ShoppingCartViewModel
@Inject
constructor(
    private val shoppingCartInteractors: ShoppingCartInteractors,
    private val sessionManager: SessionManager
) : BaseViewModel<ShoppingCartViewState>() {

    private val TAG = "ShoppingCartViewModel"

    //region override BaseViewModel
    override fun initNewViewState(): ShoppingCartViewState = ShoppingCartViewState()

    override fun handleNewData(data: ShoppingCartViewState) {
        logD(TAG, "handleNewData")

        data.let { viewState ->
            viewState.itemsList?.let {
                setItemListDate(it)
            }

            viewState.newItem?.let {
                setNewItem(it)
            }

            viewState.numItemInCache?.let {
                setNumItemInCache(it)
            }

            viewState.deletedItemID?.let {
                setDeletedItemID(it)
            }

            viewState.updatedItem?.let{
                setItemAmount(it)
            }

            viewState.myOrderId?.let {
                setOrderAdded(it)
            }

        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job : Flow<DataState<ShoppingCartViewState>?> = when(stateEvent){

            is DeleteItem->{
                shoppingCartInteractors.deleteItem.deleteItem(
                    itemId = stateEvent.itemId,
                    userId = sessionManager.cachedUser.value?.id ?: SHOPPING_CART_LOCAL,
                    stateEvent = stateEvent
                )
            }

            is UpdateItemAmount->{
                shoppingCartInteractors.updateItemAmount.updateItemAmount(
                    shoppingCartToUpdate = stateEvent.shoppingCartToUpdate,
                    newAmount = stateEvent.newAmount,
                    userId = sessionManager.cachedUser.value?.id ?: SHOPPING_CART_LOCAL,
                    stateEvent = stateEvent
                )
            }

            is GetAllItems->{
                shoppingCartInteractors.getAllItems.getAllItems(
                    isLocal = !sessionManager.isActiveSession,
                    stateEvent = stateEvent
                )
            }
            is GetShoppingCartItemAmountNum->{
                shoppingCartInteractors.getShoppingCartItemAmountNum.getShoppingCartItemAmountNum(
                    isLocal = !sessionManager.isActiveSession,
                    stateEvent = stateEvent
                )
            }

            is AddNewOrder -> {
                shoppingCartInteractors.addNewOrder.addNewOrder(
                    newOrder = stateEvent.newOrder,
                    stateEvent = stateEvent
                )
            }

            is CreateStateMessageEvent ->{
                emitStateMessageEvent(
                    stateMessage = stateEvent.stateMessage,
                    stateEvent = stateEvent
                )
            }
            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }

        launchJob(stateEvent, job)
    }
    //endregion override BaseViewModel

    //region set view state data
    private fun setItemListDate(itemList : List<ShoppingCart>){
        val update = getCurrentViewStateOrNew()
        update.itemsList = itemList.toMutableList()
        setViewState(update)
    }

    private fun setNewItem(newItem : ShoppingCart?){
        val update = getCurrentViewStateOrNew()
        update.newItem = newItem
        setViewState(update)
    }

    private fun setNumItemInCache(numItemInCache : Int?){
        val update = getCurrentViewStateOrNew()
        update.numItemInCache = numItemInCache
        setViewState(update)
    }

    private fun setDeletedItemID(itemId : String?){
        val update = getCurrentViewStateOrNew()

        update.itemsList?.let { currentList ->
            val deletedProduct : ShoppingCart? = currentList.find {
                it.id == itemId
            }

            deletedProduct?.let {
                logD(TAG, "Remove Item founded! ${it.id}")
                currentList.remove(it)
            }
        }

        update.deletedItemID = null

        setViewState(update)
    }

    private fun setItemAmount(updatedItem : ShoppingCart){
        val update = getCurrentViewStateOrNew()

        update.itemsList?.let { currentList ->
            val replaceItem : ShoppingCart? = currentList.find{
                it.id == updatedItem.id
            }

            replaceItem?.let {
                val index = currentList.indexOf(replaceItem)
                currentList[index] = updatedItem
            }
        }

        update.updatedItem = null
        setViewState(update)
    }
    //endregion set view state data

    //region session access
    val user : LiveData<User?>
        get() = sessionManager.cachedUser

    val isActiveSession : Boolean
        get() = sessionManager.isActiveSession

    //endregion session access

    fun setFilter(filter : Filter?){
        filter?.let {
            val update = getCurrentViewStateOrNew()
            update.filter = filter
            setViewState(update)
        }
    }

    fun setOrderAdded(orderAdded : String?){
        val update = getCurrentViewStateOrNew()
        update.myOrderId = orderAdded
        setViewState(update)
    }

    fun getCurrentShoppingCartList(){
        setStateEvent(GetAllItems())
    }

    fun getShoppingCartItemAmountNum() {
        setStateEvent(GetShoppingCartItemAmountNum())
    }

    fun deleteItem(itemId: String) {
        setStateEvent(DeleteItem(itemId))
    }

    fun incrementItemAmount(productInCart :ShoppingCart) {
        val newAmount = productInCart.amount + 1

        setStateEvent(
            UpdateItemAmount(
                shoppingCartToUpdate = productInCart,
                newAmount = newAmount
            )
        )
    }

    fun decrementItemAmount(productInCart :ShoppingCart) {
        val newAmount = productInCart.amount - 1

        if(newAmount <= 0) return

        setStateEvent(
            UpdateItemAmount(
                shoppingCartToUpdate = productInCart,
                newAmount = newAmount
            )
        )
    }
    fun placeOrder() {

        if(sessionManager.isActiveSession){
            val current = getCurrentViewStateOrNew()

            val products : MutableList<OrderProductList>? = current.itemsList?.map {
                OrderProductList(
                        amount = it.amount,
                        productId = it.product.id
                )
            }?.toMutableList()

            val clientId = sessionManager.cachedUser.value?.id ?: ""

            //val orderOrigin = getDeviceId()
            logD(TAG, "place ${products?.size} orders in $clientId")
            setStateEvent(AddNewOrder(
                newOrder = Order(
                        id = "",
                        delivery = Delivery(
                                address = "2",
                                city = "3",
                                country = "4",
                                description = "5",
                                postCode = "6",
                                town = "7",
                        ),
                        order_date = "8",
                        products = products?.toList() ?: listOf(),
                        clientId = clientId,
                        orderOrigin = "11",
                        status = "pending",
                )
            ))
        }
        else {
            // todo show auth screen
            logD(TAG, "authenticate to place your order")
        }

    }


}

@Parcelize
data class Filter(
    val type : String
) : Parcelable