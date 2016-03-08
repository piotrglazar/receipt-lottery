package com.piotrglazar.receiptlottery.core

import akka.actor.Actor
import com.piotrglazar.receiptlottery.{Token, VerifiedToken}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component("workingActor")
@Scope("prototype")
class WorkingActor @Autowired()(private val resultFetcher: ResultFetcher) extends Actor {

  override def receive: Receive = {
    case token: Token =>
      val result = resultFetcher.hasResult(token)
      sender() ! VerifiedToken(token, result)
  }
}
