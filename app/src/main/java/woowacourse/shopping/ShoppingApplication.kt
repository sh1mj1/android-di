package woowacourse.shopping

import android.app.Application
import woowacourse.di.InjectedActivityContainer
import woowacourse.di.InjectedComponent
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.IDateFormatter

class ShoppingApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

        container.injectedComponentContainer.add(
            InjectedComponent.InjectedSingletonComponent(ProductRepository::class, DefaultProductRepository()),
        )

        container.injectedComponentContainer.add(
            InjectedComponent.InjectedSingletonComponent(CartRepository::class, DefaultCartRepository()),
        )

//        container.injectedComponentContainer.add(
//            InjectedComponent.InjectedActivityComponent(IDateFormatter::class, DateFormatter(this)),
//        )

    }
}

private const val TAG = "ShoppingApplication"