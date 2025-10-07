A small helper for Jetpack Compose that simplifies sending results between composables.

### Install

libs.versions.toml

```toml
[versions]
navresult = "1.0.0"

[dependencies]
navresult = { group = "io.github.andannn", name = "navresult", version.ref = "navresult" }
```

then

```
dependencies {
    implementation(libs.navresult)
}
```

### Quick Start

1. Inject `NavResultOwner` into your composable.

```kotlin
val navResultOwner = rememberNavResultOwner()
CompositionLocalProvider(
    LocalNavResultOwner provides navResultOwner,
) {
    App(navigator)
}
```

2. Define a request key.

```kotlin
const val ScreenABackResult = "screen_a_back_result"
```

3. Register a handler with the request key.
```kotlin
LaunchNavResultHandler(
    requestKey = ScreenABackResult,
    resultSerializer = ScreenAResult.serializer(),
) { result ->
    // handle result
}
```

4. Define a result type which can be marked as `@Serializable`.
```kotlin
@Serializable
data class ScreenAResult(val id: Int, val name: String)
```

5. Send result.
```kotlin
resultOwner.setNavResult(
    requestKey = ScreenABackResult,
    result = ScreenAResult(1, "foo"),
    serializer = ScreenAResult.serializer()
)
```