package rhs.assignment
import com.google.auto.service.AutoService
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.pom.PomModel
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension

@AutoService(ComponentRegistrar::class)
class RhsAssignmentRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
//        val destinationPath = configuration[ImportsDumperConfigurationKeys.DESTINATION] ?: return
        val model = RhsAssignmentPomModel(project)
        project.registerService(PomModel::class.java, model)
        AnalysisHandlerExtension.registerExtension(project, RhsAssignmentExtension())
    }
}
