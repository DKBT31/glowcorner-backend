package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, Integer> {
    Optional<Feedback> findFeedbackByFeedbackID(String feedbackID);
    void deleteFeedbackByFeedbackID(String feedbackID);
}