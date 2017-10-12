/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.queue.DAO;

import com.example.queue.Models.Holder;
import java.util.List;
import org.springframework.data.repository.CrudRepository;


/**
 *
 * @author Mariano
 * https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
 */

public interface HolderDAO extends CrudRepository<Holder, String>{
    
    List<Holder> findByState(boolean state);
}
