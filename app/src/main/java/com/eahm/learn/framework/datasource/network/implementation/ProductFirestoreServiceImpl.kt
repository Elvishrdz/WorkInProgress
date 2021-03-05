package com.eahm.learn.framework.datasource.network.implementation

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.framework.datasource.network.abstraction.ProductFirestoreService
import com.eahm.learn.framework.datasource.network.implementation.provider.ProductProviderFirestoreServiceImpl.Companion.PRODUCT_REF_COLLECTION
import com.eahm.learn.framework.datasource.network.mappers.ProductNetworkMapper
import com.eahm.learn.framework.datasource.network.model.ProductNetworkEntity
import com.eahm.learn.utils.logD
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductFirestoreServiceImpl
@Inject constructor(
        private val firestore: FirebaseFirestore,
        private val networkMapper: ProductNetworkMapper
) : ProductFirestoreService {

    private val TAG = "ProductFirestoreServiceImpl"

    override suspend fun insertOrUpdateProduct(product: Product) : String{
        //val entity = networkMapper.mapToEntity(product)
        //entity.updated_at = Timestamp.now() // for updates


        //val providerId =  product.provider.business?.id ?: product.provider.user?.id ?: ""
        val providerId = product.provider.id

        if(providerId.isEmpty()){
            logD(TAG, "theres no provider id available")
            return ""
            //return some value as response
        }

        val entity = networkMapper.mapToEntity(product)

        val response = firestore
            .collection(PRODUCTS_COLLECTION)
            .add(entity)
            .addOnSuccessListener { documentReference ->
                logD(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                logD(TAG, "Error adding document $e")
                // send error reports to Firebase Crashlytics
                //  cLog(it.message)
            }
            .await()

        firestore
                .collection(PRODUCT_REF_COLLECTION)
                .document(providerId)
                .collection(PRODUCT_REF_COLLECTION)
                .document(response.id)
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

        /**
         * product id
         */
        return response.id
    }

    override suspend fun deleteProduct(primaryKey: String) {
        firestore
            .collection(PRODUCTS_COLLECTION)
            .document(USER_ID)
            .collection(PRODUCTS_COLLECTION)
            .document(primaryKey)
            .delete()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
               // cLog(it.message)
            }
            .await()
    }

    override suspend fun insertDeletedProduct(product: Product) {
        val entity = networkMapper.mapToEntity(product)
        firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .collection(PRODUCTS_COLLECTION)
            .document(entity.id)
            .set(entity)
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                //cLog(it.message)
            }
            .await()
    }

    override suspend fun insertDeletedProducts(products: List<Product>) {
        if(products.size > 500){
            throw Exception("Cannot delete more than 500 products at a time in firestore.")
        }

        val collectionRef = firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .collection(PRODUCTS_COLLECTION)

        firestore.runBatch { batch ->
            for(product in products){
                val documentRef = collectionRef.document(product.id)
                batch.set(documentRef, networkMapper.mapToEntity(product))
            }
        }.addOnFailureListener {
            // send error reports to Firebase Crashlytics
            //cLog(it.message)
        }.await()
    }

    override suspend fun deleteDeletedProduct(product: Product) {
        val entity = networkMapper.mapToEntity(product)
        firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .collection(PRODUCTS_COLLECTION)
            .document(entity.id)
            .delete()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                //cLog(it.message)
            }
            .await()
    }

    // used in testing
    override suspend fun deleteAllProducts() {
        firestore
            .collection(PRODUCTS_COLLECTION)
            .document(USER_ID)
            .delete()
            .await()
        firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .delete()
            .await()
    }

    override suspend fun getDeletedProducts(): List<Product> {
        return networkMapper.entityListToProductList(
                firestore
                        .collection(DELETES_COLLECTION)
                        .document(USER_ID)
                        .collection(PRODUCTS_COLLECTION)
                        .get()
                        .addOnFailureListener {
                            // send error reports to Firebase Crashlytics
                            //cLog(it.message)
                        }
                        .await().toObjects(ProductNetworkEntity::class.java)
        )
    }

    override suspend fun searchProduct(product: Product): Product? {
        return firestore
            .collection(PRODUCTS_COLLECTION)
            .document(USER_ID)
            .collection(PRODUCTS_COLLECTION)
            .document(product.id)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                //cLog(it.message)
            }
            .await()
            .toObject(ProductNetworkEntity::class.java)?.let {
                networkMapper.mapFromEntity(it)
            }
    }

    override suspend fun getAllProducts(): List<Product> {
        val result = firestore
                .collection(PRODUCTS_COLLECTION)
                .get()
                .await()

        val entities : MutableList<ProductNetworkEntity> = mutableListOf()

        try {
            for (document in result.documents) {
                document.toObject(ProductNetworkEntity::class.java)?.let{
                    it.id = document.id
                    logD(TAG, "parsed! ${it.id}")
                    entities.add(it)
                } ?: logD(TAG, "no parsed!")
            }
        }
        catch (e: Exception){
            logD(TAG, "failed! ${e.message.toString()}")
            entities.clear()
        }

        return networkMapper.entityListToProductList(entities)
    }


    override suspend fun insertOrUpdateProducts(products: List<Product>) {

        if(products.size > 500){
            throw Exception("Cannot insert more than 500 products at a time into firestore.")
        }

        val collectionRef = firestore
            .collection(PRODUCTS_COLLECTION)
            .document(USER_ID)
            .collection(PRODUCTS_COLLECTION)

        firestore.runBatch { batch ->
            for(product in products){
                val entity = networkMapper.mapToEntity(product)
                entity.updated_at = Timestamp.now()
                val documentRef = collectionRef.document(product.id)
                batch.set(documentRef, entity)
            }
        }.addOnFailureListener {
            // send error reports to Firebase Crashlytics
            //cLog(it.message)
        }.await()

    }

    companion object {
        const val PRODUCTS_COLLECTION = "products"
        const val USERS_COLLECTION = "users"
        const val DELETES_COLLECTION = "deletes"
        const val USER_ID = "9E7fDYAUTNUPFirw4R28NhBZE1u1" // hardcoded for single user
        const val EMAIL = "elvis@hrdz.es"
    }
}