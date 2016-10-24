package com.youzan.zan.completition.lookups;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.youzan.zan.elements.ApiConfig;
import org.jetbrains.annotations.NotNull;

public class ClientCallLookupElement extends LookupElement {
    private ApiConfig.Mod mod;

    public ClientCallLookupElement(ApiConfig.Mod mod) {
        this.mod = mod;
    }

    @NotNull
    @Override
    public String getLookupString() {
        String str = mod.getMod();
        return str == null ? "" : str;
    }

    @Override
    public void renderElement(LookupElementPresentation presentation) {
        presentation.setItemText(mod.getMod());
//        presentation.setIcon(icon);
//        presentation.setTypeText("application component");
//        presentation.setTypeGrayed(false);
    }

}
