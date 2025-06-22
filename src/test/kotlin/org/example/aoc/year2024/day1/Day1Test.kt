package org.example.aoc.year2024.day1

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Day1Test {

    private val lines = InputReader().readTestInput("2024", "1")
    private val day1 = Day1()

    @Test
    fun part1Test() {
        assertEquals(11, day1.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(31, day1.part2(lines))
    }
}
