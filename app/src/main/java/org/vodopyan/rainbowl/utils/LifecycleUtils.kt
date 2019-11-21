package org.vodopyan.rainbowl.utils

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * Casts [context] as [LifecycleOwner].
 * Use in views:
 *     val parent = parentLifecycle(context)
 *
 * @param context Context passed down to your view in constructor
 * @throws IllegalArgumentException if [context] is not a [LifecycleOwner]
 * */
fun View.parentLifecycle(context: Context): LifecycleOwner {
    if(isInEditMode)
        return buildStubLifecycleOwner()

    if(context !is LifecycleOwner)
        throw IllegalArgumentException("By convention context of a view must be lifecycle-aware. " +
                "Got ${context::class.java.simpleName}, " +
                "which is not ${LifecycleOwner::class.java.simpleName}")

    return context
}

/**
 * Creates a [LifecycleOwner] that stays in one state determined by [startWith] event
 * */
fun buildStubLifecycleOwner(startWith: Lifecycle.Event = Lifecycle.Event.ON_CREATE) =
    object : LifecycleOwner {
        private val lifecycle = LifecycleRegistry(this)
        override fun getLifecycle() = lifecycle
        init {
            lifecycle.handleLifecycleEvent(startWith)
        }
    }