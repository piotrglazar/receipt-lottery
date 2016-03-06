package com.piotrglazar.receiptlottery

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ReceiptLottery {

  @Autowired
  def runApplication_=(a: ReceiptLoteryApp): Unit = {
    a.verifyTokens()
  }
}

object ReceiptLottery extends App {

  SpringApplication.run(classOf[ReceiptLottery], args: _*)
}
