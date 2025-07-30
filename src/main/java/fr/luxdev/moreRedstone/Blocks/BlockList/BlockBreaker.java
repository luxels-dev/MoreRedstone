package fr.luxdev.moreRedstone.Blocks.BlockList;

import fr.luxdev.moreRedstone.MoreRedstone;
import fr.luxdev.moreRedstone.Utils.TagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
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

        tagManager.setBlock(block, "customBlockType", PersistentDataType.STRING, "blockBreaker");

    }

    public void onBreak(BlockBreakEvent event) {

        TagManager tagManager = plugin.getTagManager();

        if (event.getBlock().getType() != Material.DISPENSER) return;

        event.setDropItems(false);
        Block block = event.getBlock();
        Location dropSpawnLocation = block.getLocation().toCenterLocation();

        Entity entity = dropSpawnLocation.getWorld().spawnEntity(dropSpawnLocation, EntityType.ITEM, true);
        if (!(entity instanceof Item drop)) return;

        ItemStack dropItemStack = item(tagManager);

        drop.setItemStack(dropItemStack);

        tagManager.removeBlock(block, "customBlockType");

    }

    public void onPhysics(BlockPhysicsEvent event) {

        Block block = event.getBlock();

        if (block.getType() != Material.DISPENSER) return;

        if (!block.isBlockIndirectlyPowered()) return;

        BlockFace blockFace = block.getFace(block);
        if (blockFace==null) return;
        Block blockToBreak = block.getRelative(blockFace);

        if (blockToBreak.getType()==Material.BEDROCK ||
                blockToBreak.getType()==Material.BARRIER ||
                blockToBreak.getType()==Material.STRUCTURE_BLOCK ||
                blockToBreak.getType()==Material.STRUCTURE_VOID ||
                blockToBreak.getType()==Material.END_GATEWAY ||
                blockToBreak.getType()==Material.END_PORTAL ||
                blockToBreak.getType()==Material.END_PORTAL_FRAME
        ) return;

        blockToBreak.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE), true, true);

    }

    public static ItemStack item(TagManager tagManager) {

        ItemStack dropItemStack = new ItemStack(Material.DISPENSER);
        ItemMeta dropItemStackMeta = dropItemStack.getItemMeta();

        dropItemStackMeta.itemName(Component.text("Block Breaker", TextColor.color(185, 159, 148)));
        dropItemStackMeta.lore(List.of(
                Component.text("When powered by redstone :", TextColor.color(155, 150, 143)).decoration(TextDecoration.ITALIC, false),
                Component.text("  Breaks the block it is looking at", TextColor.color(155, 150, 143)).decoration(TextDecoration.ITALIC, false)
        ));

        dropItemStack.setItemMeta(dropItemStackMeta);

        tagManager.setItem(dropItemStack, "itemCustomBlockType", PersistentDataType.STRING, "blockBreakerItem");

        return dropItemStack;

    }

}
