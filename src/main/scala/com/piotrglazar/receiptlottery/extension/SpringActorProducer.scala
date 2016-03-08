package com.piotrglazar.receiptlottery.extension

import akka.actor.{Actor, IndirectActorProducer}
import com.piotrglazar.receiptlottery.core.WorkingActor
import org.springframework.context.ApplicationContext

class SpringActorProducer (private val applicationContext: ApplicationContext) extends IndirectActorProducer {
  override def produce(): Actor = {
    applicationContext.getBean("workingActor").asInstanceOf[Actor]
  }

  override def actorClass: Class[_ <: Actor] = classOf[WorkingActor]
}
