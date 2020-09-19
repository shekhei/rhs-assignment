package io.github.shekhei


import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

object ImportsDumperCliOptions {
//    val DESTINATION = CliOption(
//            optionName = "output-imports",
//            valueDescription = "<path>",
//            description = "Output imports from all compiled files to the specified file in JSON format",
//            required = false // non-required because importsDumper is a bundled plugin
//    )
}

object ImportsDumperConfigurationKeys {
//    val DESTINATION = CompilerConfigurationKey.create<String>("Destination of imports dump")
}

@AutoService(CommandLineProcessor::class)
class RhsAssignmentCli: CommandLineProcessor {
    override val pluginId: String = PLUGIN_ID

//    override val pluginOptions: Collection<AbstractCliOption> = listOf(ImportsDumperCliOptions.DESTINATION)
    override val pluginOptions: Collection<AbstractCliOption> = listOf()

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
//        when (option) {
//            ImportsDumperCliOptions.DESTINATION -> configuration.put(ImportsDumperConfigurationKeys.DESTINATION, value)
//            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
//        }
    }

    companion object {
        const val PLUGIN_ID = "rhs.assignment.rhsAssignment"
    }
}