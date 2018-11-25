package clean.code.chapter14.solution

import junit.framework.TestCase

class ArgsExceptionTest : TestCase() {
    @Throws(Exception::class)
    fun testUnexpectedMessage() {
        val e = ArgsException(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT,
                'x', null)
        TestCase.assertEquals("Argument -x unexpected.", e.errorMessage())
    }

    @Throws(Exception::class)
    fun testMissingStringMessage() {
        val e = ArgsException(ArgsException.ErrorCode.MISSING_STRING,
                'x', null)
        TestCase.assertEquals("Could not find string parameter for -x.", e.errorMessage())
    }

    @Throws(Exception::class)
    fun testInvalidIntegerMessage() {
        val e = ArgsException(ArgsException.ErrorCode.INVALID_INTEGER,
                'x', "Forty two")
        TestCase.assertEquals("Argument -x expects an integer but was 'Forty two'.",
                e.errorMessage())
    }

    @Throws(Exception::class)
    fun testMissingIntegerMessage() {
        val e = ArgsException(ArgsException.ErrorCode.MISSING_INTEGER, 'x', null)
        TestCase.assertEquals("Could not find integer parameter for -x.", e.errorMessage())
    }

    @Throws(Exception::class)
    fun testInvalidDoubleMessage() {
        val e = ArgsException(ArgsException.ErrorCode.INVALID_DOUBLE,
                'x', "Forty two")
        TestCase.assertEquals("Argument -x expects a double but was 'Forty two'.",
                e.errorMessage())
    }

    @Throws(Exception::class)
    fun testMissingDoubleMessage() {
        val e = ArgsException(ArgsException.ErrorCode.MISSING_DOUBLE,
                'x', null)
        TestCase.assertEquals("Could not find double parameter for -x.", e.errorMessage())
    }
}