package com.mctech.showcase.feature.heros.domain.error

import java.lang.RuntimeException

sealed class ComicError : RuntimeException(){
    object UnknownQuotationException : ComicError()
}