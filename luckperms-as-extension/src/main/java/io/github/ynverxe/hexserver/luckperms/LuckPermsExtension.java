package io.github.ynverxe.hexserver.luckperms;

import io.github.ynverxe.hexserver.HexServer;
import io.github.ynverxe.hexserver.extension.HexExtension;
import io.github.ynverxe.hexserver.luckperms.config.ConfigurateConfigAdapter;
import me.lucko.luckperms.minestom.CommandRegistry;
import me.lucko.luckperms.minestom.LuckPermsMinestom;
import net.luckperms.api.LuckPerms;
import net.minestom.server.ServerProcess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class LuckPermsExtension extends HexExtension {

  private @Nullable LuckPerms luckPerms;

  public LuckPermsExtension(@NotNull Object context) {
    super(context);
  }

  @Override
  protected void enable() {
    try {
      computeResource("luckperms.conf", "luckperms.conf");

      ServerProcess process = HexServer.instance().process();

      this.luckPerms = LuckPermsMinestom.builder(directory())
          .commandRegistry(CommandRegistry.of(process.command()::register, process.command()::unregister))
          .configurationAdapter(lpMinestomPlugin -> new ConfigurateConfigAdapter(lpMinestomPlugin, this))
          .dependencyManager(true)
          .logger(logger())
          .enable();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void disable() {
    LuckPermsMinestom.disable();
  }

  public @Nullable LuckPerms luckPerms() {
    return luckPerms;
  }
}