name := "Homework3"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.0.1",
  "org.scalatest"   %% "scalatest"    % "2.2.4"   % "test"
)

// https://mvnrepository.com/artifact/com.typesafe.play/play-json_2.10
libraryDependencies += "com.typesafe.play" % "play-json_2.11" % "2.4.0-M1"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-testkit_2.11
libraryDependencies += "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.12"

libraryDependencies += "com.novocode" % "junit-interface" % "0.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % "test"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

// https://mvnrepository.com/artifact/org.eclipse.jdt/core
libraryDependencies += "org.eclipse.jdt" % "core" % "3.3.0-v_771"

// https://mvnrepository.com/artifact/org.scala-lang/scala-swing
libraryDependencies += "org.scala-lang" % "scala-swing" % "2.11.0-M7"
