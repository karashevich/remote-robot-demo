package client

/**
 * This classloader stand for resolving lambda receiving from server
 */
class MyQueryClassLoader(parent: ClassLoader): ClassLoader(parent) {

    val name2ByteArrayMap = HashMap<String, ByteArray>()

    override fun findClass(name: String?): Class<*> {
        return if (name2ByteArrayMap.contains(name))
            defineClass(name, name2ByteArrayMap[name], 0, name2ByteArrayMap[name]!!.size)
        else
            super.findClass(name)
    }
}

object QueryClassLoader {
    private val myClassLoader: MyQueryClassLoader by lazy {
        val pc = Thread.currentThread().contextClassLoader
        MyQueryClassLoader(pc)
    }

    val classLoader: ClassLoader
        get() = myClassLoader

    fun addClass(className: String, classBytes: ByteArray) { myClassLoader.name2ByteArrayMap[className] = classBytes }
}