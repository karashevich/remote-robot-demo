package server

import transport.Query
import transport.Response
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

open class Server {

    private val serverSocket = ServerSocket(0)
    private val port: Int

    private lateinit var connection: Socket

    private lateinit var serverSendThread: Thread
    private lateinit var serverReceiveThread: Thread

    private val postingQueries: BlockingQueue<Query> = LinkedBlockingQueue()
    private val receivingResponses: BlockingQueue<Response> = LinkedBlockingQueue()

    init {
        port = serverSocket.localPort
        serverSocket.soTimeout = 180000
    }

    fun start() {
        println("---server.Server---")
        println("Waiting for client on port: $port")
        connection = serverSocket.accept()
        println("Client accepted on port: ${connection.port}")
        serverSendThread = ServerThreads.runServerSendThread(connection, postingQueries)
        serverReceiveThread = ServerThreads.runServerReceiveThread(connection, receivingResponses)
    }

    fun sendQuery(query: Query) {
        postingQueries.put(query)
    }

    fun receiveResponse(): Response {
        return receivingResponses.take()
    }

}
