package com.eahm.learn.framework.datasource.network.implementation

import com.eahm.learn.business.domain.model.Delivery
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.framework.datasource.network.abstraction.OrderFirestoreService
import com.eahm.learn.framework.datasource.network.implementation.ProductFirestoreServiceImpl.Companion.PRODUCTS_COLLECTION
import com.eahm.learn.framework.datasource.network.implementation.ProviderFirestoreServiceImpl.Companion.PROVIDER_COLLECTION
import com.eahm.learn.framework.datasource.network.mappers.OrderNetworkMapper
import com.eahm.learn.framework.datasource.network.model.*
import com.eahm.learn.framework.datasource.network.model.provider.AddressNetworkEntity
import com.eahm.learn.framework.datasource.network.model.provider.OrderProviderNetworkEntity
import com.eahm.learn.utils.logD
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class OrderFirestoreServiceImpl
@Inject
constructor (
    private val firestore : FirebaseFirestore,
    private val orderNetworkMapper: OrderNetworkMapper,
    private val gson : Gson
) : OrderFirestoreService {

    private val TAG = "OrderFirestoreServiceImpl"

    override suspend fun placeOrder(newOrder: Order): String {
        logD(TAG, "Simulate server order creation locally")

        val myOrders : MutableList<OrderProductEntityList> = mutableListOf()

        logD(TAG, "Place our orders in the corresponding providers order list")
        for(product in newOrder.products){
            logD(TAG, "Find the product information ${product.productId}")
            val productReference = firestore
                    .collection(PRODUCTS_COLLECTION)
                    .document(product.productId)
                    .get()
                    .await()

            val currentProduct = productReference.toObject(ProductNetworkEntity::class.java)

            currentProduct?.let{
                it.id = productReference.id


                //region deprecated
                // TODO DEPRECATED! WE JUST NEED THE PROVIDER ID TO SET THE ORDER
                /*logD(TAG, "Find the provider of product ${currentProduct.id}")
                val currentProvider = firestore
                        .collection(PROVIDER_COLLECTION)
                        .document(it.provider)
                        .get()
                        .await()
                        .toObject(ProviderNetworkEntity::class.java)

                currentProvider?.let { provider ->
                    val providerId = if(provider.businessId.isNotEmpty()) provider.businessId else provider.ownerUserId

                    if(providerId.isNotEmpty()){
                        logD(TAG, "Prepare data for the order in provider")
                        val orderInProvider = OrderProviderNetworkEntity(
                                id = "",
                                productId = product.productId,
                                address = newOrder.delivery.toNetworkEntity()
                        )

                        logD(TAG, "Place new order in the product provider")
                        val currentProductOrderInProvider = firestore
                                .collection(ORDER_PROVIDER_COLLECTION)
                                .document(providerId)
                                .collection(ORDER_PROVIDER_ACTIVE_COLLECTION)
                                .add(orderInProvider)
                                .await()

                        logD(TAG, "Save the ID reference in our order to keep track ${currentProductOrderInProvider.id}")
                        product.orderId = currentProductOrderInProvider.id
                        //orderInProvider.id = currentProductOrderInProvider.id
                    }
                }*/
                //endregion deprecated

                //region new
                logD(TAG, "Prepare current product clone")
                val currentProductDataJSON = gson.toJson(it)

                logD(TAG, "Prepare data for the order in provider")
                val orderInProvider = OrderProviderNetworkEntity(
                        id = "",
                        productId = product.productId,
                        address = newOrder.delivery.toNetworkEntity(),
                        productOrdered = currentProductDataJSON
                )

                logD(TAG, "Place new order in the product provider ${currentProduct.provider}")
                val currentProductOrderInProvider = firestore
                        .collection(ORDER_PROVIDER_COLLECTION)
                        .document(currentProduct.provider)
                        .collection(ORDER_PROVIDER_ACTIVE_COLLECTION)
                        .add(orderInProvider)
                        .await()

                logD(TAG, "Save the ID reference in our order to keep track ${currentProductOrderInProvider.id}")
                product.orderId = currentProductOrderInProvider.id
                //orderInProvider.id = currentProductOrderInProvider.id
                //endregion new

            }

            if(product.orderId.isNotEmpty()){
                logD(TAG, "Add product to final order ${product.orderId}")
                myOrders.add(orderNetworkMapper.mapProductsToEntity(product))
            }
            else {
                logD(TAG, "Product: ${product.productId} was NOT ordered!")
            }
        }

        val finalOrder = newOrder.apply {
            products = myOrders.map {
                orderNetworkMapper.mapProductsFromEntity(it)
            }
        }

        // Save the orders in the user orders node
        val result = firestore
            .collection(ORDER_COLLECTION)
            .document(newOrder.clientId)
            .collection(ORDER_COLLECTION)
            .add(
                orderNetworkMapper.mapToEntity(finalOrder)
            )
            .addOnSuccessListener {
                logD(TAG, "order ${it.id} placed in ${newOrder.clientId}")
            }
            .addOnFailureListener{
                logD(TAG, "place order on failure called. ${it.message} ")
            }
            .await()

        return result?.id ?: ""

    }

    override suspend fun cancelExistingOrder(orderId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateDeliveryInformation(newDelivery: Delivery): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getActiveOrders(userId : String): List<Order> {
        val result = firestore
                .collection(ORDER_COLLECTION)
                .document(userId)
                .collection(ORDER_COLLECTION)
                .get()
                .await()
                //.toObjects(OrderNetworkEntity::class.java)

        val orderList : MutableList<Order> = mutableListOf()

        for(order in result.documents){
            order.toObject(OrderNetworkEntity::class.java)?.let {
                logD(TAG, "adding current order ${it.id} -- ${order.id}")
                //it.id = order.id
                orderList.add(
                        orderNetworkMapper.mapFromEntity(it)
                )
            }
        }

        logD(TAG, "returning: ${orderList.size} orders")
        return orderList
    }

    companion object{
        const val ORDER_COLLECTION = "orders"
        const val ORDER_PROVIDER_COLLECTION = "ordersProvider"
        const val ORDER_PROVIDER_ACTIVE_COLLECTION = "activeOrders"
        const val ORDER_PROVIDER_DISPATCHED_COLLECTION = "dispatchedOrders"
    }

}

// Temporal
fun Delivery.toNetworkEntity() : AddressNetworkEntity {
    return AddressNetworkEntity(
            address = this.address,
            city = this.city,
            country = this.country,
            description = this.description,
            postCode = this.postCode,
            town = this.town
    )
}