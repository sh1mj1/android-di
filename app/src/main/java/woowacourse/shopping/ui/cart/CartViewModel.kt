package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sh1mj1.Inject
import com.example.sh1mj1.Qualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.BaseViewModelFactory
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.Product

class CartViewModel : ViewModel() {
    @Inject
    @Qualifier("RoomDao")
    lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.allCartProducts()
        }
    }

    fun deleteCartProduct(id: Int) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id.toLong())
            _onCartProductDeleted.value = true
            _cartProducts.value = cartRepository.allCartProducts()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val appContainer = (this[APPLICATION_KEY] as ShoppingApplication).container
                    BaseViewModelFactory(appContainer).create(CartViewModel::class.java)
                }
            }
    }
}
