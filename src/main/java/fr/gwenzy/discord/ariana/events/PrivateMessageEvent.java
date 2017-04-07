package fr.gwenzy.discord.ariana.events;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import fr.gwenzy.discord.ariana.Main;
import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.priv.GenericPrivateMessageEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 *
 * @author gwend
 */
public class PrivateMessageEvent implements EventListener{
    
    @Override
    public void onEvent(Event event) {
        if(event instanceof GenericPrivateMessageEvent){
            GenericPrivateMessageEvent gpme = (GenericPrivateMessageEvent) event;
            if(gpme.getAuthor().getId().equals("205809466514472960")){
                if(gpme.getMessage().getContent().equalsIgnoreCase("Shutdown !")){
                    Main.commands++;
                    
                    long totalSeconds = (System.currentTimeMillis()-Main.launchTimestamp)/1000;
                    long seconds = totalSeconds % 60;
                    long totalMinutes = totalSeconds / 60;
                    long minutes = totalMinutes % 60;
                    long hours = totalMinutes / 60;
                    
                    String time = hours+"h "+minutes+"m "+seconds+"s";
                    
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.GREEN);
                    eb.addField("Excecution time", time, true);
                    eb.addField("Commands used", String.valueOf(Main.commands), true);
                    eb.setDescription("Bot stats during this session");
                    gpme.getAuthor().getPrivateChannel().sendMessage(eb.build()).complete();
                    Main.jda.shutdown();

                }else if(gpme.getMessage().getContent().equalsIgnoreCase("Roles")){
                    Main.commands++;
                    gpme.getChannel().sendMessage(Main.jda.getGuildById("272754621200596992").getRoles().toString()).complete();
                    
                }
                
            }
        }
    }
    
}
