package com.example.shoppinglist.domain

import androidx.lifecycle.MutableLiveData

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun getShopItem(shopItemId: Int) :ShopItem
    fun updateShopItem(shopItem: ShopItem)
    fun getShopList() :MutableLiveData<List<ShopItem>>
}