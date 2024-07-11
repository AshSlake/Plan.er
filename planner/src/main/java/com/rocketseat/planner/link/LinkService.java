package com.rocketseat.planner.link;

import com.rocketseat.planner.activity.ActivityData;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository repository;

    public LinkCreateResponse registerlink(LinkRequestPayload payload, Trip trip) {

        Link newlink = new Link(payload.title(), payload.url(), trip);

        this.repository.save(newlink);

        return new LinkCreateResponse(newlink.getId());
    }

    public List<LinkData> getAllLinksFromTrip(UUID tripId){
        return this.repository.findByTripId(tripId).stream().map(link -> new LinkData(link.getId(),link.getTitle(),link.getUrl())).toList();
    }
}
