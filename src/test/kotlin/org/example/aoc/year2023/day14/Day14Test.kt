package org.example.aoc.year2023.day14

import kotlin.test.assertEquals
import org.example.common.readTestFile
import org.junit.jupiter.api.Test

class Day14Test {

    private val lines = readTestFile("\\year2023\\day14\\test_input.txt")
    private val day14 = Day14()

    @Test
    fun part1Test() {
        assertEquals(136, day14.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(64, day14.part2(lines))
    }

    @Test
    fun tiltNorthTest() {
        fun assertTiltNorth(input: String, expected: String) {
            assertEquals(expected, day14.parse(input.split('\n')).tiltNorth().visualize())
        }

        assertTiltNorth(
          """
                ..
                ..
          """
            .trimIndent(),
          """
                ..
                ..
          """
            .trimIndent(),
        )
        assertTiltNorth(
          """
                ..
                ##
          """
            .trimIndent(),
          """
                ..
                ##
          """
            .trimIndent(),
        )
        assertTiltNorth(
          """
                OO
                ..
          """
            .trimIndent(),
          """
                OO
                ..
          """
            .trimIndent(),
        )
        assertTiltNorth(
          """
                ..
                OO
          """
            .trimIndent(),
          """
                OO
                ..
          """
            .trimIndent(),
        )
        assertTiltNorth(
          """
                .O
                OO
          """
            .trimIndent(),
          """
                OO
                .O
          """
            .trimIndent(),
        )
        assertTiltNorth(
          """
                ...
                O.O
                .OO
          """
            .trimIndent(),
          """
                OOO
                ..O
                ...
          """
            .trimIndent(),
        )
        assertTiltNorth(
          """
                ..O
                .OO
                OOO
          """
            .trimIndent(),
          """
                OOO
                .OO
                ..O
          """
            .trimIndent(),
        )
        assertTiltNorth(
          """
                .#.
                ..#
                OOO
          """
            .trimIndent(),
          """
                O#.
                .O#
                ..O
          """
            .trimIndent(),
        )
    }

    @Test
    fun calculateLoadNorth() {
        fun assertCalculateLoadNorth(input: String, expected: Int) {
            assertEquals(expected, day14.parse(input.split('\n')).calculateLoadNorth())
        }

        assertCalculateLoadNorth(
          """
                OOOO.#.O..
                OO..#....#
                OO..O##..O
                O..#.OO...
                ........#.
                ..#....#.#
                ..O..#.O.O
                ..O.......
                #....###..
                #....#....
            """
            .trimIndent(),
          136,
        )
    }
}
