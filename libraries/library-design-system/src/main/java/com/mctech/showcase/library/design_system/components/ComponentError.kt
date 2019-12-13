package com.mctech.showcase.library.design_system.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.mctech.showcase.library.design_system.R
import com.mctech.showcase.library.design_system.databinding.ComponentErrorBinding

class ComponentError @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val containerBinding = ComponentErrorBinding.inflate(LayoutInflater.from(context))

    init {

        attrs?.apply {
            val attributes = context.obtainStyledAttributes(this, R.styleable.ComponentError)
            containerBinding.tvTitle.text = attributes.getString(R.styleable.ComponentError_errorMessage) ?: context.getString(R.string.check_your_network)
            attributes.recycle()
        }

        containerBinding.root.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        addView(containerBinding.root)
    }

    override fun setOnClickListener(onClick: OnClickListener?) {
        containerBinding.btRetry.setOnClickListener(onClick)
    }
}