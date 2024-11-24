package org.example.aoc.year2023.day13

import kotlin.test.assertEquals
import org.example.common.readTestFile
import org.junit.jupiter.api.Test

class Day13Test {

    private val lines = readTestFile("\\year2023\\day13\\test_input.txt")
    private val day13 = Day13()

    @Test
    fun part1Test() {
        assertEquals(405, day13.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(400, day13.part2(lines))
    }
}
