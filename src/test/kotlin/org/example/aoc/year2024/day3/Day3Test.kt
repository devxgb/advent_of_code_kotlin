package org.example.aoc.year2024.day3

import kotlin.test.assertEquals
import org.example.common.readTestFile
import org.junit.jupiter.api.Test

class Day3Test {

    private val lines = readTestFile("\\year2024\\day3\\test_input.txt")
    private val day3 = Day3()

    @Test
    fun part1Test() {
        assertEquals(161, day3.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(0, day3.part2(lines))
    }
}
