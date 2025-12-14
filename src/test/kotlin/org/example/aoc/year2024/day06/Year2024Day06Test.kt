package org.example.aoc.year2024.day06

import kotlin.test.assertEquals
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Direction
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Direction.DOWN
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Direction.LEFT
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Direction.RIGHT
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Direction.UP
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Map
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Position
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Position.Companion.x
import org.example.common.InputReader
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class Year2024Day06Test {

    private val lines = InputReader().readTestInput("2024", "06")
    private val year2024Day06 = Year2024Day06()

    @Test
    fun part1Test() {
        assertEquals(41, year2024Day06.part1(lines))
    }

    @ParameterizedTest
    @MethodSource("isGoingOutOfMapTestTestData")
    fun isGoingOutOfMapTest(triple: Triple<Position, Direction, Boolean>) {
        val obstacle = listOf(listOf(false, false, false), listOf(false, false, false), listOf(false, false, false))

        val (initialPosition, initialDirection, expected) = triple
        assertEquals(expected, Map(obstacle, initialPosition, initialDirection).isGoingOutOfMap())
    }

    @Test
    fun part2Test() {
        assertEquals(6, year2024Day06.part2(lines))
    }

    @Test
    fun traverseMapLoopTest() {
        val testCases =
          listOf(
            Triple(
              listOf(
                ".#..", //
                "...#", //
                "#...", //
                "..#.", //
              ),
              Position(2, 1),
              UP,
            ),
            Triple(
              listOf(
                "....", //
                ".#..", //
                "#..#", //
                "..#.", //
              ),
              Position(2, 1),
              UP,
            ),
            Triple(
              listOf(
                "...#....", //
                ".#.....#", //
                ".....#..", //
                "........", //
                "..#.....", //
                "....#...", //
                "#.......", //
                "......#.", //
              ),
              Position(3, 3),
              UP,
            ),
          )
        testCases.forEach { (obstacle, guardInitialPosition, guardInitialDirection) ->
            println("Starting")
            assertTrue(Map(obstacle.map { line -> line.map { char -> char == '#' } }, guardInitialPosition, guardInitialDirection).traverseMap())
            println("Done")
        }
    }

    companion object {
        @JvmStatic
        private fun isGoingOutOfMapTestTestData(): List<Triple<Position, Direction, Boolean>> {
            return listOf(
              Triple(1 x 0, UP, false),
              Triple(1 x 0, DOWN, false),
              Triple(1 x 0, LEFT, true),
              Triple(1 x 0, RIGHT, false),
              //
              Triple(1 x 2, UP, false),
              Triple(1 x 2, DOWN, false),
              Triple(1 x 2, LEFT, false),
              Triple(1 x 2, RIGHT, true),
              //
              Triple(0 x 1, UP, true),
              Triple(0 x 1, DOWN, false),
              Triple(0 x 1, LEFT, false),
              Triple(0 x 1, RIGHT, false),
              //
              Triple(2 x 1, UP, false),
              Triple(2 x 1, DOWN, true),
              Triple(2 x 1, LEFT, false),
              Triple(2 x 1, RIGHT, false),
              //
              Triple(1 x 1, UP, false),
              Triple(1 x 1, DOWN, false),
              Triple(1 x 1, LEFT, false),
              Triple(1 x 1, RIGHT, false),
            )
        }
    }
}
