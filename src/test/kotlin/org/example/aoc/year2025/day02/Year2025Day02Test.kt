package org.example.aoc.year2025.day02

import kotlin.test.assertEquals
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Year2025Day02Test {

    private val lines = InputReader().readTestInput("2025", "02")
    private val year2025Day02 = Year2025Day02()

    @Test
    fun part1Test() {
        assertEquals(1227775554L, year2025Day02.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(4174379265, year2025Day02.part2(lines))
    }
}
