package com.iclound.xjcloudweather.base


abstract class BaseMvpActivity<in V: IView, P: IPresenter<V>>: BaseActivity(), IView {
    /**
     * Presenter
     */
    protected var mPresenter: P? = null

    protected abstract fun createPresenter(): P

    override fun initView() {
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

}