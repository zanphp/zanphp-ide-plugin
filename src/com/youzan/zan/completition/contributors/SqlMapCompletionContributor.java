package com.youzan.zan.completition.contributors;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.diagnostic.Logger;
import com.youzan.zan.completition.providers.SqlMapCompletionProvider;
import com.youzan.zan.helper.PatternHelper;

public class SqlMapCompletionContributor extends CompletionContributor {

    private final static Logger log = Logger.getInstance(SqlMapCompletionContributor.class);

    public SqlMapCompletionContributor() {
        extend(CompletionType.BASIC, PatternHelper.stringInSqlMapMethod("execute"), new SqlMapCompletionProvider());
    }
}
