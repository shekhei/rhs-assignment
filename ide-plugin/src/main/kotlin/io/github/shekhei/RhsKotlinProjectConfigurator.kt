package io.github.shekhei

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ExternalLibraryDescriptor
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.ApiVersion
import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.idea.configuration.ConfigureKotlinStatus
import org.jetbrains.kotlin.idea.configuration.KotlinProjectConfigurator
import org.jetbrains.kotlin.idea.configuration.ModuleSourceRootGroup
import org.jetbrains.kotlin.idea.versions.LibraryJarDescriptor
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension

class RhsKotlinProjectConfigurator : KotlinProjectConfigurator {
    override val name: String
        get() = "RhsAssignment"
    override val presentableText: String
        get() = "RhsAssignment"

    override fun changeCoroutineConfiguration(module: Module, state: LanguageFeature.State) {
    }

    override fun changeGeneralFeatureConfiguration(module: Module, feature: LanguageFeature, state: LanguageFeature.State, forTests: Boolean) {
    }

    override fun configure(project: Project, excludeModules: Collection<Module>) {
        AnalysisHandlerExtension.registerExtension(project, RhsAssignmentExtension())
    }

    override fun getStatus(moduleSourceRootGroup: ModuleSourceRootGroup): ConfigureKotlinStatus {
        return ConfigureKotlinStatus.CONFIGURED
    }

    override fun getTargetPlatform() = @Suppress("DEPRECATION_ERROR") org.jetbrains.kotlin.resolve.jvm.platform.JvmPlatform.INSTANCE

    override fun updateLanguageVersion(module: Module, languageVersion: String?, apiVersion: String?, requiredStdlibVersion: ApiVersion, forTests: Boolean) {
    }

    override fun addLibraryDependency(module: Module, element: PsiElement, library: ExternalLibraryDescriptor, libraryJarDescriptors: List<LibraryJarDescriptor>) {
    }
}