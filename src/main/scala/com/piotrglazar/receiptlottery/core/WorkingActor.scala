package com.piotrglazar.receiptlottery.core

import akka.actor.Actor
import com.piotrglazar.receiptlottery.{Token, VerifiedToken}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component("workingActor")
@Scope("prototype")
class WorkingActor @Autowired()(private val resultFetcher: ResultFetcher) extends Actor {

  implicit private val executionContext = context.system.dispatcher

  override def receive: Receive = {
    case token: Token =>
      val requester = sender()
      val resultFuture = resultFetcher.hasResult(token)
      resultFuture.onSuccess { case result => requester ! VerifiedToken(token, result) }
  }
}
