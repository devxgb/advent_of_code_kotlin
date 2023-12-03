package org.example.aoc.year2023.day2

import org.example.common.readTestFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day2Test {

    private val lines = readTestFile("\\year2023\\day2\\test_input.txt")
    private val day2 = Day2()

    @Test
    fun testPart1() {
        assertEquals(8, day2.part1(lines))
    }

    @Test
    fun gameTestExample1() {
        val game = Game.fromString("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")

        assertEquals(1, game.id)

        assertEquals(3, game.turns.size)

        assertEquals(4, game.turns[0].red)
        assertEquals(0, game.turns[0].green)
        assertEquals(3, game.turns[0].blue)

        assertEquals(1, game.turns[1].red)
        assertEquals(2, game.turns[1].green)
        assertEquals(6, game.turns[1].blue)

        assertEquals(0, game.turns[2].red)
        assertEquals(2, game.turns[2].green)
        assertEquals(0, game.turns[2].blue)

        assertTrue(game.isPossible(12, 13, 14))
    }

    @Test
    fun gameTestExample2() {
        val game = Game.fromString("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue")

        assertEquals(2, game.id)

        assertEquals(3, game.turns.size)

        assertEquals(0, game.turns[0].red)
        assertEquals(2, game.turns[0].green)
        assertEquals(1, game.turns[0].blue)

        assertEquals(1, game.turns[1].red)
        assertEquals(3, game.turns[1].green)
        assertEquals(4, game.turns[1].blue)

        assertEquals(0, game.turns[2].red)
        assertEquals(1, game.turns[2].green)
        assertEquals(1, game.turns[2].blue)

        assertTrue(game.isPossible(12, 13, 14))
    }

    @Test
    fun gameTestExample3() {
        val game = Game.fromString("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red")

        assertEquals(3, game.id)

        assertEquals(3, game.turns.size)

        assertEquals(20, game.turns[0].red)
        assertEquals(8, game.turns[0].green)
        assertEquals(6, game.turns[0].blue)

        assertEquals(4, game.turns[1].red)
        assertEquals(13, game.turns[1].green)
        assertEquals(5, game.turns[1].blue)

        assertEquals(1, game.turns[2].red)
        assertEquals(5, game.turns[2].green)
        assertEquals(0, game.turns[2].blue)

        assertFalse(game.isPossible(12, 13, 14))
    }

    @Test
    fun gameTestExample4() {
        val game = Game.fromString("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red")

        assertEquals(4, game.id)

        assertEquals(3, game.turns.size)

        assertEquals(3, game.turns[0].red)
        assertEquals(1, game.turns[0].green)
        assertEquals(6, game.turns[0].blue)

        assertEquals(6, game.turns[1].red)
        assertEquals(3, game.turns[1].green)
        assertEquals(0, game.turns[1].blue)

        assertEquals(14, game.turns[2].red)
        assertEquals(3, game.turns[2].green)
        assertEquals(15, game.turns[2].blue)

        assertFalse(game.isPossible(12, 13, 14))
    }

    @Test
    fun gameTestExample5() {
        val game = Game.fromString("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")

        assertEquals(5, game.id)

        assertEquals(2, game.turns.size)

        assertEquals(6, game.turns[0].red)
        assertEquals(3, game.turns[0].green)
        assertEquals(1, game.turns[0].blue)

        assertEquals(1, game.turns[1].red)
        assertEquals(2, game.turns[1].green)
        assertEquals(2, game.turns[1].blue)

        assertTrue(game.isPossible(12, 13, 14))
    }

    @Test
    fun gameTest15() {
        val game = Game.fromString("Game 15: 4 green, 12 blue, 15 red; 10 blue, 18 green, 13 red; 20 blue, 6 green, 10 red; 20 red, 12 blue, 13 green; 12 blue, 17 green, 10 red; 1 red, 3 blue, 7 green")

        assertEquals(15, game.id)

        assertEquals(6, game.turns.size)

        assertEquals(15, game.turns[0].red)
        assertEquals(4, game.turns[0].green)
        assertEquals(12, game.turns[0].blue)

        assertEquals(13, game.turns[1].red)
        assertEquals(18, game.turns[1].green)
        assertEquals(10, game.turns[1].blue)

        assertEquals(10, game.turns[2].red)
        assertEquals(6, game.turns[2].green)
        assertEquals(20, game.turns[2].blue)

        assertEquals(20, game.turns[3].red)
        assertEquals(13, game.turns[3].green)
        assertEquals(12, game.turns[3].blue)

        assertEquals(10, game.turns[4].red)
        assertEquals(17, game.turns[4].green)
        assertEquals(12, game.turns[4].blue)

        assertEquals(1, game.turns[5].red)
        assertEquals(7, game.turns[5].green)
        assertEquals(3, game.turns[5].blue)

        assertFalse(game.isPossible(12, 13, 14))
    }

    @Test
    fun gameTest2() {
        val game = Game.fromString("Game 2: 4 blue; 4 red, 3 blue, 1 green; 4 red, 9 blue, 2 green; 5 blue, 7 green, 4 red")

        assertEquals(2, game.id)

        assertEquals(4, game.turns.size)

        assertEquals(0, game.turns[0].red)
        assertEquals(0, game.turns[0].green)
        assertEquals(4, game.turns[0].blue)

        assertEquals(4, game.turns[1].red)
        assertEquals(1, game.turns[1].green)
        assertEquals(3, game.turns[1].blue)

        assertEquals(4, game.turns[2].red)
        assertEquals(2, game.turns[2].green)
        assertEquals(9, game.turns[2].blue)

        assertEquals(4, game.turns[3].red)
        assertEquals(7, game.turns[3].green)
        assertEquals(5, game.turns[3].blue)

        assertTrue(game.isPossible(12, 13, 14))
    }

    @Test
    fun gameTest23() {
        val game = Game.fromString("Game 23: 2 red, 12 green, 5 blue; 3 red, 5 blue, 3 green; 1 red, 9 green, 1 blue; 8 green, 6 blue; 13 green")

        assertEquals(23, game.id)

        assertEquals(5, game.turns.size)

        assertEquals(2, game.turns[0].red)
        assertEquals(12, game.turns[0].green)
        assertEquals(5, game.turns[0].blue)

        assertEquals(3, game.turns[1].red)
        assertEquals(3, game.turns[1].green)
        assertEquals(5, game.turns[1].blue)

        assertEquals(1, game.turns[2].red)
        assertEquals(9, game.turns[2].green)
        assertEquals(1, game.turns[2].blue)

        assertEquals(0, game.turns[3].red)
        assertEquals(8, game.turns[3].green)
        assertEquals(6, game.turns[3].blue)

        assertEquals(0, game.turns[4].red)
        assertEquals(13, game.turns[4].green)
        assertEquals(0, game.turns[4].blue)

        assertTrue(game.isPossible(12, 13, 14))
    }

    @Test
    fun testPart2() {
        assertEquals(2286, day2.part2(lines))
    }
}