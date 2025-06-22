package org.example.aoc.year2023.day12

import java.util.Comparator.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.measureTimedValue
import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2023", "12")
    val day12 = Day12()
    println(day12.part1(lines))
    println(day12.part2(lines))
}

class Day12 {

    private val cacheV4: Cache = Cache()

    fun part1(lines: List<String>): Long {
        val inputList = parse(lines)
        val result = printTimedValue {
            inputList.sumOf { (records, contiguousActiveGroup) ->
                //                println(visualize(records,contiguousActiveGroup))
                //                println("Cache size: ${cacheV4.size()}")
                printTimedValue(enabled = false) { findNumberOfPossibleArrangement(records, contiguousActiveGroup, cacheV4) }
            }
        }
        return result
    }

    fun part2(lines: List<String>): Long {
        val inputList = parse(lines)
        val modifiedInputList =
          inputList.map { (records, contiguousActiveGroups) ->
              (records + arrayOf(null) + records + arrayOf(null) + records + arrayOf(null) + records + arrayOf(null) + records) to
                (contiguousActiveGroups + contiguousActiveGroups + contiguousActiveGroups + contiguousActiveGroups + contiguousActiveGroups)
          }

        val comparatorRecordSize = comparingInt<Pair<List<Boolean?>, List<Int>>> { (records, _) -> records.size }
        val comparatorGroupSize = comparingInt<Pair<List<Boolean?>, List<Int>>> { (_, groups) -> groups.size }
        val comparatorUnknown = comparingInt<Pair<List<Boolean?>, List<Int>>> { (records, _) -> records.count { it == null } }

        val comparator = comparatorUnknown.then(comparatorRecordSize).then(comparatorGroupSize)

        //        cacheV4.enableVerbose()

        val result = printTimedValue {
            modifiedInputList.sortedWith(comparator).sumOf { (records, contiguousActiveGroup) ->
                //                println(visualize(records,contiguousActiveGroup))
                //                println("Cache size: ${cacheV4.size()}")
                printTimedValue(enabled = false) { findNumberOfPossibleArrangement(records, contiguousActiveGroup, cacheV4) }
            }
        }
        return result
    }

    companion object {
        private fun parse(lines: List<String>): List<Pair<List<Boolean?>, List<Int>>> =
          lines.map { line ->
              val splitLine = line.split(" ")
              require(splitLine.size == 2)
              splitLine[0].map {
                  when (it) {
                      '#' -> true
                      '.' -> false
                      '?' -> null
                      else -> throw IllegalArgumentException()
                  }
              } to splitLine[1].split(",").map { it.toInt() }
          }

        private fun findNumberOfPossibleArrangement(records: List<Boolean?>, contiguousActiveGroups: List<Int>, cache: Cache): Long {
            return cache.getOrPut(records to contiguousActiveGroups) {
                if (contiguousActiveGroups.isEmpty()) {
                    if (records.none { it == true }) {
                        1
                    } else {
                        0
                    }
                } else if (records.isEmpty()) {
                    0
                } else if (records.size < (contiguousActiveGroups.sum() + contiguousActiveGroups.size - 1)) {
                    0
                } else {
                    if (records.first() == false) {
                        findNumberOfPossibleArrangement(records.drop(1), contiguousActiveGroups, cache)
                    } else if (records.first() == true) {
                        val firstGroup = contiguousActiveGroups.first()
                        if (records.size < firstGroup) {
                            0
                        } else if (records.size == firstGroup) {
                            if (records.none { it == false } && contiguousActiveGroups.size == 1) {
                                1
                            } else {
                                0
                            }
                        } else {
                            if (records.take(firstGroup).none { it == false } && records[firstGroup] != true) {
                                findNumberOfPossibleArrangement(records.drop(firstGroup + 1), contiguousActiveGroups.drop(1), cache)
                            } else {
                                0
                            }
                        }
                    } else {
                        findNumberOfPossibleArrangement(records.drop(1), contiguousActiveGroups, cache) +
                          findNumberOfPossibleArrangement(listOf(true) + records.drop(1), contiguousActiveGroups, cache)
                    }
                }
            }
        }

        private class Cache(private var verbose: Boolean = false) {
            companion object {
                private class Key(pair: Pair<List<Boolean?>, List<Int>>) {
                    private val first: List<Boolean?> = pair.first
                    private val second: List<Int> = pair.second
                    private val hasCode: Int

                    init {
                        var result = first.toTypedArray().contentHashCode()
                        result = 31 * result + second.toTypedArray().contentHashCode()
                        hasCode = result
                    }

                    override fun equals(other: Any?): Boolean {
                        if (this === other) return true
                        if (javaClass != other?.javaClass) return false

                        other as Key

                        if (first != other.first) return false
                        if (second != other.second) return false

                        return true
                    }

                    override fun hashCode(): Int = hasCode
                }
            }

            private val map: ConcurrentHashMap<Key, Long> = ConcurrentHashMap()

            private var hit: Int = 0
            private var miss: Int = 0

            private fun contains(key: Pair<List<Boolean?>, List<Int>>): Boolean {
                val contains = map.containsKey(Key(key))
                if (contains) {
                    hit++
                } else {
                    miss++
                }
                if (verbose) {
                    println("Hit: $hit, Miss: $miss")
                    if (!contains) {
                        println("Cache miss: Requested key : ${visualize(key.first, key.second)}")
                    }
                }
                return contains
            }

            fun put(key: Pair<List<Boolean?>, List<Int>>, value: Long) = map.put(Key(key), value)

            fun enableVerbose() {
                verbose = true
            }

            fun disableVerbose() {
                verbose = false
            }

            fun size() = map.mappingCount()

            fun getOrPut(key: Pair<List<Boolean?>, List<Int>>, defaultValue: () -> Long): Long {
                contains(key)
                return map.getOrPut(Key(key), defaultValue)
            }
        }

        private fun visualize(records: List<Boolean?>, contiguousActiveGroups: List<Int>) = "${visualize(records)}, Contiguous groups: $contiguousActiveGroups"

        private fun visualize(list: List<Boolean?>): String {
            return "Records: " +
              list
                .map {
                    when (it) {
                        null -> {
                            '?'
                        }

                        true -> {
                            '#'
                        }

                        else -> {
                            '.'
                        }
                    }
                }
                .joinToString(separator = "")
        }

        fun <T> printTimedValue(enabled: Boolean = true, block: () -> T): T {
            return if (enabled) {
                val (value, duration) = measureTimedValue(block)
                println("Time taken: $duration")
                value
            } else {
                block()
            }
        }
    }
}
