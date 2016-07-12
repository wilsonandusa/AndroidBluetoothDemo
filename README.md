# AndroidBluetoothDemo
##This a a stand-alone app that only contains the bluetooth connection function from my main application. Right now the problem is:

####When I press the _connect_ button, the app will initialize the service but it will also freeze the animation on the main thread, even if I explicitly put it in a separate thread. Based on the logcat I think the _sock.connect()_ funtion in the _BluetoothManager.java_ file is waiting for the callback. How should I implement to make sure everything runs in the background? Any help are welcome! Thanks
