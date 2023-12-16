package org.example.aoc.year2023.day09

import org.example.common.readTestFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {

    private val lines = readTestFile("\\year2023\\day09\\test_input.txt")
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
