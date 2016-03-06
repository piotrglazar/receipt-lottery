package com.piotrglazar.receiptlottery.utils

import scala.concurrent.{ExecutionContext, Future}

trait HttpAdapter {

  def request(url: String, params: Map[String, String]): String

  def asyncRequest(url: String, params: Map[String, String])(implicit executionContext: ExecutionContext): Future[String]
}


