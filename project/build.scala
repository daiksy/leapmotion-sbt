/*
 *  (c)FURYU CORP. 2013. All rights reserved.
 */
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

object leapmotion extends Build {
  val appName = "leapmotionOnScala"
  val appOrganization = "com.github.daiksy.leapmotion"
  val appVersion  = "0.1.0-SNAPSHOT"
  val appScalaVersion = "2.10.2"

  val main = Project(
    appName,
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      organization := appOrganization,
      version := appVersion,
      scalaVersion := appScalaVersion,
      fork := true,
      javaOptions ++= Seq(
        "-Djava.library.path=.:./lib"
      ),
      connectInput in run := true
    ) ++ formatSettings
  ).settings(SbtScalariform.scalariformSettings: _*)

  lazy val formatSettings = Seq(
    ScalariformKeys.preferences := FormattingPreferences()
    .setPreference(IndentWithTabs, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(PreserveDanglingCloseParenthesis, true)
  )

}
