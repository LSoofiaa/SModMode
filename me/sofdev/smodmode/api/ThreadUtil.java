package me.sofdev.smodmode.api;

import me.sofdev.smodmode.Main;

public class ThreadUtil {
    public static void runTask(boolean async, Main plugin, Runnable runnable) {
        if(async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }
}
