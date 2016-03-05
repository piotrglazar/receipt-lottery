package com.piotrglazar.receiptlottery

import com.piotrglazar.receiptlottery.reader.TokenReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LoteryApp @Autowired()(private val tokenReader: TokenReader) {
  tokenReader.readTokens()
    .subscribe(println(_))
}
