package nolambda.skrape.processor.chrome

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

sealed class ChromeElement {
    data class Driver(val chromeDriver: ChromeDriver) : ChromeElement()
    data class Component(val webElement: WebElement) : ChromeElement()
}

internal fun ChromeElement.findEl(selector: String): List<WebElement> {
    return when (this) {
        is ChromeElement.Driver -> chromeDriver.findElements(By.cssSelector(selector))
        is ChromeElement.Component -> webElement.findElements(By.cssSelector(selector))
    }
}

internal fun ChromeElement.attr(attrName: String): String {
    return when (this) {
        is ChromeElement.Component -> webElement.getAttribute(attrName)
        is ChromeElement.Driver -> throw IllegalStateException("Only Component can have attr")
    }
}

internal fun ChromeElement.text(): String {
    return when (this) {
        is ChromeElement.Component -> webElement.text
        is ChromeElement.Driver -> throw IllegalStateException("Only Component can have text")
    }
}