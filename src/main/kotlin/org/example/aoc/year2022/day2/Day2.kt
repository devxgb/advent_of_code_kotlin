package org.example.aoc.year2022.day2

import org.example.aoc.year2022.day2.RPSGame.ResultPair.*
import org.example.aoc.year2022.day2.RPSGame.Turn.*
import org.example.common.readFile

fun main() {
    val rpsGame = RPSGame()
    val lines = readFile("\\year2022\\day2\\input.txt")

    /************* Part 1 *************/
    lines
        .map { it.split(" ") }
        .map { it[0] to it[1] }
        .map {
            when (it.first) {
                "A" -> ROCK
                "B" -> PAPER
                "C" -> SCISSORS
                else -> throw RuntimeException()
            } to when (it.second) {
                "X" -> ROCK
                "Y" -> PAPER
                "Z" -> SCISSORS
                else -> throw RuntimeException()
            }
        }
        //.onEach { println(it) }
        .fold(rpsGame) { ongoingGame: RPSGame, turns: Pair<RPSGame.Turn, RPSGame.Turn> ->
            ongoingGame.play(turns.first, turns.second)
        }
        .scorePlayer2
        .let { println(it) }
    rpsGame.reset()
    /************* Part 2 *************/
    lines
        .map { it.split(" ") }
        .map { it[0] to it[1] }
        .map {
            when (it.first) {
                "A" -> ROCK
                "B" -> PAPER
                "C" -> SCISSORS
                else -> throw RuntimeException()
            } to when (it.second) {
                "X" -> PLAYER1_WINS
                "Y" -> GAME_DRAW
                "Z" -> PLAYER2_WINS
                else -> throw RuntimeException()
            }
        }
        .fold(rpsGame) { ongoingGame: RPSGame, turns: Pair<RPSGame.Turn, RPSGame.ResultPair> ->
            ongoingGame.reverseLookUpPlay(turns.first, turns.second)
        }
        .scorePlayer2
        .let { println(it) }
    rpsGame.reset()
}

