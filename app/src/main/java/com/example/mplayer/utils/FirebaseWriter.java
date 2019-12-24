package com.example.mplayer.utils;

import com.example.mplayer.entities.Device;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.entities.Song;
import com.example.mplayer.entities.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseWriter {

    private static FirebaseWriter instance = null;
    private static DatabaseReference reference;
    private FirebaseWriter() {
        reference = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseWriter getInstance() {
        if(instance == null) {
            instance = new FirebaseWriter();
        }
        return instance;
    }

    public static void addUser(User user) {
        reference.child("Users").child(user.getEmail()).setValue(user);
    }

    public static void addRoom(Room room) {
        reference.child("Room").child(room.getId()).setValue(room);
    }

    public static void addDevice(Device device) {
        reference.child("Devices").child(device.getId()).setValue(device);
    }

    public static void addPlaylist(Playlist playlist) {
        reference.child("Playlists").child(playlist.getId()).setValue(playlist);
    }

    public static void addSetup(Setup setup) {
        reference.child("Setup").child(setup.getId()).setValue(setup);
    }

    public static void addSong(Song song) {
        reference.child("Songs").child(song.getId()).setValue(song);
    }


}
