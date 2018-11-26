package clean.code.chapter14.solution

class ArgsException(val messageSupplier: (errorArgumentId: Char, errorParameter: String?) -> String,
                    var errorArgumentId: Char = '\u0000',
                    var errorParameter: String? = null) : RuntimeException() {

    override val message: String
        get() = messageSupplier(errorArgumentId, errorParameter)
}

enum class ArgsExceptions(val messageSupplier: (errorArgumentId: Char, errorParameter: String?) -> String) {
    UNEXPECTED_ARGUMENT({ errorArgumentId, _ -> "Argument -$errorArgumentId unexpected." }),
    ARGUMENT_TYPE_MISMATCH({ errorArgumentId, _ -> "The requested argument -$errorArgumentId has a different type." }),
    MISSING_STRING({ errorArgumentId, _ -> "Could not find string parameter for -$errorArgumentId." }),
    INVALID_INTEGER({ errorArgumentId, errorParameter -> "Argument -$errorArgumentId expects an integer but was '$errorParameter'." }),
    MISSING_INTEGER({ errorArgumentId, _ -> "Could not find integer parameter for -$errorArgumentId." }),
    INVALID_DOUBLE({ errorArgumentId, errorParameter -> "Argument -$errorArgumentId expects a double but was '$errorParameter'." }),
    MISSING_DOUBLE({ errorArgumentId, _ -> "Could not find double parameter for -$errorArgumentId." }),
    MISSING_BOOLEAN({ errorArgumentId, _ -> "Could not find boolean parameter for -$errorArgumentId." }),
    INVALID_ARGUMENT_NAME({ errorArgumentId, _ -> "'$errorArgumentId' is not a valid argument name." }),
    INVALID_ARGUMENT_FORMAT({ _, errorParameter -> "'$errorParameter' is not a valid argument format." });

    operator fun invoke(errorArgumentId: Char = '\u0000', errorParameter: String? = null) =
            ArgsException(messageSupplier, errorArgumentId, errorParameter)

}