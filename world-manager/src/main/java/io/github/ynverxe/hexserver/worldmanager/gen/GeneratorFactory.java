package io.github.ynverxe.hexserver.worldmanager.gen;

import io.github.ynverxe.hexserver.worldmanager.WorldConfigDefinition;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GeneratorFactory {

  @Nullable Generator create(@NotNull WorldConfigDefinition worldConfigDefinition);

}