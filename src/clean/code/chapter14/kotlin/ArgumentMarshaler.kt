package clean.code.chapter14.kotlin

interface ArgumentMarshaler<T : Any> {
    fun set(currentArgument: Iterator<String>)
    val value: T
    val id: Char
}

interface ArgumentMarshalerCompanion {
    val schemaIdentifier: String
}