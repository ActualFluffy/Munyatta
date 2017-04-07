/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gwenzy.discord.ariana.events;

import fr.gwenzy.discord.ariana.Main;
import fr.gwenzy.discord.ariana.database.DatabaseMethods;
import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.managers.GuildController;


/**
 *
 * @author gwend
 */
public class ChannelMessageEvent implements EventListener {

    @Override
    public void onEvent(Event event) {
        if(event instanceof GenericGuildMessageEvent){
            GenericGuildMessageEvent ggme = (GenericGuildMessageEvent) event;
            
                try{
                    String message = ggme.getMessage().getContent();
                    if(message.startsWith("@Test ")&&!message.endsWith("@Test ")){
                        Main.commands++;
                        System.out.println(message);
                        String[] args = message.split(" ");
                        if(args[1].equalsIgnoreCase("shutdown")){
                            if(ggme.getAuthor().getId().equals("205809466514472960")){

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
                                ggme.getChannel().sendMessage(eb.build()).complete();
                                Main.jda.shutdown();
                            }
                            else{
                                EmbedBuilder eb = new EmbedBuilder();
                                eb.setColor(Color.RED);
                                eb.addField("Accessing shutdown procedure...", "Checking identity...REFUSED", true);
                                eb.setDescription("Only my master can shut me down !");
                                ggme.getChannel().sendMessage(eb.build()).complete();
                            }
                        }
                        else if(args[1].equalsIgnoreCase("polls")){ 
                            if(args.length == 2){
                                EmbedBuilder eb = new EmbedBuilder();
                                eb.setColor(Color.MAGENTA);
                                eb.addField("Question", DatabaseMethods.getPollsQuestions(), true);
                                eb.addField("Poll ID", DatabaseMethods.getPollsID(), true);
                                ggme.getChannel().sendMessage(eb.build()).complete();
                            }
                            else if(args.length==3){
                                if(args[2].equalsIgnoreCase("create")){
                                    if(DatabaseMethods.createPoll(ggme.getMember().getUser().getId())){
                                        ggme.getChannel().sendMessage("A new poll has been created, you can now edit it. Type @Ariana help if needed !").complete();
                                    }
                                    else{
                                        ggme.getChannel().sendMessage("An error has occured, you may already have a created poll. If no, ask "+Main.jda.getUserById("205809466514472960").getAsMention()+" !").complete();
                                    }
                                }
                                else if(args[2].equalsIgnoreCase("summary")){
                                    if(DatabaseMethods.playerHasPollInConfig(ggme.getMember().getUser().getId())){
                                    EmbedBuilder eb = new EmbedBuilder();
                                eb.setColor(Color.MAGENTA);
                                eb.addField("Owner", ggme.getMember().getUser().getAsMention(), false);
                                eb.addField("Question", DatabaseMethods.getPollQuestion(ggme.getMember().getUser().getId()), false);
                                int i = 1;
                                String answers="";
                                    for(String answer :DatabaseMethods.getPollAnswers(ggme.getMember().getUser().getId()).split(";")){
                                        if(!answer.equals("")){
                                        answers+=i;
                                        answers+="- ";
                                        answers+=answer;
                                        answers+="\n";
                                        i++;
                                        }
                                        else
                                            answers="There is no answers yet !";
                                    }
                                eb.addField("Answers", answers, false);
                                ggme.getChannel().sendMessage(eb.build()).complete();
                                }
                                    else{
                                         ggme.getChannel().sendMessage("There is no poll created").complete();
                                    }
                                }
                                else if(args[2].equalsIgnoreCase("start")){
                                    if(Main.operatorsID.contains(ggme.getAuthor().getId()))
                                    if(!DatabaseMethods.startPoll(ggme.getMember().getUser().getId()))
                                        ggme.getChannel().sendMessage("An error has occured, please check if the poll is correctly configured").complete();
                                    else{
                                        ggme.getChannel().sendMessage("Your poll has been created, you can now find it in "+ggme.getGuild().getTextChannelById("280591678757404672").getAsMention());
                                        
                                    }
                                    
                                }
                                
                            }
                            else if(args.length>4){
                                if(Main.operatorsID.contains(ggme.getAuthor().getId()))
                                if(args[2].equalsIgnoreCase("edit")){
                                    if(args[3].equalsIgnoreCase("question")){
                                        String question = "";
                                        for(int i=4; i<args.length; i++){
                                            question += args[i];
                                            question += " ";
                                        }
                                        if(DatabaseMethods.editQuestion(ggme.getMember().getUser().getId(), question))
                                            ggme.getChannel().sendMessage("Question has been updated").complete();
                                        else
                                            ggme.getChannel().sendMessage("An error has occured").complete();
                                    }
                                }
                                if(args.length>5){
                                if(args[2].equalsIgnoreCase("edit"))
                                if (args[3].equalsIgnoreCase("answers")){
                                     if(args[4].equalsIgnoreCase("add")){
                                         String answer = "";
                                         for(int i=5; i<args.length; i++){
                                            answer += args[i];
                                            answer += " ";
                                        }
                                         if(DatabaseMethods.addAnswer(ggme.getMember().getUser().getId(), answer))
                                            ggme.getChannel().sendMessage("Answer has been added").complete();
                                        else
                                            ggme.getChannel().sendMessage("An error has occured").complete();
                                     }   
                                }
                            }
                                
                            }
                            
                        }
                        else if(args[1].equalsIgnoreCase("vote")){
                            if(ggme.getChannel().getId().equals("280591678757404672"))
                            if(args.length > 3)
                            {
                                try{
                                    System.out.println(args[2]);
                                    System.out.println(args[3]);
                                    int IDPoll = Integer.parseInt(args[2]);
                                    int answerId = Integer.parseInt(args[3]);
                                    
                                    switch(DatabaseMethods.addVoter(IDPoll, ggme.getAuthor().getId(), answerId)){
                                        case 0:
                                            ggme.getAuthor().openPrivateChannel().complete().sendMessage("Your vote has been taken into account, thank you ! :3").complete();
                                            break;
                                        case 1:
                                            ggme.getAuthor().openPrivateChannel().complete().sendMessage("An error has occured :'( please contact "+ggme.getGuild().getMemberById("205809466514472960")).complete();
                                            break;
                                        case 2:
                                            ggme.getAuthor().openPrivateChannel().complete().sendMessage("You can only vote 1 time >.<").complete();
                                            break;
                                        case 3:
                                            ggme.getAuthor().openPrivateChannel().complete().sendMessage("This poll doesn't exist, isn't started yet or already finished !").complete();
                                            break;
                                        case 4:
                                            ggme.getAuthor().openPrivateChannel().complete().sendMessage("This answer doesn't exists, please try with a correct one.").complete();
                                            break;
                                    
                                    }
                                    
                                    
                                }catch(NumberFormatException e){
                                    
                                ggme.getAuthor().openPrivateChannel().complete().sendMessage("There is a problem with your answer, please check your syntax.").complete();
                                    
                                }
                                catch(Exception e){
                                ggme.getAuthor().openPrivateChannel().complete().sendMessage("An unknown error has occured...").complete();
                                e.printStackTrace();
                                }
                            }
                            else{
                                ggme.getAuthor().openPrivateChannel().complete().sendMessage("There is a problem with your answer, please check your syntax.").complete();
                                    }
                                ggme.getMessage().deleteMessage().complete();
                        }
                        else if(args[1].equalsIgnoreCase("stop")){
                            if(Main.operatorsID.contains(ggme.getAuthor().getId()))
                                if(args.length>2)
                                try{
                                    EmbedBuilder eb = new EmbedBuilder();
                                    int IDPoll = Integer.parseInt(args[2]);

                                    if(DatabaseMethods.stopPoll(IDPoll))
                                    {
                                        eb.setColor(Color.ORANGE);
                                        eb.setDescription("Poll "+IDPoll+" just finished with "+DatabaseMethods.getTotalVotes(IDPoll)+" votes");
                                        eb.addField(DatabaseMethods.getPollQuestion(IDPoll), DatabaseMethods.getAnswerDisplay(IDPoll), true);
                                        if(!DatabaseMethods.getAnswerDisplay(IDPoll).equals("Error 0x1C54DF"))
                                            ggme.getChannel().sendMessage(eb.build()).complete();
                                    }
                                    
                                }catch(Exception e){}
                             ggme.getMessage().deleteMessage().complete();
                        }
                        else if(args[1].equalsIgnoreCase("stats")){
                             if(Main.operatorsID.contains(ggme.getAuthor().getId()))
                                if(args.length>2)
                                try{
                                    EmbedBuilder eb = new EmbedBuilder();
                                    int IDPoll = Integer.parseInt(args[2]);

                                    eb.setColor(Color.PINK);
                                    eb.setDescription("Poll "+IDPoll+" statistics : "+DatabaseMethods.getTotalVotes(IDPoll)+" votes");
                                    eb.addField(DatabaseMethods.getPollQuestion(IDPoll), DatabaseMethods.getAnswerDisplay(IDPoll), true);
                                    if(!DatabaseMethods.getAnswerDisplay(IDPoll).equals("Error 0x1C54DF"))
                                        ggme.getChannel().sendMessage(eb.build()).complete();
                                    
                                }catch(Exception e){}
                             ggme.getMessage().deleteMessage().complete();
                        }

                        
                        else{
                                ggme.getChannel().sendMessage("Unknown command").complete();
                        }
                    }

                    if(ggme.getChannel().getId().equalsIgnoreCase("273586021260722177")){
                        Member member = ggme.getMember();
                        if(ggme.getMessage().getContent().equalsIgnoreCase("I agree"))
                        if(!member.getRoles().contains(Main.jda.getGuildById("272754621200596992").getRoleById("274997401528434689"))){

                            GuildController gc = Main.jda.getGuildById("272754621200596992").getController();
                            gc.addRolesToMember(member, Main.jda.getGuildById("272754621200596992").getRoleById("274997401528434689")).complete();
                            System.out.println("A new member has been accepted : "+ggme.getAuthor().getName());
                            ggme.getAuthor().openPrivateChannel().complete();
                                    ggme.getAuthor().getPrivateChannel().sendMessage("Hey "+ggme.getAuthor().getName()+" ! You're now a member of "+ggme.getChannel().getGuild().getName()+" ! I'll ensure that you will respect the rules you just accepted, enjoy ! c:\n"
                                    + "If you have any questions, you can ask an Administrator or a Moderator \\(^_^)/").complete();
                        }

                    }
                    
        }catch(Exception e){
        }
        }}
    }
    
    

