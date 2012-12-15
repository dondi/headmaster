package edu.lmu.cs.headmaster.ws.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

public class EventTest {

    @Test
    public void fieldsSetBySettersCanBeRead() {
        
        Event event = new Event();
        
        Long id = 9000L;
        DateTime dateTime = new DateTime(0);
        String title = "Testing Event";
        String description = "An event created for testing.";
        
        event.setId(id);
        event.setDateTime(dateTime);
        event.setTitle(title);
        event.setDescription(description);
        
        assertEquals(event.getId(), id);
        assertEquals(event.getDateTime(), dateTime);
        assertEquals(event.getTitle(), title);
        assertEquals(event.getDescription(), description);
        
    }
    
}
