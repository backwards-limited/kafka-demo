package com.backwards.adt

import cats.Show

object BarShow {
  implicit val barShow: Show[Bar] = Show[Bar] { bar =>
    s"Bar: stuff = ${bar.stuff}"
  }
}