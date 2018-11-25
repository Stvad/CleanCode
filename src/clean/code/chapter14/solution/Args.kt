package clean.code.chapter14.solution

import clean.code.chapter14.solution.ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT
import clean.code.chapter14.solution.ArgsException.ErrorCode.INVALID_ARGUMENT_NAME
import clean.code.chapter14.solution.ArgsException.ErrorCode.UNEXPECTED_ARGUMENT
import java.util.HashMap
import java.util.HashSet
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.companionObjectInstance

class Args(schema: String, args: Array<String>) {
    private val marshalers: MutableMap<Char, ArgumentMarshaler> = HashMap()
    private val argsFound: MutableSet<Char> = HashSet()
    private var currentArgument: ListIterator<String>? = null

    init {
        parseSchema(schema)
        parseArgumentStrings(args.toList())
    }

    private fun parseSchema(schema: String) {
        for (element in schema.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            if (element.isNotEmpty()) parseSchemaElement(element.trim { it <= ' ' })
    }

    private fun parseSchemaElement(element: String) {
        val elementId = element[0]
        val elementTail = element.substring(1)
        validateSchemaElementId(elementId)
        when {
            elementTail.isEmpty() -> marshalers[elementId] = BooleanArgumentMarshaler()
            elementTail == "*" -> marshalers[elementId] = StringArgumentMarshaler()
            elementTail == "#" -> marshalers[elementId] = IntegerArgumentMarshaler()
            elementTail == "##" -> marshalers[elementId] = DoubleArgumentMarshaler()
            elementTail == "[*]" -> marshalers[elementId] = StringArrayArgumentMarshaler()
            else -> throw ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail)
        }
    }

    private fun validateSchemaElementId(elementId: Char) {
        if (!Character.isLetter(elementId))
            throw ArgsException(INVALID_ARGUMENT_NAME, elementId, null)
    }

    private fun parseArgumentStrings(argsList: List<String>) {
        if (argsList.isNotEmpty()) {
            currentArgument = argsList.listIterator()
            val argString = currentArgument!!.next()

            argString.takeIf { it.startsWith("-") }
                    ?.let { parseArgumentCharacters(it.substring(1)) }
                    ?: throw ArgsException(INVALID_ARGUMENT_FORMAT, '-', "")
        }
    }

    private fun parseArgumentCharacters(argChars: String) = argChars.forEach { parseArgumentCharacter(it) }

    private fun parseArgumentCharacter(argChar: Char) {
        val m = marshalers[argChar] ?: throw ArgsException(UNEXPECTED_ARGUMENT, argChar, null)

        argsFound.add(argChar)
        try {
            m.set(currentArgument)
        } catch (e: ArgsException) {
            e.errorArgumentId = argChar
            throw e
        }

    }

    fun has(arg: Char): Boolean = argsFound.contains(arg)
    fun cardinality(): Int = argsFound.size
    fun nextArgument(): Int = currentArgument!!.nextIndex()

    fun getBoolean(arg: Char): Boolean {
        return BooleanArgumentMarshaler.getValue(marshalers[arg])
    }

    fun getString(arg: Char): String {
        return StringArgumentMarshaler.getValue(marshalers[arg])
    }

    fun getInt(arg: Char): Int {
        return IntegerArgumentMarshaler.getValue(marshalers[arg])
    }

    fun getDouble(arg: Char): Double {
        return DoubleArgumentMarshaler.getValue(marshalers[arg])
    }

    fun getStringArray(arg: Char): Array<String> {
        return StringArrayArgumentMarshaler.getValue(marshalers[arg])
    }

    private val supportedMarshalers = listOf(IntegerArgumentMarshaler::class, BooleanArgumentMarshaler::class)
    private val supportedTypes = supportedMarshalers.associateBy { (it.companionObjectInstance as ArgumentMarshalerCompanion).managedType }

    fun <T : Any> get(arg: Char, clazz: KClass<T>) {
        val marshallerClass = supportedTypes[clazz] ?: throw ArgsException()
        marshallerClass.cast(marshalers[arg] ?: throw ArgsException())
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) = try {
            val arg = Args("l,p#,d*", args)
            val logging = arg.getBoolean('l')
            val port = arg.getInt('p')
            val directory = arg.getString('d')

            println("logging [$logging] port [$port] directory [$directory]")
        } catch (e: ArgsException) {
            println("Argument error: ${e.errorMessage()}")
        }
    }
}

inline fun <reified T : Any> Args.get(arg: Char) = this.get(arg, T::class)
