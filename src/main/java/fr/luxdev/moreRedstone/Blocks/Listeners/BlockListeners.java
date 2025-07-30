package fr.luxdev.moreRedstone.Blocks.Listeners;

import fr.luxdev.moreRedstone.Blocks.BlockList.BlockBreaker;
import fr.luxdev.moreRedstone.MoreRedstone;
import fr.luxdev.moreRedstone.Utils.TagManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BlockListeners implements Listener {

    private final MoreRedstone plugin;

    private final BlockBreaker blockBreaker;

    public BlockListeners(MoreRedstone plugin) {

        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        blockBreaker = new BlockBreaker(plugin);

    }

    @EventHandler
    public void placeBlockEvent(BlockPlaceEvent event) {

        ItemStack item = event.getItemInHand();
        TagManager tagManager = plugin.getTagManager();
        String itemCustomBlockType = tagManager.item(item, "itemCustomBlockType", PersistentDataType.STRING);

        switch (itemCustomBlockType) {
            case "blockBreakerItem":
                blockBreaker.onPlace(event);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void breakBlockEvent(BlockBreakEvent event) {

        Block block = event.getBlock();
        TagManager tagManager = plugin.getTagManager();
        String customBlockType = tagManager.block(block, "customBlockType").getFirst().toString();

        switch (customBlockType) {
            case "blockBreaker":
                blockBreaker.onBreak(event);
                break;
            case null, default:
                break;
        }

    }

}
