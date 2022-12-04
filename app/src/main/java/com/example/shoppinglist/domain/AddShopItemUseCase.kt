package com.example.shoppinglist.domain

class AddShopItemUseCase(private val _shopListRepository: ShopListRepository) {
    fun addShopItem(shopItem: ShopItem){
        _shopListRepository.addShopItem(shopItem)
    }
}