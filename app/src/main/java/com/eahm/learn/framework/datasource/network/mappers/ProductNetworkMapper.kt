package com.eahm.learn.framework.datasource.network.mappers

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.ProductImage
import com.eahm.learn.business.domain.model.ProductTechnicalInfo
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.network.model.ProductNetworkEntity
import com.eahm.learn.framework.datasource.network.model.ProductNetworkImage
import com.google.firebase.Timestamp
import javax.inject.Inject

/**
 * Maps Product to ProductNetworkEntity or ProductNetworkEntity to Product.
 */
class ProductNetworkMapper
@Inject
constructor(
    private val dateUtil: DateUtil
): EntityMapper<ProductNetworkEntity, Product>
{

    fun entityListToProductList(entities: List<ProductNetworkEntity>): List<Product>{
        val list: ArrayList<Product> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    fun productListToEntityList(products: List<Product>): List<ProductNetworkEntity>{
        val entities: ArrayList<ProductNetworkEntity> = ArrayList()
        for(product in products){
            entities.add(mapToEntity(product))
        }
        return entities
    }

    override fun mapFromEntity(entityModel: ProductNetworkEntity): Product {
        return Product(
            id = entityModel.id,
            title = entityModel.title,
            description = entityModel.description,
            photos = mapListFromEntity(entityModel.photos),
            technicalInfo = ProductTechnicalInfo(),
            price = entityModel.price,
            provider =  Provider(entityModel.provider, null, null),
            updated_at = dateUtil.convertFirebaseTimestampToStringData(entityModel.updated_at),
            created_at = dateUtil.convertFirebaseTimestampToStringData(entityModel.created_at),
            amountAvailable = 0,
            status = ""
        )
    }


    override fun mapToEntity(domainModel: Product): ProductNetworkEntity {
        val updated = if(domainModel.updated_at.isNotEmpty())
            dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updated_at)
        else Timestamp.now()

        val created = if(domainModel.updated_at.isNotEmpty())
            dateUtil.convertStringDateToFirebaseTimestamp(domainModel.created_at)
        else Timestamp.now()

        return ProductNetworkEntity(
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            photos = mapListToEntity(domainModel.photos),
            technicalInformation = mapTechInfoToEntity(domainModel.technicalInfo),
            price = domainModel.price,
            provider = domainModel.provider.id,
            updated_at = updated,
            created_at = created
        )
    }

    private fun mapTechInfoToEntity(technicalInfo: ProductTechnicalInfo?): Map<String, Any> {
        val capacity = technicalInfo?.capacity ?: ""
        val color = technicalInfo?.color ?: ""
        val material = technicalInfo?.material ?: ""
        val size = technicalInfo?.size ?: ""
        val weight = technicalInfo?.weight ?: ""

        return mapOf(
             "capacity" to capacity,
             "color" to color,
             "material" to material,
             "size" to size,
             "weight" to weight,
        )
    }

    private fun mapListFromEntity(photos: List<ProductNetworkImage>): List<ProductImage> {
        val photoList : MutableList<ProductImage> = mutableListOf()

        for(photo in photos){
            photoList.add(
                ProductImage(
                    photoUrl = photo.photoUrl,
                    photoDescription = photo.photoDescription
                )
            )
        }
        return photoList
    }

    private fun mapListToEntity(photos: List<ProductImage>): List<ProductNetworkImage> {
        val photoList : MutableList<ProductNetworkImage> = mutableListOf()

        for(photo in photos){
            photoList.add(
                ProductNetworkImage(
                    photoUrl = photo.photoUrl,
                    photoDescription = photo.photoDescription
                )
            )
        }
        return photoList
    }



}