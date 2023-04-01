package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashMap<String,User> userMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>(); //used
        this.groupUserMap = new HashMap<Group, List<User>>(); //used
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>(); //used
        this.userMap = new HashMap<String,User>(); //used
        this.customGroupCount = 1;
        this.messageId = 0;
    }

    public boolean checkNewUser(String number){
        if(userMap.containsKey(number)){
            return true;
        }
        return false;
    }
    public String createUser(String name,String number) throws Exception {
        userMap.put(number,new User(name,number));
        return "SUCCESS";
    }
    public Group createGroup(List<User> users){
        if(users.size()==2){
            Group group= new Group(users.get(1).getName(),users.size());
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            return group;
        }
        Group group= new Group("Group "+customGroupCount,users.size());
        groupUserMap.put(group,users);
        adminMap.put(group,users.get(0));
        customGroupCount++;

        return group;
    }
    public int createMessage(String message){
        messageId++;
        Message message1=new Message(messageId,message);
        return messageId;
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        Boolean flag=false;
        for(User user: groupUserMap.get(group)){
            if(user.equals(sender)){
                flag=true;
                break;
            }
        }
        if(!flag){
            throw new Exception("You are not allowed to send message");
        }
        senderMap.put(message,sender);
        List<Message> msg= groupMessageMap.getOrDefault(group,new ArrayList<Message>());
        msg.add(message);
        groupMessageMap.put(group,msg);
        return groupMessageMap.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        if(!adminMap.get(group).equals(approver)) throw new Exception("Approver does not have rights");
        Boolean flag=false;
        for(User user1:groupUserMap.get(group)){
            if (user1.equals(user)){
                flag=true;
                break;
            }
        }
        if(!flag) throw new Exception("User is not a participant");
        adminMap.put(group,user);

        return "SUCCESS";
    }





    /*
    Map<String,User> userMap= new HashMap<>();
    Map<String,Group> groupMap= new HashMap<>();
    Map<Integer,Message> messageMap=new HashMap<>();
    private int count=1,messageCount=1;

    public String createUser(String name,User user){
        if(userMap.containsKey(name)){
            // Throw exceptions;
        }
        userMap.put(name,user);
        return "Success";
    }

    public String createGroup(List<User> users){
        if(users.size()<2) return "Atleast 2 users are required to create a group";
        else if(users.size()==2){
            if(groupMap.containsKey(users.get(1).getName())){
                //exception
            }
            else{
                groupMap.put(users.get(1).getName(),new Group(users.get(1).getName(),users.get(0).getName(),users));
            }
        }
        else{
            groupMap.put("Group "+count,new Group("Group "+count,users.get(0).getName(),users));
            count++;
        }
        return "Group Successfully created";
    }

    public int createMessage(String message){
        messageMap.put(messageCount,new Message(messageCount,message));
        int ID=messageCount;
        messageCount++;
        return ID;
    }

    public int sendMessage(Message message, User sender, Group group){
        if(!groupMap.containsKey(group.getName())){
            //throw
        }
        Boolean flag=false;
        for(User user: groupMap.get(group.getName()).getGroupUser()){
            if(user.equals(sender)){
                flag=true;
                break;
            }
        }
        if(!flag){
            // throw
        }
        groupMap.get(group.getName()).getGroupMessage().add(message);
        return groupMap.get(group.getName()).getGroupMessage().size();

    }

    public String changeAdmin(User approver, User user, Group group){

        return "Success";
    }

     */

}