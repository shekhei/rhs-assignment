package io.github.shekhei

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.mock.MockApplication
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.application.ApplicationManager
import org.jetbrains.kotlin.com.intellij.pom.PomModel
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.com.intellij.psi.impl.source.codeStyle.IndentHelper
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.extensions.PreprocessedVirtualFileFactoryExtension
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension

@AutoService(ComponentRegistrar::class)
class RhsAssignmentRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
//        val destinationPath = configuration[ImportsDumperConfigurationKeys.DESTINATION] ?: return
        val helper = object : IndentHelper() {
            override fun getIndent(p0: PsiFile, p1: ASTNode) = 0
            override fun getIndent(p0: PsiFile, p1: ASTNode, p2: Boolean) = 0
        }

        val manager = ApplicationManager.getApplication() as MockApplication
        if ( manager.getService(IndentHelper::class.java) == null ) {
            manager.registerService(IndentHelper::class.java, helper)
//        project.picoContainer.registerComponentInstance(IndentHelper::class.java, helper)
            project.registerService(IndentHelper::class.java, helper)
        }
        val model = RhsAssignmentPomModel(project)
        project.registerService(PomModel::class.java, model)
        PreprocessedVirtualFileFactoryExtension.registerExtension(project, RhsAssignmentPreprocess())
        AnalysisHandlerExtension.registerExtension(project, RhsAssignmentExtension())
    }
}
