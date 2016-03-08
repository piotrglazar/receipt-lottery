package com.piotrglazar.receiptlottery

import akka.actor.ActorSystem
import akka.util.Timeout
import com.piotrglazar.receiptlottery.extension.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._

@SpringBootApplication
class ReceiptLottery {

  @Autowired
  def runApplication_=(a: ReceiptLoteryApp): Unit = {
    a.verifyTokens()
  }

//  @Autowired
  def runActors_=(actorSystem: ActorSystem): Unit = {
    val actor = actorSystem.actorOf(SpringExtension.provider.get(actorSystem).props())
    implicit val timeout = Timeout(5 seconds)
    val result = actor ? Token("DX9KHFP6KRT")

    println(Await.result(result, timeout.duration))
    actorSystem.stop(actor)
    actorSystem.shutdown()
    actorSystem.awaitTermination()
  }
}

object ReceiptLottery extends App {

  SpringApplication.run(classOf[ReceiptLottery], args: _*)
}
