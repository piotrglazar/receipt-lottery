package com.piotrglazar.receiptlottery.extension

import akka.actor.ActorSystem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class AppConfiguration {

  @Autowired
  var applicationContext: ApplicationContext = null

  @Bean
  def actorSystem(): ActorSystem = {
    val sys = ActorSystem()
    SpringExtension.provider.get(sys).initialize(applicationContext)
    sys
  }
}
