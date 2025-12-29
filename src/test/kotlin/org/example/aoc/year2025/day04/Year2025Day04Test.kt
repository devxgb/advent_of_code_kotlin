package org.example.aoc.year2025.day04

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Year2025Day04Test {

    private val lines = InputReader().readTestInput("2025", "04")
    private val year2025Day04 = Year2025Day04()

    @Test
    fun part1Test() {
        assertEquals(13, year2025Day04.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(43, year2025Day04.part2(lines))
    }
}
