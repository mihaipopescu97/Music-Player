package com.example.mplayer.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseHandler {

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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
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

    public void getUsers(List<User> users) {
        userHelper.getUsers(users);
    }

    public void getUser(final String id, final AtomicReference<User> user) {
        userHelper.getUser(id, user);
    }

    public void getUserIdFromEmail(final String email, final AtomicReference<String> userId) { userHelper.getUserIdFromEmail(email, userId);}

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

    public void getDevices(final List<Device> devices) {
        deviceHelper.getDevices(devices);
    }

    public void getUserDevices(final String userId, final List<Device> devices) {
        deviceHelper.getUserDevices(userId, devices);
    }

    public void getDevice(final String id, final AtomicReference<Device> device) {
        deviceHelper.getDevice(id, device);
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

    public void getSetups(final List<Setup> setups) {
        setupHelper.getSetups(setups);
    }

    public void getUserSetups(final String userId, final List<Setup> setups) {
        setupHelper.getUserSetups(userId, setups);
    }

    public void getSetup(final String id, final AtomicReference<Setup> setup) {
        setupHelper.getSetup(id, setup);
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

    public void getRooms(final List<Room> rooms) {
        roomHelper.getRooms(rooms);
    }

    public void getSetupRooms(final String setupId, final List<Room> rooms) {
        roomHelper.getSetupRooms(setupId, rooms);
    }

    public void getRoom(final String id, final AtomicReference<Room> room) {
        roomHelper.getRoom(id, room);
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

    public void getPlaylists(final List<Playlist> playlists) {
        playlistHelper.getPlaylists(playlists);
    }

    public void getRoomPlaylists(final String roomId, final AtomicReference<Playlist> playlists) {
        playlistHelper.getRoomPlaylist(roomId, playlists);
    }

    public void getUserPlaylists(final String userId, final List<Playlist> playlists) {
        playlistHelper.getUserPlaylist(userId, playlists);
    }

    public void getPlaylist(final String id, final AtomicReference<Playlist> playlist) {
        playlistHelper.getPlaylist(id, playlist);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getPlaylistSongsNames(final String id, final List<String> names) {
        playlistHelper.getPlaylistSongsNames(id, names);
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

    public void getSongs(List<Song> songs) {
        songHelper.getSongs(songs);
    }

    public void getSong(final String id, final AtomicReference<Song> song) {
        songHelper.getSong(id, song);
    }

    public void updateSong(final String id, Song song) {
        songHelper.updateSong(id, song);
    }

    public void deleteSong(final String id) {
       songHelper.deleteSong(id);
    }


}
