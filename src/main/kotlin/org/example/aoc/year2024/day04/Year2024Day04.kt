package org.example.aoc.year2024.day04

import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2024", "04")
    val year2024Day04 = Year2024Day04()
    println(year2024Day04.part1(lines))
    println(year2024Day04.part2(lines))
}

class Year2024Day04 {
    fun part1(lines: List<String>): Int {
        return countXMAS(lines)
    }

    fun part2(lines: List<String>): Int {
        return countCrossMAS(lines)
    }

    private fun countXMAS(lines: List<String>): Int {
        return (0..lines.lastIndex).map { i -> lines.iterateRow(i) }.sumOf { it.countXMAS() } +
          (0..lines[0].lastIndex).map { j -> lines.iterateColumn(j) }.sumOf { it.countXMAS() } +
          (0..lines[0].length - 4).map { j -> lines.iterateDiagonal1(0, j) }.sumOf { it.countXMAS() } +
          (1..lines.size - 4).map { i -> lines.iterateDiagonal1(i, 0) }.sumOf { it.countXMAS() } +
          (3..lines.lastIndex).map { i -> lines.iterateDiagonal2(i, 0) }.sumOf { it.countXMAS() } +
          (1..lines[0].length - 4).map { j -> lines.iterateDiagonal2(lines.lastIndex, j) }.sumOf { it.countXMAS() }
    }

    private fun countCrossMAS(lines: List<String>): Int {
        var count = 0
        for (i in 1..lines.lastIndex - 1) {
            for (j in 1..lines[0].lastIndex - 1) {
                if (lines.isCrossMAS(i, j)) {
                    count++
                }
            }
        }
        return count
    }

    private fun List<String>.isCrossMAS(row: Int, col: Int): Boolean {
        if (this[row][col] == 'A') {
            if ((this[row - 1][col - 1] == 'M' && this[row + 1][col + 1] == 'S') || (this[row - 1][col - 1] == 'S' && this[row + 1][col + 1] == 'M')) {
                if ((this[row + 1][col - 1] == 'M' && this[row - 1][col + 1] == 'S') || (this[row + 1][col - 1] == 'S' && this[row - 1][col + 1] == 'M')) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        private const val PATTERN = "XMAS"

        fun Sequence<Char>.countXMAS(): Int {
            val list = this.toList()
            return countXMAS(list) + countXMAS(list.reversed())
        }

        private fun countXMAS(list: List<Char>): Int {
            var i = 0
            var j = 0
            var count = 0
            while (i < list.size) {
                if (list[i] == PATTERN[j]) {
                    j++
                    if (j >= 4) {
                        count++
                        j = 0
                    }
                } else {
                    j = 0
                }
                if (list[i] == PATTERN[j]) {
                    j++
                }
                i++
            }
            return count
        }

        private fun List<String>.iterateRow(row: Int): Sequence<Char> {
            val list = this
            return sequence { yieldAll(list[row].toList()) }
        }

        private fun List<String>.iterateColumn(col: Int): Sequence<Char> {
            val list = this
            return sequence {
                for (i in 0..list.lastIndex) {
                    yield(list[i][col])
                }
            }
        }

        private fun List<String>.iterateDiagonal1(row: Int, col: Int): Sequence<Char> {
            val list = this
            return sequence {
                var i = row
                var j = col
                while (i < list.size && j < list[0].length) {
                    yield(list[i][j])
                    i++
                    j++
                }
            }
        }

        private fun List<String>.iterateDiagonal2(row: Int, col: Int): Sequence<Char> {
            val list = this
            return sequence {
                var i = row
                var j = col
                while (i >= 0 && j < list[0].length) {
                    yield(list[i][j])
                    i--
                    j++
                }
            }
        }
    }
}
