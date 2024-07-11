package com.rocketseat.planner.trip;

import com.rocketseat.planner.activity.*;
import com.rocketseat.planner.link.LinkCreateResponse;
import com.rocketseat.planner.link.LinkData;
import com.rocketseat.planner.link.LinkRequestPayload;
import com.rocketseat.planner.link.LinkService;
import com.rocketseat.planner.participant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private TripRepository repository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private LinkService linkService;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayLoad payload) {
        Trip newTrip = new Trip(payload);

        this.repository.save(newTrip);
        this.participantService.registroParticipantes(payload.emails_to_invited(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    //TRIPS
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {

        Optional<Trip> trip = this.repository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayLoad payLoad) {

        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawtrip = trip.get();
            rawtrip.setEndsAt(LocalDateTime.parse(payLoad.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawtrip.setStartsAt(LocalDateTime.parse(payLoad.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawtrip.setDestination(payLoad.destination());

            this.repository.save(rawtrip);
            return ResponseEntity.ok(rawtrip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {

        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawtrip = trip.get();
            rawtrip.setIsConfirmed(true);

            this.repository.save(rawtrip);
            this.participantService.triggerConfirmationParticipantes(id);
            return ResponseEntity.ok(rawtrip);
        }

        return ResponseEntity.notFound().build();
    }

    //ACTIVITIES
    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {

        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawtrip = trip.get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawtrip);

            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok(activityDataList);
    }

    //PARTICIPANTS
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {

        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawtrip = trip.get();

            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(), rawtrip);

            if (rawtrip.getIsConfirmed()) this.participantService.triggerConfirmationParticipante(payload.email());

            return ResponseEntity.ok(participantResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantData> participants = this.participantService.getAllParticipantsFromEvent(id);

        return ResponseEntity.ok(participants);
    }

    //LINKS
    @PostMapping("/{id}/links")
    public ResponseEntity<LinkCreateResponse> registerlink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {

        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawtrip = trip.get();

            LinkCreateResponse linkResponse = this.linkService.registerlink(payload, rawtrip);

            return ResponseEntity.ok(linkResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
        List<LinkData> linkDataList = this.linkService.getAllLinksFromTrip(id);

        return ResponseEntity.ok(linkDataList);
    }

}
