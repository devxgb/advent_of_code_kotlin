package org.example.common

import java.io.File

fun readTestFile(path: String): List<String> = File("${System.getProperty("java.class.path").split(";").first { it.contains("build\\\\resources\\\\test") }}$path").readLines()
