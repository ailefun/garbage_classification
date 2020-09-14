(function (window, $) {
    function CheckCounter() {
        var count = 0;
        var counterTimeout = null;

        function getCounter() {
            return count
        }

        function increase() {
            count++;
            if (counterTimeout) {
                clearTimeout(counterTimeout)
            }
            counterTimeout = setTimeout(function () {
                count = 0
            }, 60 * 1000)
        }

        return {getCounter: getCounter, increase: increase}
    }

    function DataInformer() {
        function inform(data) {
            window.DATA_GOT = data
        }

        function informRelateSites(data) {
            window.DATA_GOT_RELATED_SITES = data
        }

        function informRelateSitesState(data) {
            window.DATA_RELATED_SITES_STATE = data
        }

        return {inform: inform, informRelateSites: informRelateSites, informRelateSitesState: informRelateSitesState}
    }

    function CheckSite() {
        var isAjaxLoading = false;
        var isAnimating = false;
        var reportData;
        var dataInformer;
        var shareTool;
        var animationInterval;
        var checkCounter;
        var errorMessageNode = $("#ErrorMessage");
        var cannotCheck = $("#CannotCheck");
        var cannotReach = $("#CannotReach");
        var checkForm = $("#CheckForm");
        var checkButton = $("#CheckButton");
        var checkInput = $("#CheckText");
        var registerButton = $("#StartButton");
        var registerNewSiteButtons = $(".open-new-site-btn");
        var mainWrap = $("#MainWrap");
        var loadingState = $("#LoadingState");
        var loadingBarWrap = $("#LoadingBarWrap");
        var resultWrap = $("#ResultWrap");
        var loadingBarFill = $(".loading-bar-fill");
        var loadingStateText = $("#LoadingStateText");

        function init() {
            checkCounter = new CheckCounter();
            dataInformer = new DataInformer();
            bindEvents();
            enableCheckButton();
            autoCheckAddressInSearch()
        }

        function bindEvents() {
            $(checkInput).on("change blur", function () {
                $(this).removeClass("error")
            });
            $(checkForm).submit(function (e) {
                e.preventDefault();
                var siteAddress = $(checkInput).removeClass("error").val();
                if (!siteAddress) {
                    showError("请填写网址");
                    return false
                }
                if (!siteAddress.match(/^https?:/)) {
                    siteAddress = "http://" + siteAddress;
                    $(checkInput).val(siteAddress)
                }
                if (!isAnimating && !isAjaxLoading) {
                    $("#RelatedSiteWrap").hide();
                    if ($("#addSites_prompt")) {
                        $("#addSites_prompt").hide();
                        $("#add_sites_url").val("")
                    }
                    performCheck(siteAddress)
                }
                return false
            });
            updateRegisterEvent()
        }

        function showShareTool(siteAddress, data) {
            var shareText = siteAddress + "，百度安全指数评价：" + data.score.synopsis + "分，战胜全国 " + Math.round(data.score.rank * 10000) / 100 + "% 的网站，" + (data.score.synopsis >= data.industry.score ? "高于" : "低于") + "行业平均值 " + Math.round(Math.abs(data.score.synopsis - data.industry.score) / data.industry.score * 10000) / 100 + "%。查看详情：" + window.location.href + "?start_url=" + siteAddress;
            var waitForAnimationDone = setInterval(function () {
                if (!isAnimating) {
                    clearInterval(waitForAnimationDone);
                    shareTool = shareTool || new ShareCtrl();
                    shareTool.init(shareText)
                }
            })
        }

        function updateRegisterEvent() {
            $(registerNewSiteButtons).click(registerNewSite)
        }

        function updateRegisterSiteAddress() {
            $(registerNewSiteButtons).attr("href", getNewSiteGoToUrl())
        }

        function registerNewSite(e) {
            e.preventDefault();
            var url = getNewSiteGoToUrl();
            if (!window.isLogined) {
                window.showLoginTab(location.protocol + "//" + window.location.host + url)
            } else {
                window.open(url)
            }
        }

        function enableCheckButton() {
            $(checkButton).prop("disabled", false)
        }

        function autoCheckAddressInSearch() {
            var query = parseSearchInUrl(location.href.split("?")[1]);
            if (query.start_url && query.start_url.trim()) {
                $("#RelatedSiteWrap").hide();
                performCheck(query.start_url.trim())
            }
        }

        function parseSearchInUrl(s) {
            if (!s) {
                return {}
            }
            var a = s.split("&");
            var r = {};
            for (var i = a.length - 1; i >= 0; i--) {
                var b = a[i].split("=");
                var v = b[1] || "";
                r[b[0]] = decodeURIComponent(v.replace(/\+/g, "%20"))
            }
            return r
        }

        function showCheckingLayout(siteAddress) {
            if (!siteAddress.match(/^https?:/)) {
                siteAddress = "http://" + siteAddress
            }
            $(checkInput).val(siteAddress);
            $(".checking-host").text(siteAddress);
            $(registerNewSiteButtons).attr("href", getNewSiteGoToUrl());
            $(loadingBarWrap).show("fast")
        }

        function showCheckedLayout() {
            var isReachable;
            var isRegistered;
            var data = reportData;
            if (data) {
                isReachable = data.is_reachable;
                isRegistered = data.is_register;
                $(registerButton).show()
            }
            if (!isReachable) {
                $(loadingState).hide();
                $(cannotReach).show();
                $(resultWrap).hide("fast");
                return false
            } else {
                if (isRegistered) {
                    if (!isAnimating) {
                        $(loadingBarWrap).hide(1000);
                        $(resultWrap).show("fast")
                    }
                } else {
                    $(loadingState).hide();
                    $(cannotCheck).show();
                    $(resultWrap).hide("fast");
                    return false
                }
            }
        }

        function showCheckingResultText() {
            var data = reportData;
            var isReachable = data.is_reachable;
            var isRegistered = data.is_register;
            if (!isReachable) {
                $(loadingStateText).text("检测失败")
            } else {
                if (isRegistered) {
                    $(loadingStateText).text("检测完成")
                } else {
                    $(loadingStateText).text("检测失败")
                }
            }
        }

        function resetLayout() {
            $(resultWrap).hide("fast");
            $(loadingBarFill).css({width: "0%"});
            $(loadingState).show();
            $(cannotCheck).hide();
            $(cannotReach).hide();
            $(errorMessageNode).text("")
        }

        function getCheckRequestErrorMsg(siteAddress) {
            if (checkCounter.getCounter() >= 10) {
                return "请求太频繁，请一分钟后再试。"
            }
            siteAddress = siteAddress.trim();
            if (siteAddress.match(/^(https?:\/\/)?(\d+\.)+\d+\/?$/i)) {
                return "IP地址无法被检测，请输入对应的域名。"
            }
            return ""
        }

        function showErrorAndHideAll(text) {
            showError(text);
            hideAll()
        }

        function showError(msg) {
            $(checkInput).addClass("error");
            $(errorMessageNode).text(msg)
        }

        function hideAll() {
            $(loadingBarWrap).hide("fast");
            $(resultWrap).hide("fast");
            $(registerButton).hide("fast")
        }

        function getNewSiteGoToUrl() {
            return $(mainWrap).data("list-url") +
                "?addsite&url=" + encodeURIComponent($(checkInput).val())
        }

        function ajaxLoadReport(siteAddress) {
            if (!siteAddress.match(/^https?:/)) {
                siteAddress = "http://" + siteAddress
            }
            isAjaxLoading = true;
            $.get($(checkForm)
                .attr("action"), {site_address: siteAddress}, function (ret) {
                isAjaxLoading = false;
                if (!isAnimating) {
                    makeProgressFull()
                }
                if (ret.code === 0) {
                    informReportGot(ret.data);
                    showCheckedLayout()
                } else {
                    stopProgressAnimation();
                    showErrorAndHideAll(ret.message)
                }
                return false
            }, "json")
        }

        function ajaxLoadRelatedSites(siteAddress) {
            $.get($(checkForm).attr("action").replace("CheckSite", "getRelatedSites"), {site_address: siteAddress}, function (ret) {
                if (ret.code === 0) {
                    informRelatedSitesGot(ret.data)
                }
            }, "json")
        }

        function ajaxLoadRelatedSiteState(siteAddress) {
            $.get($(checkForm).attr("action").replace("CheckSite", "getRelatedSitesStat"), {site_address: siteAddress}, function (ret) {
                if (ret.code === 0) {
                    informRelatedSitesStateGot(ret.data)
                }
            }, "json")
        }

        function startProgressAnimation() {
            var waitingStop = 98;
            var fullTime = 4 * 1000;
            $(loadingBarFill).animate({width: waitingStop + "%"}, fullTime * waitingStop / 100, "linear");
            var map = ["恶意页面内容", "安全漏洞", "应用和服务指纹", "应用和服务指纹漏洞", "历史恶意内容", "历史安全漏洞", "历史应用和服务漏洞", "被攻击风险", "关联网站安全指数数据"];
            var percent = 0;
            var oldI = -1;
            isAnimating = true;
            animationInterval = setInterval(function () {
                isAnimating = true;
                var i = parseInt(percent / 10, 10);
                if (oldI !== i) {
                    $(loadingStateText).text(map[i]);
                    oldI = i
                }
                if (!isAjaxLoading) {
                    if (percent > waitingStop) {
                        $(loadingBarFill).css({width: percent + "%"})
                    }
                    percent += 1
                }
                if (percent > 100) {
                    stopProgressAnimation();
                    showCheckedLayout();
                    showCheckingResultText();
                    updateRegisterSiteAddress()
                }
            }, 40)
        }

        function makeProgressFull() {
            if (isAnimating) {
                $(loadingBarFill).css({width: "100%"})
            }
        }

        function stopProgressAnimation() {
            clearInterval(animationInterval);
            isAnimating = false
        }

        function informReportGot(data) {
            reportData = preTreatData(data);
            dataInformer.inform(reportData)
        }

        function informRelatedSitesGot(data) {
            dataInformer.informRelateSites(data)
        }

        function informRelatedSitesStateGot(data) {
            dataInformer.informRelateSitesState(data)
        }

        function preTreatData(data) {
            data.siteAddress = $(checkInput).val();
            var detailReport = data.detail_report;
            for (var name in detailReport) {
                if (detailReport.hasOwnProperty(name)) {
                    var detailBlock = detailReport[name];
                    deleteInServiceRequiredDetail(detailBlock, data.is_in_service);
                    calCheckItemsCount(detailBlock);
                    detailReport[name] = constructDetailTable(detailBlock)
                }
            }
            return data
        }

        function deleteInServiceRequiredDetail(detailReportBlock, isInService) {
            var checkTypes = detailReportBlock.check_types;
            for (var i = 0, len = checkTypes.length; i < len; i++) {
                var currentCheckType = checkTypes[i];
                if (currentCheckType.in_service_required && !isInService) {
                    var checkItems = currentCheckType.check_items;
                    checkItems[0].result = "REGISTER_TIP";
                    checkItems[0].rowSpan = checkItems.length;
                    for (var j = 1, lenJ = checkItems.length; j < lenJ; j++) {
                        checkItems[j].result = undefined
                    }
                }
            }
        }

        function calCheckItemsCount(detailReportBlock) {
            var checkTypes = detailReportBlock.check_types;
            var totalCheckItemCount = 0;
            for (var i = 0, len = checkTypes.length; i < len; i++) {
                var checkItemCount = checkTypes[i].check_items.length;
                totalCheckItemCount += checkItemCount;
                checkTypes[i].checkItemCount = checkItemCount
            }
            detailReportBlock.checkItemCount = totalCheckItemCount
        }

        function constructDetailTable(detailReportBlock) {
            var result = [];
            var resultLen = result.length;
            for (var i = 0, len = detailReportBlock.check_types.length; i < len; i++) {
                var currentCheckType = detailReportBlock.check_types[i];
                result = result.concat(currentCheckType.check_items);
                result[resultLen].checkType = {
                    checkItemCount: currentCheckType.checkItemCount,
                    name: currentCheckType.name,
                    registerRequired: currentCheckType.in_service_required
                };
                resultLen = result.length
            }
            result[0].dimension = {
                score: detailReportBlock.score,
                scoreDelta: detailReportBlock.score_delta,
                checkItemCount: detailReportBlock.checkItemCount
            };
            return result
        }

        function performCheck(siteAddress) {
            checkCounter.increase();
            var requestNotAllowedMsg = getCheckRequestErrorMsg(siteAddress);
            if (requestNotAllowedMsg) {
                showErrorAndHideAll(requestNotAllowedMsg);
                return
            }
            resetLayout();
            showCheckingLayout(siteAddress);
            ajaxLoadReport(siteAddress);
            startProgressAnimation();
            ajaxLoadRelatedSites(siteAddress);
            ajaxLoadRelatedSiteState(siteAddress)
        }

        return {init: init}
    }

    function ShareCtrl() {
        var shareConfig = {
            common: {
                bdSnsKey: {},
                bdText: "",
                bdMini: "2",
                bdMiniList: ["mshare", "weixin", "tqq", "qzone", "tsina", "renren", "douban", "ty", "isohu"],
                bdPic: "",
                bdStyle: "0",
                bdSize: "16"
            }, slide: {type: "slide", bdImg: "6", bdPos: "right", bdTop: "177.5"}
        };

        function init(text) {
            shareConfig.common.bdText = text;
            window._bd_share_config = shareConfig;
            var share = isInitDone();
            if (!share) {
                with (document) {
                    0[(getElementsByTagName("head")[0] || body).appendChild(createElement("script")).src = "http://bdimg.share.baidu.com/static/api/js/share.js?cdnversion=" + ~(-new Date() / 3600000)]
                }
            } else {
                share.init(shareConfig)
            }
        }

        function isInitDone() {
            return window.bdShare || window._bd_share_main
        }

        return {init: init}
    }

    var checkSite = new CheckSite();
    checkSite.init()
})(window, jQuery);