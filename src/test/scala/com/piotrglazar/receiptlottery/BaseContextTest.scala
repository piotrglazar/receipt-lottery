package com.piotrglazar.receiptlottery

import com.github.scalaspring.scalatest.TestContextManagement
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterEach, Matchers, FlatSpec}

trait BaseContextTest extends FlatSpec with Matchers with ScalaFutures with TestContextManagement with BeforeAndAfterEach {

}
