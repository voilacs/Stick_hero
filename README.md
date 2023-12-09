# ap_game_2022081_2022160
An Implementation of key java concepts with a stick hero game

Contributions:
1) Dhawal Garg:(2022160) Ensuring that the stick falls on time on pillars and the keystroke for space bar inside the appropriate start function. OOPs concept have been taken care of and special getter and setter methods for cherry picking and fetching of score is made.
2) Anmol Kumar: (2022081) In the walking timeline transition the player turns 180 degree to collect cherries and that triggers setting of ischerrycollected to false. Also score++ and cherry_count++ has been done at the time when player crosses pillar and collects cherries.

How to play the game?
1) The new game starts by pressing the button and holding spacebar is used to change the length of stick.
2) Make the stick of appropriate size and then start walking on it, a 180 flip can be used to collect cherries
3) The 180 flip happens with a single click of spacebar
4) On a failure the player falls by itself and the menu appears to revive the player with cherries or turn back to the main menu.

Description:

Description
Three classes PlatformController, StickmanGame and StickmanController are used to divide the implementation of the game into three distinguished categories.
A stickman can only be one other instances of it called in various scenes are only initialized instances of it is called.
Thus a stickman can be implemented using "SINGLETON" design pattern.

Scores are updated in each passing of a stick and cherries are also incremented at each collection of cherry.

Private github link: https://github.com/voilacs/ap_game_2022081_2022160/
