A3 CS349

1. Native Functionality:
(1) Maintaining (Adding / Removing) Tags (Can only be doing in TagView)
    (a) Click the most top button "Tag" to swap to TagView
    
    (b) Add tag by typing tag name in the bottom textbox and then clicking button "+".
        # Note that: tags with same name cannot be added;
                     avoid adding tags with the name containing special characters ";", "$", "#", " ".
    
    (c) Removing tag by clicking button "-" beside the tag you want remove
        # Note that: corresponding tag in the songs will also be removed after removing it from TagView.

(2) Assign Tags to a Song
    (a) Click the tag you want to add in the TagView and then the banner "Selected Tag" will show the 
        tag you selected;
        Click the most top button "Playlist" to swap to PlaylistView;
        Click the red button "T" beside the song that you want to add the tag and then the row, which 
        shows all tags binded to this song, will appear;
        Click the red button "+" in that row of tags to add the selected tag to this song.
        # Note that: it is unable to add this tag when this tag is removed after selecting it.
    
    (b) Can assign more tags to one song by doing (a).

(3) Change the Rating of a Song (Can only be doing in PlaylistView)
    (a) Increase the rating of a song: Click lightblue button "+" beside the song that you want to
    increase the rating.
    
    (b) Decrease the rating of a song: Click lightblue button "-" beside the song that you want to
    decrease the rating.
    
    (c) The rating is shown by the stars between the lightblue buttons.
    # Note that: the range of a song is from 1 to 5.

(4) Searching (Can only be doing in PlaylistView)
    (a) Searching a song with all of the given tags:
        Type "#tag=" and the tags separated by whitespace into the search bar and click button "Search".
        # e.g: search for a song with tags "one" and "two": 
               type "#tag=one two" into the search bar. 

    (b) Searching a song with any of the given tags:
        Type "#tag:" and the tags separated by whitespace into the search bar and click button "Search".
        # e.g: search for a song with tags "one" or "two": 
               type "#tag:one two" into the search bar.      
    
    (c) Searching a song with rating greater than or equal to a given value:
        Type "#rate>=" and the given value and click button "Search".
        # e.g: search for a song with rating that is greater than or equal to 3: 
               type "#rate>=3" into the search bar.  

(5) Save / Restore the Current Data into JSON Server
    (a) Click the most top button "Save" to push the data to the JSON Server;
    
    (b) The tag list, rating and tag data embedded in the song will be restored after refreshing the page
        or loading this page next time.

(6) Detection of losing current session of Spotify server / connection error.
    (a) A dialog will pop up to notify the user there is a session lose / connection error, and go back to
        login page automatically after confirming the message.

2. Enhanced Functionality
(1) Play a song
    (a) Play a specific song by clicking the green button "P" beside that song, and a PlayButton widget 
        provided by Spotify will pop up.
        Click the play button on this widget to start playing.
    
    (b) Play another song by clicking the green button "P" beside that song, and a new PlayButton widget
        will pop up.
    
    (c) Close the PlayButton widget by clicking the green button "P" beside the same song that is shown
        on the PlayButton.

(2) Enhanced Searching
    (a) Allow the user to search for rating of a song with other conditions.
        Like 1.(4).(c), it is able to searching a song with rate is equal to the given value by changing
        the operand to "==".
        # e.g: search for a song with rating that is equal to 3: 
               type "#rate==3" into the search bar.  
        Other operands are also available: <, >, ==, !=, <=, >=

    (b) Allow the user to do fuzzy search for a Playlist with given keyword.
        # e.g: search for a playlist with name contains keyword "English Song".
               type "#playlist:English Song" into the search bar.
               all playlists with name like "english", "english song", "ENGLISH", "song" will be presented.
        # Note: the searching for the keyword is CASE INSENSITIVE.
        
    (c) Allow the user to do exact search for a Playlist with given name.
        # e.g: search for a playlist with the exact name "English Song".
               type "#playlist=English Song" into the search bar.
               only the playlist with name like "English Song" will be presented.
        # Note: the searching for the keyword is CASE SENSITIVE.
    
    (d) Allow the user to search based on the songs' name (Ordinary Search).
        # e.g: search songs with name containing the keyword "my day".
               type "my day" into the search bar.
               the songs with word "my" or "day" will be presented, such as "Cheap Day Return", "My God".
        # Note: the searching for the keyword is CASE INSENSITIVE.

    (e) Clear Searching Results and Display All Songs & Playlists
        Clearing the contents in search bar and click button "Search".

(3) Enable another CSS for screen rotation
    (a) Automatically adjusting the margins, paddings and size of some widgets on the page.