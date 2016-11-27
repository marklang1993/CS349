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
        this.FindTag = function(tag){
            // find tag
            for(var i = 0; i < _tags.length; i++){
                if(_tags[i] === tag){
                    return i;
                }
            }
            return -1;
        };

        this.AddTag = function(tag){
            // add tag
            if(this.FindTag(tag) === -1){
                _tags.push(tag);
            }
        };

        this.RemoveTag = function(tag){
            // remove tag
            var index = this.FindTag(tag);
            if(index != -1){
                _tags.splice(index, 1);
            }
        };

    };

    // class - PlayList
    var PlayListItem = function(){
		var that = this;
        this.Name = "NULL";     // Name of playList
        this._songs = [];       // song list (hashkey, SongItem)

        // Parse getSong JSON string
        this.ParseJSON = function(jsonSongs){  
            jsonSongs.items.forEach(function(song){
                var songItem = new SongItem();
                songItem.Name = song.track.name;

                // Add key-value pair
                that._songs.push([song.track.id, songItem]);
            });
        };

        this.GetSongList = function(){
            return _songs;
        };
    };

    // class - Model
    var SpotifyWebModel = function(){
		var that = this;
        this._playLists = [];   // PlayList list (hashkey, PlayListItem)
        this._viewLists = [];   // View List

        this.ParseJSON = function(jsonPlayLists){
			// _playLists.pop();
            jsonPlayLists.forEach(function(playList){
                var playListURL = playList.tracks.href;
                var playListItem = new PlayListItem();
                playListItem.Name = playList.name;
                
                // Init. PlayList
                getSongs(playListItem.ParseJSON, playListURL); 
                // Add key-value pair
                that._playLists.push([playList.id, playListItem]);
            });
        };

        this.AddView = function(view){
            this._viewLists.push(view);
        };

        this.Notify = function(){
            this._viewLists.forEach(function(view) {
                view.update();
            });
        };
    };

    // class - View
    var PlaylistsView = function(model, divList){
        var that = this;

        // Update View
        this.update = function(){
            var html_divList = $(divList);
            html_divList.empty();

			var playLists = model._playLists;

            // Create PlayList from template
            _.forEach(playLists, function(playListTuple, idx) {
                var t_Playlist = $("template#Playlist_template");
				var t_html_Playlist = $(t_Playlist.html()); // to DOM element
                t_html_Playlist.find("h3").html(playListTuple[1].Name);

                //Create SongItem
                _.forEach(playListTuple[1]._songs, function(songItemTuple, idx) {
                    var t_SongItem = $("template#SongItem_template");
                    var t_html_SongItem = $(t_SongItem.html()); // to DOM element

                    t_html_SongItem.find(".name").html(songItemTuple[1].Name);
                    t_html_SongItem.find(".rate").html(songItemTuple[1].Rate);
                    t_html_SongItem.find(".tags").html("Tags Test");   

                    // Add to Playlist
                    t_html_Playlist.append(t_html_SongItem);                 
                });

                

                // Add to HTML page
                html_divList.append(t_html_Playlist);
            });
        };

    };
    
})(window);