/*
    Quests are currently done by using a directed graph of tasks. The graph has a few rules, it has to start with one node, being the start, and end with a single node, being the completion. (If you have multiple final tasks just make them all point to one singular final task, with no completion requirements.) Tasks have associated single events that are simple actions capable of being tripped up by the Observers in gamecore. 
*/

import java.util.*;
class Quest {
    private final int questID;
    private final String questName;
    private Graph <Task> tasks;
    private ArrayList<Task> activeTasks;
    private int questStatus;
    private ArrayList<Integer> unlockOnCompletionQuestIDs;
    private Task headNode;
    public class Task {
        private final int taskNumber;
        private boolean isFinalNode;  //The tail node is the singular final node that trips completion of the quest
        private Event completionReq; 
        public ArrayList<NPC> updatedTalksOnCompletion;
        public Task(int taskNumber, boolean isFinalNode, String type, String val){
            this.completed = false;
            this.taskNumber = taskNumber;
            this.isFinalNode = isFinalNode;
            this.updatedTalksOnCompletion = new ArrayList<NPC>();
            switch(type.toLower()) {
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
            }
        }
        public int getTaskNumber(){ return this.taskNumber; }
        public String getEventType(){ return completionReq.getType(); }
        public boolean checkEventCompletion(String val) {
            return completionReq.checkCompletion(val);
        }
        abstract public class Event {
            private String type;
            public String completionString;
            public String getType() { return this.type; }
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
                this.completiongString = "You have completed task: find a " + itemName; 
            }  
            public boolean checkCompletion(String val) {
                return val.equals(itemName);
            }  
        }
        //A talk event is tripped when an npc talks to a particular quest npc.
        public class TalkEvent extends Event {
            public NPC npc;
            public TalkEvent(NPC npc) {
                this.npc = npc;
                this.completionString = "You have completed task: Talk to " +npc.getName();
            }
            public boolean checkCompletion(String val) {
                return val.equals(npc.getName);
            }
        }
        public class rpsWinEvent extends Event {   
            public rpsWinEvent(){
                this.completionString = "You have completed task: Win a game of Rock Paper Scissors";
            }
            public boolean checkCompletion(String val) { //Passes a string because it makes all the mehod signatures the same. Helps w/ polymorphism
                return true;
            }
        }   
    }
    
    public Quest(){
        this.questStatus = 0;
        unlockOnCompletionQuestIDs = new ArrayList<Integer>();
        tasks = new Graph<Task>(); //Patricks part. 
        boolean firstNode = true;    
        while(THEREAREMORETASKSTOREAD){
            ArrayList inbound = new ArrayList<Integer>(); //TaskNumbers of all tasks that route to this new node. Should be stored in the file. Cycles will break this system
            Task t = new Task(VALUESFROMFILE);
            tasks.add(t);
            for(int i = 0; i<inbound.size(); i++) {
                tasks.connect(getTask(inbound.get(i)),t);
                i--;
            }
            if(firstNode){
                headNode = t;
                firstNode = false;
            }
        }
        questID = VALUEFROMFILE;
        questName = VALUEFROMFILE;
        activeTasks = new ArrayList<Task>();
        activeTasks.add(VALUEFROMFILE); //I need the head node to be added here. 
    }
    public int getQuestID() { return this.questID; } 
    public String updateQuests(String type, String val){
        String notify; //The string that notifies the user that they have completed a task
        int i = 0;
        while(i<activeTasks.size()) {
            Task t = activeTasks.get(i);
        if(!t.getEventType().equals(type)) {
                continue; //We only want tasks that match the type parameter
            }
            if(t.checkEventCompletion(val)) {
                activeTasks.remove(t);
                activeTasks.add(tasks.getOutbound(t));      
                notify = t.completionReq.completionString;    
            }
            i++;
        }
        return notify;
    }


    //Iterates through the graph looking for the task of the paramter taskID, returns null if not found.  
    

    public Task getTask(int taskID) {
        Stack<Task> search = new Stack<Task>();
        search.add(headNode);
        while(search.size()>0&&search.peek().getTaskID()!=taskID) {
                Task t = search.peek();
                search.pop();
                
        }
        if(search.size()==0) {
            return null;
        } else {
            return search(0);
        }
    }

}
