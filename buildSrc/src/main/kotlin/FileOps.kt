import org.gradle.api.Project
import org.gradle.api.logging.Logger

class FileOps(private val project: Project, private val logger: Logger) {
    fun createFileYearDay(
        year: String,
        day: String,
        pathName: String,
        fileName: String,
        text: String? = null
    ) {
        createFile(
            pathName = "$pathName/year$year/day$day",
            fileName = fileName,
            text = text,
        )
    }

    fun createFile(
        pathName: String,
        fileName: String,
        text: String? = null
    ) {
        val dir = project.layout.projectDirectory.dir(pathName)
        logger.info("Creating directory: $dir if does not exist already")
        project.mkdir(dir)
        val file = dir.file(fileName).asFile
        logger.info("Creating file: $file")
        file.createNewFile()
        logger.info("Writing to file: $file")
        text?.let { file.writeText(it) }
    }

    fun writeLinesToFile(
        pathName: String,
        fileName: String,
        lines: List<String>
    ) {
        val file = project.layout.projectDirectory.dir(pathName).file(fileName).asFile
        logger.info("Appending to file: $file")
        file.writeText(lines.joinToString(separator = "\n"))
    }

    fun readFileLines(
        pathName: String,
        fileName: String,
    ): List<String> {
        return project.layout.projectDirectory.dir(pathName).file(fileName).asFile.readLines()
    }
}