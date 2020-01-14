package com.example.mplayer.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Device;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.entities.Song;
import com.example.mplayer.entities.User;
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
    private static Boolean check;

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
    public void addUser(User user) {
        userRef.child(user.getId()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "User post successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "User post failed");
                    }
                });
    }

    public List<User> getUsers() {

        final List<User> users = new ArrayList<>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return users;
    }

    public User getUser(final String id) {

        final User[] searchedUser = new User[1];
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    if (user.getId().equals(id)) {
                        searchedUser[0] = user;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedUser[0];
    }

    public void updateUser(final String id, User user) {
        userRef.child(id).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "User :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update user failed");
                    }
                });

    }

    public void deleteUser(final String id) {
        userRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "User :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete user failed");
                    }
                });
    }

    ///////////////////
    //Device crud
    ///////////////////
    public void addDevice(Device device) {
        deviceRef.child(device.getId()).setValue(device)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post device successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post device failed");
                    }
                });
    }

    public List<Device> getDevices() {

        final List<Device> devices = new ArrayList<>();

        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Device device = keyNode.getValue(Device.class);
                    devices.add(device);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return devices;
    }

    public Device getDevice(final String id) {

        final Device[] searchedDevice = new Device[1];
        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Device device = keyNode.getValue(Device.class);
                    if (device.getId().equals(id)) {
                        searchedDevice[0] = device;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedDevice[0];
    }

    public void updateDevice(final String id, Device device) {
        deviceRef.child(id).setValue(device)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Device :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update device failed");
                    }
                });

    }

    public void deleteDevice(final String id) {
        deviceRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Device :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete device failed");
                    }
                });
    }

    ///////////////////
    //Setup crud
    ///////////////////
    public void addSetup(Setup setup) {
        setupRef.child(setup.getId()).setValue(setup)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post setup successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post setup failed");
                    }
                });
    }

    public List<Setup> getSetups() {

        final List<Setup> setups = new ArrayList<>();

        setupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Setup setup = keyNode.getValue(Setup.class);
                    setups.add(setup);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return setups;
    }

    public Setup getSetup(final String id) {

        final Setup[] searchedSetup = new Setup[1];
        setupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Setup setup = keyNode.getValue(Setup.class);
                    if (setup.getId().equals(id)) {
                        searchedSetup[0] = setup;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedSetup[0];
    }

    public void updateSetup(final String id, Setup setup) {
        setupRef.child(id).setValue(setup)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Setup :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update setup failed");
                    }
                });

    }

    public void deleteSetup(final String id) {
        setupRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Setup :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete setup failed");
                    }
                });
    }

    ///////////////////
    //Room crud
    ///////////////////
    public void addRoom(Room room) {
        roomRef.child(room.getId()).setValue(room)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post room successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post room failed");
                    }
                });
    }

    public List<Room> getRooms() {

        final List<Room> rooms = new ArrayList<>();

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Room room = keyNode.getValue(Room.class);
                    rooms.add(room);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return rooms;
    }

    public Room getRoom(final String id) {

        final Room[] searchedRoom = new Room[1];
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Room room = keyNode.getValue(Room.class);
                    if (room.getId().equals(id)) {
                        searchedRoom[0] = room;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedRoom[0];
    }

    public void updateRoom(final String id, Room room) {
        roomRef.child(id).setValue(room)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Room :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update room failed");
                    }
                });

    }

    public void deleteRoom(final String id) {
        roomRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Room :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete room failed");
                    }
                });
    }

    ///////////////////
    //Playlist crud
    ///////////////////
    public void addPlaylist(Playlist playlist) {
        playlistRef.child(playlist.getId()).setValue(playlist)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post playlist successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post playlist failed");
                    }
                });
    }

    public List<Playlist> getPlaylists() {

        final List<Playlist> playlists = new ArrayList<>();

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Playlist playlist = keyNode.getValue(Playlist.class);
                    playlists.add(playlist);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return playlists;
    }

    public Playlist getPlaylist(final String id) {

        final Playlist[] searchedPlaylist = new Playlist[1];
        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Playlist playlist = keyNode.getValue(Playlist.class);
                    if (playlist.getId().equals(id)) {
                        searchedPlaylist[0] = playlist;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedPlaylist[0];
    }

    public void updatePlaylist(final String id, Playlist playlist) {
        playlistRef.child(id).setValue(playlist)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Playlist :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update playlist failed");
                    }
                });

    }

    public void deletePlaylist(final String id) {
        playlistRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Playlist :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete playlist failed");
                    }
                });
    }

    //TODO test this
    public void setPlaylistSongs(final String id, final List<String> songs) {
        playlistRef.child(id).child("song").setValue(songs);
    }

    ///////////////////
    //Song crud
    ///////////////////
    public void addSong(Song song) {
        songRef.child(song.getId()).setValue(song)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post song successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post song failed");
                    }
                });
    }

    public List<Song> getSongs() {

        final List<Song> songs = new ArrayList<>();

        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Song song = keyNode.getValue(Song.class);
                    songs.add(song);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return songs;
    }

    public Song getSong(final String id) {

        final Song[] searchedSong = new Song[1];
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Song song = keyNode.getValue(Song.class);
                    if (song.getId().equals(id)) {
                        searchedSong[0] = song;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedSong[0];
    }

    public void updateSong(final String id, Song song) {
        songRef.child(id).setValue(song)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Song :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update song failed");
                    }
                });

    }

    public void deleteSong(final String id) {
        songRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Song :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete song failed");
                    }
                });
    }

    ///////////////////
    //Other
    ///////////////////
    public boolean checkChild(String baseReff, final String id) {
        check = false;
        ref.child(baseReff).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    check = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, String.valueOf(databaseError));
            }
        });

        return check;
    }

}
