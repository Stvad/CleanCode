//package clean.code.chapter14.solution
//
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertTrue
//import org.junit.Rule
//import org.junit.Test
//import org.junit.rules.ExpectedException
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//
//@RunWith(JUnit4::class)
//class ArgsTest {
//
//    @Rule
//    var exception = ExpectedException.none()
//
//    @Test
//    fun testCreateWithNoSchemaOrArguments() {
//        val args = Args("", arrayOfNulls(0))
//        assertEquals(0, args.cardinality().toLong())
//    }
//
//    @Test
//    fun testWithNoSchemaButWithOneArgument() {
//        exception.expect(ArgsException::class.java)
//
//        try {
//            Args("", arrayOf("-x"))
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT,
//                    e.errorCode)
//            assertEquals('x', e.errorArgumentId.toLong())
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testWithSchemaButWithNoArguments() {
//        exception.expect(ArgsException::class.java)
//
//        try {
//            Args("x", arrayOfNulls(0))
//        } catch (e: ArgsException) {
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testWithNoSchemaButWithMultipleArguments() {
//        try {
//            exception.expect(ArgsException::class.java)
//
//            Args("", arrayOf("-x", "-y"))
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT,
//                    e.errorCode)
//            assertEquals('x', e.errorArgumentId.toLong())
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testNonLetterSchema() {
//        exception.expect(ArgsException::class.java)
//        try {
//            Args("*", arrayOf())
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.INVALID_ARGUMENT_NAME,
//                    e.errorCode)
//            assertEquals('*', e.errorArgumentId.toLong())
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testInvalidArgumentFormat() {
//        exception.expect(ArgsException::class.java)
//        try {
//            Args("f~", arrayOf())
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT, e.errorCode)
//            assertEquals('f', e.errorArgumentId.toLong())
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testSimpleBooleanTruePresent() {
//        val args = Args("x", arrayOf("-x", "true"))
//        assertEquals(1, args.cardinality().toLong())
//        assertEquals(true, args.getBoolean('x'))
//    }
//
//    @Test
//    fun testSimpleBooleanFalsePresent() {
//        val args = Args("x", arrayOf("-x", "false"))
//        assertEquals(1, args.cardinality().toLong())
//        assertEquals(false, args.getBoolean('x'))
//    }
//
//    @Test
//    fun testMissingBooleanArgument() {
//        exception.expect(ArgsException::class.java)
//
//        try {
//            Args("x", arrayOf("-x"))
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.MISSING_BOOLEAN, e.errorCode)
//            assertEquals('x', e.errorArgumentId.toLong())
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testInvalidBoolean() {
//        val args = Args("x", arrayOf("-x", "Truthy"))
//        assertEquals(1, args.cardinality().toLong())
//        assertEquals(false, args.getBoolean('x'))
//    }
//
//    @Test
//    fun testSpacesInFormat() {
//        val args = Args("x, y", arrayOf("-xy", "true", "false"))
//        assertEquals(2, args.cardinality().toLong())
//        assertTrue(args.has('x'))
//        assertTrue(args.has('y'))
//        assertEquals(true, args.getBoolean('x'))
//        assertEquals(false, args.getBoolean('y'))
//    }
//
//
//    @Test
//    fun testInvalidArgumentValueFormat() {
//        exception.expect(ArgsException::class.java)
//
//        try {
//            Args("x, y", arrayOf("xy", "true", "false"))
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT, e.errorCode)
//            assertEquals('-', e.errorArgumentId.toLong())
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testSimpleStringPresent() {
//        val args = Args("x*", arrayOf("-x", "param"))
//        assertEquals(1, args.cardinality().toLong())
//        assertTrue(args.has('x'))
//        assertEquals("param", args.getString('x'))
//    }
//
//    @Test
//    fun testMissingStringArgument() {
//        exception.expect(ArgsException::class.java)
//
//        try {
//            Args("x*", arrayOf("-x"))
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.MISSING_STRING, e.errorCode)
//            assertEquals('x', e.errorArgumentId.toLong())
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testSimpleIntPresent() {
//        val args = Args("x#", arrayOf("-x", "42"))
//        assertEquals(1, args.cardinality().toLong())
//        assertTrue(args.has('x'))
//        assertEquals(42, args.getInt('x').toLong())
//    }
//
//    @Test
//    fun testInvalidInteger() {
//        exception.expect(ArgsException::class.java)
//
//        try {
//            Args("x#", arrayOf("-x", "Forty two"))
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.INVALID_INTEGER, e.errorCode)
//            assertEquals('x', e.errorArgumentId.toLong())
//            assertEquals("Forty two", e.errorParameter)
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testMissingInteger() {
//        exception.expect(ArgsException::class.java)
//
//        try {
//            Args("x#", arrayOf("-x"))
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.MISSING_INTEGER, e.errorCode)
//            assertEquals('x', e.errorArgumentId.toLong())
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testSimpleDoublePresent() {
//        val args = Args("x##", arrayOf("-x", "42.3"))
//        assertEquals(1, args.cardinality().toLong())
//        assertTrue(args.has('x'))
//        assertEquals(42.3, args.getDouble('x'), .001)
//    }
//
//    @Test
//    fun testInvalidDouble() {
//        exception.expect(ArgsException::class.java)
//
//        try {
//            Args("x##", arrayOf("-x", "Forty two"))
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.INVALID_DOUBLE, e.errorCode)
//            assertEquals('x', e.errorArgumentId.toLong())
//            assertEquals("Forty two", e.errorParameter)
//            throw e
//        }
//
//    }
//
//    @Test
//    fun testMissingDouble() {
//        exception.expect(ArgsException::class.java)
//
//        try {
//            Args("x##", arrayOf("-x"))
//        } catch (e: ArgsException) {
//            assertEquals(ArgsException.ErrorCode.MISSING_DOUBLE, e.errorCode)
//            assertEquals('x', e.errorArgumentId.toLong())
//            throw e
//        }
//
//    }
//}