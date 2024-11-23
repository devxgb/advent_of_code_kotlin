package org.example.aoc.year2022.day5

import java.util.regex.Pattern
import org.example.aoc.year2022.day5.ContainerStack.*
import org.example.common.readFile

private const val TIMES = "times"
private const val FROM = "from"
private const val TO = "to"
private val pattern = Pattern.compile("move (?<$TIMES>\\d+) from (?<$FROM>\\d+) to (?<$TO>\\d+)") ?: throw RuntimeException("Invalid pattern")

fun main() {
    val lines = readFile("\\year2022\\day5\\input.txt")

    // println("Container at first :\n$containerStack\n******************")
    val (containerStack1, moves1) = processInput(lines)
    moves1.forEach {
        containerStack1.move(it)
        // println("After $it :\n$containerStack\n******************")
    }
    containerStack1.topContainers.joinToString(separator = "") { it.toString() }.let { println(it) }

    // println("Container at first :\n$containerStack\n******************")
    val (containerStack2, moves2) = processInput(lines)
    moves2.forEach {
        containerStack2.moveInSameOrder(it)
        // println("After $it :\n$containerStack\n******************")
    }
    containerStack2.topContainers.joinToString(separator = "") { it.toString() }.let { println(it) }
}

fun processInput(lines: List<String>): Pair<ContainerStack, List<Move>> {
    val splitIndex = lines.indexOf("")
    val numbers = lines[splitIndex - 1].trimEnd()
    val numberOfStacks = numbers[numbers.length - 1].digitToInt()
    val containerStack =
      ContainerStack(
        lines
          .subList(0, splitIndex - 1)
          .map { line -> List(numberOfStacks) { line.getOrElse(it * 4 + 1) { ' ' } } }
          .foldRight(List(numberOfStacks) { ArrayDeque<Char>() }) { line, lists ->
              lists.mapIndexed { i: Int, stack ->
                  if (line[i] != ' ') stack.addLast(line[i])
                  stack
              }
          }
      )

    val moves =
      lines
        .subList(splitIndex + 1, lines.size)
        .map { pattern.matcher(it) }
        .map {
            if (it.matches()) {
                Move(it.group(FROM).toInt(), it.group(TO).toInt(), it.group(TIMES).toInt())
            } else {
                throw RuntimeException("Pattern not found")
            }
        }
    return containerStack to moves
}

data class ContainerStack(private val stacks: List<ArrayDeque<Char>>) {
    val topContainers: List<Char>
        get() = stacks.map { it.last() }

    data class Move(val from: Int, val to: Int, val times: Int)

    fun move(move: Move) {
        repeat(move.times) { stacks[move.from - 1].removeLast().let { stacks[move.to - 1].addLast(it) } }
    }

    fun moveInSameOrder(move: Move) {
        val temp = ArrayDeque<Char>()
        repeat(move.times) { stacks[move.from - 1].removeLast().let { temp.addLast(it) } }
        repeat(move.times) { temp.removeLast().let { stacks[move.to - 1].addLast(it) } }
    }

    override fun toString(): String {
        return stacks.joinToString(separator = "\n") { it.toString() }
    }
}
