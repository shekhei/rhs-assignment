package io.github.shekhei

import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.asJava.UltraLightClassModifierExtension
import org.jetbrains.kotlin.asJava.classes.KtUltraLightClass
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtDeclaration

class RhsAssignmentUltraLightClassBuilder :
        UltraLightClassModifierExtension {

    private fun isSuitableDeclaration(declaration: KtDeclaration): Boolean {
        return true
    }

    override fun interceptMethodsBuilding(
            declaration: KtDeclaration,
            descriptor: Lazy<DeclarationDescriptor?>,
            containingDeclaration: KtUltraLightClass,
            methodsList: MutableList<KtLightMethod>
    ) {
        methodsList.forEach {
            processChildren(it) { text: String ->
                PsiFileFactory.getInstance(it.containingFile.manager.project).createFileFromText(KotlinLanguage.INSTANCE, text)
            }
        }
    }
}