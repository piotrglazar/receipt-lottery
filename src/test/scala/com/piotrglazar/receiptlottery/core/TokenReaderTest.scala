package com.piotrglazar.receiptlottery.core

import com.piotrglazar.receiptlottery.Token
import org.scalatest.{Matchers, FlatSpec}

class TokenReaderTest extends FlatSpec with Matchers {

  "TokenReader" should "just read tokens" in {
    // given
    val tokenFile = "/testTokens.txt"

    // when
    val reader = new TokenReader(tokenFile)

    // then
    reader.readTokens().toBlocking.toList shouldBe List(Token("123ABC"), Token("456DEF"), Token("789GHI"))
  }

  "TokenReader" should "report error when something goes wrong" in {
    // given
    val notExistingTokenFile = "/notExistingFile.txt"

    // when
    val reader = new TokenReader(notExistingTokenFile)

    // then
    intercept[NullPointerException] { reader.readTokens().toBlocking.toList }
  }

  "TokenReader" should "return empty observable when token file is empty" in {
    // given
    val emptyTokenFile = "/noTokens.txt"

    // when
    val reader = new TokenReader(emptyTokenFile)

    // then
    reader.readTokens().toBlocking.toList shouldBe List.empty
  }
}
