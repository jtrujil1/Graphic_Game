name := "GraphicalGame"
version := "1.0"
scalaVersion := "2.12.10"
run / fork := true
run / connectInput := true
scalacOptions += "-Ylog-classpath"

libraryDependencies ++= Seq(
//	"org.scalafx" %% "scalafx" % "8.0.192-R14",
	"org.scalafx" %% "scalafx" % "12.0.2-R18",
	"com.novocode" % "junit-interface" % "0.11" % Test,
	"org.scalactic" %% "scalactic" % "3.0.8",
	"org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add JavaFX dependencies
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m=>
  "org.openjfx" % s"javafx-$m" % "12.0.2" classifier osName
)