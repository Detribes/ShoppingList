package com.example.shoppinglist.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener :OnShopItemLongClickListener? = null
    var onShopItemClickListener :OnShopItemClickListener? = null

    /*ViewHolder for adapter creates in his method.
    VH very useful to create metadata for Adapter to reuse it
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown ViewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.onShopItemLongClickListener(shopItem)
            true
        }
        holder.view.setOnClickListener{
            onShopItemClickListener?.onShopItemClickListener(shopItem)
        }
        holder.tvName.text = "${shopItem.name}"
        holder.tvCount.text = shopItem.count.toString()
    }
    /* That method takes position of list of objects
    and return a number that appear that obj is active or unactive
     Result of this method comes to parameter in onCreateViewHolder as viewType*/
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.active) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }

    }

    override fun onViewRecycled(holder: ShopItemViewHolder) {
        super.onViewRecycled(holder)
        holder.tvName.text = ""
        holder.tvCount.text = ""
        holder.tvName.setTextColor(
            ContextCompat.getColor(
                holder.view.context,
                android.R.color.white
            )
        )
    }

    /*Interface that use to set on click listener
     */
    interface OnShopItemLongClickListener {
        fun onShopItemLongClickListener(shopItem: ShopItem)
    }
    interface OnShopItemClickListener {
        fun onShopItemClickListener(shopItem: ShopItem)
    }
    /* CONSTANTS of magic numbers that means
    is viewType is active or unactive
    MAX_POOL_SIZE is max count of objects, that
    RV creates
     */
    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
        const val MAX_POOL_SIZE = 25
    }
}