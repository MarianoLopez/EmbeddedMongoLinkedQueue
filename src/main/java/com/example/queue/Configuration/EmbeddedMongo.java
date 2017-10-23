/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.queue.Configuration;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;


import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;
import java.io.File;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
/**
 *
 * @author Mariano
 */
@Configuration
public class EmbeddedMongo {
    @Value("${mongo.db.url}")
    private String MONGO_DB_URL;
    @Value("${mongo.db.port}")
    private String MONGO_DB_PORT;
    @Value("${mongo.db.dirPath}")
    private String MONGO_DB_DIRPATH;

    MongodStarter starter = MongodStarter.getDefaultInstance();
    MongodExecutable mongodExecutable;

    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        MongoClient mongo = new MongoClient(MONGO_DB_URL, Integer.valueOf(MONGO_DB_PORT));
        return new SimpleMongoDbFactory(mongo, "store");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    @PostConstruct
    public void construct() throws UnknownHostException, IOException {
        deleteLock();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .replication(new Storage(MONGO_DB_DIRPATH, null, 0))
                .net(new Net(MONGO_DB_URL, Integer.valueOf(MONGO_DB_PORT), true))
                .build();
        mongodExecutable = starter.prepare(mongodConfig);
        MongodProcess mongod = mongodExecutable.start();
    }
    
    private void deleteLock(){
        try {
            Files.deleteIfExists(new File(MONGO_DB_DIRPATH+"\\mongod.lock").toPath());
        } catch (IOException ex) {
            Logger.getLogger(EmbeddedMongo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void destroy() {
        if (mongodExecutable != null) {mongodExecutable.stop();}
    }
}
