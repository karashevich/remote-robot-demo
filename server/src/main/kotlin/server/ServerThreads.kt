package server

import transport.Query
import transport.Response
import java.io.EOFException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.concurrent.BlockingQueue
import kotlin.concurrent.thread

object ServerThreads {

  fun runServerReceiveThread(connection: Socket, receivingResponses: BlockingQueue<Response>) =
      thread(name = "server.Server Receive Thread", start = true) {
        val objectInputStream = ObjectInputStream(connection.getInputStream())
        try {
          while (connection.isConnected) receivingResponses.put(objectInputStream.readObject() as Response)
        } catch (eof: EOFException) {
          System.err.println("Connection to client is lost.")
        } catch (e: Exception) {
          throw e
        }
      }

  fun runServerSendThread(connection: Socket, postingQueries: BlockingQueue<Query>) = thread(name = "server.Server Send Thread", start = true) {
    val objectOutputStream = ObjectOutputStream(connection.getOutputStream())
    try {
      while (connection.isConnected)
        objectOutputStream.writeObject(postingQueries.take())
    } catch (e: InterruptedException) {
      Thread.currentThread().interrupt()
    } catch (e: Exception) {
      throw e
    } finally {
      objectOutputStream.close()
    }

  }

}



