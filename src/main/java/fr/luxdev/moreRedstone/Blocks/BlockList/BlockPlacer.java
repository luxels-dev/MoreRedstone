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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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
        Iterator<ItemStack> itemStackIterator = container.getInventory().iterator();
        while (itemStackIterator.hasNext()) {
            ItemStack itemStack = itemStackIterator.next();
            if (itemStack!=null && itemStack.getType().isBlock()) {
                material = itemStack.getType();
                if (itemStack.getAmount()==1) itemStackIterator.remove();
                else itemStack.setAmount(itemStack.getAmount()-1);
                break;
            }
        }
        if (material!=null) blockToPlace.setType(material, true);
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