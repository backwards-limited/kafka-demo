package com.backwards.console

import fansi.Color._
import fansi.EscapeAttr

trait Console {
  lazy val captionColour: EscapeAttr = Magenta
  lazy val messagesColour: EscapeAttr = Green

  def out(caption: String, messages: Any*): Unit = {
    println(captionColour(s"\n$caption:"))
    println(messagesColour(messages mkString "\n"))
  }
}