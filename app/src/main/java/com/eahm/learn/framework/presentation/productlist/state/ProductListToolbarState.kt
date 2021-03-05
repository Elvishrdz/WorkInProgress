package com.eahm.learn.framework.presentation.productlist.state

sealed class ProductListToolbarState {

    class MultiSelectionState: ProductListToolbarState(){

        override fun toString(): String {
            return "MultiSelectionState"
        }
    }

    class SearchViewState: ProductListToolbarState(){

        override fun toString(): String {
            return "SearchViewState"
        }
    }
}