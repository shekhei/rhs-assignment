package io.github.shekhei

import com.google.auto.service.AutoService
import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.project.ModuleData
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.jetbrains.kotlin.cli.common.arguments.CommonCompilerArguments
import org.jetbrains.kotlin.idea.configuration.GradleProjectImportHandler
import org.jetbrains.kotlin.idea.facet.KotlinFacet
import org.jetbrains.kotlin.utils.PathUtil
import org.jetbrains.plugins.gradle.model.data.GradleSourceSetData
import org.jetbrains.plugins.gradle.tooling.ErrorMessageBuilder
import org.jetbrains.plugins.gradle.tooling.ModelBuilderService
import java.io.File
import java.lang.reflect.InvocationTargetException

abstract class AbstractKotlinGradleModelBuilder : ModelBuilderService {
    companion object {
        val kotlinCompileJvmTaskClasses = listOf(
                "org.jetbrains.kotlin.gradle.tasks.KotlinCompile_Decorated",
                "org.jetbrains.kotlin.gradle.tasks.KotlinCompileWithWorkers_Decorated"
        )

        val kotlinCompileTaskClasses = kotlinCompileJvmTaskClasses + listOf(
                "org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile_Decorated",
                "org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon_Decorated",
                "org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompileWithWorkers_Decorated",
                "org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommonWithWorkers_Decorated"
        )
        val platformPluginIds = listOf("kotlin-platform-jvm", "kotlin-platform-js", "kotlin-platform-common")
        val pluginToPlatform = linkedMapOf(
                "kotlin" to "kotlin-platform-jvm",
                "kotlin2js" to "kotlin-platform-js"
        )
        val kotlinPluginIds = listOf("kotlin", "kotlin2js", "kotlin-android")
        val ABSTRACT_KOTLIN_COMPILE_CLASS = "org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile"

        val kotlinProjectExtensionClass = "org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension"
        val kotlinSourceSetClass = "org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet"

        val kotlinPluginWrapper = "org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapperKt"

        fun Task.getSourceSetName(): String = try {
            javaClass.methods.firstOrNull { it.name.startsWith("getSourceSetName") && it.parameterTypes.isEmpty() }?.invoke(this) as? String
        } catch (e: InvocationTargetException) {
            null // can be thrown if property is not initialized yet
        } ?: "main"
    }
}

class RhsAssignmentModel {}

@AutoService(ModelBuilderService::class)
class RhsAssignmentModelBuilderService : AbstractKotlinGradleModelBuilder() {
    override fun canBuild(modelName: String?) = RhsAssignmentModel::class.qualifiedName == modelName
    override fun buildAll(modelName: String?, project: Project): Any {
        val plugin: Plugin<*>? =  project.findPlugin(listOf(
                "io.github.shekhei:rhs-assignment-compiler-plugin"
        ))
        return RhsAssignmentModel()
    }

    override fun getErrorMessageBuilder(project: Project, e: Exception): ErrorMessageBuilder {
        return ErrorMessageBuilder.create(project, e, "Error here")
    }
    private fun Project.findPlugin(names: List<String>): Plugin<*>? {
        for (name in names) {
            plugins.findPlugin(name)?.let { return it }
        }

        return null
    }
}

class RhsAssignmentGradleProjectImportHandler : GradleProjectImportHandler {
    val compilerPluginId: String = RhsAssignmentCli.PLUGIN_ID
    val pluginJpsJarName = "rhs-assignmet-compiler-plugin.jar"
    val PLUGIN_JPS_JAR: String
        get() = File(PathUtil.kotlinPathsForIdeaPlugin.libPath, pluginJpsJarName).absolutePath
    val pluginName: String = "RhsAssignment"

    fun modifyCompilerArguments(facet: KotlinFacet, buildSystemPluginJar: String) {
        val facetSettings = facet.configuration.settings
        val commonArguments = facetSettings.compilerArguments ?: CommonCompilerArguments.DummyImpl()

        var pluginWasEnabled = false
        val oldPluginClasspaths = (commonArguments.pluginClasspaths ?: emptyArray()).filterTo(mutableListOf()) {
            val lastIndexOfFile = it.lastIndexOfAny(charArrayOf('/', File.separatorChar))
            if (lastIndexOfFile < 0) {
                return@filterTo true
            }
            val match = it.drop(lastIndexOfFile + 1).matches("$buildSystemPluginJar-.*\\.jar".toRegex())
            if (match) pluginWasEnabled = true
            !match
        }

        val newPluginClasspaths = if (pluginWasEnabled) oldPluginClasspaths + PLUGIN_JPS_JAR else oldPluginClasspaths
        commonArguments.pluginClasspaths = newPluginClasspaths.toTypedArray()
        facetSettings.compilerArguments = commonArguments
    }

    override fun importBySourceSet(facet: KotlinFacet, sourceSetNode: DataNode<GradleSourceSetData>) {
        modifyCompilerArguments(facet, compilerPluginId)
    }

    override fun importByModule(facet: KotlinFacet, moduleNode: DataNode<ModuleData>) {
        modifyCompilerArguments(facet, compilerPluginId)
    }


//    protected open fun getAnnotationsForPreset(presetName: String): List<String> = emptyList()
//
//    protected open fun getAdditionalOptions(model: T): List<AnnotationBasedCompilerPluginSetup.PluginOption> = emptyList()

//    private fun getPluginSetupByModule(
//            moduleNode: DataNode<ModuleData>
//    ): AnnotationBasedCompilerPluginSetup? {
//        val pluginModel = moduleNode.getCopyableUserData(modelKey)?.takeIf { it.isEnabled } ?: return null
//        val annotations = pluginModel.annotations
//        val presets = pluginModel.presets

//        val allAnnotations = annotations + presets.flatMap { getAnnotationsForPreset(it) }
//        val options = allAnnotations.map { AnnotationBasedCompilerPluginSetup.PluginOption(annotationOptionName, it) } + getAdditionalOptions(pluginModel)

        // For now we can't use plugins from Gradle cause they're shaded and may have an incompatible version.
        // So we use ones from the IDEA plugin.
//        val classpath = listOf(pluginJarFileFromIdea.absolutePath)
//
//        return AnnotationBasedCompilerPluginSetup(options, classpath)
//    }

//    private fun getPluginSetupBySourceSet(sourceSetNode: DataNode<GradleSourceSetData>) =
//            ExternalSystemApiUtil.findParent(sourceSetNode, ProjectKeys.MODULE)?.let { getPluginSetupByModule(it) }
}