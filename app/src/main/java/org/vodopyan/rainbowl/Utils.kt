@file:Suppress("PackageDirectoryMismatch")

package org.vodopyan.rainbowl.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) =
    observe(owner, Observer(observer))