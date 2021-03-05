package com.eahm.learn.framework.datasource.cache.dao

import androidx.room.*
import com.eahm.learn.framework.datasource.cache.model.ProductCacheEntity

@Dao
interface ProductDao {

    //region insertion
    @Insert
    suspend fun insertProduct(product: ProductCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProducts(products: List<ProductCacheEntity>): LongArray
    //endregion insertion

    //region deletion
    @Query("DELETE FROM products WHERE id = :primaryKey")
    suspend fun deleteProduct(primaryKey: String): Int

    @Query("DELETE FROM products WHERE id IN (:ids)")
    suspend fun deleteProducts(ids: List<String>): Int

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
    //endregion deletion

    //region search
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun searchProduct(id: String): ProductCacheEntity?

    //region get products
    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<ProductCacheEntity>

    // double bars ||  means concatenations and percentage simbol % is any amount of chars, underscore _ is one char ( _a%b -- a string that his second char is an 'a' and the last character is an 'b' )
    @Query("""
        SELECT * FROM products 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY updated_at ASC LIMIT (:page * :pageSize)
        """)
    suspend fun getProductsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PRODUCT_PAGINATION_PAGE_SIZE
    ): List<ProductCacheEntity>

    @Query("""
        SELECT * FROM products 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY updated_at DESC LIMIT (:page * :pageSize)
        """)
    suspend fun getProductsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PRODUCT_PAGINATION_PAGE_SIZE
    ): List<ProductCacheEntity>

    @Query("""
        SELECT * FROM products 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY title ASC LIMIT (:page * :pageSize)
        """)
    suspend fun getProductsOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int = PRODUCT_PAGINATION_PAGE_SIZE
    ): List<ProductCacheEntity>

    @Query("""
        SELECT * FROM products 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY title DESC LIMIT (:page * :pageSize)
        """)
    suspend fun getProductsOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int = PRODUCT_PAGINATION_PAGE_SIZE
    ): List<ProductCacheEntity>
    //endregion get products
    //endregion search

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getNumProducts(): Int

    @Query(
        """
        UPDATE products 
        SET 
        title = :title,  
        description = :description,
        updated_at = :updated_at
        WHERE id = :primaryKey
        """
    )
    suspend fun updateProduct(
        primaryKey: String,
        title: String,
        description: String?,
        updated_at: String
    ): Int

}
