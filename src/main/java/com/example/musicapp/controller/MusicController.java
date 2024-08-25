package com.example.musicapp.controller;

import com.example.musicapp.model.Music;
import com.example.musicapp.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicRepository musicRepository;

    @GetMapping
    public List<Music> getAllMusic() {
        return musicRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Music> createMusic(@RequestBody Music music) {
        Music savedMusic = musicRepository.save(music);
        return new ResponseEntity<>(savedMusic, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Music> getMusicById(@PathVariable Long id) {
        Optional<Music> music = musicRepository.findById(id);
        return music.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Music> updateMusic(@PathVariable Long id, @RequestBody Music music) {
        if (!musicRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        music.setId(id);
        Music updatedMusic = musicRepository.save(music);
        return ResponseEntity.ok(updatedMusic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMusic(@PathVariable Long id) {
        if (!musicRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        musicRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
