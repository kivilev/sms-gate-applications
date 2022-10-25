package com.kivilev.service;

import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    @Override
    public boolean isClientExists(Long clientId) {
        // TODO: реализовать обращение в common-client сервис с кэшом, убрать заглушку
        return clientId <= 10;
    }
}
