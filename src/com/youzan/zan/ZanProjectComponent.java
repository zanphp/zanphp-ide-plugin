package com.youzan.zan;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ZanProjectComponent implements ProjectComponent {
    private final static Logger log = Logger.getInstance(ZanProjectComponent.class);
    private static Project project;
    private static Boolean isEnableZan = null;
    private static Boolean isEnableIron = null;

    public ZanProjectComponent(Project project) {
        ZanProjectComponent.project = project;
    }

    public static boolean isEnableZan() {
        if (isEnableZan == null) {
            isEnableZan = true;
        }
        return isEnableZan;
    }

    public static boolean isEnableIron() {
        if (isEnableIron == null) {
            isEnableIron = $.fileExist(project.getBasePath() + "/v1") && $.fileExist(project.getBasePath() + "/v2");
        }

        return isEnableIron;
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "ZanProjectComponent";
    }

    @Override
    public void projectOpened() {
        isEnableIron = $.fileExist(project.getBasePath() + "/v1") && $.fileExist(project.getBasePath() + "/v2");
        // called when project is opened
    }

    @Override
    public void projectClosed() {
        // called when project is being closed
    }
}
