package nolambda.skrape.processor.chrome

import org.openqa.selenium.support.ui.WebDriverWait

interface ChromeWaiter {
    fun until(block: () -> Boolean)
}

class WebChromeWaiter(private val webDriverWait: WebDriverWait) : ChromeWaiter {
    override fun until(block: () -> Boolean) {
        try {
            webDriverWait.until { block.invoke() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

object NoWait : ChromeWaiter {
    override fun until(block: () -> Boolean) {
    }
}

