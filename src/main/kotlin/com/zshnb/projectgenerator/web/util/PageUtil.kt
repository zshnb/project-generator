package com.zshnb.projectgenerator.web.util

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.zshnb.projectgenerator.web.request.PageRequest

class PageUtil {
    companion object {
        fun <T> ofRequest(request: PageRequest): Page<T> {
            return Page(request.pageNumber, request.pageSize)
        }
    }
}