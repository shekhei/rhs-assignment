package rhs.assignment

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.com.intellij.openapi.application.ApplicationManager
import org.jetbrains.kotlin.com.intellij.openapi.components.ServiceManager
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.psi.impl.source.codeStyle.IndentHelper
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.container.ComponentProvider
import org.jetbrains.kotlin.context.ProjectContext
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.psiUtil.getNextSiblingIgnoringWhitespace
import org.jetbrains.kotlin.psi.psiUtil.getPrevSiblingIgnoringWhitespaceAndComments
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.jvm.KotlinJavaPsiFacade
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
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

fun processChildren(el: PsiElement) {
    val found = mutableListOf<PsiElement>()
    // ever block can only have one of such operator
    val children = el.children
    if (children.isEmpty()) return
    for (i in 0 until children.size - 1) {
        val first = children[i]
        val next = children[i + 1]
        if (first.text == "|" && next.text == ">") {
            // keep finding one parent with previous sibling
            val prev = findPreviousSibling(first)
            if (prev == null) throw IllegalStateException("syntax is wrong")
            // lets swap them
//                var nextEl = next.nextSibling
//                while(nextEl != null) {
//                    val factory = project.elementFactory
//                    prev.parent.addBefore(prev)
//                    nextEl = nextEl.nextSibling
//                }
//            first.parent.deleteChildRange(first, first.parent.lastChild)
            var el = first
            while (el != null) {
                val next = el.getNextSiblingIgnoringWhitespace()
                el.delete()
                el = next
            }
        }
        processChildren(first)
    }
    processChildren(children.last())
}

fun processFile(file: PsiFile) {
    processChildren(file)
}

class OurVisitor : PsiElementVisitor() {
    override fun visitFile(file: PsiFile?) {
//        super.visitFile(file)
        file?.let(::processFile)
//        processFile(file)
    }
}

class RhsAssignmentExtension() : AnalysisHandlerExtension {
    private var ran = false
    // returns true if immediate children has operator


    override fun analysisCompleted(project: Project, module: ModuleDescriptor, bindingTrace: BindingTrace, files: Collection<KtFile>): AnalysisResult? {
        if (ran) return null
        val psiFileFactory = PsiFileFactory.getInstance(project)
        files.forEach {
            if (it.language is KotlinLanguage) {
//                it.navigationElement.accept(OurVisitor())
//                val file = psiFileFactory.createFileFromText(
//                        it.virtualFilePath,
//                        it.language,
//                        it.text
//                )
//                it.processChildren {  }
//                ServiceManager
                val copied = it.copy()
                copied.accept(OurVisitor())
            }
//            processFile(it)
        }
        return AnalysisResult.RetryWithAdditionalRoots(
                bindingContext = bindingTrace.bindingContext,
                additionalJavaRoots = emptyList(),
                additionalKotlinRoots = emptyList(),
                moduleDescriptor = module,
                addToEnvironment = true
        )
    }

    override fun doAnalysis(project: Project, module: ModuleDescriptor, projectContext: ProjectContext, files: Collection<KtFile>, bindingTrace: BindingTrace, componentProvider: ComponentProvider): AnalysisResult? {
//        val psiFileFactory = KtPsiFactory(project, false)

//        return when (ran) {
//            true -> null
//            false -> AnalysisResult.EMPTY
//        }
        if (ran) return null
        val psiFileFactory = PsiFileFactory.getInstance(project)
        files.forEach {
            if (it.language is KotlinLanguage) {
//                it.navigationElement.accept(OurVisitor())
//                val file = psiFileFactory.createFileFromText(
//                        it.virtualFilePath,
//                        it.language,
//                        it.text
//                )
//                it.processChildren {  }
//                ServiceManager
                val copied = it.copy()
                copied.accept(OurVisitor())
            }
//            processFile(it)
        }
        return AnalysisResult.RetryWithAdditionalRoots(
                bindingContext = bindingTrace.bindingContext,
                additionalJavaRoots = emptyList(),
                additionalKotlinRoots = emptyList(),
                moduleDescriptor = module,
                addToEnvironment = true
        )
    }
}