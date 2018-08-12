package transport.query

import transport.Query
import transport.response.ResultResponse


open class ComputableQuery<Result>: Query() {

  fun getResponse(): ResultResponse<Result>? = null

}