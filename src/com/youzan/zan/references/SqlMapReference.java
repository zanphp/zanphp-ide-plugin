package com.youzan.zan.references;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SqlMapReference extends PsiReferenceBase {
    private static final Logger log = Logger.getInstance(SqlMapReference.class);

    private final String sid;

    public SqlMapReference(@NotNull StringLiteralExpression element, final String sid) {
        super(element);
        this.sid = sid;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        Project project = myElement.getProject();

        // 定位sqlmapphp文件
        VirtualFile baseDir = project.getBaseDir();
        VirtualFile sqlFile = null;
        String[] keys = sid.split("\\.");
        String sqlPath = "/resource/sql";
        // sqlmap 文件内数组key
        String sqlMapKey = "";
        for (int i = 0; i < keys.length; i++) {
            sqlPath = sqlPath + "/" + keys[i];

            VirtualFile tmpFile = baseDir.findFileByRelativePath(sqlPath);
            if (tmpFile == null) {
                tmpFile = baseDir.findFileByRelativePath(sqlPath + ".php");
            }

            if (tmpFile == null) {
                return null;
            }

            if (!tmpFile.isDirectory()) {
                sqlFile = tmpFile;
                for (i++; i < keys.length; i++) {
                    sqlMapKey += keys[i];
                }
                break;
            }
        }



        if (sqlFile == null || sqlMapKey.isEmpty()) {
            return null;
        }

        PsiFile psiFile = PsiManager.getInstance(project).findFile(sqlFile);
        final String sqlMapKeyFinal = sqlMapKey;


        // 递归查找结点
        class FindSqlMapKey extends PsiRecursiveElementWalkingVisitor {
            // PsiIdentifier id;
            private PsiElement result;
            public void visitElement(PsiElement element) {
                if (Objects.equals(element.getText(), sqlMapKeyFinal)) {
                    result = element;
                    stopWalking();
                }
                super.visitElement(element);
            }
        }
        FindSqlMapKey finder = new FindSqlMapKey();
        finder.visitFile(psiFile);
        return finder.result;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}