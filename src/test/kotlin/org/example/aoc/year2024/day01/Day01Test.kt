package org.example.aoc.year2024.day01

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Day01Test {

    private val lines = InputReader().readTestInput("2024", "1")
    private val day01 = Day01()

    @Test
    fun part1Test() {
        assertEquals(11, day01.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(31, day01.part2(lines))
    }
}
