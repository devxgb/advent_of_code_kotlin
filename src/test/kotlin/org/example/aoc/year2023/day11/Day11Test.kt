package org.example.aoc.year2023.day11

import org.example.aoc.year2023.day11.Day11.Companion.PositionInt
import org.example.aoc.year2023.day11.Day11.Companion.Pair
import org.example.aoc.year2023.day11.Day11.Companion.allSelfPairs
import org.example.aoc.year2023.day11.Day11.Companion.duplicateEmptyRowsAndColumn
import org.example.aoc.year2023.day11.Day11.Companion.findEmptyColumns
import org.example.aoc.year2023.day11.Day11.Companion.findEmptyRows
import org.example.aoc.year2023.day11.Day11.Companion.getAllTrue
import org.example.aoc.year2023.day11.Day11.Companion.transpose
import org.example.common.readTestFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {

    private val lines = readTestFile("\\year2023\\day11\\test_input.txt")
    private val day11 = Day11()

    @Test
    fun testFindEmptyRow() {
        val matrix = listOf(
            listOf(false, false, true),
            listOf(false, true, false),
            listOf(false, false, false),
        )
        val result = matrix.findEmptyRows()
        assertEquals(setOf(2), result)
    }

    @Test
    fun testFindEmptyColumn() {
        val matrix = listOf(
            listOf(false, false, true),
            listOf(false, true, false),
            listOf(false, false, false),
        )
        val result = matrix.findEmptyColumns()
        assertEquals(setOf(0), result)
    }

    @Test
    fun testTranspose() {
        val matrix = listOf(
            listOf(false, true, false, true),
            listOf(false, false, true, false),
            listOf(false, false, false, true),
            listOf(false, false, false, false),
        )

        val expected = listOf(
            listOf(false, false, false, false),
            listOf(true, false, false, false),
            listOf(false, true, false, false),
            listOf(true, false, true, false),
        )
        val result = matrix.transpose()
        assertEquals(expected, result)
    }

    @Test
    fun testTranspose2() {
        val matrix = listOf(
            listOf(false, true, false, true),
            listOf(false, false, true, false),
            listOf(false, false, false, true),
            listOf(false, false, false, false),
            listOf(false, false, false, false),
            listOf(false, false, false, false),
            listOf(false, false, false, false),
            listOf(false, false, false, false),
        )

        val expected = listOf(
            listOf(false, false, false, false, false, false, false, false),
            listOf(true, false, false, false, false, false, false, false),
            listOf(false, true, false, false, false, false, false, false),
            listOf(true, false, true, false, false, false, false, false),
        )
        val result = matrix.transpose()
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleEmptyRowsAndColumns() {
        val result = day11.parse(lines).duplicateEmptyRowsAndColumn()
        val expected = """
            ....#........
            .........#...
            #............
            .............
            .............
            ........#....
            .#...........
            ............#
            .............
            .............
            .........#...
            #....#.......
        """.trimIndent()
        assertEquals(expected, visualize(result))
    }

    @Test
    fun testGetAllTrue() {
        val input = """
            ....#........
            .........#...
            #............
            .............
            .............
            ........#....
            .#...........
            ............#
            .............
            .............
            .........#...
            #....#.......
        """.trimIndent().split("\n")
        val result = day11.parse(input).getAllTrue()
        val expected = setOf(
            PositionInt(0, 4),
            PositionInt(1, 9),
            PositionInt(2, 0),
            PositionInt(5, 8),
            PositionInt(6, 1),
            PositionInt(7, 12),
            PositionInt(10, 9),
            PositionInt(11, 0),
            PositionInt(11, 5),
        )
        assertEquals(expected, result)
    }

    @Test
    fun testAllSelfPair() {
        val input = setOf(1, 2, 3)
        val result = input.allSelfPairs()
        val expected = setOf(
            Pair(1,1),Pair(1,2),Pair(1,3),Pair(2,2),Pair(2,3),Pair(3,3),
        )
        assertEquals(expected, result)
    }

    @Test
    fun part1Test() {
        assertEquals(374, day11.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(1030, day11.part2(lines, 10))
    }

    @Test
    fun part2Test2() {
        assertEquals(8410, day11.part2(lines, 100))
    }

    private fun visualize(matrix: List<List<Boolean>>): String {
        return matrix.joinToString(separator = "\n") { row ->
            row.map { element -> if (element) '#' else '.' }.joinToString(separator = "")
        }
    }
}
