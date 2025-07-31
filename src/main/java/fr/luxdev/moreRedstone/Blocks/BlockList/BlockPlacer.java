package fr.luxdev.moreRedstone.Blocks.BlockList;

import fr.luxdev.moreRedstone.Blocks.BlockManager;
import fr.luxdev.moreRedstone.MoreRedstone;
import fr.luxdev.moreRedstone.Utils.TagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

public class BlockPlacer {

    private final MoreRedstone plugin;
    private final TagManager tagManager;
    private final Material material = Material.DISPENSER;

    public BlockPlacer(MoreRedstone plugin) {
        this.plugin = plugin;
        tagManager = plugin.getTagManager();
    }

    private boolean isBlockValid(Block block) {
        return BlockManager.isBlockValid(block, material);
    }

    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        if (!isBlockValid(block)) return;
        tagManager.setBlock(block, "customBlockType", PersistentDataType.STRING, "blockPlacer");
    }

    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!isBlockValid(block)) return;
        if (!(block.getState() instanceof Container container)) return;
        for (ItemStack itemStack : container.getInventory()) {
            BlockManager.dropItemOnBreakEvent(event, itemStack);
        }
        BlockManager.dropItemOnBreakEvent(event, item(tagManager));
        tagManager.removeBlock(block, "customBlockType");

    }

    public void onPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.DISPENSER) return;
        if (!block.isBlockIndirectlyPowered()) return;
        Block blockToPlace = BlockManager.getBlockLookingAt(block);
        if (blockToPlace==null || blockToPlace.getType()!=Material.AIR) return;
        if (!(block.getState() instanceof Container container)) return;
        Material material = null;
        for (ItemStack itemStack : container.getInventory()) {
            if (itemStack != null && itemStack.getType().isBlock()) {
                material = itemStack.getType();
                itemStack.setAmount(itemStack.getAmount() - 1);
                break;
            }
        }
        if (material!=null) {
            Material finalMaterial = material;
            new BukkitRunnable() {
                @Override
                public void run() {
                    blockToPlace.setType(finalMaterial, true);
                }
            }.runTaskLater(plugin, 1);
        }
    }

    public void onCreativePick(InventoryCreativeEvent event) {
        event.getWhoClicked().getInventory().setItem(event.getSlot(), item(tagManager));
    }

    public void onCreativePick(InventoryClickEvent event) {
        event.getWhoClicked().getInventory().setItem(event.getSlot(), item(tagManager));
    }

    public static ItemStack item(TagManager tagManager) {
        ItemStack dropItemStack = new ItemStack(Material.DISPENSER);
        ItemMeta dropItemStackMeta = dropItemStack.getItemMeta();

        dropItemStackMeta.itemName(Component.text("Block Placer", TextColor.color(185, 159, 148)));
        dropItemStackMeta.lore(List.of(
                Component.text("When powered by redstone :", TextColor.color(155, 150, 143)).decoration(TextDecoration.ITALIC, false),
                Component.text("  Place blocks it has from his inventory", TextColor.color(155, 150, 143)).decoration(TextDecoration.ITALIC, false)
        ));

        dropItemStack.setItemMeta(dropItemStackMeta);
        tagManager.setItem(dropItemStack, "itemCustomBlockType", PersistentDataType.STRING, "blockPlacerItem");
        return dropItemStack;
    }

}