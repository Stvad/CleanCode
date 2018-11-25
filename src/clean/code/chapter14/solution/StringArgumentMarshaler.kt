package clean.code.chapter14.solution

import clean.code.chapter14.solution.ArgsExceptions.MISSING_STRING
import java.util.NoSuchElementException

class StringArgumentMarshaler : ArgumentMarshaler<String> {
    override val value: String
        get() = stringValue

    private var stringValue = ""

    override fun set(currentArgument: Iterator<String>) {
        try {
            stringValue = currentArgument.next()
        } catch (e: NoSuchElementException) {
            throw MISSING_STRING()
        }
    }

    companion object {

        fun getValue(am: ArgumentMarshaler<*>?): String {
            return if (am != null && am is StringArgumentMarshaler)
                am.stringValue
            else
                ""
        }
    }
}