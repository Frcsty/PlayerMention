package com.github.frcsty.playermention.listener;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.github.frcsty.playermention.MentionPlugin;
import com.github.frcsty.playermention.cache.ActiveUserCache;
import com.github.frcsty.playermention.config.ConfigStorage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class AsyncPlayerChatListener implements Listener {

    @NotNull private final ActiveUserCache activeUserCache;
    @NotNull private final ConfigStorage configStorage;
    @NotNull private final Essentials essentials;

    public AsyncPlayerChatListener(@NotNull final MentionPlugin plugin) {
        this.activeUserCache = plugin.getActiveUserCache();
        this.essentials = plugin.getEssentialsProvider();
        this.configStorage = plugin.getConfigStorage();
    }

    @EventHandler public void onAsyncPlayerChat(@NotNull final AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        CompletableFuture.supplyAsync(() -> {
            handleMentions(event.getPlayer(), event.getMessage());
            return null;
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    /**
     * Handles all user mentions from a given message {@link String}
     *
     * @param message Message sent by the {@link Player}
     */
    private void handleMentions(@NotNull final Player sender, @NotNull final String message) {
        final String[] contents = message.split(" ");

        for (final String content : contents) {
            final Player mentionedPlayer = this.activeUserCache.getPlayerByName(content);

            if (mentionedPlayer == null || isUserIgnoringUser(sender, mentionedPlayer)) continue;
            playSound(mentionedPlayer);
        }
    }

    /**
     * Checks if the mentioned user is ignoring the sender
     *
     * @param sender    {@link Player} who sent the message
     * @param mentioned {@link Player} who was mentioned in the message
     * @return Returns true if the @mentioned user is ignoring the sender
     */
    private boolean isUserIgnoringUser(final Player sender, final Player mentioned) {
        final User iSender = essentials.getUser(sender.getUniqueId());
        final User iReceiver = essentials.getUser(mentioned.getUniqueId());

        if (iSender == null || iReceiver == null) return true;
        return iReceiver.isIgnoredPlayer(iSender);
    }

    /**
     * Plays a {@link Sound} for the given player
     *
     * @param player Given {@link Player}
     */
    private void playSound(@NotNull final Player player) {
        player.playSound(
                player.getLocation(),
                Sound.valueOf(this.configStorage.getConfigString("sound.name").toUpperCase()),
                Float.valueOf(this.configStorage.getConfigString("sound.volume")),
                Float.valueOf(this.configStorage.getConfigString("sound.pitch"))
        );
    }

}
