package com.youzan.zan;

import com.google.gson.Gson;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.youzan.zan.elements.SqlMapConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ZanConfig {
    private final static Logger log = Logger.getInstance(ZanConfig.class);

    private static Map<Project, SqlMapConfig> sqlMapConfigs = new HashMap<>();


    private static final String apiConfigPath = "vendor/zanphp/zan/src/Network/Common/ApiConfig.php";


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


        log.debug(sb.toString());
        String json = $.execPhp(sb.toString());
        if (json.isEmpty()) {
            return new SqlMapConfig();
        }

        log.debug(json);
        return new Gson().fromJson(json, SqlMapConfig.class);
    }
}
