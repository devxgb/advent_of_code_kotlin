import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class AOCInitInputFolder: DefaultTask() {

    private val fileOps: FileOps = FileOps(project, logger)

    @TaskAction
    fun taskAction() {
        val yearDays = readYearDayFile()
        yearDays.forEach { (year, day) ->
            fileOps.createFileYearDay(
                year = year,
                day = day,
                pathName = "src/main/resources/input",
                fileName = "input.txt",
            )
        }
    }

    private data class YearDay(val year: String, val day: String)

    private fun readYearDayFile(): List<YearDay> {
        return fileOps.readFileLines(pathName = "src/main/resources", fileName = "YearDay.csv")
            .drop(1)
            .map { line ->
                val split = line.split(",")
                require(split.size == 2)
                YearDay(split[0], split[1])
            }
    }
}