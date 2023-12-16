package org.example.aoc.year2023.day07

import org.example.common.readTestFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07Test {


    private val lines = readTestFile("\\year2023\\day07\\test_input.txt")
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