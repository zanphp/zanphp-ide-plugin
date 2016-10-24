package com.youzan.zan.helper;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.patterns.StringPattern;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.injection.PhpElementPattern;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.patterns.PhpPatterns;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

public class PatternHelper {

    public static PsiElementPattern.Capture
            stringInMethod(String methodName, StringPattern className) {

        return PlatformPatterns.psiElement(PsiElement.class)
                .withParent(methodLiteralExpression(methodName, className))
                .withLanguage(PhpLanguage.INSTANCE);
    }

    public static PsiElementPattern.Capture
            firstStringInMethod(String methodName, StringPattern className) {

        return PlatformPatterns.psiElement(PsiElement.class)
                .withParent(
                        methodLiteralExpression(methodName, className)
                                .insideStarting(
                                        PlatformPatterns.psiElement()
                                                .withElementType(PhpElementTypes.PARAMETER_LIST)
                                )
                )
                .withLanguage(PhpLanguage.INSTANCE);

    }

    /**
     * php字面量的表达式中的方法参数列表
     * @param methodName
     * @param className
     * @return
     */
    public static PhpElementPattern.Capture<StringLiteralExpression> methodLiteralExpression(String methodName, StringPattern className) {
        return PhpPatterns.phpLiteralExpression()
                .withParent(methodParamsList(methodName, className));
    }

    public static PsiElementPattern.Capture<PsiElement>
        firstMethodParameter(String methodName, StringPattern className) {

        return PlatformPatterns.psiElement()
                .withElementType(PhpElementTypes.PARAMETER_LIST)
                // .withFirstChild(PlatformPatterns.psiElement())
                .withParent(
                        PlatformPatterns.psiElement()
                                .withElementType(PhpElementTypes.METHOD_REFERENCE)
                                .referencing(
                                        PhpPatterns.psiElement()
                                                .withElementType(PhpElementTypes.CLASS_METHOD)
                                                .withName(methodName)
                                                .withParent(PhpPatterns.psiElement().withName(className))
                                )

                );
    }

    /**
     * 方法参数列表
     * @param methodName
     * @param className
     * @return
     */
    public static PsiElementPattern.Capture<PsiElement> methodParamsList(String methodName, StringPattern className) {
        return PlatformPatterns.psiElement()
                .withElementType(PhpElementTypes.PARAMETER_LIST)
                // .withFirstChild(PlatformPatterns.psiElement(PhpElementTypes.STRING))
                .withParent(
                        PlatformPatterns.psiElement()
                                .withElementType(PhpElementTypes.METHOD_REFERENCE)
                                .referencing(
                                        PhpPatterns.psiElement()
                                                .withElementType(PhpElementTypes.CLASS_METHOD)
                                                .withName(methodName)
                                                .withParent(PhpPatterns.psiElement().withName(className))
                                )

                );
    }

    public static PsiElementPattern.Capture<PsiElement>
            paramListInMethodWithName(String methodName) {

        return PlatformPatterns.psiElement()
                .withElementType(PhpElementTypes.PARAMETER_LIST)
                .withParent(
                        PlatformPatterns.psiElement()
                                .withElementType(PhpElementTypes.METHOD_REFERENCE)
                                .referencing(
                                        PhpPatterns.psiElement()
                                                .withElementType(PhpElementTypes.CLASS_METHOD)
                                                .withName(methodName)
                                )
                );
    }

    public static PsiElementPattern.Capture<PsiElement>
            arrayInMethodWithName(String methodName) {

        return PlatformPatterns.psiElement()
                .withElementType(PhpElementTypes.ARRAY_CREATION_EXPRESSION)
                .withParent(
                        PlatformPatterns.psiElement()
                                .withElementType(PhpElementTypes.METHOD_REFERENCE)
                                .referencing(
                                        PhpPatterns.psiElement()
                                                .withElementType(PhpElementTypes.CLASS_METHOD)
                                                .withName(methodName)
                                )
                );
    }


//    private static final ElementPattern<PsiElement> clientCallPatthern = PlatformPatterns.psiElement(PsiElement.class)
//            .withParent(
//                    PlatformPatterns.psiElement(StringLiteralExpression.class)
//                            .withParent(
//                                    PlatformPatterns.psiElement(PhpElementTypes.PARAMETER_LIST)
//                                            .withParent(
//                                                    PlatformPatterns.psiElement(PhpElementTypes.METHOD_REFERENCE)
//                                                            .withText(StandardPatterns.string().contains("call("))
//                                            )
//
//                            )
//            )
//            .withLanguage(PhpLanguage.INSTANCE);

    /**
     * Check element is param is parameterList in method reference
     *
     * @param name
     * @return
     */
    private PsiElementPattern.Capture<PsiElement> isParamListInMethodWithName(String name) {
        return PlatformPatterns.psiElement(PhpElementTypes.PARAMETER_LIST)
                .withParent(
                        PlatformPatterns.psiElement(PhpElementTypes.METHOD_REFERENCE)
                                .withText(StandardPatterns.string().matches(name))
                );
    }

    /**
     * Check file name
     *
     * @param namePattern
     * @return
     */
    private PsiElementPattern.Capture<PsiElement> inFile(StringPattern namePattern) {
        return PlatformPatterns.psiElement(PsiElement.class).inFile(PlatformPatterns.psiFile().withName(namePattern));
    }




    public static PsiElementPattern.Capture firstStringInClientCallMethod(String methodName) {
        return firstStringInMethod(methodName, StandardPatterns.string().oneOf("Client", "\\Zan\\Framework\\Network\\Common\\Client"));
    }

    public static PsiElementPattern.Capture stringInClientCallMethod(String methodName) {
        return stringInMethod(methodName, StandardPatterns.string().oneOf("Client", "\\Zan\\Framework\\Network\\Common\\Client"));
    }

    public static PsiElementPattern.Capture stringInSqlMapMethod(String methodName) {
        return stringInMethod(methodName, StandardPatterns.string().oneOf("Db", "\\Zan\\Framework\\Store\\Facade\\Db"));
    }
}
