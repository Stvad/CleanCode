package clean.code.chapter14.solution

import kotlin.reflect.KClass

interface ArgumentMarshaler {
    fun set(currentArgument: Iterator<String>)
}

interface ArgumentMarshalerCompanion {
    val managedType: KClass<*>
}