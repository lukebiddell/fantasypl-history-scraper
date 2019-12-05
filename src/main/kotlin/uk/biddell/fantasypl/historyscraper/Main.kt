package uk.biddell.fantasypl.historyscraper

fun main(args: Array<String>) {
    SeleniumScraper().startChrome(140734)
    println("Finished screenshot!")
}
