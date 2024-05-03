package com.example.assignment.core.service;

import com.example.assignment.config.AbstractUnitServiceTest;
import com.example.assignment.core.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MessageServiceImplTest extends AbstractUnitServiceTest {
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private MessageServiceImpl messageServiceImpl;

    @Test
    void shouldReturnMessage() {
        //GIVEN
        String code = "my.code";
        Locale defaultLocale = Locale.getDefault();
        String expectedMessage = "my message";
        //WHEN
        when(messageSource.getMessage(code, null, defaultLocale)).thenReturn(expectedMessage);
        String actualMessage = messageServiceImpl.getMessage(code);
        //THEN
        verify(messageSource, times(1)).getMessage(code, null, defaultLocale);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrowExceptionInNoMessageAvailableForTheCode() {
        //GIVEN
        String code = "non.existing.code";
        Locale defaultLocale = Locale.getDefault();
        //WHEN
        when(messageSource.getMessage(code, null, defaultLocale)).thenThrow(NoSuchMessageException.class);
        //THEN
        assertThrows(NoSuchMessageException.class, () -> messageServiceImpl.getMessage(code));
        verify(messageSource, times(1)).getMessage(code, null, defaultLocale);

    }
}
