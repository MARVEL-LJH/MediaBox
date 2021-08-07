package com.skyd.imomoe.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skyd.imomoe.bean.AnimeCoverBean
import com.skyd.imomoe.bean.PageNumberBean
import com.skyd.imomoe.model.DataSourceManager
import com.skyd.imomoe.model.impls.RankListModel
import com.skyd.imomoe.model.interfaces.IRankListModel
import com.skyd.imomoe.util.Util.showToastOnThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class RankListViewModel : ViewModel() {
    private val rankModel: IRankListModel by lazy {
        DataSourceManager.create(IRankListModel::class.java) ?: RankListModel()
    }
    var isRequesting = false
    var rankList: MutableList<AnimeCoverBean> = Collections.synchronizedList(ArrayList())
    var pageNumberBean: PageNumberBean? = null
    var mldRankData: MutableLiveData<Int> = MutableLiveData()
    var newPageIndex: Pair<Int, Int>? = null

    fun getRankListData(partUrl: String, isRefresh: Boolean = true) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (isRequesting) return@launch
                isRequesting = true

                rankModel.getRankListData(partUrl, object : IRankListModel.RankListDataCallBack {
                    override fun onSuccess(p: com.skyd.imomoe.model.util.Pair<MutableList<AnimeCoverBean>, PageNumberBean>) {
                        if (isRefresh) rankList.clear()
                        val positionStart = rankList.size
                        rankList.addAll(p.first)
                        pageNumberBean = p.second
                        newPageIndex = Pair(positionStart, rankList.size - positionStart)
                        mldRankData.postValue(if (isRefresh) 0 else 1)
                        isRequesting = false
                    }

                    override fun onError(e: Exception) {
                        mldRankData.postValue(-1)
                        isRequesting = false
                        e.printStackTrace()
                        e.message?.showToastOnThread(Toast.LENGTH_LONG)
                    }

                }).apply {
                    this ?: return@launch
                    if (isRefresh) rankList.clear()
                    val positionStart = rankList.size
                    rankList.addAll(first)
                    pageNumberBean = second
                    newPageIndex = Pair(positionStart, rankList.size - positionStart)
                    mldRankData.postValue(if (isRefresh) 0 else 1)
                    isRequesting = false
                }
            } catch (e: Exception) {
                mldRankData.postValue(-1)
                isRequesting = false
                e.printStackTrace()
                e.message?.showToastOnThread(Toast.LENGTH_LONG)
            }
        }
    }

    companion object {
        const val TAG = "RankViewModel"
    }
}