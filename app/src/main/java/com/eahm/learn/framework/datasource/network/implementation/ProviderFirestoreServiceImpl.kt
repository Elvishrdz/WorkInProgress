package com.eahm.learn.framework.datasource.network.implementation

import android.util.Log
import com.eahm.learn.business.domain.factory.provider.OrderProviderFactory
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.framework.datasource.network.abstraction.ProviderFirestoreService
import com.eahm.learn.framework.datasource.network.mappers.ProviderNetworkMapper
import com.eahm.learn.framework.datasource.network.model.ProviderNetworkEntity
import com.eahm.learn.utils.logD
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderFirestoreServiceImpl
@Inject constructor(
    private val firestore : FirebaseFirestore,
    private val networkMapper : ProviderNetworkMapper
): ProviderFirestoreService {

    private val TAG = "ProviderFirestoreServiceImpl"
    override suspend fun insertProvider(newProviderValues: Provider): Provider? {
        val result = firestore
                .collection(PROVIDER_COLLECTION)
                .add(networkMapper.mapToEntity(newProviderValues))
                .addOnSuccessListener { documentReference->
                    logD(TAG, "DocumentSnapshot inserted ID: ${documentReference.id}")
                }
                .addOnFailureListener{ e ->
                    logD(TAG, "Error inserting document ${e.message}")
                }
                .await()

        return result?.let {
            Provider(
                id = result.id,
                business = newProviderValues.business,
                user = newProviderValues.user
            )
        }
    }

    /*

      override suspend fun deleteProvider(providerId: String) {
          firestore
              .collection(PROVIDER_COLLECTION)
              .document(providerId)
              .delete()
              .addOnSuccessListener {
                  Log.d(TAG, "Provider ${providerId} successfully deleted!")
              }
              .addOnFailureListener{ e ->
                  Log.w(TAG, "Error deleting document", e)
              }
              .await()
      }

      override suspend fun updateProvider(newProviderValues: Provider) {
      val creationOrUpdate = hashMapOf<String, Any>(
            "timestamp" to FieldValue.serverTimestamp()
        )

          firestore
              .collection(PROVIDER_COLLECTION)
              .document(newProviderValues.id)
              .set(newProviderValues)
              .addOnSuccessListener {
                  Log.d(TAG, "Provider ${newProviderValues.id} successfully updated!")
              }
              .addOnFailureListener{ e ->
                  Log.w(TAG, "Error updating document", e)
              }
              .await()
      }*/

    override suspend fun getProvider(providerId: String): Provider? {
        logD(TAG, "BUSCAR en $PROVIDER_COLLECTION -> $providerId")
        // Todo remove spaces from providerId (just in case)

        return FirebaseFirestore.getInstance()
            .collection("providers")
            .document(providerId)
            .get()
            .addOnSuccessListener {document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG, "Error getting document", e)
            }
            .await()
            .toObject(ProviderNetworkEntity::class.java)?.let {
                val result = networkMapper.mapFromEntity(it)
                logD(TAG, "Objeto $it -- Mappeado: ${result.toString()}")
                result
            }

        /*if(result.exists()){
            logD(TAG, "Existe un resultado en $PROVIDER_COLLECTION ${result.data}")
        }
        else {
            logD(TAG, "No existe esta ruta")
        }

        val businessId = result.data?.get("businessId") ?: ""
        val userId = result.data?.get("ownerUserId") ?: ""

        val pro = result.toObject(ProviderNetworkEntity::class.java)

        if(pro != null){
            pro.id = result.id
            logD(TAG, "id asignado")
        }
        logD(TAG, "volver $businessId $userId")

        return pro?.let { networkMapper.mapFromEntity(it) }*/

    }

    companion object{
        const val PROVIDER_COLLECTION = "providers"
    }
}