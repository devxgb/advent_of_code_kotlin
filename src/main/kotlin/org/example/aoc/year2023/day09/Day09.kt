package org.example.aoc.year2023.day09

import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2023\\day09\\input.txt")
    val day09 = Day09()
    println(day09.part1(lines))
    println(day09.part2(lines))
}

class Day09 {
    fun part1(lines: List<String>): Int {
        val reports = parse(lines)
        return reports.sumOf { predictRight(it) }
    }

    fun part2(lines: List<String>): Int {
        val reports = parse(lines)
        return reports.sumOf { predictLeft(it) }
    }

    fun parse(lines: List<String>): List<List<Int>> = lines.map { line -> line.split(' ').map { it.toInt() } }

    fun predictRight(report: List<Int>): Int = listOf(report).extrapolateToZero().extrapolateRight()

    fun predictLeft(report: List<Int>): Int = listOf(report).extrapolateToZero().extrapolateLeft()
}

fun List<List<Int>>.extrapolateRight(): Int = this.reversed().drop(1).fold(this.last().last()) { acc, list -> acc + list.last() }

fun List<List<Int>>.extrapolateLeft(): Int = this.reversed().drop(1).fold(this.last().first()) { acc, list -> list.first() - acc }

fun List<List<Int>>.extrapolateToZero(): List<List<Int>> =
  if (this.last().all { it == 0 }) {
      this
  } else {
      this.plus(element = this.last().difference()).extrapolateToZero()
  }

fun List<Int>.difference(): List<Int> = this.zipWithNext().map { (a, b) -> b - a }
