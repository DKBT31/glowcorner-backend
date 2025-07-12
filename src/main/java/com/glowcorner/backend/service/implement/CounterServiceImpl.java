package com.glowcorner.backend.service.implement;

import com.glowcorner.backend.entity.mongoDB.Counter;
import com.glowcorner.backend.repository.Counter.CounterRepository;
import com.glowcorner.backend.service.interfaces.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounterServiceImpl implements CounterService {

    @Autowired
    private CounterRepository counterRepository;

    // <editor-fold defaultstate="collapsed" desc="getNextUserID">
    @Override
    public String getNextUserID() {
        Counter counter = counterRepository.findById("userID").orElse(null);
        if (counter == null) {
            counter = new Counter();
            counter.setId("userID");
            counter.setSequence(1);
        } else {
            counter.setSequence(counter.getSequence() + 1);
        }
        counterRepository.save(counter);
        return String.format("%04d", counter.getSequence()); // Formats as "0001"
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getNextProductID">
    @Override
    public String getNextProductID() {
        Counter counter = counterRepository.findById("productID").orElse(null);
        if (counter == null) {
            counter = new Counter();
            counter.setId("productID");
            counter.setSequence(1); // Start at 1
        } else {
            counter.setSequence(counter.getSequence() + 1);
        }
        counterRepository.save(counter);
        return String.format("1%03d", counter.getSequence()); // "1001"
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getNextOrderID">
    @Override
    public String getNextOrderID() {
        Counter counter = counterRepository.findById("orderID").orElse(null);
        if (counter == null) {
            counter = new Counter();
            counter.setId("orderID");
            counter.setSequence(1);
        } else {
            counter.setSequence(counter.getSequence() + 1);
        }
        counterRepository.save(counter);
        return String.format("A%03d", counter.getSequence()); // "A001"
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getNextOrderDetailID">
    @Override
    public String getNextOrderDetailID() {
        Counter counter = counterRepository.findById("orderDetailID").orElse(null);
        if (counter == null) {
            counter = new Counter();
            counter.setId("orderDetailID");
            counter.setSequence(1);
        } else {
            counter.setSequence(counter.getSequence() + 1);
        }
        counterRepository.save(counter);
        return String.format("B%03d", counter.getSequence()); // "B001"
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getNextSkinCareRoutineID">
    @Override
    public String getNextSkinCareRoutineID() {
        Counter counter = counterRepository.findById("routineID").orElse(null);
        if (counter == null) {
            counter = new Counter();
            counter.setId("routineID");
            counter.setSequence(1);
        } else {
            counter.setSequence(counter.getSequence() + 1);
        }
        counterRepository.save(counter);
        return String.format("C%03d", counter.getSequence()); // "C001"
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getNextFeedbackID">
    @Override
    public String getNextFeedbackID() {
        Counter counter = counterRepository.findById("feedbackID").orElse(null);
        if (counter == null) {
            counter = new Counter();
            counter.setId("feedbackID");
            counter.setSequence(1);
        } else {
            counter.setSequence(counter.getSequence() + 1);
        }
        counterRepository.save(counter);
        return String.format("F%03d", counter.getSequence()); // "F001"
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getNextQuestionID">
    @Override
    public String getNextQuestionID() {
        Counter counter = counterRepository.findById("questionId").orElse(null);
        if (counter == null) {
            counter = new Counter();
            counter.setId("questionId");
            counter.setSequence(1);
        } else {
            counter.setSequence(counter.getSequence() + 1);
        }
        counterRepository.save(counter);
        return String.format("Q%03d", counter.getSequence()); // "Q001"
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getNextPromotionID">
    @Override
    public String getNextPromotionID() {
        Counter counter = counterRepository.findById("promotionID").orElse(null);
        if (counter == null) {
            counter = new Counter();
            counter.setId("promotionID");
            counter.setSequence(1);
        } else {
            counter.setSequence(counter.getSequence() + 1);
        }
        counterRepository.save(counter);
        return String.format("P%03d", counter.getSequence()); // "P001"
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getNextOptionID">
    @Override
    public String getNextOptionID() {
        Counter counter = counterRepository.findById("optionID").orElse(null);
        if (counter == null) {
            counter = new Counter();
            counter.setId("optionID");
            counter.setSequence(1);
        } else {
            counter.setSequence(counter.getSequence() + 1);
        }
        counterRepository.save(counter);
        return String.format("O%03d", counter.getSequence()); // "O001"
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="updateCounter">
    @Override
    public void updateCounter(String counterId, long sequence) {
        Counter counter = counterRepository.findById(counterId).orElse(new Counter());
        counter.setId(counterId);
        counter.setSequence(sequence);
        counterRepository.save(counter);
    }
    // </editor-fold>
}
