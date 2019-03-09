package com.backwards.duration

import scala.concurrent.duration.Duration

object DurationOps {
  implicit val toJavaDuration: Duration => java.time.Duration =
    d => java.time.Duration.ofMillis(d.toMillis)
}