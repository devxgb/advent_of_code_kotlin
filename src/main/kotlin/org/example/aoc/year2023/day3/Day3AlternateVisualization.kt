package org.example.aoc.year2023.day3

import org.example.common.readFile


fun main() {
    val lines = readFile("\\year2023\\day3\\input.txt")
    val day3AlternateVisualization = Day3AlternateVisualization()
    println(day3AlternateVisualization.part1(lines))
    println(day3AlternateVisualization.part2(lines))
}

// For Visualization uncomment print statements
// Visualization stored under resources
class Day3AlternateVisualization {
    fun part1(lines: List<String>): Int {
//        println("Part1--------------------------------------------------")
        val engineSchematic = EngineSchematic.fromString(lines)
//        println(engineSchematic.toString())
//        println("-------------------------------------------------------")
        engineSchematic.markAllPartNumbers()
//        println(engineSchematic.toString())
//        println("-------------------------------------------------------")
        return engineSchematic.addAllPartNumber()
    }

    fun part2(lines: List<String>): Int {
//        println("Part2--------------------------------------------------")
        val engineSchematic = EngineSchematic.fromString(lines)
//        println(engineSchematic.toString())
//        println("-------------------------------------------------------")
        engineSchematic.markAllPartNumbers()
//        println(engineSchematic.toString())
//        println("-------------------------------------------------------")
        engineSchematic.markAllGearRatio()
//        println(engineSchematic.toString())
//        println("-------------------------------------------------------")
        return engineSchematic.addAllGearRatio()
    }
}

class EngineSchematic private constructor(
    private val elements: ArrayList<ArrayList<EngineSchematicElement>>,
    private val height: Int,
    private val width: Int
) {

    companion object {
        fun fromString(lines: List<String>): EngineSchematic {
            val height = lines.size
            val width = lines[0].length
            if (lines.any { it.length != width }) {
                throw IllegalArgumentException()
            }
            val elements = lines.map { line ->
                line.toCharArray().map { EngineSchematicElement.fromChar(it) }
            }
                .map { ArrayList(it) }
                .let { ArrayList(it) }

            return EngineSchematic(elements, height, width)
        }
    }

    private fun markPartNumberLeft(row: Int, column: Int, marker: (Digit) -> Unit, isMarked: (Digit) -> Boolean) {
        checkRowColumn(row, column)
        if (column <= 0) {
            //there is nothing on left side
            return
        }
        val left = elements[row][column - 1]
        if (left !is Digit) {
            return
        }
        if (isMarked(left)) {
            return
        }
        marker(left)
        markPartNumberLeft(row, column - 1, marker, isMarked)
    }

    private fun markPartNumberRight(row: Int, column: Int, marker: (Digit) -> Unit, isMarked: (Digit) -> Boolean) {
        checkRowColumn(row, column)
        if (column >= width - 1) {
            //there is nothing on right side
            return
        }
        val right = elements[row][column + 1]
        if (right !is Digit) {
            return
        }
        if (isMarked(right)) {
            return
        }
        marker(right)
        markPartNumberRight(row, column + 1, marker, isMarked)
    }

    private fun markPartNumber(row: Int, column: Int, marker: (Digit) -> Unit, isMarked: (Digit) -> Boolean) {
        checkRowColumn(row, column)
        val element = elements[row][column]
        if (element !is Digit) {
            return
        }
        if (isMarked(element)) {
            return
        }
        marker(element)
        markPartNumberLeft(row, column, marker, isMarked)
        markPartNumberRight(row, column, marker, isMarked)
    }

    private fun markSurrounding(row: Int, column: Int, marker: (Digit) -> Unit, isMarked: (Digit) -> Boolean) {
        checkRowColumn(row, column)
        if (row > 0) {
            markPartNumber(row - 1, column, marker, isMarked)
        }
        if (row < height - 1) {
            markPartNumber(row + 1, column, marker, isMarked)
        }
        if (column > 0) {
            markPartNumber(row, column - 1, marker, isMarked)
        }
        if (column < width - 1) {
            markPartNumber(row, column + 1, marker, isMarked)
        }
        if (row > 0 && column > 0) {
            markPartNumber(row - 1, column - 1, marker, isMarked)
        }
        if (row > 0 && column < width - 1) {
            markPartNumber(row - 1, column + 1, marker, isMarked)
        }
        if (row < height - 1 && column > 0) {
            markPartNumber(row + 1, column - 1, marker, isMarked)
        }
        if (row < height - 1 && column < width - 1) {
            markPartNumber(row + 1, column + 1, marker, isMarked)
        }
    }

    fun markAllPartNumbers() {
        for (row in 0..<height) {
            for (column in 0..<width) {
                val element = elements[row][column]
                if (element is SpecialChar) {
                    markSurrounding(row, column, Digit::markAsPartNumber, Digit::isPartNumber)
                }
            }
        }
    }

    private fun addAllNumber(selector: (Digit) -> Boolean): Int {
        var sum = 0
        var number = 0
        for (row in 0..<height) {
            for (column in 0..<width) {
                val element = elements[row][column]
                if (element is Digit && selector(element)) {
                    number = number * 10 + element.value
                } else {
                    sum += number
                    number = 0
                }
            }
            sum += number
            number = 0
        }
        return sum
    }

    fun addAllPartNumber(): Int {
        return addAllNumber(Digit::isPartNumber)
    }

    private fun countSurroundingNumbers(row: Int, column: Int): Int {
        checkRowColumn(row, column)
        var count = 0
        if (column > 0) {
            if (elements[row][column - 1] is Digit) {
                count++
            }
        }
        if (column < width - 1) {
            if (elements[row][column + 1] is Digit) {
                count++
            }
        }
        count += countNumbersTop(row, column)
        count += countNumbersBottom(row, column)
        return count
    }

    private fun countNumbersTop(row: Int, column: Int): Int {
        checkRowColumn(row, column)
        if (row <= 0) {
            return 0
        } else if (column <= 0) {
            return if (elements[row - 1][column] is Digit) {
                1
            } else if (elements[row - 1][column + 1] is Digit) {
                1
            } else {
                0
            }
        } else if (column >= width - 1) {
            return if (elements[row - 1][column] is Digit) {
                1
            } else if (elements[row - 1][column - 1] is Digit) {
                1
            } else {
                0
            }
        } else {
            return if (elements[row - 1][column] is Digit) {
                1
            } else {
                var count = 0
                if (elements[row - 1][column - 1] is Digit) {
                    count++
                }
                if (elements[row - 1][column + 1] is Digit) {
                    count++
                }
                count
            }
        }
    }

    private fun countNumbersBottom(row: Int, column: Int): Int {
        checkRowColumn(row, column)
        if (row >= height - 1) {
            return 0
        } else if (column <= 0) {
            return if (elements[row + 1][column] is Digit) {
                1
            } else if (elements[row + 1][column + 1] is Digit) {
                1
            } else {
                0
            }
        } else if (column >= width - 1) {
            return if (elements[row + 1][column] is Digit) {
                1
            } else if (elements[row + 1][column - 1] is Digit) {
                1
            } else {
                0
            }
        } else {
            return if (elements[row + 1][column] is Digit) {
                1
            } else {
                var count = 0
                if (elements[row + 1][column - 1] is Digit) {
                    count++
                }
                if (elements[row + 1][column + 1] is Digit) {
                    count++
                }
                count
            }
        }
    }

    fun markAllGearRatio() {
        for (row in 0..<height) {
            for (column in 0..<width) {
                val element = elements[row][column]
                if (element is SpecialChar && element.isStar()) {
                    if (countSurroundingNumbers(row, column) == 2) {
                        markSurrounding(row, column, Digit::markAsGearRatio, Digit::isGearRatio)
                    }
                }
            }
        }
    }

    fun multiplySurrounding(row: Int, column: Int): Int {
        val nums = ArrayList<Int>()
        if (column >= 0 && elements[row][column - 1] is Digit) {
            nums.add(getNumberAtPosition(row, column - 1))
        }
        if (column <= width - 1 && elements[row][column + 1] is Digit) {
            nums.add(getNumberAtPosition(row, column + 1))
        }
        if (row >= 0) {
            if (elements[row - 1][column] is Digit) {
                nums.add(getNumberAtPosition(row - 1, column))
            } else {
                if (elements[row - 1][column + 1] is Digit && column + 1 < width) {
                    nums.add(getNumberAtPosition(row - 1, column + 1))
                }
                if (column - 1 >= 0 && elements[row - 1][column - 1] is Digit) {
                    nums.add(getNumberAtPosition(row - 1, column - 1))
                }
            }
        }
        if (row <= height - 1) {
            if (elements[row + 1][column] is Digit) {
                nums.add(getNumberAtPosition(row + 1, column))
            } else {
                if (column + 1 < width && elements[row + 1][column + 1] is Digit) {
                    nums.add(getNumberAtPosition(row + 1, column + 1))
                }
                if (column - 1 >= 0 && elements[row + 1][column - 1] is Digit) {
                    nums.add(getNumberAtPosition(row + 1, column - 1))
                }
            }
        }
        return nums.reduce { a, b -> a * b }
    }

    private fun getNumberAtPosition(row: Int, column: Int): Int {
        checkRowColumn(row, column)
        if (elements[row][column] !is Digit) {
            throw IllegalArgumentException("Not a digit. Row: $row, Column: $column")
        }
        var j = column
        while (j >= 0 && elements[row][j] is Digit) {
            j--
        }
        j++
        var num = 0;
        while (j < width && elements[row][j] is Digit) {
            num = num * 10 + (elements[row][j] as Digit).value
            j++
        }
        return num
    }

    fun addAllGearRatio(): Int {
        var sum = 0
        for (row in 0..<height) {
            for (column in 0..<width) {
                val element = elements[row][column]
                if (element is SpecialChar && element.isStar()) {
                    if (countSurroundingNumbers(row, column) == 2) {
                        sum += multiplySurrounding(row, column)
                    }
                }
            }
        }
        return sum
    }

    private fun checkRowColumn(row: Int, column: Int) {
        if (row < 0 || row >= height) {
            throw IllegalArgumentException("Invalid row : $row")
        }
        if (column < 0 || column >= width) {
            throw IllegalArgumentException("Invalid column : $column")
        }
    }

    override fun toString(): String {
        return elements.joinToString(separator = "\n") { row ->
            row.joinToString(separator = "") { char ->
                when (char) {
                    is Digit -> {
                        char.value.toString() + (if (char.isGearRatio) "G" else if (char.isPartNumber) "P" else " ")
                    }

                    is SpecialChar -> {
                        "${char.char} "
                    }

                    is Empty -> {
                        ". "
                    }

                    else -> {
                        throw IllegalArgumentException()
                    }
                }
            }
        }
    }
}

interface EngineSchematicElement {
    companion object {
        fun fromChar(char: Char): EngineSchematicElement {
            return if (char.isDigit()) {
                Digit(char.digitToInt(), false)
            } else if (char == '.') {
                Empty()
            } else {
                SpecialChar(char)
            }
        }
    }
}

class Digit(
    val value: Int,
    isPartNumber: Boolean = false,
    isGearRatio: Boolean = false
) : EngineSchematicElement {

    var isPartNumber = isPartNumber
        private set

    var isGearRatio = isGearRatio
        private set

    fun markAsPartNumber() {
        isPartNumber = true
    }

    fun markAsGearRatio() {
        isGearRatio = true
    }
}

class Empty() : EngineSchematicElement

class SpecialChar(val char: Char) : EngineSchematicElement {
    fun isStar(): Boolean {
        return char == '*'
    }
}