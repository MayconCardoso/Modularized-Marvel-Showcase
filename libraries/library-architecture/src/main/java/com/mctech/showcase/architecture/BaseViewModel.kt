package com.mctech.showcase.architecture

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mctech.showcase.library.logger.Logger
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext.get
import java.lang.reflect.Modifier

open class BaseViewModel : ViewModel() {
    val logger: Logger by get().koin.inject()

    private val userFlowInteraction = mutableListOf<UserInteraction>()

    /**
     * Called by view to send 'an interaction' to the view model.
     */
    fun interact(userInteraction: UserInteraction) {
        viewModelScope.launch {
            suspendedInteraction(userInteraction)
        }
    }

    /**
     * Called by view to send 'a suspended interaction' to the view model.
     * It's called when the suspended function must be connect with the view life cycle.
     */
    suspend fun suspendedInteraction(userInteraction: UserInteraction) {
        userFlowInteraction.add(userInteraction)
        handleUserInteraction(userInteraction)
    }

    @VisibleForTesting(otherwise = Modifier.PROTECTED)
    open suspend fun handleUserInteraction(interaction: UserInteraction) = Unit

    fun reprocessLastInteraction() {
        viewModelScope.launch {
            handleUserInteraction(userFlowInteraction.last())
        }
    }

    suspend fun suspendedreprocessLastInteraction() {
        handleUserInteraction(userFlowInteraction.last())
    }

    override fun onCleared() {
        userFlowInteraction.clear()
        super.onCleared()
    }
}