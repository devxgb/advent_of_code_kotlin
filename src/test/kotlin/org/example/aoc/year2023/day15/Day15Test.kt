package org.example.aoc.year2023.day15

import kotlin.test.assertEquals
import org.example.common.readTestFile
import org.junit.jupiter.api.Test

class Day15Test {

    private val lines = readTestFile("\\year2023\\day15\\test_input.txt")
    private val day15 = Day15()

    @Test
    fun part1Test() {
        assertEquals(1320, day15.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(145, day15.part2(lines))
    }
}
