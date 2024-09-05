package woowacourse.shopping

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class CommonViewModelFactory(
    val application: ShoppingApplication,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")

        val args =
            constructor.parameters.map { parameter ->
                val containers = application.container.injectedComponentContainer
                val find = containers.find(parameter.type.classifier as KClass<*>)
                    ?: throw java.lang.IllegalArgumentException("Unresolved dependency for ${parameter.type}")
                Log.d(TAG, "find: $find")
                find
            }.toTypedArray()

        return constructor.call(*args)
    }
}

private const val TAG = "CommonViewModelFactory"

//class CommonActivityFactory {
//    fun <T: Activity> create(modelClass: Class<T>) : T {
//        val kClass = modelClass.kotlin
//        val constructor = kClass.primaryConstructor
//    }
//}