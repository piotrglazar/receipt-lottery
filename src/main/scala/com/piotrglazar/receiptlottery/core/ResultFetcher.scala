package com.piotrglazar.receiptlottery.core

import com.piotrglazar.receiptlottery.Token
import com.piotrglazar.receiptlottery.utils.HttpAdapter
import org.htmlcleaner.{HtmlCleaner, TagNode}
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Component

import scala.concurrent.{ExecutionContext, Future}

@Component
class ResultFetcher @Autowired()(@Value("${results.page}") private val pageAddress: String, private val httpAdapter: HttpAdapter,
                                 implicit private val executionContext: ExecutionContext) {

  def hasResult(token: Token): Future[Boolean] = {
    val rawContentFuture = httpAdapter.asyncRequest(pageAddress, Map("code" -> token.value))
    rawContentFuture.map { rawContent =>
      val cleanContent: TagNode = new HtmlCleaner().clean(rawContent)

      !getResultTables(cleanContent)
        .flatMap(getResultTableBody)
        .flatMap(getResultTableRows)
        .isEmpty
    }
  }

  private def getResultTables(page: TagNode): Array[TagNode] =
    page.getElementsByAttValue("class", "results-table", true, true)

  private def getResultTableBody(table: TagNode): Array[TagNode] =
    table.getElementsByName("tbody", true)

  private def getResultTableRows(tableBody: TagNode): Array[TagNode] =
    tableBody.getElementsByName("tr", true)
}
