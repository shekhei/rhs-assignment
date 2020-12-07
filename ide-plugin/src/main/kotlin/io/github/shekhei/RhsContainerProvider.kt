package io.github.shekhei

import com.intellij.codeInsight.ContainerProvider
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.nextLeaf
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.psiUtil.getPrevSiblingIgnoringWhitespaceAndComments

typealias KtFileCreator = (text: String) -> PsiElement

fun createKotlinFile(project: Project): KtFileCreator = { text: String ->
    PsiFileFactory.getInstance(project)
            .createFileFromText(KotlinLanguage.INSTANCE, text)
}

fun findPreviousSibling(el: PsiElement): PsiElement? {
    var el: PsiElement? = el
    while (el != null) {
        val prev = el.getPrevSiblingIgnoringWhitespaceAndComments()
        if (prev != null) return prev
        el = el.parent
    }
    return null
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


class RhsContainerProvider : ContainerProvider {
    override fun getContainer(p0: PsiElement): PsiElement? {
        processChildren(p0) { text: String ->
            PsiFileFactory.getInstance(p0.containingFile.manager.project).createFileFromText(KotlinLanguage.INSTANCE, text)
        }
        return p0
    }
}