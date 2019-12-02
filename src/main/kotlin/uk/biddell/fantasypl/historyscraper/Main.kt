package uk.biddell.fantasypl.historyscraper

import kz.qwertukg.driver
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

private const val timeout = 3L

val byTeam: By = By.cssSelector("div[class='sc-bdVaJa elkxqB']")
val byNext: By = By.cssSelector("div[class='Pager__PagerButton-s2eddx-3 Pager__PagerButtonNext-s2eddx-4 kCqZVW']")

fun main(args: Array<String>) {
    start(140733)

    println("Finished screenshot!")
}

private fun ChromeDriver.existsElement(by: By) = findElements(by).isNotEmpty()

private fun ChromeDriver.findElementsSafe(by: By): List<WebElement>? = with(findElements(by)) {
    if (isNotEmpty()) this else null
}

private fun ChromeDriver.findElementSafe(by: By): WebElement? = findElementsSafe(by)?.get(0)

fun test() {
    val teamID = 140733
    val i = 1

    driver(ChromeDriver()) {
        get("https://fantasy.premierleague.com/entry/$teamID/event/$i")
    }
}

fun start(teamID: Int) {

    val driver = ChromeDriver(
        ChromeOptions().apply {
            setHeadless(true)
            addArguments("--window-size=1920,1880")
        }
    ).apply {
        manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS)
    }

    var i = 1

    driver["https://fantasy.premierleague.com/entry/$teamID/event/$i"]


    //while(driver.existsElement(byNext)){}
    while (driver.findElements(byNext).size >= 2) {
        println(driver.currentUrl)
        WebDriverWait(driver, timeout)
            .until(ExpectedConditions.visibilityOfElementLocated(byNext))
        //if(!driver.elementExists(byNext)) break

        println("Saving $i")
        driver.screenshotTeam(File("img/${teamID}_${i++}.png"))

        driver.findElementSafe(byNext)?.click() ?: println("Could not find next button?")
        //driver["https://fantasy.premierleague.com/entry/$teamID/event/$i"]
        //val a = driver.findElement(byNext).click()
        //println(driver.currentUrl)

    }




}

fun ChromeDriver.screenshotTeam(dest: File) {
    //val ele = findElement(byTeam)
    val ele = WebDriverWait(this, 10)
        .until(ExpectedConditions.visibilityOfElementLocated(byTeam))

    val screenshot = getScreenshotAs(OutputType.FILE)

    val point = ele.location
    val eleScreenshot = ImageIO.read(screenshot).getSubimage(
        point.getX() + 1, point.getY(),
        ele.size.getWidth(), ele.size.getHeight()
    )
    ImageIO.write(eleScreenshot, "png", screenshot)

    screenshot.copyTo(dest, true)
}

