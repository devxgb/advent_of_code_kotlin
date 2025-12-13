package org.example.aoc.year2024.day02

import kotlin.math.absoluteValue
import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2024", "2")
    val day02 = Day02()
    println(day02.part1(lines))
    println(day02.part2(lines))
}

class Day02 {
    fun part1(lines: List<String>): Int {
        val inputList = parse(lines)
        val result = inputList.count { input -> isSafe(input) }
        return result
    }

    fun part2(lines: List<String>): Int {
        val inputList = parse(lines)
        val result = inputList.count { input -> isSafeOneLess(input) }
        return result
    }

    fun parse(lines: List<String>): List<List<Int>> {
        return lines.map { line -> line.split(' ').map { it.toInt() } }
    }

    fun isSafe(input: List<Int>): Boolean {
        val diffs = input.zipWithNext { a, b -> b - a }
        return isSafeDiff(diffs)
    }

    private fun isSafeDiff(diffs: List<Int>): Boolean {
        check(diffs.isNotEmpty())
        return diffs.none { it.times(diffs[0]) < 0 } && diffs.none { it.absoluteValue !in 1..3 }
    }

    /** Complexity: O(n). Slightly more efficient than [isSafeOneLessV2], but less readable */
    fun isSafeOneLess(input: List<Int>): Boolean {
        val diffs = input.zipWithNext { a, b -> b - a }
        if (isSafeDiff(diffs)) {
            return true
        }
        check(diffs.size > 2)
        val (positives, negatives) = diffs.filter { it != 0 }.partition { it > 0 }
        val majorlyIncreasing = positives.count() >= negatives.count()
        val anomalies = diffs.withIndex().filter { (_, value) -> (value == 0) || (value.absoluteValue >= 4) || majorlyIncreasing.xor(value > 0) }
        check(anomalies.isNotEmpty())
        return if (anomalies.count() > 2) {
            false
        } else {
            val anomalyIndex = anomalies.first().index
            val (updatedDiffs1, updatedDiffs2) =
              if (anomalyIndex == 0 || anomalyIndex == diffs.lastIndex) {
                  if (anomalyIndex == 0) {
                      diffs.drop(1) to listOf(diffs.first() + diffs[1]).plus(diffs.drop(2))
                  } else {
                      diffs.dropLast(1) to diffs.dropLast(2).plus(diffs[diffs.lastIndex - 1] + diffs.last())
                  }
              } else {
                  val updatedDiffs1 = diffs.take(anomalyIndex - 1).plus(diffs[anomalyIndex - 1] + diffs[anomalyIndex]).plus(diffs.drop(anomalyIndex + 1))
                  val updatedDiffs2 = diffs.take(anomalyIndex).plus(diffs[anomalyIndex] + diffs[anomalyIndex + 1]).plus(diffs.drop(anomalyIndex + 2))
                  updatedDiffs1 to updatedDiffs2
              }
            check(updatedDiffs1.isNotEmpty())
            check(updatedDiffs2.isNotEmpty())
            isSafeDiff(updatedDiffs1) || isSafeDiff(updatedDiffs2)
        }
    }

    /** Complexity: O(n). Slightly less efficient that [isSafeOneLess], but more readable */
    fun isSafeOneLessV2(input: List<Int>): Boolean {
        val diffs = input.zipWithNext { a, b -> b - a }
        if (isSafeDiff(diffs)) {
            return true
        }
        check(diffs.size > 2)
        val (positives, negatives) = diffs.filter { it != 0 }.partition { it > 0 }
        val majorlyIncreasing = positives.count() >= negatives.count()
        val anomalies = diffs.withIndex().filter { (_, value) -> (value == 0) || (value.absoluteValue >= 4) || majorlyIncreasing.xor(value > 0) }
        check(anomalies.isNotEmpty())
        return if (anomalies.count() > 2) {
            false
        } else {
            val anomalyIndex = anomalies.first().index
            val updatedInput1 = input.take(anomalyIndex).plus(input.drop(anomalyIndex + 1))
            if (isSafe(updatedInput1)) {
                return true
            }
            val updatedInput2 = input.take(anomalyIndex + 1).plus(input.drop(anomalyIndex + 2))
            if (isSafe(updatedInput2)) {
                return true
            }
            false
        }
    }

    /** Complexity: O(n^2) */
    fun isSafeOneLessBruteForce(input: List<Int>): Boolean {
        if (isSafe(input)) {
            return true
        }
        input.indices.forEach { index ->
            val updatedInput = input.take(index).plus(input.takeLast(input.lastIndex - index))
            check(input.size - updatedInput.size == 1)
            if (isSafe(updatedInput)) {
                return true
            }
        }
        return false
    }

    private fun visualizeDiff(input: List<Int>): String {
        val diffs = input.zipWithNext { a, b -> b - a }
        val anyCharacterOnce = ".".toRegex()
        val line1 =
          input
            .dropLast(1)
            .withIndex()
            .joinToString(separator = "") { (index, each) -> each.toString() + diffs[index].toString().replace(anyCharacterOnce, " ").plus("  ") }
            .plus(input.last().toString())

        val line2 =
          diffs.withIndex().joinToString(separator = "") { (index, each) -> input[index].toString().replace(anyCharacterOnce, " ").plus(" ").plus(each.toString()).plus(" ") }

        return "\n" + line1 + "\n" + line2 + "\n"
    }
}
