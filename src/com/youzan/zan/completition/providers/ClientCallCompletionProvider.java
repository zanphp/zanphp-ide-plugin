package com.youzan.zan.completition.providers;

import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import com.youzan.zan.ZanConfig;
import com.youzan.zan.completition.lookups.ClientCallLookupElement;
import com.youzan.zan.elements.ApiConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ClientCallCompletionProvider<CompletionParameters> extends CompletionProvider {
    private static final Logger log = Logger.getInstance(ClientCallCompletionProvider.class);

    @Override
    protected void addCompletions(@NotNull com.intellij.codeInsight.completion.CompletionParameters completionParameters,
                                  ProcessingContext processingContext,
                                  @NotNull CompletionResultSet completionResultSet) {

        PsiFile currentFile = completionParameters.getPosition().getContainingFile();
        Project project = currentFile.getProject();

        Map<String, ApiConfig.Mod> map = ZanConfig.getApiConfig(project);

        if (map != null && map.size() > 0) {
            map.forEach((k, mod) -> completionResultSet.addElement(new ClientCallLookupElement(mod)));
        }
    }
}
