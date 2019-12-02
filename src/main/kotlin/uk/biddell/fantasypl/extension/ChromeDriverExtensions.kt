package uk.biddell.fantasypl.extension

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

fun ChromeDriver.existsElement(by: By) = findElements(by).isNotEmpty()

fun ChromeDriver.findElementsSafe(by: By): List<WebElement>? = with(findElements(by)) {
    if (isNotEmpty()) this else null
}

fun ChromeDriver.findElementSafe(by: By): WebElement? = findElementsSafe(by)?.get(0)