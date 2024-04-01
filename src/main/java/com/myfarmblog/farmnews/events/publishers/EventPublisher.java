package com.myfarmblog.farmnews.events.publishers;

import com.myfarmblog.farmnews.events.event.CompleteRegistrationEvent;
import com.myfarmblog.farmnews.utils.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void completeRegistrationEventPublisher(String email, String name, HttpServletRequest request){
        eventPublisher.publishEvent(new CompleteRegistrationEvent(email, name, AuthenticationUtils.applicationUrl(request)));
    }


}
