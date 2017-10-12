/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.queue.Controllers;

import com.example.queue.Models.Holder;
import com.example.queue.Services.HolderService;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mariano
 */
@RestController(value = "/")
public class Main {
    @Autowired
    private HolderService holderService;
    
    //from db
    @GetMapping()
    public Iterable<Holder> getAll(){
        return this.holderService.findAll();
    }
    //from queue
    @GetMapping("queue")
    public ConcurrentLinkedQueue<Holder> getQueue(){
        return this.holderService.getQueue();
    }
    //poll queue & update db
    @GetMapping("poll")
    public Holder get(){
        return this.holderService.poll();
    }
}
