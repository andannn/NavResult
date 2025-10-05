package me.andannn.navresult

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.currentCompositeKeyHashCode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.savedstate.SavedState
import androidx.savedstate.serialization.decodeFromSavedState
import kotlinx.serialization.KSerializer

/**
 * Add effect for handle nav result.
 *
 * @param requestKey request key send from destination by [NavResultOwner.setNavResult].
 * @param onResult callback for handle nav result.
 */
@Composable
fun LaunchNavResultHandler(
    requestKey: String,
    onResult: (SavedState) -> Unit,
) {
    val navResultOwner = LocalNavResultOwner.current as NavResultOwnerImpl

    val currentOnResult by rememberUpdatedState(onResult)
    val composeHashCode = currentCompositeKeyHashCode
    DisposableEffect(requestKey, onResult, currentOnResult) {
        if (navResultOwner.navResultStore.containsKey(requestKey)) {
            navResultOwner.navResultStore.remove(requestKey)?.let { result ->
                currentOnResult.invoke(result)
            }
        } else {
            navResultOwner.setNavResultListener(
                requestKey = requestKey,
                composeHashCode = composeHashCode,
                listener = {
                    currentOnResult.invoke(it)
                },
            )
        }

        onDispose {
            navResultOwner.clearNavResultListener(
                requestKey = requestKey,
                composeHashCode = composeHashCode,
            )
        }
    }
}


/**
 * Add effect for handle nav result.
 *
 * @param requestKey request key send from destination by [NavResultOwner.setNavResult].
 * @param resultSerializer serializer for [T].
 * @param onResult callback for handle nav result.
 */
@Composable
fun <T> LaunchNavResultHandler(
    requestKey: String,
    resultSerializer: KSerializer<T>,
    onResult: (T) -> Unit,
) {
    LaunchNavResultHandler(
        requestKey = requestKey,
        onResult = { savedState ->
            onResult(decodeFromSavedState(resultSerializer, savedState))
        },
    )
}
