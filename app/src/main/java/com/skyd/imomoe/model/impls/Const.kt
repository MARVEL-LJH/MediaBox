package com.skyd.imomoe.model.impls

import com.skyd.imomoe.model.interfaces.IConst

class Const : IConst {
    private val actionUrl: IConst.IActionUrl = ActionUrl()

    class ActionUrl : IConst.IActionUrl {
        override fun ANIME_RANK(): String = "/top/"
        override fun ANIME_PLAY(): String = "/v/"
        override fun ANIME_DETAIL(): String = "/show/"
        override fun ANIME_SEARCH(): String = "/search/"
    }

    override fun MAIN_URL(): String? = "http://www.yhdm.so"

    override fun about(): String {
        return "默认数据源\n数据来源：${MAIN_URL()}"
    }

    override fun getActionUrl(): IConst.IActionUrl = actionUrl
}
