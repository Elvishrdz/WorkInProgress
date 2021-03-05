package com.eahm.learn.framework.datasource.network.implementation.provider

import com.eahm.learn.business.domain.model.provider.OrderProvider
import com.eahm.learn.framework.datasource.network.abstraction.provider.OrderProviderFirebaseService
import com.eahm.learn.framework.datasource.network.mappers.provider.OrderProviderNetworkMapper
import com.eahm.learn.framework.datasource.network.model.provider.OrderProviderNetworkEntity
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.utils.logD
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// TODO DONT INJECT THE SESSION MANAGER HERE JUST USE WHAT IS NECESSARY TO DO THE OPERATIONS DONT DO THE JOBS OF OTHERS
class OrderProviderFirebaseServiceImpl(
    private val firestore : FirebaseFirestore,
    private val orderProviderNetworkMapper : OrderProviderNetworkMapper
) : OrderProviderFirebaseService {

    private val TAG = "OrderProviderFirebaseServiceImpl"

    override suspend fun getOrders(providerId : String): List<OrderProvider> {
        logD(TAG, "get orders from $providerId")
        val allOrders : MutableList<OrderProvider> = mutableListOf()

        if(providerId.isNotEmpty()){
            logD(TAG, "we have a provider: $providerId")
            val orders = firestore
                    .collection(ORDERS_PROVIDER)
                    .document(providerId)
                    .collection(ORDERS_PROVIDER_ACTIVE)
                    .get()
                    .await()

            logD(TAG, "We have result? ${orders.isEmpty}")
            for(document in orders){
                val currentOrder = document.toObject(OrderProviderNetworkEntity::class.java)
                currentOrder.id = document.id
                logD(TAG, "add order provider: ${currentOrder.id}")
                allOrders.add(
                        orderProviderNetworkMapper.mapFromEntity(currentOrder)
                )
            }
        }

        logD(TAG, "total orders: ${allOrders.size}")
        return allOrders
    }

    override suspend fun deleteOrder(orderId: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun setDispatchedOrder(orderId: String): OrderProvider {
        TODO("Not yet implemented")
    }

    companion object{
        const val ORDERS_PROVIDER = "ordersProvider"
        const val ORDERS_PROVIDER_ACTIVE = "activeOrders"
    }
}