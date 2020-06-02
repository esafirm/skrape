package nolambda.skrape

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import nolambda.skrape.nodes.Value
import nolambda.skrape.processor.chrome.ChromeValueFormatter
import nolambda.skrape.processor.chrome.NoWait

class ChromeValueFormatterSpec : StringSpec({
    val formatter = ChromeValueFormatter(NoWait)
    val value = Value(
        name = "value",
        valueType = Value.TYPE_STRING,
        selector = "td.a"
    )

    "it will recognize type string" {
        formatter.isForType(value) shouldBe true
    }

    "it won't recognize unknown value type" {
        formatter.isForType(Value("", "", "")) shouldBe false
    }
})