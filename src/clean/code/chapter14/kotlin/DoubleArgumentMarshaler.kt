package clean.code.chapter14.kotlin

import clean.code.chapter14.kotlin.ArgsExceptions.INVALID_DOUBLE
import clean.code.chapter14.kotlin.ArgsExceptions.MISSING_DOUBLE
import java.util.NoSuchElementException

class DoubleArgumentMarshaler : ArgumentMarshaler<Double> {
    override val value: Double
        get() = doubleValue

    private var doubleValue = 0.0

    override fun set(currentArgument: Iterator<String>) {
        var parameter: String? = null
        try {
            parameter = currentArgument.next()
            doubleValue = parameter.toDouble()
        } catch (e: NoSuchElementException) {
            throw MISSING_DOUBLE()
        } catch (e: NumberFormatException) {
            throw INVALID_DOUBLE(errorParameter = parameter)
        }
    }

    companion object : ArgumentMarshalerCompanion {
        override val schemaIdentifier = "##"
        override val managedType = Double::class
    }
}