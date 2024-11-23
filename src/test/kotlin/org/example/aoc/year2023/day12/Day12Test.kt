package org.example.aoc.year2023.day12

import kotlin.test.assertEquals
import org.example.common.readTestFile
import org.junit.jupiter.api.Test

class Day12Test {
    private val lines = readTestFile("\\year2023\\day12\\test_input.txt")
    private val day12 = Day12()

    @Test
    fun part1Test() {
        assertEquals(21, day12.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(525152, day12.part2(lines))
    }
}
