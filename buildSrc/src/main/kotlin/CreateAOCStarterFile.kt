import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class CreateAOCStarterFile : DefaultTask() {
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
        createFile(
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
        createFile(year = yearString, day = dayString, pathName = "src/main/resources/input", fileName = "input.txt")

        // /src/test/kotlin
        createFile(
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
        createFile(year = yearString, day = dayString, pathName = "src/test/resources/input", fileName = "test_input.txt")
    }

    private fun createFile(
        year: String,
        day: String,
        pathName: String,
        fileName: String,
        text: String? = null
    ) {
        val srcMainKotlinDir = project.layout.projectDirectory.dir(pathName).dir("year$year").dir("day$day")
        project.mkdir(srcMainKotlinDir)
        val srcMainKotlinFile = srcMainKotlinDir.file(fileName).asFile
        srcMainKotlinFile.createNewFile()
        text?.let { srcMainKotlinFile.writeText(it) }
    }
}