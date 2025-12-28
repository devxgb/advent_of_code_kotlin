package org.example.aoc.year2025.day02

import kotlin.math.log
import kotlin.math.pow
import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2025", "02")
    val year2025Day02 = Year2025Day02()
    println(year2025Day02.part1(lines))
    println(year2025Day02.part2(lines))
}

class Year2025Day02 {

    companion object {
        fun isRepeatingTwice(num: Long): Boolean {
            val digitCount = log(num.toDouble(), 10.0).toInt() + 1
            if (digitCount % 2 != 0) {
                return false
            }
            val tens = 10.0.pow(digitCount / 2).toInt()
            val firstPart = num / tens
            val secondPart = num % tens
            return firstPart == secondPart
        }

        fun isRepeating(str: String): Boolean {
            if (str.length <= 1) {
                return false
            }
            for (i in 1..(str.length / 2)) {
                if (str.length % i == 0) {
                    if (isRepeating(str, i)) {
                        return true
                    }
                }
            }
            return false
        }

        fun isRepeating(str: String, repeatSize: Int): Boolean {
            require(str.length % repeatSize == 0)
            val match = str.take(repeatSize)
            var i = 1
            while (i * repeatSize <= str.lastIndex) {
                if (!subStringEqual(str, match, i * repeatSize)) {
                    return false
                }
                i++
            }
            return true
        }

        fun subStringEqual(str: String, match: String, startIndex: Int): Boolean {
            for (i in 0..match.lastIndex) {
                if (match[i] != str[startIndex + i]) {
                    return false
                }
            }
            return true
        }
    }

    fun part1(lines: List<String>): Long {
        require(lines.size == 1)
        return lines
          .first()
          .split(',')
          .map { range ->
              val split = range.split('-')
              require(split.size == 2)
              LongRange(split[0].toLong(), split[1].toLong())
          }
          .sumOf { range -> range.filter { isRepeatingTwice(it) }.sum() }
    }

    fun part2(lines: List<String>): Long {
        require(lines.size == 1)
        return lines
          .first()
          .split(',')
          .map { range ->
              val split = range.split('-')
              require(split.size == 2)
              LongRange(split[0].toLong(), split[1].toLong())
          }
          .sumOf { range -> range.filter { isRepeating(it.toString()) }.sum() }
    }
}
