package com.eahm.learn.framework.presentation.orderlistdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eahm.learn.business.domain.model.Order

const val CURRENT_ORDER_BUNDLE_KEY = "Current_Order_Key"

class OrderListDetailViewModel : ViewModel() {

    private val _currentOrder : MutableLiveData<Order?> = MutableLiveData(null)

    val currentOrder : LiveData<Order?>
        get() = _currentOrder

    fun setCurrentOrder(order : Order){
        _currentOrder.value = order
    }


}