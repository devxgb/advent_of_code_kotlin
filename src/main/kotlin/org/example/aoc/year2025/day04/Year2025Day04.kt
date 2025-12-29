package org.example.aoc.year2025.day04

import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2025", "04")
    val year2025Day04 = Year2025Day04()
    println(year2025Day04.part1(lines))
    println(year2025Day04.part2(lines))
}

class Year2025Day04 {
    fun part1(lines: List<String>): Int {
        val grid = Grid(lines.map { line -> line.map { char -> char == '@' } })
        return grid.neighborCount.flatten().count { it in 0..<4 }
    }

    fun part2(lines: List<String>): Int {
        val grid = Grid(lines.map { line -> line.map { char -> char == '@' } })
        val initialCount = grid.grid.flatten().count { it }
        while (grid.neighborCount.flatten().count { it in 0..<4 } > 0) {
            grid.removeOuter()
        }
        return initialCount - grid.neighborCount.flatten().count { it >= 4 }
    }

    class Grid(val grid: List<List<Boolean>>) {
        val rows = grid.size
        val cols = grid[0].size
        private val _neighborCount: List<MutableList<Int>> = countNeighbors()
        val neighborCount: List<List<Int>> = _neighborCount

        private fun neighbours(location: Pair<Int, Int>): List<Pair<Int, Int>> {
            val x = location.first
            val y = location.second
            return listOfNotNull(
              (x - 1 to y - 1).takeIf { x > 0 && y > 0 },
              (x - 1 to y).takeIf { x > 0 },
              (x - 1 to y + 1).takeIf { x > 0 && y < cols - 1 },
              (x to y - 1).takeIf { y > 0 },
              (x to y + 1).takeIf { y < cols - 1 },
              (x + 1 to y - 1).takeIf { x < rows - 1 && y > 0 },
              (x + 1 to y).takeIf { x < rows - 1 },
              (x + 1 to y + 1).takeIf { x < rows - 1 && y < cols - 1 },
            )
        }

        private fun countNeighbors(): List<MutableList<Int>> {
            val neighborCount = List(rows) { MutableList(cols) { -1 } }
            for (i in 0..<rows) {
                for (j in 0..<cols) {
                    if (grid[i][j]) {
                        neighborCount[i][j] = 0
                    }
                }
            }
            for (i in 0..<rows) {
                for (j in 0..<cols) {
                    if (grid[i][j]) {
                        neighbours(i to j).filter { (x, y) -> neighborCount[x][y] >= 0 }.forEach { (x, y) -> neighborCount[x][y]++ }
                    }
                }
            }
            return neighborCount
        }

        fun removeOuter() {
            for (i in 0..<rows) {
                for (j in 0..<cols) {
                    if (_neighborCount[i][j] in 0..<4) {
                        _neighborCount[i][j] = -1
                        neighbours(i to j).forEach { (x, y) -> _neighborCount[x][y]-- }
                    }
                }
            }
        }
    }
}
