package fr.luxdev.moreRedstone.Commands;

import fr.luxdev.moreRedstone.Blocks.BlockList.BlockBreaker;
import fr.luxdev.moreRedstone.Blocks.BlockList.BlockPlacer;
import fr.luxdev.moreRedstone.MoreRedstone;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiveItemCommand implements TabExecutor {

    private final MoreRedstone plugin;

    public GiveItemCommand(MoreRedstone plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("redstone")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("redstone")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("You need to be a player to execute this command");
            return true;
        }

        ArrayList<ItemStack> listOfItems = new ArrayList<>();
        ArrayList<String> listOfItemNames = new ArrayList<>();

        if (strings.length==0) {
            player.sendMessage("§cPlease specify item names");
            return true;
        }

        for (String string : strings) {
            switch (string.toLowerCase()) {
                case "block_breaker":
                    listOfItems.add(BlockBreaker.item(plugin.getTagManager()));
                    listOfItemNames.add(string.toLowerCase());
                    break;
                case "block_placer":
                    listOfItems.add(BlockPlacer.item(plugin.getTagManager()));
                    listOfItemNames.add(string.toLowerCase());
                    break;
                default:
                    player.sendMessage("§c\""+string.toLowerCase()+"\" does not exist");
                    break;
            }
        }

        for (ItemStack listOfItem : listOfItems) {
            player.getInventory().addItem(listOfItem);
        }

        StringBuilder givingMessage = new StringBuilder("§a");

        if (listOfItemNames.isEmpty()) givingMessage.append("0 items given");
        else {
            for (String itemName : listOfItemNames) {
                givingMessage.append(itemName).append(", ");
            }
            String verb = listOfItemNames.size()>1 ? "were" : "was";
            givingMessage.append(verb).append(" given");
        }

        player.sendMessage(givingMessage.toString());

        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of("block_breaker", "block_placer");
    }

}
