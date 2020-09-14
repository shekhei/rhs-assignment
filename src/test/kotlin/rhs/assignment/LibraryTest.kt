package rhs.assignment

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.mock.MockApplication
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.Disposable
import org.jetbrains.kotlin.com.intellij.openapi.application.ApplicationManager
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.com.intellij.psi.impl.source.codeStyle.IndentHelper
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.parsing.KotlinParserDefinition
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.Locale

@ExtendWith(MockKExtension::class)
class LibraryTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            Locale.setDefault(Locale("en", "US"))
        }

    }

    @MockK
    lateinit var disposable: Disposable



    @BeforeEach
    fun setupEach() {

//        val app = MockApplication(disposable)
//        ApplicationManager.setApplication(
//                app,
//                { FileTypeRegistry.getInstance() },
//                disposable
//        )
//        app.registerService(IndentHelper::class.java, object : IndentHelper() {
//            override fun getIndent(p0: PsiFile, p1: ASTNode) = 0
//            override fun getIndent(p0: PsiFile, p1: ASTNode, p2: Boolean) = 0
//        })
    }

    object RegisterIndentHelper : ComponentRegistrar {
        fun createEnvironment(): KotlinCoreEnvironment {
            val appWasNull = ApplicationManager.getApplication() == null
            val compilerConfiguration = CompilerConfiguration()
            compilerConfiguration.put(JVMConfigurationKeys.USE_PSI_CLASS_FILES_READING, true)

            val parentDisposable = Disposer.newDisposable()
            return KotlinCoreEnvironment.createForTests(parentDisposable, compilerConfiguration, EnvironmentConfigFiles.JVM_CONFIG_FILES)
        }
        override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
//            val helper = object : IndentHelper() {
//                override fun getIndent(p0: PsiFile, p1: ASTNode) = 0
//                override fun getIndent(p0: PsiFile, p1: ASTNode, p2: Boolean) = 0
//            }

//            val manager = ApplicationManager.getApplication() as MockApplication
//            manager.registerService(IndentHelper::class.java, helper)
//            project.picoContainer.registerComponentInstance(IndentHelper::class.java, helper)
//            project.registerService(IndentHelper::class.java, helper)
            AnalysisHandlerExtension.registerExtension(createEnvironment().project, RhsAssignmentExtension())
        }

    }

    @TempDir
    lateinit var tempDir: File
    private fun prepareCompilation(vararg sourceFiles: SourceFile) = KotlinCompilation().apply {
        workingDir = tempDir
//        compilerPlugins = listOf(RegisterIndentHelper, RhsAssignmentRegistrar())
        compilerPlugins = listOf(RegisterIndentHelper)
        commandLineProcessors = listOf(ImportsDumperCommandLineProcessor())
        messageOutputStream = System.out
        inheritClassPath = true
        sources = sourceFiles.asList()
        verbose = false
        jvmTarget = JvmTarget.fromString("1.8")!!.description
    }

    @Test
    fun `test my annotation processor`() {
        val classFile = SourceFile.kotlin(
                "dummy.kt",
                """
                                class Test {
                            fun a(b: Boolean): Int {
                              when(b) {
                                true -> 10
                                false -> 11
                              } |> val test
                              return test
                            }
                            }
                            """
        )
        val result = prepareCompilation(classFile).compile()
        val testClz = result.classLoader.loadClass("DummyKt")
        testClz.fields
    }

    @Test
    fun `test it`() {
        val classFile = SourceFile.kotlin(
                "dummy.kt",
                """
                                class Test {
                            fun a(b: Boolean): Int {
                              val test = when(b) {
                                true -> 10
                                false -> 11
                              }
                              return test
                            }
                            }
                            """
        )
        val result = prepareCompilation(classFile).compile()
        val testClz = result.classLoader.loadClass("DummyKt")
        testClz.fields
    }

//    @Test
//    fun `test complex`() {
//        assertThis(
//                CompilerTest(
//                        config = {
//                            System.setProperty("CURRENT_VERSION", "1.3.61-SNAPSHOT")
//                            metaDependencies + addMetaPlugins(MetaPlugin() as Meta)
//                        },
//                        code = {
//                            """
//                            | fun a(b: Boolean): Int {
//                            |   when(b) {
//                            |     true -> 10
//                            |     false -> 11
//                            |   } |> val test
//                            |   return test
//                            | }
//                            """.source
//                        },
//                        assert = {
//                            quoteOutputMatches("""
//                                | val test = 10
//                            """.source)
//                        }
//                )
//        )
//    }
}