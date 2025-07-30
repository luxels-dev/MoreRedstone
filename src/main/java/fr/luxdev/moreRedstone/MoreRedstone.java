package fr.luxdev.moreRedstone;

import fr.luxdev.moreRedstone.Utils.TagManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoreRedstone extends JavaPlugin {

    private TagManager tagManager;

    @Override
    public void onEnable() {

        tagManager = new TagManager(this);

    }

    @Override
    public void onDisable() {

    }

    public TagManager getTagManager() {
        return tagManager;
    }

}
