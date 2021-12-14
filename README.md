## Problems while importing

* Inputs are not the same as in JVM - requires removing blank chars (which are not even present there)
* There is no working debugger in any IDE (even in IntelliJ IDEA Ultimate)
* Reading files is not so trivial as in Java
* Some functions are not working (!!!). For example function below is not working, but was fine in Kotlin-JVM
* The IDE is really slow (comparing to "standard" IntelliJ)
* YOU REALLY NEED TO TRIM ALL STRINGS BEFORE THE USAGE

```kotlin
/**
 * Transposes matrix
 */
@Deprecated("WTF: not working in kotlin native", replaceWith = ReplaceWith("transpose()"))
fun <T> List<List<T>>.trans(): List<List<T>> {
    // don't know why, but not working in kotlin native :(
    return (this[0].indices).map { i ->
        this.map { it[i] }
    }
}
```