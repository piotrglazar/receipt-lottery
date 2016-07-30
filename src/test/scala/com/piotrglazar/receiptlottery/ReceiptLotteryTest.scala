package com.piotrglazar.receiptlottery

import akka.actor.ActorSystem
import org.springframework.beans.factory.annotation.{Qualifier, Autowired}
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = Array[Class[_]](classOf[ReceiptLottery]))
class ReceiptLotteryTest extends BaseContextTest  {

  override def beforeAll(): Unit = {
    System.setProperty("token.file", "/testTokens.txt")
    System.setProperty("results.page", "https://loteriaparagonowa.gov.pl/wyniki")
    super.beforeAll()
  }

  @Autowired
  @Qualifier("actorSystem")
  var actorSystem: ActorSystem = _

  "ReceiptLottery" should "use actors to verify tokens" in {
    // given
    // context loaded

    // when
    new ReceiptLottery().runActors_=(actorSystem)

    // then
    System.setProperty("token.file", "")
  }
}
