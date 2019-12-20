package nl.codestar.logging

import org.slf4j.LoggerFactory
import org.slf4j.Marker
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName
import org.slf4j.Logger as Slf4jLogger

interface LoggerLike {
  val logger: Slf4jLogger
  val enabled: Boolean
  val marker: Marker?

  fun isTraceEnabled(): Boolean = enabled && if(marker != null) logger.isTraceEnabled(marker) else logger.isTraceEnabled
  fun isDebugEnabled(): Boolean = enabled && if(marker != null) logger.isDebugEnabled(marker) else logger.isDebugEnabled
  fun isInfoEnabled(): Boolean = enabled && if(marker != null) logger.isInfoEnabled(marker) else logger.isInfoEnabled
  fun isWarnEnabled(): Boolean = enabled && if(marker != null) logger.isWarnEnabled(marker) else logger.isWarnEnabled
  fun isErrorEnabled(): Boolean = enabled && if(marker != null) logger.isErrorEnabled(marker) else logger.isErrorEnabled

  fun trace(message: String) {
    if (isTraceEnabled()) {
      logger.trace(message)
    }
  }

  fun trace(message: String, error: Throwable) {
    if (isTraceEnabled()) {
      logger.trace(message, error)
    }
  }

  fun debug(message: String) {
    if (isDebugEnabled()) {
      logger.debug(message)
    }
  }

  fun debug(message: String, error: Throwable) {
    if (isDebugEnabled()) {
      logger.debug(message, error)
    }
  }

  fun info(message: String) {
    if (isInfoEnabled()) {
      logger.info(message)
    }
  }

  fun info(message: String, error: Throwable) {
    if (isInfoEnabled()) {
      logger.info(message, error)
    }
  }

  fun warn(message: String) {
    if (isWarnEnabled()) {
      logger.warn(message)
    }
  }

  fun warn(message: String, error: Throwable) {
    if (isWarnEnabled()) {
      logger.warn(message, error)
    }
  }

  fun error(message: String) {
    if (isErrorEnabled()) {
      logger.error(message)
    }
  }

  fun error(message: String, error: Throwable) {
    if (isErrorEnabled()) {
      logger.error(message, error)
    }
  }
}

interface Logging {
  val logger: Logger
    get() = Logger(this::class)
}

class Logger(override val logger: Slf4jLogger, override val enabled: Boolean = true, override val marker: Marker? = null) : LoggerLike {
  constructor(name: String): this(LoggerFactory.getLogger(name))
  constructor(kClass: KClass<*>): this(LoggerFactory.getLogger(kClass.jvmName))

  fun withMarker(marker: Marker): Logger = Logger(logger, enabled, marker)
}
