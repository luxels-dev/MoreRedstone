package fr.luxdev.moreRedstone;

import org.bukkit.plugin.java.JavaPlugin;

public final class MoreRedstone extends JavaPlugin {

    public TagManager tagManager;

    @Override
    public void onEnable() {

        tagManager = new TagManager(this);

    }

    @Override
    public void onDisable() {

    }
}
