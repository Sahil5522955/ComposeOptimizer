package com.sahiljaroli.composeoptimizer

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.psi.KtPsiFactory

class RememberQuickFix : LocalQuickFix {
    override fun getFamilyName(): String = "Wrap with remember { }"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement
        val factory = KtPsiFactory(project)
        val newExpressionText = "remember { ${element.text} }"
        try {
            val newExpression = factory.createExpression(newExpressionText)
            element.replace(newExpression)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}