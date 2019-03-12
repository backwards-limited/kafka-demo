package com.backwards.collection

import java.util
import scala.collection.JavaConverters._
import scala.language.implicitConversions

object JavaOps extends JavaOps

trait JavaOps {
  implicit def toJavaCollection[T](t: T): util.Collection[T] = toJavaCollection(Seq(t))

  implicit def toJavaCollection[T](ts: Seq[T]): util.Collection[T] = ts.asJavaCollection

  implicit def toJava[K, V](m: Map[K, V]): util.Map[K, V] = m.asJava
}