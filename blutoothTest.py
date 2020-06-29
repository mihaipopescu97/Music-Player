from bluetooth import *
import threading
import os
from firebase import firebase
import RPi.GPIO as GPIO
import subprocess


server_sock = None
receivedData = None
userId = None
setupId = None
process = None
songs = ['music.mp3']
actSong = None
paired = False
pins = []

rooms = []
songs = []
actSong = None
EXTENSION = ".mp3"
index = None
uuid = "45b5fe7f-19a9-44a2-b29c-7693d8e6d39f"
deviceID = "45b5fe7f-19a9-44a2-b29c-7693d8e6d39f"
paired = False

class Device:
	def __init__(self, id, userId):
		self.id = id
		self.userId = userId

class Setup:
	def __init__(self, id, rooms, userId):
		self.id = id
		self.rooms = rooms
		self.userId = userId

class Room:
	def __init__(self, id, playlistId):
		self.id = id
		self.playlistId = playlistId

class Playlist:
	def __init__(self, id, songs):
		self.id = id
		self.songs = songs

firebase = firebase.FirebaseApplication('https://musicplayer-9fb0b.firebaseio.com/', None)

# Initiate firebase data
def initData(user, setupId):
	global deviceID
	global firebase
	global rooms
	global songs
	
	deviceJson = firebase.get('/Devices/' + deviceID, '')
	print deviceJson
	device = Device(deviceJson["id"], deviceJson["userId"])
	if device.userId == user:	
		setupJson = firebase.get('/Setups/' + setupId, '')
		print setupJson
		setup = Setup(setupJson["id"], setupJson["rooms"], setupJson["userId"])
		roomJson = firebase.get('/Rooms/' + setup.rooms[0], '')
		print roomJson
		room = Room(roomJson["id"], roomJson["playlistId"])


		for i in range(0, len(setup.rooms)):
			roomsJson = firebase.get('/Rooms/' + setup.rooms[i], '')
			auxRoom = Room(roomsJson["id"], roomsJson["playlistId"])
			rooms.append(auxRoom)

		playlistJson = firebase.get('/Playlist/' + room.playlistId, '')
		playlist = Playlist(playlistJson["id"], playlistJson["songs"])

		for j in range(0, len(playlist.songs)):
			songs.append(playlist.songs[j] + EXTENSION)

# Handle Bluetooth Data
def handleData(data):
	global userId
	global setupId
	global actSong
	global paired
	if data[0] == "data":
		userId = data[1]
		print userId
		setupId = data[2]
		print setupId
		paired = True
	elif data[0] == "play":
		if process == None:
				actSong = songs[0]
				index = 0
				newTrack(actSong)

		print process
		if data[1] == "true":	
			startTrack()
		else:
			pauseTrack()
	elif data[0] == "sound":
		# like 0.x
		songVolume = float(data[1]) * 10000
		print "sound changed to %s" % songVolume
		changeVolume(songVolume)
	elif data[0] == "progress":
		#like xxx.xx
		print "progress changed to %s" % data[1]
		songProg = float(data[1]) / 10
		changeProgress(songProg, actSong)
	elif data[0] == "change":
		if data[1] == "next":
			print "next"
			index += 1
			if index == len(songs):
				index = 0
			actSong = songs[index]
			newTrack(actSong)
		else:
			print "prev"
			index -= 1
			if index == -1:
				index = songs[len(songs) -1]
			actSong = songs[index]
			newTrack(actSong)


# Bluetooth initialization of server
def init_server():
	global uuid
	global server_sock
	os.system('sudo hciconfig hci0 piscan')
	os.system('sudo sdptool add SP')
	server_sock = BluetoothSocket( RFCOMM )
	server_sock.bind(("", 5))
	server_sock.listen(5)
	port = server_sock.getsockname()[1]
	advertise_service( server_sock, "AquaPiServer",
    	               service_id = uuid,
        	           service_classes = [ uuid, SERIAL_PORT_CLASS ],
            	       profiles = [ SERIAL_PORT_PROFILE ], 
#               	    protocols = [ OBEX_UUID ] 
                    	)
	print("Waiting for connection on RFCOMM channel %d" % port)

# Read bluetooth data
def receive_commands():
	global receivedData
	global server_sock
	while True:
		client_sock, client_info = server_sock.accept()
		print "Accepted connection from ", client_info

		try:
			while True:
				receivedData = client_sock.recv(1024)
				if len(receivedData) == 0:
					break
			
		except IOError:
			pass

		except KeyboardInterrupt:

			print "disconnected"

			client_sock.close()
			server_sock.close()
			print "all done"
			break

# Change volume of the player
def changeVolume(volume):
	# os.system('pacmd set-sink-volume 0 0x2500')
	print "Changed volume to %s" % volume
	# os.system('pacmd set-sink-volume 0 0x%s' % volume)
	os.system('sudo amixer set PCM -- -%s' % volume)

# Change progress of the player
def changeProgress(progress, currentSong):
	#progress like 20.0
	global process
	print "canhaged progress to %s" % progress
	os.system('sudo kill %s' % process.pid)
	proc = subprocess.Popen(['cvlc', '--start-time=%s' % progress, currentSong])
	process = proc

# Change current track
def startTrack():
	global process
	print "Start track"
	os.system('sudo kill -SIGCONT %d' % process.pid)

# Pause current track
def pauseTrack():
	global process
	print "Pause track"
	os.system('sudo kill -SIGSTOP %d' % process.pid)

# Start new process
def newTrack(songName):
	global process
	print "New track %s" % songName
	if process == None:
		process = subprocess.Popen(['cvlc', songName])
	else: 
		os.system('sudo kill %s' % process.pid)
		proc = subprocess.Popen(['cvlc', songName])
		process = proc

def setPins():
	global rooms
	global pins

	print len(rooms)
	k = 9
	for i in range(0, len(rooms) * 2):
		k += 1
		pins.append(k)
		GPIO.setup(k, GPIO.IN)
		print "pin for %s set" % k


if __name__ == '__main__':
	global paired
	GPIO.setmode(GPIO.BOARD)
	reads = [0, 0]
	counter = 0
	speakers = [False, False]
	init_server()
	k = 9

	try:
		recvCommandsThread = threading.Thread(target = receive_commands)
		recvCommandsThread.start()
		while True:
			if receivedData:
				print "received [%s]" % receivedData
				auxData = receivedData.split(':')
				handleData(auxData)
				setPins()
				
			if paired:
				read = [0, 0]
				for i in range(0, len(rooms) * 2): 
					read[i] = GPIO.input(10 + i)
					reads[i] += read[i]
				counter += 1

				if (counter == 20000):
					
					if reads[0] < 20000 and speakers[0] == False:
						os.system('amixer cset numid=3 1')
						speakers[0] = True
						# speakers[1] = False
						if process == None:
							actSong = songs[0]
							index = 0
							newTrack(actSong)
						startTrack()
						print 'Presence in room 1'
						#do audio
					elif reads[1] < 20000 and speakers[0] == True:
						speakers[0] = False
						# speakers[1] = False
						if process == None:
							actSong = songs[0]
							index = 0
							newTrack(actSong)
						pauseTrack()
						print 'Left room 1'
					# elif reads[2]  < 20000 and speakers[1] == False:
					# os.system('amixer cset numid=3 2')
					# 	speakers[0] = False
					# 	speakers[1] = True
					# 	startTrack(process.pid)
					# 	print 'Presence in room 2'
					# elif reads[3]  < 20000 and speakers[1] == True:
					# 	print reads[3]
					# 	speakers[0] = False
					# 	speakers[1] = False
					# 	pauseTrack(process.pid)
					# 	print 'Left room 2'

					for i in range(0, len(reads)):
						reads[i] = 0
					counter = 0
			receivedData = None
	except KeyboardInterrupt:
		recvCommandsThread.join()
		print "disconnected"

		client_sock.close()
		server_sock.close()
		print "all done"


