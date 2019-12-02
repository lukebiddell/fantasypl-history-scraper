package uk.biddell.fantasypl.historyscraper

import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import uk.biddell.fantasypl.extension.findElementSafe
import java.io.File
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
import kotlin.math.roundToInt

private const val timeout = 3L
private const val zoom = 1.0
private const val width = 1800
private const val height = 1850

private val byTeam: By = By.cssSelector("div[class='sc-bdVaJa elkxqB']")
private val byNext: By =
    By.cssSelector("div[class='Pager__PagerButton-s2eddx-3 Pager__PagerButtonNext-s2eddx-4 kCqZVW']")

private fun zoom(x: Int) = (x * zoom).roundToInt()

fun main(args: Array<String>) {
    start(140733)
    println("Finished screenshot!")
}

fun start(teamID: Int) = ChromeDriver(ChromeOptions().apply {
    setHeadless(true)
    addArguments("--window-size=${zoom(width)},${zoom(height)}")
}).run {
    manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS)

    var i = 1

    get("https://fantasy.premierleague.com/entry/$teamID/event/$i")

    do {
        println(currentUrl)
        val finalGameweek = findElements(byNext).size < 2
        screenshotTeam(File("img/${teamID}_${i++}.png"))
        findElementSafe(byNext)?.click()
    } while (!finalGameweek)
}

private fun ChromeDriver.screenshotTeam(dest: File) {
    executeScript("document.body.style.zoom = '$zoom'")

    val ele = WebDriverWait(this, timeout)
        .until(ExpectedConditions.visibilityOfElementLocated(byTeam))

    val screenshot = getScreenshotAs(OutputType.FILE)

    val point = ele.location
    val dimension = ele.size

    val croppedScreenshot = ImageIO.read(screenshot).getSubimage(
        zoom(point.x + 1), zoom(point.y),
        zoom(dimension.width), zoom(dimension.height)
    )

    ImageIO.write(croppedScreenshot, "png", screenshot)

    screenshot.copyTo(dest, true)

    executeScript("document.body.style.zoom = '1.0'")
}

