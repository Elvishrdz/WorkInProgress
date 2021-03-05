package com.eahm.learn.di

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.interactors.order.OrderListInteractors
import com.eahm.learn.business.interactors.orderlistprovider.OrderListProviderInteractors
import com.eahm.learn.business.interactors.product.ProductInteractors
import com.eahm.learn.business.interactors.productdetail.ProductDetailInteractors
import com.eahm.learn.business.interactors.productlist.ProductListInteractors
import com.eahm.learn.business.interactors.profile.ProfileInteractors
import com.eahm.learn.business.interactors.shoppingcart.ShoppingCartInteractors
import com.eahm.learn.business.interactors.userproductlist.UserProductListInteractors
import com.eahm.learn.framework.presentation.common.ProductViewModelFactory
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.splash.manager.NetworkSyncManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@FlowPreview
@Module
object ProductViewModelModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideProductViewModelFactory(
            sessionManager : SessionManager,
            productInteractors : ProductInteractors,
            productListInteractors: ProductListInteractors,
            productDetailInteractors: ProductDetailInteractors,
            shoppingCartInteractors : ShoppingCartInteractors,
            orderListProviderInteractors : OrderListProviderInteractors,
            orderListInteractors : OrderListInteractors,
            profileInteractors : ProfileInteractors,
            userProductListInteractors : UserProductListInteractors,
            networkSyncManager: NetworkSyncManager,
            productFactory: ProductFactory,
            userFactory : UserFactory,
            editor: SharedPreferences.Editor,
            sharedPreferences: SharedPreferences,
    ): ViewModelProvider.Factory{
        return ProductViewModelFactory(
            productInteractors = productInteractors,
            productListInteractors = productListInteractors,
            productDetailInteractors = productDetailInteractors,
            shoppingCartInteractors = shoppingCartInteractors,
            productFactory = productFactory,
            networkSyncManager = networkSyncManager,
            editor = editor,
            sharedPreferences = sharedPreferences,
            sessionManager = sessionManager,
            orderListProviderInteractors = orderListProviderInteractors,
            orderListInteractors = orderListInteractors,
            profileInteractors = profileInteractors,
            userProductListInteractors = userProductListInteractors,
            userFactory = userFactory
        )
    }

}