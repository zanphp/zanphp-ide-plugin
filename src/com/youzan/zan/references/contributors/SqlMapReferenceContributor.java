package com.youzan.zan.references.contributors;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.youzan.zan.references.providers.SqlMapReferenceProvider;
import org.jetbrains.annotations.NotNull;

public class SqlMapReferenceContributor extends PsiReferenceContributor {

    private static final Logger log = Logger.getInstance(SqlMapReferenceContributor.class);

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar psiReferenceRegistrar) {

        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(StringLiteralExpression.class).withLanguage(PhpLanguage.INSTANCE),
                new SqlMapReferenceProvider()
        );
    }
}
