package org.example.aoc.year2023.day04

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Day04Test {
    private val lines = InputReader().readTestInput("2023", "04")
    private val day4 = Day4()

    @Test
    fun part1Test() {
        assertEquals(13, day4.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(30, day4.part2(lines))
    }
}
