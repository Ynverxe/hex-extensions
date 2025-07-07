package io.github.ynverxe.hextester;

import com.github.ynverxe.hexserver.HexServer;
import com.github.ynverxe.hexserver.extension.HexExtension;
import com.github.ynverxe.hexserver.world.HexWorld;
import java.util.UUID;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;

public class HexTesterExtension extends HexExtension {

  public HexTesterExtension(@NotNull Object context) {
    super(context);
  }

  @Override
  protected void enable() throws Exception {
    initSpawningInstance();
  }

  private void initSpawningInstance() {
    HexWorld world = HexServer.instance().extensionWorldLookup().byIndex(0)
        .orElseGet(() -> {
          HexWorld def = new HexWorld(UUID.randomUUID(), DimensionType.OVERWORLD, "default");

          def.setChunkSupplier(LightingChunk::new);

          def.setGenerator(unit -> {
            unit.modifier().fillHeight(-1, 0, Block.GRASS_BLOCK);
            unit.modifier().fillHeight(-2, -1, Block.DIRT);
            unit.modifier().fillHeight(-3, -2, Block.BEDROCK);
          });

          def.eventNode().addListener(PlayerSpawnEvent.class, event -> {
            event.getPlayer().teleport(new Pos(0, 1, 0));
          });

          worldManager().register(def);
          return def;
        });

    eventNode().addListener(AsyncPlayerConfigurationEvent.class, event -> {
      if (event.getSpawningInstance() == null) {
        event.setSpawningInstance(world);
      }
    });
  }
}