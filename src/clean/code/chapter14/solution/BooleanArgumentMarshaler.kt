package clean.code.chapter14.solution

import java.util.NoSuchElementException

import clean.code.chapter14.solution.ArgsExceptions.MISSING_BOOLEAN
import kotlin.reflect.KClass

class BooleanArgumentMarshaler : ArgumentMarshaler<Boolean> {
    private var booleanValue = false

    override fun set(currentArgument: Iterator<String>) = try {
        booleanValue = currentArgument.next().toBoolean()
    } catch (e: NoSuchElementException) {
        throw MISSING_BOOLEAN()
    }

    override val value: Boolean
        get() = booleanValue

    companion object : ArgumentMarshalerCompanion {
        override val schemaIdentifier = ""
        override val managedType = Boolean::class
    }
}
