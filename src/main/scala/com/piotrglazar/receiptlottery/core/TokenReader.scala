package com.piotrglazar.receiptlottery.core

import com.piotrglazar.receiptlottery.Token
import org.springframework.beans.factory.annotation.{Value, Autowired}
import org.springframework.stereotype.Component
import rx.lang.scala.Observable

import scala.io.Source
import scala.util.{Failure, Success, Try}

@Component
class TokenReader @Autowired()(@Value("${token.file}") private val path: String) {

  def readTokens(): Observable[Token] = {
    readContent() match {
      case Success(items) => Observable.from(items)
      case Failure(e) => Observable.error(e)
    }
  }

  private def readContent(): Try[List[Token]] =
    Try {
      Source.fromInputStream(getClass.getResourceAsStream(path))
        .getLines()
        .filter(!_.isEmpty)
        .map(Token)
        .toList
    }
}
