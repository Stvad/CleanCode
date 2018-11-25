package clean.code.chapter14.solution

import java.util.NoSuchElementException

import clean.code.chapter14.solution.ArgsException.ErrorCode.*

class DoubleArgumentMarshaler : ArgumentMarshaler<*> {
    private var doubleValue = 0.0

    @Throws(ArgsException::class)
    override fun set(currentArgument: Iterator<String>) {
        var parameter: String? = null
        try {
            parameter = currentArgument.next()
            doubleValue = java.lang.Double.parseDouble(parameter)
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_DOUBLE)
        } catch (e: NumberFormatException) {
            throw ArgsException(INVALID_DOUBLE, parameter)
        }

    }

    companion object {

        fun getValue(am: ArgumentMarshaler<*>?): Double {
            return if (am != null && am is DoubleArgumentMarshaler)
                am.doubleValue
            else
                0.0
        }
    }
}