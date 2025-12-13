import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class AOCCreateStarterFile : DefaultTask() {

    companion object {
        private const val SRC_MAIN_RESOURCES = "src/main/resources"
        private const val SRC_TEST_RESOURCES = "src/test/resources"
    }

    private val fileOps: FileOps = FileOps(project, logger)

    @TaskAction
    fun taskAction() {
        print("Enter year: ")
        System.out.flush()
        val year = readln().toInt()
        require(year in 2015..2025) { "Year out of range" }
        val yearString = year.toString()
        print("Enter day: ")
        System.out.flush()
        val day = readln().toInt()
        require(day in 1..25) { "Day out of range" }
        val dayString = "%02d".format(day)

        // /src/main/kotlin
        fileOps.createFileYearDay(
            year = yearString,
            day = dayString,
            pathName = "src/main/kotlin/org/example/aoc",
            fileName = "Year${year}Day$day.kt",
            text = """
                        package org.example.aoc.year$year.day$day
                        
                        import org.example.common.InputReader
                        
                        fun main() {
                            val lines = InputReader().readInput("$year", "$day")
                            val year${year}Day$day = Year${year}Day$day()
                            println(year${year}Day$day.part1(lines))
                            println(year${year}Day$day.part2(lines))
                        }
                        
                        class Year${year}Day$day {
                            fun part1(lines: List<String>): Int {
                                return 0
                            }
                        
                            fun part2(lines: List<String>): Int {
                                return 0
                            }
                        }
                    """.trimIndent()
        )

        // /src/main/resources
        fileOps.createFileYearDay(year = yearString, day = dayString, pathName = "$SRC_MAIN_RESOURCES/input", fileName = "input.txt")

        // /src/test/kotlin
        fileOps.createFileYearDay(
            year = yearString,
            day = dayString,
            pathName = "src/test/kotlin/org/example/aoc",
            fileName = "Year${year}Day${day}Test.kt",
            text = """
                        package org.example.aoc.year$year.day$day
                        
                        import kotlin.test.assertEquals
                        import org.example.common.InputReader
                        import org.junit.jupiter.api.Test
                        
                        class Year${year}Day${day}Test {
                        
                            private val lines = InputReader().readTestInput("$year", "$day")
                            private val year${year}Day${day} = Year${year}Day${day}()
                        
                            @Test
                            fun part1Test() {
                                assertEquals(0, year${year}Day${day}.part1(lines))
                            }
                        
                            @Test
                            fun part2Test() {
                                assertEquals(0, year${year}Day${day}.part2(lines))
                            }
                        }
                    """.trimIndent()
        )

        // /src/test/resources
        fileOps.createFileYearDay(year = yearString, day = dayString, pathName = "$SRC_MAIN_RESOURCES/input", fileName = "test_input.txt")

        // /src/main/resources/YearDay.csv
        addToYearDayCSV(year = yearString, day = dayString)

        // info
        logger.quiet("Get input form: https://adventofcode.com/$year/day/$day/input")
    }

    data class YearDay(val year: String, val day: String): Comparable<YearDay> {
        companion object {
            private val comparator: Comparator<YearDay> = Comparator.comparing<YearDay, String> { (year, _) -> year }.thenComparing { (_, day) -> day }
        }
        override fun compareTo(other: YearDay): Int = comparator.compare(this, other)
    }

    private fun addToYearDayCSV(year: String, day: String) {
        val csvFileName = "YearDay.csv"
        val csv = fileOps.readFileLines(pathName = SRC_MAIN_RESOURCES, fileName = csvFileName)
        val header = csv.take(1)
        val existingRows = csv.drop(1)
        val updatedRows = existingRows
            .map { line ->
                val split = line.split(",")
                require(split.size == 2)
                YearDay(split[0], split[1])
            }
            .toSet()
            .plus(YearDay(year = year, day = day))
            .sorted()
            .map { (year, day) -> "$year,$day" }
        if(existingRows == updatedRows) {
            logger.info("No update to src/main/resources/YearDay.csv")
        } else {
            logger.info("Updating src/main/resources/YearDay.csv")
            fileOps.writeLinesToFile(
                pathName = SRC_MAIN_RESOURCES,
                fileName = csvFileName,
                lines = header+updatedRows
            )
        }
    }
}