package org.example.aoc.year2023.day05

import org.example.aoc.year2023.day05.LongRange.Companion.sortAndMerge
import org.example.aoc.year2023.day05.RangedMapping.Companion.map
import org.example.aoc.year2023.day05.RangedMapping.Companion.mapOrSelf
import org.example.common.readTestFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Test {

    private val lines = readTestFile("\\year2023\\day05\\test_input.txt")
    private val day5 = Day5()

    @Test
    fun parseSeedTest() {
        assertEquals(listOf(79L, 14L, 55L, 13L), day5.parseSeeds(lines))
    }

    @Test
    fun parseSeedToSoilTest() {
        assertEquals(
            listOf(
                RangedMapping(50, 98, 2),
                RangedMapping(52, 50, 48)
            ),
            day5.parseSeedToSoil(lines)
        )
    }

    @Test
    fun parseSoilToFertilizerTest() {
        assertEquals(
            listOf(
                RangedMapping(0, 15, 37),
                RangedMapping(37, 52, 2),
                RangedMapping(39, 0, 15)
            ),
            day5.parseSoilToFertilizer(lines)
        )
    }

    @Test
    fun parseFertilizerToWaterTest() {
        assertEquals(
            listOf(
                RangedMapping(49, 53, 8),
                RangedMapping(0, 11, 42),
                RangedMapping(42, 0, 7),
                RangedMapping(57, 7, 4)
            ),
            day5.parseFertilizerToWater(lines)
        )
    }

    @Test
    fun parseWaterToLightTest() {
        assertEquals(
            listOf(
                RangedMapping(88, 18, 7),
                RangedMapping(18, 25, 70)
            ),
            day5.parseWaterToLight(lines)
        )
    }

    @Test
    fun parseLightToTemperatureTest() {
        assertEquals(
            listOf(
                RangedMapping(45, 77, 23),
                RangedMapping(81, 45, 19),
                RangedMapping(68, 64, 13)
            ),
            day5.parseLightToTemperature(lines)
        )
    }

    @Test
    fun parseTemperatureToHumidityTest() {
        assertEquals(
            listOf(
                RangedMapping(0, 69, 1),
                RangedMapping(1, 0, 69)
            ),
            day5.parseTemperatureToHumidity(lines)
        )
    }

    @Test
    fun parseHumidityToLocationTest() {
        assertEquals(
            listOf(
                RangedMapping(60, 56, 37),
                RangedMapping(56, 93, 4)
            ),
            day5.parseHumidityToLocation(lines)
        )
    }

    @Test
    fun testMapOrSelf() {
        val mappings = listOf(
            RangedMapping(50, 98, 2),
            RangedMapping(52, 50, 48)
        )

        for (i in 0L..49L) {
            assertEquals(i, mappings.mapOrSelf(i))
        }

        for (i in 50L..97L) {
            assertEquals(i + 2, mappings.mapOrSelf(i))
        }

        for (i in 98L..99L) {
            assertEquals(i - 48, mappings.mapOrSelf(i))
        }

    }

    @Test
    fun mapRangeTest() {
        assertEquals(
            listOf(
                LongRange(101, 10)
            ),
            listOf(RangedMapping(101, 1, 10)).map(listOf(LongRange(1, 10)))
        )

        assertEquals(
            listOf(
                LongRange(111, 10)
            ),
            listOf(RangedMapping(101, 1, 30)).map(listOf(LongRange(11, 10)))
        )

        assertEquals(
            listOf(
                LongRange(21, 10),
                LongRange(111, 10)
            ),
            listOf(RangedMapping(101, 1, 20)).map(listOf(LongRange(11, 20)))
        )

        assertEquals(
            listOf(
                LongRange(1, 10),
                LongRange(111, 10)
            ),
            listOf(RangedMapping(111, 11, 20)).map(listOf(LongRange(1, 20)))
        )

        assertEquals(
            listOf(
                LongRange(1, 10),
                LongRange(21, 10),
                LongRange(111, 10)
            ),
            listOf(RangedMapping(111, 11, 10)).map(listOf(LongRange(1, 30)))
        )
    }

    @Test
    fun mapRangeListTest() {
        assertEquals(
            listOf(
                LongRange(57, 13),
                LongRange(81, 14)
            ),
            listOf(
                RangedMapping(50, 98, 2),
                RangedMapping(52, 50, 48)
            ).map(
                listOf(
                    LongRange(79, 14),
                    LongRange(55, 13)
                )
            )
        )

        assertEquals(
            listOf(
                LongRange(101, 10)
            ),
            listOf(
                RangedMapping(101, 1, 10),
                RangedMapping(201, 101, 10)
            ).map(
                listOf(
                    LongRange(1, 10)
                )
            )
        )

        assertEquals(
            listOf(
                LongRange(101, 10),
                LongRange(201, 10)
            ),
            listOf(
                RangedMapping(101, 1, 10),
                RangedMapping(201, 101, 10)
            ).map(
                listOf(
                    LongRange(1, 10),
                    LongRange(101, 10)
                )
            )
        )

        assertEquals(
            listOf(
                LongRange(101, 10)
            ),
            listOf(
                RangedMapping(101, 1, 10),
                RangedMapping(101, 21, 10)
            ).map(
                listOf(
                    LongRange(1, 10),
                    LongRange(21, 10)
                )
            )
        )

        assertEquals(
            listOf(
                LongRange(101, 10)
            ),
            listOf(
                RangedMapping(101, 1, 10),
                RangedMapping(101, 21, 10)
            ).map(
                listOf(
                    LongRange(1, 10),
                    LongRange(21, 10)
                )
            )
        )

        assertEquals(
            listOf(
                LongRange(101, 30)
            ),
            listOf(
                RangedMapping(101, 1, 20),
                RangedMapping(111, 21, 20)
            ).map(
                listOf(
                    LongRange(1, 20),
                    LongRange(21, 20)
                )
            )
        )

        assertEquals(
            listOf(
                LongRange(101, 30)
            ),
            listOf(
                RangedMapping(101, 1, 20),
                RangedMapping(111, 21, 20)
            ).map(
                listOf(
                    LongRange(1, 20),
                    LongRange(21, 20)
                )
            )
        )

        assertEquals(
            listOf(
                LongRange(101, 30)
            ),
            listOf(
                RangedMapping(101, 1, 10),
                RangedMapping(121, 21, 10),
                RangedMapping(111, 11, 10)
            ).map(
                listOf(
                    LongRange(1, 30)
                )
            )
        )
    }

    @Test
    fun mapSeedToLocationTest() {
        val seedToSoil = day5.parseSeedToSoil(lines)
        val soilToFertilizer = day5.parseSoilToFertilizer(lines)
        val fertilizerToWater = day5.parseFertilizerToWater(lines)
        val waterToLight = day5.parseWaterToLight(lines)
        val lightToTemperature = day5.parseLightToTemperature(lines)
        val temperatureToHumidity = day5.parseTemperatureToHumidity(lines)
        val humidityToLocation = day5.parseHumidityToLocation(lines)

        for ((seed, expectedLocation) in listOf(
            79L to 82L,
            14L to 43L,
            55L to 86L,
            13L to 35L
        )) {
            val actualLocation = day5.mapSeedToLocation(
                seed = seed,
                seedToSoil = seedToSoil,
                soilToFertilizer = soilToFertilizer,
                fertilizerToWater = fertilizerToWater,
                waterToLight = waterToLight,
                lightToTemperature = lightToTemperature,
                temperatureToHumidity = temperatureToHumidity,
                humidityToLocation = humidityToLocation
            )
            assertEquals(expectedLocation, actualLocation)
        }
    }

    @Test
    fun overlapsTest() {
        assertEquals(true, LongRange(1, 10).overlaps(LongRange(10, 10)))
        assertEquals(true, LongRange(10, 10).overlaps(LongRange(1, 10)))
        assertEquals(false, LongRange(1, 10).overlaps(LongRange(11, 10)))
        assertEquals(false, LongRange(11, 10).overlaps(LongRange(1, 10)))
        assertEquals(true, LongRange(1, 10).overlaps(LongRange(1, 10)))
        assertEquals(true, LongRange(1, 10).overlaps(LongRange(5, 5)))
        assertEquals(true, LongRange(1, 10).overlaps(LongRange(1, 5)))
    }

    @Test
    fun overlapsOrAdjacentToTest() {
        assertEquals(true, LongRange(1, 10).overlapsOrAdjacentTo(LongRange(10, 10)))
        assertEquals(true, LongRange(0, 10).overlapsOrAdjacentTo(LongRange(1, 10)))
        assertEquals(true, LongRange(1, 10).overlapsOrAdjacentTo(LongRange(11, 10)))
        assertEquals(true, LongRange(11, 10).overlapsOrAdjacentTo(LongRange(1, 10)))
        assertEquals(false, LongRange(1, 10).overlapsOrAdjacentTo(LongRange(12, 10)))
        assertEquals(false, LongRange(12, 10).overlapsOrAdjacentTo(LongRange(1, 10)))
    }

    @Test
    fun containsTest() {
        assertEquals(true, LongRange(1, 30).contains(LongRange(11, 10)))
        assertEquals(true, LongRange(1, 10).contains(LongRange(1, 10)))
        assertEquals(true, LongRange(1, 10).contains(LongRange(5, 5)))
        assertEquals(true, LongRange(1, 10).contains(LongRange(1, 5)))
        assertEquals(false, LongRange(1, 20).contains(LongRange(11, 20)))
        assertEquals(false, LongRange(1, 20).contains(LongRange(101, 10)))
    }

    @Test
    fun splitByTest() {
        assertEquals(listOf(LongRange(1, 10)), LongRange(1, 10).splitBy(LongRange(1, 10)))
        assertEquals(listOf(LongRange(1, 10)), LongRange(1, 10).splitBy(LongRange(100, 10)))
        assertEquals(listOf(LongRange(11, 10)), LongRange(11, 10).splitBy(LongRange(1, 30)))
        assertEquals(
            listOf(
                LongRange(1, 10),
                LongRange(11, 10),
                LongRange(21, 10)
            ),
            LongRange(1, 30).splitBy(LongRange(11, 10))
        )
        assertEquals(
            listOf(
                LongRange(1, 10),
                LongRange(11, 10)
            ),
            LongRange(1, 20).splitBy(LongRange(11, 20))
        )

        assertEquals(
            listOf(
                LongRange(11, 10),
                LongRange(21, 10)
            ),
            LongRange(11, 20).splitBy(LongRange(1, 20))
        )
    }

    @Test
    fun seedRangeMergeTest() {
        val actual = listOf(
            LongRange(1, 10),
            LongRange(11, 10),
            LongRange(15, 16),
            LongRange(101, 10),
            LongRange(112, 9)
        ).sortAndMerge()
        val expected = listOf(
            LongRange(1, 30),
            LongRange(101, 10),
            LongRange(112, 9)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun seedRangeMergeTest2() {
        val actual = listOf(
            LongRange(112, 9),
            LongRange(11, 10),
            LongRange(101, 10),
            LongRange(15, 16),
            LongRange(1, 10),
        ).sortAndMerge()
        val expected = listOf(
            LongRange(1, 30),
            LongRange(101, 10),
            LongRange(112, 9)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun parseSeedAsRangeTest() {
        val result = day5.parseSeedAsRange(lines)
        val expected = listOf(
            LongRange(79, 14),
            LongRange(55, 13)
        )
        assertEquals(expected, result)
    }

    @Test
    fun part1Test() {
        assertEquals(35, day5.part1(lines))
    }

    @Test
    fun part2Test() {
        assertEquals(46, day5.part2(lines))
    }

}