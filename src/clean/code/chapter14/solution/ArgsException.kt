package clean.code.chapter14.solution

//import clean.code.chapter14.solution.ArgsException.ErrorCode.*
//
//class ArgsExceptionJb(var errorCode: ErrorCode = OK, var errorArgumentId: Char = '\u0000', var errorParameter: String? = null) : Exception() {
//
//    fun errorMessage(): String =
//            when (errorCode) {
//                OK -> "TILT: Should not get here."
//                UNEXPECTED_ARGUMENT -> "Argument -$errorArgumentId unexpected."
//                MISSING_STRING -> "Could not find string parameter for -$errorArgumentId."
//                INVALID_INTEGER -> "Argument -$errorArgumentId expects an integer but was '$errorParameter'."
//                MISSING_INTEGER -> "Could not find integer parameter for -$errorArgumentId."
//                INVALID_DOUBLE -> "Argument -$errorArgumentId expects a double but was '$errorParameter'."
//                MISSING_DOUBLE -> "Could not find double parameter for -$errorArgumentId."
//                MISSING_BOOLEAN -> "Could not find boolean parameter for -$errorArgumentId."
//                INVALID_ARGUMENT_NAME -> "'$errorArgumentId' is not a valid argument name."
//                INVALID_ARGUMENT_FORMAT -> "'$errorParameter' is not a valid argument format."
//            }
//
//    override val message: String
//        get() = errorMessage()
//
//    enum class ErrorCode {
//        OK,
//        INVALID_ARGUMENT_FORMAT,
//        UNEXPECTED_ARGUMENT,
//        INVALID_ARGUMENT_NAME,
//        MISSING_STRING,
//        MISSING_INTEGER,
//        INVALID_INTEGER,
//        MISSING_DOUBLE,
//        INVALID_DOUBLE,
//        MISSING_BOOLEAN
//    }
//}

//data class ArgsExceptionDetails(var errorArgumentId: Char = '\u0000', var errorParameter: String? = null)
//
//sealed class ArgsExceptions(val messageSupplier: () -> String, open var errorArgumentId: Char = '\u0000', open var errorParameter: String? = null) {
//    class UNEXPECTED_ARGUMENT(override var errorArgumentId: Char = '\u0000', override var errorParameter: String? = null) : ArgsExceptions({ "Argument -$errorArgumentId unexpected." })
//    class MISSING_STRING() : ArgsExceptions("Could not find string parameter for -$errorArgumentId.")
//    class INVALID_INTEGER() : ArgsExceptions("Argument -$errorArgumentId expects an integer but was '$errorParameter'.")
//    class MISSING_INTEGER() : ArgsExceptions("Could not find integer parameter for -$errorArgumentId.")
//    class INVALID_DOUBLE() : ArgsExceptions("Argument -$errorArgumentId expects a double but was '$errorParameter'.")
//    class MISSING_DOUBLE() : ArgsExceptions("Could not find double parameter for -$errorArgumentId.")
//    class MISSING_BOOLEAN() : ArgsExceptions("Could not find boolean parameter for -$errorArgumentId.")
//    class INVALID_ARGUMENT_NAME() : ArgsExceptions("'$errorArgumentId' is not a valid argument name.")
//    class INVALID_ARGUMENT_FORMAT() : ArgsExceptions("'$errorParameter' is not a valid argument format.")
//
//    open fun invoke(errorArgumentId: Char = '\u0000', errorParameter: String? = null) {
//        return ArgsException()
//    }
//}

//fun suppCreator(message: String) = { errorArgumentId: Char, errorParameter: String? -> "" }

class ArgsException(val messageSupplier: (errorArgumentId: Char, errorParameter: String?) -> String,
                    var errorArgumentId: Char = '\u0000',
                    var errorParameter: String? = null) : RuntimeException() {
    override val message: String
        get() = messageSupplier(errorArgumentId, errorParameter)
}


enum class ArgsExceptions(val messageSupplier: (errorArgumentId: Char, errorParameter: String?) -> String) {
    UNEXPECTED_ARGUMENT({ errorArgumentId, _ -> "Argument -$errorArgumentId unexpected." }),
    MISSING_STRING({ errorArgumentId, _ -> "Could not find string parameter for -$errorArgumentId." }),
    INVALID_INTEGER({ errorArgumentId, errorParameter -> "Argument -$errorArgumentId expects an integer but was '$errorParameter'." }),
    MISSING_INTEGER({ errorArgumentId, _ -> "Could not find integer parameter for -$errorArgumentId." }),
    INVALID_DOUBLE({ errorArgumentId, errorParameter -> "Argument -$errorArgumentId expects a double but was '$errorParameter'." }),
    MISSING_DOUBLE({ errorArgumentId, _ -> "Could not find double parameter for -$errorArgumentId." }),
    MISSING_BOOLEAN({ errorArgumentId, _ -> "Could not find boolean parameter for -$errorArgumentId." }),
    INVALID_ARGUMENT_NAME({ errorArgumentId, _ -> "'$errorArgumentId' is not a valid argument name." }),
    INVALID_ARGUMENT_FORMAT({ _, errorParameter -> "'$errorParameter' is not a valid argument format." });

    open fun invoke(errorArgumentId: Char = '\u0000', errorParameter: String? = null) =
            ArgsException(messageSupplier, errorArgumentId, errorParameter)

}