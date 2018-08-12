package transport.query

class CapturedLambdaComputableQuery<Capture, Result>(val lambda: (Capture) -> Result): ComputableQuery<Result>()