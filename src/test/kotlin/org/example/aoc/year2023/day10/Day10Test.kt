package org.example.aoc.year2023.day10

import kotlin.test.assertEquals
import org.example.common.readTestFile
import org.junit.jupiter.api.Test

class Day10Test {
    private val lines = readTestFile("\\year2023\\day10\\test_input.txt")
    private val day10 = Day10()

    @Test
    fun part1Test() {
        assertEquals(8, day10.part1(lines))
    }

    @Test
    fun part2Test1() {
        val lines =
          """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........

            """
            .trimIndent()
            .split('\n')
        assertEquals(4, day10.part2(lines))
    }

    @Test
    fun part2Test3() {
        val lines =
          """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L

            """
            .trimIndent()
            .split('\n')
        assertEquals(10, day10.part2(lines))
    }
}
