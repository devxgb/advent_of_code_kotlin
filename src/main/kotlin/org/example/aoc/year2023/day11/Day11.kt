package org.example.aoc.year2023.day11

import kotlin.math.absoluteValue
import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2023\\day11\\input.txt")
    val day11 = Day11()
    println(day11.part1(lines))
    println(day11.part2(lines, 1000000))
}

class Day11 {
    fun part1(lines: List<String>): Int {
        val matrix = parse(lines)
        val result = matrix.duplicateEmptyRowsAndColumn().getAllTrue().allSelfPairs().sumOf { (first, second) -> first.manhattanDistance(second) }
        return result
    }

    fun part2(lines: List<String>, timesToExpand: Int): Long {
        val matrix = parse(lines)
        val emptyRows = matrix.findEmptyRows()
        val emptyColumns = matrix.findEmptyColumns()
        val result =
          matrix
            .getAllTrue()
            .map { it.tolong() }
            .toSet()
            .duplicateRows(rowsToDuplicate = emptyRows, timesToDuplicate = timesToExpand)
            .duplicateColumns(columnsToDuplicate = emptyColumns, timesToDuplicate = timesToExpand)
            .allSelfPairs()
            .sumOf { (first, second) -> first.manhattanDistance(second) }
        return result
    }

    fun parse(lines: List<String>): List<List<Boolean>> {
        return lines.map { line -> line.map { char -> char == '#' } }
    }

    companion object {
        fun List<List<Boolean>>.findEmptyRows(): Set<Int> {
            return this.withIndex().filter { (_, row) -> row.all { element -> !element } }.map { (rowIndex, _) -> rowIndex }.toSet()
        }

        fun List<List<Boolean>>.findEmptyColumns(): Set<Int> {
            return this.transpose().findEmptyRows()
        }

        fun List<List<Boolean>>.transpose(): List<List<Boolean>> {
            return this.fold(initial = List(this.size) { emptyList() }) { acc, row -> acc.zip(row).map { (accRow, element) -> accRow.plus(element) } }
        }

        fun List<List<Boolean>>.duplicateEmptyRowsAndColumn(): List<List<Boolean>> {
            val emptyRows = this.findEmptyRows()
            val emptyColumns = this.findEmptyColumns()
            return this.duplicateRows(emptyRows).duplicateColumns(emptyColumns)
        }

        private fun List<List<Boolean>>.duplicateRows(rowsToDuplicate: Set<Int>): List<List<Boolean>> {
            return this.flatMapIndexed { rowIndex, row ->
                if (rowsToDuplicate.contains(rowIndex)) {
                    listOf(row, row)
                } else {
                    listOf(row)
                }
            }
        }

        private fun List<List<Boolean>>.duplicateColumns(columnsToDouble: Set<Int>): List<List<Boolean>> {
            return this.map { row ->
                row.flatMapIndexed { columnIndex: Int, element: Boolean ->
                    if (columnsToDouble.contains(columnIndex)) {
                        listOf(element, element)
                    } else {
                        listOf(element)
                    }
                }
            }
            // FIXME This does not work
            //            return this.transpose().doubleRows(columnsToDouble).transpose()
        }

        fun List<List<Boolean>>.getAllTrue(): Set<PositionInt> {
            return this.flatMapIndexed { rowIndex, row -> row.mapIndexed { columnIndex, element -> PositionInt(rowIndex, columnIndex) to element } }
              .filter { (_, element) -> element }
              .map { (position, _) -> position }
              .toSet()
        }

        fun Set<PositionLong>.duplicateRows(rowsToDuplicate: Set<Int>, timesToDuplicate: Int): Set<PositionLong> {
            return this.map { eachPosition ->
                  val numberOfRowsToShift = rowsToDuplicate.count { each -> eachPosition.row > each }.toLong().times(timesToDuplicate - 1)
                  eachPosition.plus(row = numberOfRowsToShift)
              }
              .toSet()
        }

        fun Set<PositionLong>.duplicateColumns(columnsToDuplicate: Set<Int>, timesToDuplicate: Int): Set<PositionLong> {
            return this.map { eachPosition ->
                  val numberOfColumnsToShift = columnsToDuplicate.count { each -> eachPosition.column > each }.toLong().times(timesToDuplicate - 1)
                  eachPosition.plus(column = numberOfColumnsToShift)
              }
              .toSet()
        }

        data class PositionInt(val row: Int, val column: Int) {
            fun manhattanDistance(other: PositionInt): Int = (this.row - other.row).absoluteValue + (this.column - other.column).absoluteValue

            fun plus(row: Int = 0, column: Int = 0): PositionInt = PositionInt(this.row + row, this.column + column)

            fun plus(row: Long = 0, column: Long = 0): PositionLong = PositionLong(this.row + row, this.column + column)

            fun tolong() = PositionLong(row.toLong(), column.toLong())

            override fun toString(): String = "($row, $column)"
        }

        data class PositionLong(val row: Long, val column: Long) {
            fun manhattanDistance(other: PositionLong): Long = (this.row - other.row).absoluteValue + (this.column - other.column).absoluteValue

            fun plus(row: Long = 0, column: Long = 0): PositionLong = PositionLong(this.row + row, this.column + column)

            override fun toString(): String = "($row, $column)"
        }

        data class Pair<T>(val first: T, val second: T) {
            override fun equals(other: Any?): Boolean {
                return (other is Pair<*> && ((first == other.first && second == other.second) || (first == other.second && second == other.first)))
            }

            override fun hashCode(): Int {
                return first.hashCode() + second.hashCode()
            }
        }

        fun <T> Set<T>.allSelfPairs(): Set<Pair<T>> {
            return this.flatMap { each -> this.map { Pair(each, it) } }.toSet()
        }
    }
}
