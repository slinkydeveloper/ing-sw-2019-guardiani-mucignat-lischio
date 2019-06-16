#!/usr/bin/env kscript
//DEPS com.github.javaparser:javaparser-core:3.12.0

@file:MavenRepository("bintray", "https://dl.bintray.com/fleshgrinder/com.fleshgrinder.kotlin")

@file:DependsOn("com.github.javaparser:javaparser-core:3.12.0")
@file:DependsOn("com.fleshgrinder.kotlin:case-format:0.1.0")

import java.io.File

data class Command(val properties: Map<String, Type>)

val loadedFile = File("src/main/java/com/adrenalinici/adrenaline/view/GameView.java").useLines { it.toList() }.joinToString(separator = "\n")

val compilationUnit = JavaParser.parse(loadedFile)
val iGameView = compilationUnit.getInterfaceByName("GameView").get()

val showMethods = iGameView.methods.filter { it.nameAsString.startsWith("show") }.map { it.nameAsString.substring(4) to it }.toMap()
val commands = showMethods.mapValues { Command(it.value.parameters.toList().map { it.nameAsString to it.type }.toMap()) }

for ((name, command) in commands) {
  println("Discovered command $name")
  File("src/main/java/com/adrenalinici/adrenaline/network/outbox/${name}Message.java").printWriter().use { out ->
    out.println("""
      |package com.adrenalinici.adrenaline.network.outbox;
      |
      |import com.adrenalinici.adrenaline.model.common.*;
      |
      |import java.util.*;
      |
      |import java.util.function.Consumer;
      |
      |public class ${name}Message implements OutboxMessage {
      |
  """.trimMargin())
    command.properties.forEach{ (name, type) ->
      out.println("  private $type $name;")
    }
    out.println()
    out.println("  public ${name}Message(${command.properties.map { (name, type) -> "$type $name" }.joinToString(separator = ", ")}) {")
    command.properties.forEach{ (name, _) ->
      out.println("    this.$name = $name;")
    }
    out.println("  }")
    out.println()

    command.properties.forEach{ (name, type) ->
      out.println("  public $type get${name.capitalize()}() { return this.$name; }")
      out.println()
    }

    out.println("""
        |  @Override
        |  public void on${name}Message(Consumer<${name}Message> c) { c.accept(this); }
        |
    """.trimMargin())

    out.println("}")
  }
  println("Wrote src/main/java/com/adrenalinici/adrenaline/network/outbox/${name}Message.java")
  println("-----------")
}

File("src/main/java/com/adrenalinici/adrenaline/network/outbox/OutboxMessage.java").printWriter().use { out ->
  out.println("""
    |package com.adrenalinici.adrenaline.network.outbox;
    |
    |import java.io.Serializable;
    |
    |import java.util.function.Consumer;
    |
    |public interface OutboxMessage extends Serializable {
    |
""".trimMargin())

  for ((name, command) in commands) {
    out.println("  default void on${name.capitalize()}Message(Consumer<${name.capitalize()}Message> c) {}")
    out.println()
  }

  out.println("""
      |  default void onModelEventMessage(Consumer<ModelEventMessage> c) {}
      |
      |  default void onAvailableMatchesMessage(Consumer<AvailableMatchesMessage> c) {}
      |
      |}
  """.trimMargin())
}
//
//File("src/main/java/com/adrenalinici/adrenaline/network/server/GameViewServer.java").printWriter().use { out ->
//  out.println("""
//    |package com.adrenalinici.adrenaline.network.server;
//    |
//    |import com.adrenalinici.adrenaline.model.common.*;
//    |import com.adrenalinici.adrenaline.network.outbox.*;
//    |import InboxEntry;
//    |
//    |import java.util.*;
//    |import java.util.concurrent.BlockingQueue;
//    |
//    |public class GameViewServer extends BaseGameViewServer {
//    |
//    |  public GameViewServer(BlockingQueue<InboxEntry> inbox, BlockingQueue<OutboxMessage> outbox, Set<PlayerColor> availablePlayers) {
//    |    super(inbox, outbox, availablePlayers);
//    |  }
//""".trimMargin())
//
//  for ((name, command) in commands) {
//    out.println("  @Override")
//    out.println("  public void show$name(${command.properties.map { (name, type) -> "$type $name" }.joinToString(separator = ", ")}) {")
//    out.println("    broadcast(new ${name}Message(${command.properties.map { (name, type) -> "$name" }.joinToString(separator = ", ")}));")
//    out.println("  }")
//    out.println()
//  }
//
//  out.println("}")
//}
//
//println("Wrote src/main/java/com/adrenalinici/adrenaline/network/server/GameViewServer.java")
//println("-----------")
//
//File("src/test/java/com/adrenalinici/adrenaline/network/server/GameViewSocketOutboxMessageTest.java").printWriter().use { out ->
//  out.println("""
//    |package com.adrenalinici.adrenaline.network.server;
//    |
//    |import com.adrenalinici.adrenaline.model.*;
//    |import com.adrenalinici.adrenaline.model.common.*;
//    |import com.adrenalinici.adrenaline.network.outbox.*;
//    |import org.junit.Test;
//    |import org.mockito.ArgumentCaptor;
//    |
//    |import java.io.IOException;
//    |import java.util.*;
//    |
//    |import static com.adrenalinici.adrenaline.network.outbox.OutboxMockData.*;
//    |import static org.assertj.core.api.Assertions.assertThat;
//    |import static org.mockito.Mockito.times;
//    |import static org.mockito.Mockito.verify;
//    |
//    |@SuppressWarnings("unchecked")
//    |public class GameViewSocketOutboxMessageTest extends BaseGameViewSocketIntegrationTest {
//    |
//""".trimMargin())
//
//  for ((name, command) in commands) {
//    out.println("""
//        |  @Test
//        |  public void show${name}Test() throws IOException, InterruptedException {
//    """.trimMargin())
//    out.println("    serverGameView.show${name}(${command.properties.map { (name, type) -> name.toUpperSnakeCase() }.joinToString(separator = ", ")});")
//    out.println("""
//        |
//        |    Thread.sleep(100);
//        |
//    """.trimMargin())
//    for ((propName, propType) in command.properties) {
//      propType.ifClassOrInterfaceType {
//        out.println("    ArgumentCaptor<$it> ${propName}Captor = ArgumentCaptor.forClass(${it.nameAsString}.class);")
//      }
//    }
//    out.println("    verify(mockedClientView, times(1)).show${name}(${command.properties.map { (name, type) -> "${name}Captor.capture()" }.joinToString(separator = ", ")});")
//    out.println()
//    for ((propName, propType) in command.properties) {
//      out.println("    assertThat(${propName}Captor.getValue()).isEqualTo(${propName.toUpperSnakeCase()});")
//    }
//    out.println("""
//        |  }
//        |
//    """.trimMargin())
//  }
//
//  out.println("}")
//}
//
//println("Wrote src/test/java/com/adrenalinici/adrenaline/network/server/GameViewSocketOutboxMessageTest.java")
//println("-----------")
//
//File("src/test/java/com/adrenalinici/adrenaline/network/server/GameViewRmiOutboxMessageTest.java").printWriter().use { out ->
//  out.println("""
//    |package com.adrenalinici.adrenaline.network.server;
//    |
//    |import com.adrenalinici.adrenaline.model.*;
//    |import com.adrenalinici.adrenaline.model.common.*;
//    |import com.adrenalinici.adrenaline.network.outbox.*;
//    |import org.junit.Test;
//    |import org.mockito.ArgumentCaptor;
//    |
//    |import java.io.IOException;
//    |import java.util.*;
//    |
//    |import static com.adrenalinici.adrenaline.network.outbox.OutboxMockData.*;
//    |import static org.assertj.core.api.Assertions.assertThat;
//    |import static org.mockito.Mockito.times;
//    |import static org.mockito.Mockito.verify;
//    |
//    |@SuppressWarnings("unchecked")
//    |public class GameViewRmiOutboxMessageTest extends BaseGameViewRmiIntegrationTest {
//    |
//""".trimMargin())
//
//  for ((name, command) in commands) {
//    out.println("""
//        |  @Test
//        |  public void show${name}Test() throws IOException, InterruptedException {
//    """.trimMargin())
//    out.println("    serverGameView.show${name}(${command.properties.map { (name, type) -> name.toUpperSnakeCase() }.joinToString(separator = ", ")});")
//    out.println("""
//        |
//        |    Thread.sleep(500);
//        |
//    """.trimMargin())
//    for ((propName, propType) in command.properties) {
//      propType.ifClassOrInterfaceType {
//        out.println("    ArgumentCaptor<$it> ${propName}Captor = ArgumentCaptor.forClass(${it.nameAsString}.class);")
//      }
//    }
//    out.println("    verify(mockedClientView, times(1)).show${name}(${command.properties.map { (name, type) -> "${name}Captor.capture()" }.joinToString(separator = ", ")});")
//    out.println()
//    for ((propName, propType) in command.properties) {
//      out.println("    assertThat(${propName}Captor.getValue()).isEqualTo(${propName.toUpperSnakeCase()});")
//    }
//    out.println("""
//        |  }
//        |
//    """.trimMargin())
//  }
//
//  out.println("}")
//}
//
//println("Wrote src/test/java/com/adrenalinici/adrenaline/network/server/GameViewRmiOutboxMessageTest.java")
//println("-----------")
//
//File("src/main/java/com/adrenalinici/adrenaline/network/client/ClientViewProxy.java").printWriter().use { out ->
//  out.println("""
//    |package com.adrenalinici.adrenaline.client.client;
//    |
//    |import InboxMessage;
//    |import com.adrenalinici.adrenaline.network.outbox.*;
//    |import Observable;
//    |import com.adrenalinici.adrenaline.view.BaseClientGameView;
//    |
//    |public class ClientViewProxy extends Observable<InboxMessage> {
//    |
//    |  private BaseClientGameView view;
//    |
//    |  public ClientViewProxy(BaseClientGameView view) {
//    |    this.view = view;
//    |    this.view.registerObserver(this::notifyEvent);
//    |  }
//    |
//    |  public void handleNewServerMessage(OutboxMessage message) {
//""".trimMargin())
//
//  for ((name, command) in commands) {
//    out.println("    message.on${name}Message(e -> view.show${name}(${command.properties.map { (name, type) -> "e.get${name.capitalize()}()" }.joinToString(separator = ", ")}));")
//  }
//
//  out.println("""
//      |    message.onModelEventMessage(e -> view.onEvent(e.getModelEvent()));
//      |    message.onChooseMyPlayerMessage(e -> view.showChooseMyPlayer(e.getPlayerColors()));
//      |  }
//      |}
//  """.trimMargin())
//}
//
//println("src/main/java/com/adrenalinici/adrenaline/network/client/ClientViewProxy.java")
//println("-----------")

println("Wrote src/test/java/com/adrenalinici/adrenaline/network/server/GameViewSocketOutboxMessageTest.java")
println("-----------")

File("src/test/java/com/adrenalinici/adrenaline/network/server/GameViewRmiOutboxMessageTest.java").printWriter().use { out ->
  out.println("""
    |package com.adrenalinici.adrenaline.network.server;
    |
    |import com.adrenalinici.adrenaline.model.*;
    |import com.adrenalinici.adrenaline.model.common.*;
    |import com.adrenalinici.adrenaline.network.outbox.*;
    |import org.junit.Test;
    |import org.mockito.ArgumentCaptor;
    |
    |import java.io.IOException;
    |import java.util.*;
    |
    |import static com.adrenalinici.adrenaline.network.outbox.OutboxMockData.*;
    |import static org.assertj.core.api.Assertions.assertThat;
    |import static org.mockito.Mockito.times;
    |import static org.mockito.Mockito.verify;
    |
    |@SuppressWarnings("unchecked")
    |public class GameViewRmiOutboxMessageTest extends BaseGameViewRmiIntegrationTest {
    |
""".trimMargin())

  for ((name, command) in commands) {
    out.println("""
        |  @Test
        |  public void show${name}Test() throws IOException, InterruptedException {
    """.trimMargin())
    out.println("    serverGameView.show${name}(${command.properties.map { (name, type) -> name.toUpperSnakeCase() }.joinToString(separator = ", ")});")
    out.println("""
        |
        |    Thread.sleep(500);
        |
    """.trimMargin())
    for ((propName, propType) in command.properties) {
      propType.ifClassOrInterfaceType {
        out.println("    ArgumentCaptor<$it> ${propName}Captor = ArgumentCaptor.forClass(${it.nameAsString}.class);")
      }
    }
    out.println("    verify(mockedClientView, times(1)).show${name}(${command.properties.map { (name, type) -> "${name}Captor.capture()" }.joinToString(separator = ", ")});")
    out.println()
    for ((propName, propType) in command.properties) {
      out.println("    assertThat(${propName}Captor.getValue()).isEqualTo(${propName.toUpperSnakeCase()});")
    }
    out.println("""
        |  }
        |
    """.trimMargin())
  }

  out.println("}")
}

println("Wrote src/test/java/com/adrenalinici/adrenaline/network/server/GameViewRmiOutboxMessageTest.java")
println("-----------")

File("src/main/java/com/adrenalinici/adrenaline/network/client/ClientViewProxy.java").printWriter().use { out ->
  out.println("""
    |package com.adrenalinici.adrenaline.client.client;
    |
    |import InboxMessage;
    |import com.adrenalinici.adrenaline.network.outbox.*;
    |import Observable;
    |import com.adrenalinici.adrenaline.cli.BaseClientGameView;
    |
    |public class ClientViewProxy extends Observable<InboxMessage> {
    |
    |  private BaseClientGameView view;
    |
    |  public ClientViewProxy(BaseClientGameView view) {
    |    this.view = view;
    |    this.view.registerObserver(this::notifyEvent);
    |  }
    |
    |  public void handleNewServerMessage(OutboxMessage message) {
""".trimMargin())

  for ((name, command) in commands) {
    out.println("    message.on${name}Message(e -> view.show${name}(${command.properties.map { (name, type) -> "e.get${name.capitalize()}()" }.joinToString(separator = ", ")}));")
  }

  out.println("""
      |    message.onModelEventMessage(e -> view.onEvent(e.getModelEvent()));
      |    message.onChooseMyPlayerMessage(e -> view.showChooseMyPlayer(e.getPlayerColors()));
      |  }
      |}
  """.trimMargin())
}

println("src/main/java/com/adrenalinici/adrenaline/network/client/ClientViewProxy.java")
println("-----------")

println("Completed!")
