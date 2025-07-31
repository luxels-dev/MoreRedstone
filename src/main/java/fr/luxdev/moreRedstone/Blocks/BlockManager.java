package fr.luxdev.moreRedstone.Blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BlockManager {

    public static boolean isBlockValid(Block block, Material material) {
        return block.getType() == material;
    }

    public static void dropItemOnBreakEvent(BlockBreakEvent event, ItemStack itemStack) {
        event.setDropItems(false);
        Location dropSpawnLocation = event.getBlock().getLocation().toCenterLocation();

        Entity entity = dropSpawnLocation.getWorld().spawnEntity(dropSpawnLocation, EntityType.ITEM, true);
        if (!(entity instanceof Item drop)) return;

        drop.setItemStack(itemStack);
    }
    
    public static boolean isBlockUnbreakable(Block block) {
        return block.getType()==Material.BEDROCK ||
                block.getType()==Material.BARRIER ||
                block.getType()==Material.STRUCTURE_BLOCK ||
                block.getType()==Material.STRUCTURE_VOID ||
                block.getType()==Material.END_GATEWAY ||
                block.getType()==Material.END_PORTAL ||
                block.getType()==Material.END_PORTAL_FRAME ||
                block.getType()==Material.COMMAND_BLOCK ||
                block.getType()==Material.CHAIN_COMMAND_BLOCK ||
                block.getType()==Material.REPEATING_COMMAND_BLOCK;
    }

    public static @Nullable Block getBlockLookingAt(Block block) {
        BlockData blockData = block.getBlockData();
        if (!(blockData instanceof Directional directional)) return null;
        BlockFace blockFace = directional.getFacing();
        return block.getRelative(blockFace);
    }

    public static void cancelInventoryOpenableBlockInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction()!= Action.RIGHT_CLICK_BLOCK) return;
        boolean isSneaking = player.isSneaking();
        boolean holdingBlockItem = event.getItem() != null && (event.getItem().getType().isBlock() || !event.getItem().getType().isSolid());
        if (!isSneaking || !holdingBlockItem) {
            event.setCancelled(true);
        }
    }

}
