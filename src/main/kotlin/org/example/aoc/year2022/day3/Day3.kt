package org.example.aoc.year2022.day3

import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2022\\day3\\input.txt")

    /******** Part 1 ********/
    lines
        .map {
            it.subSequence(0..it.length / 2) to it.subSequence(it.length / 2 until it.length)
        }
        .map { pair -> pair.first.asIterable().first { pair.second.contains(it) } }
        .sumOf { priority(it) }
        .let(::println)

    /******** Part 2 ********/
    lines
        .withIndex()
        .groupBy({ it.index / 3 }, { it.value })
        .values
        .map { Triple(it[0], it[1], it[2]) }
        .map { triple ->
            triple.first
                .filter { triple.second.contains(it) }
                .first { triple.third.contains(it) }
        }
        .onEach { println(it) }
        .sumOf { priority(it) }
        .let { println(it) }

}



private fun priority(char: Char): Int = if (char.isLowerCase()) {
    char.code - 96
} else {
    char.code - 38
}