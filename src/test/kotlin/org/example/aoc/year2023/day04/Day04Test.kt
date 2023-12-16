package org.example.aoc.year2023.day04

import org.example.common.readTestFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day04Test {

    private val lines = readTestFile("\\year2023\\day04\\test_input.txt")
    private val day4 = Day4()

    @Test
    fun part1Test() {
        assertEquals(13,  day4.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(30,  day4.part2(lines))
    }
}