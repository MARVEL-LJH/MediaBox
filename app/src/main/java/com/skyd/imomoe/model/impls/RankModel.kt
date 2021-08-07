package com.skyd.imomoe.model.impls

import com.skyd.imomoe.bean.TabBean
import com.skyd.imomoe.config.Api
import com.skyd.imomoe.model.DataSourceManager
import com.skyd.imomoe.model.util.JsoupUtil
import com.skyd.imomoe.model.util.ParseHtmlUtil
import com.skyd.imomoe.model.interfaces.IRankModel
import org.jsoup.select.Elements
import java.util.*

class RankModel : IRankModel {
    private var bgTimes = 0
    private var tabList: ArrayList<TabBean> = ArrayList()

    override fun getRankTabData(callBack: IRankModel.RankTabDataCallBack): ArrayList<TabBean>? {
        tabList.clear()
        getWeekRankData()
        getAllRankData()
        return tabList
    }

    private fun getAllRankData() {
        val const = DataSourceManager.getConst() ?: Const()
        val document = JsoupUtil.getDocument(Api.MAIN_URL + const.actionUrl.ANIME_RANK())
        val areaChildren: Elements = document.select("[class=area]")[0].children()
        for (i in areaChildren.indices) {
            when (areaChildren[i].className()) {
                "gohome" -> {
                    tabList.add(
                        tabList.size, TabBean(
                            "",
                            const.actionUrl.ANIME_RANK(),
                            "",
                            areaChildren[i].select("h1").select("a").text()
                        )
                    )
                }
            }
        }
    }

    private fun getWeekRankData() {
        bgTimes = 0
        val url = Api.MAIN_URL
        val document = JsoupUtil.getDocument(url)
        val areaChildren: Elements = document.select("[class=area]")[0].children()
        for (i in areaChildren.indices) {
            when (areaChildren[i].className()) {
                "side r" -> {
                    val sideRChildren = areaChildren[i].children()
                    for (j in sideRChildren.indices) {
                        when (sideRChildren[j].className()) {
                            "bg" -> {
                                if (bgTimes++ == 0) continue

                                val bgChildren = sideRChildren[j].children()
                                for (k in bgChildren.indices) {
                                    when (bgChildren[k].className()) {
                                        "dtit" -> {
                                            tabList.add(
                                                0,
                                                TabBean(
                                                    "",
                                                    "/",
                                                    "",
                                                    ParseHtmlUtil.parseDtit(bgChildren[k])
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
