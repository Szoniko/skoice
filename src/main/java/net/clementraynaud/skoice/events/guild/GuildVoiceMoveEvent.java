/*
 * Copyright 2020, 2021, 2022 Clément "carlodrift" Raynaud, Lucas "Lucas_Cdry" Cadiry and contributors
 * Copyright 2016, 2017, 2018, 2019, 2020, 2021 Austin "Scarsz" Shapiro
 *
 * This file is part of Skoice.
 *
 * Skoice is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Skoice is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Skoice.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.clementraynaud.skoice.events.guild;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

import static net.clementraynaud.skoice.config.Config.getCategory;
import static net.clementraynaud.skoice.config.Config.getMinecraftID;
import static net.clementraynaud.skoice.networks.NetworkManager.networks;
import static net.clementraynaud.skoice.networks.NetworkManager.updateMutedUsers;

public class GuildVoiceMoveEvent extends ListenerAdapter {

    @Override
    public void onGuildVoiceMove(net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent event) {
        if (event.getChannelJoined().getParent() != null && !event.getChannelJoined().getParent().equals(getCategory()) &&
                event.getChannelLeft().getParent() != null && event.getChannelLeft().getParent().equals(getCategory())) {
            UUID minecraftID = getMinecraftID(event.getMember());
            if (minecraftID == null) return;
            OfflinePlayer player = Bukkit.getOfflinePlayer(minecraftID);
            if (player.isOnline()) {
                networks.stream()
                        .filter(network -> network.contains(player.getPlayer().getUniqueId()))
                        .forEach(network -> network.remove(player.getPlayer()));
            }
        }
        updateMutedUsers(event.getChannelJoined(), event.getMember());
    }
}
