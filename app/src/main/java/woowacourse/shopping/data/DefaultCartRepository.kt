package woowacourse.shopping.data

import woowacourse.shopping.model.Product

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(CartProductEntity(product.name, product.price, product.imageUrl))
    }

    override suspend fun allCartProducts(): List<Product> = dao.getAll().toData()

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}

fun List<CartProductEntity>.toData(): List<Product> =
    map {
        Product(it.id, it.name, it.price, it.imageUrl, it.createdAt)
    }
