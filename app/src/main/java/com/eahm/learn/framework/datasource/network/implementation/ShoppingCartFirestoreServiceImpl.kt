package com.eahm.learn.framework.datasource.network.implementation

import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.framework.datasource.network.abstraction.ShoppingCartFirestoreService
import com.eahm.learn.framework.datasource.network.mappers.ProductNetworkMapper
import com.eahm.learn.framework.datasource.network.mappers.ShoppingCartNetworkMapper
import com.eahm.learn.framework.datasource.network.model.ShoppingCartNetworkEntity
import com.eahm.learn.utils.logD
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCartFirestoreServiceImpl
@Inject
constructor(
    private val firestore : FirebaseFirestore,
    private val shoppingCartNetworkMapper: ShoppingCartNetworkMapper
) : ShoppingCartFirestoreService {

    private val TAG = "ShoppingCartFirestoreServiceImpl"

    override suspend fun getAllShoppingCart(userId : String): List<ShoppingCart> {
        val shoppingCart : MutableList<ShoppingCart> = mutableListOf()

        if(userId.isNotEmpty()){
            logD(TAG, "get documents of $userId from server ")
            val shoppingCartDocument = firestore
                    .collection(SHOPPING_CART_COLLECTION)
                    .document(userId)
                    .collection(SHOPPING_CART_COLLECTION)
                    .get()
                    .await()

            logD(TAG, "total obtained: ${shoppingCartDocument.documents.size}")
            for(element in shoppingCartDocument.documents){
                element.toObject(ShoppingCartNetworkEntity::class.java)?.let {
                    shoppingCart.add(
                        shoppingCartNetworkMapper.mapFromEntity(it)
                    )
                }
            }
        }
        logD(TAG, "returning ${shoppingCart.size} elements")
        return shoppingCart
    }

    override suspend fun insertInShoppingCart(userId : String, newProduct: ShoppingCart): ShoppingCart? {
        var shoppingCart : ShoppingCart? = null

        if(userId.isNotEmpty()){
            logD(TAG,"insert new product for $userId")
            val newProductReference = firestore
                    .collection(SHOPPING_CART_COLLECTION)
                    .document(userId)
                    .collection(SHOPPING_CART_COLLECTION)
                    .add(
                        shoppingCartNetworkMapper.mapToEntity(newProduct)
                    )
                    .await()

            newProductReference?.let {
                logD(TAG,"prepare the product with id ${it.id}")
                shoppingCart = ShoppingCart(
                        id = it.id,
                        product = newProduct.product,
                        amount = newProduct.amount
                )
            }
        }

        logD(TAG,"return $shoppingCart product")
        return shoppingCart
    }

    override suspend fun removeFromShoppingCart(userId :String, shoppingCartId: String): Int {
        val result = firestore
                .collection(SHOPPING_CART_COLLECTION)
                .document(userId)
                .collection(SHOPPING_CART_COLLECTION)
                .document(shoppingCartId)
                .delete()

                .await()

        return 1
    }

    override suspend fun cleanShoppingCart(userId: String): Int {
        // TODO not recommended to delete collections here. Use a cloud function. webservice
        val result = firestore
                .collection(SHOPPING_CART_COLLECTION)
                .document(userId)
                .collection(SHOPPING_CART_COLLECTION)
                .get()
                .await()

        for(shopItem in result.documents){
            logD(TAG, "deleting ${shopItem.id}")
            firestore
                    .collection(SHOPPING_CART_COLLECTION)
                    .document(userId)
                    .collection(SHOPPING_CART_COLLECTION)
                    .document(shopItem.id)
                    .delete()
                    .await()
        }

        logD(TAG, "clean $userId shopping cart elements: $result")

        return 1
    }

    override suspend fun updateAmount(userId : String, shoppingCartId: String, newAmount: Int): Int {
        val result = firestore
                .collection(SHOPPING_CART_COLLECTION)
                .document(userId)
                .collection(SHOPPING_CART_COLLECTION)
                .document(shoppingCartId)
                .update(
                    mapOf(
                        "amount" to newAmount
                    )
                )
                .await()

        return 1
    }

    override suspend fun searchItem(shoppingCartId: String): ShoppingCart? {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsAmount(): Int? {
        TODO("Not yet implemented")
    }

    companion object{
        const val SHOPPING_CART_COLLECTION = "shoppingCart"
    }
}