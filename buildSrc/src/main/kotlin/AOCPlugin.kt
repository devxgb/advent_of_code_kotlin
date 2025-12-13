import org.gradle.api.Plugin
import org.gradle.api.Project

class AOCPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("aocCreateStarterFile", AOCCreateStarterFile::class.java)
        project.tasks.register("aocInitInputFolder", AOCInitInputFolder::class.java)
    }
}