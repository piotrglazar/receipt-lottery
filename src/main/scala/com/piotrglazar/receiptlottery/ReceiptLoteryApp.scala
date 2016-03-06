package com.piotrglazar.receiptlottery

import java.util.concurrent.{TimeUnit, CountDownLatch}

import com.piotrglazar.receiptlottery.reader.TokenReader
import org.slf4j.{LoggerFactory, Logger}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.lang.scala.{Subscriber, Observable}
import rx.lang.scala.schedulers.IOScheduler

@Component
class ReceiptLoteryApp @Autowired()(private val tokenReader: TokenReader, private val resultFetcher: ResultFetcher) {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def verifyTokens(): Unit = {
    logger.info("Verifying tokens")
    val latch = new CountDownLatch(1)

    tokenReader.readTokens()
      .flatMap(singlePageObs)
      .filter(_.isWinning)
      .subscribe(println(_), e => {logger.error(s"Error happened", e); latch.countDown()}, () => latch.countDown())

    if (!latch.await(10, TimeUnit.SECONDS))
      logger.error("Timeout elapsed")
  }

  private def singlePageObs(token: Token): Observable[VerifiedToken] =
    Observable { s: Subscriber[VerifiedToken] =>
      s.onNext(VerifiedToken(token, resultFetcher.hasResult(token)))
      s.onCompleted()
    }.subscribeOn(IOScheduler())

}
