package com.piotrglazar.receiptlottery

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ReceiptLottery {

  println("Hello world")
}

object ReceiptLottery extends App {

  SpringApplication.run(classOf[ReceiptLottery], args: _*)
}
