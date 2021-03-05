package com.eahm.learn.business.domain.factory

import com.eahm.learn.business.domain.model.Address
import com.eahm.learn.business.domain.model.Location
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressFactory
@Inject
constructor(){

    fun createAddress() : Address {
        return Address(
            street = "",
            buildingNumber = "",
            postCode = "",
            countryCode = "",
            description  = "",
            geolocation = Location()
        )
    }
}