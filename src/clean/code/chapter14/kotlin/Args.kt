package clean.code.chapter14.kotlin

import clean.code.chapter14.kotlin.ArgsExceptions.INVALID_ARGUMENT_FORMAT
import clean.code.chapter14.kotlin.ArgsExceptions.INVALID_ARGUMENT_NAME
import clean.code.chapter14.kotlin.ArgsExceptions.UNEXPECTED_ARGUMENT
import java.util.HashMap
import java.util.HashSet
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.primaryConstructor

class Args(schema: String, args: Array<String>) {
    private val marshalers: MutableMap<Char, ArgumentMarshaler<out Any>> = HashMap()
    private val argsFound: MutableSet<Char> = HashSet()
    private lateinit var currentArgument: ListIterator<String>

    init {
        parseSchema(schema)
        parseArgumentStrings(args.toList())
    }

    private fun parseSchema(schema: String) =
            schema.split(',')
                    .filter { it.isNotEmpty() }
                    .map { it.trim() }
                    .forEach(::parseSchemaElement)

    private fun parseSchemaElement(element: String) {
        val elementId = element[0]
        validateSchemaElementId(elementId)

        val elementTail = element.substring(1)
        val marshallerInstantiator = marshalerProviders[elementTail]
                ?: throw INVALID_ARGUMENT_FORMAT(elementId, elementTail)
        marshalers[elementId] = marshallerInstantiator.call()
    }

    private fun validateSchemaElementId(elementId: Char) {
        if (!Character.isLetter(elementId)) throw INVALID_ARGUMENT_NAME(elementId)
    }

    private fun parseArgumentStrings(argsList: List<String>) {
        currentArgument = argsList.listIterator()
        while (currentArgument.hasNext()) {
            val argString = currentArgument.next()
            if (argString.startsWith("-")) {
                argString.substring(1).forEach(::parseArgumentCharacter)
            } else {
                currentArgument.previous()
                break
            }
        }
    }

    private fun parseArgumentCharacter(argChar: Char) {
        val marshaler = marshalers[argChar] ?: throw UNEXPECTED_ARGUMENT(argChar)
        argsFound.add(argChar)
        try {
            marshaler.set(currentArgument)
        } catch (e: ArgsException) {
            e.errorArgumentId = argChar
            throw e
        }
    }

    inline fun <reified T : Any> get(arg: Char) = this.get(arg, T::class)
    fun <T : Any> get(arg: Char, clazz: KClass<T>): T =
            try {
                clazz.cast(marshalers[arg]!!.value)
            } catch (e: TypeCastException) {
                throw ArgsExceptions.ARGUMENT_TYPE_MISMATCH(arg)
            }

    fun has(arg: Char): Boolean = argsFound.contains(arg)
    fun cardinality(): Int = argsFound.size

    companion object {
        private val supportedMarshalers = listOf(IntegerArgumentMarshaler::class,
                BooleanArgumentMarshaler::class,
                StringArgumentMarshaler::class,
                DoubleArgumentMarshaler::class)
        private val marshalerProviders = supportedMarshalers
                .associate { (it.companionObjectInstance as ArgumentMarshalerCompanion).schemaIdentifier to it.primaryConstructor }
    }
}


fun main(args: Array<String>) = try {
    println(args.toList())
    val arg = Args("l,p#,d*", args)
    val logging: Boolean = arg.get('l')
    val port: Int = arg.get('p')
    val directory = arg.get<String>('d')

    println("logging [$logging] port [$port] directory [$directory]")
} catch (e: ArgsException) {
    println("Argument error: ${e.message}")
}