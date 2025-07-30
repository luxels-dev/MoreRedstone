package fr.luxdev.moreRedstone.Blocks.BlockList;

import fr.luxdev.moreRedstone.MoreRedstone;
import fr.luxdev.moreRedstone.Utils.TagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BlockBreaker {

    private final MoreRedstone plugin;

    public BlockBreaker(MoreRedstone plugin) {
        this.plugin = plugin;
    }

    public void onPlace(BlockPlaceEvent event) {

        if (event.getBlockPlaced().getType() != Material.DISPENSER) return;

        Block block = event.getBlockPlaced();
        TagManager tagManager = plugin.getTagManager();

        tagManager.setBlock(block, "customBlockType", "blockBreaker");

    }

    public void onBreak(BlockBreakEvent event) {

        if (event.getBlock().getType() != Material.DISPENSER) return;

        event.setDropItems(false);
        Block block = event.getBlock();
        Location dropSpawnLocation = block.getLocation().toCenterLocation();

        Entity entity = dropSpawnLocation.getWorld().spawnEntity(dropSpawnLocation, EntityType.ITEM, false);
        if (!(entity instanceof Item drop)) return;

        ItemStack dropItemStack = item();

        drop.setItemStack(dropItemStack);

    }

    public static ItemStack item(TagManager tagManager) {

        ItemStack dropItemStack = new ItemStack(Material.DISPENSER);
        ItemMeta dropItemStackMeta = dropItemStack.getItemMeta();

        dropItemStackMeta.itemName(Component.text("Block Breaker", TextColor.color(185, 159, 148)));
        dropItemStackMeta.lore(List.of(
                Component.text("When powered by redstone :", TextColor.color(155, 150, 143)),
                Component.text("  Breaks the block it is looking at", TextColor.color(155, 150, 143))
        ));

        dropItemStack.setItemMeta(dropItemStackMeta);

        tagManager.setItem(dropItemStack, "itemCustomBlockType", PersistentDataType.STRING, "blockBreakerItem");

        return dropItemStack;

    }

}
