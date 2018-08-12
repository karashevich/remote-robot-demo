package client

import app.App
import transport.Response
import transport.query.ClassQuery
import transport.query.LambdaComputableQuery
import transport.response.ResultResponse
import transport.response.TypicalResults

class FestClient(serverHost: String = "127.0.0.1", serverPort: Int, receiveObjectClassloader: ClassLoader)
  : Client(
    serverHost = serverHost,
    serverPort = serverPort,
    startupActivity = { /**App().appLauncher.start()**/ },
    receiveObjectClassLoader = receiveObjectClassloader) {

  @Volatile
  private var signalToStop = false

  fun startActivity() {
    while (!signalToStop) {
      val query = super.getQuery()
      when (query) {
        is LambdaComputableQuery<*> -> {
          val result = query.lambda()
          if (result is Unit)
            sendResponse(ResultResponse(TypicalResults.UNIT_RESPONSE))
          else
            sendResponse(ResultResponse(result))
        }
        is ClassQuery -> {
          QueryClassLoader.addClass(query.className, query.classByteArray)
          QueryClassLoader.classLoader.loadClass(query.className)
          Class.forName(query.className, false, QueryClassLoader.classLoader)
          sendResponse(Response())
        }
      }
    }
  }

  fun stop() {
    signalToStop = true
  }

}

