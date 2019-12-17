/* ********************************** */
( function ( mw ) {
	'use strict';

	// Catch exceptions to avoid fatal in Chrome's "Block data storage" mode
	// which throws when accessing the localStorage property itself, as opposed
	// to the standard behaviour of throwing on getItem/setItem. (T148998)
	var
		localStorage = ( function () {
			try {
				return window.localStorage;
			} catch ( e ) {}
		}() ),
		sessionStorage = ( function () {
			try {
				return window.sessionStorage;
			} catch ( e ) {}
		}() );

	/**
	 * A wrapper for an HTML5 Storage interface (`localStorage` or `sessionStorage`)
	 * that is safe to call on all browsers.
	 *
	 * @class mw.SafeStorage
	 * @private
	 * @param {Object|undefined} store The Storage instance to wrap around
	 */
	function SafeStorage( store ) {
		this.store = store;
	}

	/**
	 * Retrieve value from device storage.
	 *
	 * @param {string} key Key of item to retrieve
	 * @return {string|null|boolean} String value, null if no value exists, or false
	 *  if localStorage is not available.
	 */
	SafeStorage.prototype.get = function ( key ) {
		try {
			return this.store.getItem( key );
		} catch ( e ) {}
		return false;
	};

	/**
	 * Set a value in device storage.
	 *
	 * @param {string} key Key name to store under
	 * @param {string} value Value to be stored
	 * @return {boolean} Whether the save succeeded or not
	 */
	SafeStorage.prototype.set = function ( key, value ) {
		try {
			this.store.setItem( key, value );
			return true;
		} catch ( e ) {}
		return false;
	};

	/**
	 * Remove a value from device storage.
	 *
	 * @param {string} key Key of item to remove
	 * @return {boolean} Whether the save succeeded or not
	 */
	SafeStorage.prototype.remove = function ( key ) {
		try {
			this.store.removeItem( key );
			return true;
		} catch ( e ) {}
		return false;
	};

	/**
	 * A wrapper for the HTML5 `localStorage` interface
	 * that is safe to call on all browsers.
	 *
	 * @class
	 * @singleton
	 * @extends mw.SafeStorage
	 */
	mw.storage = new SafeStorage( localStorage );

	/**
	 * A wrapper for the HTML5 `sessionStorage` interface
	 * that is safe to call on all browsers.
	 *
	 * @class
	 * @singleton
	 * @extends mw.SafeStorage
	 */
	mw.storage.session = new SafeStorage( sessionStorage );

}( window.mediaWiki ) );

slice = Array.prototype.slice;
	/**
	 * Object constructor for messages.
	 *
	 * Similar to the Message class in MediaWiki PHP.
	 *
	 * Format defaults to 'text'.
	 *
	 *     @example
	 *
	 *     var obj, str;
	 *     mw.messages.set( {
	 *         'hello': 'Hello world',
	 *         'hello-user': 'Hello, $1!',
	 *         'welcome-user': 'Welcome back to $2, $1! Last visit by $1: $3'
	 *     } );
	 *
	 *     obj = new mw.Message( mw.messages, 'hello' );
	 *     mw.log( obj.text() );
	 *     // Hello world
	 *
	 *     obj = new mw.Message( mw.messages, 'hello-user', [ 'John Doe' ] );
	 *     mw.log( obj.text() );
	 *     // Hello, John Doe!
	 *
	 *     obj = new mw.Message( mw.messages, 'welcome-user', [ 'John Doe', 'Wikipedia', '2 hours ago' ] );
	 *     mw.log( obj.text() );
	 *     // Welcome back to Wikipedia, John Doe! Last visit by John Doe: 2 hours ago
	 *
	 *     // Using mw.message shortcut
	 *     obj = mw.message( 'hello-user', 'John Doe' );
	 *     mw.log( obj.text() );
	 *     // Hello, John Doe!
	 *
	 *     // Using mw.msg shortcut
	 *     str = mw.msg( 'hello-user', 'John Doe' );
	 *     mw.log( str );
	 *     // Hello, John Doe!
	 *
	 *     // Different formats
	 *     obj = new mw.Message( mw.messages, 'hello-user', [ 'John "Wiki" <3 Doe' ] );
	 *
	 *     obj.format = 'text';
	 *     str = obj.toString();
	 *     // Same as:
	 *     str = obj.text();
	 *
	 *     mw.log( str );
	 *     // Hello, John "Wiki" <3 Doe!
	 *
	 *     mw.log( obj.escaped() );
	 *     // Hello, John &quot;Wiki&quot; &lt;3 Doe!
	 *
	 * @class mw.Message
	 *
	 * @constructor
	 * @param {mw.Map} map Message store
	 * @param {string} key
	 * @param {Array} [parameters]
	 */
	function Message( map, key, parameters ) {
		this.format = 'text';
		this.map = map;
		this.key = key;
		this.parameters = parameters === undefined ? [] : slice.call( parameters );
		return this;
	}

	Message.prototype = {
		/**
		 * Get parsed contents of the message.
		 *
		 * The default parser does simple $N replacements and nothing else.
		 * This may be overridden to provide a more complex message parser.
		 * The primary override is in the mediawiki.jqueryMsg module.
		 *
		 * This function will not be called for nonexistent messages.
		 *
		 * @return {string} Parsed message
		 */
		parser: function () {
			return this.parameters[0];
			//return mw.format.apply( null, [ this.map.get( this.key ) ].concat( this.parameters ) );
		},

		// eslint-disable-next-line valid-jsdoc
		/**
		 * Add (does not replace) parameters for `$N` placeholder values.
		 *
		 * @param {Array} parameters
		 * @chainable
		 */
		params: function ( parameters ) {
			var i;
			for ( i = 0; i < parameters.length; i++ ) {
				this.parameters.push( parameters[ i ] );
			}
			return this;
		},

		/**
		 * Convert message object to its string form based on current format.
		 *
		 * @return {string} Message as a string in the current form, or `<key>` if key
		 *  does not exist.
		 */
		toString: function () {
			var text;

			if ( !this.exists() ) {
				// Use ?key? as text if key does not exist
				// Err on the side of safety, ensure that the output
				// is always html safe in the event the message key is
				// missing, since in that case its highly likely the
				// message key is user-controlled.
				// '?' is used instead of '<' to side-step any
				// double-escaping issues.
				// (Keep synchronised with Message::toString() in PHP.)
				return '?' + mw.html.escape( this.key ) + '?';
			}

			if ( this.format === 'plain' || this.format === 'text' || this.format === 'parse' ) {
				text = this.parser();
			}

			if ( this.format === 'escaped' ) {
				text = this.parser();
				text = mw.html.escape( text );
			}

			return text;
		},

		/**
		 * Change format to 'parse' and convert message to string
		 *
		 * If jqueryMsg is loaded, this parses the message text from wikitext
		 * (where supported) to HTML
		 *
		 * Otherwise, it is equivalent to plain.
		 *
		 * @return {string} String form of parsed message
		 */
		parse: function () {
			this.format = 'parse';
			return this.toString();
		},

		/**
		 * Change format to 'plain' and convert message to string
		 *
		 * This substitutes parameters, but otherwise does not change the
		 * message text.
		 *
		 * @return {string} String form of plain message
		 */
		plain: function () {
			this.format = 'plain';
			return this.toString();
		},

		/**
		 * Change format to 'text' and convert message to string
		 *
		 * If jqueryMsg is loaded, {{-transformation is done where supported
		 * (such as {{plural:}}, {{gender:}}, {{int:}}).
		 *
		 * Otherwise, it is equivalent to plain
		 *
		 * @return {string} String form of text message
		 */
		text: function () {
			this.format = 'text';
			return this.toString();
		},

		/**
		 * Change the format to 'escaped' and convert message to string
		 *
		 * This is equivalent to using the 'text' format (see #text), then
		 * HTML-escaping the output.
		 *
		 * @return {string} String form of html escaped message
		 */
		escaped: function () {
			this.format = 'escaped';
			return this.toString();
		},

		/**
		 * Check if a message exists
		 *
		 * @see mw.Map#exists
		 * @return {boolean}
		 */
		exists: function () {
			return true;
			//return this.map.exists( this.key );
		}
	};

/* ******************************* */
/**
 * Library for simple URI parsing and manipulation.
 *
 * Intended to be minimal, but featureful; do not expect full RFC 3986 compliance. The use cases we
 * have in mind are constructing 'next page' or 'previous page' URLs, detecting whether we need to
 * use cross-domain proxies for an API, constructing simple URL-based API calls, etc. Parsing here
 * is regex-based, so may not work on all URIs, but is good enough for most.
 *
 * You can modify the properties directly, then use the #toString method to extract the full URI
 * string again. Example:
 *
 *     var uri = new mw.Uri( 'http://example.com/mysite/mypage.php?quux=2' );
 *
 *     if ( uri.host == 'example.com' ) {
 *         uri.host = 'foo.example.com';
 *         uri.extend( { bar: 1 } );
 *
 *         $( 'a#id1' ).attr( 'href', uri );
 *         // anchor with id 'id1' now links to http://foo.example.com/mysite/mypage.php?bar=1&quux=2
 *
 *         $( 'a#id2' ).attr( 'href', uri.clone().extend( { bar: 3, pif: 'paf' } ) );
 *         // anchor with id 'id2' now links to http://foo.example.com/mysite/mypage.php?bar=3&quux=2&pif=paf
 *     }
 *
 * Given a URI like
 * `http://usr:pwd@www.example.com:81/dir/dir.2/index.htm?q1=0&&test1&test2=&test3=value+%28escaped%29&r=1&r=2#top`
 * the returned object will have the following properties:
 *
 *     protocol  'http'
 *     user      'usr'
 *     password  'pwd'
 *     host      'www.example.com'
 *     port      '81'
 *     path      '/dir/dir.2/index.htm'
 *     query     {
 *                   q1: '0',
 *                   test1: null,
 *                   test2: '',
 *                   test3: 'value (escaped)'
 *                   r: ['1', '2']
 *               }
 *     fragment  'top'
 *
 * (N.b., 'password' is technically not allowed for HTTP URIs, but it is possible with other kinds
 * of URIs.)
 *
 * Parsing based on parseUri 1.2.2 (c) Steven Levithan <http://stevenlevithan.com>, MIT License.
 * <http://stevenlevithan.com/demo/parseuri/js/>
 *
 * @class mw.Uri
 */

/* eslint-disable no-use-before-define */

( function ( mw, $ ) {
	var parser, properties;

	/**
	 * Function that's useful when constructing the URI string -- we frequently encounter the pattern
	 * of having to add something to the URI as we go, but only if it's present, and to include a
	 * character before or after if so.
	 *
	 * @private
	 * @static
	 * @param {string|undefined} pre To prepend
	 * @param {string} val To include
	 * @param {string} post To append
	 * @param {boolean} raw If true, val will not be encoded
	 * @return {string} Result
	 */
	function cat( pre, val, post, raw ) {
		if ( val === undefined || val === null || val === '' ) {
			return '';
		}

		return pre + ( raw ? val : mw.Uri.encode( val ) ) + post;
	}

	/**
	 * Regular expressions to parse many common URIs.
	 *
	 * As they are gnarly, they have been moved to separate files to allow us to format them in the
	 * 'extended' regular expression format (which JavaScript normally doesn't support). The subset of
	 * features handled is minimal, but just the free whitespace gives us a lot.
	 *
	 * @private
	 * @static
	 * @property {Object} parser
	 */
	parser = {
    strict: "^\n(?:(?<protocol>[^:/?#]+):)?\n(?://(?:\n\t(?:\n\t\t(?<user>[^:@/?#]*)\n\t\t(?::(?<password>[^:@/?#]*))?\n\t)?@)?\n\t(?<host>[^:/?#]*)\n\t(?::(?<port>\\d*))?\n)?\n(?<path>(?:[^?#/]*/)*[^?#]*)\n(?:\\?(?<query>[^#]*))?\n(?:\\#(?<fragment>.*))?\n",
    loose: /^(?:(?![^:@]+:[^:@\/]*@)([^:\/?#.]+):)?(?:\/\/)?(?:(?:([^:@\/?#]*)(?::([^:@\/?#]*))?)?@)?([^:\/?#]*)(?::(\\d*))?((?:\/(?:[^?#](?![^?#\/]*\\.[^?#\/.]+(?:[?#]|$)))*\/?)?[^?#/]*)(?:\\?([^#]*))?(?:\\#(.*))?/
    //loose: /^(?:(?![^:@]+:[^:@\/]*@)(?<protocol>[^:\/?#.]+):)?(?:\/\/)?(?:(?:(?<user>[^:@\/?#]*)(?::(?<password>[^:@\/?#]*))?)?@)?(?<host>[^:\/?#]*)(?::(?<port>\\d*))?((?:\/(?:[^?#](?![^?#\/]*\\.[^?#\/.]+(?:[?#]|$)))*\/?)?[^?#/]*)(?:\\?(?<query>[^#]*))?(?:\\#(?<fragment>.*))?/
    //loose: /^\n(?:\n\t(?![^:@]+:[^:@/]*@)\n\t(?<protocol>[^:/?#.]+):\n)?\n(?://)?\n(?:(?:\n\t(?<user>[^:@/?#]*)\n\t(?::(?<password>[^:@/?#]*))?\n)?@)?\n(?<host>[^:/?#]*)\n(?::(?<port>\\d*))?\n(\n\t(?:/\n\t\t(?:[^?#]\n\t\t\t(?![^?#/]*\\.[^?#/.]+(?:[?#]|$))\n\t\t)*/?\n\t)?\n\t[^?#/]*\n)\n(?:\\?(?<query>[^#]*))?\n(?:\\#(?<fragment>.*))?\n/
    //"strict.regexp": "^\n(?:(?<protocol>[^:/?#]+):)?\n(?://(?:\n\t(?:\n\t\t(?<user>[^:@/?#]*)\n\t\t(?::(?<password>[^:@/?#]*))?\n\t)?@)?\n\t(?<host>[^:/?#]*)\n\t(?::(?<port>\\d*))?\n)?\n(?<path>(?:[^?#/]*/)*[^?#]*)\n(?:\\?(?<query>[^#]*))?\n(?:\\#(?<fragment>.*))?\n",
    //"loose.regexp": "^\n(?:\n\t(?![^:@]+:[^:@/]*@)\n\t(?<protocol>[^:/?#.]+):\n)?\n(?://)?\n(?:(?:\n\t(?<user>[^:@/?#]*)\n\t(?::(?<password>[^:@/?#]*))?\n)?@)?\n(?<host>[^:/?#]*)\n(?::(?<port>\\d*))?\n(\n\t(?:/\n\t\t(?:[^?#]\n\t\t\t(?![^?#/]*\\.[^?#/.]+(?:[?#]|$))\n\t\t)*/?\n\t)?\n\t[^?#/]*\n)\n(?:\\?(?<query>[^#]*))?\n(?:\\#(?<fragment>.*))?\n"
		//strict: mw.template.get( 'mediawiki.Uri', 'strict.regexp' ).render(),
		//loose: mw.template.get( 'mediawiki.Uri', 'loose.regexp' ).render()
	};

	/**
	 * The order here matches the order of captured matches in the `parser` property regexes.
	 *
	 * @private
	 * @static
	 * @property {Array} properties
	 */
	properties = [
		'protocol',
		'user',
		'password',
		'host',
		'port',
		'path',
		'query',
		'fragment'
	];

	/**
	 * @property {string} protocol For example `http` (always present)
	 */
	/**
	 * @property {string|undefined} user For example `usr`
	 */
	/**
	 * @property {string|undefined} password For example `pwd`
	 */
	/**
	 * @property {string} host For example `www.example.com` (always present)
	 */
	/**
	 * @property {string|undefined} port For example `81`
	 */
	/**
	 * @property {string} path For example `/dir/dir.2/index.htm` (always present)
	 */
	/**
	 * @property {Object} query For example `{ a: '0', b: '', c: 'value' }` (always present)
	 */
	/**
	 * @property {string|undefined} fragment For example `top`
	 */

	/**
	 * A factory method to create a Uri class with a default location to resolve relative URLs
	 * against (including protocol-relative URLs).
	 *
	 * @method
	 * @param {string|Function} documentLocation A full url, or function returning one.
	 *  If passed a function, the return value may change over time and this will be honoured. (T74334)
	 * @member mw
	 * @return {Function} Uri class
	 */
	mw.UriRelative = function ( documentLocation ) {
		var getDefaultUri = ( function () {
			// Cache
			var href, uri;

			return function () {
				var hrefCur = typeof documentLocation === 'string' ? documentLocation : documentLocation();
				if ( href === hrefCur ) {
					return uri;
				}
				href = hrefCur;
				uri = new Uri( href );
				return uri;
			};
		}() );

		/**
		 * Construct a new URI object. Throws error if arguments are illegal/impossible, or
		 * otherwise don't parse.
		 *
		 * @class mw.Uri
		 * @constructor
		 * @param {Object|string} [uri] URI string, or an Object with appropriate properties (especially
		 *  another URI object to clone). Object must have non-blank `protocol`, `host`, and `path`
		 *  properties. If omitted (or set to `undefined`, `null` or empty string), then an object
		 *  will be created for the default `uri` of this constructor (`location.href` for mw.Uri,
		 *  other values for other instances -- see mw.UriRelative for details).
		 * @param {Object|boolean} [options] Object with options, or (backwards compatibility) a boolean
		 *  for strictMode
		 * @param {boolean} [options.strictMode=false] Trigger strict mode parsing of the url.
		 * @param {boolean} [options.overrideKeys=false] Whether to let duplicate query parameters
		 *  override each other (`true`) or automagically convert them to an array (`false`).
		 */
		function Uri( uri, options ) {
			var prop, hrefCur,
				hasOptions = ( options !== undefined ),
				defaultUri = getDefaultUri();

			options = typeof options === 'object' ? options : { strictMode: !!options };
			options = $.extend( {
				strictMode: false,
				overrideKeys: false
			}, options );

			if ( uri !== undefined && uri !== null && uri !== '' ) {
				if ( typeof uri === 'string' ) {
					this.parse( uri, options );
				} else if ( typeof uri === 'object' ) {
					// Copy data over from existing URI object
					for ( prop in uri ) {
						// Only copy direct properties, not inherited ones
						if ( uri.hasOwnProperty( prop ) ) {
							// Deep copy object properties
							if ( Array.isArray( uri[ prop ] ) || $.isPlainObject( uri[ prop ] ) ) {
								this[ prop ] = $.extend( true, {}, uri[ prop ] );
							} else {
								this[ prop ] = uri[ prop ];
							}
						}
					}
					if ( !this.query ) {
						this.query = {};
					}
				}
			} else if ( hasOptions ) {
				// We didn't get a URI in the constructor, but we got options.
				hrefCur = typeof documentLocation === 'string' ? documentLocation : documentLocation();
				this.parse( hrefCur, options );
			} else {
				// We didn't get a URI or options in the constructor, use the default instance.
				return defaultUri.clone();
			}

			// protocol-relative URLs
			if ( !this.protocol ) {
				this.protocol = defaultUri.protocol;
			}
			// No host given:
			if ( !this.host ) {
				this.host = defaultUri.host;
				// port ?
				if ( !this.port ) {
					this.port = defaultUri.port;
				}
			}
			if ( this.path && this.path[ 0 ] !== '/' ) {
				// A real relative URL, relative to defaultUri.path. We can't really handle that since we cannot
				// figure out whether the last path component of defaultUri.path is a directory or a file.
				throw new Error( 'Bad constructor arguments' );
			}
			if ( !( this.protocol && this.host && this.path ) ) {
				throw new Error( 'Bad constructor arguments' );
			}
		}

		/**
		 * Encode a value for inclusion in a url.
		 *
		 * Standard encodeURIComponent, with extra stuff to make all browsers work similarly and more
		 * compliant with RFC 3986. Similar to rawurlencode from PHP and our JS library
		 * mw.util.rawurlencode, except this also replaces spaces with `+`.
		 *
		 * @static
		 * @param {string} s String to encode
		 * @return {string} Encoded string for URI
		 */
		Uri.encode = function ( s ) {
			return encodeURIComponent( s )
				.replace( /!/g, '%21' ).replace( /'/g, '%27' ).replace( /\(/g, '%28' )
				.replace( /\)/g, '%29' ).replace( /\*/g, '%2A' )
				.replace( /%20/g, '+' );
		};

		/**
		 * Decode a url encoded value.
		 *
		 * Reversed #encode. Standard decodeURIComponent, with addition of replacing
		 * `+` with a space.
		 *
		 * @static
		 * @param {string} s String to decode
		 * @return {string} Decoded string
		 */
		Uri.decode = function ( s ) {
			return decodeURIComponent( s.replace( /\+/g, '%20' ) );
		};

		Uri.prototype = {

			/**
			 * Parse a string and set our properties accordingly.
			 *
			 * @private
			 * @param {string} str URI, see constructor.
			 * @param {Object} options See constructor.
			 */
			parse: function ( str, options ) {
				var q, matches,
					uri = this,
					hasOwn = Object.prototype.hasOwnProperty;

				// Apply parser regex and set all properties based on the result
				//matches = parser[ options.strictMode ? 'strict' : 'loose' ].exec( str );
				matches = str.match(parser[ options.strictMode ? 'strict' : 'loose' ]);
				properties.forEach( function ( property, i ) {
					uri[ property ] = matches[ i + 1 ];
				} );

				// uri.query starts out as the query string; we will parse it into key-val pairs then make
				// that object the "query" property.
				// we overwrite query in uri way to make cloning easier, it can use the same list of properties.
				q = {};
				// using replace to iterate over a string
				if ( uri.query ) {
					uri.query.replace( /(?:^|&)([^&=]*)(?:(=)([^&]*))?/g, function ( $0, $1, $2, $3 ) {
						var k, v;
						if ( $1 ) {
							k = Uri.decode( $1 );
							v = ( $2 === '' || $2 === undefined ) ? null : Uri.decode( $3 );

							// If overrideKeys, always (re)set top level value.
							// If not overrideKeys but this key wasn't set before, then we set it as well.
							if ( options.overrideKeys || !hasOwn.call( q, k ) ) {
								q[ k ] = v;

							// Use arrays if overrideKeys is false and key was already seen before
							} else {
								// Once before, still a string, turn into an array
								if ( typeof q[ k ] === 'string' ) {
									q[ k ] = [ q[ k ] ];
								}
								// Add to the array
								if ( Array.isArray( q[ k ] ) ) {
									q[ k ].push( v );
								}
							}
						}
					} );
				}
				uri.query = q;

				// Decode uri.fragment, otherwise it gets double-encoded when serializing
				if ( uri.fragment !== undefined ) {
					uri.fragment = Uri.decode( uri.fragment );
				}
			},

			/**
			 * Get user and password section of a URI.
			 *
			 * @return {string}
			 */
			getUserInfo: function () {
				return cat( '', this.user, cat( ':', this.password, '' ) );
			},

			/**
			 * Get host and port section of a URI.
			 *
			 * @return {string}
			 */
			getHostPort: function () {
				return this.host + cat( ':', this.port, '' );
			},

			/**
			 * Get the userInfo, host and port section of the URI.
			 *
			 * In most real-world URLs this is simply the hostname, but the definition of 'authority' section is more general.
			 *
			 * @return {string}
			 */
			getAuthority: function () {
				return cat( '', this.getUserInfo(), '@' ) + this.getHostPort();
			},

			/**
			 * Get the query arguments of the URL, encoded into a string.
			 *
			 * Does not preserve the original order of arguments passed in the URI. Does handle escaping.
			 *
			 * @return {string}
			 */
			getQueryString: function () {
				var args = [];
				$.each( this.query, function ( key, val ) {
					var k = Uri.encode( key ),
						vals = Array.isArray( val ) ? val : [ val ];
					vals.forEach( function ( v ) {
						if ( v === null ) {
							args.push( k );
						} else if ( k === 'title' ) {
							args.push( k + '=' + mw.util.wikiUrlencode( v ) );
						} else {
							args.push( k + '=' + Uri.encode( v ) );
						}
					} );
				} );
				return args.join( '&' );
			},

			/**
			 * Get everything after the authority section of the URI.
			 *
			 * @return {string}
			 */
			getRelativePath: function () {
				return this.path + cat( '?', this.getQueryString(), '', true ) + cat( '#', this.fragment, '' );
			},

			/**
			 * Get the entire URI string.
			 *
			 * May not be precisely the same as input due to order of query arguments.
			 *
			 * @return {string} The URI string
			 */
			toString: function () {
				return this.protocol + '://' + this.getAuthority() + this.getRelativePath();
			},

			/**
			 * Clone this URI
			 *
			 * @return {Object} New URI object with same properties
			 */
			clone: function () {
				return new Uri( this );
			},

			/**
			 * Extend the query section of the URI with new parameters.
			 *
			 * @param {Object} parameters Query parameters to add to ours (or to override ours with) as an
			 *  object
			 * @return {Object} This URI object
			 */
			extend: function ( parameters ) {
				$.extend( this.query, parameters );
				return this;
			}
		};

		return Uri;
	};

	// Default to the current browsing location (for relative URLs).
	mw.Uri = mw.UriRelative( function () {
		return location.href;
	} );

}( mediaWiki, jQuery ) );

mw.message = function ( key ) {
			var parameters = xowa.cfg.get(key);
			return new Message( null, key, [parameters] );
		}

function fakenewTitle(title) {
		var t = Object.create( mw.Title.prototype ); //new Object();
		var n = title.indexOf("#");
		if (n >= 0) {
			t.title = title.substr(0, n);
			t.fragment = title.substr(n+1);
		} else {
			t.title = title;
			t.fragment = null;
		}
		t.namespace = 0;
		t.ext = null;
		return t;
}

mw.Title = function (txt) {
	return fakenewTitle(txt);
}
mw.Title.newFromText = fakenewTitle;
mw.Title.prototype.getNamespaceId = function () {
			return this.namespace;
		};
mw.Title.prototype.getMainText = function () {
			return this.title;
		};
mw.Title.prototype.getPrefixedDb = function () {
			return this.title;
		};
mw.Title.prototype.getUrl = function () {
			return this.title;
		};
mw.Title.prototype.getFragment = function () {
			return this.fragment;
		};

mw.now = mwNow;

window.xowa_global_values['wgContentNamespaces'] = [
	0
];
