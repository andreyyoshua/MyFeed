package com.iface.feed.utilities

import android.view.View
import androidx.databinding.BindingConversion

@BindingConversion
fun booleanToVisibility(value: Boolean?) = if (value == true) View.VISIBLE else View.GONE