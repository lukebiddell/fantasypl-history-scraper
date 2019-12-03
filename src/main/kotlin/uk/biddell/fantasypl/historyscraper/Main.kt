package uk.biddell.fantasypl.historyscraper

fun main(args: Array<String>) {
    SeleniumScraper().startChrome(140733)
    println("Finished screenshot!")
}
