package org.example.aoc.year2022.day4

import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2022", "4")
    val processedInput = lines.map { input -> input.split(",").map { group -> group.split("-").map { it.toInt() }.let { it[0]..it[1] } }.let { it[0] to it[1] } }

    /** ************* Part 1 ************** */
    processedInput.count { it.first.contains(it.second) || it.second.contains(it.first) }.let { println(it) }

    /** ************* Part 2 ************** */
    processedInput.count { it.first.overlaps(it.second) }.let { println(it) }
}

fun IntRange.contains(another: IntRange): Boolean = (another.first >= this.first) && (another.last <= this.last)

fun IntRange.overlaps(another: IntRange): Boolean = (this.contains(another.first) || this.contains(another.last)) || (another.contains(this.first) || another.contains(this.last))
