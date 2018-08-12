package transport.response

import transport.Response

class ResultResponse<Result>(val result: Result): Response()