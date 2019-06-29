#!/usr/bin/env kscript

@file:DependsOn("ch.ifocusit:plantuml-builder:1.4")
@file:DependsOn("com.adrenalinici:adrenalina-common:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-client:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-gui:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-cli:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-server:1.0-SNAPSHOT")

import java.io.File

fun renderUML(vararg packages: String): String {
  val builder = ClassDiagramBuilder()

  packages.forEach {pkgName ->
    val classPath = ClassPath.from(ClassLoader.getSystemClassLoader())

    com.adrenalinici.adrenaline.common.model.Action.SHOOT

    classPath.getTopLevelClasses(pkgName).forEach {
      println("Discovered ${it.load()}")
      builder.addClasse(it.load())
    }
    Package.getPackages().forEach { println(it.name) }
    val pkg = Package.getPackage(pkgName)
    println("Adding package ${pkg.name}")
    builder
      .addPackage(pkg)
  }

  return builder.build()
}

File("stuff.puml").printWriter().use { out -> out.print(renderUML(
  "com.adrenalinici.adrenaline.common.model"
)) }
