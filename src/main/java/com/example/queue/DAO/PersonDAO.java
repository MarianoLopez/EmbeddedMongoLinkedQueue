/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.queue.DAO;

import com.example.queue.Models.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Mariano
 */
public interface PersonDAO extends MongoRepository<Person, String>{
    Person findFirstByName(String name);
}
