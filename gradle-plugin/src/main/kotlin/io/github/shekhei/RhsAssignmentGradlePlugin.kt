package io.github.shekhei

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.plugin.management.internal.autoapply.AutoAppliedGradleEnterprisePlugin.GROUP
import org.gradle.plugin.management.internal.autoapply.AutoAppliedGradleEnterprisePlugin.VERSION
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class RhsAssignmentGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project) {
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project

        return project.provider { listOf() }
    }

    override fun getCompilerPluginId() = "rhs-assignment-compiler-plugin"

    override fun getPluginArtifact() = SubpluginArtifact(
            groupId = "io.github.shekhei",
            artifactId = "rhs-assignment-compiler-plugin",
            version = "0.0.1-SNAPSHOT"
    )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>) = kotlinCompilation.target.project
            .plugins.hasPlugin(RhsAssignmentGradlePlugin::class.java)
}