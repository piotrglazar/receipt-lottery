package com.piotrglazar.receiptlottery

import akka.actor.ActorSystem
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ReceiptLottery {

}

object ReceiptLottery extends App {

  private val applicationContext = SpringApplication.run(classOf[ReceiptLottery], args: _*)

  private val app = applicationContext.getBean(classOf[ReceiptLoteryApp])

  app.verifyTokens()

  applicationContext.getBean(classOf[ActorSystem]).shutdown()
}
