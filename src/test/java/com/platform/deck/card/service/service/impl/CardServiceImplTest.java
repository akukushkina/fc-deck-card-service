package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.mapper.CardMapper;
import com.platform.deck.card.service.repository.CardRepository;
import com.platform.deck.card.service.service.ServiceValidation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    CardMapper cardMapper;

    @Mock
    ServiceValidation serviceValidation;

    @InjectMocks
    CardServiceImpl cardService;
}
