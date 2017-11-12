## ChatNao
Talk to your NAO!

ChatNao is a Programm based on IBM's watson and the AIML Chatbot Charliebot.

#Simple Instructions
[1] Change the IP in the start.bat to the IP of your NAO
[2] Start the start.bat in cmd
[3] 2 Windows will open. Leave the new window open (in background)
[4] The first window will prompt you to start the recording by pressing enter
[5] Talk to your robot. The recording will last 3 seconds.
[6] Wait a bit. This step can take a while, depending on Computer power and Internet speed (normally about 5-20 seconds)
[7] Your NAO should give you an answer.

# Detailed instructions:

To use this Programm, simply download it from the Releases tab, change the IP in the start.bat to the IP of your NAO and start it.
The start.bat should open 2 Windows.The chatbot will boot up in the first. The other window is the important one for you.
This Window will connect to your NAO. 
Once It has connected succesfully, you will be requested to press Enter to start a recording.
Once you start the recording, the NAO will record your voice for 3 seconds. 
After that, the programm will say "Recording stopped".
The Audio will now be converted to FLAC and send to Watson. The answer will then be put into the chatbot.
The answer of the chatbot is then given to the NAO and he will speak.

# Credits
Main Java Code: Kasukoi (Sebastian Grohs)
With support from: Gideon Baur, Leona Maehler, Max Krass, Tilman Hoffbauer

Charliebot by thammegowda
Watson by IBM

Used Java Libraries:
- Watson Developer Cloud Java SDK 4.0.0
- Commons IO 2.4
- OkIO 1.13.0
- OkHTTP 3.9.0
- Java FLAC Encoder 0.3.2
- Java NAOqi SDK 2.4.3.28
Thanks for your awesome Libraries!!!
