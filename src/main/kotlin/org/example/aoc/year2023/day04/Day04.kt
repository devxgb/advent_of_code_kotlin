package org.example.aoc.year2023.day04

import org.example.common.readFile
import kotlin.math.pow

fun main() {
    val lines = readFile("\\year2023\\day04\\input.txt")
    val day4 = Day4()
    println(day4.part1(lines))
    println(day4.part2(lines))
}

class Day4 {
    fun part1(lines: List<String>): Int {
        val cards = parse(lines)
        return cards.sumOf { it.getPoint() }
    }

    fun part2(lines: List<String>): Int {
        val cards = parse(lines)
        require(cards.mapIndexed { i, card -> card.matching<=cards.size-i-1 }.all { it })

        return cards.foldIndexed(List(cards.size) { 1 }) { index: Int, numberOfCardList: List<Int>, currentCard: Card ->
            val numberOfCurrentCard = numberOfCardList[index]
            numberOfCardList.mapIndexed { i: Int, numberOfCard: Int ->
                if(i in index+1..index+currentCard.matching) {
                    numberOfCard+numberOfCurrentCard
                } else {
                    numberOfCard
                }
            }
        }
            .sum()
    }

    private fun parse(lines: List<String>): List<Card> {
        val cards = lines.map { line ->
            val splitLine = line.split(":", "|").map { it.trim() }
            require(splitLine.size == 3) { "size = "+splitLine.size }
            val space = " +".toRegex()
            require(splitLine[0].split(space).size == 2)
            Card(
                id = splitLine[0].split(space)[1].toInt(),
                winningNumber = splitLine[1].split(space).map { it.toInt() }.toSet(),
                number = splitLine[2].split(space).map { it.toInt() }.toSet()
            )
        }
        require(cards.size > 1)
        require(cards.mapIndexed{ i, card->i+1==card.id}.all { it })
        require(cards.all { it.winningNumber.size == cards[0].winningNumber.size })
        require(cards.all { it.number.size == cards[0].number.size })
        return cards
    }
}

data class Card(
    val id: Int,
    val winningNumber: Set<Int>,
    val number: Set<Int>
) {

    val matching : Int = number.intersect(winningNumber).size

    fun getPoint(): Int {
        return if(matching==0) 0 else (2.0).pow(matching - 1).toInt()
    }
}