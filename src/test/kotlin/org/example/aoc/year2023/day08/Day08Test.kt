package org.example.aoc.year2023.day08

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.example.aoc.year2023.day08.Instruction.*
import org.example.common.InputReader
import org.junit.jupiter.api.Test

class Day08Test {

    private val lines = InputReader().readTestInput("2023", "08")
    private val lines2 = InputReader().read("year2023/day08/test_input2.txt")
    private val lines3 = InputReader().read("year2023/day08/test_input3.txt")
    private val day8 = Day8()

    @Test
    fun part1Test() {
        assertEquals(2, day8.part1(lines))
    }

    @Test
    fun part1Test2() {
        assertEquals(6, day8.part1(lines2))
    }

    @Test
    fun part2Test3() {
        assertEquals(6L, day8.part2(lines3))
    }

    @Test
    fun parseGraphTest() {
        val graph = day8.parseGraph(lines)
        assertContainsEdge(graph, "AAA", "BBB", "CCC")
        assertContainsEdge(graph, "BBB", "DDD", "EEE")
        assertContainsEdge(graph, "CCC", "ZZZ", "GGG")
        assertContainsEdge(graph, "DDD", "DDD", "DDD")
        assertContainsEdge(graph, "EEE", "EEE", "EEE")
        assertContainsEdge(graph, "GGG", "GGG", "GGG")
        assertContainsEdge(graph, "ZZZ", "ZZZ", "ZZZ")
    }

    @Test
    fun parseGraphTest2() {
        val graph = day8.parseGraph(lines2)
        assertContainsEdge(graph, "AAA", "BBB", "BBB")
        assertContainsEdge(graph, "BBB", "AAA", "ZZZ")
        assertContainsEdge(graph, "ZZZ", "ZZZ", "ZZZ")
    }

    @Test
    fun parseInstructionTest() {
        assertEquals(listOf(RIGHT, LEFT), day8.parseInstructions(lines))
    }

    @Test
    fun parseInstructionTest2() {
        assertEquals(listOf(LEFT, LEFT, RIGHT), day8.parseInstructions(lines2))
    }

    private fun assertContainsEdge(graph: Graph, from: String, toLeft: String, toRight: String) {
        assertTrue(graph.containsEdgeLeft(from, toLeft), "Expected a left-edge from $from to $toLeft")
        assertTrue(graph.containsEdgeRight(from, toRight), "Expected a right-edge from $from to $toRight")
    }
}
