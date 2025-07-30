package fr.luxdev.moreRedstone;

import fr.luxdev.moreRedstone.Blocks.Listeners.BlockListeners;
import fr.luxdev.moreRedstone.Commands.GiveItemCommand;
import fr.luxdev.moreRedstone.Utils.TagManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoreRedstone extends JavaPlugin {

    private TagManager tagManager;
    private GiveItemCommand giveItemCommand;
    private BlockListeners blockListeners;

    @Override
    public void onEnable() {

        tagManager = new TagManager(this);
        giveItemCommand = new GiveItemCommand(this);
        blockListeners = new BlockListeners(this);

    }

    @Override
    public void onDisable() {

    }

    public TagManager getTagManager() {
        return tagManager;
    }

}
