package edu.lmu.cs.headmaster.ws.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

public class EventTest {

    @Test
    public void fieldsSetBySettersCanBeRead() {
        
        Event event = new Event();
        
        final Long ID = 9000L;
        DateTime dateTime = new DateTime(0);
        final String TITLE = "Testing Event";
        final String DESCRIPTION = "An event created for testing.";
        
        event.setId(ID);
        event.setDateTime(dateTime);
        event.setTitle(TITLE);
        event.setDescription(DESCRIPTION);
        
        assertEquals(event.getId(), ID);
        assertEquals(event.getDateTime(), dateTime);
        assertEquals(event.getTitle(), TITLE);
        assertEquals(event.getDescription(), DESCRIPTION);
        
    }
    
}
