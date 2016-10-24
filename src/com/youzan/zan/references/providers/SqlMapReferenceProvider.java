package com.youzan.zan.references.providers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.youzan.zan.$;
import com.youzan.zan.references.SqlMapReference;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SqlMapReferenceProvider extends PsiReferenceProvider {
    private static final Logger log = Logger.getInstance(SqlMapReferenceProvider.class);

    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(
            @NotNull PsiElement psiElement,
            @NotNull ProcessingContext processingContext) {

        PsiElement variableContext = psiElement.getContext();
        if(!(variableContext instanceof ParameterList)) {
            return PsiReference.EMPTY_ARRAY;
        }

        ParameterList parameterList = (ParameterList) variableContext;
        if (!(parameterList.getContext() instanceof MethodReference)) {
            return PsiReference.EMPTY_ARRAY;
        }

        // Db::execute(parameters...)
        MethodReference methodReference = (MethodReference)parameterList.getContext();

        String methodName = methodReference.getName();
        if (!Objects.equals(methodName, "execute")) {
            return PsiReference.EMPTY_ARRAY;
        }

        PhpExpression classReference = methodReference.getClassReference();
        if (classReference == null) {
            return PsiReference.EMPTY_ARRAY;
        }

        String className = classReference.getName();
        if (className == null) {
            return PsiReference.EMPTY_ARRAY;
        }

        if (!className.equalsIgnoreCase("Db")) {
            return PsiReference.EMPTY_ARRAY;
        }

        PsiElement[] parameters = parameterList.getParameters();
        if (parameters.length == 0) {
            return PsiReference.EMPTY_ARRAY;
        }

        PsiElement firstParameter = parameters[0];
        if (!psiElement.equals(firstParameter)) {
            return PsiReference.EMPTY_ARRAY;
        }

        // 去除字符串类型参数引号
        String sid = firstParameter.getText().trim();
        sid = sid.substring(1, sid.length() - 1).trim();
        return new PsiReference[] {
                new SqlMapReference((StringLiteralExpression) psiElement, sid)
        };
    }
}
