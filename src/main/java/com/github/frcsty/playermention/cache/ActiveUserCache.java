package com.github.frcsty.playermention.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages Our active users, for parsing on user mention
 */
public final class ActiveUserCache {

    @NotNull private final Cache<String, Player> playerCache;

    /**
     * Builds our Cache with predefined parameters
     */
    public ActiveUserCache() {
        this.playerCache = CacheBuilder.newBuilder()
                .maximumSize(500)
                .build();
    }

    /**
     * Retrieves a {@link Player} from the given playerName input {@link String}
     * or null if not present
     *
     * @param playerName    Parsed player name estimation
     * @return  Linked {@link Player} instance or null
     */
    @Nullable public Player getPlayerByName(@NotNull final String playerName) {
        return this.playerCache.getIfPresent(playerName.toLowerCase());
    }

    /**
     * Adds specified {@link Player} to our activeUser Cache, and
     * lowercase's the key-name to maintain consistency
     *
     * @param player    Given {@link Player} to be cached
     */
    public void addToCache(@NotNull final Player player) {
        this.playerCache.put(player.getName().toLowerCase(), player);
    }

    /**
     * Removes a {@link Player} from our Cache by a given {@link String} name
     * which is lowercase'd
     *
     * @param playerName    Given player name to be removed from our active user cache
     */
    public void removeFromCacheByName(@NotNull final String playerName) {
        this.playerCache.invalidate(playerName.toLowerCase());
    }
}
