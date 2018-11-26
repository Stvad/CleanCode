package clean.code.chapter14.kotlin

import clean.code.chapter14.kotlin.ArgsExceptions.MISSING_BOOLEAN
import java.util.NoSuchElementException

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
