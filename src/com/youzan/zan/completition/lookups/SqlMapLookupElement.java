package com.youzan.zan.completition.lookups;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.youzan.zan.elements.SqlMapConfig;
import org.jetbrains.annotations.NotNull;

public class SqlMapLookupElement extends LookupElement {
    private String sid;
    private SqlMapConfig.Sql sql;

    public SqlMapLookupElement(String sid, SqlMapConfig.Sql sql) {
        this.sid = sid;
        this.sql = sql;
    }

    @NotNull
    @Override
    public String getLookupString() {
        return sid == null ? "" : sid;
    }

    @Override
    public void renderElement(LookupElementPresentation presentation) {
        presentation.setItemText(sid);
//        presentation.setIcon(icon);
//        presentation.setTypeText("application component");
//        presentation.setTypeGrayed(false);
    }

}
