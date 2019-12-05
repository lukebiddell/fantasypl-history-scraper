package uk.biddell.fantasypl.historyscraper

import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import ru.yandex.qatools.ashot.AShot
import java.awt.image.RenderedImage
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
            println("$gameweek: $currentUrl")
            val finalGameweek = findElements(byNext).size < 2
            screenshotTeam(File("img/$teamID/${teamID}_${gameweek}.png"))
            findElement(byNext).click()
        } while (!finalGameweek)
    }

    private fun WebDriver.ashotTeam(dest: File) {
        val ele = findElement(byTeam)
        val screenshot = AShot().takeScreenshot(this, ele)
        screenshot.image.write("png", dest)
    }

    private fun ChromeDriver.screenshotTeam(dest: File) = with(getScreenshotAs(OutputType.FILE)) {

        //val scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);" +
        //"var elementTop = arguments[0].getBoundingClientRect().top;" +
        //       "window.scrollBy(0, elementTop-(viewPortHeight/2));"

        //executeScript(scrollElementIntoMiddle, ele)

        val ele = findElement(byTeam)

        ele.location.let { p ->
            println("p.x: ${p.x}\tp.y: ${p.y}")
            executeScript("window.scroll(${p.x}, ${p.y});")
        }


        //executeScript("arguments[0].scrollIntoView(false);", ele)
        cropArea(findElement(byTeam))
        copyTo(dest, true)
        executeScript("window.scrollTo(0, 0);")

    }

    private fun File.cropArea(ele: WebElement) {
        val point = ele.location
        val dimension = ele.size

        val x = zoom(point.x + 1)
        val y = zoom(point.y)
        val w = zoom(dimension.width - 1)
        val h = zoom(dimension.height)

        println("x: $x\ty: $y\tw: $w\th: $h")
//
//        val x = zoom(max(point.x + 1, 0))
//        val y = zoom(max(point.y, 0))
//        val w = zoom(min(dimension.width - 1, this@SeleniumScraper.width - point.x))
//        val h = zoom(min(dimension.height, this@SeleniumScraper.height - point.y))

        with(read()) {
            getSubimage(x, y, w, h).write("png", this@cropArea)
        }

    }

    private fun File.read() = ImageIO.read(this)

    private fun RenderedImage.write(formatName: String, output: File) =
        ImageIO.write(this, formatName, output)

}