#!/usr/bin/env kscript

@file:DependsOn("ch.ifocusit:plantuml-builder:1.4")
@file:DependsOn("org.reflections:reflections:0.9.11")
@file:DependsOn("com.adrenalinici:adrenalina-common:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-client:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-gui:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-cli:1.0-SNAPSHOT")
@file:DependsOn("com.adrenalinici:adrenalina-server:1.0-SNAPSHOT")

import java.io.File


fun renderUML(vararg packages: String): String {
  val builder = ClassDiagramBuilder()

  packages.forEach {pkgName ->
    val reflections = Reflections(
      ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage(pkgName))
        .setScanners(SubTypesScanner(false))
        .filterInputsBy(FilterBuilder().includePackage(pkgName))
    );

    val classes = reflections.getSubTypesOf(Object::class.java);

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
