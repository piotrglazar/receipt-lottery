package com.piotrglazar.receiptlottery

import java.util.concurrent.{TimeUnit, CountDownLatch}

import com.piotrglazar.receiptlottery.ReceiptLoteryApp.{AppState, logger}
import com.piotrglazar.receiptlottery.core.{ResultFetcher, TokenReader}
import org.slf4j.{LoggerFactory, Logger}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.lang.scala.{Subscriber, Observable}
import rx.lang.scala.schedulers.IOScheduler

object ReceiptLoteryApp {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  class AppState {
    private val latch = new CountDownLatch(1)

    var winning: List[VerifiedToken] = List.empty

    def workCompleted(): Unit = latch.countDown()

    def reportError(t: Throwable): Unit = {
      logger.error(s"Error happened while verifying tokens", t)
      workCompleted()
    }

    def awaitForWorkToBeFinished(): Unit =
      if (!latch.await(10, TimeUnit.SECONDS))
        logger.error("Timeout elapsed")

    def registerWinning(verifiedToken: VerifiedToken): Unit =
      winning = verifiedToken :: winning

  }
}

@Component
class ReceiptLoteryApp @Autowired()(private val tokenReader: TokenReader, private val resultFetcher: ResultFetcher) {

  def verifyTokens(): Unit = {
    logger.info("Verifying tokens")
    val state = new AppState()

    tokenReader.readTokens()
      .flatMap(singlePageObs)
      .filter(_.isWinning)
      .subscribe(state.registerWinning, state.reportError, state.workCompleted)

    state.awaitForWorkToBeFinished()

    if (state.winning.isEmpty)
      logger.info("Sorry, no winning tokens")
    else
      state.winning.foreach(vt => logger.info("Winning token {}", vt.token.value))
  }

  private def singlePageObs(token: Token): Observable[VerifiedToken] =
    Observable { s: Subscriber[VerifiedToken] =>
      s.onNext(VerifiedToken(token, resultFetcher.hasResult(token)))
      s.onCompleted()
    }.subscribeOn(IOScheduler())

}
