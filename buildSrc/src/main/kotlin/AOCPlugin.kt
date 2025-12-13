import org.gradle.api.Plugin
import org.gradle.api.Project

class AOCPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        println("Testing")
        project.tasks.register("aoc", CreateAOCStarterFile::class.java)
    }
}