package org.example.aoc.year2025.day03

import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2025", "03")
    val year2025Day03 = Year2025Day03()
    println(year2025Day03.part1(lines))
    println(year2025Day03.part2(lines))
}

class Year2025Day03 {
    fun part1(lines: List<String>): Int {
        val result =
          lines.sumOf { batteryBank ->
              val firstMaxIndex = findSubArrayMaxIndex(batteryBank, 0, batteryBank.lastIndex - 1)
              require(firstMaxIndex >= 0)
              require(firstMaxIndex < batteryBank.lastIndex)
              val secondMaxIndex = findSubArrayMaxIndex(batteryBank, firstMaxIndex + 1, batteryBank.lastIndex)
              batteryBank[firstMaxIndex].digitToInt() * 10 + batteryBank[secondMaxIndex].digitToInt()
          }
        return result
    }

    fun part2(lines: List<String>): Long {
        val result: Long =
          lines.sumOf { batteryBank ->
              val maxIndex1 = findSubArrayMaxIndex(batteryBank, 0, batteryBank.lastIndex - 11)
              val maxIndex2 = findSubArrayMaxIndex(batteryBank, maxIndex1 + 1, batteryBank.lastIndex - 10)
              val maxIndex3 = findSubArrayMaxIndex(batteryBank, maxIndex2 + 1, batteryBank.lastIndex - 9)
              val maxIndex4 = findSubArrayMaxIndex(batteryBank, maxIndex3 + 1, batteryBank.lastIndex - 8)
              val maxIndex5 = findSubArrayMaxIndex(batteryBank, maxIndex4 + 1, batteryBank.lastIndex - 7)
              val maxIndex6 = findSubArrayMaxIndex(batteryBank, maxIndex5 + 1, batteryBank.lastIndex - 6)
              val maxIndex7 = findSubArrayMaxIndex(batteryBank, maxIndex6 + 1, batteryBank.lastIndex - 5)
              val maxIndex8 = findSubArrayMaxIndex(batteryBank, maxIndex7 + 1, batteryBank.lastIndex - 4)
              val maxIndex9 = findSubArrayMaxIndex(batteryBank, maxIndex8 + 1, batteryBank.lastIndex - 3)
              val maxIndex10 = findSubArrayMaxIndex(batteryBank, maxIndex9 + 1, batteryBank.lastIndex - 2)
              val maxIndex11 = findSubArrayMaxIndex(batteryBank, maxIndex10 + 1, batteryBank.lastIndex - 1)
              val maxIndex12 = findSubArrayMaxIndex(batteryBank, maxIndex11 + 1, batteryBank.lastIndex)

              (batteryBank[maxIndex1].digitToInt().toLong() * 100000000000 +
                batteryBank[maxIndex2].digitToInt().toLong() * 10000000000 +
                batteryBank[maxIndex3].digitToInt().toLong() * 1000000000 +
                batteryBank[maxIndex4].digitToInt().toLong() * 100000000 +
                batteryBank[maxIndex5].digitToInt().toLong() * 10000000 +
                batteryBank[maxIndex6].digitToInt().toLong() * 1000000 +
                batteryBank[maxIndex7].digitToInt().toLong() * 100000 +
                batteryBank[maxIndex8].digitToInt().toLong() * 10000 +
                batteryBank[maxIndex9].digitToInt().toLong() * 1000 +
                batteryBank[maxIndex10].digitToInt().toLong() * 100 +
                batteryBank[maxIndex11].digitToInt().toLong() * 10 +
                batteryBank[maxIndex12].digitToInt().toLong())
          }
        return result
    }

    fun findSubArrayMaxIndex(str: String, startIndex: Int, endIndex: Int): Int {
        var maxIndex = -1
        var max = 0
        for (i in startIndex..endIndex) {
            val digit = str[i].digitToInt()
            if (digit > max) {
                maxIndex = i
                max = digit
            }
        }
        require(maxIndex >= startIndex)
        require(maxIndex <= endIndex)
        require(max > 0)
        return maxIndex
    }
}
