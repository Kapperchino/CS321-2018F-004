/*
    Quests are currently done by using a directed graph of tasks. The graph has a few rules, it has to start with one node, being the start, and end with a single node, being the completion. (If you have multiple final tasks just make them all point to one singular final task, with no completion requirements.) Tasks have associated single events that are simple actions capable of being tripped up by the Observers in gamecore. 
*/

import java.util.*;
class Quest extends  {
    private final int questID;
    private final String questName;
    private Graph <Task> tasks;
    private ArrayList<int> activeTasks;
    public class Task {
        private final int taskNumber;
        private boolean Completed; 
        private boolean isFinalNode;  //The tail node is the singular final node that trips completion of the quest
        private boolean isCompletableOutOfOrder; //defaults to false.
        private Event completionReq; 
        private Task(int taskNumber, boolean isFinalNode){
            this.completed = false;
            this.taskNumber = taskNumber;
            this.isFinalNode = isFinalNode;
            this.isCompletableOutOfOrder = false;
        }
        public int getTaskNumber(){ return this.taskNumber; }
        public MoveTask(int taskNumber, boolean isFinalNode, String location) {
            this = new Task(taskNumber, isFinalNode);
            this.completionReq = new MoveEvent(location);
        }
        public PickupTask(int taskNumber, boolean isFinalNode, String itemName) {
            this = new Task(taskNumber, isFinalNode);
            this.completionReq = new PickupEvent(itemName);
        }
        public TalkTask(int taskNumber, boolean isFinalNode, NPC npc) {
            this = new Task(taskNumber, isFinalNode);
            this.completionReq = new TalkEvent(npc);
        }

        abstract public class Event {} //This exists so that We can store Events in general.
        //A move event is tripped when a Player moves to a particular spot (String location)
        public class MoveEvent extends Event {
            public String location; 
            public MoveEvent(String location) {
                this.location = location;
            }
        }
        //A pickup event is tripped whenever a user picks up a particular item (String itemName)
        public class PickupEvent extends Event {
            public String itemName;
            public String locationName; //Optional
            public PickupEvent(String itemName){
                this.itemName = itemName; //TODO: check if the item string is valid. 
            }    
            public PickupEvent(String itemName, String locationName){
                this = new PickupEvent(itemName);
                this.locationName = locationName;
            }
        }
        //A talk event is tripped when an npc talks to a particular quest npc.
        public TalkEvent extends Event {
            public NPC npc;
            public TalkEvent(NPC npc) {
                this.npc = npc;
            }
        }

    }
    
    public Quest(int questID, String questName, Graph<Task> tasks){
        tasks = null; //Patricks part. 
        while(THEREAREMORETASKSTOREAD){
            ArrayList inbound = new ArrayList<int>(); //TaskNumbers of all tasks that route to this new node. Should be stored in the file. Cycles will break this system
            Task t = new Task(VALUESFROMFILE);
            tasks.add(t);
            int i = inbound.size();
            while(i>0) {
                tasks.connect(inbound(i),t);
                i--;
            }
            if(t.isCompletableOutOfOrder){
                activeTasks.add(t.getTaskNumber);
            }
        }
        questID = null;
        questName = null;
        activeTasks = new ArrayList<int>();
        activeTasks.add(); //I need the head node to be added here. 
         
    

    }
    //Iterates through the graph looking for the task of the paramter taskID, returns null if not found.  
    public Task getTask(int taskID) {
        Iterator it = tasks.iterator(); //Damn you java! I want pointers so i dont need o(n) runtime for something that should be o(1)!
        Task next = null;
        while(it.hasNext()){
            next = it.next();
            if(next.getTaskNumber()==taskID) {
                return next;
            }
        }
        return null;
    }
}
