package com.glowcorner.backend.service.interfaces;

public interface CounterService {
    String getNextUserID();
    String getNextProductID();
    String getNextOrderID();
    String getNextOrderDetailID();
    String getNextSkinCareRoutineID();

    String getNextFeedbackID();
    String getNextQuestionID();
    String getNextPromotionID();
    String getNextOptionID();

    void updateCounter(String counterId, long sequence);
}
