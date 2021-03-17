if ("undefined" == typeof $WH) {
  $WH = { wowheadRemote: !0 };
  var g_hostRoot = "https://70.wowfan.net",
    g_staticUrl = "https://cdn.jsdelivr.net/gh/wowfanet/w/70",
    g_staticDataUrl = "https://cdn.jsdelivr.net/gh/wowfanet/w/wdb";
}
if (void 0 === $WowheadPower)
  var $WowheadPower = new (function () {
    function init() {
      if (isRemote) {
        var e = document.createElement("script");
        (e.src = g_staticUrl + "/js/basic.js"), head.appendChild(e);
      } else attachEvent();
      for (var t in SCALES) for (var r in LOCALES) SCALES[t][r] = SCALES_NONE;
    }
    function initCSS() {
      if (
        "undefined" != typeof aowow_tooltips &&
        "hide" in aowow_tooltips &&
        void 0 === whcss &&
        document.styleSheets
      ) {
        var e = document.createElement("style");
        (e.type = "text/css"),
          head.appendChild(e),
          window.createPopup || head.appendChild(document.createTextNode("")),
          (whcss = document.styleSheets[document.styleSheets.length - 1]);
        for (var t in aowow_tooltips.hide)
          aowow_tooltips.hide[t] &&
            (whcss.insertRule
              ? whcss.insertRule(
                  ".wowhead-tooltip .whtt-" + t + "{display : none}",
                  whcss.cssRules.length
                )
              : whcss.addRule &&
                whcss.addRule(
                  ".wowhead-tooltip .whtt-" + t,
                  "display : none",
                  -1
                ));
      }
    }
    function attachEvent() {
      eventAttached ||
        ((eventAttached = !0), $WH.aE(document, "mouseover", onMouseOver));
    }
    function onDOMReady(e) {
      if ("undefined" != typeof jQuery) return void jQuery(e);
      /in/.test(document.readyState)
        ? setTimeout(onDOMReady.bind(null, e), 9)
        : e();
    }
    function updateCursorPos(e) {
      var t = $WH.g_getCursorPos(e);
      (cursorX = t.x), (cursorY = t.y);
    }
    function scanElement(t, e) {
      if ("A" != t.nodeName) return -2323;
      var rel = t.rel;
      try {
        t.dataset && t.dataset.hasOwnProperty("wowhead")
          ? (rel = t.dataset.wowhead)
          : t.getAttribute &&
            t.getAttribute("data-wowhead") &&
            (rel = t.getAttribute("data-wowhead"));
      } catch (e) {}
      if (
        (t.href.length || rel) &&
        !(
          (rel && /^np\b/.test(rel)) ||
          "true" == t.getAttribute("data-disable-wowhead-tooltip")
        )
      ) {
        var i0,
          i1,
          i2,
          url,
          params = {};
        currentParams = params;
        var p = function (e, t, r) {
          "buff" == t || "sock" == t || "map" == t
            ? (params[t] = !0)
            : "rand" == t || "ench" == t || "lvl" == t || "c" == t
            ? (params[t] = parseInt(r))
            : "gems" == t || "pcs" == t || "know" == t || "cri" == t
            ? (params[t] = r.split(":"))
            : "who" == t || "domain" == t
            ? (params[t] = r)
            : "when" == t
            ? (params[t] = new Date(parseInt(r)))
            : "premium" == t
            ? (params[t] = !0)
            : "text" == t && (params[t] = !0);
        };
        if (
          (1 & opt.applyto &&
            ((i1 = 2),
            (i2 = 3),
            0 == t.href.indexOf("http://") || 0 == t.href.indexOf("https://")
              ? ((i0 = 1),
                (url = t.href.match(
                  /^https?:\/\/(.*)\/?\??(item|quest|spell|achievement|event|npc|object|itemset|currency)=(-?[0-9]+)/
                )),
                null == url &&
                  (url = t.href.match(
                    /^https?:\/\/(.*)\/?\??(profile)=([^&#]+)/
                  )),
                (showLogo = 0))
              : ((url = t.href.match(
                  /()\?(item|quest|spell|achievement|event|npc|object|itemset|currency)=(-?[0-9]+)/
                )),
                null == url && (url = t.href.match(/()\?(profile)=([^&#]+)/)),
                (showLogo = 1))),
          null == url &&
            rel &&
            2 & opt.applyto &&
            ((i0 = 0),
            (i1 = 1),
            (i2 = 2),
            (url = rel.match(
              /(item|quest|spell|achievement|event|npc|object|itemset|currency).?(-?[0-9]+)/
            )),
            (showLogo = 1)),
          t.href.replace(/([a-zA-Z]+)=?([a-zA-Z0-9:-]*)/g, p),
          rel && rel.replace(/([a-zA-Z]+)=?([a-zA-Z0-9:-]*)/g, p),
          params.gems && params.gems.length > 0)
        ) {
          var i;
          for (
            i = Math.min(3, params.gems.length - 1);
            i >= 0 && !parseInt(params.gems[i]);
            --i
          );
          ++i,
            0 == i
              ? delete params.gems
              : i < params.gems.length &&
                (params.gems = params.gems.slice(0, i));
        }
        if (url) {
          var locale,
            domain = "www";
          if (
            ((currentA = t),
            params.domain
              ? (domain = params.domain)
              : i0 && url[i0] && (domain = url[i0].split(".")[0]),
            (domain2 = url[i0].split(".")[1]),
            "tbcdb" != domain2)
          )
            return;
          if (
            (REDIRECTS[domain] && (domain = REDIRECTS[domain]),
            (locale = $WH.g_getLocaleFromDomain(domain)),
            -1 == $WH.in_array(["fr", "de", "cn", "es", "ru", "en"], domain))
          )
            for (i in document.scripts)
              if (document.scripts[i].src) {
                var dmn = document.scripts[i].src.match(
                  /widgets\/power.js\?(lang|locale)=(en|fr|de|cn|es|ru)/i
                );
                if (dmn) {
                  (domain = dmn[2]),
                    (locale = $WH.g_getLocaleFromDomain(dmn[2]));
                  break;
                }
              }
          if (
            ((currentDomain = domain),
            -1 != t.href.indexOf("#") &&
              -1 != document.location.href.indexOf(url[i1] + "=" + url[i2]))
          )
            return;
          (mode =
            0 == t.parentNode.className.indexOf("icon") &&
            "DIV" == t.parentNode.nodeName
              ? 1
              : 0),
            t.onmouseout ||
              (0 == mode && (t.onmousemove = onMouseMove),
              (t.onmouseout = onMouseOut)),
            e && updateCursorPos(e);
          var type = $WH.g_getIdFromTypeName(url[i1]),
            typeId = url[i2];
          if (
            (display(type, typeId, locale, params),
            e || "undefined" == typeof aowow_tooltips)
          )
            return;
          var data = LOOKUPS[type][0][getFullId(typeId, params)],
            timeout = function (t) {
              if (
                data.status[locale] != STATUS_OK &&
                data.status[locale] != STATUS_NOTFOUND
              )
                return void window.setTimeout(function () {
                  timeout(t);
                }, 5);
              aowow_tooltips.renamelinks &&
                type != TYPE_QUEST &&
                (eval("name = data.name_" + LOCALES[locale]),
                name && (t.innerHTML = "<span>" + name + "</span>")),
                aowow_tooltips.iconizelinks &&
                  (type == TYPE_ITEM ||
                    type == TYPE_ACHIEVEMENT ||
                    type == TYPE_SPELL) &&
                  data.icon &&
                  ((t.className += " icontinyl"),
                  t.setAttribute(
                    "style",
                    t.getAttribute("style") + "; padding-left:18px !important;"
                  ),
                  (t.style.verticalAlign = "center"),
                  (t.style.background =
                    "url(" +
                    g_staticDataUrl +
                    "/images/wow/icons/tiny/" +
                    data.icon.toLocaleLowerCase() +
                    ".gif) left center no-repeat")),
                aowow_tooltips.colorlinks &&
                  type == TYPE_ITEM &&
                  (t.className += " q" + data.quality);
            };
          timeout(t);
        }
      }
    }
    function onMouseOver(e) {
      e = $WH.$E(e);
      for (
        var t = e._target, r = 0;
        null != t && r < 5 && -2323 == scanElement(t, e);

      )
        (t = t.parentNode), ++r;
    }
    function onMouseMove(e) {
      (e = $WH.$E(e)),
        updateCursorPos(e),
        $WH.Tooltip.move(cursorX, cursorY, 0, 0, CURSOR_HSPACE, CURSOR_VSPACE);
    }
    function onMouseOut() {
      (currentType = null), (currentA = null), $WH.Tooltip.hide();
    }
    function getTooltipField(e, t) {
      var r = "tooltip";
      return (
        currentParams && currentParams.buff && (r = "buff"),
        currentParams && currentParams.text && (r = "text"),
        currentParams && currentParams.premium && (r = "tooltip_premium"),
        r + (t || "") + "_" + LOCALES[e]
      );
    }
    function getIconField() {
      return currentParams && currentParams.text ? "text_icon" : "icon";
    }
    function getSpellsField(e) {
      return (
        (currentParams && currentParams.buff ? "buff" : "") +
        "spells_" +
        LOCALES[e]
      );
    }
    function initElement(e, t, r) {
      var n = LOOKUPS[e][0];
      null == n[t] && (n[t] = {}),
        null == n[t].status && (n[t].status = {}),
        null == n[t].response && (n[t].response = {}),
        null == n[t].status[r] && (n[t].status[r] = STATUS_NONE);
    }
    function display(e, t, r, n) {
      n || (n = {});
      var s = getFullId(t, n);
      (currentType = e),
        decodeURI(s) == s
          ? ((s = currentId = encodeURI(s)), (t = encodeURI(t)))
          : (currentId = s),
        (currentLocale = r),
        (currentParams = n),
        initElement(e, s, r);
      var a = LOOKUPS[e][0];
      a[s].status[r] == STATUS_OK || a[s].status[r] == STATUS_NOTFOUND
        ? showTooltip(
            a[s][getTooltipField(r)],
            a[s][getIconField()],
            a[s].map,
            a[s][getSpellsField(r)],
            a[s][getTooltipField(r, 2)]
          )
        : a[s].status[r] == STATUS_QUERYING || a[s].status[r] == STATUS_SCALES
        ? showTooltip(_LANG.loading)
        : request(e, t, r, null, n);
    }
    function request(e, t, r, n, s) {
      var a = getFullId(t, s),
        o = LOOKUPS[e][0];
      if (o[a].status[r] == STATUS_NONE || o[a].status[r] == STATUS_ERROR) {
        (o[a].status[r] = STATUS_QUERYING),
          n ||
            (o[a].timer = setTimeout(function () {
              showLoading.apply(this, [e, a, r]);
            }, 333));
        var i = "";
        for (var c in s)
          ("rand" != c &&
            "ench" != c &&
            "gems" != c &&
            "sock" != c &&
            "lvl" != c) ||
            ("object" == typeof s[c]
              ? (i += "&" + c + "=" + s[c].join(":"))
              : !0 === s[c]
              ? (i += "&" + c)
              : (i += "&" + c + "=" + s[c]));
        var l = $WH.g_getDomainFromLocale(r),
          u = g_hostRoot + "/";
        $WH.g_ajaxIshRequest(
          u + "?" + LOOKUPS[e][1] + "=" + t + "&domain=" + l + "&power" + i
        ),
          SCALES[e] &&
            SCALES[e][r] == SCALES_NONE &&
            ($WH.g_ajaxIshRequest(u + SCALES[e].url),
            (SCALES[e][r] = SCALES_QUERYING));
      }
    }
    function showTooltip(e, t, r, n, s) {
      currentA &&
        currentA._fixTooltip &&
        (e = currentA._fixTooltip(e, currentType, currentId, currentA)),
        initCSS();
      if (e) {
        if (null != currentParams) {
          if (currentParams.pcs && currentParams.pcs.length) {
            for (var a = 0, o = 0, i = currentParams.pcs.length; o < i; ++o) {
              var c;
              (c = e.match(
                new RegExp(
                  "<span>\x3c!--si([0-9]+:)*" +
                    currentParams.pcs[o] +
                    '(:[0-9]+)*--\x3e<a href="\\?item=(\\d+)">(.+?)</a></span>'
                )
              )) &&
                ((e = e.replace(
                  c[0],
                  '<span class="q8">\x3c!--si' +
                    currentParams.pcs[o] +
                    '--\x3e<a href="?item=' +
                    c[3] +
                    '">' +
                    ($WH.isset("g_items") && g_items[currentParams.pcs[o]]
                      ? g_items[currentParams.pcs[o]][
                          "name_" + LOCALES[currentLocale]
                        ]
                      : c[4]) +
                    "</a></span>"
                )),
                ++a);
            }
            a > 0 &&
              ((e = e.replace("(0/", "(" + a + "/")),
              (e = e.replace(
                new RegExp("<span>\\(([0-" + a + "])\\)", "g"),
                '<span class="q2">($1)'
              )));
          }
          if (
            (currentParams.c &&
              ((e = e.replace(
                /<span class="c([0-9]+?)">(.+?)<\/span><br \/>/g,
                '<span class="c$1" style="display: none">$2</span>'
              )),
              (e = e.replace(
                new RegExp(
                  '<span class="c(' +
                    currentParams.c +
                    ')" style="display: none">(.+?)</span>',
                  "g"
                ),
                '<span class="c$1">$2</span><br />'
              ))),
            currentParams.know &&
              currentParams.know.length &&
              (e = $WH.g_setTooltipSpells(e, currentParams.know, n)),
            currentParams.lvl
              ? (e = $WH.g_setTooltipLevel(
                  e,
                  currentParams.lvl,
                  currentParams.buff
                ))
              : $WH.gc("compare_level") &&
                window.location.href.match(/\?compare/i) &&
                (e = $WH.g_setTooltipLevel(
                  e,
                  $WH.gc("compare_level"),
                  currentParams.buff
                )),
            currentParams.who &&
              currentParams.when &&
              ((e = e.replace(
                "<table><tr><td><br />",
                '<table><tr><td><br /><span class="q2">' +
                  $WH.sprintf(
                    _LANG.achievementcomplete,
                    currentParams.who,
                    currentParams.when.getMonth() + 1,
                    currentParams.when.getDate(),
                    currentParams.when.getFullYear()
                  ) +
                  "</span><br /><br />"
              )),
              (e = e.replace(/class="q0"/g, 'class="r3"'))),
            currentType == TYPE_ACHIEVEMENT && currentParams.cri)
          )
            for (var o = 0; o < currentParams.cri.length; o++)
              e = e.replace(
                new RegExp(
                  "\x3c!--cr" + parseInt(currentParams.cri[o]) + ":[^<]+",
                  "g"
                ),
                '<span class="q2">$&</span>'
              );
        }
      } else
        (e = LOOKUPS[currentType][2] + " not found :("),
          (t = "inv_misc_questionmark"),
          !0;
      currentParams.map && r && r.getMap && (s = r.getMap()),
        1 == mode
          ? ($WH.Tooltip.setIcon(null),
            $WH.Tooltip.show(currentA, e, null, null, null, s))
          : ($WH.Tooltip.setIcon(t),
            $WH.Tooltip.showAtXY(
              e,
              cursorX,
              cursorY,
              CURSOR_HSPACE,
              CURSOR_VSPACE,
              s
            )),
        isRemote &&
          $WH.Tooltip.logo &&
          ($WH.Tooltip.logo.style.display = showLogo ? "block" : "none");
    }
    function showLoading(e, t, r) {
      if (currentType == e && currentId == t && currentLocale == r) {
        showTooltip(_LANG.loading);
        LOOKUPS[e][0][t].timer = setTimeout(function () {
          notFound.apply(this, [e, t, r]);
        }, 3850);
      }
    }
    function notFound(e, t, r) {
      (LOOKUPS[e][0][t].status[r] = STATUS_ERROR),
        currentType == e &&
          currentId == t &&
          currentLocale == r &&
          showTooltip(_LANG.noresponse);
    }
    function getFullId(e, t) {
      return (
        e +
        (t.rand ? "r" + t.rand : "") +
        (t.ench ? "e" + t.ench : "") +
        (t.gems ? "g" + t.gems.join(",") : "") +
        (t.sock ? "s" : "")
      );
    }
    var isRemote = $WH.wowheadRemote,
      opt = { applyto: 3 },
      head = document.getElementsByTagName("head")[0],
      whcss,
      currentType,
      currentId,
      currentLocale,
      currentDomain,
      currentParams,
      currentA,
      cursorX,
      cursorY,
      mode = 0,
      eventAttached = !1,
      npcs = {},
      objects = {},
      items = {},
      quests = {},
      spells = {},
      achievements = {},
      holidays = {},
      itemsets = {},
      currencies = {},
      profiles = {},
      showLogo = 1,
      STATUS_NONE = 0,
      STATUS_QUERYING = 1,
      STATUS_ERROR = 2,
      STATUS_NOTFOUND = 3,
      STATUS_OK = 4,
      STATUS_SCALES = 5,
      SCALES_NONE = 0,
      SCALES_LOADED = 1,
      SCALES_QUERYING = 2,
      TYPE_NPC = 1,
      TYPE_OBJECT = 2,
      TYPE_ITEM = 3,
      TYPE_ITEMSET = 4,
      TYPE_QUEST = 5,
      TYPE_SPELL = 6,
      TYPE_ACHIEVEMENT = 10,
      TYPE_HOLIDAY = 12,
      TYPE_CURRENCY = 17,
      TYPE_PROFILE = 100,
      CURSOR_HSPACE = 15,
      CURSOR_VSPACE = 15,
      _LANG = {
        loading: "Loading...",
        noresponse: "No response from server :(",
        achievementcomplete: "Achievement earned by $1 on $2/$3/$4",
      },
      LOOKUPS = {
        1: [npcs, "npc", "NPC"],
        2: [objects, "object", "Object"],
        3: [items, "item", "Item"],
        4: [itemsets, "itemset", "Item Set"],
        5: [quests, "quest", "Quest"],
        6: [spells, "spell", "Spell"],
        10: [achievements, "achievement", "Achievement"],
        12: [holidays, "event", "Holiday"],
        17: [currencies, "currency", "Currency"],
        100: [profiles, "profile", "Profile"],
      },
      SCALES = {
        3: { url: "?data=item-scaling" },
        6: { url: "?data=item-scaling" },
      },
      LOCALES = {
        0: "enus",
        2: "frfr",
        3: "dede",
        4: "zhcn",
        6: "eses",
        8: "ruru",
      },
      REDIRECTS = { wotlk: "www", ptr: "www", www: "en" };
    if (isRemote)
      var Locale = {
        getId: function () {
          return 0;
        },
        getName: function () {
          return "enus";
        },
      };
    (this.init = function () {
      isRemote &&
        $WH.ae(
          head,
          $WH.ce("link", {
            type: "text/css",
            href: g_staticDataUrl + "/css70/basic.css",
            rel: "stylesheet",
          })
        ),
        attachEvent(),
        onDOMReady(function () {
          if ("undefined" != typeof aowow_tooltips) {
            for (var e = 0; e < document.links.length; e++) {
              var t = document.links[e];
              "true" != t.getAttribute("noscan") && scanElement(t);
            }
            initCSS();
          }
        });
    }),
      (this.loadScales = function (e, t) {
        var r = LOOKUPS[e][0];
        for (var n in LOCALES)
          if (t == n || (!t && !isNaN(n))) {
            SCALES[e][n] = SCALES_LOADED;
            for (var s in r)
              r[s].status[n] == STATUS_SCALES &&
                r[s].response[n] &&
                r[s].response[n]();
          }
      }),
      (this.register = function (e, t, r, n) {
        var s = LOOKUPS[e][0];
        if ((initElement(e, t, r), SCALES[e] && SCALES[e][r] != SCALES_LOADED))
          return (
            (s[t].status[r] = STATUS_SCALES),
            void (s[t].response[r] = this.register.bind(this, e, t, r, n))
          );
        s[t].timer && (clearTimeout(s[t].timer), (s[t].timer = null)),
          $WH.wowheadRemote ||
            $.isEmptyObject(n.map) ||
            (null == s[t].map &&
              (s[t].map = new Mapper(
                { parent: $WH.ce("div"), zoom: 3, zoomable: !1, buttons: !1 },
                !0
              )),
            s[t].map.update(n.map, !0),
            delete n.map),
          $WH.cO(s[t], n),
          (s[t].status[r] != STATUS_QUERYING &&
            s[t].status[r] != STATUS_SCALES) ||
            (s[t][getTooltipField(r)]
              ? (s[t].status[r] = STATUS_OK)
              : (s[t].status[r] = STATUS_NOTFOUND)),
          currentType == e &&
            t == currentId &&
            currentLocale == r &&
            showTooltip(
              s[t][getTooltipField(r)],
              s[t].icon,
              s[t].map,
              s[t][getSpellsField(r)],
              s[t][getTooltipField(r, 2)]
            );
      }),
      (this.registerNpc = function (e, t, r) {
        this.register(TYPE_NPC, e, t, r);
      }),
      (this.registerCurrency = function (e, t, r) {
        this.register(TYPE_CURRENCY, e, t, r);
      }),
      (this.registerObject = function (e, t, r) {
        this.register(TYPE_OBJECT, e, t, r);
      }),
      (this.registerItem = function (e, t, r) {
        this.register(TYPE_ITEM, e, t, r);
      }),
      (this.registerHoliday = function (e, t, r) {
        this.register(TYPE_HOLIDAY, e, t, r);
      }),
      (this.registerItemSet = function (e, t, r) {
        this.register(TYPE_ITEMSET, e, t, r);
      }),
      (this.registerQuest = function (e, t, r) {
        this.register(TYPE_QUEST, e, t, r);
      }),
      (this.registerSpell = function (e, t, r) {
        this.register(TYPE_SPELL, e, t, r);
      }),
      (this.registerAchievement = function (e, t, r) {
        this.register(TYPE_ACHIEVEMENT, e, t, r);
      }),
      (this.registerProfile = function (e, t, r) {
        this.register(TYPE_PROFILE, e, t, r);
      }),
      (this.displayTooltip = function (e, t, r, n) {
        display(e, t, r, n);
      }),
      (this.request = function (e, t, r, n) {
        n || (n = {}),
          initElement(e, getFullId(t, n), r),
          request(e, t, r, 1, n);
      }),
      (this.requestItem = function (e, t) {
        this.request(TYPE_ITEM, e, Locale.getId(), t);
      }),
      (this.requestSpell = function (e) {
        this.request(TYPE_SPELL, e, Locale.getId());
      }),
      (this.getStatus = function (e, t, r) {
        var n = LOOKUPS[e][0];
        return null != n[t] ? n[t].status[r] : STATUS_NONE;
      }),
      (this.getItemStatus = function (e, t) {
        this.getStatus(TYPE_ITEM, e, t);
      }),
      (this.getSpellStatus = function (e, t) {
        this.getStatus(TYPE_SPELL, e, t);
      }),
      (this.refreshLinks = function () {
        if ("undefined" != typeof aowow_tooltips)
          for (var e = 0; e < document.links.length; e++) {
            for (
              var t = document.links[e], r = t.parentNode, n = !1;
              null != r;

            ) {
              if (
                (" " + r.className + " ")
                  .replace(/[\n\t]/g, " ")
                  .indexOf(" wowhead-tooltip ") > -1
              ) {
                n = !0;
                break;
              }
              r = r.parentNode;
            }
            n || scanElement(t);
          }
        this.hideTooltip();
      }),
      (this.setParent = function (e) {
        $WH.Tooltip.reset(), $WH.Tooltip.prepare(e);
      }),
      isRemote &&
        ((this.set = function (e) {
          $WH.cO(opt, e);
        }),
        (this.showTooltip = function (e, t, r) {
          updateCursorPos(e), showTooltip(t, r);
        }),
        (this.hideTooltip = function () {
          $WH.Tooltip.hide();
        }),
        (this.moveTooltip = function (e) {
          onMouseMove(e);
        })),
      init();
  })();
