plugins {
    application
    kotlin("jvm") version "1.3.61"
}

application {
    mainClassName = "uk.biddell.fantasypl.historyscraper.MainKt"
}

group = "uk.biddell.fantasypl.historyscraper"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compile(kotlin("stdlib"))
    compile("org.seleniumhq.selenium:selenium-java:3.141.59")
    compile("ru.yandex.qatools.ashot:ashot:1.5.2")
    compile("commons-io:commons-io:2.5")
    compile("com.google.code.gson:gson:2.8.5")
    compile("org.hamcrest:hamcrest:2.1")
    //compile("com.github.qwertukg:SeleniumBuilder:90f6493c72")
}


//tasks {
//    compileKotlin {
//        kotlinOptions.jvmTarget = "1.8"
//    }
//    compileTestKotlin {
//        kotlinOptions.jvmTarget = "1.8"
//    }
//}

//tasks.withType<Jar> {
//    manifest {
//        attributes["Main-Class"] = application.mainClassName
//    }
//
//    from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
//}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }

    from(configurations.compile.get().map { if (it.isDirectory) it else zipTree(it) })
}