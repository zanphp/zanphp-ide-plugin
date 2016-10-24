package com.youzan.zan.completition.providers;

import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import com.youzan.zan.$;
import com.youzan.zan.ZanConfig;
import com.youzan.zan.completition.lookups.SqlMapLookupElement;
import com.youzan.zan.elements.SqlMapConfig;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.nio.file.*;

public class SqlMapCompletionProvider<CompletionParameters> extends CompletionProvider {
    private static final Logger log = Logger.getInstance(SqlMapCompletionProvider.class);

    private Project project;
    private CompletionResultSet completionResultSet;
    private SqlMapConfig sqlMapConfig;

    @Override
    protected void addCompletions(@NotNull com.intellij.codeInsight.completion.CompletionParameters completionParameters,
                                  ProcessingContext processingContext,
                                  @NotNull CompletionResultSet completionResultSet) {


        this.completionResultSet = completionResultSet;

        PsiFile currentFile = completionParameters.getPosition().getContainingFile();
        project = currentFile.getProject();

        sqlMapConfig = ZanConfig.getSqlMapConfig(project);

        if (sqlMapConfig != null && sqlMapConfig.size() > 0) {
            sqlMapConfig.forEach((sid, sql) -> completionResultSet.addElement(new SqlMapLookupElement(sid, sql)));
            watchSqlMap();
            // wtf();
        }
    }

    // TODO 这里只处理了添加，未处理移除，木有接口！！！！
    // TODO 貌似可以用反射处理下！！！！
    // https://github.com/JetBrains/intellij-community/blob/master/platform/lang-impl/src/com/intellij/codeInsight/completion/impl/CompletionServiceImpl.java
    // !!!监听sqlMap文件变动
    private void watchSqlMap() {
        new Thread(() -> {
            try {
                String basePath = project.getBasePath();
                String sqlMapPath = project.getBasePath() + "/resource/sql";
                if (basePath == null || !$.fileExist(sqlMapPath)) {
                    return;
                }

                WatchService watchService= FileSystems.getDefault().newWatchService();

                Path dir = Paths.get(project.getBasePath() + "/resource/sql");

                final WatchKey watchKey = dir.register(watchService,
                        StandardWatchEventKinds.OVERFLOW,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);

                while(true)
                {
                    final WatchKey wk = watchService.take();
                    if (!$.fileExist(sqlMapPath)) {
                        continue;
                    }

                    // 增量更新, 这里没有处理移除情况
                    // for(WatchEvent<?> event : wk.pollEvents()) {}
                    ZanConfig.parseSqlMapConfig(basePath).forEach((sid, sql) -> {
                        if (sqlMapConfig.get(sid) == null) {
                            sqlMapConfig.put(sid, sql);
                            completionResultSet.addElement(new SqlMapLookupElement(sid, sql));
                        }
                    });

                    boolean valid = wk.reset();
                    if(!valid)
                    {
                        // "Key has been unregisterede";
                        break;
                    }
                }

            } catch (Throwable t) {
                log.warn(t);
            }
        }).start();
    }

    private void wtf() {
        Field[] fields = completionResultSet.getClass().getDeclaredFields();
//        Optional<Field> next = Arrays.stream(fields).filter(f -> {
//            try {
//                f.setAccessible(true);
//                Object value = f.get(completionResultSet);
//                if (value == null) {
//                    return false;
//                }
//                if (value instanceof CompletionResultSet) {
//                    return true;
//                }
//            } catch (IllegalAccessException e) {
//                return false;
//            }
//            return false;
//        }).findFirst();

//        for (int i = 0; i < fields.length; i++) {
//            log.info(fields[i].getDeclaringClass().getName());
//            log.info(fields[i].getName());
//            fields[i].setAccessible(true);
//            try {
//                Object value = fields[i].get(completionResultSet);
//                if (value != null) {
//                    log.info(value.toString());
//                }
//            } catch (IllegalAccessException e) {
//                log.info(e);
//            }
//            log.info("==========================");
//        }
    }
}
