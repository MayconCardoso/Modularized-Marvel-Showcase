package com.mctech.showcase.feature.heros.domain.error

import java.lang.RuntimeException

object NetworkException : RuntimeException("Algo inesperado aconteceu enquanto tentava conectar no servidor. Tente novamente em alguns segundos.")