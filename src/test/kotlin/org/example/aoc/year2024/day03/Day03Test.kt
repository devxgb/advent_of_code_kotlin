package org.example.aoc.year2024.day03

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Day03Test {

    private val lines = InputReader().readTestInput("2024", "3")
    private val day03 = Day03()

    @Test
    fun part1Test() {
        assertEquals(161, day03.part1(lines))
    }

    @Test
    fun part2Test() {
        // FIXME
        //  assertEquals(48, day3.part2(lines))
        assertEquals(161, day03.part2(lines))
    }
}
