import scala.sys.process._
import scala.util.Try

ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / organization     := "it.pagopa"
ThisBuild / organizationName := "Pagopa S.p.A."
ThisBuild / version          := ComputeVersion.version
ThisBuild / githubOwner      := "pagopa"
ThisBuild / githubRepository := "interop-selfcare-proxy-clients"
ThisBuild / resolvers += Resolver.githubPackages("pagopa")

val generateCode  = taskKey[Unit]("A task for generating the code starting from the swagger definition")
val packagePrefix = settingKey[String]("The package prefix derived from the uservice name")
val projectName   = settingKey[String]("The project name prefix derived from the uservice name")

lazy val root = (project in file("."))
  .settings(name := "interop-selfcare-proxy-clients", publish / skip := true)
  .aggregate(selfcareV2Client)

cleanFiles += baseDirectory.value / "selfcare-v2-client" / "src"
cleanFiles += baseDirectory.value / "selfcare-v2-client" / "target"

lazy val selfcareV2Client = project
  .in(file("selfcare-v2-client"))
  .settings(
    name                := "interop-selfcare-v2-client",
    packagePrefix       := name.value
      .replaceFirst("interop-", "interop.")
      .replaceFirst("selfcare-", "selfcare.")
      .replaceFirst("v2-", "v2.")
      .replaceAll("-", ""),
    projectName         := name.value
      .replaceFirst("interop-", "")
      .replaceFirst("selfcare-", ""),
    generateCode        := {
      Process(s"""openapi-generator-cli generate -t template/scala-akka-http-client
                 |                               -i selfcare-v2-client/interface-specification.yml
                 |                               -g scala-akka
                 |                               -p projectName=${projectName.value}
                 |                               -p invokerPackage=it.pagopa.${packagePrefix.value}.invoker
                 |                               -p modelPackage=it.pagopa.${packagePrefix.value}.model
                 |                               -p apiPackage=it.pagopa.${packagePrefix.value}.api
                 |                               -p modelPropertyNaming=original
                 |                               -p dateLibrary=java8
                 |                               -o selfcare-v2-client""".stripMargin).!!
    },
    scalacOptions       := Seq(),
    libraryDependencies := Dependencies.Jars.client,
    updateOptions       := updateOptions.value.withGigahorse(false)
  )
(Compile / compile) := ((Compile / compile) dependsOn selfcareV2Client / generateCode).value

