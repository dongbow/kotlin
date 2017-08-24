/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.uast.kotlin

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.uast.*

class KotlinAnnotationArrayInitializerUCallExpression(
        expressions: List<KtExpression>,
        override val uastParent: UElement?
) : KotlinAbstractUExpression(), UCallExpression {
    override val kind: UastCallKind = UastCallKind.NESTED_ARRAY_INITIALIZER

    override val valueArguments: List<UExpression> by lz {
        expressions.map { getLanguagePlugin().convert<UExpression>(it, this) }
    }

    override val psi: PsiElement?  = null

    override val methodIdentifier: UIdentifier? = null

    override val classReference: UReferenceExpression? = null

    override val methodName: String? = null

    override val valueArgumentCount: Int = valueArguments.size

    override val typeArgumentCount: Int = 0

    override val typeArguments: List<PsiType> = emptyList()

    override val returnType: PsiType? = null

    override fun resolve() = null

    override val receiver: UExpression? = null

    override val receiverType: PsiType? = null
}