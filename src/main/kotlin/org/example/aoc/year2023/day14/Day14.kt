package org.example.aoc.year2023.day14

import org.example.aoc.year2023.day14.Day14.Companion.Element.*
import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2023\\day14\\input.txt")
    val day14 = Day14()
    println(day14.part1(lines))
    println(day14.part2(lines))
}

class Day14 {
    fun part1(lines: List<String>): Int {
        val platform = parse(lines)
        val result = platform.tiltNorth().calculateLoadNorth()
        return result
    }

    fun part2(lines: List<String>): Int {
        val platform = parse(lines)
        var stop = false
        var iteration = 0
        val maxIteration = 1_000_000_000
        var current = platform
        val seen: LinkedHashSet<Platform> = java.util.LinkedHashSet.newLinkedHashSet(0)
        seen.add(platform)
        var cyclePosition: Int = -1
        while (!stop && iteration < maxIteration) {
            val next = current.tiltNorthWestSouthEast()
            if (next in seen) {
                cyclePosition = seen.lastIndexOf(next)
                stop = true
            } else {
                seen.add(next)
                current = next
                iteration++
            }
        }
        check(cyclePosition == -1 || iteration < maxIteration)
        val result =
          if (cyclePosition == -1) {
              current.calculateLoadNorth()
          } else {
              val remainder = (maxIteration - cyclePosition).mod(seen.size - cyclePosition)
              seen.toList()[cyclePosition + remainder].calculateLoadNorth()
          }
        return result
    }

    fun parse(lines: List<String>): Platform {
        return Platform(
          lines.map { line ->
              line.map { char ->
                  when (char) {
                      '.' -> EMPTY
                      '#' -> SQUARE_ROCK
                      'O' -> ROUND_ROCK
                      else -> throw IllegalArgumentException()
                  }
              }
          }
        )
    }

    companion object {

        enum class Element {
            ROUND_ROCK,
            SQUARE_ROCK,
            EMPTY,
        }

        data class Coordinate(val row: Int, val column: Int) {
            fun north(): Coordinate = Coordinate(row - 1, column)

            fun south(): Coordinate = Coordinate(row + 1, column)
        }

        class Platform(private val matrix: List<List<Element>>) {
            init {
                check(matrix.isNotEmpty())
                check(matrix[0].isNotEmpty())
                check(matrix.none { it.size != matrix[0].size })
            }

            private val rowIndices = matrix.indices
            private val columnIndices = matrix[0].indices
            private val rows: Int = matrix.size
            private val columns: Int = matrix[0].size

            operator fun get(coordinate: Coordinate): Element = matrix[coordinate.row][coordinate.column]

            operator fun get(row: Int, column: Int): Element = matrix[row][column]

            private fun isWithinBound(coordinate: Coordinate): Boolean = coordinate.row in matrix.indices && coordinate.column in matrix[0].indices

            private fun isOutOfBound(coordinate: Coordinate): Boolean = isWithinBound(coordinate).not()

            fun tiltNorth(): Platform {
                var result = this
                for (rowIndex in rowIndices) {
                    for (columnIndex in columnIndices) {
                        if (result[rowIndex, columnIndex] == ROUND_ROCK) {
                            result = result.rollNorth(Coordinate(rowIndex, columnIndex))
                        }
                    }
                }
                return result
            }

            private fun rollNorth(coordinateToRoll: Coordinate): Platform {
                var currentCoordinate = coordinateToRoll.north()
                if (this.isOutOfBound(currentCoordinate) || this[currentCoordinate] != EMPTY) return this
                while (this.isWithinBound(currentCoordinate) && this[currentCoordinate] == EMPTY) {
                    currentCoordinate = currentCoordinate.north()
                }
                currentCoordinate = currentCoordinate.south()
                return this.replace(currentCoordinate, ROUND_ROCK).replace(coordinateToRoll, EMPTY)
            }

            private fun replace(coordinate: Coordinate, value: Element): Platform {
                val row = matrix[coordinate.row]
                val replaceRow = row.replace(coordinate.column, value)
                return Platform(matrix.replace(coordinate.row, replaceRow))
            }

            fun calculateLoadNorth(): Int {
                var result = 0
                for (rowIndex in rowIndices) {
                    for (columnIndex in columnIndices) {
                        if (matrix[rowIndex][columnIndex] == ROUND_ROCK) result += calculateLoadNorth(Coordinate(rowIndex, columnIndex))
                    }
                }
                return result
            }

            private fun calculateLoadNorth(coordinate: Coordinate): Int {
                return rows - coordinate.row
            }

            fun tiltNorthWestSouthEast(): Platform {
                return Platform(
                  Platform(Platform(Platform(this.tiltNorth().matrix.rotateClockWise()).tiltNorth().matrix.rotateClockWise()).tiltNorth().matrix.rotateClockWise())
                    .tiltNorth()
                    .matrix
                    .rotateClockWise()
                )
            }

            fun visualize(): String {
                return matrix.joinToString(separator = "\n") { row ->
                    row
                      .map { element ->
                          when (element) {
                              EMPTY -> '.'
                              SQUARE_ROCK -> '#'
                              ROUND_ROCK -> 'O'
                          }
                      }
                      .joinToString(separator = "")
                }
            }

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Platform

                return matrix == other.matrix
            }

            override fun hashCode(): Int {
                return matrix.hashCode()
            }

            companion object {
                private fun <T> List<List<T>>.transpose(): List<List<T>> {
                    return this.fold(initial = List(this[0].size) { emptyList() }) { acc, row -> acc.zip(row) { a, b -> a.plus(b) } }
                }

                private fun <T> List<List<T>>.rotateClockWise(): List<List<T>> {
                    return this.transpose().map { row -> row.reversed() }
                }
            }
        }

        private fun <T> List<T>.replace(index: Int, value: T): List<T> = slice(0..<index).plus(value).plus(slice(index + 1..lastIndex))
    }
}
