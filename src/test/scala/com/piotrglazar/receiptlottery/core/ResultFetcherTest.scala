package com.piotrglazar.receiptlottery.core

import com.piotrglazar.receiptlottery.Token
import com.piotrglazar.receiptlottery.utils.ScalajHttpAdapter
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FlatSpec, Matchers}

class ResultFetcherTest extends FlatSpec with Matchers {

  val resultFetcher = new ResultFetcher("https://loteriaparagonowa.gov.pl/wyniki", new ScalajHttpAdapter(2000))

  val tokens = Table(("token", "result"),
    (Token("D2T1UGL9M34"), true),
    (Token("C91B2MGBM5F"), false))

  "ResultFetcher" should "find result for token" in {
    forAll(tokens) { (token: Token, expectedResult: Boolean) =>
      // when
      val result = resultFetcher.hasResult(token)

      // then
      result shouldBe expectedResult
    }
  }

}
