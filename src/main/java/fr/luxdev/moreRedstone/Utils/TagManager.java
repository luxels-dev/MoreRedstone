package fr.luxdev.moreRedstone.Utils;

import fr.luxdev.moreRedstone.MoreRedstone;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.type.NullType;
import java.util.List;

public class TagManager {

    private final MoreRedstone plugin;

    public TagManager(MoreRedstone plugin) {
        this.plugin = plugin;
    }

    public @NotNull List<MetadataValue> block(Block block, String key) {
        return block.getMetadata(key);
    }

    public void setBlock(Block block, String key, Object value) {

        if (isValid(value))
        {
            block.setMetadata(key, new FixedMetadataValue(plugin, value));
        }

        else {
            plugin.getLogger().warning(value.toString()+" was not saved, because it wasn't a valid value\nKey : "+key+"\nBlock : "+block.toString());
        }

    }

    public @Nullable <P, C> C item(ItemStack item, String key, PersistentDataType<P, C> type) {
        return item.getPersistentDataContainer().get(new NamespacedKey(plugin, key), type);
    }

    public <P, C> void setItem(ItemStack item, String key, PersistentDataType<P, C> type, C value) {
        item.editPersistentDataContainer(persistentDataContainer -> {
            persistentDataContainer.set(new NamespacedKey(plugin, key), type, value);
        });
    }

    private boolean isValid(Object object) {
        return object instanceof Boolean ||
                object instanceof Byte ||
                object instanceof Double ||
                object instanceof Float ||
                object instanceof Integer ||
                object instanceof Long ||
                object instanceof Short ||
                object instanceof String ||
                object instanceof NullType;
    }

}
