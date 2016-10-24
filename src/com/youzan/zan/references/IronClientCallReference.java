package com.youzan.zan.references;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.youzan.zan.$;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IronClientCallReference extends PsiReferenceBase {
    private static final Logger log = Logger.getInstance(IronClientCallReference.class);

    private final String callPath;

    public IronClientCallReference(@NotNull StringLiteralExpression element, final String callPath) {
        super(element);
        this.callPath = callPath;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        Project project = myElement.getProject();
        StringBuilder apiPath = new StringBuilder("v2/api");
        String basePath = project.getBasePath();

        String[] keys = callPath.split("\\.");
        if (keys.length < 3) {
            return null;
        }

        apiPath.append("/");
        // 第一部分为模块名
        apiPath.append(keys[0]);
        apiPath.append("/controllers");

        // 倒数两部分为文件名与方法名
        for (int i = 1; i < keys.length - 2; i++) {
            apiPath.append("/");
            apiPath.append(keys[i]);
        }
        // 文件名 首字母大写
        apiPath.append("/");
        apiPath.append(Character.toUpperCase(keys[keys.length - 2].charAt(0)));
        apiPath.append(keys[keys.length - 2].substring(1));
        apiPath.append(".php");

        String apiFilePath = apiPath.toString();
        if (!$.fileExist(basePath + "/" + apiFilePath)) {
            // TODO 提示 basePath + "/" + apiFilePath 路径不存在
            return null;
        }

        VirtualFile apiFile = project.getBaseDir().findFileByRelativePath(apiFilePath);
        if (apiFile == null) {
            return null;
        }
        PsiFile psiFile = PsiManager.getInstance(project).findFile(apiFile);
        final String method = keys[keys.length - 1];

        // 递归查找结点
        class FindApiMethod extends PsiRecursiveElementWalkingVisitor {
            // PsiIdentifier id;
            private PsiElement result;
            public void visitElement(PsiElement element) {
                if (Objects.equals(element.getText(), method)) {
                    result = element;
                    stopWalking();
                }
                super.visitElement(element);
            }
        }
        FindApiMethod finder = new FindApiMethod();
        finder.visitFile(psiFile);

        return finder.result;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}