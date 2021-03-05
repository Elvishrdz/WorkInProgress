package com.eahm.learn.framework.presentation.productlist.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.framework.presentation.productlist.state.ProductListToolbarState.*

class ProductListInteractionManager {

    //region variables
    private val _selectedProducts: MutableLiveData<ArrayList<Product>> = MutableLiveData()
    private val _currentProducts : MutableLiveData<ArrayList<Product>> = MutableLiveData()
    private val _toolbarState: MutableLiveData<ProductListToolbarState> = MutableLiveData(SearchViewState())

    val selectedProducts: LiveData<ArrayList<Product>>
        get() = _selectedProducts

    val currentProducts : LiveData<ArrayList<Product>>
        get() = _currentProducts

    val toolbarState: LiveData<ProductListToolbarState>
        get() = _toolbarState

    //endregion variables

    fun setToolbarState(state: ProductListToolbarState){
        _toolbarState.value = state
    }

    fun isMultiSelectionStateActive(): Boolean{
        return _toolbarState.value.toString() == MultiSelectionState().toString()
    }

    fun getSelectedProducts():ArrayList<Product> = _selectedProducts.value?: ArrayList()

    fun addOrRemoveProductFromSelectedList(product: Product){
        var list = _selectedProducts.value
        if(list == null){
            list = ArrayList()
        }
        if (list.contains(product)){
            list.remove(product)
        }
        else{
            list.add(product)
        }
        _selectedProducts.value = list
    }

    fun isProductSelected(product: Product): Boolean{
        return _selectedProducts.value?.contains(product)?: false
    }

    fun clearSelectedProducts(){
        _selectedProducts.value = null
    }

    fun setCurrentProducts(currentProducts : ArrayList<Product>){
        _currentProducts.value = currentProducts
    }

}



