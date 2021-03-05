package com.eahm.learn.framework.presentation.common

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
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
import com.eahm.learn.framework.presentation.authentication.AuthViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.orderlist.OrderListViewModel
import com.eahm.learn.framework.presentation.orderlistdetail.OrderListDetailViewModel
import com.eahm.learn.framework.presentation.providerorderlist.ProviderOrderListViewModel
import com.eahm.learn.framework.presentation.product.ProductViewModel
import com.eahm.learn.framework.presentation.productdetail.ProductDetailViewModel
import com.eahm.learn.framework.presentation.productlist.ProductListViewModel
import com.eahm.learn.framework.presentation.profile.ProfileViewModel
import com.eahm.learn.framework.presentation.shoppingcart.ShoppingCartViewModel
import com.eahm.learn.framework.presentation.splash.SplashViewModel
import com.eahm.learn.framework.presentation.splash.manager.NetworkSyncManager
import com.eahm.learn.framework.presentation.providerproductlist.UserProductListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class ProductViewModelFactory
@Inject
constructor(
        private val sessionManager : SessionManager,
        private val productInteractors : ProductInteractors,
        private val productListInteractors: ProductListInteractors,
        private val productDetailInteractors: ProductDetailInteractors,
        private val shoppingCartInteractors : ShoppingCartInteractors,
        private val orderListProviderInteractors : OrderListProviderInteractors,
        private val orderListInteractors : OrderListInteractors,
        private val profileInteractors : ProfileInteractors,
        private val userProductListInteractors : UserProductListInteractors,
        private val networkSyncManager: NetworkSyncManager,
        private val productFactory: ProductFactory,
        private val userFactory : UserFactory,
        private val editor: SharedPreferences.Editor,
        private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass){

            ShoppingCartViewModel::class.java ->{
                ShoppingCartViewModel(
                    shoppingCartInteractors = shoppingCartInteractors,
                    sessionManager = sessionManager
                ) as T
            }

            ProductListViewModel::class.java -> {
                ProductListViewModel(
                    productInteractors = productListInteractors,
                    shoppingCartInteractors = shoppingCartInteractors,
                    productFactory = productFactory,
                    editor = editor,
                    sharedPreferences = sharedPreferences,
                    sessionManager = sessionManager
                ) as T
            }

            ProductDetailViewModel::class.java -> {
                ProductDetailViewModel(
                    productDetailInteractors = productDetailInteractors,
                    sessionManager = sessionManager
                ) as T
            }

            SplashViewModel::class.java -> {
                SplashViewModel(
                    networkSyncManager = networkSyncManager,
                    sessionManager = sessionManager
                ) as T
            }

            AuthViewModel::class.java -> {
                AuthViewModel(
                    sessionManager = sessionManager
                ) as T
            }

            ProductViewModel::class.java -> {
                ProductViewModel(
                        productInteractors = productInteractors,
                        sessionManager = sessionManager,
                        productFactory = productFactory
                ) as T
            }

            ProviderOrderListViewModel::class.java -> {
                ProviderOrderListViewModel(
                        sessionManager,
                        orderListProviderInteractors
                ) as T
            }

            ProfileViewModel::class.java ->{
                ProfileViewModel(
                        sessionManager,
                        profileInteractors,
                        userFactory
                ) as T
            }

            OrderListViewModel::class.java -> {
                OrderListViewModel(
                        sessionManager = sessionManager,
                        orderListInteractors = orderListInteractors
                ) as T
            }

            OrderListDetailViewModel::class.java -> {
                OrderListDetailViewModel() as T
            }

            UserProductListViewModel::class.java ->{
                UserProductListViewModel(
                        sessionManager = sessionManager,
                        userProductListInteractors = userProductListInteractors
                ) as T
            }

            else -> {
                throw IllegalArgumentException("unknown model class $modelClass")
            }
        }
    }
}