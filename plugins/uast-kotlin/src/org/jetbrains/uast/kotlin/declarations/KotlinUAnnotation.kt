package org.jetbrains.uast.kotlin

import com.intellij.psi.PsiClass
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.annotationClass
import org.jetbrains.kotlin.resolve.source.getPsi
import org.jetbrains.uast.*

class KotlinUAnnotation(
        override val psi: KtAnnotationEntry,
        override val uastParent: UElement?
) : UAnnotation {
    private val resolvedAnnotation by lz { psi.analyze()[BindingContext.ANNOTATION, psi] }

    override val qualifiedName: String?
        get() = resolvedAnnotation?.fqName?.asString()

    override val attributeValues: List<UNamedExpression> by lz {
        val argumentsWithoutName = psi.valueArguments.filter { it.getArgumentName() == null }
        val valueNamedExpression: UNamedExpression? = when {
            argumentsWithoutName.size == 1 ->
                KotlinUNamedExpression.create(argumentsWithoutName.single(), this)
            argumentsWithoutName.size > 1 ->
                KotlinUNamedExpression.create(argumentsWithoutName.mapNotNull { it.getArgumentExpression() }, this)
            else -> null
        }

        val namedExpressions = psi.valueArguments
                .filter { it.getArgumentName() != null }
                .map { KotlinUNamedExpression.create(it, this) }

        valueNamedExpression?.let { namedExpressions + it } ?: namedExpressions
    }

    override fun resolve(): PsiClass? {
        val descriptor = resolvedAnnotation?.annotationClass ?: return null
        return descriptor.toSource()?.getMaybeLightElement(this) as? PsiClass
    }

    override fun findAttributeValue(name: String?): UExpression? =
            findDeclaredAttributeValue(name)

    override fun findDeclaredAttributeValue(name: String?): UExpression? {
        return attributeValues.find {
            it.name == name ||
            (name == null && it.name == "value") ||
            (name == "value" && it.name == null)
        }?.expression
    }

}

