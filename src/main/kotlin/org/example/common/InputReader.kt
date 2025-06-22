package org.example.common

import java.io.File
import java.io.FileNotFoundException

class InputReader {
    fun readInput(year: String, day: String): List<String> {
        return read("year$year/day$day/input.txt")
    }

    fun readTestInput(year: String, day: String): List<String> {
        return read("year$year/day$day/test_input.txt")
    }

    fun read(path: String): List<String> {
        val resource = this::class.java.classLoader.getResource(path)?.file ?: throw FileNotFoundException(path)
        return File(resource).readLines()
    }
}
