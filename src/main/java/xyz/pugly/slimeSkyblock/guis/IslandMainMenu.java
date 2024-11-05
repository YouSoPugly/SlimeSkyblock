package xyz.pugly.slimeSkyblock.guis;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.configurable.ConfigurableGui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class IslandMainMenu extends ConfigurableGui {
    public IslandMainMenu(@NotNull Player player) {
        super(player, "island-main-menu");
    }

    // TODO: Implement the GUI
    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
