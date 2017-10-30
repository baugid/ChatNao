Updated 7-30-2002

---------------
INTRODUCTION:
---------------
AlicebotFlash is all you need to add a Flash client to your Alicebot ProgramD server. If you are a Flash Guru, this file contains the source movie clip which can be easily added to (or modified for use with) your own Flash movie to add chat functionality. You can customize it as much as you want. And even if you're not a Flash Guru, you can still add the Alicebot Flash client to your web site. The AlicebotFlash client is actually more efficient and faster than the HTML client since there is no browser page reload for each exchange.

The client SWF file is as stripped-down as can be (it's only 2K!). There are no embedded fonts, no sounds, and almost no graphics. It's purpose is to allow you, the botmaster, to change it as much as you like.

Author: 
   Christopher Fahey
   askrom@graphpaper.com
   http://www.graphpaper.com

Questions and comments are welcome. Thanks!



---------------
REQUIREMENTS:
---------------
AlicebotFlash is was made in Flash 5, and the client interface requires the Flash 5 or Flash 6 player/plug in. It will not work with the stand-alone Flash player - it will only work in browsers.

AlicebotFlash is meant to work with the ProgramD 4.1.4 or 4.1.5 server, available at http://www.alicebot.org

NOTE: You have to run the AlicebotFlash.swf file on the same physical server as the Program D Server (the :2001 box) or else it won't work. This is a Flash security constraint - it cannot access files/data from another physical server.



------------------------------------------------------------
SETTING UP YOUR PRoGRAMD SERVER TO WORK WITH ALICEBOTFLASH CLIENT:
------------------------------------------------------------
ProgramD 4.1.4 users:
  The ProgramD 4.1.4 server works well with AlicebotFlash these days, but some versions of Program D 4.1.4 have an incorrect version of the flash template file (located at "{ProgramD}\templates\flash\chat.flash"). This file should be replaced with the "chat.flash_414" file located in this .zip file (in the AlicebotFlash "\server\" folder). Remember to rename the file back to "chat.flash". When you replace this file, you must restart ProgramD for the change to take effect.

ProgramD 4.1.5 users:
  The ProgramD 4.1.5 server works well with AlicebotFlash these days, but some versions of Program D 4.1.5 have an incorrect version of the flash template file (located at "{ProgramD}\templates\flash\chat.flash"). This file should be replaced with the "chat.flash_415" file located in this .zip file (in the AlicebotFlash "\server\" folder). Remember to rename the file back to "chat.flash". When you replace this file, you must restart ProgramD for the change to take effect.


------------------------
TEST YOUR CONFIGURATION:
------------------------

First, run Program D.

If the following link does not work, you are totally lost and it's not my fault:
     [1] ---> http://localhost:2001/CHAT
  Subscribe to the Alicebot mailing list and learn how to setup your bot server correctly there. Once you have that working, then try using this flash interface.

If the following link does not work, you may have not correctly configured your bot server as per the directions above:
     [2] ---> http://localhost:2001/CHAT?flash=true
  This link should result in something like the following:
     &bot_name=Alice &alice_in=CONNECT &alice_out=Hello user and thanks for connecting. &textLoaded=1
If you see something like this, then you need to replace the ProgramD chat.flash file with the correct one in this .zip file:
     &/:alice_in=CONNECT  &/:alice_out=Hello user and thanks for connecting. &textloaded=OK 


If both of the above links ([1] and [2]) work, then simply run the AlicebotFlash.html file. It should show you the flash client and you're ready for chatting.


----------------
USING THE FILES:
----------------
Except for the chat.flash file, NONE of these files need to go in your ProgramD directory. They can go anywhere you want (I put them in the same directory as the rest of the files for the web site on which my bot resides). The "client" files are intended to go on your web server.


---------------
INGREDIENTS:
---------------
This AlicebotFlash distribution consists of three parts (in three directories):

--> CLIENT: 
          [-] AlicebotFlash.swf - The actual flash movie. 
               Note that this file won't work in the Flash 
               standalone player: it must be displayed in 
               a web browser. This file is uneditable - to
               make any changes to the appearance 
               (fonts, colors, layout, etc) or functionality of 
               this, you need to edit the original souce flash 
               file (see SOURCE, below).
          [-] AlicebotFlash.html - Simple HTML file 
               containing <OBJECT> tag embedding the Flash 
               movie. Copy this code into your web page to 
               embed the flash movie in your site.
          [-] AlicebotFlash.ini - This file is read at startup 
               by AlicebotFlash.swf to set up certain 
               variables.
                  &botDisplayName=BOT:
                       (This is the bot's name as displayed in 
                       the Flash interface. Generally, you will
                       want to make this the same as your 
                       bot's name, but in case you don't you 
                       can change it here)
                  &clientDisplayName=USER:
                       (This is the prompt that appears before 
                       the user input field)
                  &botUrl=http://www.yourdomain.com:2001/CHAT?flash=true
                       (This is the URL of your Alicebot Server. 
                       The "?flash=true" tells the server to 
                       use the flash template (currently 
                       chat.flash) to format the output. 
                       This value is not hard-coded into the SWF 
                       so that you can change it without having 
                       to rebuild the SWF file.)
                  &connectText=CONNECT
                       (This is the first text message sent to the 
                       bot the when the user first connects to it. 
                       "CONNECT" is the AIML standard, but if you 
                       want to change it you certainly can.)
                  &hideConnectText=Y
                       (if you want to hide the initial connect 
                       message, set to "Y"; "N" to display it.)
                  &iniLoaded=1
                       (don't change this - it tells flash that 
                       the ini file is done loading)


--> SERVER:
          [-] chat.flash -  This is the TEMPLATE file 
               that the Program D Server uses to format the 
               Alicebot output for use by a flash client. This file 
               will be part of the Program D Server distribution,
               and as such you should not change it. At this time,
               it does not extract HTML tags from the bot's 
               output, but this may be changed in future builds.

--> SOURCE:
          [-] AlicebotFlash.fla - This is the Flash 5 source file. 
               The library contains a movie clip called "Chat_Interface", 
               an instance of which is placed on the main timeline. 
               This movie clip contains all of the chat functionality, 
               and it can be inserted into other Flash 5 movies with 
               almost no adjustment. The movie should be self 
               explanatory, but it essentially works like this:
                    1) load the ini file.
                    2) CONNECT to the server
                    3) Display the interface and wait for user input.
                    4) When user input has occurred, send 
                        the query to the server's URL. Disable
                        the user input field until the bot's response 
                        has fully loaded (this prevents the user from
                        typing their next response while the bot is 
                        still thinking)
                    5) Repeat from #3.


--------------- THANKS! -------------------


