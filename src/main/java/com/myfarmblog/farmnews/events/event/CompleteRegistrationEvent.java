package com.myfarmblog.farmnews.events.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter

public class CompleteRegistrationEvent extends ApplicationEvent {
    private String name;
    private String email;
    private String applicationUrl;

    public CompleteRegistrationEvent(String name, String email, String applicationUrl) {
        super(email);
        this.name = name;
        this.email = email;
        this.applicationUrl = applicationUrl;
    }
}
