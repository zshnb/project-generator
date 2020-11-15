package com.zshnb.web.util

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.zshnb.web.request.PageRequest

class PageUtil {
    companion object {
        fun <T> ofRequest(request: PageRequest): Page<T> {
            return Page(request.pageNumber, request.pageSize)
        }
    }
}