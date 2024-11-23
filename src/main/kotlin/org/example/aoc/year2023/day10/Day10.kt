package org.example.aoc.year2023.day10

import org.example.aoc.year2023.day10.Day10.Companion.Direction.*
import org.example.aoc.year2023.day10.Day10.Companion.Elements.*
import org.example.aoc.year2023.day10.Day10.Companion.Elements.Companion.bottomLeftPipe
import org.example.aoc.year2023.day10.Day10.Companion.Elements.Companion.bottomRightPipe
import org.example.aoc.year2023.day10.Day10.Companion.Elements.Companion.ground
import org.example.aoc.year2023.day10.Day10.Companion.Elements.Companion.horizontalPipe
import org.example.aoc.year2023.day10.Day10.Companion.Elements.Companion.start
import org.example.aoc.year2023.day10.Day10.Companion.Elements.Companion.topLeftPipe
import org.example.aoc.year2023.day10.Day10.Companion.Elements.Companion.topRightPipe
import org.example.aoc.year2023.day10.Day10.Companion.Elements.Companion.verticalPipe
import org.example.aoc.year2023.day10.Day10.Companion.Turn.*
import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2023\\day10\\input.txt")
    val day10 = Day10()
    println(day10.part1(lines))
    println(day10.part2(lines))
}

class Day10 {
    companion object {
        data class Position(val row: Int, val column: Int) {
            fun north(): Position = Position(row - 1, column)

            fun south(): Position = Position(row + 1, column)

            fun east(): Position = Position(row, column + 1)

            fun west(): Position = Position(row, column - 1)

            fun move(direction: Direction): Position {
                return when (direction) {
                    NORTH -> Position(row - 1, column)
                    SOUTH -> Position(row + 1, column)
                    EAST -> Position(row, column + 1)
                    WEST -> Position(row, column - 1)
                }
            }

            override fun toString() = "($row, $column)"
        }

        class Layout(private val elements: List<List<Element>>) {
            private val rows: Int = elements.size
            private val columns: Int = elements[0].size
            private val startPosition: Position =
              elements
                .mapIndexed { rowIndex, row -> rowIndex to row.indexOfFirst { it is Start } }
                .first { (_, column) -> column != -1 }
                .let { (row, column) -> Position(row, column) }
            private var isMainLoopTraversed: Boolean = false
            private var isPartOfMainLoop: List<List<Boolean>>? = null
            private var isInsideMarked: Boolean = false
            private var isInside: List<List<Boolean>>? = null

            fun get(position: Position): Element {
                return elements[position.row][position.column]
            }

            private fun isPartOfMainLoop(position: Position): Boolean {
                if (!isMainLoopTraversed) throw IllegalStateException()
                return isPartOfMainLoop?.get(position.row)?.get(position.column) ?: throw IllegalStateException()
            }

            private fun isInside(row: Int, column: Int): Boolean {
                if (!isInsideMarked) throw IllegalStateException()
                return isInside?.get(row)?.get(column) ?: throw IllegalStateException()
            }

            fun north(position: Position): Position = position.north().also { checkOutOfBound(it) }

            fun south(position: Position): Position = position.south().also { checkOutOfBound(it) }

            fun east(position: Position): Position = position.east().also { checkOutOfBound(it) }

            fun west(position: Position): Position = position.west().also { checkOutOfBound(it) }

            private fun isOutOfBound(position: Position): Boolean = position.row < 0 || position.row > this.rows || position.column < 0 || position.column > this.columns

            private fun checkOutOfBound(position: Position) {
                if (isOutOfBound(position)) {
                    throw IndexOutOfBoundsException()
                }
            }

            fun findLoopLength(): Int {
                return runCatching {
                      println("Starting North")
                      findLoopLength(NORTH)
                  }
                  .recoverCatching {
                      println("Starting South")
                      findLoopLength(SOUTH)
                  }
                  .recoverCatching {
                      println("Starting East")
                      findLoopLength(EAST)
                  }
                  .recoverCatching {
                      println("Starting West")
                      findLoopLength(WEST)
                  }
                  .getOrElse { throw LoopNotFoundException() }
            }

            private fun findLoopLength(startDirection: Direction): Int {
                val nextPosition = runCatching { tryMove(startPosition, startDirection) }.getOrElse { throw LoopNotFoundException() }
                return findLoopLengthTailRec(nextPosition, startDirection, 1)
            }

            private tailrec fun findLoopLengthTailRec(position: Position, direction: Direction, accumulator: Int): Int {
                val currentElement = get(position)
                //                println("Current position : ${position}, Current Element:
                // ${currentElement::class.simpleName}, Incoming direction: ${direction.name}")
                return when (currentElement) {
                    is Ground -> throw LoopNotFoundException()
                    is Start -> accumulator
                    is Pipe -> {
                        val (newDirection, _) = currentElement.runCatching { turn(direction) }.getOrElse { throw LoopNotFoundException() }
                        val newPosition = runCatching { tryMove(position, newDirection) }.getOrElse { throw LoopNotFoundException() }
                        findLoopLengthTailRec(newPosition, newDirection, accumulator + 1)
                    }
                }
            }

            private fun tryMove(position: Position, direction: Direction): Position {
                return position.move(direction).also { checkOutOfBound(it) }
            }

            fun countInside(): Int {
                if (!isInsideMarked) {
                    runCatching {
                          println("Starting North")
                          traverseLoopAndMarkInside(NORTH)
                      }
                      .recoverCatching {
                          println("Starting South")
                          traverseLoopAndMarkInside(SOUTH)
                      }
                      .recoverCatching {
                          println("Starting East")
                          traverseLoopAndMarkInside(EAST)
                      }
                      .recoverCatching {
                          println("Starting West")
                          traverseLoopAndMarkInside(WEST)
                      }
                      .getOrElse { throw LoopNotFoundException() }
                }
                if (!isInsideMarked) throw IllegalStateException()
                return isInside?.flatten()?.count { it } ?: throw IllegalStateException()
            }

            private fun traverseLoopAndMarkInside(startDirection: Direction) {
                if (!isInsideMarked) {
                    val nextPosition = runCatching { tryMove(startPosition, startDirection) }.getOrElse { throw LoopNotFoundException() }
                    val (sideToMark, isPartOfMainLoop) = traverseMainLoopTailRec(nextPosition, startDirection, TurnStack(), List(rows) { MutableList(columns) { false } })
                    this.isMainLoopTraversed = true
                    this.isPartOfMainLoop = isPartOfMainLoop
                    val isInside = markInsideMainLoopTailRec(nextPosition, startDirection, sideToMark, List(rows) { MutableList(columns) { false } })
                    this.isInsideMarked = true
                    this.isInside = isInside
                }
            }

            private tailrec fun traverseMainLoopTailRec(
              position: Position,
              direction: Direction,
              turnStack: TurnStack,
              isPartOfMainLoop: List<MutableList<Boolean>>,
            ): Pair<Turn, List<List<Boolean>>> {
                val currentElement = get(position)
                return when (currentElement) {
                    is Ground -> throw LoopNotFoundException()
                    is Start -> return turnStack.evaluate() to isPartOfMainLoop
                    is Pipe -> {
                        val (newDirection, turn) = currentElement.runCatching { turn(direction) }.getOrElse { throw LoopNotFoundException() }
                        turnStack.push(turn)
                        isPartOfMainLoop[position.row][position.column] = true
                        val newPosition = runCatching { tryMove(position, newDirection) }.getOrElse { throw LoopNotFoundException() }
                        traverseMainLoopTailRec(newPosition, newDirection, turnStack, isPartOfMainLoop)
                    }
                }
            }

            private tailrec fun markInsideMainLoopTailRec(position: Position, direction: Direction, sideToMark: Turn, isInside: List<MutableList<Boolean>>): List<List<Boolean>> {
                val currentElement = get(position)
                return when (currentElement) {
                    is Ground -> throw LoopNotFoundException()
                    is Start -> return isInside
                    is Pipe -> {

                        val directionToMark1 = direction.turn(sideToMark)
                        val positionToMark1 = tryMove(position, directionToMark1)
                        markStraightLineTailRec(positionToMark1, directionToMark1, isInside)

                        val (newDirection, _) = currentElement.runCatching { turn(direction) }.getOrElse { throw LoopNotFoundException() }

                        val directionToMark2 = newDirection.turn(sideToMark)
                        val positionToMark2 = tryMove(position, directionToMark2)
                        markStraightLineTailRec(positionToMark2, directionToMark2, isInside)
                        //                        println("Marking for position: $position, current
                        // element: ${currentElement::class.simpleName}, currentDirection:
                        // ${direction.name}, direction-to-mark-1: ${directionToMark1.name},
                        // direction-to-mark-2: ${directionToMark2.name}")

                        val newPosition = runCatching { tryMove(position, newDirection) }.getOrElse { throw LoopNotFoundException() }
                        markInsideMainLoopTailRec(newPosition, newDirection, sideToMark, isInside)
                    }
                }
            }

            private tailrec fun markStraightLineTailRec(position: Position, direction: Direction, isInside: List<MutableList<Boolean>>) {
                val currentElement = get(position)
                return when (currentElement) {
                    is Start -> Unit
                    is Ground,
                    is Pipe -> {
                        if (isPartOfMainLoop(position)) {
                            Unit
                        } else {
                            isInside[position.row][position.column] = true
                            val newPosition = tryMove(position, direction)
                            markStraightLineTailRec(newPosition, direction, isInside)
                        }
                    }
                }
            }

            fun printStartPosition() {
                println("Start position: $startPosition")
            }

            fun visualize(scaleUp: Boolean = true, paintInsideMainLoop: Boolean = false) {
                elements
                  .mapIndexed { rowIndex, row ->
                      row
                        .mapIndexed { _, column ->
                            when (scaleUp) {
                                true ->
                                  when (column) {
                                      is Ground -> "     "
                                      is HorizontalPipe -> "—————"
                                      is VerticalPipe -> "  |  "
                                      is BottomRightPipe -> "  ┌——"
                                      is BottomLeftPipe -> "——┐  "
                                      is TopRightPipe -> "  └——"
                                      is TopLeftPipe -> "——┘  "
                                      is Start -> "——┼——"
                                  }

                                false ->
                                  when (column) {
                                      is Ground -> "   "
                                      is HorizontalPipe -> "———"
                                      is VerticalPipe -> " | "
                                      is BottomRightPipe -> " ┌—"
                                      is BottomLeftPipe -> "—┐ "
                                      is TopRightPipe -> " └—"
                                      is TopLeftPipe -> "—┘ "
                                      is Start -> "—┼—"
                                  }
                            }
                        }
                        .mapIndexed { columnIndex, column ->
                            if (paintInsideMainLoop && isInside(rowIndex, columnIndex)) {
                                if (scaleUp) {
                                    "■■■■■"
                                } else {
                                    " ■ "
                                }
                            } else {
                                column
                            }
                        }
                        .joinToString(separator = "")
                  }
                  .flatMap { line ->
                      if (scaleUp.not()) {
                          listOf(line)
                      } else {
                          val up =
                            line
                              .map {
                                  when (it) {
                                      '|',
                                      '└',
                                      '┘',
                                      '┼' -> '|'
                                      '■' -> '■'
                                      else -> ' '
                                  }
                              }
                              .joinToString(separator = "")
                          val down =
                            line
                              .map {
                                  when (it) {
                                      '|',
                                      '┌',
                                      '┐',
                                      '┼' -> '|'
                                      '■' -> '■'
                                      else -> ' '
                                  }
                              }
                              .joinToString(separator = "")
                          listOf(up, line, down)
                      }
                  }
                  .forEach { println(it) }
            }
        }

        class LoopNotFoundException() : Exception()

        class Elements {
            companion object {
                val ground = Ground
                val verticalPipe = VerticalPipe
                val horizontalPipe = HorizontalPipe
                val bottomLeftPipe = BottomLeftPipe
                val bottomRightPipe = BottomRightPipe
                val topLeftPipe = TopLeftPipe
                val topRightPipe = TopRightPipe
                val start = Start
            }

            sealed interface Element

            data object Ground : Element

            data object Start : Element

            sealed interface Pipe : Element {
                fun turn(direction: Direction): Pair<Direction, Turn>
            }

            data object VerticalPipe : Pipe {
                override fun turn(direction: Direction): Pair<Direction, Turn> {
                    return when (direction) {
                        NORTH -> NORTH to NO_TURN
                        SOUTH -> SOUTH to NO_TURN
                        EAST -> throw IllegalStateException()
                        WEST -> throw IllegalStateException()
                    }
                }
            }

            data object HorizontalPipe : Pipe {
                override fun turn(direction: Direction): Pair<Direction, Turn> {
                    return when (direction) {
                        NORTH -> throw IllegalStateException()
                        SOUTH -> throw IllegalStateException()
                        EAST -> EAST to NO_TURN
                        WEST -> WEST to NO_TURN
                    }
                }
            }

            data object BottomLeftPipe : Pipe {
                override fun turn(direction: Direction): Pair<Direction, Turn> {
                    return when (direction) {
                        NORTH -> WEST to LEFT
                        SOUTH -> throw IllegalStateException()
                        EAST -> SOUTH to RIGHT
                        WEST -> throw IllegalStateException()
                    }
                }
            }

            data object BottomRightPipe : Pipe {
                override fun turn(direction: Direction): Pair<Direction, Turn> {
                    return when (direction) {
                        NORTH -> EAST to RIGHT
                        SOUTH -> throw IllegalStateException()
                        EAST -> throw IllegalStateException()
                        WEST -> SOUTH to LEFT
                    }
                }
            }

            data object TopLeftPipe : Pipe {
                override fun turn(direction: Direction): Pair<Direction, Turn> {
                    return when (direction) {
                        NORTH -> throw IllegalStateException()
                        SOUTH -> WEST to RIGHT
                        EAST -> NORTH to LEFT
                        WEST -> throw IllegalStateException()
                    }
                }
            }

            data object TopRightPipe : Pipe {
                override fun turn(direction: Direction): Pair<Direction, Turn> {
                    return when (direction) {
                        NORTH -> throw IllegalStateException()
                        SOUTH -> EAST to LEFT
                        EAST -> throw IllegalStateException()
                        WEST -> NORTH to RIGHT
                    }
                }
            }
        }

        enum class Direction {
            NORTH,
            SOUTH,
            EAST,
            WEST;

            fun turn(turn: Turn): Direction {
                return when (turn) {
                    NO_TURN -> {
                        when (this) {
                            NORTH -> NORTH
                            SOUTH -> SOUTH
                            EAST -> EAST
                            WEST -> WEST
                        }
                    }

                    LEFT -> {
                        when (this) {
                            NORTH -> WEST
                            SOUTH -> EAST
                            EAST -> NORTH
                            WEST -> SOUTH
                        }
                    }

                    RIGHT -> {
                        when (this) {
                            NORTH -> EAST
                            SOUTH -> WEST
                            EAST -> SOUTH
                            WEST -> NORTH
                        }
                    }
                }
            }
        }

        enum class Turn {
            NO_TURN,
            LEFT,
            RIGHT,
        }

        class TurnStack {
            private val stack: ArrayDeque<Turn> = ArrayDeque()

            fun push(turn: Turn) {
                return when (turn) {
                    NO_TURN -> Unit
                    LEFT,
                    RIGHT -> {
                        try {
                            val first = stack.first()
                            if (first == turn) {
                                stack.addFirst(turn)
                            } else {
                                stack.removeFirst()
                                Unit
                            }
                        } catch (e: NoSuchElementException) {
                            stack.addFirst(turn)
                        }
                    }
                }
            }

            fun evaluate(): Turn {
                if (stack.isEmpty()) throw IllegalStateException()
                println("There are ${stack.size} elements in stack")
                println("Stack : $stack")
                return stack.first()
            }
        }

        fun visualize(lines: List<String>) {
            lines
              .map { line ->
                  line
                    .map {
                        when (it) {
                            '.' -> "     "
                            '-' -> "—————"
                            '|' -> "  |  "
                            'F' -> "  ┌——"
                            '7' -> "——┐  "
                            'L' -> "  └——"
                            'J' -> "——┘  "
                            'S' -> "——┼——"
                            else -> throw IllegalArgumentException()
                        }
                    }
                    .joinToString(separator = "")
              }
              .flatMap { line ->
                  val up =
                    line
                      .map {
                          when (it) {
                              '|',
                              '└',
                              '┘',
                              '┼' -> '|'
                              else -> ' '
                          }
                      }
                      .joinToString(separator = "")
                  val down =
                    line
                      .map {
                          when (it) {
                              '|',
                              '┌',
                              '┐',
                              '┼' -> '|'
                              else -> ' '
                          }
                      }
                      .joinToString(separator = "")
                  listOf(up, line, down)
              }
              .forEach { println(it) }
        }
    }

    fun part1(lines: List<String>): Int {
        //        visualize(lines)
        val layout = parse(lines)
        //        layout.printStartPosition()
        val loopLength = layout.findLoopLength()
        return loopLength / 2
    }

    fun part2(lines: List<String>): Int {
        val layout = parse(lines)
        val insideArea = layout.countInside()
        //        layout.visualize(scaleUp = false, paintInsideMainLoop = false)
        layout.visualize(scaleUp = false, paintInsideMainLoop = true)
        return insideArea
    }

    private fun parse(lines: List<String>): Layout {
        return lines
          .map { line ->
              line.map {
                  when (it) {
                      '.' -> ground
                      '-' -> horizontalPipe
                      '|' -> verticalPipe
                      'F' -> bottomRightPipe
                      '7' -> bottomLeftPipe
                      'L' -> topRightPipe
                      'J' -> topLeftPipe
                      'S' -> start
                      else -> throw IllegalArgumentException()
                  }
              }
          }
          .let { Layout(it) }
    }
}
