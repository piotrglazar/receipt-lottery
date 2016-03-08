package com.piotrglazar.receiptlottery.extension

import akka.actor.{Props, ExtendedActorSystem, Extension, AbstractExtensionId}
import org.springframework.context.ApplicationContext

class SpringExt extends Extension {

  @volatile
  private var applicationContext: ApplicationContext = null

  def initialize(applicationContext: ApplicationContext): Unit = this.applicationContext = applicationContext

  def props(): Props = {
    Props.create(classOf[SpringActorProducer], applicationContext)
  }
}

object SpringExtension {

  val provider = new SpringExtension
}

class SpringExtension extends AbstractExtensionId[SpringExt] {

  override def createExtension(system: ExtendedActorSystem): SpringExt = new SpringExt
}
