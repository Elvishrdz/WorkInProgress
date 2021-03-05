package com.eahm.learn.business.interactors.common

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.business.data.network.abstraction.ShoppingCartNetworkDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailStateEvent
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertProductInCart<ViewState>(
    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
    private val shoppingCartNetworkDataSource: ShoppingCartNetworkDataSource
) {

    private val TAG = "InsertProductInCart"

    fun insertProductInCart(
        userId : String,
        product : Product,
        stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow {
        logD(TAG, "insert product in the cart ${product.title}")

        if(userId.isEmpty()){
            // Emit error message
            logD(TAG, "user id is not valid")
            return@flow
        }

        if(userId != SHOPPING_CART_LOCAL){
            logD(TAG, "we are authenticated. this is a user id")

            val newShoppingCartProduct = ShoppingCart(
                id = "autogenerate",
                product = product,
                amount = 1
            )
            //region in progress
            /* // todo fix this
             val productInShoppingCart = searchItem(product, stateEvent)
             logD(TAG, "search if product is already in the cart. Result: ${productInShoppingCart?.id}")

             if(productInShoppingCart != null){
                 //Already in the shopping cart. IncreaseAmount
                 val updateAmountResult = updateAmountOfExistingProduct(
                     productInShoppingCart = productInShoppingCart,
                     stateEvent = stateEvent
                 )
                 logD(TAG, "updateAmountOfExistingProduct Result: ${updateAmountResult?.amount}")

                 var message = SHOPPING_CART_INSERT_FAIL
                 var data : ViewState? = null

                 when(stateEvent){
                     is ProductDetailStateEvent -> {
                         message = SHOPPING_CART_INSERT_SUCCESS
                         data = ProductDetailViewState(
                             productInCart = updateAmountResult
                         ) as ViewState
                     }
                 }

                 logD(TAG, "insertProductInCart Final emit: $message")
                 emit(
                     DataState.data(
                         response = Response(
                             message = message,
                             uiComponentType = UIComponentType.Toast(),
                             messageType = MessageType.Success()
                         ),
                         data = data,
                         stateEvent = stateEvent
                     )
                 )
             }
             else {
                 val newShoppingCartProduct = ShoppingCart(
                     id = UUID.randomUUID().toString(),
                     product = product,
                     amount = 1
                 )

                 val cacheResponse = safeCacheCall(IO){
                     shoppingCartCacheDataSource.insertInShoppingCart(
                         newProduct = newShoppingCartProduct,
                         isLocal = true
                     )
                 }

                 val cacheResult = object : CacheResponseHandler<ViewState, Long>(
                     response = cacheResponse,
                     stateEvent = stateEvent
                 ){
                     override suspend fun handleSuccess(resultObj: Long): DataState<ViewState>? {
                         var message = SHOPPING_CART_INSERT_FAIL
                         var data : ViewState? = null

                         if(resultObj > 0){
                             when(stateEvent){
                                 is ProductDetailStateEvent -> {
                                     message = SHOPPING_CART_INSERT_SUCCESS
                                     data = ProductDetailViewState(
                                         productInCart = newShoppingCartProduct
                                     ) as ViewState

                                     logD(TAG, "isProductDetailStateEvent : $SHOPPING_CART_INSERT_SUCCESS")
                                 }
                             }
                         }

                         return DataState.data(
                             response = Response(
                                 message = message,
                                 uiComponentType = UIComponentType.Toast(),
                                 messageType = MessageType.Success()
                             ),
                             data = data,
                             stateEvent = stateEvent
                         )

                     }
                 }
                 emit(cacheResult.getResult())

             }*/




            //endregion in progress

            shoppingCartNetworkDataSource.insertInShoppingCart(userId, newShoppingCartProduct)?.let {

                val updatedShoppingCartProduct = ShoppingCart(
                    id = it.id,
                    product = product,
                    amount = 1
                )

                // save in cache
                val cacheResponse = safeCacheCall(IO){
                    shoppingCartCacheDataSource.insertInShoppingCart(
                        newProduct = updatedShoppingCartProduct,
                        isLocal = false
                    )
                }

                val cacheResult = object : CacheResponseHandler<ViewState, Long>(
                    response = cacheResponse,
                    stateEvent = stateEvent
                ){
                    override suspend fun handleSuccess(resultObj: Long): DataState<ViewState>? {
                        var message = SHOPPING_CART_INSERT_FAIL
                        var data : ViewState? = null

                        if(resultObj > 0){
                            when(stateEvent){
                                is ProductDetailStateEvent -> {
                                    message = SHOPPING_CART_INSERT_SUCCESS
                                    data = ProductDetailViewState(
                                        productInCart = newShoppingCartProduct
                                    ) as ViewState

                                    logD(TAG, "isProductDetailStateEvent : $SHOPPING_CART_INSERT_SUCCESS")
                                }
                            }
                        }

                        return DataState.data(
                            response = Response(
                                message = message,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                            ),
                            data = data,
                            stateEvent = stateEvent
                        )

                    }
                }
                emit(cacheResult.getResult())
            } ?: emit(DataState.data(
                response = Response(
                    message = SHOPPING_CART_INSERT_FAIL,
                    uiComponentType = UIComponentType.None(),
                    messageType = MessageType.Error()
                ),
                data = ProductDetailViewState(
                    productInCart = null
                ) as ViewState,
                stateEvent = stateEvent
            ))


        }
        else {
            logD(TAG, "no user authenticated. InsertProductInCart ${product.title}")

            val productInShoppingCart = searchItem(product, stateEvent)
            logD(TAG, "searchItem Result: ${productInShoppingCart?.id}")

            if(productInShoppingCart != null){
                //Already in the shopping cart. IncreaseAmount
                val updateAmountResult = updateAmountOfExistingProduct(
                    productInShoppingCart = productInShoppingCart,
                    stateEvent = stateEvent
                )
                logD(TAG, "updateAmountOfExistingProduct Result: ${updateAmountResult?.amount}")

                var message = SHOPPING_CART_INSERT_FAIL
                var data : ViewState? = null

                when(stateEvent){
                    is ProductDetailStateEvent -> {
                        message = SHOPPING_CART_INSERT_SUCCESS
                        data = ProductDetailViewState(
                            productInCart = updateAmountResult
                        ) as ViewState
                    }
                }

                logD(TAG, "insertProductInCart Final emit: $message")
                emit(
                    DataState.data(
                        response = Response(
                            message = message,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = data,
                        stateEvent = stateEvent
                    )
                )
            }
            else {
                val newShoppingCartProduct = ShoppingCart(
                    id = UUID.randomUUID().toString(),
                    product = product,
                    amount = 1
                )

                val cacheResponse = safeCacheCall(IO){
                    shoppingCartCacheDataSource.insertInShoppingCart(
                        newProduct = newShoppingCartProduct,
                        isLocal = true
                    )
                }

                val cacheResult = object : CacheResponseHandler<ViewState, Long>(
                    response = cacheResponse,
                    stateEvent = stateEvent
                ){
                    override suspend fun handleSuccess(resultObj: Long): DataState<ViewState>? {
                        var message = SHOPPING_CART_INSERT_FAIL
                        var data : ViewState? = null

                        if(resultObj > 0){
                            when(stateEvent){
                                is ProductDetailStateEvent -> {
                                    message = SHOPPING_CART_INSERT_SUCCESS
                                    data = ProductDetailViewState(
                                        productInCart = newShoppingCartProduct
                                    ) as ViewState

                                    logD(TAG, "isProductDetailStateEvent : $SHOPPING_CART_INSERT_SUCCESS")
                                }
                            }
                        }

                        return DataState.data(
                            response = Response(
                                message = message,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                            ),
                            data = data,
                            stateEvent = stateEvent
                        )

                    }
                }
                emit(cacheResult.getResult())

            }

        }
    }

    private suspend fun searchItem(
            product : Product,
            stateEvent: StateEvent
    ) : ShoppingCart?{

        val result = safeCacheCall(IO){
            shoppingCartCacheDataSource.searchItem(product.id)
        }

        val cacheResult = object : CacheResponseHandler<ShoppingCart?, ShoppingCart?>(
                response = result,
                stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: ShoppingCart?): DataState<ShoppingCart?>? {
                var message = SHOPPING_CART_SEARCH_ITEM_FAILED
                var messageType : MessageType = MessageType.Error()

                if(resultObj != null){
                    message = SHOPPING_CART_SEARCH_ITEM_SUCCESS
                    messageType = MessageType.Success()
                }

                return DataState.data(
                    response = Response(
                            message = message,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = messageType
                    ),
                    data = resultObj,
                    stateEvent = stateEvent
                )
            }
        }

        return cacheResult.getResult()?.data
    }

    private suspend fun updateAmountOfExistingProduct(
            productInShoppingCart: ShoppingCart,
            stateEvent: StateEvent
    ) : ShoppingCart? {

        val newAmount = productInShoppingCart.amount + 1

        val updatedProduct = ShoppingCart(
                id = productInShoppingCart.id,
                product = productInShoppingCart.product,
                amount = newAmount
        )

        val cacheResponse = safeCacheCall(IO){
            shoppingCartCacheDataSource.updateAmount(
                    shoppingCartId = productInShoppingCart.id,
                    newAmount = newAmount)
        }

        val cacheResult = object : CacheResponseHandler<ShoppingCart, Int>(
                response = cacheResponse,
                stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ShoppingCart>? {
                var message = SHOPPING_CART_UPDATE_ITEM_AMOUNT_FAILED
                var messageType : MessageType = MessageType.Error()
                var data : ShoppingCart? = null

                if(resultObj > 0){
                    message = SHOPPING_CART_UPDATE_ITEM_AMOUNT_SUCCESS
                    messageType = MessageType.Success()
                    data = updatedProduct
                }

                return DataState.data(
                        response = Response(
                                message = message,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = messageType
                        ),
                        data = data,
                        stateEvent = stateEvent
                )
            }

        }.getResult()

        return cacheResult?.data
    }

    companion object{
        const val SHOPPING_CART_INSERT_SUCCESS = "Successfully added to the shopping cart"
        const val SHOPPING_CART_INSERT_FAIL = "Failed to insert in the shopping cart"
        const val SHOPPING_CART_UPDATE_ITEM_AMOUNT_SUCCESS = "Product amount successfully Updated!"
        const val SHOPPING_CART_UPDATE_ITEM_AMOUNT_FAILED = "Failed updating product amount!"
        const val SHOPPING_CART_SEARCH_ITEM_SUCCESS = "Product successfully founded in the shopping cart"
        const val SHOPPING_CART_SEARCH_ITEM_FAILED = "The product was not found in the shopping cart"

        const val SHOPPING_CART_LOCAL = "LOCAL_SHOPPING_CART"
    }

}