package com.sahiljaroli.composeoptimizer

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.*

class ComposeOptimizerInspection : AbstractKotlinInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : KtVisitorVoid() {
            override fun visitNamedFunction(function: KtNamedFunction) {
                super.visitNamedFunction(function)
                if (function.annotationEntries.any { it.shortName?.asString() == "Composable" }) {
                    function.valueParameters.forEach { param ->
                        val typeText = param.typeReference?.text ?: ""
                        if (typeText.startsWith("ArrayList") || typeText.startsWith("MutableList")) {
                            holder.registerProblem(
                                param,
                                "Compose Optimizer: Mutable collections ($typeText) cause unnecessary recompositions. Use List or persistentListOf."
                            )
                        }
                    }
                }
            }

            override fun visitCallExpression(expression: KtCallExpression) {
                super.visitCallExpression(expression)
                val callee = expression.calleeExpression?.text
                if (callee == "mutableStateOf" || callee == "mutableIntStateOf") {
                    val parent = expression.parent
                    val isRemembered = parent is KtCallExpression && parent.calleeExpression?.text == "remember"
                    if (!isRemembered) {
                        holder.registerProblem(
                            expression,
                            "Compose Optimizer: State created without 'remember' will be lost on recomposition.",
                            RememberQuickFix()
                        )
                    }
                }
                if (callee == "sleep" || callee == "readText" || callee == "Thread.sleep") {
                    holder.registerProblem(
                        expression,
                        "Compose Optimizer: Blocking I/O call detected in composition phase. Move to a LaunchedEffect."
                    )
                }
            }
        }
    }

    
}