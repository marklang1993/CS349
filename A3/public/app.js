"use strict";

// An anonymous function that is executed passing "window" to the
// parameter "exports".  That is, it exports startApp to the window
// environment.
(function(exports) {

	// Connector
	var connector;
	
	// MVC
	var model;

// --------------------------------mvc.js###Start--------------------------------
	// class - Song
    var SongItem = function() {
        // variable definition
        this.Name = "NULL";    	// Name of the song
        this.Rate = 1;         	// Rating of the song (1 ~ 5)
		this.isDisplay = true;	// Display this SongItem
		this.TagShow = false; 	// Flag: showing tag
        this._tags = [];       	// Tags of this song

        // Tag operations
		this.TagShownToggle = function(){
			this.TagShow = !this.TagShow;
		};

		// Failed : return -1;
		this.FindTag = function(tag){
            // find tag
			var index = -1;
            _.forEach(this._tags, function(_tag, idx)
			{
				if(_tag === tag){
                    index = idx;
                }
			});
            return index;
        };

        this.AddTag = function(tag){
            // add tag
            if(this.FindTag(tag) === -1){
                this._tags.push(tag);
            }
        };

        this.RemoveTag = function(tag){
            // remove tag
            var index = this.FindTag(tag);
            if(index != -1){
                this._tags.splice(index, 1);
            }
        };

		this.RateAdjust = function(rateOp){
			if(this.Rate < 5 && rateOp === "+"){		// inc.
				this.Rate = this.Rate + 1; 
			}
			else if(this.Rate > 1 && rateOp === "-"){	// dec.
				this.Rate = this.Rate - 1; 
			}
		}

    };

    // class - PlayList
    var PlayListItem = function(){
		var that = this;
        this.Name = "NULL";     // Name of playList
		this.isDisplay = true;	// Display this Playlist
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

		// Recheck all SongItems
		this.Recheck = function(){
			this.isDisplay = false;
			_.forEach(this._songs, function(songItemTuple, idx){
				if(songItemTuple[1].isDisplay === true){
					this.isDisplay = true;
					return;
				}
			});
		};

        this.GetSongList = function(){
            return _songs;
        };
    };

	// class - TagCollection
	var TagCollection = function(){
		var that = this;
		this.selectedTag = "";	// Selected tag
		this._tags = [];		// All tags

		// Failed : return -1;
		this.FindTag = function(tag){
            // find tag
			var index = -1;
            _.forEach(this._tags, function(_tag, idx)
			{
				if(_tag === tag){
                    index = idx;
                }
			});
            return index;
        };

        this.AddTag = function(tag){
            // add tag
            if(that.FindTag(tag) === -1){
                that._tags.push(tag);
            }
        };

        this.RemoveTag = function(tag){
            // remove tag
            var index = that.FindTag(tag);
            if(index != -1){
                that._tags.splice(index, 1);
            }
        };
	};

    // class - Model
    var SpotifyWebModel = function(){
		var that = this;
        this._playLists = [];   	// PlayList list (hashkey, PlayListItem)
        this._viewLists = [];   	// View List
		this._tagCollection = {};	// Tag Collection

		// Init. TagCollection
		this.InitTagCollection = function(){
			this._tagCollection = new TagCollection();
		};

		// Insert Tag into TagCollection
		this.InsertTag = function(tag){
			this._tagCollection.AddTag(tag);
		};

		// Remove Tag from both TagCollection and SongItems
		this.RemoveTag = function(tag){
			// Remove Tag from TagCollection
			this._tagCollection.RemoveTag(tag);

			// Remove Tag from all SongItems
			_.forEach(this._playLists, function(playListTuple, idx) {
				_.forEach(playListTuple[1]._songs, function(songItemTuple, idx){
					songItemTuple[1].RemoveTag(tag);
				});
			});
		};

		// Search
		this.Search = function(keyWord){
			// Check isEmpty
			if(keyWord === "") {
				// Set all SongItem to Display
				this.Search_Set_isDisplay_All(true);
			}
			else {
				// Set all SongItem not to Display first
				this.Search_Set_isDisplay_All(false);
				// Try to Split and Check expression
				if(keyWord.search(/#tag/gi) != -1){
					var pos = keyWord.search(/#tag/gi) + 3;
					// Read operand & Split keyword
					if(pos + 2 < keyWord.length){
						pos = pos + 1;
						var pos_exprs = pos + 1;
						var exprs = this.Search_Split(keyWord.substr(pos_exprs, keyWord.length - pos_exprs));
						if(keyWord.substr(pos, 1) == ":"){
							// Contain any of the tags
							_.forEach(this._playLists, function(playListTuple, idx){
								var isDisplayVal = false;	// isDisplay of PlayList
								_.forEach(playListTuple[1]._songs, function(songItemTuple, idx){
									var ret = _.intersection(exprs, songItemTuple[1]._tags);
									if(ret.length > 0){
										songItemTuple[1].isDisplay = true;
									}
									// Save result to playlist
									isDisplayVal = isDisplayVal || songItemTuple[1].isDisplay;
								});
								playListTuple[1].isDisplay = isDisplayVal;
							});
						}
						else if(keyWord.substr(pos, 1) == "="){
							// Contain all tags
							_.forEach(this._playLists, function(playListTuple, idx){
								var isDisplayVal = false;	// isDisplay of PlayList
								_.forEach(playListTuple[1]._songs, function(songItemTuple, idx){
									var ret = _.intersection(exprs, songItemTuple[1]._tags);
									if(ret.length === exprs.length){
										songItemTuple[1].isDisplay = true;
									}
									// Save result to playlist
									isDisplayVal = isDisplayVal || songItemTuple[1].isDisplay;
								});
								playListTuple[1].isDisplay = isDisplayVal;
							});
						}
					}
				}
				else if (keyWord.search(/#rate>=/gi) != -1){
					// Rate greater than or equal to
					
				}

				// Search SongItem Name

			}
		}

		// Search Helper functions: Set 
		this.Search_Set_isDisplay_All = function(val){
			// Set all SongItem to Display
			_.forEach(this._playLists, function(playListTuple, idx){
				_.forEach(playListTuple[1]._songs, function(songItemTuple, idx){
					songItemTuple[1].isDisplay = val;
				});
				playListTuple[1].isDisplay = val;
			});
		};

		this.Search_Split = function(expression){
			var exprs = expression.split(" ");
			return exprs;
		};

		// Parse JSON string of PlayList
        this.ParseJSON = function(jsonPlayLists){
			// _playLists.pop();
            jsonPlayLists.forEach(function(playList){
                var playListURL = playList.tracks.href;
                var playListItem = new PlayListItem();
                playListItem.Name = playList.name;
                
                // Init. PlayList
                connector.getSongs(playListItem.ParseJSON, playListURL); 
                // Add key-value pair
                that._playLists.push([playList.id, playListItem]);
            });
        };

		// Subscribe view to model
        this.AddView = function(view){
            this._viewLists.push(view);
        };

		// Notify all views
        this.Notify = function(){
            this._viewLists.forEach(function(view) {
                view.update();
            });
        };
		
    };

    // class - View
	var SwitchBtnView = function(controller, divList){
		var that = this;

		// Update View
		this.update = function(){
			var html_divList = $(divList);
			// Bind buttons
			html_divList.find("#switchPlaylistBtn").click(controller.makeSwitchPlaylistBtn());
			html_divList.find("#switchTaglistBtn").click(controller.makeSwitchTaglistBtn());
		};
	};

    var PlaylistsView = function(model, controller, divList){
        var that = this;

        // Update View
        this.update = function(){
			// Set Searchbar Button
			$("div#Playlists").find("#PlaylistsSearchbar").find("#songSearchBtn").click(controller.makeSearchBarBtnController(model));
			
			// PlayList view			
            var html_divList = $(divList);
            html_divList.empty();

			var playLists = model._playLists;

            // Create PlayList from template
            _.forEach(playLists, function(playListTuple, idx) {
				// Check isDisplay
				if(playListTuple[1].isDisplay === true){
					var t_Playlist = $("template#Playlist_template");
					var t_html_Playlist = $(t_Playlist.html()); // to DOM element
					t_html_Playlist.find("h3").html(playListTuple[1].Name);

					//Create SongItem
					_.forEach(playListTuple[1]._songs, function(songItemTuple, idx) {

						// Check isDisplay
						if(songItemTuple[1].isDisplay === true){
							var t_SongItem = $("template#SongItem_template");
							var t_html_SongItem = $(t_SongItem.html()); // to DOM element

							t_html_SongItem.find(".name").html(idx + "." + songItemTuple[1].Name);
							t_html_SongItem.find("#rateDecBtn").find(".rateBtn").click(controller.makeRateOpBtnController(songItemTuple[1], "-"));					
							t_html_SongItem.find(".rate").html(that.toSTAR(songItemTuple[1].Rate));
							t_html_SongItem.find("#rateIncBtn").find(".rateBtn").click(controller.makeRateOpBtnController(songItemTuple[1], "+"));

							// Tag related
							t_html_SongItem.find("#tagShownBtn").find(".tagBtn").click(controller.makeTagShownToggleController(songItemTuple[1]));														
							if(songItemTuple[1].TagShow === true){
								t_html_SongItem.find(".tags").show();
								// # tagBtnList
								_.forEach(songItemTuple[1]._tags, function(tag, idx) {
									// Add Button
									var pos = t_html_SongItem.find(".tags").find(".tagBtnList");
									pos.append("<button class=\"tagBtn\" id=\"" + tag + "\">" + tag + "</button>");
									// Add handler
									pos.find("#" + tag).click(controller.makeDelTagBtnController(songItemTuple[1], tag));
								});
								// # Bind handler to tagAppendBtn
								var pos = t_html_SongItem.find(".tags").find("#tagAppendBtn");
								pos.click(controller.makeAddTagBtnController(songItemTuple[1], model));
							}
							else{
								t_html_SongItem.find(".tags").hide();
							}
							// Add to Playlist
							t_html_Playlist.append(t_html_SongItem);   
						}
					});

					// Add to HTML page
					html_divList.append(t_html_Playlist);
				}
            });
        };

		// parse rate to STAR(1 ~ 5)
		this.toSTAR = function(rate){
			var STAR = "";
			for(var i = 0; i < rate; i++){
				STAR += "★";
			}
			for(var i = 0; i < 5 - rate; i++){
				STAR += "☆";
			}
			return STAR;
		};

    };

	var TaglistsView = function(model, controller, divList){
		var that = this;

		this.update = function(){
			// select right locations
			var html_divList = $(divList);
			html_divList = html_divList.find(".CurrentTags");
            html_divList.empty();
			var html_inputArea = $(divList);
			html_inputArea = html_inputArea.find(".InputTag");
			// Bind selectedTag Display
			$(divList).find("#selectedTag").html("Selected Tag: "+ model._tagCollection.selectedTag);

			// Bind Input Button
			html_inputArea.find("#addTagBtn").click(controller.makeAddTagBtnController());

			// Pick data
			var tagCollection = model._tagCollection._tags;

			// Create TagList from template
			var t_Taglist = $("template#Taglist_template");
			var t_html_Taglist = $(t_Taglist.html()); // to DOM element
			_.forEach(tagCollection, function(tag, idx){
				// Add one row
				var tagBtnID = "_tagId_" + idx;
				t_html_Taglist.append("<div class=\"tagCollectionItem\" id=\"" + tagBtnID + "\"> </div>");
                // Add Text & Button
				var pos = t_html_Taglist.find("#" + tagBtnID);
				pos.append("<button class=\"tagCollectionName\">" + tag + "</button>");
                pos.append("<button class=\"tagBtn\">-</button>");

				// Add handler
				pos.find(".tagCollectionName").click(controller.makeSelectTagBtnController(tag));		
				pos.find(".tagBtn").click(controller.makeRemoveTagBtnController(model,tag));			
			});

			// Add to HTML page
			html_divList.append(t_html_Taglist);
		};
	}

	// class - Controller 
	var SwitchBtnController = function(){
		// Switch to Playlist Button handler
		this.makeSwitchPlaylistBtn = function(){
			return function(){
				var playlistPos = $("div#Playlists");
				var taglistPos = $("div#Taglists");
				playlistPos.show();
				taglistPos.hide();
			};
		};

		// Switch to Taglist Button handler
		this.makeSwitchTaglistBtn = function(){
			return function(){
				var playlistPos = $("div#Playlists");
				var taglistPos = $("div#Taglists");
				playlistPos.hide();
				taglistPos.show();
			};
		};
	};

	var PlaylistsController = function(model){
		
		// Rate operation Button handler
		this.makeRateOpBtnController = function(songItem, rateOp){
			return function(){
				songItem.RateAdjust(rateOp);
				// update
				model.Notify();
			};
		};

		// Tag Row Display Toggle Button handler
		this.makeTagShownToggleController = function(songItem){
			return function(){
				songItem.TagShownToggle();
				// update
				model.Notify();
			};
		};

		// Insert tag Button handler
		this.makeAddTagBtnController = function(songItem, model){
			return function(){
				if(model._tagCollection.selectedTag != ""){
					if(model._tagCollection.FindTag(model._tagCollection.selectedTag != -1)){
						songItem.AddTag(model._tagCollection.selectedTag);
						// update
						model.Notify();
					}
				}
			};
		};

		// Delete tag Button handler
		this.makeDelTagBtnController = function(songItem, tag){
			return function(){
				songItem.RemoveTag(tag);
				// update
				model.Notify();
			};
		};

		// Searchbar Button handler
		this.makeSearchBarBtnController = function(model){
			return function(){
				// Search
				var keyWord = $(".songSearchbar").val();
				model.Search(keyWord);
				// update
				model.Notify();
			};
		};

		// Test handler
		this.makeTest = function(){
			return function(){
				console.log("This is a test!");
			};
		};

	};

	var TaglistsController = function(model){
		// Add new Tag to TagCollection
		this.makeAddTagBtnController = function(){
			return function(){
				// Get Input Area
				var tagTextBox = $(".tagTextBox");
				var tag = tagTextBox.val();
				if(tag != ""){
					tagTextBox.val("");
					model.InsertTag(tag);
					// update
					model.Notify();
				}
			};
		};

		// Remove tag from TagCollection
		this.makeRemoveTagBtnController = function(model, tag){
			return function(){
				// check selectedTag
				if(model._tagCollection.selectedTag === tag){
					// clear
					model._tagCollection.selectedTag = "";
				}
				// remove from model
				model.RemoveTag(tag);
				// update
				model.Notify();
			};
		};

		// Set tag as selectedTag
		this.makeSelectTagBtnController = function(tag){
			return function(){
				model._tagCollection.selectedTag = tag;
				// update
				model.Notify();
			};
		};
	}

// --------------------------------mvc.js###End--------------------------------

// --------------------------------connector.js###Start--------------------------------

	/*
	*  Starter code for University of Waterloo CS349 Fall 2016.
	*  
	*  bwbecker 20161113
	*  
	*  Some code adapted from https://github.com/possan/playlistcreator-example
	*/
	var Connector = function() {
		var client_id = '617e177e250b42f28cc2c7994cf90cb9';		// Fill in with your value from Spotify
		var redirect_uri = 'http://localhost:3000/index.html';
		this.g_access_token = '6973204623354ff6ae5a1b16295b34de';

		/*
		* Get the playlists of the logged-in user.
		*/
		this.getPlaylists = function(callback) {
			console.log('getPlaylists');
			var url = 'https://api.spotify.com/v1/me/playlists';
			$.ajax(url, {
				dataType: 'json',
				async: false,
				headers: {
					'Authorization': 'Bearer ' + this.g_access_token
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
		this.getSongs = function(callback, url) {
			console.log('getSongs from: ' + url);
			$.ajax(url, {
				dataType: 'json',
				async: false,
				headers: {
					'Authorization': 'Bearer ' + this.g_access_token
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
		this.doLogin = function(callback) {
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
		this.loggedIn = function() {
			$('#login').hide();
			$('#loggedin').show();

			this.getPlaylists(model.ParseJSON);
			model.Notify();

			// Post data to a server-side database.  See 
			// https://github.com/typicode/json-server
			var now = new Date();
			$.post("http://localhost:3000/demo", {"msg": "accessed at " + now.toISOString()}, null, "json");
		}
	};
		
// --------------------------------connector.js###End--------------------------------	

	/*
	 * Export startApp to the window so it can be called from the HTML's
	 * onLoad event.
	 */
	exports.startApp = function() {
		console.log('start app.');
		// init.
		connector = new Connector();
		model = new SpotifyWebModel();
		var controllerPlayList = new PlaylistsController(model);
		var controllerTagList = new TaglistsController(model);
		var controllerSwitchBtn = new SwitchBtnController();
		var viewSwitchBtn = new SwitchBtnView(controllerSwitchBtn, "div#viewSwitchBtn");
		var viewPlayList = new PlaylistsView(model, controllerPlayList, "div#PlaylistsDisplay");
		var viewTagList = new TaglistsView(model, controllerTagList, "div#Taglists");
		model.AddView(viewSwitchBtn);
		model.AddView(viewPlayList);
		model.AddView(viewTagList);
		model.InitTagCollection();

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
				connector.doLogin(function() {});
			});
			$('#login').show();
			$('#loggedin').hide();
		} else {
			connector.g_access_token = args['access_token'];
			connector.loggedIn();
		}
	}

})(window);
