package uk.biddell.fantasypl.historyscraper

import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
import kotlin.math.roundToInt


//private val robot = Robot()

class SeleniumScraper() {

    private val timeout = 100L
    private val zoom = 1.0
    private val numberOfZooms = 6
    private val width = 1800
    private val height = 1850
    private val headless = true

    private val byTeam = By.cssSelector("div[class='sc-bdVaJa elkxqB']")
    private val byNext =
        By.cssSelector("div[class='Pager__PagerButton-s2eddx-3 Pager__PagerButtonNext-s2eddx-4 kCqZVW']")

    private val options = ChromeOptions().apply {
        //setPageLoadStrategy(PageLoadStrategy.NONE)
        addArguments("--window-size=$width,$height", "--force-device-scale-factor=$zoom")
        //addArguments("start-maximized") // https://stackoverflow.com/a/26283818/1689770
        addArguments("enable-automation") // https://stackoverflow.com/a/43840128/1689770
        setHeadless(headless)
        addArguments("--no-sandbox") //https://stackoverflow.com/a/50725918/1689770
        addArguments("--disable-infobars") //https://stackoverflow.com/a/43840128/1689770
        addArguments("--disable-dev-shm-usage") //https://stackoverflow.com/a/50725918/1689770
        addArguments("--disable-browser-side-navigation") //https://stackoverflow.com/a/49123152/1689770
        addArguments("--disable-gpu") //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
    }

    private val driver = ChromeDriver(options).apply {
        manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS)
    }

    private fun zoom(x: Int) = (x * zoom).roundToInt()


    fun startChrome(teamID: Int) = driver.run {
        get("https://fantasy.premierleague.com/entry/$teamID/event/1")

        do {
            val gameweek = with(currentUrl) { substring(this.lastIndexOf('/') + 1) }
            println(gameweek)
            val finalGameweek = findElements(byNext).size < 2
            screenshotTeam(File("img/${teamID}_${gameweek}.png"))
            findElement(byNext).click()
        } while (!finalGameweek)
    }

    private fun ChromeDriver.screenshotTeam(dest: File) {
        val ele = findElement(byTeam)
        val screenshot = getScreenshotAs(OutputType.FILE)

        val point = ele.location
        val dimension = ele.size

        val croppedScreenshot = ImageIO.read(screenshot).getSubimage(
            zoom(point.x + 1), zoom(point.y),
            zoom(dimension.width - 1), zoom(dimension.height)
        )

        ImageIO.write(croppedScreenshot, "png", screenshot)

        screenshot.copyTo(dest, true)
    }

}