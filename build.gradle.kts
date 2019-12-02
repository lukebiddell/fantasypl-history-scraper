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

//dependencies {
//    implementation(kotlin("stdlib-jdk8"))
//    implementation("org.seleniumhq.selenium:selenium-java:3.+")
//}

dependencies {
    compile(kotlin("stdlib"))
    compile("org.seleniumhq.selenium:selenium-java:3.+")
    compile("com.github.qwertukg:SeleniumBuilder:90f6493c72")
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

    // This line of code recursively collects and copies all of a project's files
    // and adds them to the JAR itself. One can extend this task, to skip certain
    // files or particular types at will
    from(configurations.compile.get().map { if (it.isDirectory) it else zipTree(it) })
}