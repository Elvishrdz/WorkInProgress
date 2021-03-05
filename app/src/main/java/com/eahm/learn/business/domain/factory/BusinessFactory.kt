package com.eahm.learn.business.domain.factory

import com.eahm.learn.business.domain.model.Business
import javax.inject.Singleton

@Singleton
class BusinessFactory {

    fun createBusiness(id: String): Business? {
        return if(id.isNotEmpty()){
            Business(
                id = id,
                name = "",
                status = ""
            )
        }
        else null
    }
}