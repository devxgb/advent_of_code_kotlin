package org.example.aoc.year2023.day03

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Day03AlternateVisualizationTest {
    private val lines = InputReader().readTestInput("2023", "03")
    private val day3AlternateVisualization = Day3AlternateVisualization()

    @Test
    fun testPart1() {
        assertEquals(4361, day3AlternateVisualization.part1(lines))
    }

    @Test
    fun testPart2() {
        assertEquals(467835, day3AlternateVisualization.part2(lines))
    }
}
