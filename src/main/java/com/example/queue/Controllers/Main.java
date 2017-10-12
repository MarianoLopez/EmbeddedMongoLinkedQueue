/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.queue.Controllers;

import com.example.queue.DAO.HolderDAO;
import com.example.queue.DAO.PersonDAO;
import com.example.queue.Models.Holder;
import com.example.queue.Models.Person;
import com.example.queue.Services.QueueService;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mariano
 */
@RestController(value = "/")
public class Main {
    @Autowired
    private QueueService queueService;
    @Autowired
    private HolderDAO holderDAO;
    @Autowired
    private PersonDAO personDAO;
    //from db
    @GetMapping()
    public Iterable<Holder> getAll(){
        return this.holderDAO.findAll();
    }
    //from queue
    @GetMapping("queue")
    public ConcurrentLinkedQueue<Holder> getQueue(){
        return this.queueService.getQueue();
    }
    //poll queue & update db
    @GetMapping("poll")
    public Holder get(@RequestParam(value = "name", required = false, defaultValue = "Test")String name){
        return this.queueService.poll(name);
    }
    //from db
    @GetMapping("persons")
    public Iterable<Person> getAllPersons(){
        return this.personDAO.findAll();
    }
}
