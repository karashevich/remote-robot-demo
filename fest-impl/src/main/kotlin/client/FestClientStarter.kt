package client

import java.net.URLClassLoader

object FestClientStarter {

    @JvmStatic
    fun main(args: Array<String>) {
        URLClassLoader.getSystemClassLoader()
        println("---FestClient---")
        print("Please enter server's port: ")
        val port = readLine()?.toInt() ?: throw Exception("Unable to get server's port from console")
        FestClient(serverPort = port, receiveObjectClassloader = QueryClassLoader.classLoader).startActivity()
        while (true) {
            print("Write 'q' to stop client: ")
            if (readLine()?.toCharArray()?.first() == 'q') break
        }
    }
}