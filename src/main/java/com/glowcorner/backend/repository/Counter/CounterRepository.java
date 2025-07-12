package com.glowcorner.backend.repository.Counter;

import com.glowcorner.backend.entity.mongoDB.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends MongoRepository<Counter, String> {
}