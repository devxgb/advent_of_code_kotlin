package org.example.aoc.year2024.day04

import kotlin.test.assertEquals
import org.example.aoc.year2024.day04.Year2024Day04.Companion.countXMAS
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Year2024Day04Test {

    private val lines = InputReader().readTestInput("2024", "04")
    private val year2024Day04 = Year2024Day04()

    @Test
    fun part1Test() {
        assertEquals(18, year2024Day04.part1(lines))
    }

    @Test
    fun part1Test1() {
        assertEquals(0, year2024Day04.part1(listOf("....", "....", "....", "....")))
        assertEquals(1, year2024Day04.part1(listOf("S...", ".A..", "..M.", "...X")))
        assertEquals(1, year2024Day04.part1(listOf("...X", "..M.", ".A..", "S...")))
        assertEquals(1, year2024Day04.part1(listOf("..S.", "..A.", "..M..", "..X.")))
        assertEquals(1, year2024Day04.part1(listOf("....", "....", "SAMX", "....")))
    }

    @Test
    fun countMatchXMASTest() {
        assertEquals(0, "ABCD".asSequence().countXMAS())
        assertEquals(1, "XMAS".asSequence().countXMAS())
        assertEquals(1, "AXMASA".asSequence().countXMAS())
        assertEquals(1, "AAAXMASAAA".asSequence().countXMAS())
        assertEquals(1, "XXXXMASAAA".asSequence().countXMAS())
        assertEquals(2, "AAAXMASAAAXMAS".asSequence().countXMAS())
        assertEquals(0, "AAAXMAAAAXMA".asSequence().countXMAS())
        assertEquals(2, "XMASXMAS".asSequence().countXMAS())
        assertEquals(2, "XMASSSSSSXMAS".asSequence().countXMAS())
        assertEquals(1, "XMAXMAS".asSequence().countXMAS())

        assertEquals(1, "AAASAMXAAA".asSequence().countXMAS())
        assertEquals(2, "AAASAMXAAASAMX".asSequence().countXMAS())
    }

    @Test
    fun part2Test() {
        assertEquals(9, year2024Day04.part2(lines))
    }
}
