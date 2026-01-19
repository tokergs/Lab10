package com.example.lab10.service;

import com.example.lab10.Repository.NoteJdbcRepository;
import com.example.lab10.Repository.NoteRepository;
import com.example.lab10.dto.note.NoteRequestDto;
import com.example.lab10.dto.note.NoteResponseDto;
import com.example.lab10.entity.Note;
import com.example.lab10.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteJdbcRepository noteJdbcRepository;

    public NoteService(NoteRepository noteRepository, NoteJdbcRepository noteJdbcRepository) {
        this.noteRepository = noteRepository;
        this.noteJdbcRepository = noteJdbcRepository;
    }

    public List<NoteResponseDto> listMyNotes(Integer userId) {
        return noteRepository.findAllByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public NoteResponseDto getMyNote(Integer userId, Integer noteId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        return toDto(note);
    }

    public NoteResponseDto createMyNote(Integer userId, NoteRequestDto req) {
        Note note = new Note();
        note.setUserId(userId);
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());
        note.setCreatedAt(java.time.Instant.now().toString());
        return toDto(noteRepository.save(note));
    }

    public NoteResponseDto updateMyNote(Integer userId, Integer noteId, NoteRequestDto req) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());
        return toDto(noteRepository.save(note));
    }

    public void deleteMyNote(Integer userId, Integer noteId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        noteRepository.delete(note);
    }

    public List<NoteResponseDto> searchMyNotesByTitle(Integer userId, String q) {
        return noteJdbcRepository.searchByTitle(userId, q);
    }

    private NoteResponseDto toDto(Note note) {
        NoteResponseDto dto = new NoteResponseDto();
        dto.id = note.getId();
        dto.title = note.getTitle();
        dto.content = note.getContent();
        dto.createdAt = note.getCreatedAt();
        return dto;
    }
}

