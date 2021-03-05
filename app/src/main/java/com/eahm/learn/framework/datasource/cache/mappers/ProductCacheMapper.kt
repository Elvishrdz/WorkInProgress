package com.eahm.learn.framework.datasource.cache.mappers

import android.util.Log
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.ProductImage
import com.eahm.learn.business.domain.model.ProductTechnicalInfo
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.cache.model.ProductCacheEntity
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type
import javax.inject.Inject

class ProductCacheMapper
@Inject constructor(
        private val dateUtil: DateUtil,
        private val gson: Gson
) : EntityMapper<ProductCacheEntity, Product>{

    override fun mapFromEntity(entityModel: ProductCacheEntity): Product {

        val listType: Type = object : TypeToken<ArrayList<ProductImage?>?>() {}.type

        val photos: List<ProductImage> = gson.fromJson(entityModel.photos, listType)
        val techInfo : ProductTechnicalInfo = gson.fromJson(entityModel.technicalInfo, ProductTechnicalInfo::class.java)

        return Product(
                id = entityModel.id,
                title = entityModel.title,
                description = entityModel.description,
                photos = photos,
                technicalInfo = techInfo,
                price = entityModel.price,
                provider = Provider(entityModel.provider, null, null),
                updated_at = entityModel.updated_at,
                created_at = entityModel.created_at,
                amountAvailable = entityModel.amountAvailable,
                status = entityModel.status
        )
    }

    override fun mapToEntity(domainModel: Product): ProductCacheEntity {

        val photosJson = gson.toJson(domainModel.photos)
        val techInfoJson = gson.toJson(domainModel.technicalInfo)

        Log.d("CLOUDFIRES", "convertToJSON: $photosJson $techInfoJson")

        return ProductCacheEntity(
                id = domainModel.id,
                title = domainModel.title,
                description = domainModel.description,
                photos = photosJson,
                technicalInfo = techInfoJson,
                price = domainModel.price,
                provider =  domainModel.provider.id,
                updated_at = domainModel.updated_at,
                created_at = domainModel.created_at,
                amountAvailable = domainModel.amountAvailable,
                status = domainModel.status
        )
    }


    fun productListToEntityList(products: List<Product>): List<ProductCacheEntity> {
        TODO("Convert list entity to domain")
    }

    fun entityListToProductList(products: List<ProductCacheEntity>): List<Product> {
        val productList = mutableListOf<Product>()
        for(productCache in products){
            productList.add(mapFromEntity(productCache))
        }
        return productList
    }

}