import scala.sys.process._
import scala.util.Try

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / organization     := "it.pagopa"
ThisBuild / organizationName := "Pagopa S.p.A."
ThisBuild / version          := ComputeVersion.version

ThisBuild / resolvers += "Pagopa Nexus Snapshots" at s"https://${System.getenv("MAVEN_REPO")}/nexus/repository/maven-snapshots/"
ThisBuild / resolvers += "Pagopa Nexus Releases" at s"https://${System.getenv("MAVEN_REPO")}/nexus/repository/maven-releases/"
ThisBuild / credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

val generateCode  = taskKey[Unit]("A task for generating the code starting from the swagger definition")
val packagePrefix = settingKey[String]("The package prefix derived from the uservice name")
val projectName   = settingKey[String]("The project name prefix derived from the uservice name")

lazy val root = (project in file("."))
  .settings(name := "interop-selfcare-proxy-clients", publish / skip := true)
  .aggregate(partyProcessClient, partyManagementClient, userRegistryClient)

cleanFiles += baseDirectory.value / "party-process-client" / "src"
cleanFiles += baseDirectory.value / "party-process-client" / "target"

lazy val partyProcessClient = project
  .in(file("party-process-client"))
  .settings(
    name                := "interop-selfcare-party-process-client",
    packagePrefix       := name.value
      .replaceFirst("interop-", "interop.")
      .replaceFirst("selfcare-", "selfcare.")
      .replaceFirst("party-process-", "partyprocess-.")
      .replaceAll("-", ""),
    projectName         := name.value
      .replaceFirst("interop-", "")
      .replaceFirst("selfcare-", ""),
    generateCode        := {
      Process(s"""openapi-generator-cli generate -t template/scala-akka-http-client
                 |                               -i party-process-client/interface-specification.yml
                 |                               -g scala-akka
                 |                               -p projectName=${projectName.value}
                 |                               -p invokerPackage=it.pagopa.${packagePrefix.value}.invoker
                 |                               -p modelPackage=it.pagopa.${packagePrefix.value}.model
                 |                               -p apiPackage=it.pagopa.${packagePrefix.value}.api
                 |                               -p modelPropertyNaming=original
                 |                               -p dateLibrary=java8
                 |                               -p entityStrictnessTimeout=15
                 |                               -o party-process-client""".stripMargin).!!
    },
    scalacOptions       := Seq(),
    libraryDependencies := Dependencies.Jars.client,
    updateOptions       := updateOptions.value.withGigahorse(false),
    publishTo           := {
      val nexus = s"https://${System.getenv("MAVEN_REPO")}/nexus/repository/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "maven-snapshots/")
      else
        Some("releases" at nexus + "maven-releases/")
    }
  )

cleanFiles += baseDirectory.value / "party-management-client" / "src"
cleanFiles += baseDirectory.value / "party-management-client" / "target"

lazy val partyManagementClient = project
  .in(file("party-management-client"))
  .settings(
    name                := "interop-selfcare-party-management-client",
    packagePrefix       := name.value
      .replaceFirst("interop-", "interop.")
      .replaceFirst("selfcare-", "selfcare.")
      .replaceFirst("party-management-", "partymanagement.")
      .replaceAll("-", ""),
    projectName         := name.value
      .replaceFirst("interop-", "")
      .replaceFirst("selfcare-", ""),
    generateCode        := {
      Process(s"""openapi-generator-cli generate -t template/scala-akka-http-client
                 |                               -i party-management-client/interface-specification.yml
                 |                               -g scala-akka
                 |                               -p projectName=${projectName.value}
                 |                               -p invokerPackage=it.pagopa.${packagePrefix.value}.invoker
                 |                               -p modelPackage=it.pagopa.${packagePrefix.value}.model
                 |                               -p apiPackage=it.pagopa.${packagePrefix.value}.api
                 |                               -p modelPropertyNaming=original
                 |                               -p dateLibrary=java8
                 |                               -o party-management-client""".stripMargin).!!
    },
    scalacOptions       := Seq(),
    libraryDependencies := Dependencies.Jars.client,
    updateOptions       := updateOptions.value.withGigahorse(false),
    publishTo           := {
      val nexus = s"https://${System.getenv("MAVEN_REPO")}/nexus/repository/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "maven-snapshots/")
      else
        Some("releases" at nexus + "maven-releases/")
    }
  )

cleanFiles += baseDirectory.value / "user-registry-client" / "src"
cleanFiles += baseDirectory.value / "user-registry-client" / "target"

lazy val userRegistryClient = project
  .in(file("user-registry-client"))
  .settings(
    name                := "interop-selfcare-user-registry-client",
    packagePrefix       := name.value
      .replaceFirst("interop-", "interop.")
      .replaceFirst("selfcare-", "selfcare.")
      .replaceFirst("user-registry-", "userregistry.")
      .replaceAll("-", ""),
    projectName         := name.value
      .replaceFirst("interop-", "")
      .replaceFirst("selfcare-", ""),
    generateCode        := {
      Process(s"""openapi-generator-cli generate -t template/scala-akka-http-client
                 |                               -i user-registry-client/interface-specification.yml
                 |                               -g scala-akka
                 |                               -p projectName=${projectName.value}
                 |                               -p invokerPackage=it.pagopa.${packagePrefix.value}.invoker
                 |                               -p modelPackage=it.pagopa.${packagePrefix.value}.model
                 |                               -p apiPackage=it.pagopa.${packagePrefix.value}.api
                 |                               -p modelPropertyNaming=original
                 |                               -p dateLibrary=java8
                 |                               -o user-registry-client""".stripMargin).!!
    },
    scalacOptions       := Seq(),
    libraryDependencies := Dependencies.Jars.client,
    updateOptions       := updateOptions.value.withGigahorse(false),
    publishTo           := {
      val nexus = s"https://${System.getenv("MAVEN_REPO")}/nexus/repository/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "maven-snapshots/")
      else
        Some("releases" at nexus + "maven-releases/")
    }
  )

(Compile / compile) := ((Compile / compile) dependsOn partyProcessClient / generateCode).value
(Compile / compile) := ((Compile / compile) dependsOn partyManagementClient / generateCode).value
(Compile / compile) := ((Compile / compile) dependsOn userRegistryClient / generateCode).value
