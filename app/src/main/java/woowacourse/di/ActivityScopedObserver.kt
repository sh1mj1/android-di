package woowacourse.di

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.IDateFormatter
import kotlin.reflect.KClass

class ActivityScopedObserver(
    private val container: InjectedContainer,
    private val activityContext: Context,
    private val activityClass: KClass<*>,
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onActivityCreate() {
        Log.d(TAG, "onActivityCreate: called")
        if (container.find(IDateFormatter::class) == null) {
            container.add(InjectedComponent.InjectedActivityComponent(IDateFormatter::class, DateFormatter(activityContext), activityClass))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActivityDestroy() {
        Log.d(TAG, "onActivityDestroy: called")
        container.clearActivityScopedObjects()
    }
}

private const val TAG = "ActivityScopedObserver"
