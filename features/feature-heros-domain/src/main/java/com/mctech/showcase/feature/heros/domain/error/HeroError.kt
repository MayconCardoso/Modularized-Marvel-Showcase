package com.mctech.showcase.feature.heros.domain.error

import java.lang.RuntimeException

sealed class HeroError : RuntimeException(){
    object UnknownQuotationException : HeroError()
}