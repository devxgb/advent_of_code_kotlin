package org.example.aoc.year2023.day01

import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2023\\day01\\input.txt")

    // Part1
    val result1 =
      lines
        .asSequence()
        .map { line -> line.toCharArray().filter { it.isDigit() } }
        .map { it.first() to it.last() }
        .map { (first, last) -> first.digitToInt().times(10).plus(last.digitToInt()) }
        .sum()

    println(result1)

    // Part2
    val result2 =
      lines
        .asSequence()
        .map { line -> line.convertFirstDigit() to line.convertLastDigit() }
        .map { (lineForFirstDigit, lineForLastDigit) -> lineForFirstDigit.toCharArray().first { it.isDigit() } to lineForLastDigit.toCharArray().last { it.isDigit() } }
        .map { (first, last) -> first.digitToInt().times(10).plus(last.digitToInt()) }
        .sum()

    println(result2)
}

val wordDigitMapping = mapOf("one" to "1", "two" to "2", "three" to "3", "four" to "4", "five" to "5", "six" to "6", "seven" to "7", "eight" to "8", "nine" to "9")

val digitWords = wordDigitMapping.keys

fun wordToDigit(word: String): String = wordDigitMapping[word] ?: throw RuntimeException("Invalid word")

fun String.replaceLast(oldValue: String, newValue: String): String {
    val lastIndex = lastIndexOf(oldValue)
    if (lastIndex == -1) {
        return this
    }
    val prefix = substring(0, lastIndex)
    val suffix = substring(lastIndex + oldValue.length)
    return "$prefix$newValue$suffix"
}

fun String.convertFirstDigit(): String =
  digitWords
    .map { it to (it.toRegex().find(this)?.range?.first ?: Int.MAX_VALUE) }
    .minWithOrNull(Comparator.comparing { (_, occurrence) -> occurrence })
    ?.first
    ?.let { this.replaceFirst(it, wordToDigit(it)) } ?: this

fun String.convertLastDigit(): String =
  digitWords
    .map { it to (it.toRegex().findAll(this).lastOrNull()?.range?.last ?: Int.MIN_VALUE) }
    .maxWithOrNull(Comparator.comparing { (_, occurrence) -> occurrence })
    ?.first
    ?.let { this.replaceLast(it, wordToDigit(it)) } ?: this
