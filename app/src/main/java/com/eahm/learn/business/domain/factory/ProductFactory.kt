package com.eahm.learn.business.domain.factory

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.ProductImage
import com.eahm.learn.business.domain.model.ProductTechnicalInfo
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.util.DateUtil
import kotlinx.android.synthetic.main.fragment_product.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Testing class
 */
@Singleton
class ProductFactory
@Inject constructor(
    private val dateUtil : DateUtil
) {

    fun createSingleProduct(
        id : String? = null,
        product : Product
    ) : Product {
        return Product(
            id = id ?: UUID.randomUUID().toString(),
            title = product.title,
            description = product.description,
            price = product.price,
            photos = product.photos,
            provider = Provider(UUID.randomUUID().toString(), null, null),
            updated_at = dateUtil.getCurrentTimestamp(),
            created_at = dateUtil.getCurrentTimestamp(),
            amountAvailable = 0,
            status = ""
        )
    }

    fun createEmptyProduct(
        id : String? = null
    ) : Product{
        return Product(
            id = id ?: UUID.randomUUID().toString(),
            title = "",
            description = "",
            price = 0f,
            photos = listOf(),
            provider = Provider(UUID.randomUUID().toString(), null, null),
            updated_at = dateUtil.getCurrentTimestamp(),
            created_at = dateUtil.getCurrentTimestamp(),
            amountAvailable = 0,
            status = "unavailable"
        )
    }

    fun createLocalTestProduct(
        title : String,
        description : String,
        price : Float,
        providerId : String = ""
    ) : Product {
        return Product(
            id = "",
            title = title,
            description = description,
            photos = listOf(
                ProductImage(
                photoUrl = "https://www.hsasystems.com/media/7000/box_p2150035_450px.jpg",
                photoDescription = "front view"
            )
            ),
            price = price,
            technicalInfo = ProductTechnicalInfo(
                size = "30x20x10",
                weight = "10kg",
                color = "azul oscuro",
                material = "ceramica solida",
                capacity = "1L",
            ),
            provider = Provider(providerId, null, null),
            reviewTotal = 1,
            reviewScore = 4.5f,
            updated_at = "",
            created_at = "",
            amountAvailable = 5,
            status = "available"

        )
    }

    fun createProduct(product: Product, provider: Provider): Product {
        return Product(
            id = product.id,
            title = product.title,
            description = product.description,
            price = product.price,
            photos = product.photos,
            provider = if(product.provider.id.isNotEmpty()) product.provider else if(provider.id.isNotEmpty()) provider else Provider("",null, null),
            updated_at = product.updated_at,
            created_at = product.created_at,
            amountAvailable = product.amountAvailable,
            status = product.status
        )
    }


}