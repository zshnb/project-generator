package com.zshnb.projectgenerator.web.common

data class Response<T>(var data: T? = null,
                       var message: String = "") {

    companion object {
        fun <T> ok(data: T): Response<T> {
            return Response(data)
        }

        fun ok(): Response<String> {
            return Response()
        }

        fun error(message: String): Response<String> {
            return Response(message = message)
        }
    }
}