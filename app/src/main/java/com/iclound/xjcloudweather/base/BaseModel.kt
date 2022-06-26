package com.iclound.xjcloudweather.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseModel : IModel, LifecycleObserver {

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun addDisposable(disposable: Disposable) {
        if(mCompositeDisposable == null)
            mCompositeDisposable = CompositeDisposable()
        disposable?.let { mCompositeDisposable?.add(it) }
    }

    override fun onDetach() {
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestory(owner: LifecycleOwner){
        owner.lifecycle.removeObserver(this)
    }
}