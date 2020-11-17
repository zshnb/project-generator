package com.zshnb.projectgenerator.web.exception

import java.lang.RuntimeException

class InvalidArgumentsException(val msg: String) : RuntimeException(msg) {
}