package com.iclound.xjcloudweather.widgets

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.iclound.xjcloudweather.R
import kotlinx.android.synthetic.main.dialog_permissions.*
import kotlinx.android.synthetic.main.dialog_permissions.view.*

class PermissionDialog : DialogFragment() {
    companion object {
        val TITLE = "title"
        val CONTENT = "content"
        val RIGHT_TEXT = "right_text"
        val CANCEL_ABLE = "cancel_able"

        fun newInstance(builder: Builder): PermissionDialog {
            val bundle = Bundle()
            val fragment = PermissionDialog()
            bundle.putString(TITLE, builder.title)
            bundle.putString(CONTENT, builder.content)
            bundle.putString(RIGHT_TEXT, builder.rightText)
            fragment.arguments = bundle
            fragment.isCancelable = builder.cancelAble
            return fragment
        }

        fun newBuilder(): Builder {
            return Builder()
        }
    }

    private var onConfirmClickListener: (() -> Unit)? = null
    private var onCancelClickListener: (() -> Unit)? = null

    fun setOnConfirmClickListener(click: (() -> Unit)): PermissionDialog {
        this.onConfirmClickListener = click
        return this
    }

    fun setOnCancelClickListener(click: (() -> Unit)): PermissionDialog {
        this.onCancelClickListener = click
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return@OnKeyListener true
            }
            false
        })

        val rootView = inflater.inflate(R.layout.dialog_permissions, container, false)
        initView(rootView)
        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog = activity?.let { Dialog(it, R.style.BottomDialog) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_permissions)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    fun initView(view: View) {
        val title = this.arguments?.getString(TITLE)
        if (title.isNullOrEmpty().not()) {
            view.dg_title.visibility = View.VISIBLE
            view.dg_title?.text = title
        }
        val content = this.arguments?.getString(CONTENT)
        if (content.isNullOrEmpty().not()) {
            view.dg_content?.visibility = View.VISIBLE
            view.dg_content?.text = content
        }
        val rightText = this.arguments?.getString(RIGHT_TEXT)
        if (rightText.isNullOrEmpty().not()) {
            view.dg_confirm?.text = rightText
        }

        view.dg_confirm?.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(v: View?) {
                onConfirmClickListener?.invoke()
                dismiss()
            }
        })
        view.dg_cancel?.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(v: View?) {
                onCancelClickListener?.invoke()
                dismiss()
            }
        })
    }

    class Builder {
        private var mConfirmDialog: PermissionDialog? = null
        var title: String = ""
        var content: String = ""
        var rightText: String = ""
        var cancelAble: Boolean = false

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setContent(content: String): Builder {
            this.content = content
            return this
        }

        fun setRightText(rightText: String): Builder {
            this.rightText = rightText
            return this
        }

        fun setCancelAble(cancelAble: Boolean): Builder {
            this.cancelAble = cancelAble
            return this
        }

        fun build(): PermissionDialog? {
            if (this.mConfirmDialog == null) {
                this.mConfirmDialog = newInstance(this)
            }
            return this.mConfirmDialog
        }
    }
}