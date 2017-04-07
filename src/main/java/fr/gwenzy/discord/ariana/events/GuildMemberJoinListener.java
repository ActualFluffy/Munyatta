/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gwenzy.discord.ariana.events;

import fr.gwenzy.discord.ariana.Main;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 *
 * @author gwend
 */
public class GuildMemberJoinListener implements EventListener{
    
    @Override
    public void onEvent(Event event) {
        if(event instanceof GuildMemberJoinEvent){
            GuildMemberJoinEvent gmje = (GuildMemberJoinEvent) event;
            
            Main.jda.getGuildById("272754621200596992").getTextChannelById("273586021260722177").sendMessage("Hello "+gmje.getMember().getAsMention()+", Welcome to **Electronic Projects** ! I'm Ariana and I am the assistant bot of this server : I am always here, ready to help you !\n" +
"Here you can learn about Arduino, Processing, 3D printing ... and get some cool project ideas !\n" +
"But before entering the server, please read carefully the rules in "+Main.jda.getGuildById("272754621200596992").getTextChannelById("272754621200596992").getAsMention()+" : even if they are simple, they ensure everyone feels good here !\n" +
"\n" +
"Once you are done, type **\"I agree\"** in this channel ( nothing else than \"I agree\") to access the rest of this server, I'll personally give you your member role :3. Have fun here ! :kissing_heart: ").complete();
            
        }
    }
}
