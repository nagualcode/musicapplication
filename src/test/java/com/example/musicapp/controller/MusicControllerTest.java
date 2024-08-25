package com.example.musicapp.controller;

import com.example.musicapp.model.Music;
import com.example.musicapp.repository.MusicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MusicController.class)
public class MusicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicRepository musicRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllMusic() throws Exception {
        Music music1 = new Music();
        music1.setId(1L);
        music1.setTitle("Song A");
        music1.setArtist("Artist A");

        Music music2 = new Music();
        music2.setId(2L);
        music2.setTitle("Song B");
        music2.setArtist("Artist B");

        Mockito.when(musicRepository.findAll()).thenReturn(Arrays.asList(music1, music2));

        mockMvc.perform(get("/api/music"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Song A")))
                .andExpect(jsonPath("$[0].artist", is("Artist A")))
                .andExpect(jsonPath("$[1].title", is("Song B")))
                .andExpect(jsonPath("$[1].artist", is("Artist B")));
    }

    @Test
    void testCreateMusic() throws Exception {
        Music music = new Music();
        music.setId(1L);
        music.setTitle("Song A");
        music.setArtist("Artist A");

        Mockito.when(musicRepository.save(any(Music.class))).thenReturn(music);

        mockMvc.perform(post("/api/music")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Song A\",\"artist\":\"Artist A\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Song A")))
                .andExpect(jsonPath("$.artist", is("Artist A")));
    }

    @Test
    void testGetMusicById() throws Exception {
        Music music = new Music();
        music.setId(1L);
        music.setTitle("Song A");
        music.setArtist("Artist A");

        Mockito.when(musicRepository.findById(anyLong())).thenReturn(Optional.of(music));

        mockMvc.perform(get("/api/music/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Song A")))
                .andExpect(jsonPath("$.artist", is("Artist A")));
    }

    @Test
    void testUpdateMusic() throws Exception {
        Music existingMusic = new Music();
        existingMusic.setId(1L);
        existingMusic.setTitle("Song A");
        existingMusic.setArtist("Artist A");

        Music updatedMusic = new Music();
        updatedMusic.setId(1L);
        updatedMusic.setTitle("Song B");
        updatedMusic.setArtist("Artist B");

        Mockito.when(musicRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(musicRepository.save(any(Music.class))).thenReturn(updatedMusic);

        mockMvc.perform(put("/api/music/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Song B\",\"artist\":\"Artist B\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Song B")))
                .andExpect(jsonPath("$.artist", is("Artist B")));
    }

    @Test
    void testDeleteMusic() throws Exception {
        Mockito.when(musicRepository.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/music/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(musicRepository, Mockito.times(1)).deleteById(1L);
    }
}
