package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayLoad payload) {
        Trip newTrip = new Trip(payload);

        this.repository.save(newTrip);
        this.participantService.registroParticipantes(payload.emails_to_invited(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {

        Optional<Trip> trip = this.repository.findById(id);

        return  trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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

        return  ResponseEntity.notFound().build();
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

        return  ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant (@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {

        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawtrip = trip.get();

            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(),rawtrip);

            if(rawtrip.getIsConfirmed()) this.participantService.triggerConfirmationParticipante(payload.email());

            return ResponseEntity.ok(participantResponse);
        }

        return  ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantData> participants = this.participantService.getAllParticipantsFromEvent(id);

        return ResponseEntity.ok(participants);
    }
}
