package org.example.aoc.year2023.day2

import org.example.common.readFile
import kotlin.math.max
import kotlin.math.min

fun main() {

    val lines = readFile("\\year2023\\day2\\input.txt")
    val day2 = Day2()
    println(day2.part1(lines))
    println(day2.part2(lines))
}

class Day2 {
    fun part1(lines: List<String>): Int {
        val totalRed = 12
        val totalGreen = 13
        val totalBlue = 14
        return lines.map { line -> Game.fromString(line) }
            .filter { game -> game.isPossible(totalRed, totalGreen, totalBlue) }
            .sumOf { it.id }
    }
    fun part2(lines: List<String>): Int {
        return lines.map { line -> Game.fromString(line) }
            .map {game ->
                game.turns.fold(Turn(red = Int.MIN_VALUE, green = Int.MIN_VALUE, blue = Int.MIN_VALUE)) { turnMax, turn ->
                    Turn(red = max(turnMax.red, turn.red), green = max(turnMax.green, turn.green), blue = max(turnMax.blue, turn.blue))
                }
            }
            .sumOf { turn ->
                turn.red * turn.green * turn.blue
            }
    }


}

class Game(
    val id: Int,
    val turns: List<Turn>
) {
    companion object {
        fun fromString(str: String): Game {
            val idTurns = str.split(":").map { it.trim() }
            if (idTurns.size != 2) {
                throw RuntimeException("invalid input")
            }
            val id = idTurns[0].substring(5).toInt()
            val turns = idTurns[1].split(";").map { it.trim() }
            val parsedTurns = turns.map { turn ->
                val cubes = turn.split(",")
                if (cubes.size > 3) {
                    throw RuntimeException("invalid input")
                }
                cubes.map { it.trim() }
                    .associate { cube ->
                        val numColor = cube.split(" ")
                        if (numColor.size != 2) {
                            throw RuntimeException("invalid input")
                        }
                        numColor[1] to numColor[0].toInt()
                    }
            }
                .map { turn ->
                    Turn(turn["red"] ?: 0, turn["green"] ?: 0, turn["blue"] ?: 0)
                }

            return Game(id, parsedTurns)
        }
    }

    fun isPossible(totalRed: Int, totalGreen: Int, totalBlue: Int): Boolean {
        return turns.all { it.isPossible(totalRed, totalGreen, totalBlue) }
    }
}

class Turn(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0
) {
    fun isPossible(totalRed: Int, totalGreen: Int, totalBlue: Int): Boolean {
        return red <= totalRed && green <= totalGreen && blue <= totalBlue
    }
}
