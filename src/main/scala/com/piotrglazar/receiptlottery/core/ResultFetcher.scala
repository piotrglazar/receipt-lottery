package com.piotrglazar.receiptlottery.core

import com.piotrglazar.receiptlottery.Token
import com.piotrglazar.receiptlottery.utils.HttpAdapter
import org.htmlcleaner.{HtmlCleaner, TagNode}
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Component

@Component
class ResultFetcher @Autowired()(@Value("${results.page}") private val pageAddress: String, private val httpAdapter: HttpAdapter) {

  def hasResult(token: Token): Boolean = {
    val rawContent = httpAdapter.request(pageAddress, Map("code" -> token.value))
    val cleanContent: TagNode = new HtmlCleaner().clean(rawContent)

    !getResultTables(cleanContent)
      .flatMap(getResultTableBody)
      .flatMap(getResultTableRows)
      .isEmpty
  }

  private def getResultTables(page: TagNode): Array[TagNode] =
    page.getElementsByAttValue("class", "results-table", true, true)

  private def getResultTableBody(table: TagNode): Array[TagNode] =
    table.getElementsByName("tbody", true)

  private def getResultTableRows(tableBody: TagNode): Array[TagNode] =
    tableBody.getElementsByName("tr", true)
}
