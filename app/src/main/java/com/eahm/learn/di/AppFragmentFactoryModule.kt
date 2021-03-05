package com.eahm.learn.di

import androidx.lifecycle.ViewModelProvider
import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.framework.presentation.common.FragmentFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Module
object AppFragmentFactoryModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideProductFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory,
        dateUtil: DateUtil,
        productFactory : ProductFactory,
        userFactory : UserFactory
    ): androidx.fragment.app.FragmentFactory {
        return FragmentFactory(
            viewModelFactory,
            dateUtil,
            productFactory,
            userFactory
        )
    }
}