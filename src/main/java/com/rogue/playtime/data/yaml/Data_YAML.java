/*
 * Copyright (C) 2013 Spencer Alderman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.rogue.playtime.data.yaml;

import com.rogue.playtime.Playtime;
import com.rogue.playtime.data.DataHandler;
import com.rogue.playtime.runnable.yaml.YAMLAddRunnable;
import com.rogue.playtime.runnable.yaml.YAMLResetRunnable;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @since 1.3.0
 * @author 1Rogue
 * @version 1.3.0
 */
public class Data_YAML implements DataHandler {
    
    YAML yaml;
    BukkitTask updater;
    Playtime plugin;

    public int getValue(String data, String username) {
        if (data.equals("onlinetime") && !Bukkit.getPlayer(username).isOnline()) {
            return -1;
        }
        return yaml.getFile().getInt("users." + plugin.getBestPlayer(username) + "." + data);
    }
    
    public void onDeath(String username) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new YAMLResetRunnable(username, "deathtime", yaml));
    }
    
    public void onLogout(String username) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new YAMLResetRunnable(username, "onlinetime", yaml));
    }

    public void verifyFormat() {
    }

    public void setup() {
        yaml = new YAML();
        plugin = Playtime.getPlugin();
    }

    public void initiateRunnable() {
        updater = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new YAMLAddRunnable(plugin, yaml), 1200L, 1200L);
    }

    public void cleanup() {
        updater.cancel();
        yaml.forceSave();
        yaml = null;
    }
}
