/*
    Quests are currently done by using a directed graph of tasks. The graph has a few rules, it has to start with one node, being the start, and end with a single node, being the completion. (If you have multiple final tasks just make them all point to one singular final task, with no completion requirements.) Tasks have associated single events that are simple actions capable of being tripped up by the Observers in gamecore. 
*/

import java.util.*;
class Quest {
    private final String questName;
    private LinkedList <Task> tasks;
    private Task activeTask;
    public class Task {
        Event completionReq; 
        public Task(String type, String val){
            switch(type.toLowerCase()) {
                case "move":
                    this.completionReq = new MoveEvent(val);    
                    break;
                case "pickup":
                    this.completionReq = new PickupEvent(val);
                    break;
                case "talk":
                    this.completionReq = new TalkEvent(val);
                    break;
                case "rpswin":
                    this.completionReq = new rpsWinEvent();
                    break;
                case "shopevent":
                    this.completionReq = new shopEvent();
                    break;
                case "sayevent":
                    this.completionReq = new sayEvent();
                    break;
                case "ghoulpokeEvent":
                    this.completionReq = new ghoulPokeEvent();
            }
        }
        public String getEventType(){ return completionReq.getType(); }
        public boolean checkEventCompletion(String val) {
            return completionReq.checkCompletion(val);
        }
        public String getCompletionString() {
           return completionReq.completionString;
        }
        abstract public class Event {
            private String type;
            public String completionString;
            public String getType() { return this.type; }
            public boolean checkCompletion(String val) {return false;} //Needed so the child classes can overload this.
        } //This exists so that We can store Events in general.
        //A move event is tripped when a Player moves to a particular spot (String location)
        public class MoveEvent extends Event {
            public String location; 
            public MoveEvent(String location) {
                this.location = location;
                this.completionString = "You have completed task: visit " + location;
            }
            public boolean checkCompletion(String val) {
                return location.equals(val);
            }
        }
        //A pickup event is tripped whenever a user picks up a particular item (String itemName)
        public class PickupEvent extends Event {
            public String itemName;
            public PickupEvent(String itemName){
                this.itemName = itemName;
                this.completionString = "You have completed task: find a " + itemName; 
            }  
            public boolean checkCompletion(String val) {
                return true;
            }  
        }
        //A talk event is tripped when an npc talks to a particular quest npc.
        public class TalkEvent extends Event {
            public String npcName;
            public TalkEvent(String npcName) {
                this.npcName = npcName;
                this.completionString = "You have completed task: Talk to " + npcName;
            }
            public boolean checkCompletion(String val) {
                return val.equals(npcName);
            }
        }
        public class rpsWinEvent extends Event {   
            public rpsWinEvent(){
                this.completionString = "You have completed task: Win a game of Rock Paper Scissors";
            }
            public boolean checkCompletion(String val) { //Passes a string because it makes all the method signatures the same. Helps w/ polymorphism
                return true;
            }
        } 
        public class shopEvent extends Event {
            public shopEvent() { 
                this.completionString = "You have completed task: Buy from the shop";
            }            
            public boolean checkCompletion(String val) {
                return true;
            }
        }
        public class sayEvent extends Event {
            public sayEvent() {
                this.completionString = "You have completed task: say something";
            }
            public boolean checkCompletion(String val) {
                return true;
            }
        }
        public class ghoulPokeEvent extends Event {
            public ghoulPokeEvent() {
                this.completionString = "You have completed task: Poke a ghoul";
            }
            public boolean checkCompletion(String val) {
                return true;
            }
        }
        
      
    }
    public String getQuestName(){
        return this.questName;
    }
    public Quest(String questName){
        tasks = new LinkedList<Task>();
        //Add the tasks in. Im not sure what they look like.
        this.questName = questName;
        tasks.add(new Task("move", "Exploratory Hall"));
        tasks.add(new Task("shopevent", ""));
        tasks.add(new Task("pickupevent", ""));
        /*    
        tasks.add(new Task("sayevent", ""));
        tasks.add(new Task("ghoulpokeevent",""));
        tasks.add(new Task("talkevent","professor"));
        tasks.add(new Task("rpswinevent", ""));
        */
    } 
    public int updateQuests(String type, String val){
        if(!activeTask.getEventType().equals(type)) {
            return 0; //Completed event does not match required event
        }
        if(activeTask.checkEventCompletion(val)) {
            System.out.println(activeTask.getCompletionString());
            tasks.remove(activeTask);
            if(tasks.size()==0) {
                return 2; //Task completed, which finished the quest
            }
            activeTask = tasks.get(0);
        }
        return 1; //Task completed, but quest is not completed
    }  
}
