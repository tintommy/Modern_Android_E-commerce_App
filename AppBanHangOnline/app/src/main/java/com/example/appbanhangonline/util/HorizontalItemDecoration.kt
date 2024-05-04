package com.example.appbanhangonline.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.time.temporal.TemporalAmount

class HorizontalItemDecoration(private val amount: Int = 15) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = amount
    }}