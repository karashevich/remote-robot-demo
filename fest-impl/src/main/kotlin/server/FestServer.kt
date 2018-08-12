package server

import transport.Query
import transport.query.ClassQuery
import transport.query.ComputableQuery
import transport.query.LambdaComputableQuery
import transport.response.ResultResponse
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class FestServer: Server() {

  fun <Result> performOnApp(lambda: () -> Result): Result {
    val query = LambdaComputableQuery(lambda)
    val lambdaClass = lambda.javaClass
    val classByteArray: ByteArray = lambdaClass.getResourceAsStream("${lambdaClass.name.substringAfterLast('.')}.class").toByteArray()

    println("Sending lambda in class: ${lambdaClass.name}")

    // TODO: add lock here, to prevent other sending operations
    sendQueryWithoutResult(ClassQuery(lambdaClass.name, classByteArray))
    return sendQueryWithResult(query)
  }

  private fun <Result> sendQueryWithResult(query: ComputableQuery<Result>): Result {
    sendQuery(query)
    return (receiveResponse() as ResultResponse<Result>).result
  }

  private fun sendQueryWithoutResult(query: Query) {
    sendQuery(query)
    receiveResponse()
  }


  @Throws(IOException::class)
  private fun InputStream.toByteArray(): ByteArray {
    val out = ByteArrayOutputStream()
    val byteArray = ByteArray(1000)
    var read: Int = this.read(byteArray, 0, byteArray.size)
    while (read  != -1) {
      out.write(byteArray, 0, read)
      read = this.read(byteArray, 0, byteArray.size)
    }
    out.flush()
    return out.toByteArray()
  }

}