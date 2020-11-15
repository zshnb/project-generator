package com.zshnb.web.exception

import java.lang.RuntimeException

class InvalidArgumentsException(val msg: String) : RuntimeException(msg) {
}