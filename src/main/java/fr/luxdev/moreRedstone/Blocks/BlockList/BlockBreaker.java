package fr.luxdev.moreRedstone.Blocks.BlockList;

import fr.luxdev.moreRedstone.Blocks.BlockManager;
import fr.luxdev.moreRedstone.MoreRedstone;
import fr.luxdev.moreRedstone.Utils.TagManager;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BlockBreaker {

    private final MoreRedstone plugin;
    private final TagManager tagManager;
    private final Material material = Material.DISPENSER;

    public BlockBreaker(MoreRedstone plugin) {
        this.plugin = plugin;
        tagManager = plugin.getTagManager();
    }

    private boolean isBlockValid(Block block) {
        return BlockManager.isBlockValid(block, material);
    }

    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        if (!isBlockValid(block)) return;
        tagManager.setBlock(block, "customBlockType", PersistentDataType.STRING, "blockBreaker");
    }

    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!isBlockValid(block)) return;
        BlockManager.dropItemOnBreakEvent(event, item(tagManager));
        tagManager.removeBlock(block, "customBlockType");
    }

    public void onPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.DISPENSER) return;
        if (!block.isBlockIndirectlyPowered()) return;

        Block blockToBreak = BlockManager.getBlockLookingAt(block);
        if (blockToBreak==null) return;
        if (BlockManager.isBlockUnbreakable(blockToBreak)) return;
        if (tagManager.block(blockToBreak, "customBlockType", PersistentDataType.STRING)!=null) return;
        blockToBreak.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE), true, true);
    }

    public void onInteract(PlayerInteractEvent event) {
        BlockManager.cancelInventoryOpenableBlockInteraction(event);
    }

    public void onCreativePick(PlayerPickItemEvent event) {
        event.getPlayer().getInventory().setItem(event.getTargetSlot(), item(tagManager));
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
