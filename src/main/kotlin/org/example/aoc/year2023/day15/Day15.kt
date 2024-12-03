package org.example.aoc.year2023.day15

import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2023\\day15\\input.txt")
    val day15 = Day15()
    println(day15.part1(lines))
    println(day15.part2(lines))
}

class Day15 {
    fun part1(lines: List<String>): Int {
        val input = parse(lines)
        val result = input.sumOf { hash(it) }
        return result
    }

    fun part2(lines: List<String>): Int {
        val input = parse(lines)
        val myHashMap = MyHashMap(hash = { key -> hash(key) })
        input.forEach { myHashMap.execute(it) }
        val result = myHashMap.calculateResult()
        return result
    }

    fun parse(lines: List<String>): List<String> {
        require(lines.size == 1)
        return lines[0].split(',')
    }

    private fun hash(string: String): Int {
        return string.fold(initial = 0) { acc, element -> ((acc + element.code) * 17).mod(256) }
    }

    private class MyHashMap(private val hash: (String) -> Int) {
        companion object {
            private data class MyEntry(var key: String, var value: Int)
        }

        private val table: Array<ArrayList<MyEntry>> = Array(size = 256) { arrayListOf() }

        fun remove(key: String) {
            val entryList = table[hash(key)]
            val index = entryList.indexOfFirst { it.key == key }
            if (index != -1) {
                entryList.removeAt(index)
            }
        }

        fun put(key: String, value: Int) {
            val entryList = table[hash(key)]
            val index = entryList.indexOfFirst { it.key == key }
            if (index == -1) {
                entryList.add(MyEntry(key, value))
            } else {
                entryList[index].value = value
            }
        }

        fun execute(instruction: String) {
            if (instruction.contains('-')) {
                require(instruction.length == instruction.indexOf('-') + 1)
                remove(instruction.dropLast(1))
            } else if (instruction.contains('=')) {
                require(instruction.length == instruction.indexOf('=') + 2)
                put(instruction.dropLast(2), instruction.last().digitToInt())
            } else {
                require(false)
            }
        }

        fun calculateResult(): Int {
            return table
              .map { entryList -> entryList.withIndex().sumOf { (index, entry) -> (index + 1) * entry.value } }
              .withIndex()
              .sumOf { (index, value) -> (index + 1) * value }
        }
    }
}
