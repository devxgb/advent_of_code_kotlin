package org.example.aoc.year2024.day05

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Year2024Day05Test {

    private val lines = InputReader().readTestInput("2024", "05")
    private val year2024Day05 = Year2024Day05()

    @Test
    fun part1Test() {
        assertEquals(143, year2024Day05.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(123, year2024Day05.part2(lines))
    }
}
