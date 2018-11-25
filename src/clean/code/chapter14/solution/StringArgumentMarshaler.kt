package clean.code.chapter14.solution

import java.util.NoSuchElementException

import clean.code.chapter14.solution.ArgsException.ErrorCode.MISSING_STRING

class StringArgumentMarshaler : ArgumentMarshaler<*> {
    private var stringValue = ""

    @Throws(ArgsException::class)
    override fun set(currentArgument: Iterator<String>) {
        try {
            stringValue = currentArgument.next()
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_STRING)
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