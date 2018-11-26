package clean.code.chapter14.solution

import clean.code.chapter14.solution.ArgsExceptions.INVALID_ARGUMENT_FORMAT
import clean.code.chapter14.solution.ArgsExceptions.INVALID_ARGUMENT_NAME
import clean.code.chapter14.solution.ArgsExceptions.UNEXPECTED_ARGUMENT
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
                    .forEach { parseSchemaElement(it.trim()) }

    private fun parseSchemaElement(element: String) {
        val elementId = element[0]
        validateSchemaElementId(elementId)

        val elementTail = element.substring(1)
        val marshallerInstantiator = Companion.schemaInstantiators[elementTail]
                ?: throw INVALID_ARGUMENT_FORMAT(elementId, elementTail)
        marshalers[elementId] = marshallerInstantiator.call()
    }

    private fun validateSchemaElementId(elementId: Char) {
        if (!Character.isLetter(elementId)) throw INVALID_ARGUMENT_NAME(elementId)
    }

    private fun parseArgumentStrings(argsList: List<String>) {
        if (argsList.isEmpty()) return

        currentArgument = argsList.listIterator()
        val argString = currentArgument.next()

        argString.takeIf { it.startsWith("-") }
                ?.let { parseArgumentCharacters(it.substring(1)) }
                ?: throw INVALID_ARGUMENT_FORMAT('-')
    }

    private fun parseArgumentCharacters(argChars: String) = argChars.forEach { parseArgumentCharacter(it) }

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

    fun has(arg: Char): Boolean = argsFound.contains(arg)
    fun cardinality(): Int = argsFound.size
    fun nextArgument(): Int = currentArgument.nextIndex()


    inline fun <reified T : Any> get(arg: Char) = this.get(arg, T::class)

    fun <T : Any> get(arg: Char, clazz: KClass<T>): T =
            try {
                clazz.cast(marshalers[arg]!!.value)
            } catch (e: TypeCastException) {
                throw ArgsExceptions.ARGUMENT_TYPE_MISMATCH(arg)
            }


    companion object {
        private val supportedMarshalers = listOf(IntegerArgumentMarshaler::class, BooleanArgumentMarshaler::class, StringArgumentMarshaler::class)
        private val schemaInstantiators = supportedMarshalers
                .associate { (it.companionObjectInstance as ArgumentMarshalerCompanion).schemaIdentifier to it.primaryConstructor }
    }
}


fun main(args: Array<String>) = try {
    val arg = Args("l,p#,d*", args)
    val logging: Boolean = arg.get('l')
    val port: Int = arg.get('p')
    val directory = arg.get<String>('d')

    println("logging [$logging] port [$port] directory [$directory]")
} catch (e: ArgsException) {
    println("Argument error: ${e.message}")
}