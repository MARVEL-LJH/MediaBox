package com.skyd.imomoe.model.impls

import com.skyd.imomoe.bean.AnimeCoverBean
import com.skyd.imomoe.bean.PageNumberBean
import com.skyd.imomoe.config.Api
import com.skyd.imomoe.model.util.JsoupUtil
import com.skyd.imomoe.model.util.ParseHtmlUtil
import com.skyd.imomoe.model.interfaces.IMonthAnimeModel
import com.skyd.imomoe.model.util.Pair
import org.jsoup.select.Elements
import java.util.ArrayList

class MonthAnimeModel : IMonthAnimeModel {
    override fun getMonthAnimeData(
        partUrl: String,
        callBack: IMonthAnimeModel.AllTabDataCallBack
    ): Pair<ArrayList<AnimeCoverBean>, PageNumberBean?>? {
        val monthAnimeList: ArrayList<AnimeCoverBean> = ArrayList()
        val url = Api.MAIN_URL + partUrl
        val document = JsoupUtil.getDocument(url)
        val areaElements: Elements = document.getElementsByClass("area")
        for (i in areaElements.indices) {
            val areaChildren: Elements = areaElements[i].children()
            for (j in areaChildren.indices) {
                when (areaChildren[j].className()) {
                    "lpic" -> {
                        monthAnimeList.addAll(ParseHtmlUtil.parseLpic(areaChildren[j], url))
                    }
                }
            }
        }
        return Pair(monthAnimeList, null)
    }
}