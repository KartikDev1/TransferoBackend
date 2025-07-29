package com.example.Transfero.config;


import com.example.Transfero.model.FileMeta;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Configuration
public class MongoTTLConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void initTTLIndex() {
        IndexOperations indexOps = mongoTemplate.indexOps(FileMeta.class);
        Index ttlIndex = new Index()
                .on("createdAt", org.springframework.data.domain.Sort.Direction.ASC)
                .expire(300); // Expire after 600 seconds = 10 minutes
        indexOps.createIndex(ttlIndex);
    }

}
