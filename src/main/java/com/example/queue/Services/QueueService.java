/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.queue.Services;

import com.example.queue.DAO.HolderDAO;
import com.example.queue.DAO.PersonDAO;
import com.example.queue.Models.Holder;
import com.example.queue.Models.Person;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariano
 */
@Service
public class QueueService {
    @Autowired
    private HolderDAO holderDAO;
    @Autowired
    private PersonDAO personDAO;
    private ConcurrentLinkedQueue<Holder> queue = new ConcurrentLinkedQueue<>();
    
    @PostConstruct
    private void initQueue(){
        this.holderDAO.findByState(false).forEach(queue::add);//add from db with state false
        for(int i=0;i<2;i++){
            new Thread(() -> {
                long threadId = Thread.currentThread().getId();
                for(int e=0;e<25;e++){
                    Holder holder = new Holder();
                    holder.setDescripction("T: "+threadId+" index: "+e);
                    this.add(holder);//queue
                }
            }).start();
        }
    }
    public ConcurrentLinkedQueue<Holder> getQueue() {return queue;}
    
    public boolean add(Holder h){
        return this.getQueue().add(this.holderDAO.save(h));
    }
    public Holder poll(String name){
        Holder h =  this.getQueue().poll();
        h.setState(true);
        Person p = personDAO.findFirstByName(name);
        if(p==null){
            p = personDAO.save(new Person(name));
        }
        h.setPerson(p);
        return this.holderDAO.save(h);
    }   
}
