package com.eahm.learn.di

import com.eahm.learn.framework.presentation.BaseApplication
import com.eahm.learn.framework.presentation.MainActivity
import com.eahm.learn.framework.presentation.authentication.AuthFragment
import com.eahm.learn.framework.presentation.orderlist.OrderListFragment
import com.eahm.learn.framework.presentation.orderlistdetail.OrderListDetailFragment
import com.eahm.learn.framework.presentation.providerorderlist.ProviderOrderListFragment
import com.eahm.learn.framework.presentation.product.ProductFragment
import com.eahm.learn.framework.presentation.productdetail.ProductDetailFragment
import com.eahm.learn.framework.presentation.productlist.ProductListFragment
import com.eahm.learn.framework.presentation.profile.ProfileFragment
import com.eahm.learn.framework.presentation.shoppingcart.ShoppingCartFragment
import com.eahm.learn.framework.presentation.splash.SplashFragment
import com.eahm.learn.framework.presentation.providerproductlist.UserProductListFragment
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
@Component(modules = [
    AppModule::class,
    ProductionModule::class,
    AppFragmentFactoryModule::class,
    ProductViewModelModule::class
])
interface AppComponent   {


    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: BaseApplication): AppComponent
    }


    // Manual Injections

    fun inject(mainActivity: MainActivity)

    fun inject(splashFragment : SplashFragment)

    fun inject(productListFragment: ProductListFragment)

    fun inject(shoppingCartFragment : ShoppingCartFragment)

    fun inject(authFragment : AuthFragment)

    fun inject(productFragment : ProductFragment)

    fun inject(productDetailFragment : ProductDetailFragment)

    fun inject(providerOrderListFragment : ProviderOrderListFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject(orderListFragment: OrderListFragment)
    fun inject(orderListDetailFragment: OrderListDetailFragment)

    fun inject(userProductListFragment: UserProductListFragment)


}