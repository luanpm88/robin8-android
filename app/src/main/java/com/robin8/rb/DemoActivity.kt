//package com.robin8.rb
//
//import android.app.Activity
//import android.os.Bundle
//import android.widget.ScrollView
//
//import com.handmark.pulltorefresh.library.PullToRefreshBase
//import kotlinx.android.synthetic.main.activity_demo.*
//
//class DemoActivity : Activity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_demo)
//    refresh_scrollview.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("最后更新 2019年03月20日17:32:30")
//    refresh_scrollview.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新")
//    refresh_scrollview.getLoadingLayoutProxy(true, false).setRefreshingLabel("R8引擎加载中")
//    refresh_scrollview.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新")
//
//        //        scrollView.getLoadingLayoutProxy(false,true).setLastUpdatedLabel("UpdatedLabel");
//        //        scrollView.getLoadingLayoutProxy(false,true).setPullLabel("PullLabel");
//        //        scrollView.getLoadingLayoutProxy(false,true).setRefreshingLabel("RefreshingLabel");
//        //        scrollView.getLoadingLayoutProxy(false,true).setReleaseLabel("ReleaseLabel");
//    refresh_scrollview.mode = PullToRefreshBase.Mode.BOTH
//    refresh_scrollview.setOnRefreshListener(object : PullToRefreshBase.OnRefreshListener2<ScrollView> {
//            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ScrollView>) {
//                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("最后更新 2019年03月20日17:32:30")
//                refresh_scrollview.postDelayed({ refresh_scrollview.onRefreshComplete() }, 2000)
//
//            }
//
//            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ScrollView>) {
//                refresh_scrollview.postDelayed({
//                    //                        refreshView.getLoadingLayoutProxy(false,true).setLastUpdatedLabel("2222UpdatedLabel");
//                    //                        refreshView.getLoadingLayoutProxy(false,true).setPullLabel("2222PullLabel");
//                    //                        refreshView.getLoadingLayoutProxy(false,true).setRefreshingLabel("2222RefreshingLabel");
//                    //                        refreshView.getLoadingLayoutProxy(false,true).setReleaseLabel("2222ReleaseLabel");
//                    refresh_scrollview.onRefreshComplete()
//                }, 2000)
//
//            }
//        })
//    }
//}
