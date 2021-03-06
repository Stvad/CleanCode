package clean.code.chapter14.kotlin

import clean.code.chapter14.kotlin.ArgsExceptions.MISSING_STRING
import java.util.NoSuchElementException

class StringArgumentMarshaler(override val id: Char) : ArgumentMarshaler<String> {
    override val value: String
        get() = stringValue

    private var stringValue = ""

    override fun set(currentArgument: Iterator<String>) = try {
        stringValue = currentArgument.next()
    } catch (e: NoSuchElementException) {
        throw MISSING_STRING(id)
    }

    companion object : ArgumentMarshalerCompanion {
        override val schemaIdentifier = "*"
    }
}