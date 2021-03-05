package com.eahm.learn.framework.datasource.network.implementation.provider

import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.framework.datasource.network.abstraction.provider.ProductProviderFirestoreService
import com.eahm.learn.utils.logD
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductProviderFirestoreServiceImpl(
        private val firestore : FirebaseFirestore,
        private val productFactory: ProductFactory
) : ProductProviderFirestoreService {

    private val TAG = "ProductProviderFirestoreServiceImpl"

    override suspend fun insertProductProvider(providerId: String, productId: String): Boolean {
         firestore
                .collection(PRODUCT_REF_COLLECTION)
                .document(providerId)
                .collection(PRODUCT_REF_COLLECTION)
                .document(productId)
                .set(
                        mapOf(
                                "status" to "available"
                        )
                )
                .addOnSuccessListener {
                    logD(TAG, "saving product reference completed!")
                }
                .addOnFailureListener{
                    logD(TAG, "saving product reference has failed! ${it.message}")

                }
                .await()
        //todo check this
        return true

    }

    override suspend fun getAllProductsProviderIds(providerId: String): List<Product> {
        logD(TAG, "get all products providers ids from $providerId")
        val list : MutableList<Product> = mutableListOf()

        if(providerId.isNotEmpty()){
            val documents = firestore
                    .collection(PRODUCT_REF_COLLECTION)
                    .document(providerId)
                    .collection(PRODUCT_REF_COLLECTION)
                    .get()
                    .addOnSuccessListener {
                        logD(TAG, "saving product reference completed! ${it.documents}")
                    }
                    .addOnFailureListener{
                        logD(TAG, "saving product reference has failed! ${it.message}")

                    }
                    .await()



            for(product in documents.documents){
                logD(TAG, "adding ${product.id}")
                list.add(
                        productFactory.createEmptyProduct(product.id)
                )
            }
        }

        return list
    }

    override suspend fun deleteProductProvider(providerId : String, productId: String): Int {
        val documents = firestore
                .collection(PRODUCT_REF_COLLECTION)
                .document(providerId)
                .collection(PRODUCT_REF_COLLECTION)
                .document(productId)
                .delete()
                .addOnSuccessListener {
                    logD(TAG, "saving product reference completed!")
                }
                .addOnFailureListener{
                    logD(TAG, "saving product reference has failed! ${it.message}")

                }
                .await()

        //todo check
        return 1
    }

    companion object{
        const val PRODUCT_PROVIDER_COLLECTION = "productsRef"
        const val PRODUCT_REF_COLLECTION = "productsRef"

    }
}