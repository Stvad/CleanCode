package clean.code.chapter14.kotlin

import java.util.NoSuchElementException
import kotlin.reflect.KClass

class IntegerArgumentMarshaler : ArgumentMarshaler<Int> {
    override val value: Int
        get() = intValue

    private var intValue = 0

    override fun set(currentArgument: Iterator<String>) {
        var parameter: String? = null
        try {
            parameter = currentArgument.next()
            intValue = parameter.toInt()
        } catch (e: NoSuchElementException) {
            throw ArgsExceptions.MISSING_INTEGER()
        } catch (e: NumberFormatException) {
            throw ArgsExceptions.INVALID_INTEGER(errorParameter = parameter)
        }

    }

    companion object : ArgumentMarshalerCompanion {
        override val schemaIdentifier = "#"
    }
}