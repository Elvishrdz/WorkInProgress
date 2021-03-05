package com.eahm.learn.framework.datasource.cache.implementation

import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.framework.datasource.cache.abstraction.ShoppingCartDaoService
import com.eahm.learn.framework.datasource.cache.dao.ShoppingCartDao
import com.eahm.learn.framework.datasource.cache.mappers.ProductCacheMapper
import com.eahm.learn.framework.datasource.cache.mappers.ShoppingCartCacheMapper
import com.eahm.learn.utils.logD
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCartDaoServiceImpl
@Inject
constructor(
    private val shoppingCartDao : ShoppingCartDao,
    private val productCacheMapper : ProductCacheMapper,
    private val shoppingCartCacheMapper : ShoppingCartCacheMapper
): ShoppingCartDaoService {

    private val TAG = "ShoppingCartDaoServiceImpl"

    override suspend fun getAllShoppingCart(isLocal : Boolean): List<ShoppingCart> {
        //val result = shoppingCartDao.getAllShoppingCartWithProduct()

        val result = if(isLocal)
            shoppingCartDao.getAllLocalShoppingCart()
        else
            shoppingCartDao.getAllNonLocalShoppingCart()

        val shoppingCart : MutableList<ShoppingCart> = mutableListOf()

        for(element in result){
            shoppingCart.add(
                ShoppingCart(
                    id = element.shoppingCart.id,
                    product = productCacheMapper.mapFromEntity(element.product),
                    amount = element.shoppingCart.amount
                )
            )
        }

        logD(TAG, "getAllShoppingCart size: ${shoppingCart.size}")
        return shoppingCart
    }

    override suspend fun insertInShoppingCart(newProduct: ShoppingCart, isLocal : Boolean) : Long {
        logD(TAG, "insertInShoppingCart")
        return shoppingCartDao.insertInShoppingCart(shoppingCartCacheMapper.mapToEntity(newProduct, isLocal))
    }

    override suspend fun removeFromShoppingCart(shoppingCartId: String): Int
    = shoppingCartDao.removeFromShoppingCart(shoppingCartId)

    override suspend fun cleanShoppingCart(isLocal : Boolean) : Int {
        return if(isLocal)
            shoppingCartDao.deleteLocals()
        else
            shoppingCartDao.deleteNonLocals()
    }

    override suspend fun updateAmount(shoppingCartId: String, newAmount: Int): Int
    = shoppingCartDao.updateAmount(shoppingCartId, newAmount)

    override suspend fun searchItem(productId: String): ShoppingCart?{
        /*val result = shoppingCartDao.searchItem(productId)
        logD(TAG, "Search item result: ${result?.productID} | ${result?.amount}")

        if(result != null){
           return shoppingCartCacheMapper.mapFromEntity(
                    result
            )
        }
        else return null*/


        return shoppingCartDao.searchItem(productId)?.let { result ->
            logD(TAG, "Search item result: ${result.productID} | ${result.amount}")
            shoppingCartCacheMapper.mapFromEntity(result)
        }
    }

    override suspend fun getItemsAmount(isLocal : Boolean): Int =
        if(isLocal)
            shoppingCartDao.getLocalItemsAmount()
        else
            shoppingCartDao.getNonLocalItemsAmount()

}