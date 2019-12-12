package com.mctech.showcase.library.networking

sealed class NetworkError : Exception() {
    object ClientException          : NetworkError()
    object RemoteException          : NetworkError()

    object HostUnreachable          : NetworkError()
    object OperationTimeout         : NetworkError()
    object ConnectionSpike          : NetworkError()
    object UnknownNetworkingError   : NetworkError()

    override fun toString() =
        when (this) {
            ClientException         -> "Issue originated from client"
            RemoteException         -> "Issue incoming from server"

            HostUnreachable         -> "Cannot reach remote host"
            OperationTimeout        -> "Networking operation timed out"
            ConnectionSpike         -> "In-flight networking operation interrupted"
            UnknownNetworkingError  -> "Fatal networking exception"
        }
}