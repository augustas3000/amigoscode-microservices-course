package com.gustyflows.fraud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class DefaultFraudCheckService implements IFraudCheckService {

    private final FraudCheckHistoryRepository fraudCheckHistoryRepository;

    @Override
    public boolean isFraudulent(Integer customerId) {
        fraudCheckHistoryRepository.save(
             FraudCheckHistory.builder()
             .customerId(customerId)
             .isFraudster(false)
             .createdAt(LocalDateTime.now())
             .build()
        );

        return false;
        //in real life this would probably call a thrid party system
        //or perhaps a more complex implementation
    }
}
