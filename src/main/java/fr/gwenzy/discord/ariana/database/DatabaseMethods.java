/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gwenzy.discord.ariana.database;

import fr.gwenzy.discord.ariana.Main;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.EmbedBuilder;

/**
 *
 * @author gwend
 */
public class DatabaseMethods {
    
    static Connection con;
    static Statement state;
    
    private static Statement connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Creating DB");
            con = DriverManager.getConnection("jdbc:sqlite:Ariana.db");
            state = con.createStatement();
            state.executeUpdate("CREATE TABLE IF NOT EXISTS polls ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "owner TEXT NOT NULL,"
                    + "question TEXT,"
                    + "answers TEXT,"
                    + "votes TEXT,"
                    + "answered TEXT,"
                    + "state INTEGER NOT NULL)");
            return state;
                    } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        return null;
        
    }
    
    public static boolean startPoll(String owner){
        if(playerHasPollInConfig(owner))
            if(!getPollQuestion(owner).equals(""))
                if(!getPollAnswers(owner).equals(""))
                {
                    try{
                        connect();
                        ResultSet result = state.executeQuery("SELECT * FROM polls WHERE owner='"+owner+"' AND state=0");
                        result.next();
                        int ID = result.getInt("id");
                        
                        state.executeUpdate("UPDATE polls SET state = 1 WHERE owner='"+owner+"' AND state=0");
                        disconnect();
                        
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(Color.CYAN);
                        eb.setDescription("A new poll has been created by "+Main.jda.getUserById(owner).getAsMention() +" (Poll ID : "+ID+"). You can now vote on this poll by typing on this channel **@Ariana vote "+ID+" <Answer ID>**");
                        
                        int i = 1;
                                String answers="";
                                    for(String answer :DatabaseMethods.getPollAnswers(ID).split(";")){
                                        if(answer!=""){
                                        answers+=i;
                                        answers+="- ";
                                        answers+=answer;
                                        answers+="\n";
                                        i++;
                                        }
                                    }
                                    
                        eb.addField(getPollQuestion(ID), answers, true);
                        Main.jda.getTextChannelById("280591678757404672").sendMessage(eb.build()).complete();
                        return true;
                    }catch(Exception e){
                        e.printStackTrace();
                        return false;
                    }
                }
        else
                    System.out.println("1");
        else
                    System.out.println("2");
        else
                    System.out.println("3");
        return false;
            
    }
    
    
    public static boolean createPoll(String owner){
        
        if(playerHasPollActive(owner))
            return false;
        else{
            try {
                connect();
                state.executeUpdate("INSERT INTO polls (owner, state) VALUES('"+owner+"', 0)");
                disconnect();
                
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                
                return false;
            }
        }
        
    }
    
    public static String getPollsQuestions(){
        try {
            connect();
            String list="";
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE state != 0");
            while(result.next()){
                list+=result.getString("question");
                list+="\n";
            }
            disconnect();
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
          return "";

        }
    }

    public static String getPollQuestion(String owner){
        try {
            connect();
            String list="";
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE state == 0 AND owner='"+owner+"'");
            result.next();
            list = result.getString("question");
            disconnect();
            if(list==null)
                return "No question set !";
            else
                return list;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
          return "";

        }
    }

    public static String getPollQuestion(int ID){
        try {
            connect();
            String list="";
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE id="+ID);
            result.next();
            list = result.getString("question");
            disconnect();
            
            
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
          return "";

        }
    }
    public static String getPollAnswers(String owner){
        try {
            connect();
            String list="";
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE state = 0 AND owner='"+owner+"'");
            //result.next();
            list = result.getString("answers");
            disconnect();
            if(list==null)
                return "";
            else
                return list;
        } catch (Exception ex) {
            ex.printStackTrace();
          return "";

        }
        
    }
    public static String getPollAnswers(int ID){
        try {
            connect();
            String list="";
            String answers="";
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE id="+ID);
            //result.next();
            list = result.getString("answers");
            disconnect();
            
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
          return "";

        }
        
    }public static String getPollVoters(int ID){
        try {
            connect();
            String list="";
            String answers="";
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE id="+ID);
            //result.next();
            list = result.getString("answered");
            disconnect();
            if(list==null)
                return "";
            else
                return list;
        } catch (Exception ex) {
            ex.printStackTrace();
          return "";

        }
        
    }public static String getPollVotes(int ID){
        try {
            connect();
            String list="";
            String answers="";
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE id="+ID);
            //result.next();
            list = result.getString("votes");
            disconnect();
            if(list==null)
                return "";
            else
                return list;
        } catch (Exception ex) {
            ex.printStackTrace();
          return "";

        }
        
    }public static String getPollVotes(String owner){
        try {
            connect();
            String list="";
            String answers="";
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE owner='"+owner+"' AND state = 0");
            //result.next();
            list = result.getString("votes");
            disconnect();
            if(list==null)
                return "";
            else
                return list;
        } catch (Exception ex) {
            ex.printStackTrace();
          return "";

        }
        
    }
    
    public static boolean hasVoted(int IDPoll, String IDUser){
        if(getPollVoters(IDPoll).contains(IDUser))
            return true;
        else
            return false;
    }
    
    public static boolean addAnswer(String owner, String answer){
        
        try{
            String answers = getPollAnswers(owner);
            String votes = getPollVotes(owner);
            
            connect();
            System.out.println("Adding "+answer+" to "+answers);
            answers+=answer;
            answers+=";";
            votes+="0;";
            System.out.println("Executing: "+"UPDATE polls SET answers='"+answers.replaceAll("'", "''")+"', votes='"+votes+"' WHERE owner ='"+owner+"' AND state=0");
            state.executeUpdate("UPDATE polls SET answers='"+answers.replaceAll("'", "''")+"' WHERE owner ='"+owner+"' AND state=0");
            disconnect();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
                        return false;

        }
    }
    
    public static boolean isPollStarted(int IDPoll){
        try{
            connect();
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE id="+IDPoll+" AND state=1");
            boolean response = result.next();
            disconnect();
            return response;
                    
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public static int addVoter(int IDPoll, String voter, int answerId){
      if(isPollStarted(IDPoll))
       if(!hasVoted(IDPoll, voter))
        try{
            String answers = getPollVoters(IDPoll);
            String votes = getPollVotes(IDPoll);
            HashMap<Integer, Integer> votesList = new HashMap<>();
            int i=1;
            try{
                System.out.println(votes);
            for(String vote : votes.split(";")){
                votesList.put(i, Integer.parseInt(vote));
                i++;
            }
            }catch(Exception e){}
            if(answerId<1 || answerId > votesList.size())
                return 4;
            System.out.println("Break point");
            votesList.put(answerId, votesList.get(answerId)+1);
            
            String newVotes = "";
            for(int i2 : votesList.values()){
                newVotes+=String.valueOf(i2);
                newVotes+=";";
            }
            connect();
            answers+=voter;
            answers+=";";
            System.out.println("Executing: "+"UPDATE polls SET answers='"+answers.replaceAll("'", "''")+"', votes='"+newVotes+"' WHERE id ="+IDPoll);
            state.executeUpdate("UPDATE polls SET answered='"+answers.replaceAll("'", "''")+"', votes='"+newVotes+"' WHERE id ="+IDPoll);
            disconnect();
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
                        return 1;

        }
       else
           return 2;
      else
          return 3;
    }
    public static String getPollsID(){
        try {
            connect();
            String list="";
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE state != 0");
            while(result.next()){
                list+=result.getString("id");
                list+="\n";
            }
            disconnect();
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
          return "";

        }
    }
    
    public static boolean editQuestion(String owner, String question){
        try{
            connect();
            state.executeUpdate("UPDATE polls SET question='"+question.replaceAll("'", "''")+"' WHERE owner ='"+owner+"' AND state=0");
            disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
                        return false;

        }
    }
    
    public static boolean playerHasPollActive(String owner){
        try {
            connect();
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE owner='"+owner+"' AND (state=0 OR state=1)");
            boolean hasActive = result.next();
            disconnect();
            return hasActive;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
                        return true;

        }
        
        
    }
    public static boolean playerHasPollInConfig(String owner){
        try {
            connect();
            ResultSet result = state.executeQuery("SELECT * FROM polls WHERE owner='"+owner+"' AND state = 0");
            boolean hasActive = result.next();
            disconnect();
            return hasActive;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
                        return true;

        }
        
        
    }
    
    public static int getTotalVotes(int IDPoll){
        try {
            connect();
            ResultSet result = state.executeQuery("SELECT * FROM polls where id="+IDPoll);
            result.next();
            String votes = result.getString("votes");
            
            disconnect();
            int nbVotes = 0;
             for(String vote : votes.split(";")){
                 try{
                     nbVotes+=Integer.parseInt(vote);
                 }catch(Exception e){}
             }
                 
                 return nbVotes;
        } catch (SQLException ex) {
            return -1;
        }
        
                
    }
    
    public static String getAnswerDisplay(int IDPoll){
        try {
            String display="";
            connect();
            ResultSet result = state.executeQuery("SELECT * FROM polls where id="+IDPoll);
            
            if(!result.next()){
                disconnect();
                return "Error 0x1C54DF";}
            else{
            String answers = result.getString("answers");
            String votes = result.getString("votes");
            disconnect();
            int totalVotes = getTotalVotes(IDPoll);

            for (int i=0; i<answers.split(";").length; i++){
                display+=answers.split(";")[i];
                display+=" - ";
                if(totalVotes!=0)
                    display+=String.valueOf(Math.round(Double.parseDouble(votes.split(";")[i])/(double)totalVotes*100));
                else
                    display+="/";
                display+="%";
                display+="\n";
                if(totalVotes!=0)
                    for(int i2=0; i2<Math.round(Double.parseDouble(votes.split(";")[i])/(double)totalVotes*50);i2++){
                        display+="#";
                    }
                else
                    display+="----------------";
                display+="\n";
                display+="\n";
            }}
            return display;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
            return "An error has occured :c";
        }
        
        
    }
    
    public static boolean stopPoll(int IDPoll) {
        try{
            if(isPollStarted(IDPoll)){
            connect();
            state.executeUpdate("UPDATE polls SET state = 2 WHERE id="+IDPoll);
            disconnect();
            return true;
            }
            else
                return false;
        }catch(Exception e){return false;}
    }
    public static void initDB(){
        connect();
        disconnect();
               
    }
    
    private static void disconnect(){
        try {
            if(state!=null && con!=null){
            state.close();
            con.close();
        }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    
    
}
