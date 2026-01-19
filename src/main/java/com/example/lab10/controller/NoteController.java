package com.example.lab10.controller;

import com.example.lab10.dto.note.NoteRequestDto;
import com.example.lab10.dto.note.NoteResponseDto;
import com.example.lab10.security.UserPrincipal;
import com.example.lab10.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteResponseDto>> listMyNotes(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(noteService.listMyNotes(principal.getId()));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteResponseDto> getMyNote(@AuthenticationPrincipal UserPrincipal principal,
                                                     @PathVariable Integer id) {
        return ResponseEntity.ok(noteService.getMyNote(principal.getId(), id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteResponseDto> createMyNote(@AuthenticationPrincipal UserPrincipal principal,
                                                        @Valid @RequestBody NoteRequestDto req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createMyNote(principal.getId(), req));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteResponseDto> updateMyNote(@AuthenticationPrincipal UserPrincipal principal,
                                                        @PathVariable Integer id,
                                                        @Valid @RequestBody NoteRequestDto req) {
        return ResponseEntity.ok(noteService.updateMyNote(principal.getId(), id, req));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMyNote(@AuthenticationPrincipal UserPrincipal principal,
                                             @PathVariable Integer id) {
        noteService.deleteMyNote(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Raw SQL (prepared statement) search endpoint.
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteResponseDto>> searchMyNotes(@AuthenticationPrincipal UserPrincipal principal,
                                                               @RequestParam(name = "q", required = false) String q) {
        return ResponseEntity.ok(noteService.searchMyNotesByTitle(principal.getId(), q));
    }
}

