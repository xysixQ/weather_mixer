package com.weathermixer.sixq

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import androidx.activity.compose.BackHandler
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
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.OpenInBrowser
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

private const val VariflightAdsbUrl = "https://flightadsb.variflight.com/#/home"
private const val KunyuChartUrl = "https://aips.siniswift.com/doc/examples/map.html"
private const val DesktopChromeUserAgent =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
        "(KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"

private enum class ToolDestination {
    AviationChart,
    FlightTrack,
}

@Composable
internal fun ToolsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    var destination by remember { mutableStateOf<ToolDestination?>(null) }
    BackHandler(enabled = destination != null) {
        destination = null
    }
    AnimatedContent(
        targetState = destination,
        modifier = modifier.fillMaxSize(),
        transitionSpec = {
            val openingTool = targetState != null
            (
                fadeIn(animationSpec = tween(durationMillis = 180)) +
                    slideInHorizontally(animationSpec = tween(durationMillis = 260)) { width ->
                        if (openingTool) width / 5 else -width / 5
                    }
                ) togetherWith (
                fadeOut(animationSpec = tween(durationMillis = 140)) +
                    slideOutHorizontally(animationSpec = tween(durationMillis = 220)) { width ->
                        if (openingTool) -width / 8 else width / 8
                    }
                )
        },
        label = "toolDestination",
    ) { page ->
        when (page) {
            null -> ToolsIndex(
                modifier = Modifier,
                onBack = onBack,
                onOpenAviationChart = { destination = ToolDestination.AviationChart },
                onOpenFlightTrack = { destination = ToolDestination.FlightTrack },
            )
            ToolDestination.AviationChart -> AviationChartPage(
                modifier = Modifier,
                onBack = { destination = null },
            )
            ToolDestination.FlightTrack -> FlightTrackPage(
                modifier = Modifier,
                onBack = { destination = null },
            )
        }
    }
}
@Composable
private fun ToolsIndex(
    modifier: Modifier,
    onBack: () -> Unit,
    onOpenAviationChart: () -> Unit,
    onOpenFlightTrack: () -> Unit,
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
        ToolEntryCard(
            title = "航图",
            subtitle = "坤舆航图 SDK",
            icon = { Icon(Icons.Filled.Map, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            onClick = onOpenAviationChart,
        )
        ToolEntryCard(
            title = "航迹",
            subtitle = "Variflight ADS-B",
            icon = { Icon(Icons.Filled.FlightTakeoff, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            onClick = onOpenFlightTrack,
        )
    }
}

@Composable
private fun ToolEntryCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            icon()
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null)
        }
    }
}

@Composable
private fun AviationChartPage(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    var reloadKey by remember { mutableIntStateOf(0) }
    ToolBrowserPage(
        modifier = modifier,
        title = "航图",
        onBack = onBack,
        onReload = { reloadKey++ },
        externalUrl = KunyuChartUrl,
    ) {
        AviationChartWebView(reloadKey = reloadKey)
    }
}

@Composable
private fun FlightTrackPage(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    var reloadKey by remember { mutableIntStateOf(0) }
    ToolBrowserPage(
        modifier = modifier,
        title = "航迹",
        onBack = onBack,
        onReload = { reloadKey++ },
        externalUrl = VariflightAdsbUrl,
    ) {
        FlightTrackWebView(reloadKey = reloadKey)
    }
}

@Composable
private fun ToolBrowserPage(
    modifier: Modifier,
    title: String,
    onBack: () -> Unit,
    onReload: () -> Unit,
    externalUrl: String,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回小工具")
            }
            Spacer(Modifier.width(6.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            IconButton(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(externalUrl)))
                },
            ) {
                Icon(Icons.Filled.OpenInBrowser, contentDescription = "用浏览器打开")
            }
            IconButton(onClick = onReload) {
                Icon(Icons.Filled.Refresh, contentDescription = "重新加载")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            content()
        }
    }
}
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun AviationChartWebView(reloadKey: Int) {
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
                    postPageLoadScript = kunyuChartFixScript(),
                )
                tag = reloadKey
                loadUrl(KunyuChartUrl)
            }
        },
        update = { view ->
            webView = view
            if (view.tag != reloadKey) {
                view.tag = reloadKey
                view.loadUrl(KunyuChartUrl)
            }
        },
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun FlightTrackWebView(reloadKey: Int) {
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
                    postPageLoadScript = flightTrackViewportFixScript(),
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

private fun kunyuChartFixScript(): String = """
(function () {
  const layerItems = [
    { id: 'amm', label: 'AMM' },
    { id: 'airline', label: '航路' },
    { id: 'airport', label: '机场' },
    { id: 'vor', label: 'VOR' },
    { id: 'ndb', label: 'NDB' },
    { id: 'airspace', label: 'FIR' },
    { id: 'controlled', label: '管制空域' },
    { id: 'restricted', label: '限制空域' }
  ];

  function getKunyuSdk() {
    if (typeof sdk !== 'undefined' && sdk) return sdk;
    if (window.sdk) return window.sdk;
    return null;
  }

  function installKunyuStyles() {
    if (document.getElementById('weather-mixer-kunyu-style')) return;
    const style = document.createElement('style');
    style.id = 'weather-mixer-kunyu-style';
    style.textContent = [
      'html, body { margin: 0 !important; padding: 0 !important; overflow: hidden !important; background: #dbe8ef !important; }',
      '#map { position: fixed !important; inset: 0 !important; width: 100vw !important; height: 100vh !important; min-height: 100vh !important; }',
      '#weather-mixer-layer-toggle { position: fixed !important; top: 12px !important; right: 12px !important; z-index: 2147483647 !important; border: 0 !important; border-radius: 999px !important; padding: 22px 30px !important; min-width: 132px !important; color: #082033 !important; background: rgba(255,255,255,.94) !important; box-shadow: 0 8px 24px rgba(8,32,51,.24) !important; font: 850 38px/1.12 system-ui,-apple-system,BlinkMacSystemFont,"Segoe UI",sans-serif !important; }',
      '#weather-mixer-layer-panel { position: fixed !important; top: 100px !important; right: 12px !important; left: auto !important; z-index: 2147483646 !important; overflow-y: auto !important; overflow-x: hidden !important; border-radius: 24px !important; box-sizing: border-box !important; padding: 24px !important; color: #082033 !important; background: rgba(255,255,255,.97) !important; box-shadow: 0 16px 40px rgba(8,32,51,.28) !important; font-family: system-ui,-apple-system,BlinkMacSystemFont,"Segoe UI",sans-serif !important; }',
      '#weather-mixer-layer-panel[hidden] { display: none !important; }',
      'body > #layer-control-panel:not(#weather-mixer-layer-panel) { display: none !important; }',
      '.weather-mixer-layer-head { display: block !important; margin-bottom: 10px !important; width: 100% !important; box-sizing: border-box !important; }',
      '.weather-mixer-layer-title { margin: 0 !important; font-size: 40px !important; line-height: 1.25 !important; font-weight: 850 !important; }',
      '.weather-mixer-layer-row { display: grid !important; grid-template-columns: 58px 1fr !important; align-items: center !important; column-gap: 22px !important; min-height: 88px !important; width: 100% !important; box-sizing: border-box !important; padding: 14px 4px !important; font-size: 38px !important; font-weight: 800 !important; white-space: nowrap !important; }',
      '.weather-mixer-layer-row input { width: 48px !important; height: 48px !important; margin: 0 !important; accent-color: #0f5a9c !important; pointer-events: none !important; }',
      '.weather-mixer-layer-row span { display: block !important; overflow: hidden !important; text-overflow: ellipsis !important; }'
    ].join('\n');
    document.head.appendChild(style);
  }

  function fixKunyuLayout() {
    installKunyuStyles();
    const heightPx = Math.max(window.innerHeight || 0, document.documentElement.clientHeight || 0, 700) + 'px';
    ['html', 'body', '#map'].forEach(function (selector) {
      document.querySelectorAll(selector).forEach(function (element) {
        element.style.setProperty('width', '100%', 'important');
        element.style.setProperty('height', heightPx, 'important');
        element.style.setProperty('min-height', heightPx, 'important');
        element.style.setProperty('display', 'block', 'important');
        element.style.setProperty('overflow', 'hidden', 'important');
      });
    });
    document.querySelectorAll('canvas').forEach(function (canvas) {
      canvas.style.setProperty('width', '100%', 'important');
      canvas.style.setProperty('height', heightPx, 'important');
    });
    window.dispatchEvent(new Event('resize'));
  }

  function initialLayerState() {
    if (!window.__weatherMixerKunyuLayerState) {
      window.__weatherMixerKunyuLayerState = {};
      layerItems.forEach(function (item) { window.__weatherMixerKunyuLayerState[item.id] = true; });
    }
    return window.__weatherMixerKunyuLayerState;
  }


  function layerIds(item) {
    return item.ids || [item.id];
  }
  function callKunyuLayerMethod(chart, methodName, args) {
    try {
      if (chart && typeof chart[methodName] === 'function') {
        chart[methodName].apply(chart, args);
        return true;
      }
    } catch (ignored) {}
    return false;
  }


  function setMapboxLayerVisibility(map, layerId, visible) {
    try {
      map.setLayoutProperty(layerId, 'visibility', visible ? 'visible' : 'none');
      return true;
    } catch (ignored) {}
    return false;
  }

  function setAirportSymbolVisibility(chart, visible) {
    if (!chart || !chart.map || typeof chart.map.getStyle !== 'function') return;
    const map = chart.map;
    const layers = (map.getStyle().layers || []);
    layers.forEach(function (layer) {
      try {
        if (layer.type !== 'symbol') return;
        const haystack = JSON.stringify([
          layer.id,
          layer.source,
          layer['source-layer'],
          layer.layout,
          layer.metadata,
          layer.filter
        ]).toLowerCase();
        if (
          haystack.indexOf('airport') >= 0 ||
          haystack.indexOf('aerodrome') >= 0 ||
          haystack.indexOf('airfield') >= 0 ||
          haystack.indexOf('机场') >= 0
        ) {
          setMapboxLayerVisibility(map, layer.id, visible);
        }
      } catch (ignored) {}
    });
  }
  function layerRuntimeState() {
    if (!window.__weatherMixerKunyuLayerRuntime) {
      window.__weatherMixerKunyuLayerRuntime = { added: {}, visible: {} };
    }
    return window.__weatherMixerKunyuLayerRuntime;
  }

  function builtInLayerKeywords(id) {
    const keywords = {
      amm: ['amm'],
      airline: ['airline', 'airway', 'airways', 'route', 'awy', '航路'],
      airport: ['airport', 'aerodrome', 'airfield', '机场'],
      vor: ['vor'],
      ndb: ['ndb'],
      airspace: ['airspace', 'fir'],
      controlled: ['controlled', '管制'],
      restricted: ['restricted', '限制']
    };
    return keywords[id] || [id];
  }

  function setBuiltInStyleLayersVisibility(chart, id, visible) {
    if (!chart || !chart.map || typeof chart.map.getStyle !== 'function') return;
    const map = chart.map;
    const keywords = builtInLayerKeywords(id);
    const layers = (map.getStyle().layers || []);
    layers.forEach(function (layer) {
      try {
        const haystack = JSON.stringify([
          layer.id,
          layer.source,
          layer['source-layer'],
          layer.layout,
          layer.metadata,
          layer.filter
        ]).toLowerCase();
        if (keywords.some(function (keyword) { return haystack.indexOf(keyword) >= 0; })) {
          setMapboxLayerVisibility(map, layer.id, visible);
        }
      } catch (ignored) {}
    });
  }

  function ensureKunyuLayerAdded(chart, id) {
    const runtime = layerRuntimeState();
    if (runtime.added[id]) return;
    if (callKunyuLayerMethod(chart, 'addBuiltInLayer', [id])) {
      runtime.added[id] = true;
      runtime.visible[id] = true;
    }
  }

  function setKunyuLayerVisible(chart, id, visible) {
    if (!chart) return;
    const runtime = layerRuntimeState();
    ensureKunyuLayerAdded(chart, id);
    if (runtime.visible[id] !== visible) {
      let toggled = false;
      try {
        if (typeof chart.toggleBuiltInLayer === 'function') {
          chart.toggleBuiltInLayer(id);
          toggled = true;
        }
      } catch (ignored) {}
      if (!toggled) {
        callKunyuLayerMethod(chart, 'setLayerVisible', [id, visible]);
        callKunyuLayerMethod(chart, 'setBuiltInLayerVisible', [id, visible]);
        callKunyuLayerMethod(chart, visible ? 'showBuiltInLayer' : 'hideBuiltInLayer', [id]);
        callKunyuLayerMethod(chart, visible ? 'showLayers' : 'hideLayers', [[id]]);
      }
      runtime.visible[id] = visible;
    }
    if (id === 'airport') {
      setAirportSymbolVisibility(chart, visible);
    } else {
      setBuiltInStyleLayersVisibility(chart, id, visible);
    }
  }
  function syncKunyuLayers(chart) {
    const state = initialLayerState();
    layerItems.forEach(function (item) {
      layerIds(item).forEach(function (id) { setKunyuLayerVisible(chart, id, state[item.id] !== false); });
    });
  }

  function applyKunyuPanelMetrics() {
    const panel = document.getElementById('weather-mixer-layer-panel');
    const toggle = document.getElementById('weather-mixer-layer-toggle');
    const viewportWidth = Math.max(window.innerWidth || document.documentElement.clientWidth || 360, 320);
    const viewportHeight = Math.max(window.innerHeight || document.documentElement.clientHeight || 700, 560);
    if (toggle) {
      toggle.style.setProperty('top', '12px', 'important');
      toggle.style.setProperty('right', '12px', 'important');
      toggle.style.setProperty('min-width', '132px', 'important');
    }
    if (!panel) return;
    const panelWidth = Math.max(360, Math.min(viewportWidth - 18, 620));
    const panelMaxHeight = Math.max(460, viewportHeight - 120);
    panel.style.setProperty('width', panelWidth + 'px', 'important');
    panel.style.setProperty('min-width', panelWidth + 'px', 'important');
    panel.style.setProperty('max-width', panelWidth + 'px', 'important');
    panel.style.setProperty('max-height', panelMaxHeight + 'px', 'important');
    panel.style.setProperty('display', panel.hidden ? 'none' : 'block', 'important');
  }

  function setPanelCollapsed(collapsed) {
    const panel = document.getElementById('weather-mixer-layer-panel');
    const toggle = document.getElementById('weather-mixer-layer-toggle');
    if (!panel || !toggle) return;
    panel.hidden = collapsed;
    toggle.textContent = collapsed ? '图层' : '收起';
    toggle.setAttribute('aria-expanded', String(!collapsed));
    window.__weatherMixerKunyuPanelCollapsed = collapsed;
    applyKunyuPanelMetrics();
  }

  function appendKunyuLayerRow(panel, item, state, chart) {
    const row = document.createElement('div');
    row.className = 'weather-mixer-layer-row';
    row.setAttribute('role', 'checkbox');
    row.setAttribute('aria-label', item.label);
    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.value = item.id;
    checkbox.checked = state[item.id] !== false;
    checkbox.tabIndex = -1;
    checkbox.setAttribute('aria-hidden', 'true');
    const label = document.createElement('span');
    label.textContent = item.label;

    function applyChecked(checked) {
      checkbox.checked = checked;
      row.setAttribute('aria-checked', String(checked));
      state[item.id] = checked;
      layerIds(item).forEach(function (id) { setKunyuLayerVisible(chart || getKunyuSdk(), id, checked); });
    }


    row.addEventListener('click', function (event) {
      event.preventDefault();
      event.stopPropagation();
      applyChecked(!checkbox.checked);
    });
    row.appendChild(checkbox);
    row.appendChild(label);
    row.setAttribute('aria-checked', String(checkbox.checked));
    panel.appendChild(row);
  }

  function buildKunyuLayerPanel(chart) {
    installKunyuStyles();
    const state = initialLayerState();
    let toggle = document.getElementById('weather-mixer-layer-toggle');
    if (!toggle) {
      toggle = document.createElement('button');
      toggle.id = 'weather-mixer-layer-toggle';
      toggle.type = 'button';
      toggle.addEventListener('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        setPanelCollapsed(!window.__weatherMixerKunyuPanelCollapsed);
      });
      document.body.appendChild(toggle);
    }

    const officialPanel = document.getElementById('layer-control-panel');
    if (officialPanel && officialPanel.id !== 'weather-mixer-layer-panel') {
      officialPanel.style.setProperty('display', 'none', 'important');
      officialPanel.hidden = true;
    }
    let panel = document.getElementById('weather-mixer-layer-panel');
    if (!panel) {
      panel = document.createElement('div');
      panel.id = 'weather-mixer-layer-panel';
      document.body.appendChild(panel);
    }

    if (panel.dataset.weatherMixerPanel !== 'ready' || panel.querySelectorAll('.weather-mixer-layer-row').length < layerItems.length) {
      panel.innerHTML = '';
      const head = document.createElement('div');
      head.className = 'weather-mixer-layer-head';
      const title = document.createElement('h2');
      title.className = 'weather-mixer-layer-title';
      title.textContent = '图层控制';
      head.appendChild(title);
      panel.appendChild(head);
      layerItems.forEach(function (item) {
        appendKunyuLayerRow(panel, item, state, chart);
      });
      ['click', 'dblclick', 'touchstart', 'touchmove', 'wheel'].forEach(function (eventName) {
        panel.addEventListener(eventName, function (event) { event.stopPropagation(); }, { passive: false });
      });
      panel.dataset.weatherMixerPanel = 'ready';
    }

    panel.querySelectorAll('input[type="checkbox"]').forEach(function (input) {
      input.checked = state[input.value] !== false;
      const row = input.closest('.weather-mixer-layer-row');
      if (row) row.setAttribute('aria-checked', String(input.checked));
    });
    setPanelCollapsed(window.__weatherMixerKunyuPanelCollapsed !== false);
    syncKunyuLayers(chart);
  }
  function applyKunyuContrast(chart) {
    if (!chart || !chart.map || typeof chart.map.getStyle !== 'function') return false;
    const map = chart.map;
    try {
      if (typeof chart.setTheme === 'function') chart.setTheme('light');
    } catch (ignored) {}
    buildKunyuLayerPanel(chart);
    const layers = (map.getStyle().layers || []);
    layers.forEach(function (layer) {
      try {
        if (layer.type === 'background') {
          map.setPaintProperty(layer.id, 'background-color', '#dbe8ef');
        } else if (layer.type === 'fill') {
          map.setPaintProperty(layer.id, 'fill-color', '#b8d5dd');
          map.setPaintProperty(layer.id, 'fill-opacity', 0.74);
        } else if (layer.type === 'line') {
          map.setPaintProperty(layer.id, 'line-color', '#0f5a9c');
          map.setPaintProperty(layer.id, 'line-opacity', 0.98);
          map.setPaintProperty(layer.id, 'line-width', 4.2);
        } else if (layer.type === 'circle') {
          map.setPaintProperty(layer.id, 'circle-color', '#e4482e');
          map.setPaintProperty(layer.id, 'circle-radius', 7.8);
          map.setPaintProperty(layer.id, 'circle-opacity', 0.96);
        } else if (layer.type === 'symbol') {
          map.setPaintProperty(layer.id, 'text-color', '#082033');
          map.setPaintProperty(layer.id, 'text-halo-color', '#ffffff');
          map.setPaintProperty(layer.id, 'text-halo-width', 2);
          map.setPaintProperty(layer.id, 'icon-opacity', 1);
          map.setLayoutProperty(layer.id, 'text-size', ['interpolate', ['linear'], ['zoom'], 3, 23, 6, 27, 9, 33]);
          map.setLayoutProperty(layer.id, 'icon-size', ['interpolate', ['linear'], ['zoom'], 3, 1.72, 8, 2.07]);
          map.setLayoutProperty(layer.id, 'text-allow-overlap', true);
        }
      } catch (ignored) {}
    });
    try {
      if (!window.__weatherMixerKunyuCentered && typeof chart.flyTo === 'function') {
        chart.flyTo({ center: [116.3974, 39.9093], zoom: 6.75, duration: 0 });
        window.__weatherMixerKunyuCentered = true;
      }
      if (typeof map.resize === 'function') map.resize();
    } catch (ignored) {}
    return true;
  }

  function runKunyuFix() {
    fixKunyuLayout();
    applyKunyuContrast(getKunyuSdk());
  }

  runKunyuFix();
  window.setTimeout(runKunyuFix, 320);
  window.setTimeout(runKunyuFix, 900);
  window.setTimeout(runKunyuFix, 1800);
  window.setTimeout(runKunyuFix, 3600);
})();
""".trimIndent()
@SuppressLint("SetJavaScriptEnabled")
private fun WebView.configureInteractiveWebView(
    useWideViewport: Boolean,
    loadWithOverviewMode: Boolean,
    webPageZoomEnabled: Boolean,
    postPageLoadScript: String? = null,
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

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            val script = postPageLoadScript ?: return
            val target = view ?: return
            target.evaluateJavascript(script, null)
            target.postDelayed({ target.evaluateJavascript(script, null) }, 700L)
            target.postDelayed({ target.evaluateJavascript(script, null) }, 1_800L)
            target.postDelayed({ target.evaluateJavascript(script, null) }, 3_600L)
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

private fun flightTrackViewportFixScript(): String = """
(function () {
  const viewportWidth = 1280;
  const viewportScale = 0.38;

  function installWeatherMixerFlightStyles() {
    if (document.getElementById('weather-mixer-flight-style')) return;
    const style = document.createElement('style');
    style.id = 'weather-mixer-flight-style';
    style.textContent = [
      'html, body { margin: 0 !important; padding: 0 !important; overflow: hidden !important; }',
      '#app, .layout, .layout-main, .no-footer, .all-height, .home-container { min-width: 1280px !important; }',
      '.feedback, [class*="feedback"], [class*="tool"], [class*="control"], button, input, select { font-size: 23px !important; }',
      '.amap-controlbar, .amap-scalecontrol, .amap-toolbar, .amap-geolocation-con, .amap-maptypecontrol, [class*="toolbar"], [class*="panel"] { transform: scale(1.55) !important; transform-origin: top left !important; }'
    ].join('\n');
    document.head.appendChild(style);
  }

  function tryZoomKnownMapObjects() {
    if (window.__weatherMixerFlightZoomed) return;
    const seen = [];
    const candidates = [];
    ['map', 'amap', 'aMap', 'flightMap', 'adsbMap'].forEach(function (name) {
      if (window[name]) candidates.push(window[name]);
    });
    try {
      Object.keys(window).slice(0, 500).forEach(function (name) {
        const value = window[name];
        if (value && typeof value === 'object' && typeof value.getZoom === 'function' && typeof value.setZoom === 'function') {
          candidates.push(value);
        }
      });
    } catch (ignored) {}
    candidates.some(function (candidate) {
      if (!candidate || seen.indexOf(candidate) >= 0) return false;
      seen.push(candidate);
      try {
        const zoom = candidate.getZoom();
        if (typeof zoom === 'number' && isFinite(zoom)) {
          candidate.setZoom(Math.min(zoom + 1.8, 18));
          window.__weatherMixerFlightZoomed = true;
          return true;
        }
      } catch (ignored) {}
      return false;
    });
  }

  function applyWeatherMixerFlightLayoutFix() {
    installWeatherMixerFlightStyles();
    let meta = document.querySelector('meta[name="viewport"]');
    if (!meta) {
      meta = document.createElement('meta');
      meta.name = 'viewport';
      document.head.appendChild(meta);
    }
    meta.content = 'width=' + viewportWidth + ', initial-scale=' + viewportScale + ', maximum-scale=' + viewportScale + ', user-scalable=no';

    document.documentElement.style.removeProperty('zoom');
    document.body.style.removeProperty('zoom');
    document.documentElement.style.setProperty('min-width', viewportWidth + 'px', 'important');
    document.body.style.setProperty('min-width', viewportWidth + 'px', 'important');

    const heightPx = Math.max(window.innerHeight || 0, document.documentElement.clientHeight || 0, 1200) + 'px';
    [
      'html', 'body', '#app', '.layout', '.layout-main', '.no-footer', '.all-height',
      '.home-container', '#map-container', '.map-comp', '.amap-container', '.amap-maps',
      '.amap-drags', '.amap-layers', '.amap-overlays', '.amap-controls'
    ].forEach(function (selector) {
      document.querySelectorAll(selector).forEach(function (element) {
        element.style.removeProperty('zoom');
        element.style.setProperty('height', heightPx, 'important');
        element.style.setProperty('min-height', heightPx, 'important');
        element.style.setProperty('max-height', 'none', 'important');
        if (selector !== 'html' && selector !== 'body') {
          element.style.setProperty('display', 'block', 'important');
        }
      });
    });
    tryZoomKnownMapObjects();
    window.dispatchEvent(new Event('resize'));
  }

  applyWeatherMixerFlightLayoutFix();
  window.setTimeout(applyWeatherMixerFlightLayoutFix, 320);
  window.setTimeout(applyWeatherMixerFlightLayoutFix, 1200);
  window.setTimeout(applyWeatherMixerFlightLayoutFix, 2600);
  window.setTimeout(tryZoomKnownMapObjects, 4200);
})();
""".trimIndent()

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
