/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gwenzy.discord.ariana;

import fr.gwenzy.discord.ariana.database.DatabaseMethods;
import fr.gwenzy.discord.ariana.events.*;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 *
 * @author gwend
 */
public class Main {
    
    public static JDA jda;
    public static final long launchTimestamp = System.currentTimeMillis();
    public static final List<String> operatorsID = Arrays.asList("205809466514472960", "224940744362819584");
    public static int commands;
    public static void main(String[] args){
        try {
            
            commands=0;
            JDABuilder jdaB = new JDABuilder(AccountType.BOT);
            jdaB.setGame(Game.of("@Ariana help"));
            //jdaB.setToken("BOT TOKEN");
            jdaB.setToken("BOT TOKEN");
            DatabaseMethods.initDB();
            jdaB.addListener(new PrivateMessageEvent());
            jdaB.addListener(new GuildMemberJoinListener());
            jdaB.addListener(new ChannelMessageEvent());
            jda = jdaB.buildBlocking();
            
            
            
            
            
        } catch (LoginException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RateLimitedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
