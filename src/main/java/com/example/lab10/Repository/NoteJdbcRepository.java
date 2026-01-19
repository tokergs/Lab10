package com.example.lab10.Repository;

import com.example.lab10.dto.note.NoteResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NoteJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public NoteJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Prepared-statement raw SQL query (requirement from Lab 11-12).
     * Returns only notes owned by the given user.
     */
    public List<NoteResponseDto> searchByTitle(Integer userId, String q) {
        String like = "%" + (q == null ? "" : q) + "%";
        String sql = "SELECT id, title, content, created_at FROM notes WHERE user_id = ? AND title LIKE ? ORDER BY id DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs), userId, like);
    }

    private NoteResponseDto mapRow(ResultSet rs) throws SQLException {
        NoteResponseDto dto = new NoteResponseDto();
        dto.id = rs.getInt("id");
        dto.title = rs.getString("title");
        dto.content = rs.getString("content");
        dto.createdAt = rs.getString("created_at");
        return dto;
    }
}

