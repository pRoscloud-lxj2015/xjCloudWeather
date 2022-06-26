package com.iclound.xjcloudweather.base

import android.view.View

abstract class BaseMvpFragment<in V: IView, P : IPresenter<V>> : BaseFragment(), IView {

    protected var mPresenter: P? = null

    protected abstract fun createPresenter(): P

    override fun initView(view: View) {
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
        this.mPresenter = null
    }
}