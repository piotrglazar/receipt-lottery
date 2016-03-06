package com.piotrglazar.receiptlottery.utils

import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Component

import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http

@Component
class ScalajHttpAdapter @Autowired()(@Value("${http.timeouts:2000}") private val timeouts: Int) extends HttpAdapter {

  override def request(url: String, params: Map[String, String]): String = {
    Http(url)
      .params(params)
      .timeout(timeouts, timeouts)
      .asString
      .body
  }

  override def asyncRequest(url: String, params: Map[String, String])(implicit executionContext: ExecutionContext): Future[String] = {
    Future(request(url, params))
  }
}
