package com.github.frcsty.playermention;

import com.earth2me.essentials.Essentials;
import com.github.frcsty.playermention.cache.ActiveUserCache;
import com.github.frcsty.playermention.config.ConfigStorage;
import com.github.frcsty.playermention.listener.AsyncPlayerChatListener;
import com.github.frcsty.playermention.listener.PlayerCacheListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.logging.Level;

public final class MentionPlugin extends JavaPlugin {

    @NotNull private final ActiveUserCache activeUserCache = new ActiveUserCache();
    @NotNull private final ConfigStorage configStorage = new ConfigStorage();
    private Essentials essentials;

    @Override public void onEnable() {
        saveDefaultConfig();

        this.essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        if (essentials == null) {
            getLogger().log(Level.WARNING, "Failed to find Essentials Provider. Disabling plugin!");
            getPluginLoader().disablePlugin(this);
            return;
        }

        this.configStorage.load(this);

        registerListeners(
                new PlayerCacheListener(this),
                new AsyncPlayerChatListener(this)
        );
    }

    @Override public void onDisable() {
        reloadConfig();
    }

    /**
     * Returns an instance of our cache containing active players
     *
     * @return  Returns our loaded {@link ActiveUserCache}
     */
    @NotNull public ActiveUserCache getActiveUserCache() {
        return this.activeUserCache;
    }

    /**
     * Returns an instance of our config storage
     *
     * @return  Returns our loaded {@link ConfigStorage}
     */
    @NotNull public ConfigStorage getConfigStorage() {
        return this.configStorage;
    }

    /**
     * Returns an instance of the loaded {@link Essentials} provider
     *
     * @return  Returns our {@link Essentials} provider
     */
    @NotNull public Essentials getEssentialsProvider() {
        return this.essentials;
    }

    /**
     * Registers all provided listeners
     *
     * @param listeners {@link Listener}'s to be registered
     */
    private void registerListeners(@NotNull final Listener... listeners) {
        final PluginManager manager = getServer().getPluginManager();

        Arrays.stream(listeners).forEach(listener -> manager.registerEvents(listener, this));
    }

}
