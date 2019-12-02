package uk.biddell.fantasypl.historyscraper

import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

val byTeam: By = By.cssSelector("div[class='sc-bdVaJa elkxqB']")
val byNext: By = By.cssSelector("div[class='Pager__PagerButton-s2eddx-3 Pager__PagerButtonNext-s2eddx-4 kCqZVW']")

fun main(args: Array<String>) {
    start(140733)

    println("Finished screenshot!")
}
private fun ChromeDriver.existsElement(by: By) = findElements(by).isNotEmpty()

fun start(teamID: Int) {

    val driver = ChromeDriver(
        ChromeOptions().apply {
            setHeadless(true)
            addArguments("--window-size=1920,1880")
        }
    ).apply {
        //manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS)
    }

    var i = 1

    driver["https://fantasy.premierleague.com/entry/$teamID/event/$i"]



    while(driver.existsElement(byNext)){
        println("Saving $i")
        driver.screenshotTeam(File("${teamID}_${i++}.png"))
        println(driver.currentUrl)
        driver.findElement(byNext).click()
        println(driver.currentUrl)

    }




}

fun ChromeDriver.screenshotTeam(dest: File){
    val ele = findElement(byTeam)


    val screenshot = getScreenshotAs(OutputType.FILE)

    val point = ele.location
    val eleScreenshot = ImageIO.read(screenshot).getSubimage(
        point.getX() + 1, point.getY(),
        ele.size.getWidth(), ele.size.getHeight()
    )
    ImageIO.write(eleScreenshot, "png", screenshot)

    screenshot.copyTo(dest, true)
}

