# Zan IntelliJ Plugin - Alpha

`PHPStorm 2016.1.2` `PHPStorm 2016.2.1`

`IntelliJ IDEA 2016.1` `IntelliJ IDEA 2016.2`

测试通过

## Install

Preference -> Plugins -> Install plugins from disk... -> 选择release/zan-ide-plugin.jar -> restart PhpStorm

## Feature

### 1. Db::execute("sid参数自动补全");
![sid自动补全](http://gitlab.qima-inc.com/php-lib/zan-ide-plugin/raw/master/screenshot/completation_db_execute.gif)

### 2. Db::execute("sid ctrl+mouse 自动跳转到sql文件定义位置")
![sql跳转](http://gitlab.qima-inc.com/php-lib/zan-ide-plugin/raw/master/screenshot/reference_sqlmap.gif)

## 一些问题

1. 调试扩展最最好使用社区版本, 开源, 否则断点调试信息可能不对
2. 将项目module改成plugin: No plugin module specified for configuration： 把项目根目录的*.iml中<module type="JAVA_MODULE" 改成<module type="PLUGIN_MODULE" />
3. 导入com.jetbrains.php package

```
https://confluence.jetbrains.com/display/PhpStorm/Setting-up+environment+for+PhpStorm+plugin+development

1. Open Project Structure File | Project Structure
2. Select Libraries
3. Press Add button
4. Find and select php-openapi.jar and php.jar. They are located in <your_installation_of_PhpStorm/plugins/php/lib.
5. Agree to add the libraries to your Module
6. Open Modules | Dependencies and change Scope to Provided. This step is necessary because otherwise ClassCastException will be thrown because two instances of the library will be loaded via different class loaders
7. Adding dependencies to plugin.xml
    1. Open plugin.xml in the directory META-INF
    2. Add two <depends> items to plugin.xml:
        - <depends>com.jetbrains.php</depends>
        - <depends>com.intellij.modules.platform</depends>
```