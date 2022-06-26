package com.iclound.xjcloudweather.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.iclound.xjcloudweather.event.MapLocationEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


abstract class BasePresenter<M : IModel, V : IView> : IPresenter<V>, LifecycleObserver {
    var mView: V? = null
    var mModel: M? = null

    var location: Location? = null

    private val isVisible: Boolean
         get() = mView != null

    private var mCompositeDisposable: CompositeDisposable? = null

    open fun createModel(): M? = null

    /**
     * 是否使用EventBus()
     */
    open fun useEventBus(): Boolean = false

    override fun attachView(view: V){
        this.mView = view
        mModel = createModel()
        if(mView is LifecycleOwner){
            (mView as LifecycleOwner).lifecycle.addObserver(this)
            if(mModel != null && mModel is LifecycleOwner){
                (mModel as LifecycleOwner).lifecycle.addObserver(mModel as LifecycleObserver)
            }
        }
        if(useEventBus())
            EventBus.getDefault().register(this)
    }

    override fun detachView()
    {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        unDispose()
        mModel?.onDetach()
        this.mView = null
        this.mModel = null
        this.mCompositeDisposable = null

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    open fun onMapLocationEvent(mapLocation: MapLocationEvent) {
//        location = mapLocation.location
//        EventBus.getDefault().post()
//    }

    private fun unDispose() {
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
    }

    @Deprecated("")
    open fun addSubscription(disposable: Disposable){
        if(mCompositeDisposable == null){
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let { mCompositeDisposable?.add(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory(owner: LifecycleOwner){
        owner.lifecycle.removeObserver(this)
    }
}