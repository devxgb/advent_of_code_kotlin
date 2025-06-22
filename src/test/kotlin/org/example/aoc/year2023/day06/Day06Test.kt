package org.example.aoc.year2023.day06

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Day06Test {
    private val lines = InputReader().readTestInput("2023", "06")
    private val day6 = Day6()

    @Test
    fun part1() {
        assertEquals(288, day6.part1(lines))
    }

    @Test
    fun part2() {
        assertEquals(71503, day6.part2(lines))
    }

    @Test
    fun parsePart1Test() {
        assertEquals(listOf(7 to 9, 15 to 40, 30 to 200), day6.parsePart1(lines))
    }

    @Test
    fun parsePart2Test() {
        assertEquals(71530L to 940200L, day6.parsePart2(lines))
    }

    @Test
    fun calculateDistanceTravelledTest() {
        assertEquals(6, day6.calculateDistanceTravelled(7, 1))
        assertEquals(10, day6.calculateDistanceTravelled(7, 2))
        assertEquals(12, day6.calculateDistanceTravelled(7, 3))
        assertEquals(12, day6.calculateDistanceTravelled(7, 4))
        assertEquals(10, day6.calculateDistanceTravelled(7, 5))
        assertEquals(6, day6.calculateDistanceTravelled(7, 6))
    }

    @Test
    fun calculateNumberOfWin() {
        assertEquals(4, day6.calculateNumberOfWin(7, 9))
        assertEquals(8, day6.calculateNumberOfWin(15, 40))
        assertEquals(9, day6.calculateNumberOfWin(30, 200))
        assertEquals(71503, day6.calculateNumberOfWin(71530, 940200))
    }
}
