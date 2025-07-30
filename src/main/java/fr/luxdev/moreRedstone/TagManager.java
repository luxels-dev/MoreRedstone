package fr.luxdev.moreRedstone;

import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TagManager {

    MoreRedstone plugin;

    public TagManager(MoreRedstone plugin) {
        this.plugin = plugin;
    }

    public @NotNull List<MetadataValue> block(Block block, String key) {
        return block.getMetadata(key);
    }

    public void setBlock(Block block, String key, Object value) {

        if (value instanceof Boolean ||
                value instanceof Byte ||
                value instanceof Double ||
                value instanceof Float ||
                value instanceof Integer ||
                value instanceof Long ||
                value instanceof Short ||
                value instanceof String)
        {
            block.setMetadata(key, new FixedMetadataValue(plugin, value));
        }

        else {
            plugin.getLogger().warning(value.toString()+" was not saved, because it wasn't a valid value\nKey : "+key+"\nBlock : "+block.toString());
        }
    }

}
