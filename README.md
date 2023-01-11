# Shopping list

<img align="right" width="180px" src="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn1.iconfinder.com%2Fdata%2Ficons%2Fshopping-and-commerce-2-9%2F134%2F197-512.png&f=1&nofb=1&ipt=16d6d88e936339cc6bfa5b07492306b509b85d3b95d2640378ed9fd9309fc634&ipo=images" title="shoplist logo" />

This application for android is a shopping list.
I did it for several purposes:
1. Learn how to use the RecyclerView component well
2. Learn how to work with multiple screens and fragments.
3. Learn to use dataBinding/viewBinding

In many ways, this is a typical ToDoList, where you can add/edit/delete things, and their amount needed for purchases. 
Also, things can be made enable/disabled.
## What does this application do?
As mentioned earlier, this application is essentially a toDoList. It allows you to execute UseCase's such as:
1. Delete the purchase item with its quantity
2. Edit the purchase item and its quantity
3. Make it disabled/enabled
---
There are several ways to switch to edit/add mode:
1. In the panel mode of the screen, the edit/delete menu will launch as a `ShopItemFragment.kt` without overlapping the rest of the activity.
2. In normal screen mode, switch to another edit/delete activity `ShopItemActivity.kt`

```kotlin
_binding.buttonAddShopItem.setOnClickListener{
    if(_isOnePaneMode()) {
        val intent = ShopItemActivity.newIntentAddItem(this)
        startActivity(intent)
    } else {
        _launchFragment(ShopItemFragment.newInstanceAddItem())
    }
}
```

## What about a demonstration?
<img src="https://media4.giphy.com/media/Z5UNIzg8Oi4TYb8baL/giphy.gif?cid=790b7611cab6e2eafa4b2c35d8388172b82a7ed5ba83b3d0&rid=giphy.gif&ct=g"/>

## What about architecture

This application was built according to Uncle Bob's "Clean Architecture" rules.
The application is divided into 3 parts.
1. Working with data
2. Business logic
3. Working with the presentation
   
The components of the presentation layer were composed according to the principle of MVVM architecture.


# About RV & data/viewBinding

RV in `activity_main.xml`
```xml
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvShopList"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
        tools:listitem="@layout/item_shop_enabled"/>
```

There are several DiffUtil Kotlin classes:
1. `ShopListDiffCallback.kt` , which runs on the main thread, which is why I did not use this class to optimize the list.
2. `ShopItemDiffCallback.kt` , this class is already used, since its implementation is simpler and it is more productive, since it works in a separate thread
## Data/View Binding
In the first versions of the application, I used findViewById to initialize components, but now I have improved it to use binding.

For good annotation processing we use this project plugin

```gradle
plugins {
    id 'kotlin-kapt'
}
```
to connect the binding, we prescribe the following parameters
```gradle
buildFeatures {
        dataBinding true
    }
```
I created a `BindingAdapters.kt` file to annotate the input error observer in the textInputLayout.
```kotlin
@BindingAdapter("errorInputData")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_data)
    } else {
        null
    }
    textInputLayout.error = message
}
```
```xml
<layout>
    <data>
        <variable
            name="viewModel"
            type="com.example.shoppinglist.presenter.ShopItemViewModel"
            />
    </data>
```
```xml
app:errorInputData="@{viewModel.errorInputData}"
```
