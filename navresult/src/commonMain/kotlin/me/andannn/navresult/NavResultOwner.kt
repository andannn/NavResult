package me.andannn.navresult

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositeKeyHashCode
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.savedstate.SavedState
import androidx.savedstate.serialization.encodeToSavedState
import kotlinx.serialization.KSerializer

/**
 *
 */
val LocalNavResultOwner =
    staticCompositionLocalOf<NavResultOwner> {
        error("No RootNavigator provided")
    }

/**
 *
 */
interface NavResultOwner {
    fun setNavResult(
        requestKey: String,
        result: SavedState,
    )
}

/**
 *
 */
fun <T> NavResultOwner.setNavResult(
    requestKey: String,
    result: T,
    serializer: KSerializer<T>,
) {
    setNavResult(
        requestKey = requestKey,
        result = encodeToSavedState(serializer, result),
    )
}

/**
 *
 */
@Composable
fun rememberNavResultOwner(): NavResultOwner {
    val navResultStore: NavResultStore = rememberNavResultStore()

    return remember(navResultStore) {
        NavResultOwnerImpl(navResultStore)
    }
}

@Composable
internal fun rememberNavResultStore(): NavResultStore =
    rememberSaveable(
        saver =
            Saver(
                save = {
                    it.results
                },
                restore = {
                    NavResultStore(it)
                },
            ),
    ) {
        NavResultStore()
    }

internal class NavResultStore(
    internal val results: MutableMap<String, Bundle> = mutableMapOf(),
) : MutableMap<String, Bundle> by results

internal class NavResultOwnerImpl(
    val navResultStore: NavResultStore,
) : NavResultOwner {
    private val resultListeners = mutableMapOf<RequestListenerKey, (Bundle) -> Unit>()

    override fun setNavResult(
        requestKey: String,
        result: Bundle,
    ) {
        val listeners =
            resultListeners
                .filter {
                    it.key.requestKey == requestKey
                }.values

        if (listeners.isEmpty()) {
            navResultStore.results.put(requestKey, result)
            return
        }

        listeners.forEach {
            it.invoke(result)
        }
    }

    fun setNavResultListener(
        requestKey: String,
        composeHashCode: CompositeKeyHashCode,
        listener: (Bundle) -> Unit,
    ) {
        resultListeners.put(RequestListenerKey(requestKey, composeHashCode), listener)
    }

    fun clearNavResultListener(
        requestKey: String,
        composeHashCode: CompositeKeyHashCode,
    ) {
        resultListeners.remove(RequestListenerKey(requestKey, composeHashCode))
    }

    private data class RequestListenerKey(
        val requestKey: String,
        val composeHashCode: CompositeKeyHashCode,
    )
}
