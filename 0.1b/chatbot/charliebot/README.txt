Charliebot v4.1.8
Maintained by Aaron Rosenzweig -> aaron@cocoanutstech.com
Charlie is available at the following two places:
1) www.cocoanutstech.com
2) sourceforge.net

Quick Start
=====================
We focus on Mac OS X at Cocoanuts. Technically, Charliebot will run on any Java 1.3 VM but we haven't tested any scripts on other operating systems. You will likely need to modify the scripts for use on a non-unix based OS.

From Mac OS X's Terminal you can startup Charliebot by typing the following:
./server.sh   <<then press return>>

In a few seconds Charliebot will start up and be running. There are currently three personalities installed on the Charliebot, they are:

Charlie
Anna
Alice

You can talk with any of them from the Terminal window but it would be better to open up a Web Browser to the following addresses:

Charlie -> http://localhost:2001/?botid=Charlie&template=chat
Anna -> http://localhost:2001/?botid=Alice&template=alice
Alice -> http://localhost:2001/?botid=Anna&template=anna

You can also talk with them in the Terminal window. For a list of Terminal commands you may type the following on the command line:
/help

For example, to talk to Alice you would type:
/talkto Alice

To go back to Charlie, type:
/talkto Charlie

What is the purpose of the Charlie project?
======================
To create a robot which pretends to be a man. His name is Charlie. He was created to support the www.cocoanutstech.com Web site. This is his bio:
* 58 years old
* Retired, studying biology at Emory (borrowed from Anna team)
* Gray hair
* Brown eyes
* Wife's name is Jules
* Hates Microsoft Windows
* Likes to discuss computers and the Internet.


What is the history of the Charlie project?
======================
Charlie directly stems from the Anna project. In turn, Anna directly stems from the Program D project. Furthermore, Program D stems from work done at the ALICE Foundation.

In short, we wanted a robot to support our cocoanutstech.com domain. We felt that the Program D code would best suit our needs. Unfortunately, the Program D code had disappeared from the net and we had to snoop around to rescue it. The Anna team made their AIML code (robot logic) available but not the source code of Program D which they used. We had to decompile the Anna jar files to recreate some source code which we could modify.

Program D version 4.1.5 requires a Java 1.4 VM. Because we need it to run on Mac OS X, which only has a Java 1.3 VM, we needed to modify the code. This wasn't so hard but required some minor magic done to the source code. We are calling the current code "Charliebot v4.1.8"


How to modify Charlie's logic
======================
You can modify Charlie's logic by editing the AIML files located in "brains/charlie"; however, an easier method would be to go into the "Project Builder AIML editing" folder and open up the Project Builder document. This way you have access to all the indexing and search facilities of Project Builder and fast access to each of the AIML files.


Enabling/Disabling personalities
======================
Each personality requires about 20 megs of Ram and takes time to load. You may want to disable personalities and enable others, perhaps your own new personality :-) To do this, go into "conf/startup.xml" and modify the "enabled" property of each personality. For example:

        <bot id="Alice" enabled="true">
		or
        <bot id="Alice" enabled="false">


Targeting - what is it? and why it might be useful
======================
This release did some work to bring back a relatively unknown feature of the Program D code called "targeting." It is useful for analyzing how people talk to your robot and to create new programming to support better conversations.

To invoke Targeting, just type the following in the command line:
/targeting.sh <<then press return>>

A graphical interface will appear and will begin to analyze log files. You will soon see some interesting info.

There are three main windows which you can select by clicking on tabs at the bottom of the screen, they are:
Targets
Inputs
Categories

Targets Window - This shows that a chat user typed a particular "input." Then, it shows what that input "matched" in the AIML logic. Next, it shows what you may want to create for a "new category" in AIML. You can modify the various fields to your liking and then modify the "reply" field and then click "save category." Once it is saved, it is put into a file located in "targets/targets.aiml" where you can later open up in a text editor and move into your robots brain.

Here is an example:
INPUT:		what is dos
MATCHED:	WHAT IS *
NEW CATEGORY:	WHAT IS DOS
REPLY:		What about it? DOS stands for Desparate Orthodontic 
		Services...no it really stands for Disk Operating 
		System. It was the first Operating system for the 
		IBM PC. It was created after the Apple had already 
		been released.

Inputs Window - The Inputs Window lists all the different inputs users gave in list form along with the patterns that they matched. You can double click on any of the items in the list to be taken to the "Targets Window" where you can edit and save.

Categories Window - The Categories Window shows you how many times various patterns have been matched. The number of times is the number of "activations." This list view is useful for determining which parts of your AIML are getting hit the most often and are probably most deserving of your tender loving care. Here to you may double click on any of the items in the list to go to the "Targeting Window" for editing.


Future Plans
======================
Like everyone else, we are quite busy. We plan to pretty things up a bit in the not so distant future. In the short term, we will be making several modifications to Charlie's logic (the AIML files). Feel free to grab what you want and use it as you see fit. If you'd like to help add to Charlie's logic, send an Email to aaron@cocoanutstech.com
