package com.weathermixer.sixq

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

private const val VariflightAdsbUrl = "https://flightadsb.variflight.com/#/home"
private const val KunyuSdkBaseUrl = "https://aips.siniswift.com/"
private const val DesktopChromeUserAgent =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
        "(KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"

@Composable
internal fun ToolsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SettingsSubpageHeader(title = "小工具", onBack = onBack)
        AviationChartCard()
        FlightTrackCard()
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun AviationChartCard() {
    var reloadKey by remember { mutableIntStateOf(0) }
    WebToolCard(
        title = "航图",
        icon = {
            Icon(Icons.Filled.Map, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        onReload = { reloadKey++ },
    ) {
        var webView by remember { mutableStateOf<WebView?>(null) }

        DisposableEffect(Unit) {
            onDispose {
                webView?.destroy()
                webView = null
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webView = this
                    configureInteractiveWebView(
                        useWideViewport = false,
                        loadWithOverviewMode = false,
                        webPageZoomEnabled = false,
                    )
                    tag = reloadKey
                    loadKunyuMapHtml()
                }
            },
            update = { view ->
                webView = view
                if (view.tag != reloadKey) {
                    view.tag = reloadKey
                    view.loadKunyuMapHtml()
                }
            },
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun FlightTrackCard() {
    var reloadKey by remember { mutableIntStateOf(0) }
    WebToolCard(
        title = "航迹",
        icon = {
            Icon(Icons.Filled.FlightTakeoff, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        onReload = { reloadKey++ },
    ) {
        var webView by remember { mutableStateOf<WebView?>(null) }

        DisposableEffect(Unit) {
            onDispose {
                webView?.destroy()
                webView = null
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webView = this
                    configureInteractiveWebView(
                        useWideViewport = true,
                        loadWithOverviewMode = true,
                        webPageZoomEnabled = false,
                    )
                    settings.userAgentString = DesktopChromeUserAgent
                    tag = reloadKey
                    loadUrl(VariflightAdsbUrl)
                }
            },
            update = { view ->
                webView = view
                view.settings.userAgentString = DesktopChromeUserAgent
                if (view.tag != reloadKey) {
                    view.tag = reloadKey
                    view.loadUrl(VariflightAdsbUrl)
                }
            },
        )
    }
}

private fun WebView.loadKunyuMapHtml() {
    loadDataWithBaseURL(
        KunyuSdkBaseUrl,
        kunyuMapHtml(),
        "text/html",
        "UTF-8",
        null,
    )
}

private fun kunyuMapHtml(): String = """
<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, viewport-fit=cover">
  <link rel="stylesheet" href="sdk/sdk.style.css">
  <style>
    html, body, #map {
      width: 100%;
      height: 100%;
      margin: 0;
      padding: 0;
      overflow: hidden;
      background: #edf3f8;
      touch-action: none;
      overscroll-behavior: none;
      font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
    }
    #map canvas, #map .mapboxgl-canvas {
      touch-action: none;
    }
    .layer-panel {
      position: absolute;
      top: 8px;
      right: 8px;
      z-index: 5;
      display: grid;
      gap: 6px;
      max-width: calc(100% - 16px);
      padding: 8px;
      border-radius: 12px;
      color: #dcecff;
      background: rgba(10, 16, 24, 0.72);
      backdrop-filter: blur(8px);
    }
    .layer-button {
      border: 0;
      border-radius: 9px;
      padding: 6px 9px;
      color: #0f1720;
      background: #c9e6ff;
      font-size: 12px;
      font-weight: 650;
    }
    .layer-button.off {
      color: #dcecff;
      background: rgba(255, 255, 255, 0.14);
    }
    .status {
      position: absolute;
      left: 10px;
      bottom: 10px;
      z-index: 6;
      max-width: calc(100% - 20px);
      padding: 7px 9px;
      border-radius: 10px;
      color: #e8f2ff;
      background: rgba(10, 16, 24, 0.72);
      font-size: 12px;
      line-height: 1.35;
      pointer-events: none;
    }
    .status.ready {
      opacity: 0;
      transition: opacity 420ms ease;
    }
    .status.error {
      color: #ffd2d2;
      opacity: 1;
    }
  </style>
</head>
<body>
  <div id="map"></div>
  <div id="layers" class="layer-panel"></div>
  <div id="status" class="status">航图加载中</div>
  <script src="sdk/nav-sdk.min.js"></script>
  <script>
    (function () {
      const status = document.getElementById('status');
      const layerPanel = document.getElementById('layers');
      const layerItems = [
        { id: 'amm', label: '底图', checked: true },
        { id: 'airline', label: '航路', checked: true },
        { id: 'airport', label: '机场', checked: true },
        { id: 'vor', label: 'VOR', checked: true },
        { id: 'ndb', label: 'NDB', checked: true },
        { id: 'controlled', label: '管制', checked: false },
        { id: 'restricted', label: '限制', checked: false },
        { id: 'airspace', label: '空域', checked: true }
      ];
      let mapInstance = null;
      let readyOnce = false;

      function writeStatus(text, isError) {
        status.textContent = text;
        status.className = isError ? 'status error' : 'status ready';
      }

      function setLayer(id, visible) {
        if (!mapInstance) return;
        try {
          if (visible && typeof mapInstance.addBuiltInLayer === 'function') {
            mapInstance.addBuiltInLayer(id);
          }
          if (typeof mapInstance.toggleBuiltInLayer === 'function') {
            mapInstance.toggleBuiltInLayer(id, visible);
          } else if (typeof mapInstance.setLayerVisible === 'function') {
            mapInstance.setLayerVisible(id, visible);
          }
        } catch (error) {
          console.warn('Kunyu layer failed:', id, error);
        }
      }

      function buildLayerPanel() {
        layerPanel.innerHTML = '';
        layerItems.forEach(function (item) {
          const button = document.createElement('button');
          button.className = 'layer-button' + (item.checked ? '' : ' off');
          button.textContent = item.label;
          button.addEventListener('click', function () {
            item.checked = !item.checked;
            button.className = 'layer-button' + (item.checked ? '' : ' off');
            setLayer(item.id, item.checked);
          });
          layerPanel.appendChild(button);
        });
      }

      function markReady() {
        if (readyOnce) return;
        readyOnce = true;
        layerItems.forEach(function (item) { setLayer(item.id, item.checked); });
        writeStatus('坤舆航图', false);
      }

      function boot() {
        if (!window.navMap || !window.navMap.MapSDK) {
          writeStatus('航图 SDK 加载中', false);
          window.setTimeout(boot, 240);
          return;
        }
        buildLayerPanel();
        try {
          mapInstance = new window.navMap.MapSDK({
            container: 'map',
            center: [105, 35],
            zoom: 4,
            maxZoom: 16.5
          });
          if (typeof mapInstance.on === 'function') {
            mapInstance.on('loadComplete', markReady);
            mapInstance.on('error', function (error) {
              writeStatus('航图加载失败：' + (error && error.message ? error.message : '地图资源不可用'), true);
            });
          }
          window.setTimeout(markReady, 1800);
        } catch (error) {
          writeStatus('航图初始化失败：' + (error && error.message ? error.message : error), true);
        }
      }

      window.addEventListener('error', function (event) {
        writeStatus('航图脚本错误：' + event.message, true);
      });
      boot();
    })();
  </script>
</body>
</html>
""".trimIndent()

@Composable
private fun WebToolCard(
    title: String,
    icon: @Composable () -> Unit,
    onReload: () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                icon()
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                IconButton(onClick = onReload) {
                    Icon(Icons.Filled.Refresh, contentDescription = "重新加载")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(520.dp)
                    .clip(RoundedCornerShape(18.dp)),
            ) {
                content()
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebView.configureInteractiveWebView(
    useWideViewport: Boolean,
    loadWithOverviewMode: Boolean,
    webPageZoomEnabled: Boolean,
) {
    WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
    CookieManager.getInstance().setAcceptCookie(true)
    webViewClient = object : WebViewClient() {
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?,
        ) {
            super.onReceivedError(view, request, error)
            if (request?.isForMainFrame == true && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                view?.loadDataWithBaseURL(
                    request.url?.toString(),
                    errorHtml("网页加载失败", error?.description?.toString().orEmpty().ifBlank { "网络或 WebView 组件异常" }),
                    "text/html",
                    "UTF-8",
                    null,
                )
            }
        }
    }
    webChromeClient = object : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            if (BuildConfig.DEBUG) {
                android.util.Log.w(
                    "WeatherWebTool",
                    "${consoleMessage?.message()} @ ${consoleMessage?.sourceId()}:${consoleMessage?.lineNumber()}",
                )
            }
            return super.onConsoleMessage(consoleMessage)
        }

        override fun onGeolocationPermissionsShowPrompt(
            origin: String?,
            callback: GeolocationPermissions.Callback?,
        ) {
            callback?.invoke(origin, true, false)
        }

        override fun onPermissionRequest(request: PermissionRequest?) {
            request?.deny()
        }
    }
    setLayerType(View.LAYER_TYPE_HARDWARE, null)
    isVerticalScrollBarEnabled = false
    isHorizontalScrollBarEnabled = false
    setInitialScale(100)
    setOnTouchListener { view, event ->
        if (
            event.actionMasked == MotionEvent.ACTION_DOWN ||
            event.actionMasked == MotionEvent.ACTION_MOVE ||
            event.pointerCount > 1
        ) {
            view.parent?.requestDisallowInterceptTouchEvent(true)
        }
        false
    }
    settings.javaScriptEnabled = true
    settings.javaScriptCanOpenWindowsAutomatically = true
    settings.domStorageEnabled = true
    settings.cacheMode = WebSettings.LOAD_DEFAULT
    settings.mediaPlaybackRequiresUserGesture = false
    settings.loadWithOverviewMode = loadWithOverviewMode
    settings.useWideViewPort = useWideViewport
    settings.textZoom = 100
    settings.setGeolocationEnabled(true)
    settings.allowContentAccess = true
    settings.allowFileAccess = true
    settings.loadsImagesAutomatically = true
    settings.blockNetworkImage = false
    settings.setSupportMultipleWindows(false)
    settings.setSupportZoom(webPageZoomEnabled)
    settings.builtInZoomControls = webPageZoomEnabled
    settings.displayZoomControls = false
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        settings.offscreenPreRaster = true
    }
}

private fun errorHtml(title: String, message: String): String = """
<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <style>
    body {
      margin: 0;
      min-height: 100vh;
      display: grid;
      place-items: center;
      padding: 20px;
      box-sizing: border-box;
      color: #e8f2ff;
      background: #0b1117;
      font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
    }
    .box {
      width: 100%;
      border-radius: 16px;
      padding: 16px;
      background: rgba(33, 48, 65, 0.86);
    }
    h1 { margin: 0 0 8px; font-size: 18px; }
    p { margin: 0; font-size: 13px; line-height: 1.5; color: #b8c7d8; word-break: break-word; }
  </style>
</head>
<body>
  <div class="box">
    <h1>${title.escapeHtml()}</h1>
    <p>${message.escapeHtml()}</p>
  </div>
</body>
</html>
""".trimIndent()

private fun String.escapeHtml(): String = buildString(length) {
    for (char in this@escapeHtml) {
        when (char) {
            '&' -> append("&amp;")
            '<' -> append("&lt;")
            '>' -> append("&gt;")
            '"' -> append("&quot;")
            '\'' -> append("&#39;")
            else -> append(char)
        }
    }
}
