package com.backwards.adt

import cats.Show
import cats.implicits._
import com.backwards.adt.BarShow._

object FooShow {
  implicit val fooShow: Show[Foo] = Show[Foo] { foo =>
    s"Foo: thing = ${foo.thing}, bar = ${foo.bar.show}"
  }
}