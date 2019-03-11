package com.backwards.serialization

case class DeserializationException(cause: Throwable) extends Exception(cause)