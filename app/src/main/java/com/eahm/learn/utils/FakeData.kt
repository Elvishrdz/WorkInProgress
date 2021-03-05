package com.eahm.learn.utils

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.ProductImage
import com.eahm.learn.business.domain.model.ProductTechnicalInfo

fun getFakeProductList() : List<Product>{
    val fakeList = mutableListOf<Product>()

    //region fake products
    /*val product1 = Product(
        id = "1",
        title = "Taza para el cafe",
        description = "Bueno bonito y barato! para todas tus mañanas, despierta con la sastisfaccion de una taza de calidad.",
        photos = listOf(
            ProductImage(
                photoUrl = "http://www.compupagina.com/wp-content/uploads/2020/07/taza.png",
                photoDescription = "Taza color blanco"
            )
        ),
        price = 120.00f,
        technicalInfo = ProductTechnicalInfo(
            size = "70x50x30",
            weight = "550g",
            color = "verde rojo azul amarillo",
            material = "porcelana",
            capacity = "50ml",
        ),
        provider = "id",
        reviewTotal = 1524,
        reviewScore = 4.8f,
        updated_at = "29 de diciembre de 2020, 0:00:00 UTC+1",
        created_at = "29 de diciembre de 2020, 0:00:00 UTC+1"
    )

    val product2 = Product(
            id = "2",
            title = "Camisa de Algodon",
            description = "Bueno bonito y barato! para todas tus mañanas, despierta con la sastisfaccion de una taza de calidad.",
            photos = listOf(
                    ProductImage(
                            photoUrl = "http://www.compupagina.com/wp-content/uploads/2020/07/taza.png",
                            photoDescription = "Camisa color blanco"
                    )
            ),
            price = 120.00f,
            technicalInfo = ProductTechnicalInfo(
                    size = "70x50x30",
                    weight = "",
                    color = "azul rojo azul amarillo",
                    material = "algodon",
                    capacity = "",
            ),
            provider = "id",
            reviewTotal = 727,
            reviewScore = 3.9f,
            updated_at = "29 de diciembre de 2020, 0:00:00 UTC+1",
            created_at = "29 de diciembre de 2020, 0:00:00 UTC+1"
    )

    val product3 = Product(
            id = "3",
            title = "Camisa de Algodon",
            description = "Bueno bonito y barato! para todas tus mañanas, despierta con la sastisfaccion de una taza de calidad.",
            photos = listOf(
                    ProductImage(
                            photoUrl = "http://www.compupagina.com/wp-content/uploads/2020/07/taza.png",
                            photoDescription = "Camisa color blanco"
                    )
            ),
            price = 120.00f,
            technicalInfo = ProductTechnicalInfo(
                    size = "70x50x30",
                    weight = "",
                    color = "azul rojo azul amarillo",
                    material = "algodon",
                    capacity = "",
            ),
            provider = "id",
            reviewTotal = 727,
            reviewScore = 3.9f,
            updated_at = "29 de diciembre de 2020, 0:00:00 UTC+1",
            created_at = "29 de diciembre de 2020, 0:00:00 UTC+1"
    )

    val product4 = Product(
            id = "4",
            title = "Camisa de Algodon",
            description = "Bueno bonito y barato! para todas tus mañanas, despierta con la sastisfaccion de una taza de calidad.",
            photos = listOf(
                    ProductImage(
                            photoUrl = "http://www.compupagina.com/wp-content/uploads/2020/07/taza.png",
                            photoDescription = "Camisa color blanco"
                    )
            ),
            price = 120.00f,
            technicalInfo = ProductTechnicalInfo(
                    size = "70x50x30",
                    weight = "",
                    color = "azul rojo azul amarillo",
                    material = "algodon",
                    capacity = "",
            ),
            provider = "id",
            reviewTotal = 727,
            reviewScore = 3.9f,
            updated_at = "29 de diciembre de 2020, 0:00:00 UTC+1",
            created_at = "29 de diciembre de 2020, 0:00:00 UTC+1"
    )

    val product5 = Product(
            id = "5",
            title = "Camisa de Algodon",
            description = "Bueno bonito y barato! para todas tus mañanas, despierta con la sastisfaccion de una taza de calidad.",
            photos = listOf(
                    ProductImage(
                            photoUrl = "http://www.compupagina.com/wp-content/uploads/2020/07/taza.png",
                            photoDescription = "Camisa color blanco"
                    )
            ),
            price = 120.00f,
            technicalInfo = ProductTechnicalInfo(
                    size = "70x50x30",
                    weight = "",
                    color = "azul rojo azul amarillo",
                    material = "algodon",
                    capacity = "",
            ),
            provider = "id",
            reviewTotal = 727,
            reviewScore = 3.9f,
            updated_at = "29 de diciembre de 2020, 0:00:00 UTC+1",
            created_at = "29 de diciembre de 2020, 0:00:00 UTC+1"
    )

    val product6 = Product(
            id = "6",
            title = "Camisa de Algodon",
            description = "Bueno bonito y barato! para todas tus mañanas, despierta con la sastisfaccion de una taza de calidad.",
            photos = listOf(
                    ProductImage(
                            photoUrl = "http://www.compupagina.com/wp-content/uploads/2020/07/taza.png",
                            photoDescription = "Camisa color blanco"
                    )
            ),
            price = 120.00f,
            technicalInfo = ProductTechnicalInfo(
                    size = "70x50x30",
                    weight = "",
                    color = "azul rojo azul amarillo",
                    material = "algodon",
                    capacity = "",
            ),
            provider = "id",
            reviewTotal = 727,
            reviewScore = 3.9f,
            updated_at = "29 de diciembre de 2020, 0:00:00 UTC+1",
            created_at = "29 de diciembre de 2020, 0:00:00 UTC+1"
    )

    val product7= Product(
            id = "7",
            title = "Camisa de Algodon",
            description = "Bueno bonito y barato! para todas tus mañanas, despierta con la sastisfaccion de una taza de calidad.",
            photos = listOf(
                    ProductImage(
                            photoUrl = "http://www.compupagina.com/wp-content/uploads/2020/07/taza.png",
                            photoDescription = "Camisa color blanco"
                    )
            ),
            price = 120.00f,
            technicalInfo = ProductTechnicalInfo(
                    size = "70x50x30",
                    weight = "",
                    color = "azul rojo azul amarillo",
                    material = "algodon",
                    capacity = "",
            ),
            provider = "id",
            reviewTotal = 727,
            reviewScore = 3.9f,
            updated_at = "29 de diciembre de 2020, 0:00:00 UTC+1",
            created_at = "29 de diciembre de 2020, 0:00:00 UTC+1"
    )*/


    //endregion fake products

   /* fakeList.add(product1)
    fakeList.add(product2)
    fakeList.add(product3)
    fakeList.add(product4)
    fakeList.add(product5)
    fakeList.add(product6)
    fakeList.add(product7)
*/
    return fakeList
}
