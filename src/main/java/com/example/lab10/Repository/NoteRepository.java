package com.example.lab10.Repository;

import com.example.lab10.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findAllByUserId(Integer userId);
    Optional<Note> findByIdAndUserId(Integer id, Integer userId);
}

