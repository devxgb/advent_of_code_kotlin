package org.example.aoc.year2024.day02

import kotlin.test.assertEquals
import kotlin.time.measureTime
import org.example.common.InputReader
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day02Test {

    private val testCaseLines = InputReader().readTestInput("2024", "2")
    private val day02 = Day02()

    @Test
    fun part1Test() {
        assertEquals(2, day02.part1(testCaseLines))
    }

    @Test
    fun part2Test() {
        assertEquals(4, day02.part2(testCaseLines))
    }

    @Test
    fun isSafeTest() {
        val assertIsSafe =
          fun(expected: Boolean, list: List<Int>) {
              assertEquals(expected, day02.isSafe(list))
          }
        assertIsSafe(true, listOf(1, 2, 3, 4))
        assertIsSafe(false, listOf(1, 2, 2, 3))

        assertIsSafe(true, listOf(7, 6, 4, 2, 1))
        assertIsSafe(false, listOf(1, 2, 7, 8, 9))
        assertIsSafe(false, listOf(9, 7, 6, 2, 1))
        assertIsSafe(false, listOf(1, 3, 2, 4, 5))
        assertIsSafe(false, listOf(8, 6, 4, 4, 1))
        assertIsSafe(true, listOf(1, 3, 6, 7, 9))
    }

    @Test
    fun isSafeOneLessTest() {
        val assertIsSafeOneLess =
          fun(expected: Boolean, list: List<Int>) {
              assertEquals(expected, day02.isSafeOneLess(list))
          }

        assertIsSafeOneLess(true, listOf(1, 2, 3, 4, 5))
        assertIsSafeOneLess(true, listOf(1, 2, 3, 3, 4, 5))
        assertIsSafeOneLess(false, listOf(1, 2, 3, 3, 3, 4, 5))
        assertIsSafeOneLess(true, listOf(5, 4, 3, 2, 1))
        assertIsSafeOneLess(true, listOf(5, 4, 3, 3, 2, 1))
        assertIsSafeOneLess(false, listOf(5, 4, 3, 3, 3, 2, 1))
        assertIsSafeOneLess(true, listOf(1, 2, 6, 4, 5))
        assertIsSafeOneLess(false, listOf(1, 2, 9, 6, 7))
        assertIsSafeOneLess(true, listOf(9, 8, 1, 6, 5, 4))
        assertIsSafeOneLess(false, listOf(9, 8, 1, 3, 2, 1))
        assertIsSafeOneLess(true, listOf(9, 2, 3, 4, 5, 6, 7))

        assertIsSafeOneLess(false, listOf(1, 2, 3, 7, 8, 9))
        assertIsSafeOneLess(true, listOf(1, 2, 3, 4, 5, 9))
        assertIsSafeOneLess(false, listOf(9, 8, 7, 3, 2, 1))
        assertIsSafeOneLess(true, listOf(9, 8, 7, 6, 5, 1))

        assertIsSafeOneLess(true, listOf(7, 6, 4, 2, 1))
        assertIsSafeOneLess(false, listOf(1, 2, 7, 8, 9))
        assertIsSafeOneLess(false, listOf(9, 7, 6, 2, 1))
        assertIsSafeOneLess(true, listOf(1, 3, 2, 4, 5))
        assertIsSafeOneLess(true, listOf(8, 6, 4, 4, 1))
        assertIsSafeOneLess(true, listOf(1, 3, 6, 7, 9))
    }

    @Disabled("This is Benchmark. This unnecessarily slows down test execution.")
    @Test
    fun isSafeOneLessBruteForceBenchMark() {
        val benchMarkInputList =
          listOf(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1),
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 1),
            listOf(9, 1, 2, 3, 4, 5, 6, 7, 8, 9),
          )
        val benchMark = BenchMark(warmUpIteration = 1, actualIteration = 10, repeat = 10000)
        println("isSafeOneLess")
        benchMark.benchMark { benchMarkInputList.forEach { eachInput -> day02.isSafeOneLess(eachInput) } }
        println("isSafeOneLessV2")
        benchMark.benchMark { benchMarkInputList.forEach { eachInput -> day02.isSafeOneLessV2(eachInput) } }

        println("isSafeOneLessBruteForce")
        benchMark.benchMark { benchMarkInputList.forEach { eachInput -> day02.isSafeOneLessBruteForce(eachInput) } }
    }

    private class BenchMark(private val warmUpIteration: Int, private val actualIteration: Int, private val repeat: Int) {
        fun benchMark(block: () -> Unit) {
            repeat(warmUpIteration) { block.invoke() }
            val avgDuration = (1..actualIteration).map { measureTime { repeat(repeat) { block() } } }.reduce { acc, x -> acc.plus(x) }.div(actualIteration)
            println("BenchMark Result: Average Duration = $avgDuration")
        }
    }
}
