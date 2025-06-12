package io.github.ynverxe.hexserver.worldmanager;

import com.github.ynverxe.hexserver.extension.HexExtension;
import io.github.ynverxe.configuratehelper.handler.FastConfiguration;
import io.github.ynverxe.configuratehelper.handler.factory.ConfigurationLoaderFactory;
import io.github.ynverxe.hexserver.util.MessageHandler;
import io.github.ynverxe.hexserver.worldmanager.command.GoToWorld;
import io.github.ynverxe.hexserver.worldmanager.command.ListWorldsCommand;
import io.github.ynverxe.hexserver.worldmanager.command.LoadWorldCommand;
import io.github.ynverxe.hexserver.worldmanager.command.WorldInfoCommand;
import io.leangen.geantyref.TypeToken;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.util.List;

public class WorldManagerExtension extends HexExtension {

  private WorldImporter importer;
  private MessageHandler messageHandler;

  public WorldManagerExtension(@NotNull Object context) {
    super(context);
  }

  @Override
  public void enable() {
    try {
      this.importer = new WorldImporter(worldManager());
      this.messageHandler = new MessageHandler(configurationFactory());

      MinecraftServer.getCommandManager().register(
          new ListWorldsCommand(this), new WorldInfoCommand(this), new LoadWorldCommand(this.importer), new GoToWorld());

      loadWorldsFromManifest();

      eventNode().addListener(AsyncPlayerConfigurationEvent.class, event -> {
        this.worldManager().internalView()
            .findFirst()
            .ifPresent(event::setSpawningInstance);
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void disable() {}

  private void loadWorldsFromManifest() throws IOException {
    FastConfiguration configuration = configurationFactory().create("worlds.yml", "worlds.yml");

    List<WorldConfigDefinition> worldDefinitions = configuration.node().get(new TypeToken<List<WorldConfigDefinition>>() {});

    for (WorldConfigDefinition worldDefinition : worldDefinitions) {
      try {
        importer.loadWorld(worldDefinition);
      } catch (Exception e) {
        logger().error("Cannot load world '{}'", worldDefinition.name, e);
      }
    }
  }

  @Override
  protected @NotNull ConfigurationLoaderFactory createConfigurationFactory() {
    return () -> YamlConfigurationLoader.builder()
        .nodeStyle(NodeStyle.BLOCK)
        .indent(2)
        .headerMode(HeaderMode.PRESERVE);
  }

  public WorldImporter importer() {
    return importer;
  }

  public MessageHandler messageHandler() {
    return messageHandler;
  }
}