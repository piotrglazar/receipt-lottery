package com.piotrglazar.receiptlottery

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import akka.pattern.ask
import com.piotrglazar.receiptlottery.core.TokenReader
import com.piotrglazar.receiptlottery.extension.SpringExtension
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.test.context.ContextConfiguration
import rx.lang.scala.Observable

import scala.concurrent.duration._

@ContextConfiguration(classes = Array[Class[_]](classOf[ReceiptLottery]))
class ReceiptLotteryTest extends BaseContextTest  {

  override def beforeAll(): Unit = {
    System.setProperty("token.file", "/realTokens.txt")
    System.setProperty("results.page", "https://loteriaparagonowa.gov.pl/wyniki")
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    System.setProperty("token.file", "")
    super.afterAll()
  }

  @Autowired
  @Qualifier("actorSystem")
  var actorSystem: ActorSystem = _

  @Autowired
  var tokenReader: TokenReader = _

  "ReceiptLottery" should "use actors to verify tokens" in {
    // given
    val actor = actorSystem.actorOf(SpringExtension.provider.get(actorSystem).props())

    // when
    val tokens = runActors(actor, tokenReader)

    // then
    val tokenList = tokens.toBlocking.toList
    tokenList should have size 1

    // cleanup
    actorSystem.stop(actor)
  }

  private def runActors(actor: ActorRef, tokenReader: TokenReader): Observable[VerifiedToken] = {
    implicit val timeout = Timeout(5 seconds)
    tokenReader
      .readTokens()
      .flatMap(t => Observable.from((actor ? t).mapTo[VerifiedToken])(actorSystem.dispatcher))
  }
}
