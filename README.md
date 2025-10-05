A small helper for Jetpack Compose that simplifies sending results between composables.

### Quick Start

1. Define a request key.
```kotlin
const val ScreenABackResult = "screen_a_back_result"
```

2. Register a handler with the request key.
```kotlin
LaunchNavResultHandler(
    requestKey = ScreenABackResult,
    resultSerializer = ScreenAResult.serializer(),
) { result ->
    screenANavResult = result.toString()
}
```

3. Define a result type which can be marked as `@Serializable`.
```kotlin
@Serializable
data class ScreenAResult(val id: Int, val name: String)
```

4. Send result.
```kotlin
resultOwner.setNavResult(
    requestKey = ScreenABackResult,
    result = ScreenAResult(1, "foo"),
    serializer = ScreenAResult.serializer()
)
```