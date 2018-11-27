package clean.code.chapter14.kotlin

import kotlin.reflect.KClass

interface ArgumentMarshaler<T : Any> {
    fun set(currentArgument: Iterator<String>)
    val value: T
}

interface ArgumentMarshalerCompanion {
    val schemaIdentifier: String
}