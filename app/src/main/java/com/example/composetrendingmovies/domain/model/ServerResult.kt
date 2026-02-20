package com.example.composetrendingmovies.domain.model

data class ServerResult<out T>(
    val status: Status,
    val data: T?,
    val error: ErrorResponse?,
    val message: String?
) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): ServerResult<T> {
            return ServerResult(
                status = Status.SUCCESS,
                data = data,
                error = null,
                message = null
            )
        }

        fun <T> error(message: String, error: ErrorResponse?): ServerResult<T> {
            return ServerResult(
                status = Status.ERROR,
                data = null,
                error = error,
                message = message
            )
        }

        fun <T> loading(data: T? = null): ServerResult<T> {
            return ServerResult(
                status = Status.LOADING,
                data = data,
                error = null,
                message = null
            )
        }
    }

    override fun toString(): String {
        return "ServerResponse(status = $status, error = $error, data = $data, message = $message)"
    }
}