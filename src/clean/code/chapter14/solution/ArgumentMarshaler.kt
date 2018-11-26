package clean.code.chapter14.solution

import kotlin.reflect.KClass

interface ArgumentMarshaler<T : Any> {
    fun set(currentArgument: Iterator<String>)
    val value: T
//    val defaultValue: T

}

//inline fun<reified R> ArgumentMarshaler<Any>.get(): R? = if (R::class == this.value::class) value as R else null
inline fun <reified R> ArgumentMarshaler<out Any>.get(): R? = this.value as? R
//if the requested type does not match type of parameter original implementation would return the default for requested type. gonna try to emulate it here

interface ArgumentMarshalerCompanion {
    val managedType: KClass<*>
    val schemaIdentifier: String
}