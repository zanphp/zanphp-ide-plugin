package com.youzan.zan;

import com.google.gson.Gson;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.youzan.zan.elements.ApiConfig;
import com.youzan.zan.elements.SqlMapConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZanConfig {
    private final static Logger log = Logger.getInstance(ZanConfig.class);

    private static Map<Project, Map<String, ApiConfig.Mod>> apiConfigs = new HashMap<>();
    private static Map<Project, SqlMapConfig> sqlMapConfigs = new HashMap<>();


    private static final String apiConfigPath = "vendor/zanphp/zan/src/Network/Common/ApiConfig.php";

    @Nullable
    public static Map<String, ApiConfig.Mod> getApiConfig(@NotNull final Project project)  {
        String path = project.getBasePath();
        if (path == null) {
            return null;
        }
        apiConfigs.putIfAbsent(project, parseApiConfig(path + "/" + apiConfigPath));
        return apiConfigs.get(project);
    }

    @Nullable
    public static SqlMapConfig getSqlMapConfig(@NotNull final Project project) {
        String path = project.getBasePath();
        if (path == null) {
            return null;
        }
        sqlMapConfigs.putIfAbsent(project, parseSqlMapConfig(path));
        return sqlMapConfigs.get(project);
    }

    public static SqlMapConfig parseSqlMapConfig(@NotNull final String basePath) {
        StringBuilder sb = new StringBuilder();
        sb.append("$basePath = \"" + basePath + "\";\n");
        sb.append("require $basePath . \"/vendor/autoload.php\";\n");
        sb.append("\\Zan\\Framework\\Foundation\\Core\\Path::init($basePath);\n");
        sb.append("$sqlPath = \\Zan\\Framework\\Foundation\\Core\\Path::getSqlPath();\n");
        sb.append("if (is_dir($sqlPath)) {\n");
        sb.append("    $sqlMaps = \\Zan\\Framework\\Foundation\\Core\\ConfigLoader::getInstance()->loadDistinguishBetweenFolderAndFile($sqlPath);\n");
        sb.append("    if (null == $sqlMaps || [] == $sqlMaps) {\n");
        sb.append("        echo \"{}\";\n");
        sb.append("    } else {\n");
        sb.append("        $ret = [];\n");
        sb.append("        foreach ($sqlMaps as $file => $sqlMap) {\n");
        sb.append("            foreach ($sqlMap as $sid => $sql) {\n");
        sb.append("                if($sid === 'table') continue;");
        sb.append("                $ret[\"$file.$sid\"] = $sql;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("        echo json_encode($ret);\n");
        sb.append("    }\n");
        sb.append("} else {\n");
        sb.append("    echo \"{}\";\n");
        sb.append("}\n");

        String json = $.execPhp(sb.toString());
        if (json.isEmpty()) {
            return new SqlMapConfig();
        }

        log.info(json);
        return new Gson().fromJson(json, SqlMapConfig.class);
    }

    @Nullable
    public static Map<String, ApiConfig.Mod> parseApiConfig (@NotNull final String path) {
        Map<String, ApiConfig.Mod> mods = new HashMap<>();

        if (!$.fileExist(path)) {
            return mods;
        }

        String code = "print(json_encode(require(\"" + path + "\")));";
        String json = $.execPhp(code);

        if (json.isEmpty()) {
            return mods;
        }

        log.info(json);

        // TODO 重新定义ApiConfig实体
        // TODO 配置整理逻辑修改成PHP代码
        ApiConfig apiConfig = new Gson().fromJson(json, ApiConfig.class);

        for (Map.Entry<String, ApiConfig.Mod> item : apiConfig.getTest().entrySet()) {
            ApiConfig.Mod mod = item.getValue();
            if (mod.getSub() == null) {
                String modKey = mod.getMod() == null ? item.getKey() : mod.getMod();
                mods.put(modKey, mod);
            } else {
                List<ApiConfig.Mod> subMods = mod.getSub();
                for (ApiConfig.Mod subMod : subMods) {
                    if (subMod.getMod() == null) {
                        subMod.setMod(mod.getMod());
                    }
                    if (subMod.getHost() == null) {
                        subMod.setHost(mod.getHost());
                    }
                    if (subMod.getType() == null) {
                        subMod.setType(mod.getType());
                    }
                    if (subMod.getMod() == null) {
                        continue;
                    }
                    mods.put(subMod.getMod(), subMod);
                }
            }
        }
        return mods;
    }
}
