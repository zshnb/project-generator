package com.zshnb.projectgenerator.web.common

class ListResponse<T>(val list: List<T>,
                      val total: Long) {
    companion object {
        fun <T> ok(list: List<T>): ListResponse<T> {
            return ListResponse(list, list.size.toLong())
        }
    }
}