#!/usr/bin/env kscript
//KOTLIN_OPTS -cp /usr/lib/jvm/java/jre/lib/ext/jfxrt.jar

@file:DependsOn("ch.ifocusit:plantuml-builder:1.5-SNAPSHOT")
@file:DependsOn("org.reflections:reflections:0.9.11")
@file:DependsOn("com.adrenalinici:adrenalina-common:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-client:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-gui:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-cli:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-server:1.0-SNAPSHOT")

import ch.ifocusit.plantuml.classdiagram.ClassDiagramBuilder
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder

import java.io.File

fun renderUML(vararg packages: String): String {
  val builder = ClassDiagramBuilder()

  val m = packages.map {pkgName ->
    val reflections = Reflections(
      ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage(pkgName))
        .setScanners(SubTypesScanner(false))
        .filterInputsBy(FilterBuilder().includePackage(pkgName))
    );

    val classes = reflections
      .getSubTypesOf(Object::class.java)
      .filter { it.`package`.name.equals(pkgName) }
      .filter { !it.isAnonymousClass }
      .filter { it.enclosingClass == null }
      .toSet()

    val pkg = Package.getPackage(pkgName)

    pkg to classes
  }.toMap()

  m.forEach {(pkg, classes) ->
    println("Adding package ${pkg.name}")
    builder
      .addPackageWithClasses(pkg, classes)
  }

  return builder.build()
}

fun writeUML(filename: String, vararg packages: String) {
  println("Starting generation of $filename")
  File(filename).printWriter().use { out -> out.print(renderUML(*packages)) }
  println("End generation of $filename")
}

fun renderImage(filename: String) {
  println("Starting rendering of $filename")
  Runtime.getRuntime().exec("bash plantuml_run.sh $filename").waitFor()
  println("End rendering of $filename")
}

println("Java version ${System.getProperty("java.version")}")

writeUML("deliveries/final/uml/adrenaline-common.puml",
  "com.adrenalinici.adrenaline.common.model.event",
  "com.adrenalinici.adrenaline.common.model.light",
  "com.adrenalinici.adrenaline.common.model",
  "com.adrenalinici.adrenaline.common.network.inbox",
  "com.adrenalinici.adrenaline.common.network.outbox",
  "com.adrenalinici.adrenaline.common.network.rmi",
  "com.adrenalinici.adrenaline.common.util",
  "com.adrenalinici.adrenaline.common.view"
)

writeUML("deliveries/final/uml/adrenaline-client.puml",
  "com.adrenalinici.adrenaline.client",
  "com.adrenalinici.adrenaline.client.rmi",
  "com.adrenalinici.adrenaline.client.socket"
)

writeUML("deliveries/final/uml/adrenaline-cli.puml",
  "com.adrenalinici.adrenaline.cli"
)

writeUML("deliveries/final/uml/adrenaline-gui.puml",
  "com.adrenalinici.adrenaline.gui",
  "com.adrenalinici.adrenaline.gui.controller"
)

writeUML("deliveries/final/uml/adrenaline-server.puml",
  "com.adrenalinici.adrenaline.server",
  "com.adrenalinici.adrenaline.server.controller",
  "com.adrenalinici.adrenaline.server.controller.guns",
  "com.adrenalinici.adrenaline.server.controller.nodes",
  "com.adrenalinici.adrenaline.server.controller.nodes.guns",
  "com.adrenalinici.adrenaline.server.flow",
  "com.adrenalinici.adrenaline.server.flow.impl",
  "com.adrenalinici.adrenaline.server.model",
  "com.adrenalinici.adrenaline.server.network",
  "com.adrenalinici.adrenaline.server.network.handlers",
  "com.adrenalinici.adrenaline.server.network.rmi",
  "com.adrenalinici.adrenaline.server.network.socket"
)

writeUML("deliveries/final/uml/adrenaline-complete.puml",
  "com.adrenalinici.adrenaline.common.model.event",
  "com.adrenalinici.adrenaline.common.model.light",
  "com.adrenalinici.adrenaline.common.model",
  "com.adrenalinici.adrenaline.common.network.inbox",
  "com.adrenalinici.adrenaline.common.network.outbox",
  "com.adrenalinici.adrenaline.common.network.rmi",
  "com.adrenalinici.adrenaline.common.util",
  "com.adrenalinici.adrenaline.common.view",
  "com.adrenalinici.adrenaline.client",
  "com.adrenalinici.adrenaline.client.rmi",
  "com.adrenalinici.adrenaline.client.socket",
  "com.adrenalinici.adrenaline.cli",
  "com.adrenalinici.adrenaline.gui",
  "com.adrenalinici.adrenaline.gui.controller",
  "com.adrenalinici.adrenaline.server",
  "com.adrenalinici.adrenaline.server.controller",
  "com.adrenalinici.adrenaline.server.controller.guns",
  "com.adrenalinici.adrenaline.server.controller.nodes",
  "com.adrenalinici.adrenaline.server.controller.nodes.guns",
  "com.adrenalinici.adrenaline.server.flow",
  "com.adrenalinici.adrenaline.server.flow.impl",
  "com.adrenalinici.adrenaline.server.model",
  "com.adrenalinici.adrenaline.server.network",
  "com.adrenalinici.adrenaline.server.network.handlers",
  "com.adrenalinici.adrenaline.server.network.rmi",
  "com.adrenalinici.adrenaline.server.network.socket"
)

renderImage("deliveries/final/uml/adrenaline-common.puml")
renderImage("deliveries/final/uml/adrenaline-client.puml")
renderImage("deliveries/final/uml/adrenaline-cli.puml")
renderImage("deliveries/final/uml/adrenaline-gui.puml")
renderImage("deliveries/final/uml/adrenaline-server.puml")
renderImage("deliveries/final/uml/adrenaline-complete.puml")
