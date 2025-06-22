package org.example.aoc.year2023.day07

import kotlin.Comparator
import org.example.aoc.year2023.day07.Hand.Companion.sortedWithJokerOrder
import org.example.aoc.year2023.day07.HandType.*
import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2023", "07")
    val day7 = Day7()
    println(day7.part1(lines))
    println(day7.part2(lines))
}

class Day7 {
    fun part1(lines: List<String>): Int {
        val hands = parse(lines)
        return hands.sorted().mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    fun part2(lines: List<String>): Any {
        val hands = parse(lines)
        return hands.sortedWithJokerOrder().mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    fun parse(lines: List<String>): List<Hand> {
        return lines.map { line ->
            val splitLine = line.split(" ")
            val cards = splitLine[0].toCharArray().map { Card.fromChar(it) }
            val bid = splitLine[1].toInt()
            Hand(cards[0], cards[1], cards[2], cards[3], cards[4], bid)
        }
    }
}

class Hand(val first: Card, val second: Card, val third: Card, val fourth: Card, val fifth: Card, val bid: Int) : Comparable<Hand> {

    val handType: HandType
    val handTypeJoker: HandType

    init {
        handType = calculateHandType()
        handTypeJoker = calculateHandTypeJoker()
    }

    private fun calculateHandType(): HandType {
        val cardGroupBy = listOf(first, second, third, fourth, fifth).groupBy { it }
        return when {
            cardGroupBy.size == 1 && cardGroupBy.all { it.value.size == 5 } -> FIVE_OF_A_KIND
            cardGroupBy.size == 2 && cardGroupBy.all { it.value.size == 1 || it.value.size == 4 } -> FOUR_OF_A_KIND
            cardGroupBy.size == 2 && cardGroupBy.all { it.value.size == 2 || it.value.size == 3 } -> FULL_HOUSE
            cardGroupBy.size == 3 && cardGroupBy.all { it.value.size == 3 || it.value.size == 1 } -> THREE_OF_A_KIND
            cardGroupBy.size == 3 && cardGroupBy.all { it.value.size == 2 || it.value.size == 1 } -> TWO_PAIR
            cardGroupBy.size == 4 && cardGroupBy.all { it.value.size == 2 || it.value.size == 1 } -> ONE_PAIR
            cardGroupBy.size == 5 && cardGroupBy.all { it.value.size == 1 } -> HIGH_CARD
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    private fun calculateHandTypeJoker(): HandType {
        val cardGroupBy = listOf(first, second, third, fourth, fifth).groupBy { it }
        val joker = cardGroupBy[Card.Card_J]?.size ?: 0
        return when {
            cardGroupBy.size == 1 && cardGroupBy.all { it.value.size == 5 } -> FIVE_OF_A_KIND
            cardGroupBy.size == 2 && cardGroupBy.all { it.value.size == 1 || it.value.size == 4 } -> {
                if (joker == 1 || joker == 4) FIVE_OF_A_KIND else if (joker == 0) FOUR_OF_A_KIND else throw IllegalArgumentException()
            }

            cardGroupBy.size == 2 && cardGroupBy.all { it.value.size == 2 || it.value.size == 3 } -> {
                if (joker == 2 || joker == 3) FIVE_OF_A_KIND else if (joker == 0) FULL_HOUSE else throw IllegalArgumentException()
            }

            cardGroupBy.size == 3 && cardGroupBy.all { it.value.size == 3 || it.value.size == 1 } -> {
                if (joker == 1 || joker == 3) FOUR_OF_A_KIND else if (joker == 0) THREE_OF_A_KIND else throw IllegalArgumentException()
            }

            cardGroupBy.size == 3 && cardGroupBy.all { it.value.size == 2 || it.value.size == 1 } -> {
                if (joker == 2) FOUR_OF_A_KIND else if (joker == 1) FULL_HOUSE else if (joker == 0) TWO_PAIR else throw IllegalArgumentException()
            }

            cardGroupBy.size == 4 && cardGroupBy.all { it.value.size == 2 || it.value.size == 1 } -> {
                if (joker == 2 || joker == 1) THREE_OF_A_KIND else if (joker == 0) ONE_PAIR else throw IllegalArgumentException()
            }

            cardGroupBy.size == 5 && cardGroupBy.all { it.value.size == 1 } -> {
                if (joker == 1) ONE_PAIR else if (joker == 0) HIGH_CARD else throw IllegalArgumentException()
            }

            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    companion object {
        private val comparator: Comparator<Hand> =
          Comparator.comparing { hand: Hand -> hand.handType }
            .thenComparing { hand: Hand -> hand.first }
            .thenComparing { hand: Hand -> hand.second }
            .thenComparing { hand: Hand -> hand.third }
            .thenComparing { hand: Hand -> hand.fourth }
            .thenComparing { hand: Hand -> hand.fifth }

        private val comparatorJoker: Comparator<Hand> =
          Comparator.comparing { hand: Hand -> hand.handTypeJoker }
            .thenComparing(Hand::first, Card.comparatorJoker)
            .thenComparing(Hand::second, Card.comparatorJoker)
            .thenComparing(Hand::third, Card.comparatorJoker)
            .thenComparing(Hand::fourth, Card.comparatorJoker)
            .thenComparing(Hand::fifth, Card.comparatorJoker)

        fun List<Hand>.sortedWithJokerOrder(): List<Hand> {
            return this.sortedWith(comparatorJoker)
        }
    }

    override fun compareTo(other: Hand): Int {
        return comparator.compare(this, other)
    }

    override fun toString(): String {
        return first.toString() + second.toString() + third.toString() + fourth.toString() + fifth.toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is Hand && comparator.compare(this, other) == 0
    }

    override fun hashCode(): Int {
        var result = first.hashCode()
        result = 31 * result + second.hashCode()
        result = 31 * result + third.hashCode()
        result = 31 * result + fourth.hashCode()
        result = 31 * result + fifth.hashCode()
        result = 31 * result + bid
        result = 31 * result + handType.hashCode()
        result = 31 * result + handTypeJoker.hashCode()
        return result
    }
}

enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
}

enum class Card {
    Card_2,
    Card_3,
    Card_4,
    Card_5,
    Card_6,
    Card_7,
    Card_8,
    Card_9,
    Card_T,
    Card_J,
    Card_Q,
    Card_K,
    Card_A;

    companion object {
        fun fromChar(char: Char): Card {
            return when (char) {
                '2' -> Card_2
                '3' -> Card_3
                '4' -> Card_4
                '5' -> Card_5
                '6' -> Card_6
                '7' -> Card_7
                '8' -> Card_8
                '9' -> Card_9
                'T' -> Card_T
                'J' -> Card_J
                'Q' -> Card_Q
                'K' -> Card_K
                'A' -> Card_A
                else -> throw IllegalArgumentException()
            }
        }

        val comparatorJoker: Comparator<Card> = Comparator { card1, card2 ->
            if (card1 == Card_J && card2 == Card_J) {
                0
            } else if (card1 == Card_J) {
                -1
            } else if (card2 == Card_J) {
                1
            } else {
                card1.compareTo(card2)
            }
        }
    }

    override fun toString(): String {
        return this.name.toCharArray()[5].toString()
    }
}
