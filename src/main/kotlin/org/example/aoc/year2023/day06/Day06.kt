package org.example.aoc.year2023.day06

import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2023", "06")
    val day6 = Day6()
    println(day6.part1(lines))
    println(day6.part2(lines))
}

class Day6 {
    fun part1(lines: List<String>): Int =
      parsePart1(lines).map { (raceTime, recordDistance) -> calculateNumberOfWin(raceTime.toLong(), recordDistance.toLong()) }.reduce(Int::times)

    fun part2(lines: List<String>): Int {
        val (raceTime, recordDistance) = parsePart2(lines)
        return calculateNumberOfWin(raceTime, recordDistance)
    }

    fun parsePart1(lines: List<String>): List<Pair<Int, Int>> {
        require(lines.size == 2)
        val raceTimes = lines[0].split(":")[1].trim().split(" +".toRegex()).map { it.toInt() }
        val recordDistances = lines[1].split(":")[1].trim().split(" +".toRegex()).map { it.toInt() }
        require(raceTimes.size == recordDistances.size)
        return IntRange(0, raceTimes.size - 1).map { raceTimes[it] to recordDistances[it] }
    }

    fun parsePart2(lines: List<String>): Pair<Long, Long> {
        require(lines.size == 2)
        val time = lines[0].split(":")[1].trim().replace(" ", "").toLong()
        val recordDistance = lines[1].split(":")[1].trim().replace(" ", "").toLong()
        return time to recordDistance
    }

    fun calculateDistanceTravelled(raceTime: Long, buttonPressTime: Long): Long {
        require(buttonPressTime in 1..<raceTime)
        val speed = buttonPressTime
        val timeToTravel = raceTime - buttonPressTime
        return speed * timeToTravel
    }

    fun calculateNumberOfWin(raceTime: Long, recordDistance: Long): Int =
      LongRange(1, raceTime - 1L).map { buttonPressTime -> calculateDistanceTravelled(raceTime, buttonPressTime) }.count { distance -> distance > recordDistance }
}
