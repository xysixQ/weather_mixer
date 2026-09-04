# M-Weather

M-Weather 是一款以多天气源融合为核心的 Android 天气应用。风格及部分数据处理方式参考了Breezy Weather，同时添加了部分特色功能。重点放在快速展示天气实况、可配置首页卡片、动态天气背景、个性化建议，以及国内外不同天气源的组合兜底。

## 功能概览

- 多天气源融合：国内优先使用彩云天气，国外支持 Open-Meteo、MSN、met.no、NWS 等。
- 首页模块化：大温度、每日预报、逐小时预报、降雨、风速、空气质量、紫外线、湿度、气压等模块可调整顺序。
- 天气详情页：支持每日和逐小时预报详情。
- 动态体验：优秀的动画、互动及震动反馈。
- 设置：天气源高度自定义、界面语言字符串支持模板化修改。
- 额外：航图和实时航迹入口。

## 本地配置&构建说明

1.拉取源码到本地

2.在local.properties.example中配置自己的sdk位置和API-key。
常用字段示例：
```properties
sdk.dir=（android-sdk的位置，示例：D\:\\Android\\android-sdk）
baiduIpLocationApiKey=
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
(彩云天气和 MSN 天气使用内置默认 endpoint 参数，不需要用户在本地配置或设置页填写 Key；设置页会隐藏这些内置参数。)

！！！最后一定要记得把文件名重命名成local.properties

3.构建APK

（推荐）直接运行根目录的MWeatherBuildAssistant.exe进行构建，仅支持使用jks签名，目前使用ROM私钥签名功能暂不可用。

（传统方法）在项目根目录执行：
```powershell
.\gradlew.bat --no-daemon --stacktrace :app:assembleDebug
```
或
```powershell
.\gradlew.bat --no-daemon --stacktrace :app:assembleRelease
```

APK 输出位置：

```text
app/build/outputs/apk/debug/app-xxx.apk
```
