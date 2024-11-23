package org.example.aoc.year2023.day08

import org.example.aoc.year2023.day08.Instruction.*
import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2023\\day08\\input.txt")
    val day8 = Day8()
    println(day8.part1(lines))
    println(day8.part2(lines))
}

class Day8 {
    fun part1(lines: List<String>): Int {
        val graph = parseGraph(lines)
        val instructions = parseInstructions(lines)
        val graphTraversal = GraphTraversal(graph, "AAA", instructions) { it == "ZZZ" }
        while (graphTraversal.isEnd().not()) {
            graphTraversal.moveNext()
        }
        return graphTraversal.stepCount
    }

    fun part2(lines: List<String>): Long {
        val graph = parseGraph(lines)
        val parseInstructions = parseInstructions(lines)

        val graphTraversals = graph.getVerticesWithOutgoingEdge().filter { it.endsWith('A') }.map { node -> GraphTraversal(graph, node, parseInstructions) { it.endsWith('Z') } }

        return graphTraversals
          .map {
              while (it.isEnd().not()) {
                  it.moveNext()
              }
              it.stepCount
          }
          .map { it.toLong() }
          .reduce { acc, i -> lcm(acc, i) }
    }

    fun parseGraph(lines: List<String>): Graph {
        return lines
          .drop(2)
          .map { line ->
              val splitLine = line.split('=', ',', '(', ')', ' ').filter { it.isNotEmpty() }
              require(splitLine.size == 3)
              Triple(splitLine[0], splitLine[1], splitLine[2])
          }
          .fold(Graph()) { acc, triple ->
              acc.addDirectionalEdge(triple.first, triple.second, triple.third)
              acc
          }
    }

    fun parseInstructions(lines: List<String>): List<Instruction> {
        return lines[0].map { char ->
            when (char) {
                'L' -> {
                    LEFT
                }

                'R' -> {
                    RIGHT
                }

                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
    }
}

class Graph {
    private val edges: HashMap<String, Pair<String, String>> = hashMapOf()

    fun addDirectionalEdge(from: String, toLeft: String, toRight: String) {
        require(from !in edges)
        edges[from] = Pair(toLeft, toRight)
    }

    fun containsEdgeLeft(from: String, toLeft: String): Boolean {
        return edges[from]?.first == toLeft
    }

    fun containsEdgeRight(from: String, toRight: String): Boolean {
        return edges[from]?.second == toRight
    }

    fun getLeftEdge(from: String): String {
        return edges[from]?.first ?: throw NoSuchElementException()
    }

    fun getRightEdge(from: String): String {
        return edges[from]?.second ?: throw NoSuchElementException()
    }

    fun getVerticesWithOutgoingEdge(): Set<String> {
        return edges.keys.toSet()
    }
}

class GraphTraversal(private val graph: Graph, start: String, private val instructions: List<Instruction>, private val endCondition: (String) -> Boolean) {
    private var currentEdge: String = start
    var stepCount: Int = 0
        private set

    fun moveNext(): String {
        val instruction = instructions[stepCount.mod(instructions.size)]
        val nextEdge =
          when (instruction) {
              LEFT -> graph.getLeftEdge(currentEdge)
              RIGHT -> graph.getRightEdge(currentEdge)
          }
        currentEdge = nextEdge
        stepCount++
        return currentEdge
    }

    fun isEnd(): Boolean {
        return endCondition.invoke(currentEdge)
    }
}

enum class Instruction {
    LEFT,
    RIGHT,
}

fun hcf(a: Long, b: Long): Long {
    val (min, max) =
      if (a < b) {
          a to b
      } else {
          b to a
      }
    if (min == 0L || min == 1L) {
        return min
    }
    if (max.mod(min) == 0L) {
        return min
    }
    return hcf(max.mod(min), min)
}

fun lcm(a: Long, b: Long): Long {
    return a.times(b).div(hcf(a, b))
}
