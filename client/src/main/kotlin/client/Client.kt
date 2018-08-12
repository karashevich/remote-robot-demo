package client

import transport.Query
import transport.Response
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

open class Client(serverHost: String = "127.0.0.1", serverPort: Int, receiveObjectClassLoader: ClassLoader, startupActivity: () -> Unit = {}) {

    private val connection: Socket = Socket()
    private val clientConnectionTimeout = 60000 //in ms

    private val clientReceiveThread: Thread
    private val clientSendThread: Thread

    private val receivingQueries: BlockingQueue<Query> = LinkedBlockingQueue()
    private val postingResponses: BlockingQueue<Response> = LinkedBlockingQueue()

    init {
        connection.connect(InetSocketAddress(InetAddress.getByName(serverHost), serverPort), clientConnectionTimeout)
        println("Client successfully connected to server on port: $serverPort")
        println("Client's response port is: ${connection.port}")

        clientSendThread = ClientThreads.runClientSendThread(connection, postingResponses)
        clientReceiveThread = ClientThreads.runClientReceiveThread(connection, receivingQueries, receiveObjectClassLoader)

        startupActivity()
    }

    fun getQuery(): Query {
        return receivingQueries.take()
    }

    fun sendResponse(response: Response) {
        postingResponses.put(response)
    }

}