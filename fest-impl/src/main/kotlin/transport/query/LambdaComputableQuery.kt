package transport.query

class LambdaComputableQuery<Result>(val lambda: () -> Result): ComputableQuery<Result>()