package clean.code.chapter14.solution

import java.util.NoSuchElementException

import clean.code.chapter14.solution.ArgsException.ErrorCode.MISSING_BOOLEAN
import kotlin.reflect.KClass

class BooleanArgumentMarshaler : ArgumentMarshaler {
    private var booleanValue = false

    override fun set(currentArgument: Iterator<String>) = try {
        booleanValue = currentArgument.next().toBoolean()
    } catch (e: NoSuchElementException) {
        throw ArgsException(MISSING_BOOLEAN)
    }

    val value: Boolean
        get() = booleanValue

    companion object : ArgumentMarshalerCompanion {
        override val managedType: KClass<*> = Boolean::class
    }
}
