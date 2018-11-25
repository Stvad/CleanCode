package clean.code.chapter14.solution

import java.util.NoSuchElementException

import clean.code.chapter14.solution.ArgsException.ErrorCode.INVALID_INTEGER
import clean.code.chapter14.solution.ArgsException.ErrorCode.MISSING_INTEGER
import kotlin.reflect.KClass

class IntegerArgumentMarshaler : ArgumentMarshaler {
    private var intValue = 0

    @Throws(ArgsException::class)
    override fun set(currentArgument: Iterator<String>) {
        var parameter: String? = null
        try {
            parameter = currentArgument.next()
            intValue = Integer.parseInt(parameter)
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_INTEGER)
        } catch (e: NumberFormatException) {
            throw ArgsException(INVALID_INTEGER, parameter)
        }

    }

    companion object : ArgumentMarshalerCompanion {
        override val managedType: KClass<*> = Int::class


        fun getValue(am: ArgumentMarshaler?): Int {
            return if (am != null && am is IntegerArgumentMarshaler)
                am.intValue
            else
                0
        }
    }
}