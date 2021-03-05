package com.eahm.learn.di

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.eahm.learn.business.data.cache.abstraction.*
import com.eahm.learn.business.data.cache.abstraction.provider.OrderProviderCacheDataSource
import com.eahm.learn.business.data.cache.abstraction.provider.ProductProviderCacheDataSource
import com.eahm.learn.business.data.cache.implementation.*
import com.eahm.learn.business.data.cache.implementation.provider.OrderProviderCacheDataSourceImpl
import com.eahm.learn.business.data.cache.implementation.provider.ProductProviderCacheDataSourceImpl
import com.eahm.learn.business.data.network.abstraction.*
import com.eahm.learn.business.data.network.abstraction.ShoppingCartNetworkDataSource
import com.eahm.learn.business.data.network.abstraction.provider.OrderProviderNetworkDataSource
import com.eahm.learn.business.data.network.abstraction.provider.ProductProviderNetworkDataSource
import com.eahm.learn.business.data.network.implementation.*
import com.eahm.learn.business.data.network.implementation.provider.OrderProviderNetworkDataSourceImpl
import com.eahm.learn.business.data.network.implementation.provider.ProductProviderNetworkDataSourceImpl
import com.eahm.learn.business.domain.factory.*
import com.eahm.learn.business.domain.factory.provider.OrderProviderFactory
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.business.interactors.common.DeleteProduct
import com.eahm.learn.business.interactors.common.GetShoppingCartItemAmountNum
import com.eahm.learn.business.interactors.common.InsertProductInCart
import com.eahm.learn.business.interactors.common.sync.SyncProvider
import com.eahm.learn.business.interactors.order.OrderListInteractors
import com.eahm.learn.business.interactors.order.RetrieveOrders
import com.eahm.learn.business.interactors.orderlistprovider.OrderListProviderInteractors
import com.eahm.learn.business.interactors.orderlistprovider.RetrieveOrdersAsProvider
import com.eahm.learn.business.interactors.product.ProductInteractors
import com.eahm.learn.business.interactors.productdetail.ProductDetailInteractors
import com.eahm.learn.business.interactors.productlist.*
import com.eahm.learn.business.interactors.profile.CreateNewProviderRelation
import com.eahm.learn.business.interactors.profile.ProfileInteractors
import com.eahm.learn.business.interactors.shoppingcart.*
import com.eahm.learn.business.interactors.shoppingcart.order.AddNewOrder
import com.eahm.learn.business.interactors.splash.SyncProducts
import com.eahm.learn.business.interactors.userproductlist.GetProviderProducts
import com.eahm.learn.business.interactors.userproductlist.UserProductListInteractors
import com.eahm.learn.framework.datasource.cache.AppDatabase
import com.eahm.learn.framework.datasource.cache.abstraction.*
import com.eahm.learn.framework.datasource.cache.abstraction.provider.OrderProviderDaoService
import com.eahm.learn.framework.datasource.cache.abstraction.provider.ProductProviderDaoService
import com.eahm.learn.framework.datasource.cache.dao.*
import com.eahm.learn.framework.datasource.cache.dao.provider.OrderProviderDao
import com.eahm.learn.framework.datasource.cache.dao.provider.ProductProviderDao
import com.eahm.learn.framework.datasource.cache.implementation.*
import com.eahm.learn.framework.datasource.cache.implementation.provider.OrderProviderServiceImpl
import com.eahm.learn.framework.datasource.cache.mappers.*
import com.eahm.learn.framework.datasource.cache.mappers.provider.OrderProviderCacheMapper
import com.eahm.learn.framework.datasource.cache.mappers.provider.OrderWithProductCacheMapper
import com.eahm.learn.framework.datasource.cache.mappers.provider.ProductProviderCacheMapper
import com.eahm.learn.framework.datasource.network.abstraction.*
import com.eahm.learn.framework.datasource.network.abstraction.provider.OrderProviderFirebaseService
import com.eahm.learn.framework.datasource.network.abstraction.provider.ProductProviderFirestoreService
import com.eahm.learn.framework.datasource.network.implementation.*
import com.eahm.learn.framework.datasource.network.implementation.provider.OrderProviderFirebaseServiceImpl
import com.eahm.learn.framework.datasource.network.implementation.provider.ProductProviderFirestoreServiceImpl
import com.eahm.learn.framework.datasource.network.mappers.*
import com.eahm.learn.framework.datasource.network.mappers.provider.AddressNetworkMapper
import com.eahm.learn.framework.datasource.network.mappers.provider.OrderProviderNetworkMapper
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.profile.manager.ShoppingCartSyncManager
import com.eahm.learn.framework.presentation.splash.manager.NetworkSyncManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@SuppressLint("JvmStaticProvidesInObjectDetector")
@ExperimentalCoroutinesApi
@FlowPreview
@Module
object AppModule {

    // https://developer.android.com/reference/java/text/SimpleDateFormat.html?hl=pt-br

    @JvmStatic
    @Singleton
    @Provides
    fun provideDateFormat(): SimpleDateFormat {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC-7") // match firestore
        return sdf
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDateUtil(dateFormat: SimpleDateFormat): DateUtil {
        return DateUtil(
                dateFormat
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPrefsEditor(
            sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    //region provide Factory's
    @JvmStatic
    @Singleton
    @Provides
    fun providerOrderProviderFactory(
            addressFactory: AddressFactory,
            productFactory : ProductFactory
    ): OrderProviderFactory {
        return OrderProviderFactory(addressFactory, productFactory)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providerAddressFactory(): AddressFactory {
        return AddressFactory()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideBusinessFactory(): BusinessFactory {
        return BusinessFactory()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providerOrderFactory(): OrderFactory = OrderFactory()

    @JvmStatic
    @Singleton
    @Provides
    fun provideProductFactory(dateUtil: DateUtil): ProductFactory {
        return ProductFactory(
                dateUtil
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserFactory(dateUtil : DateUtil): UserFactory {
        return UserFactory(dateUtil)
    }



    //endregion provide Factory's

    //region provide data access objects
    @JvmStatic
    @Singleton
    @Provides
    fun provideProductDAO(appDatabase: AppDatabase): ProductDao =
            appDatabase.productDao()


    @JvmStatic
    @Singleton
    @Provides
    fun provideShoppingCartDao(appDatabase : AppDatabase) : ShoppingCartDao =
            appDatabase.shoppingCartDao()


    @JvmStatic
    @Singleton
    @Provides
    fun provideProviderDao(appDatabase : AppDatabase) : ProviderDao =
            appDatabase.providerDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideBusinessDao(appDatabase : AppDatabase) : BusinessDao =
            appDatabase.businessDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserDao(appDatabase : AppDatabase) : UserDao =
            appDatabase.userDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideClientDao(appDatabase : AppDatabase) : ClientDao =
            appDatabase.clientDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderDao(appDatabase : AppDatabase) : OrderDao = appDatabase.orderDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderProviderDao(appDatabase : AppDatabase) : OrderProviderDao = appDatabase.orderProviderDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideProductProviderDao(appDatabase : AppDatabase) : ProductProviderDao = appDatabase.productProviderDao()



    //endregion provide data access objects

    //region provide dao services
    @JvmStatic
    @Singleton
    @Provides
    fun provideProductDaoService(
            productDao: ProductDao,
            productEntityMapper: ProductCacheMapper,
            dateUtil: DateUtil
    ): ProductDaoService {
        return ProductDaoServiceImpl(productDao, productEntityMapper, dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideShoppingCartDaoService(
            shoppingCartDao : ShoppingCartDao,
            productCacheMapper : ProductCacheMapper,
            shoppingCartCacheMapper: ShoppingCartCacheMapper
    ) : ShoppingCartDaoService{
        return ShoppingCartDaoServiceImpl(
                shoppingCartDao,
                productCacheMapper,
                shoppingCartCacheMapper
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideProviderDaoService(
            providerDao : ProviderDao,
            providerCacheMapper : ProviderCacheMapper,
    ) : ProviderDaoService {
        return ProviderDaoServiceImpl(
                providerDao,
                providerCacheMapper
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideBusinessDaoService(
            businessDao : BusinessDao,
            businessCacheMapper : BusinessCacheMapper,
    ) : BusinessDaoService {
        return BusinessDaoServiceImpl(
                businessDao,
                businessCacheMapper
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserDaoService(
            userDao : UserDao,
            clientDao: ClientDao,
            userCacheMapper : UserCacheMapper,
            clientCacheMapper : ClientCacheMapper
    ) : UserDaoService {
        return UserDaoServiceImpl(
                userDao,
                clientDao,
                userCacheMapper,
                clientCacheMapper
        )
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderService(
            orderDao: OrderDao,
            orderCacheMapper: OrderCacheMapper
    ) : OrderDaoService {
        return OrderDaoServiceImpl (
                orderDao,
                orderCacheMapper
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderProviderService(
            orderProviderDao: OrderProviderDao,
            orderProviderCacheMapper: OrderProviderCacheMapper
    ) : OrderProviderDaoService {
        return OrderProviderServiceImpl(
                orderProviderDao,
                orderProviderCacheMapper
        )
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideProductProviderDaoService(
            productProviderDao: ProductProviderDao,
            productProviderCacheMapper: ProductProviderCacheMapper
    ) : ProductProviderDaoService {
        return ProductProviderDaoServiceImpl(
                productProviderDao,
                productProviderCacheMapper
        )
    }

    //endregion provide dao services

    //region cache object mappers
    @JvmStatic
    @Singleton
    @Provides
    fun provideProductCacheMapper(dateUtil: DateUtil, gson : Gson): ProductCacheMapper {
        return ProductCacheMapper(dateUtil, gson)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providesShoppingCartCacheMapper(
            productFactory : ProductFactory
    ) : ShoppingCartCacheMapper =
            ShoppingCartCacheMapper(productFactory)

    @JvmStatic
    @Singleton
    @Provides
    fun providesProviderCacheMapper(
            businessFactory : BusinessFactory,
            userFactory : UserFactory
    ) : ProviderCacheMapper =
            ProviderCacheMapper(
                    businessFactory,
                    userFactory
            )

    @JvmStatic
    @Singleton
    @Provides
    fun providesBusinessCacheMapper() : BusinessCacheMapper =
            BusinessCacheMapper()

    @JvmStatic
    @Singleton
    @Provides
    fun providesUserCacheMapper(
            gson : Gson,
            userFactory: UserFactory
    ) : UserCacheMapper  {
        return UserCacheMapper(
                gson,
                userFactory
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providesClientCacheMapper(
            gson : Gson,
            userFactory: UserFactory
    ) : ClientCacheMapper  {
        return ClientCacheMapper(
                gson,
                userFactory
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providesOrderCacheMapper(
            orderFactory: OrderFactory,
            gson : Gson
    ) : OrderCacheMapper = OrderCacheMapper(
            orderFactory,
            gson
    )

    @JvmStatic
    @Singleton
    @Provides
    fun providesOrderWithProductCacheMapper(
            orderProviderFactory : OrderProviderFactory,
            orderProviderCacheMapper: OrderProviderCacheMapper,
            productCacheMapper: ProductCacheMapper
    ) : OrderWithProductCacheMapper = OrderWithProductCacheMapper(
            orderProviderFactory,
            orderProviderCacheMapper,
            productCacheMapper
    )

    @JvmStatic
    @Singleton
    @Provides
    fun providesOrderProviderCacheMapper(
            productFactory: ProductFactory,
            orderProviderFactory: OrderProviderFactory,
            gson : Gson
    ) : OrderProviderCacheMapper = OrderProviderCacheMapper(
            productFactory,
            orderProviderFactory,
            gson
    )

    @JvmStatic
    @Singleton
    @Provides
    fun providesProductProviderCacheMapper() : ProductProviderCacheMapper = ProductProviderCacheMapper()

    //endregion object mappers

    //region provide cache data source

    @JvmStatic
    @Singleton
    @Provides
    fun provideProductCacheDataSource(
            productDaoService: ProductDaoService
    ): ProductCacheDataSource {
        return ProductCacheDataSourceImpl(productDaoService)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideShoppingCartCacheDataSource(
            shoppingCartDaoService : ShoppingCartDaoService
    ) : ShoppingCartCacheDataSource{
        return ShoppingCartCacheDataSourceImpl(shoppingCartDaoService)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideProviderCacheDataSource(
            providerDaoService : ProviderDaoService
    ) : ProviderCacheDataSource {
        return ProviderCacheDataSourceImpl(providerDaoService)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideBusinessCacheDataSource(
            businessDaoService : BusinessDaoService
    ) : BusinessCacheDataSource {
        return BusinessCacheDataSourceImpl(businessDaoService)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserCacheDataSource(
            userDaoService : UserDaoService
    ) : UserCacheDataSource {
        return UserCacheDataSourceImpl(userDaoService)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderCacheDataSource(
            orderDaoService : OrderDaoService
    ) : OrderCacheDataSource {
        return OrderCacheDataSourceImpl(
                orderDaoService
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderProviderCacheDataSource(
            orderProviderDaoService : OrderProviderDaoService
    ) : OrderProviderCacheDataSource {
        return OrderProviderCacheDataSourceImpl(
                orderProviderDaoService
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideProductProviderCacheDataSource(
            productProviderDaoService : ProductProviderDaoService
    ) : ProductProviderCacheDataSource {
        return ProductProviderCacheDataSourceImpl(
                productProviderDaoService
        )
    }

    //endregion provide cache data source

    //region provide firestore services
    @JvmStatic
    @Singleton
    @Provides
    fun provideProductFirestoreService(
            firebaseFirestore: FirebaseFirestore,
            networkMapper: ProductNetworkMapper
    ): ProductFirestoreService {
        return ProductFirestoreServiceImpl(
                firebaseFirestore,
                networkMapper
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideProviderFirestoreService(
            firebaseFirestore: FirebaseFirestore,
            networkMapper : ProviderNetworkMapper
    ): ProviderFirestoreService {
        return ProviderFirestoreServiceImpl(
                firebaseFirestore,
                networkMapper
        )
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideBusinessFirestoreService(
            firebaseFirestore: FirebaseFirestore,
            networkMapper : BusinessNetworkMapper
    ): BusinessFirestoreService {
        return BusinessFirestoreServiceImpl(
                firebaseFirestore,
                networkMapper
        )
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideUserFirestoreService(
            firebaseFirestore: FirebaseFirestore,
            networkMapper : UserNetworkMapper
    ): UserFirestoreService {
        return UserFirestoreServiceImpl(
                firebaseFirestore,
                networkMapper
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderFirestoreService(
            firebaseFirestore: FirebaseFirestore,
            orderNetworkMapper: OrderNetworkMapper,
            gson : Gson
    ): OrderFirestoreService {
        return OrderFirestoreServiceImpl(
                firebaseFirestore,
                orderNetworkMapper,
                gson
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderProviderFirestoreService(
            firebaseFirestore: FirebaseFirestore,
            orderProviderNetworkMapper : OrderProviderNetworkMapper
    ): OrderProviderFirebaseService {
        return OrderProviderFirebaseServiceImpl(
                firebaseFirestore,
                orderProviderNetworkMapper
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideShoppingCartFirestoreService(
            firebaseFirestore: FirebaseFirestore,
            shoppingCartNetworkMapper: ShoppingCartNetworkMapper
    ): ShoppingCartFirestoreService {
        return ShoppingCartFirestoreServiceImpl(
                firebaseFirestore,
                shoppingCartNetworkMapper
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideProductProviderFirestoreService(
            firebaseFirestore: FirebaseFirestore,
            productFactory: ProductFactory
    ): ProductProviderFirestoreService {
        return ProductProviderFirestoreServiceImpl(
                firebaseFirestore,
                productFactory
        )
    }

    //endregion provide firestore services

    //region network object mappers
    @JvmStatic
    @Singleton
    @Provides
    fun provideProductNetworkMapper(dateUtil: DateUtil): ProductNetworkMapper {
        return ProductNetworkMapper(dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideProviderNetworkMapper(
            businessFactory : BusinessFactory,
            userFactory : UserFactory
    ): ProviderNetworkMapper {
        return ProviderNetworkMapper(
                businessFactory,
                userFactory
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideBusinessNetworkMapper(): BusinessNetworkMapper = BusinessNetworkMapper()

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserNetworkMapper(
            dateUtil : DateUtil,
            addressMapper : UserAddressNetworkMapper,
            phoneNumberMapper : UserPhoneNumberNetworkMapper,
            userFactory: UserFactory
    ): UserNetworkMapper {
        return UserNetworkMapper(
                dateUtil,
                addressMapper,
                phoneNumberMapper,
                userFactory
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserAddressNetworkMapper() : UserAddressNetworkMapper {
        return UserAddressNetworkMapper()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserPhoneNumberNetworkMapper() : UserPhoneNumberNetworkMapper {
        return UserPhoneNumberNetworkMapper()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideAddressNetworkMapper() : AddressNetworkMapper {
        return AddressNetworkMapper()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderProviderNetworkMapper(
            addressNetworkMapper : AddressNetworkMapper,
            productFactory: ProductFactory,
            gson : Gson
    ) : OrderProviderNetworkMapper {
        return OrderProviderNetworkMapper(
                addressNetworkMapper,
                productFactory,
                gson
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderNetworkMapper() : OrderNetworkMapper = OrderNetworkMapper()

    @JvmStatic
    @Singleton
    @Provides
    fun provideShoppingCartNetworkMapper(
            productFactory : ProductFactory
    ) : ShoppingCartNetworkMapper = ShoppingCartNetworkMapper(
        productFactory
    )

    //endregion network object mappers

    //region provider network data source
    @JvmStatic
    @Singleton
    @Provides
    fun provideProductNetworkDataSource(
            firestoreService: ProductFirestoreServiceImpl
    ): ProductNetworkDataSource {
        return ProductNetworkDataSourceImpl(
                firestoreService
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideProviderNetworkDataSource(
            firestoreService: ProviderFirestoreServiceImpl
    ): ProviderNetworkDataSource {
        return ProviderNetworkDataSourceImpl(
                firestoreService
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideBusinessNetworkDataSource(
            firestoreService: BusinessFirestoreServiceImpl
    ): BusinessNetworkDataSource {
        return BusinessNetworkDataSourceImpl(
                firestoreService
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserNetworkDataSource(
            firestoreService: UserFirestoreServiceImpl
    ): UserNetworkDataSource {
        return UserNetworkDataSourceImpl(
                firestoreService
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderNetworkDataSource(
            orderFirestoreService : OrderFirestoreServiceImpl
    ) : OrderNetworkDataSource {
        return OrderNetworkDataSourceImpl(orderFirestoreService)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideShoppingCartNetworkDataSource(
            shoppingCartFirestoreService: ShoppingCartFirestoreService
    ) : ShoppingCartNetworkDataSource {
        return ShoppingCartNetworkDataSourceImpl(
                shoppingCartFirestoreService
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOrderProviderNetworkDataSource(
            orderProviderFirebaseService: OrderProviderFirebaseService
    ) : OrderProviderNetworkDataSource {
        return OrderProviderNetworkDataSourceImpl(
                orderProviderFirebaseService
        )
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideProductProviderNetworkDataSource(
            productProviderFirestoreService: ProductProviderFirestoreService
    ) : ProductProviderNetworkDataSource {
        return ProductProviderNetworkDataSourceImpl(
                productProviderFirestoreService
        )
    }



    //endregion provider network data source

    //region provide interactors
    @JvmStatic
    @Singleton
    @Provides
    fun provideProductDetailInteractors(
            productCacheDataSource: ProductCacheDataSource,
            shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
            shoppingCartNetworkDataSource: ShoppingCartNetworkDataSource,
            productNetworkDataSource: ProductNetworkDataSource,
            syncProvider : SyncProvider

    ): ProductDetailInteractors {
        return ProductDetailInteractors(
                DeleteProduct(productCacheDataSource, productNetworkDataSource),
                InsertProductInCart(shoppingCartCacheDataSource, shoppingCartNetworkDataSource),
                SearchProductInCart(shoppingCartCacheDataSource),
                syncProvider
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideProductListInteractors(
            productCacheDataSource: ProductCacheDataSource,
            productNetworkDataSource: ProductNetworkDataSource,
            shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
            productFactory: ProductFactory
    ): ProductListInteractors {
        return ProductListInteractors(
                InsertNewProduct(productCacheDataSource, productNetworkDataSource, productFactory),
                DeleteProduct(productCacheDataSource, productNetworkDataSource),
                SearchProducts(productCacheDataSource),
                GetNumProducts(productCacheDataSource),
                RestoreDeletedProduct(productCacheDataSource, productNetworkDataSource),
                DeleteMultipleProducts(productCacheDataSource, productNetworkDataSource),
                GetShoppingCartItemAmountNum(shoppingCartCacheDataSource)
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideShoppingCartInteractors(
            shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
            shoppingCartNetworkDataSource: ShoppingCartNetworkDataSource,
            orderNetworkDataSource : OrderNetworkDataSource,
            orderCacheDataSource: OrderCacheDataSource
    ) : ShoppingCartInteractors{
        return ShoppingCartInteractors(
                DeleteItem(shoppingCartCacheDataSource, shoppingCartNetworkDataSource),
                UpdateItemAmount(shoppingCartCacheDataSource, shoppingCartNetworkDataSource),
                GetAllItems(shoppingCartCacheDataSource),
                GetShoppingCartItemAmountNum(shoppingCartCacheDataSource),
                AddNewOrder(orderNetworkDataSource, orderCacheDataSource, shoppingCartCacheDataSource, shoppingCartNetworkDataSource)
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideProductInteractors(
            productCacheDataSource : ProductCacheDataSource,
            productNetworkDataSource: ProductNetworkDataSource,
            productFactory: ProductFactory
    ) : ProductInteractors {
        return ProductInteractors(
                InsertNewProduct(productCacheDataSource, productNetworkDataSource, productFactory)
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideOrderListProviderInteractors(
            orderProviderCacheDataSource: OrderProviderCacheDataSource,
            orderProviderNetworkDataSource: OrderProviderNetworkDataSource
    ) : OrderListProviderInteractors {
        return OrderListProviderInteractors(
                RetrieveOrdersAsProvider(
                        orderProviderCacheDataSource,
                        orderProviderNetworkDataSource
                )
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideProfileInteractors(
            providerCacheDataSource: ProviderCacheDataSource,
            providerNetworkDataSource: ProviderNetworkDataSource,
            userNetworkDataSource: UserNetworkDataSource,
            userCacheDataSource: UserCacheDataSource,
    ) : ProfileInteractors {
        return ProfileInteractors(
            CreateNewProviderRelation(
                providerCacheDataSource,
                providerNetworkDataSource,
                userNetworkDataSource,
                userCacheDataSource
            )
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideOrderListInteractors(
         orderCacheDataSource: OrderCacheDataSource,
         orderNetworkDataSource: OrderNetworkDataSource
    ) : OrderListInteractors {
        return OrderListInteractors(
            RetrieveOrders(
                orderCacheDataSource,
                orderNetworkDataSource
            )
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideUserProductListInteractors(
         productProviderCacheDataSource: ProductProviderCacheDataSource,
         productProviderNetworkDataSource: ProductProviderNetworkDataSource
    ) : UserProductListInteractors {
        return UserProductListInteractors(
            GetProviderProducts(productProviderCacheDataSource, productProviderNetworkDataSource)
        )
    }

    //endregion provide interactors

    @JvmStatic
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSyncProducts(
            productCacheDataSource: ProductCacheDataSource,
            productNetworkDataSource: ProductNetworkDataSource,
            productFactory : ProductFactory,
            providerNetworkDataSource : ProviderNetworkDataSource,
            syncProvider : SyncProvider
    ): SyncProducts {
        return SyncProducts(
                productCacheDataSource,
                productNetworkDataSource,
                productFactory,
                providerNetworkDataSource,
                syncProvider
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providesSyncProviderManager(
            providerNetworkDataSource: ProviderNetworkDataSource,
            businessNetworkDataSource: BusinessNetworkDataSource,
            userNetworkDataSource: UserNetworkDataSource,
            providerCacheDataSource: ProviderCacheDataSource,
            businessCacheDataSource: BusinessCacheDataSource,
            userCacheDataSource: UserCacheDataSource
    ) : SyncProvider {
        return  SyncProvider(
                providerNetworkDataSource,
                businessNetworkDataSource,
                userNetworkDataSource,
                providerCacheDataSource,
                businessCacheDataSource,
                userCacheDataSource
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providesShoppingCartSyncManager(
        shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
        shoppingCartNetworkDataSource: ShoppingCartNetworkDataSource
    ) : ShoppingCartSyncManager {
        return  ShoppingCartSyncManager(
                shoppingCartCacheDataSource,
                shoppingCartNetworkDataSource,
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNetworkSyncManager(
            syncProducts: SyncProducts
    ): NetworkSyncManager {
        return NetworkSyncManager(
                syncProducts
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSessionManager(
            firebaseAuth : FirebaseAuth,
            userCacheDataSource: UserCacheDataSource,
            userNetworkDataSource: UserNetworkDataSource,
            userFactory: UserFactory,
            shoppingCartSyncManager : ShoppingCartSyncManager,

            shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
            orderProviderCacheDataSource: OrderProviderCacheDataSource,
            orderCacheDataSource: OrderCacheDataSource

    ) : SessionManager{
        return SessionManager(
                firebaseAuth,
                userCacheDataSource,
                userNetworkDataSource,
                userFactory,
                shoppingCartSyncManager,

                shoppingCartCacheDataSource,
                orderProviderCacheDataSource,
                orderCacheDataSource
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

}

