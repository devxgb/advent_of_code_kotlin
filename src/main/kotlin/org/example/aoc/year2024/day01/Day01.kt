package org.example.aoc.year2024.day01

import kotlin.math.absoluteValue
import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2024", "1")
    val day01 = Day01()
    println(day01.part1(lines))
    println(day01.part2(lines))
}

class Day01 {
    fun part1(lines: List<String>): Int {
        val (inputList1, inputList2) = parse(lines)
        require(inputList1.size == inputList2.size)
        val result = inputList1.sorted().zip(inputList2.sorted()) { a, b -> a.minus(b).absoluteValue }.sum()
        return result
    }

    fun part2(lines: List<String>): Int {
        val (inputList1, inputList2) = parse(lines)
        val count = inputList2.groupingBy { it }.eachCount()
        val result = inputList1.sumOf { count.getOrDefault(it, 0).times(it) }
        return result
    }

    fun parse(lines: List<String>): Pair<List<Int>, List<Int>> {
        return lines.fold(initial = Pair(emptyList<Int>(), emptyList<Int>())) { acc, line ->
            val split = line.split("   ")
            require(split.size == 2)
            acc.first.plus(split[0].toInt()) to acc.second.plus(split[1].toInt())
        }
    }
}
