# M-Weather

M-Weather 是一款以多天气源融合为核心的 Android 天气应用。它使用 Jetpack Compose 和 Material 3 构建，重点放在快速展示天气实况、可配置首页卡片、动态天气背景、个性化建议，以及国内外不同天气源的组合兜底。

## 功能概览

- 多天气源融合：国内优先彩云天气，国外支持 Open-Meteo、MSN、met.no、NWS 等源参与补充。
- 首页模块化：大温度、每日预报、逐小时预报、降雨、风速、空气质量、紫外线、湿度、气压等模块可调整顺序。
- 天气详情页：支持每日和逐小时预报详情、左右滑动切换日期/小时、来源信息和融合说明。
- 动态体验：天气背景、下拉刷新、主题扩散动画、卡片拖动、可选震动反馈。
- 设置与调试：天气源配置、温度单位、主题模式、语言字符串导入导出、预警场景测试入口。
- 小工具页：航图和实时航迹入口，通过 WebView 加载对应网页/SDK 页面。

## 技术栈

- Kotlin
- Jetpack Compose
- Material 3
- MVI 状态管理
- OkHttp
- Android Gradle Plugin

## 本地配置

项目会从根目录 `local.properties` 读取可选 API Key / Host，并通过 `BuildConfig` 注入。`local.properties` 已被 `.gitignore` 忽略，不应提交到仓库。

常用字段示例：

```properties
baiduIpLocationApiKey=  # 兼容 baiduMapApiKey / baiduAk / baiduApiKey
amapApiKey=
qWeatherApiKey=
qWeatherApiHost=
seniverseApiKey=
openWeatherApiKey=
visualCrossingApiKey=
meteostatRapidApiKey=
aiAdviceApiKey=
aiAdviceEndpoint=
```

彩云天气和 MSN 天气使用内置默认 endpoint 参数，不需要用户在本地配置或设置页填写 Key；设置页会隐藏这些内置参数。

## 构建与验证

在项目根目录执行：

```powershell
$env:ANDROID_USER_HOME='D:\andr_appli\.android'
.\gradlew.bat --gradle-user-home D:\andr_appli\.gradle "-Pkotlin.compiler.execution.strategy=in-process" testDebugUnitTest lintDebug assembleDebug
```

Debug APK 输出位置：

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 仓库说明

这是个人开发中的私有项目仓库。预报数据仅供生活参考，灾害性天气请以当地气象部门正式发布的信息为准。