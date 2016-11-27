/*
 *  Starter code for University of Waterloo CS349 Fall 2016.
 *  
 *  bwbecker 20161113
 *  
 *  Some code adapted from https://github.com/possan/playlistcreator-example
 */
"use strict";

// An anonymous function that is executed passing "window" to the
// parameter "exports".  That is, it exports startApp to the window
// environment.
(function(exports) {
	var client_id = '617e177e250b42f28cc2c7994cf90cb9';		// Fill in with your value from Spotify
	var redirect_uri = 'http://localhost:3000/index.html';
	var g_access_token = '6973204623354ff6ae5a1b16295b34de';

	// MVC
	var model;
	var viewPlayList;

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

	/*
	 * Get the playlists of the logged-in user.
	 */
	function getPlaylists(callback) {
		console.log('getPlaylists');
		var url = 'https://api.spotify.com/v1/me/playlists';
		$.ajax(url, {
			dataType: 'json',
			async: false,
			headers: {
				'Authorization': 'Bearer ' + g_access_token
			},
			success: function(r) {
				console.log('got playlist response', r);
				callback(r.items);
			},
			error: function(r) {
				callback(null);
			}
		});
	}

	/*
	 * Get the songs from a playlist of the logged-in user
	 */
	function getSongs(callback, url) {
		console.log('getSongs from: ' + url);
		$.ajax(url, {
			dataType: 'json',
			async: false,
			headers: {
				'Authorization': 'Bearer ' + g_access_token
			},
			success: function(r) {
				console.log('got songs from a playlist ', r);
				callback(r);
			},
			error: function(r) {
				callback(null);
			}
		});
	}
	
	/*
	 * Redirect to Spotify to login.  Spotify will show a login page, if
	 * the user hasn't already authorized this app (identified by client_id).
	 * 
	 */
	var doLogin = function(callback) {
		var url = 'https://accounts.spotify.com/authorize?client_id=' + client_id +
			'&response_type=token' +
			'&scope=playlist-read-private' +
			'&redirect_uri=' + encodeURIComponent(redirect_uri);

		console.log("doLogin url = " + url);
		window.location = url;
	}

	/*
	 * What to do once the user is logged in.
	 */
	function loggedIn() {
		$('#login').hide();
		$('#loggedin').show();
		// getPlaylists(function(items) {
		// 	console.log('items = ', items);
		// 	items.forEach(function(item){
		// 		$('#playlists').append('<li id="' + item.id + '" >' + item.name + '</li>');
		// 		var url = item.tracks.href;
		// 		getSongs(function(songs){
		// 			console.log(songs);
					
		// 			// Add song column title
		// 			var songsHtmlStr = '';
		// 			songs.items.forEach(function(song){
		// 				songsHtmlStr += ('<li>' + song.track.name + '</li>');
		// 			});
		// 			$('#'+item.id).append('<p>Songs: <ul>'+songsHtmlStr+' </ul></p>');

		// 		}, url);
		// 	});
		// });
		getPlaylists(model.ParseJSON);
		model.Notify();

		// Post data to a server-side database.  See 
		// https://github.com/typicode/json-server
		var now = new Date();
		$.post("http://localhost:3000/demo", {"msg": "accessed at " + now.toISOString()}, null, "json");
	}

	/*
	 * Export startApp to the window so it can be called from the HTML's
	 * onLoad event.
	 */
	exports.startApp = function() {
		console.log('start app.');
		model = new SpotifyWebModel();
		viewPlayList = new PlaylistsView(model, "div#Playlists");
		model.AddView(viewPlayList);

		console.log('location = ' + location);

		// Parse the URL to get access token, if there is one.
		var hash = location.hash.replace(/#/g, '');
		var all = hash.split('&');
		var args = {};
		all.forEach(function(keyvalue) {
			var idx = keyvalue.indexOf('=');
			var key = keyvalue.substring(0, idx);
			var val = keyvalue.substring(idx + 1);
			args[key] = val;
		});
		console.log('args', args);

		if (typeof(args['access_token']) == 'undefined') {
			$('#start').click(function() {
				doLogin(function() {});
			});
			$('#login').show();
			$('#loggedin').hide();
		} else {
			g_access_token = args['access_token'];
			loggedIn();
		}
	}

})(window);
