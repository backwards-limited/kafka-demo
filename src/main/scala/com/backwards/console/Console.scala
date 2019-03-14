package com.backwards.console

import fansi.Color._
import fansi.EscapeAttr

trait Console {
  lazy val captionColour: EscapeAttr = LightBlue
  lazy val messagesColour: EscapeAttr = Green

  def out(caption: String, messages: Any*): Unit = {
    println(captionColour(s"\n$caption:"))
    println(messagesColour(messages mkString "\n"))
  }
}