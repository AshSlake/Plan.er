package com.rocketseat.planner.participant;

import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantsRepository repository;

    public void registroParticipantes(List<String> participantesConvidados, Trip trip) {
        List <Participant> participants = participantesConvidados.stream().map(email -> new Participant(email,trip)).toList();

        this.repository.saveAll(participants);

        System.out.println("id do participante " + participants.get(0).getId());
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip){

        Participant newparticipant = new Participant(email,trip);
        this.repository.save(newparticipant);

        return new ParticipantCreateResponse(newparticipant.getId());
    }

    public void triggerConfirmationParticipantes(UUID tripId) {}

    public void triggerConfirmationParticipante(String email) {}

    public List<ParticipantData> getAllParticipantsFromEvent(UUID tripId) {

        return this.repository.findByTripId(tripId).stream().map(participant -> new ParticipantData(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsConfirmed())).toList();
    }
}
