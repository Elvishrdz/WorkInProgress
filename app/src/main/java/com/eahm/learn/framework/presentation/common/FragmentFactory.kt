package com.eahm.learn.framework.presentation.common

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.domain.util.DateUtil
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class FragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dateUtil: DateUtil,
    private val productFactory : ProductFactory,
    private val userFactory: UserFactory,
): FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String)
    = when(className){

        ShoppingCartFragment::class.java.name -> {
            val fragment = ShoppingCartFragment(viewModelFactory)
            fragment
        }

        ProductDetailFragment::class.java.name -> {
            val fragment = ProductDetailFragment(viewModelFactory)
            fragment
        }

        ProductListFragment::class.java.name -> {
            val fragment = ProductListFragment(viewModelFactory, dateUtil)
            fragment
        }

        SplashFragment::class.java.name -> {
            val fragment = SplashFragment(viewModelFactory)
            fragment
        }

        AuthFragment::class.java.name -> {
            val fragment = AuthFragment(viewModelFactory, userFactory)
            fragment
        }

        ProductFragment::class.java.name -> {
            val fragment = ProductFragment(viewModelFactory)
            fragment
        }

        ProviderOrderListFragment::class.java.name -> {
            ProviderOrderListFragment(viewModelFactory)
        }

        ProfileFragment::class.java.name -> {
            ProfileFragment(viewModelFactory)
        }

        OrderListFragment::class.java.name -> {
            OrderListFragment(viewModelFactory)
        }

        OrderListDetailFragment::class.java.name -> {
            OrderListDetailFragment(viewModelFactory)
        }

        UserProductListFragment::class.java.name ->{
            UserProductListFragment(viewModelFactory)
        }

        else -> {
            super.instantiate(classLoader, className)
        }
    }
}