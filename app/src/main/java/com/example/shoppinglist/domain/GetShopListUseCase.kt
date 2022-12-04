package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

class GetShopListUseCase(private val _shopListRepository: ShopListRepository) {

    fun getShopList(): LiveData<List<ShopItem>> {
       return _shopListRepository.getShopList()
    }
}