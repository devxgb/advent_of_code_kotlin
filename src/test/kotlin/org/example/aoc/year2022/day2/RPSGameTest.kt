package org.example.aoc.year2022.day2

import kotlin.test.assertEquals
import org.example.aoc.year2022.day2.RPSGame.ResultPair.*
import org.example.aoc.year2022.day2.RPSGame.Turn.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class RPSGameTest {

    private val rpsGame = RPSGame()

    @AfterEach
    fun afterEach() {
        rpsGame.reset()
    }

    private fun assertGame(turnPlayer1: RPSGame.Turn, turnPlayer2: RPSGame.Turn, expectedScorePlayer1: Int, expectedScorePlayer2: Int) {
        rpsGame.play(turnPlayer1, turnPlayer2)
        assertEquals(expectedScorePlayer1, rpsGame.scorePlayer1)
        assertEquals(expectedScorePlayer2, rpsGame.scorePlayer2)
    }

    private fun assertGameReverseLookup(turnPlayer1: RPSGame.Turn, result: RPSGame.ResultPair, expectedScorePlayer1: Int, expectedScorePlayer2: Int) {
        rpsGame.reverseLookUpPlay(turnPlayer1, result)
        assertEquals(expectedScorePlayer1, rpsGame.scorePlayer1)
        assertEquals(expectedScorePlayer2, rpsGame.scorePlayer2)
    }

    /** ******** Test Play ************** */
    @Test
    fun test1() {
        assertGame(ROCK, PAPER, 1, 8)
    }

    @Test
    fun test2() {
        assertGame(PAPER, ROCK, 8, 1)
    }

    @Test
    fun test3() {
        assertGame(SCISSORS, SCISSORS, 6, 6)
    }

    /** ******** Test reverseLookUpPlay ************** */
    @Test
    fun test4() {
        assertGameReverseLookup(ROCK, GAME_DRAW, 4, 4)
    }

    @Test
    fun test5() {
        assertGameReverseLookup(PAPER, PLAYER1_WINS, 8, 1)
    }

    @Test
    fun test6() {
        assertGameReverseLookup(SCISSORS, PLAYER2_WINS, 3, 7)
    }
}
