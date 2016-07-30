package com.piotrglazar.receiptlottery.core

import com.piotrglazar.receiptlottery.Token
import com.piotrglazar.receiptlottery.utils.ScalajHttpAdapter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

class ResultFetcherTest extends FlatSpec with Matchers with ScalaFutures {

  implicit val timeout = PatienceConfig(Span(1, Seconds))

  val resultFetcher = new ResultFetcher("https://loteriaparagonowa.gov.pl/wyniki", new ScalajHttpAdapter(2000), global)

  val tokens = Table(("token", "result"),
    (Token("D2T1UGL9M34"), true),
    (Token("C91B2MGBM5F"), false))

  "ResultFetcher" should "find result for token" in {
    forAll(tokens) { (token: Token, expectedResult: Boolean) =>
      // when
      val result = resultFetcher.hasResult(token)

      // then
      result.futureValue shouldBe expectedResult
    }
  }

}
