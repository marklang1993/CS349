/*
 * SpotifyWebModel module 
 */

"use strict";

(function(exports){

    // class - Song
    var SongItem = function() {
        // variable definition
        this.Name = "NULL";    // Name of the song
        this.Rate = 1;         // Rating of the song (1 ~ 5) 
        this._tags = [];       // Tags of this song

        // Tag operations
        var FindTag = function(tag){
            // find tag
            for(var i = 0; i < _tags.length; i++){
                if(_tags[i] == tag){
                    return i;
                }
            }
            return -1;
        };

        var AddTag = function(tag){
            // add tag
            if(this.FindTag(tag) == -1){
                _tags.push(tag);
            }
        };

        var RemoveTag = function(tag){
            // remove tag
            var index = this.FindTag(tag);
            if(index != -1){
                _tags.splice(index, 1);
            }
        };

    };

    // class - PlayList
    var PlayListItem = function(){
        // variable definition
        this.Name = "NULL";     // Name of playList
        this._songs = [];       // song list (hashkey, SongItem)

        // Parse getSong JSON string
        var ParseJSON = function(jsonSongs){
            var that = this;   
            jsonSongs.items.forEach(function(song){
                var songItem = new SongItem();
                songItem.Name = song.track.id;

                // Add key-value pair
                that._songs.push([
                    song.track.id,
                    songItem
                ]);
            });
        };

        var GetSongList = function(){
            return _songs;
        };
    };

    // class - Model
    var SpotifyWebModel = function(){
        this._playLists = [];   // PlayList list (hashkey, PlayListItem)
        this._viewLists = [];   // View List

        var ParseJSON = function(jsonPlayLists){
            var that = this;
            jsonPlayLists.forEach(function(playList){
                var playListURL = playList.tracks.href;
                var playListItem = new PlayListItem();
                playListItem.Name = playList.name;
                
                // Init. PlayList
                getSongs(playListItem.ParseJSON, playListURL); 
                // Add key-value pair
                that._playLists.push([
                    jsonPlayLists.id,
                    playListItem
                ]);
            });
        };

        var subscribe = function(view){
            _viewLists.push(view);
        };

        var notify = function(){
            _viewLists.forEach(function(view) {
                view.update();
            });
        };
    };

    // class - View
    var PlaylistsView = function(model, divList){
        var that = this;
        var playLists = this.model._playLists;

        // Update View
        var update = function(){
            var html_divList = $(divList);
            that.html_divList.empty();

            // Create PlayList from template
            playLists.forEach(function(playList) {
                var t_Playlist = $("template#Playlist_template");

                //Create SongItem
                playList._songs.forEach(function(songItem) {
                    var t_SongItem = $("template#SongItem_template");
                    var t_html_SongItem = $(t_SongItem.html()); // to DOM element

                    t_html_SongItem.find(".SongItem.name").html(songItem.Name);
                    t_html_SongItem.find(".SongItem.rate").html(songItem.Rate);
                    t_html_SongItem.find(".SongItem.tags").html("Tags Test");   

                    // Add to Playlist
                    t_Playlist.append(t_html_SongItem);                 
                });

                var t_html_Playlist = $(t_Playlist.html()); // to DOM element
                t_html_Playlist.find(".Playlist.name").html(playList.Name);

                // Add to HTML page
                html_divList.append(t_html_Playlist);
            });
        };

    };
    
})(window);