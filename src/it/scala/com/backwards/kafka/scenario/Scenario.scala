package com.backwards.kafka.scenario

import scala.reflect.ClassTag

trait Scenario extends ScenarioFixture {
  out("Boot", getClass.getName.replaceAll("\\$$", ""))

  def topicOf[C: ClassTag]: String =
    hyphenated(
      splitCamelCase(
        implicitly[ClassTag[C]].runtimeClass.getSimpleName.replaceAll("\\$$", "")
      )
    )

  def splitCamelCase(s: String): String =
    s.replaceAll(
      String.format("%s|%s|%s",
        "(?<=[A-Z])(?=[A-Z][a-z])",
        "(?<=[^A-Z])(?=[A-Z])",
        "(?<=[A-Za-z])(?=[^A-Za-z])"
      ),
      " "
    ).replaceAll("  ", " ")

  def hyphenated(s: String, lowerCase: Boolean = true): String =
    (if (lowerCase) s.toLowerCase else s).replaceAll("\\s+", "-")
}