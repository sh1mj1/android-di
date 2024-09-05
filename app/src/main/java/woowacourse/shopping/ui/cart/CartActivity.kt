package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleEventObserver
import woowacourse.di.ActivityScopedObserver
import woowacourse.di.InjectedComponent
import woowacourse.shopping.R
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<CartViewModel> {
        CartViewModel.Factory
    }

    private lateinit var dateFormatter: IDateFormatter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = application as ShoppingApplication
        val injectedContainer = application.container.injectedComponentContainer

//        lifecycle.addObserver(ActivityScopedObserver(injectedContainer, this, CartActivity::class))

        val observer = LifecycleEventObserver { owner, event ->
            when (event) {
                ON_CREATE -> {
                    if (injectedContainer.find(IDateFormatter::class) == null) {
                        injectedContainer.add(
                            InjectedComponent.InjectedActivityComponent(
                                IDateFormatter::class,
                                DateFormatter(this),
                                CartActivity::class
                            )
                        )
                    }
                }

                ON_DESTROY -> {
                    injectedContainer.clearActivityScopedObjects()
                }

                ON_START -> super.onStart()
                ON_RESUME -> super.onResume()
                ON_PAUSE -> super.onPause()
                ON_STOP -> super.onStop()
                ON_ANY -> {}
            }
        }
        lifecycle.addObserver(observer)

        
        dateFormatter = injectedContainer.find(IDateFormatter::class) as IDateFormatter



        setupDateFormatter()
        setupBinding()
        setupToolbar()
        setupView()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupDateFormatter() {


    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
        setContentView(binding.root)
    }

    private fun setupView() {
        setupCartProductData()
        setupCartProductList()
    }

    private fun setupCartProductData() {
        viewModel.getAllCartProducts()
    }

    private fun setupCartProductList() {
        viewModel.cartProducts.observe(this) {
            val adapter =
                CartProductAdapter(
                    items = it,
                    dateFormatter = dateFormatter,
                    onClickDelete = viewModel::deleteCartProduct,
                )
            binding.rvCartProducts.adapter = adapter
        }
        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }
}

private const val TAG = "CartActivity"