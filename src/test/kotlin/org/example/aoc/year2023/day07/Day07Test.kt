package org.example.aoc.year2023.day07

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Day07Test {
    private val lines = InputReader().readTestInput("2023", "07")
    private val day7 = Day7()

    @Test
    fun part1Test() {
        assertEquals(6440, day7.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(5905, day7.part2(lines))
    }
}
