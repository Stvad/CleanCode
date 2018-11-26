package clean.code.chapter14.solution

import kotlin.reflect.KClass

interface ArgumentMarshaler<T : Any> {
    fun set(currentArgument: Iterator<String>)
    val value: T
}

interface ArgumentMarshalerCompanion {
    val managedType: KClass<*>
    val schemaIdentifier: String
}