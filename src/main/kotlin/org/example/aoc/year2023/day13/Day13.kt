package org.example.aoc.year2023.day13

import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2023\\day13\\input.txt")
    val day13 = Day13()
    println(day13.part1(lines))
    println(day13.part2(lines))
}

class Day13 {
    fun part1(lines: List<String>): Int {
        val inputList = parse(lines)
        //        inputList.forEach { println(visualize(it)+'\n') }
        val result =
          inputList
            .map { input ->
                val verticalSymmetry = findVerticalSymmetry(input)
                if (verticalSymmetry != -1) return@map verticalSymmetry + 1
                val horizontalSymmetry = findHorizontalSymmetry(input)
                if (horizontalSymmetry != -1) return@map (horizontalSymmetry + 1) * 100
                throw IllegalArgumentException()
            }
            .sum()
        return result
    }

    fun part2(lines: List<String>): Int {
        val inputList = parse(lines)
        //        inputList.forEach { println(visualize(it)+'\n') }
        val result =
          inputList
            .map { input ->
                val verticalSymmetryWithOneDiff = findVerticalSymmetryWithOneDiff(input)
                if (verticalSymmetryWithOneDiff != -1) return@map verticalSymmetryWithOneDiff + 1
                val horizontalSymmetryWithOneDiff = findHorizontalSymmetryWithOneDiff(input)
                if (horizontalSymmetryWithOneDiff != -1) return@map (horizontalSymmetryWithOneDiff + 1) * 100
                throw IllegalArgumentException()
            }
            .sum()
        return result
    }

    private fun findVerticalSymmetry(matrix: List<List<Boolean>>): Int {
        return findHorizontalSymmetry(transpose(matrix))
    }

    private fun findHorizontalSymmetry(matrix: List<List<Boolean>>): Int {
        var rowOfSymmetry = -1
        for (rowIndex in 0..matrix.size - 2) {
            if (isRowOfSymmetry(matrix, rowIndex)) {
                rowOfSymmetry = rowIndex
                break
            }
        }
        return rowOfSymmetry
    }

    private fun isRowOfSymmetry(matrix: List<List<Boolean>>, row: Int): Boolean {
        check(row in 0..matrix.size - 2)
        var isRowOfSymmetry = true
        var currentRow = row
        var currentReflectionRow = row + 1
        while (isRowOfSymmetry && currentRow >= 0 && currentReflectionRow < matrix.size) {
            if (equals(matrix[currentRow], matrix[currentReflectionRow])) {
                currentRow--
                currentReflectionRow++
            } else {
                isRowOfSymmetry = false
            }
        }
        return isRowOfSymmetry
    }

    private fun equals(list1: List<Boolean>, list2: List<Boolean>): Boolean {
        return if (list1.isEmpty() && list2.isEmpty()) {
            true
        } else if (list1.isEmpty() || list2.isEmpty()) {
            false
        } else {
            (list1.size == list2.size) && list1.zip(list2) { element1, element2 -> element1.xor(element2) }.reduce { acc, element -> acc.or(element) }.not()
        }
    }

    private fun findVerticalSymmetryWithOneDiff(matrix: List<List<Boolean>>): Int {
        return findHorizontalSymmetryWithOneDiff(transpose(matrix))
    }

    private fun findHorizontalSymmetryWithOneDiff(matrix: List<List<Boolean>>): Int {
        var rowOfSymmetry = -1
        for (rowIndex in 0..matrix.size - 2) {
            if (isRowOfSymmetryWithOneDiff(matrix, rowIndex)) {
                rowOfSymmetry = rowIndex
                break
            }
        }
        return rowOfSymmetry
    }

    private fun isRowOfSymmetryWithOneDiff(matrix: List<List<Boolean>>, row: Int): Boolean {
        check(row in 0..matrix.size - 2)
        var numberOfDiffTotal = 0
        var currentRow = row
        var currentReflectionRow = row + 1
        while (currentRow >= 0 && currentReflectionRow < matrix.size) {
            val numberOfDiff = numberOfDiffs(matrix[currentRow], matrix[currentReflectionRow])
            numberOfDiffTotal += numberOfDiff
            currentRow--
            currentReflectionRow++
            if (numberOfDiffTotal > 1) return false
        }
        return numberOfDiffTotal == 1
    }

    private fun numberOfDiffs(list1: List<Boolean>, list2: List<Boolean>): Int {
        check(list1.isNotEmpty() && list2.isNotEmpty())
        check(list1.size == list2.size)
        return list1.zip(list2) { element1, element2 -> element1 == element2 }.count { !it }
    }

    private fun transpose(matrix: List<List<Boolean>>): List<List<Boolean>> {
        if (matrix.isEmpty()) return emptyList()
        check(matrix.none { it.size != matrix[0].size })
        return matrix.fold(initial = List(matrix[0].size) { emptyList<Boolean>() }) { acc, row -> acc.zip(row) { list, element -> list.plus(element) } }
    }

    private fun parse(lines: List<String>): List<List<List<Boolean>>> {
        return lines
          .map { line ->
              line.map { each ->
                  when (each) {
                      '#' -> true
                      '.' -> false
                      else -> throw IllegalArgumentException()
                  }
              }
          }
          .split(emptyList())
    }

    private fun <T> List<T>.split(delimiter: T): List<List<T>> {
        val delimiterIndices = this.withIndex().filter { it.value == delimiter }.map { it.index }
        return (listOf(-1) + delimiterIndices).zip(delimiterIndices + listOf(this.size)).map { (first, second) -> this.subList(first + 1, second) }
    }

    private fun visualize(matrix: List<List<Boolean>>): String {
        return matrix.joinToString(separator = "\n") { row -> row.map { element -> if (element) '#' else '.' }.joinToString(separator = "") }
    }
}
