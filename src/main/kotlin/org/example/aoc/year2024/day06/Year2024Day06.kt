package org.example.aoc.year2024.day06

import org.example.aoc.year2024.day06.Year2024Day06.Companion.Direction.DOWN
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Direction.LEFT
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Direction.RIGHT
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Direction.UP
import org.example.aoc.year2024.day06.Year2024Day06.Companion.Position.Companion.x
import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2024", "06")
    val year2024Day06 = Year2024Day06()
    println(year2024Day06.part1(lines))
    println(year2024Day06.part2(lines))
}

class Year2024Day06 {
    fun part1(lines: List<String>): Int {
        val obstacle = lines.map { line -> line.map { char -> char == '#' } }
        val guardInitialPosition = lines.mapIndexed { row, line -> row to line.indexOfFirst { it == '^' } }.first { (_, col) -> col >= 0 }.let { (row, col) -> row x col }
        val map = Map(obstacle, guardInitialPosition, UP)
        val isLoop = map.traverseMap()
        require(!isLoop)
        return map.getTotalVisited()
    }

    fun part2(lines: List<String>): Int {
        val obstacle = lines.map { line -> line.map { char -> char == '#' } }
        val guardInitialPosition = lines.mapIndexed { row, line -> row to line.indexOfFirst { it == '^' } }.first { (_, col) -> col >= 0 }.let { (row, col) -> row x col }

        val referenceMap = Map(obstacle, guardInitialPosition, UP)
        val isLoop = referenceMap.traverseMap()
        require(!isLoop)

        var loopCount = 0
        val mutableObstacle: MutableList<MutableList<Boolean>> = obstacle.map { each -> each.toMutableList() }.toMutableList()
        for (i in 0..obstacle.lastIndex) {
            for (j in 0..obstacle[0].lastIndex) {
                if (!mutableObstacle[i][j] && referenceMap.visitedDirection(i x j) != null) {
                    mutableObstacle[i][j] = true
                    val map = Map(mutableObstacle, guardInitialPosition, UP)
                    val isLoop = map.traverseMap()
                    if (isLoop) {
                        loopCount++
                    }
                    mutableObstacle[i][j] = false
                }
            }
        }

        return loopCount
    }

    companion object {
        class Map(val obstacle: List<List<Boolean>>, guardInitialPosition: Position, guardInitialDirection: Direction) {
            var guardCurrentPosition: Position = guardInitialPosition
                private set

            var guardCurrentDirection: Direction = guardInitialDirection
                private set

            val row: Int = obstacle.size
            val col: Int = obstacle[0].size
            private val visitedDirection: ArrayList<ArrayList<Direction?>> = create2DArrayList(row, col, null)

            init {
                markIsVisited(guardInitialPosition, guardInitialDirection)
            }

            /** @return isLoop */
            fun traverseMap(): Boolean {
                while (isGoingOutOfMap().not()) {
                    moveNext()
                    if (visitedDirection(guardCurrentPosition) == guardCurrentDirection) {
                        //                        println(visitedDirection.joinToString(separator =
                        // "") { line -> line.map { if (it != null) '*' else '.'
                        // }.joinToString(separator = " ", postfix = "\n") })
                        return true
                    }
                    markIsVisited(guardCurrentPosition, guardCurrentDirection)
                }
                //                println(visitedDirection.joinToString(separator = "") { line ->
                // line.map { if (it != null) '*' else '.' }.joinToString(separator = " ", postfix =
                // "\n") })
                return false
            }

            fun getTotalVisited(): Int = visitedDirection.flatten().count { it != null }

            fun moveNext() {
                val newPosition = guardCurrentPosition.move(guardCurrentDirection)
                if (isObstacle(newPosition)) {
                    guardCurrentDirection = guardCurrentDirection.turnRight()
                } else {
                    guardCurrentPosition = newPosition
                }
            }

            fun markIsVisited(position: Position, direction: Direction) {
                if(visitedDirection[position.row][position.col]==null) {
                    visitedDirection[position.row][position.col] = direction
                }
            }

            fun visitedDirection(position: Position): Direction? = visitedDirection[position.row][position.col]

            fun isObstacle(position: Position): Boolean = obstacle[position.row][position.col]

            fun isGoingOutOfMap(): Boolean {
                return when {
                    guardCurrentDirection == UP && guardCurrentPosition.row <= 0 -> {
                        true
                    }

                    guardCurrentDirection == DOWN && guardCurrentPosition.row >= row - 1 -> {
                        true
                    }

                    guardCurrentDirection == LEFT && guardCurrentPosition.col <= 0 -> {
                        true
                    }

                    guardCurrentDirection == RIGHT && guardCurrentPosition.col >= col - 1 -> {
                        true
                    }

                    else -> {
                        false
                    }
                }
            }

            companion object {
                private fun <T> create2DArrayList(row: Int, col: Int, initialValue: T): ArrayList<ArrayList<T>> {
                    val arrayList = ArrayList<ArrayList<T>>(row)
                    repeat(row) {
                        val rowList = ArrayList<T>(col)
                        repeat(col) { rowList.add(initialValue) }
                        arrayList.add(rowList)
                    }
                    require(arrayList.size == row)
                    require(arrayList[0].size == col)
                    return arrayList
                }
            }
        }

        data class Position(val row: Int, val col: Int) {
            fun move(direction: Direction): Position =
              when (direction) {
                  UP -> row - 1 x col
                  DOWN -> row + 1 x col
                  LEFT -> row x col - 1
                  RIGHT -> row x col + 1
              }

            companion object {
                infix fun Int.x(other: Int): Position = Position(this, other)
            }
        }

        enum class Direction {
            UP {
                override fun turnRight() = RIGHT
            },
            DOWN {
                override fun turnRight() = LEFT
            },
            LEFT {
                override fun turnRight() = UP
            },
            RIGHT {
                override fun turnRight() = DOWN
            };

            abstract fun turnRight(): Direction
        }
    }
}
