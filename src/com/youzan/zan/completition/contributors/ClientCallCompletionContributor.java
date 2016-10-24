package com.youzan.zan.completition.contributors;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.diagnostic.Logger;
import com.youzan.zan.completition.providers.ClientCallCompletionProvider;
import com.youzan.zan.helper.PatternHelper;

public class ClientCallCompletionContributor extends CompletionContributor {

    private final static Logger log = Logger.getInstance(ClientCallCompletionContributor.class);

    public ClientCallCompletionContributor() {
        extend(CompletionType.BASIC, PatternHelper.stringInClientCallMethod("call"), new ClientCallCompletionProvider());
    }
}
