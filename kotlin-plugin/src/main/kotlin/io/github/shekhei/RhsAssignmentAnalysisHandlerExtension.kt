package io.github.shekhei

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.com.intellij.openapi.application.ApplicationManager
import org.jetbrains.kotlin.com.intellij.openapi.components.ServiceManager
import org.jetbrains.kotlin.com.intellij.openapi.fileTypes.FileType
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.psi.impl.source.codeStyle.IndentHelper
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.LanguageVersionSettings
import org.jetbrains.kotlin.container.ComponentProvider
import org.jetbrains.kotlin.context.ProjectContext
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.extensions.PreprocessedVirtualFileFactoryExtension
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.psiUtil.astReplace
import org.jetbrains.kotlin.psi.psiUtil.getNextSiblingIgnoringWhitespace
import org.jetbrains.kotlin.psi.psiUtil.getPrevSiblingIgnoringWhitespaceAndComments
import org.jetbrains.kotlin.psi.psiUtil.nextLeaf
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.BindingTraceContext
import org.jetbrains.kotlin.resolve.CodeAnalyzerInitializer
import org.jetbrains.kotlin.resolve.jvm.KotlinJavaPsiFacade
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
import org.jetbrains.kotlin.resolve.lazy.KotlinCodeAnalyzer
import java.io.File

fun findPreviousSibling(el: PsiElement): PsiElement? {
    var el: PsiElement? = el
    while (el != null) {
        val prev = el.getPrevSiblingIgnoringWhitespaceAndComments()
        if (prev != null) return prev
        el = el.parent
    }
    return null
}

typealias KtFileCreator = (text: String) -> PsiElement

fun createKotlinFile(project: Project): KtFileCreator = { text: String ->
    PsiFileFactory.getInstance(project)
            .createFileFromText(KotlinLanguage.INSTANCE, text)
}

fun createAssignmentOp(ktFileCreator: KtFileCreator) = ktFileCreator("val a = 10").let {
    it.firstChild.nextLeaf { it.text == "=" }!!
}

fun processChildren(el: PsiElement, ktFileCreator: KtFileCreator) {
    // ever block can only have one of such operator
    val children = el.children
    if (children.isEmpty()) return
    for (i in 0 until children.size) {
        val first = children[i]
        var str = ""
        var node = first
        var offset = 0
        while (offset < 3 && "=>>".startsWith(str) && node != null) {
            if (str == "=>>") {
                // matched!
                break
            }
            str += node.text
            node = node.nextSibling
            offset++
        }
        if (str == "=>>") {
            val next = children[i + offset]
            // keep finding one parent with previous sibling
            val prev = findPreviousSibling(first)
            if (prev == null) {
                throw IllegalStateException("syntax error, |> must be not be first expression")
            }
            // lets swap them
            if (next === next.parent.lastChild) {
                throw IllegalStateException("syntax error, |> must be followed by property statement")
            }
            var nextEl = next.nextSibling
            // if this is the last element, this should fail straight away
            while (nextEl != null) {
                val sibling = nextEl.nextSibling
                prev.parent.addBefore(nextEl, prev)
                if (nextEl == next.parent.lastChild) {
                    break
                }
                nextEl = sibling
            }
            prev.parent.addBefore(createAssignmentOp(ktFileCreator), prev)
            first.parent.deleteChildRange(first, first.parent.lastChild)
        }
        processChildren(first, ktFileCreator)
    }
}

fun processFile(file: PsiFile) {
    processChildren(file, createKotlinFile(file.manager.project))
}

class OurVisitor : PsiElementVisitor() {
    override fun visitFile(file: PsiFile?) {
//        super.visitFile(file)
        file?.let(::processFile)
//        processFile(file)
    }
}

class RhsAssignmentPreprocess : PreprocessedVirtualFileFactoryExtension {
    override fun createPreprocessedFile(file: VirtualFile?) = file?.let { LightVirtualFile(it.name, it.fileType, String(it.contentsToByteArray(), it.charset)) }
    override fun createPreprocessedLightFile(file: LightVirtualFile?) = file
    override fun isPassThrough() = false

}

class RhsAssignmentExtension() : AnalysisHandlerExtension {
    private var ran = false

    // returns true if immediate children has operator

    init {
        println("ERM what")
    }

    override fun analysisCompleted(project: Project, module: ModuleDescriptor, bindingTrace: BindingTrace, files: Collection<KtFile>): AnalysisResult? {
        println("ERM")
        if (ran) return null
        return super.analysisCompleted(project, module, bindingTrace, files)
    }

    override fun doAnalysis(project: Project, module: ModuleDescriptor, projectContext: ProjectContext, files: Collection<KtFile>, bindingTrace: BindingTrace, componentProvider: ComponentProvider): AnalysisResult? {
        println("ERM what2")
//        val psiFileFactory = KtPsiFactory(project, false)

//        return when (ran) {
//            true -> null
//            false -> AnalysisResult.EMPTY
//        }
        // returning null at the end allows the lazy analysis to happen, which generates all the class descriptors and everything else
        // unless we want to handle it ourselves
        if (ran) return null //AnalysisResult.success(bindingTrace.bindingContext, module, shouldGenerateCode = false)
        ran = true
        val psiFileFactory = PsiFileFactory.getInstance(project)
        val newFiles = files.map {
            if (it.fileType is KotlinFileType && !it.isScript()) {
//                it.navigationElement.accept(OurVisitor())
//                val file = psiFileFactory.createFileFromText(
//                        it.virtualFilePath,
//                        it.language,
//                        it.text
//                )
//                it.processChildren {  }
//                ServiceManager
                val copied = it
                copied.accept(OurVisitor())
                val newFile = PsiFileFactory.getInstance(project).createFileFromText(
                        copied.virtualFile.name,
                        it.fileType,
                        copied.text
                )
                val first = copied.firstChild
                val last = copied.lastChild
                copied.deleteChildRange(first, last)
                newFile.children.forEach {
                    copied.add(it!!)
                }
                copied
//                newFile
            } else it
//            processFile(it)
        }
//        val a = GenerationState.Builder(
//                project= project,
//                module = module,
//                bindingContext = bindingTrace.bindingContext,
//                files = newFiles,
//                configuration =
//        ).build()
//        return AnalysisResult.success(
//                bindingContext = bindingTrace.bindingContext,
//                module = module,
//                shouldGenerateCode = true
//        )
        return AnalysisResult.RetryWithAdditionalRoots(
                bindingContext = bindingTrace.bindingContext,
                additionalJavaRoots = emptyList(),
                additionalKotlinRoots = emptyList(),
                moduleDescriptor = module,
                addToEnvironment = true
        )
    }
}

