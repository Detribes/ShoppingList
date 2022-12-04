package com.example.shoppinglist.domain

class DeleteShopItemUseCase(private val _shopListRepository: ShopListRepository) {
    fun deleteShopItem(shopItem: ShopItem) {
        _shopListRepository.deleteShopItem(shopItem)
    }
}