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

	/*
	 * Get the playlists of the logged-in user.
	 */
	function getPlaylists(callback) {
		console.log('getPlaylists');
		var url = 'https://api.spotify.com/v1/me/playlists';
		$.ajax(url, {
			dataType: 'json',
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
		getPlaylists(function(items) {
			console.log('items = ', items);
			items.forEach(function(item){
				$('#playlists').append('<li id="' + item.id + '" >' + item.name + '</li>');
				var url = item.tracks.href;
				getSongs(function(songs){
					console.log(songs);
					
					// Add song column title
					var songsHtmlStr = '';
					songs.items.forEach(function(song){
						songsHtmlStr += ('<li>' + song.track.name + '</li>');
					});
					$('#'+item.id).append('<p>Songs: <ul>'+songsHtmlStr+' </ul></p>');

				}, url);
			});
		});

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
