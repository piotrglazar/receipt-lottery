package com.piotrglazar.receiptlottery

import java.util
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{ConcurrentLinkedQueue, CountDownLatch, TimeUnit}

import com.piotrglazar.receiptlottery.ReceiptLoteryApp.{AppState, logger}
import com.piotrglazar.receiptlottery.core.{ResultFetcher, TokenReader}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.lang.scala.Observable
import rx.lang.scala.schedulers.IOScheduler

import scala.concurrent.ExecutionContext

object ReceiptLoteryApp {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  class AppState {
    private val latch: CountDownLatch = new CountDownLatch(1)

    val numberOfVerifiedTokens: AtomicInteger = new AtomicInteger()

    val winning: ConcurrentLinkedQueue[VerifiedToken] = new ConcurrentLinkedQueue()

    def workCompleted(): Unit = latch.countDown()

    def reportError(t: Throwable): Unit = {
      logger.error(s"Error happened while verifying tokens", t)
      workCompleted()
    }

    def awaitForWorkToBeFinished(): Unit =
      if (!latch.await(1, TimeUnit.MINUTES))
        logger.error("Timeout elapsed")

    def registerToken(verifiedToken: VerifiedToken): Unit = {
      numberOfVerifiedTokens.incrementAndGet()
      if (verifiedToken.isWinning)
        winning.add(verifiedToken)
    }
  }
}

@Component
class ReceiptLoteryApp @Autowired()(private val tokenReader: TokenReader, private val resultFetcher: ResultFetcher,
                                    implicit private val executionContext: ExecutionContext) {

  def verifyTokens(): Unit = {
    logger.info("Verifying tokens")
    val state = new AppState()

    tokenReader.readTokens()
      .flatMap(singlePageObs)
      .subscribe(state.registerToken, state.reportError, state.workCompleted)

    state.awaitForWorkToBeFinished()

    if (state.winning.isEmpty)
      logger.info(s"Sorry, no winning tokens, verified ${state.numberOfVerifiedTokens.get()}")
    else
      logWinningTokens(state.winning)
  }

  private def logWinningTokens(queue: util.Queue[VerifiedToken]): Unit =
    while (!queue.isEmpty) {
      logger.info("Winning token {}", queue.poll())
    }

  private def singlePageObs(token: Token): Observable[VerifiedToken] =
    Observable.from(resultFetcher.hasResult(token))
      .map(result => VerifiedToken(token, result))
      .subscribeOn(IOScheduler())
}
