package fr.luxdev.moreRedstone.Blocks.Listeners;

import fr.luxdev.moreRedstone.Blocks.BlockList.BlockBreaker;
import fr.luxdev.moreRedstone.Blocks.BlockList.BlockPlacer;
import fr.luxdev.moreRedstone.MoreRedstone;
import fr.luxdev.moreRedstone.Utils.TagManager;
import io.papermc.paper.event.block.BlockFailedDispenseEvent;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BlockListeners implements Listener {

    private final MoreRedstone plugin;

    private final BlockBreaker blockBreaker;
    private final BlockPlacer blockPlacer;

    public BlockListeners(MoreRedstone plugin) {

        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        blockBreaker = new BlockBreaker(plugin);
        blockPlacer = new BlockPlacer(plugin);

    }

    @EventHandler
    public void placeBlockEvent(BlockPlaceEvent event) {

        switch (getitemType(event.getItemInHand())) {
            case BLOCK_BREAKER_ITEM:
                blockBreaker.onPlace(event);
                break;
            case BLOCK_PLACER_ITEM:
                blockPlacer.onPlace(event);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void breakBlockEvent(BlockBreakEvent event) {

        if (event.getPlayer().getGameMode()== GameMode.CREATIVE) return;
        Block block = event.getBlock();
        TagManager tagManager = plugin.getTagManager();
        String customBlockType = tagManager.block(block, "customBlockType", PersistentDataType.STRING);
        if (customBlockType==null) return;

        switch (getBlockType(event.getBlock())) {
            case BLOCK_BREAKER:
                blockBreaker.onBreak(event);
                break;
            case BLOCK_PLACER:
                blockPlacer.onBreak(event);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void blockPhysicsEvent(BlockPhysicsEvent event) {

        switch (getBlockType(event.getBlock())) {
            case BLOCK_BREAKER:
                blockBreaker.onPhysics(event);
                break;
            case BLOCK_PLACER:
                blockPlacer.onPhysics(event);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void blockInteractEvent(PlayerInteractEvent event) {

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock==null) return;

        switch (getBlockType(clickedBlock)) {
            case BLOCK_BREAKER:
                blockBreaker.onInteract(event);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void blockDispenseEvent(BlockFailedDispenseEvent event) {

        Block clickedBlock = event.getBlock();

        switch (getBlockType(clickedBlock)) {
            case BLOCK_BREAKER, BLOCK_PLACER:
                event.shouldPlayEffect(false);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void blockReallyDispenseEvent(BlockDispenseEvent event) {

        Block clickedBlock = event.getBlock();

        switch (getBlockType(clickedBlock)) {
            case BLOCK_BREAKER, BLOCK_PLACER:
                event.setCancelled(true);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void blockDropEvent(BlockDropItemEvent event) {

        Block clickedBlock = event.getBlock();

        switch (getBlockType(clickedBlock)) {
            case BLOCK_BREAKER, BLOCK_PLACER:
                event.setCancelled(true);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void blockPreDispenseEvent(BlockPreDispenseEvent event) {

        Block clickedBlock = event.getBlock();

        switch (getBlockType(clickedBlock)) {
            case BLOCK_BREAKER, BLOCK_PLACER:
                event.setCancelled(true);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void blockInventoryMoveEvent(InventoryMoveItemEvent event) {

        if (!(event.getDestination().getHolder() instanceof Dispenser dispenser)) return;
        Block clickedBlock = dispenser.getBlock();

        switch (getBlockType(clickedBlock)) {
            case BLOCK_BREAKER:
                event.setCancelled(true);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void blockCreativePickEvent(InventoryCreativeEvent event) {

        if (event.getClick()!= ClickType.MIDDLE) return;
        Block clickedBlock = event.getWhoClicked().getTargetBlockExact(10);
        if (clickedBlock==null) return;

        switch (getBlockType(clickedBlock)) {
            case BLOCK_BREAKER:
                blockBreaker.onCreativePick(event);
                break;
            case BLOCK_PLACER:
                blockPlacer.onCreativePick(event);
                break;
            case null, default:
                break;
        }

    }

    @EventHandler
    public void blockInventoryPickEvent(InventoryClickEvent event) {

        if (event.getClick()!= ClickType.CREATIVE) return;
        Block clickedBlock = event.getWhoClicked().getTargetBlockExact(10);
        if (clickedBlock==null) return;

        switch (getBlockType(clickedBlock)) {
            case BLOCK_BREAKER:
                blockBreaker.onCreativePick(event);
                break;
            case BLOCK_PLACER:
                blockPlacer.onCreativePick(event);
                break;
            case null, default:
                break;
        }

    }

    private BlockType getBlockType(Block block) {

        TagManager tagManager = plugin.getTagManager();
        String customBlockType = tagManager.block(block, "customBlockType", PersistentDataType.STRING);
        return switch (customBlockType) {
            case "blockBreaker" -> BlockType.BLOCK_BREAKER;
            case "blockPlacer" -> BlockType.BLOCK_PLACER;
            case null, default -> null;
        };

    }

    private ItemType getitemType(ItemStack itemStack) {

        TagManager tagManager = plugin.getTagManager();
        String itemCustomBlockType = tagManager.item(itemStack, "itemCustomBlockType", PersistentDataType.STRING);
        return switch (itemCustomBlockType) {
            case "blockBreakerItem" -> ItemType.BLOCK_BREAKER_ITEM;
            case "blockPlacerItem" -> ItemType.BLOCK_PLACER_ITEM;
            case null, default -> null;
        };

    }

    public enum BlockType {

        BLOCK_BREAKER,
        BLOCK_PLACER

    }

    public enum ItemType {

        BLOCK_BREAKER_ITEM,
        BLOCK_PLACER_ITEM

    }

}
