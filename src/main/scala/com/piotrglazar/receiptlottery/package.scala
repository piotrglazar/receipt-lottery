package com.piotrglazar

package object receiptlottery {

  case class Token(value: String)

  case class VerifiedToken(token: Token, isWinning: Boolean)
}
