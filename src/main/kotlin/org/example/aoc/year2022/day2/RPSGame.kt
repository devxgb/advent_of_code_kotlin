package org.example.aoc.year2022.day2

import org.example.aoc.year2022.day2.RPSGame.ResultPair.*
import org.example.aoc.year2022.day2.RPSGame.Turn.*

class RPSGame {
    enum class Turn(val value: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3),
    }

    enum class Result(val value: Int) {
        WIN(6),
        LOSS(0),
        DRAW(3),
    }

    enum class ResultPair(val resultPlayer1: Result, val resultPlayer2: Result) {
        PLAYER1_WINS(Result.WIN, Result.LOSS),
        PLAYER2_WINS(Result.LOSS, Result.WIN),
        GAME_DRAW(Result.DRAW, Result.DRAW),
    }

    var scorePlayer1 = 0
        private set

    var scorePlayer2 = 0
        private set

    fun play(turnPlayer1: Turn, turnPlayer2: Turn): RPSGame {
        val resultPair = RULE_TABLE[turnPlayer1]?.get(turnPlayer2) ?: throw RuntimeException()
        scorePlayer1 += turnPlayer1.value + resultPair.resultPlayer1.value
        scorePlayer2 += turnPlayer2.value + resultPair.resultPlayer2.value
        return this
    }

    fun reverseLookUpPlay(turnPlayer1: Turn, result: ResultPair): RPSGame {
        val turnPlayer2 = REVERSE_LOOKUP_RULE_TABLE[turnPlayer1]?.get(result) ?: throw RuntimeException()
        scorePlayer1 += turnPlayer1.value + result.resultPlayer1.value
        scorePlayer2 += turnPlayer2.value + result.resultPlayer2.value
        return this
    }

    fun reset(): RPSGame {
        scorePlayer1 = 0
        scorePlayer2 = 0
        return this
    }

    companion object {
        private val RULE_TABLE =
          hashMapOf(
            ROCK to hashMapOf(ROCK to GAME_DRAW, PAPER to PLAYER2_WINS, SCISSORS to PLAYER1_WINS),
            PAPER to hashMapOf(ROCK to PLAYER1_WINS, PAPER to GAME_DRAW, SCISSORS to PLAYER2_WINS),
            SCISSORS to hashMapOf(ROCK to PLAYER2_WINS, PAPER to PLAYER1_WINS, SCISSORS to GAME_DRAW),
          )

        private val REVERSE_LOOKUP_RULE_TABLE =
          hashMapOf(
            ROCK to hashMapOf(GAME_DRAW to ROCK, PLAYER2_WINS to PAPER, PLAYER1_WINS to SCISSORS),
            PAPER to hashMapOf(PLAYER1_WINS to ROCK, GAME_DRAW to PAPER, PLAYER2_WINS to SCISSORS),
            SCISSORS to hashMapOf(PLAYER2_WINS to ROCK, PLAYER1_WINS to PAPER, GAME_DRAW to SCISSORS),
          )
    }
}
