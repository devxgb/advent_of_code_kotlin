package org.example.aoc.year2023.day7

import org.example.aoc.year2023.day7.Hand.Companion.sortedWithJokerOrder
import org.example.aoc.year2023.day7.HandType.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HandTest {

    private fun createHand(str: String): Hand {
        val cards = str.toCharArray().map { Card.fromChar(it) }
        return Hand(cards[0], cards[1], cards[2], cards[3], cards[4], 0)
    }

    @Test
    fun initTest() {
        assertEquals(FIVE_OF_A_KIND, createHand("AAAAA").handType)
        assertEquals(FOUR_OF_A_KIND, createHand("AA8AA").handType)
        assertEquals(FULL_HOUSE, createHand("23332").handType)
        assertEquals(THREE_OF_A_KIND, createHand("TTT98").handType)
        assertEquals(TWO_PAIR, createHand("23432").handType)
        assertEquals(ONE_PAIR, createHand("A23A4").handType)
        assertEquals(HIGH_CARD, createHand("23456").handType)

        assertEquals(ONE_PAIR, createHand("32T3K").handType)
        assertEquals(TWO_PAIR, createHand("KK677").handType)
        assertEquals(TWO_PAIR, createHand("KTJJT").handType)
        assertEquals(THREE_OF_A_KIND, createHand("T55J5").handType)
        assertEquals(THREE_OF_A_KIND, createHand("QQQJA").handType)

        assertEquals(ONE_PAIR, createHand("32T3K").handTypeJoker)
        assertEquals(TWO_PAIR, createHand("KK677").handTypeJoker)
        assertEquals(FOUR_OF_A_KIND, createHand("KTJJT").handTypeJoker)
        assertEquals(FOUR_OF_A_KIND, createHand("T55J5").handTypeJoker)
        assertEquals(FOUR_OF_A_KIND, createHand("QQQJA").handTypeJoker)
    }

    @Test
    fun comparisonTest() {
        assertTrue { createHand("33332") > createHand("2AAAA") }
        assertTrue { createHand("77888") > createHand("77788") }
    }

    @Test
    fun sortingTest() {
        assertEquals(
            listOf(
                createHand("32T3K"),
                createHand("KTJJT"),
                createHand("KK677"),
                createHand("T55J5"),
                createHand("QQQJA")
            ),
            listOf(
                createHand("32T3K"),
                createHand("T55J5"),
                createHand("KK677"),
                createHand("KTJJT"),
                createHand("QQQJA")
            ).sorted()
        )
    }

    @Test
    fun sortingJokerTest() {
        assertEquals(
            listOf(
                createHand("32T3K"),
                createHand("KK677"),
                createHand("T55J5"),
                createHand("QQQJA"),
                createHand("KTJJT")
            ),
            listOf(
                createHand("32T3K"),
                createHand("T55J5"),
                createHand("KK677"),
                createHand("KTJJT"),
                createHand("QQQJA")
            ).sortedWithJokerOrder()
        )
    }
}