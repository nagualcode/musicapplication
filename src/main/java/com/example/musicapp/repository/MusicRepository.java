package com.example.musicapp.repository;

import com.example.musicapp.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
