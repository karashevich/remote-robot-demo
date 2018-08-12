package client

import org.apache.commons.io.input.ClassLoaderObjectInputStream
import transport.Query
import transport.Response
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.concurrent.BlockingQueue
import kotlin.concurrent.thread

object ClientThreads {

    fun runClientReceiveThread(connection: Socket, receivingQueries: BlockingQueue<Query>, receiveClassLoader: ClassLoader) =
            thread(name = "Client Receive Thread", start = true) {
                val objectInputStream = ClassLoaderObjectInputStream(receiveClassLoader, connection.getInputStream())
                try {
                    while (connection.isConnected)
                        receivingQueries.put(
                                objectInputStream.readObject() as Query)
                } catch (e: Exception) {
                    throw e
                }
            }

    fun runClientSendThread(connection: Socket, postingResponses: BlockingQueue<Response>) = thread(name = "Client Send Thread", start = true) {
        val objectOutputStream = ObjectOutputStream(connection.getOutputStream())
        try {
            while (connection.isConnected)
                objectOutputStream.writeObject(postingResponses.take())
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        } catch (e: Exception) {
            throw e
        } finally {
            objectOutputStream.close()
        }

    }
}