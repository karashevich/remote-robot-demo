package app

import org.fest.reflect.type.Type
import org.fest.swing.launcher.ApplicationLauncher
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.net.URLClassLoader
import java.nio.file.Paths
import javax.swing.JButton

class App (pathToJar: String, mainClassName: String) {

    val appLauncher: ApplicationLauncher

    init {
        val appJarUrl = Paths.get(pathToJar).toUri().toURL()
        val classLoader = URLClassLoader(arrayOf(appJarUrl))
        val mainClass = Type.newType(mainClassName).withClassLoader(classLoader).load()
        appLauncher = ApplicationLauncher.application(mainClass)
    }

    companion object {

        /**
         * To start Application pass to program arguments path to the application's jar and name of the main class
         */
        @JvmStatic
        fun main(args: Array<String>) {
            App(args[0], args[1]).appLauncher.start()
            val button = robot.RobotHolder.getRobot().finder().findAll { it is JButton }.first()
            val classLoader = Thread.currentThread().contextClassLoader
        }



        class JButtonInvocationHandler : InvocationHandler {

            override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
                println("Proxy ${proxy?.javaClass?.name}, method: ${method?.name}, args: $args")
                return Any()
            }
        }
    }

}