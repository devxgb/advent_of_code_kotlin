package org.example.aoc.year2025.day01

import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2025", "01")
    val year2025Day01 = Year2025Day01()
    println(year2025Day01.part1(lines))
    println(year2025Day01.part2(lines))
}

class Year2025Day01 {
    fun part1(lines: List<String>): Int {
        val dial = Dial(start = 50)
        var zeroCount = 0
        for (line in lines) {
            val direction = line[0]
            val turn = line.substring(1).toInt()
            when (direction) {
                'R' -> {
                    dial.turnRight(turn)
                }
                'L' -> {
                    dial.turnLeft(turn)
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
            if (dial.curr == 0) {
                zeroCount++
            }
        }
        return zeroCount
    }

    fun part2(lines: List<String>): Int {
        val dial = Dial(start = 50)
        for (line in lines) {
            val direction = line[0]
            val turn = line.substring(1).toInt()
            when (direction) {
                'R' -> {
                    dial.turnRight(turn)
                }
                'L' -> {
                    dial.turnLeft(turn)
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
        return dial.zeroClick
    }

    private class Dial(val start: Int) {
        var curr: Int = start
            private set

        var zeroClick: Int = 0
            private set

        tailrec fun turnRight(turn: Int) {
            if (curr + turn <= 99) {
                curr += turn
                return
            }
            val prevCurr = curr
            curr = 0
            zeroClick++
            turnRight(turn - (99 - prevCurr) - 1)
        }

        tailrec fun turnLeft(turn: Int) {
            if (curr - turn >= 0) {
                curr -= turn
                if (curr == 0) {
                    zeroClick++
                }
                return
            }
            val prevCurr = curr
            curr = 99
            if (prevCurr != 0) {
                zeroClick++
            }
            turnLeft(turn - prevCurr - 1)
        }
    }
}
