package hcmus.zingmp3.core.service.artist;

import hcmus.zingmp3.common.domain.model.Artist;
import hcmus.zingmp3.common.events.ArtistCreateEvent;
import hcmus.zingmp3.common.events.ArtistDeleteEvent;
import hcmus.zingmp3.common.events.ArtistUpdateEvent;
import hcmus.zingmp3.core.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistCommandServiceImpl implements ArtistCommandService {
    private final EventService eventService;

    @Override
    public void create(
            final Artist object
    ) {
        ArtistCreateEvent event = new ArtistCreateEvent(object);
        eventService.create(event);
    }

    @Override
    public void update(
            final Artist object
    ) {
        ArtistUpdateEvent event = new ArtistUpdateEvent(object);
        eventService.create(event);
    }

    @Override
    public void delete(
            final Artist object
    ) {
        ArtistDeleteEvent event = new ArtistDeleteEvent(object);
        eventService.create(event);
    }
}
