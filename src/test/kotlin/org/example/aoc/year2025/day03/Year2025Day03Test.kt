package org.example.aoc.year2025.day03

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Year2025Day03Test {

    private val lines = InputReader().readTestInput("2025", "03")
    private val year2025Day03 = Year2025Day03()

    @Test
    fun part1Test() {
        assertEquals(357, year2025Day03.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(3121910778619, year2025Day03.part2(lines))
    }
}
