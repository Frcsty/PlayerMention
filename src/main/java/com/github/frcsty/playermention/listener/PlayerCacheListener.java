package com.github.frcsty.playermention.listener;

import com.github.frcsty.playermention.MentionPlugin;
import com.github.frcsty.playermention.cache.ActiveUserCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Handles active user cache {@link }
 */
public final class PlayerCacheListener implements Listener {

    @NotNull private final ActiveUserCache activeUserCache;

    public PlayerCacheListener(@NotNull final MentionPlugin plugin) {
        this.activeUserCache = plugin.getActiveUserCache();
    }

    /**
     * Adds the joined user to our {@link ActiveUserCache}
     *
     * @param event PlayerJoinEvent
     */
    @EventHandler public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.activeUserCache.addToCache(player);
    }

    /**
     * Removes the user that quit from our {@link ActiveUserCache}
     *
     * @param event PlayerQuitEvent
     */
    @EventHandler public void onPlayerLeave(@NotNull final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.activeUserCache.removeFromCacheByName(player.getName());
    }

}
