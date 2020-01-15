package com.example.mplayer.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Device;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.entities.Song;
import com.example.mplayer.entities.User;
import com.example.mplayer.utils.helpers.DeviceHelper;
import com.example.mplayer.utils.helpers.PlaylistHelper;
import com.example.mplayer.utils.helpers.RoomHelper;
import com.example.mplayer.utils.helpers.SetupHelper;
import com.example.mplayer.utils.helpers.SongHelper;
import com.example.mplayer.utils.helpers.UserHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHandler {

    private static final String TAG = "FirebaseHandler";

    private static DatabaseReference userRef;
    private static DatabaseReference deviceRef;
    private static DatabaseReference setupRef;
    private static DatabaseReference roomRef;
    private static DatabaseReference playlistRef;
    private static DatabaseReference songRef;

    private static UserHelper userHelper;
    private static DeviceHelper deviceHelper;
    private static SetupHelper setupHelper;
    private static RoomHelper roomHelper;
    private static PlaylistHelper playlistHelper;
    private static SongHelper songHelper;

    private static FirebaseHandler instance = null;
    private static DatabaseReference ref;


    private FirebaseHandler() {
        //Set the instance
        ref = FirebaseDatabase.getInstance().getReference();

        //Set the references
        userRef = ref.child("Users");
        deviceRef = ref.child("Devices");
        setupRef = ref.child("Setups");
        roomRef = ref.child("Rooms");
        playlistRef = ref.child("Playlist");
        songRef = ref.child("Songs");

        //Set the helpers
        userHelper = UserHelper.getInstance(userRef);
        deviceHelper = DeviceHelper.getInstance(deviceRef);
        setupHelper = SetupHelper.getInstance(setupRef);
        roomHelper = RoomHelper.getInstance(roomRef);
        playlistHelper = PlaylistHelper.getInstance(playlistRef);
        songHelper = SongHelper.getInstance(songRef);


    }

    public static synchronized FirebaseHandler getInstance() {
        if (instance == null) {
            instance = new FirebaseHandler();
        }
        return instance;
    }

    ////////////////////
    //User crud
    ///////////////////
    public void addUser(final User user) {
        userHelper.addUser(user);
    }

    public List<User> getUsers() {
        return userHelper.getUsers();
    }

    public User getUser(final String id) {
        return userHelper.getUser(id);
    }

    public void updateUser(final String id, User user) {
        userHelper.updateUser(id, user);
    }

    public void deleteUser(final String id) {
       userHelper.deleteUser(id);
    }

    ///////////////////
    //Device crud
    ///////////////////
    public void addDevice(final Device device) {
       deviceHelper.addDevice(device);
    }

    public List<Device> getDevices() {
        return deviceHelper.getDevices();
    }

    public List<Device> getUserDevices(final String userId) {
        return deviceHelper.getUserDevices(userId);
    }

    public Device getDevice(final String id) {
        return deviceHelper.getDevice(id);
    }

    public void updateDevice(final String id, Device device) {
       deviceHelper.updateDevice(id, device);
    }

    public void deleteDevice(final String id) {
       deviceHelper.deleteDevice(id);
    }

    ///////////////////
    //Setup crud
    ///////////////////
    public void addSetup(final Setup setup) {
        setupHelper.addSetup(setup);
    }

    public List<Setup> getSetups() {
        return setupHelper.getSetups();
    }

    public List<Setup> getDeviceSetups(final String deviceId) {
        return setupHelper.getDeviceSetups(deviceId);
    }

    public Setup getSetup(final String id) {
        return setupHelper.getSetup(id);
    }

    public void updateSetup(final String id, Setup setup) {
        setupHelper.updateSetup(id, setup);
    }

    public void deleteSetup(final String id) {
      setupHelper.deleteSetup(id);
    }

    ///////////////////
    //Room crud
    ///////////////////
    public void addRoom(final Room room) {
       roomHelper.addRoom(room);
    }

    public List<Room> getRooms() {
        return  roomHelper.getRooms();
    }

    public List<Room> getSetupRooms(final String setupId) {
        return roomHelper.getSetupRooms(setupId);
    }

    public Room getRoom(final String id) {
        return roomHelper.getRoom(id);
    }

    public void updateRoom(final String id, Room room) {
        roomHelper.updateRoom(id, room);
    }

    public void deleteRoom(final String id) {
        roomHelper.deleteRoom(id);
    }

    ///////////////////
    //Playlist crud
    ///////////////////
    public void addPlaylist(Playlist playlist) {
       playlistHelper.addPlaylist(playlist);
    }

    public List<Playlist> getPlaylists() {
        return playlistHelper.getPlaylists();
    }

    public List<Playlist> getUserPlaylists(final String userId) {
        return playlistHelper.getUserPlaylist(userId);
    }

    public Playlist getPlaylist(final String id) {
        return playlistHelper.getPlaylist(id);
    }

    public void updatePlaylist(final String id, Playlist playlist) {
        playlistHelper.updatePlaylist(id, playlist);
    }

    public void deletePlaylist(final String id) {
       playlistHelper.deletePlaylist(id);
    }

    ///////////////////
    //Song crud
    ///////////////////
    public void addSong(Song song) {
       songHelper.addSong(song);
    }

    public List<Song> getSongs() {
        return songHelper.getSongs();
    }

    public Song getSong(final String id) {
        return songHelper.getSong(id);
    }

    public void updateSong(final String id, Song song) {
        songHelper.updateSong(id, song);
    }

    public void deleteSong(final String id) {
       songHelper.deleteSong(id);
    }


}
