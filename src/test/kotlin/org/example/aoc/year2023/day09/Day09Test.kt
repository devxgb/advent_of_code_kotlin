package org.example.aoc.year2023.day09

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Day09Test {
    private val lines = InputReader().readTestInput("2023", "09")
    private val day09 = Day09()

    @Test
    fun part1Test() {
        assertEquals(114, day09.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(2, day09.part2(lines))
    }
}
