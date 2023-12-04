package org.example.aoc.year2023.day3

import org.example.common.readTestFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day3AlternateVisualizationTest {
    private val lines = readTestFile("\\year2023\\day3\\test_input.txt")
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