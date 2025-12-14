package org.example.aoc.year2025.day01

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Year2025Day01Test {

    private val lines = InputReader().readTestInput("2025", "01")
    private val year2025Day01 = Year2025Day01()

    @Test
    fun part1Test() {
        assertEquals(3, year2025Day01.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(6, year2025Day01.part2(lines))
    }
}
