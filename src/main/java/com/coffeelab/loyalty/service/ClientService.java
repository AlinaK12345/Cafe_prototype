package com.coffeelab.loyalty.service;

import com.coffeelab.loyalty.dto.ClientDto;
import com.coffeelab.loyalty.model.BonusTxn;
import com.coffeelab.loyalty.model.BonusTxnType;
import com.coffeelab.loyalty.model.Client;
import com.coffeelab.loyalty.repo.BonusTxnRepository;
import com.coffeelab.loyalty.repo.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final BonusTxnRepository bonusTxnRepository;

    public ClientService(ClientRepository clientRepository, BonusTxnRepository bonusTxnRepository) {
        this.clientRepository = clientRepository;
        this.bonusTxnRepository = bonusTxnRepository;
    }

    public ClientDto findByPhone(String phone) {
        Client client = clientRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with phone: " + phone));
        return toDto(client);
    }

    @Transactional
    public ClientDto register(String phone, String fullName) {
        if (clientRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Client already exists with phone: " + phone);
        }

        Client client = new Client();
        client.setPhone(phone);
        client.setFullName(fullName);
        client.setBonusBalance(BigDecimal.ZERO);
        client = clientRepository.save(client);

        // Welcome bonus +50
        BonusTxn bonusTxn = new BonusTxn();
        bonusTxn.setClient(client);
        bonusTxn.setTxnType(BonusTxnType.EARN);
        bonusTxn.setSignedAmount(new BigDecimal("50"));
        bonusTxn.setReason("Welcome bonus");
        bonusTxnRepository.save(bonusTxn);

        client.setBonusBalance(client.getBonusBalance().add(new BigDecimal("50")));
        client = clientRepository.save(client);

        return toDto(client);
    }

    private ClientDto toDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.id = client.getId();
        dto.phone = client.getPhone();
        dto.fullName = client.getFullName();
        dto.bonusBalance = client.getBonusBalance();
        return dto;
    }
}