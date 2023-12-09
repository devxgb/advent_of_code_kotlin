package org.example.aoc.year2023.day5

import org.example.aoc.year2023.day5.LongRange.Companion.sortAndMerge
import org.example.aoc.year2023.day5.RangedMapping.Companion.map
import org.example.aoc.year2023.day5.RangedMapping.Companion.mapOrSelf
import org.example.common.readFile

fun main() {
    val lines = readFile("\\year2023\\day5\\input.txt")
    val day5 = Day5()
    println(day5.part1(lines))
    println(day5.part2(lines))
}

class Day5 {

    private val str_seed_to_soil = "seed-to-soil map:"
    private val str_soil_to_fertilizer = "soil-to-fertilizer map:"
    private val str_fertilizer_to_water = "fertilizer-to-water map:"
    private val str_water_to_light = "water-to-light map:"
    private val str_light_to_temperature = "light-to-temperature map:"
    private val str_temperature_to_humidity = "temperature-to-humidity map:"
    private val str_humidity_to_location = "humidity-to-location map:"

    fun part1(lines: List<String>): Long {
        val seeds = parseSeeds(lines)
        val seedToSoil = parseSeedToSoil(lines)
        val soilToFertilizer = parseSoilToFertilizer(lines)
        val fertilizerToWater = parseFertilizerToWater(lines)
        val waterToLight = parseWaterToLight(lines)
        val lightToTemperature = parseLightToTemperature(lines)
        val temperatureToHumidity = parseTemperatureToHumidity(lines)
        val humidityToLocation = parseHumidityToLocation(lines)

        return seeds.minOfOrNull { seed ->
            mapSeedToLocation(
                seed = seed,
                seedToSoil = seedToSoil,
                soilToFertilizer = soilToFertilizer,
                fertilizerToWater = fertilizerToWater,
                waterToLight = waterToLight,
                lightToTemperature = lightToTemperature,
                temperatureToHumidity = temperatureToHumidity,
                humidityToLocation = humidityToLocation
            )
        }
            ?: throw IllegalArgumentException()
    }

    fun part2(lines: List<String>): Long {
        val seedRanges = parseSeedAsRange(lines)
        val seedToSoil = parseSeedToSoil(lines)
        val soilToFertilizer = parseSoilToFertilizer(lines)
        val fertilizerToWater = parseFertilizerToWater(lines)
        val waterToLight = parseWaterToLight(lines)
        val lightToTemperature = parseLightToTemperature(lines)
        val temperatureToHumidity = parseTemperatureToHumidity(lines)
        val humidityToLocation = parseHumidityToLocation(lines)


        return seedRanges.sortAndMerge()
            .let { seedToSoil.map(it) }
            .let { soilToFertilizer.map(it) }
            .let { fertilizerToWater.map(it) }
            .let { waterToLight.map(it) }
            .let { lightToTemperature.map(it) }
            .let { temperatureToHumidity.map(it) }
            .let { humidityToLocation.map(it) }
            .minOrNull()
            ?.start
            ?:throw IllegalArgumentException()
    }

    fun parseSeeds(lines: List<String>): List<Long> {
        return lines[0].split(":")[1].trim().split(" ").map { it.toLong() }
    }

    fun parseSeedAsRange(lines: List<String>): List<LongRange> {
        val splitLines = lines[0].split(":")[1].trim().split(" ").map { it.toLong() }
        require(splitLines.size.mod(2) == 0)
        return splitLines.withIndex()
            .groupBy { it.index / 2 }
            .values
            .map { LongRange(it[0].value, it[1].value) }
    }

    fun parseSeedToSoil(lines: List<String>): List<RangedMapping> {
        return parseMapping(lines, str_seed_to_soil, str_soil_to_fertilizer)
    }

    fun parseSoilToFertilizer(lines: List<String>): List<RangedMapping> {
        return parseMapping(lines, str_soil_to_fertilizer, str_fertilizer_to_water)
    }

    fun parseFertilizerToWater(lines: List<String>): List<RangedMapping> {
        return parseMapping(lines, str_fertilizer_to_water, str_water_to_light)
    }

    fun parseWaterToLight(lines: List<String>): List<RangedMapping> {
        return parseMapping(lines, str_water_to_light, str_light_to_temperature)
    }

    fun parseLightToTemperature(lines: List<String>): List<RangedMapping> {
        return parseMapping(lines, str_light_to_temperature, str_temperature_to_humidity)
    }

    fun parseTemperatureToHumidity(lines: List<String>): List<RangedMapping> {
        return parseMapping(lines, str_temperature_to_humidity, str_humidity_to_location)
    }

    fun parseHumidityToLocation(lines: List<String>): List<RangedMapping> {
        return parseMapping(lines, str_humidity_to_location)
    }


    private fun parseMapping(
        lines: List<String>,
        firstString: String,
        lastString: String? = null
    ): List<RangedMapping> {
        val fromIndex = lines.indexOfFirst { it == firstString } + 1
        val toIndex = if (lastString == null) lines.size else lines.indexOfFirst { it == lastString } - 1
        require(fromIndex in 0..toIndex)
        require(toIndex in fromIndex + 1..lines.size)

        return lines.subList(fromIndex, toIndex)
            .map { line ->
                val split = line.split(" ").map { it.toLong() }
                require(split.size == 3)
                RangedMapping(destination = split[0], source = split[1], size = split[2])
            }
    }

    fun mapSeedToLocation(
        seed: Long,
        seedToSoil: List<RangedMapping>,
        soilToFertilizer: List<RangedMapping>,
        fertilizerToWater: List<RangedMapping>,
        waterToLight: List<RangedMapping>,
        lightToTemperature: List<RangedMapping>,
        temperatureToHumidity: List<RangedMapping>,
        humidityToLocation: List<RangedMapping>
    ): Long {
        return seed
            .let { seedToSoil.mapOrSelf(it) }
            .let { soilToFertilizer.mapOrSelf(it) }
            .let { fertilizerToWater.mapOrSelf(it) }
            .let { waterToLight.mapOrSelf(it) }
            .let { lightToTemperature.mapOrSelf(it) }
            .let { temperatureToHumidity.mapOrSelf(it) }
            .let { humidityToLocation.mapOrSelf(it) }
    }
}

data class RangedMapping(
    val destination: Long,
    val source: Long,
    val size: Long
) {

    companion object {
        fun List<RangedMapping>.map(num: Long): Long? {
            return this.fold(null) { previous: Long?, mapping: RangedMapping ->
                previous ?: mapping.map(num)
            }
        }


        fun List<RangedMapping>.mapOrSelf(num: Long): Long {
            return this.map(num) ?: num
        }

        fun List<RangedMapping>.map(ranges: List<LongRange>): List<LongRange> {
            return this.fold(ranges.map { it to false }) { acc, mapping ->
                acc.flatMap { (range, isMapped) ->
                    if (isMapped) {
                        listOf(range to true)
                    } else {
                        mapping.map(range)
                    }
                }
            }
                .map { it.first }
                .sortAndMerge()
        }
    }

    fun contains(num: Long): Boolean {
        return num in source..<source + size
    }

    fun contains(range: LongRange): Boolean {
        return range.overlaps(LongRange(source, size))
    }

    fun map(num: Long): Long? {
        return if (!contains(num)) {
            null
        } else {
            destination + (num - source)
        }
    }

    fun mapOrSelf(num: Long): Long {
        return this.map(num) ?: num
    }

    fun map(range: LongRange): List<Pair<LongRange, Boolean>> {
        return range.splitBy(LongRange(source, size))
            .map {
                if (this.contains(it)) {
                    LongRange(this.mapOrSelf(it.start), it.size) to true
                } else {
                    it to false
                }
            }
    }
}

class LongRange(
    val start: Long,
    val size: Long
) : Comparable<LongRange> {

    val end: Long = start + size - 1

    private val comparator: Comparator<LongRange> =
        Comparator.comparingLong<LongRange?> { it.start }.thenComparingLong { it.end }

    companion object {
        fun List<LongRange>.sortAndMerge(): List<LongRange> {
            return this.sorted()
                .fold(ArrayDeque<LongRange>(this.size)) { stack, current ->
                    val previous = stack.removeLastOrNull()
                    if (previous != null) {
                        if (previous.overlapsOrAdjacentTo(current)) {
                            val start = previous.start
                            val range = current.end - previous.start + 1
                            stack.addLast(LongRange(start, range))
                        } else {
                            stack.addLast(previous)
                            stack.addLast(current)
                        }
                    } else {
                        stack.addLast(current)
                    }
                    stack
                }
                .toList()
        }
    }

    override fun compareTo(other: LongRange): Int {
        return comparator.compare(this, other)
    }

    override fun equals(other: Any?): Boolean {
        return other is LongRange && this.start == other.start && this.size == other.size
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + size.hashCode()
        return result
    }

    fun overlaps(other: LongRange): Boolean {
        return if (this.start == other.start) {
            true
        } else if (this.start < other.start) {
            this.end >= other.start
        } else {
            this.start <= other.end
        }
    }

    fun overlapsOrAdjacentTo(other: LongRange): Boolean {
        return if (this.start == other.start) {
            true
        } else if (this.start < other.start) {
            this.end >= other.start - 1
        } else {
            this.start <= other.end + 1
        }
    }

    fun contains(other: LongRange): Boolean {
        return other.start >= this.start && other.end <= this.end
    }

    fun splitBy(other: LongRange): List<LongRange> {
        return if (other == this || !this.overlaps(other) || other.contains(this)) {
            listOf(this)
        } else if (!this.contains(other)) {
            if (this < other) {
                listOf(
                    LongRange(this.start, other.start - this.start),
                    LongRange(other.start, this.end - other.start + 1)
                )
            } else {
                listOf(
                    LongRange(this.start, other.end - this.start + 1),
                    LongRange(other.end + 1, this.end - other.end)
                )
            }
        } else if(this.start == other.start) {
            listOf(
                LongRange(this.start, other.end-this.start+1),
                LongRange(other.end + 1, this.end - other.end)
            )
        } else if(this.end == other.end) {
            listOf(
                LongRange(this.start, other.start-this.start),
                LongRange(other.start, this.end - other.start+1)
            )
        }
        else {
            listOf(
                LongRange(this.start, other.start - this.start),
                LongRange(other.start, other.size),
                LongRange(other.end + 1, this.end - other.end)
            )
        }
    }

}

