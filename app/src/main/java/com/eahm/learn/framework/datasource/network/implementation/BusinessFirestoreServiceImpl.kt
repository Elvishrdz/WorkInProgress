package com.eahm.learn.framework.datasource.network.implementation

import android.util.Log
import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.framework.datasource.network.abstraction.BusinessFirestoreService
import com.eahm.learn.framework.datasource.network.mappers.BusinessNetworkMapper
import com.eahm.learn.framework.datasource.network.model.BusinessNetworkEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessFirestoreServiceImpl
@Inject constructor(
    private val firestore: FirebaseFirestore,
    private val networkMapper: BusinessNetworkMapper
) : BusinessFirestoreService {

    private val TAG = "BusinessFirestoreServiceImpl"

    override suspend fun getBusiness(businessId: String): Business? {
        return firestore
            .collection(BUSINESS_COLLECTION)
            .document(businessId)
            .get()
            .addOnSuccessListener {
                Log.d(TAG, "Business $businessId successfully retrieved! ${it.data}")
            }
            .addOnFailureListener{ e ->
                Log.w(TAG, "Error getting document", e)
            }
            .await()
            .toObject(BusinessNetworkEntity::class.java)?.let {
                networkMapper.mapFromEntity(it)
            }
    }

    companion object{
        const val BUSINESS_COLLECTION = "business"
    }
}