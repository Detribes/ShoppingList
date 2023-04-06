package com.example.shoppinglist.domain

class UpdateShopItemUseCase(private val _shopListRepository: ShopListRepository){
    suspend fun updateShopItem(shopItem: ShopItem){
        _shopListRepository.updateShopItem(shopItem)
    }
}