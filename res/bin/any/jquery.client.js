    (function() {
        var profileCache = {};
        $.client = {
            profile: function(nav) {
                if (nav === undefined) {
                    nav = window.navigator;
                }
                if (profileCache[nav.userAgent + '|' + nav.platform] !== undefined) {
                    return profileCache[nav.userAgent + '|' + nav.platform];
                }
                var versionNumber, key = nav.userAgent + '|' + nav.platform, uk = 'unknown', x = 'x', wildUserAgents = ['Opera', 'Navigator', 'Minefield', 'KHTML', 'Chrome', 'PLAYSTATION 3', 'Iceweasel'], userAgentTranslations = [[/(Firefox|MSIE|KHTML,?\slike\sGecko|Konqueror)/, ''], ['Chrome Safari', 'Chrome'], ['KHTML', 'Konqueror'], ['Minefield', 'Firefox'], ['Navigator', 'Netscape'], ['PLAYSTATION 3', 'PS3']], versionPrefixes = ['camino', 'chrome', 'firefox', 'iceweasel', 'netscape', 'netscape6', 'opera', 'version', 'konqueror', 'lynx', 'msie', 'safari', 'ps3', 'android'], versionSuffix = '(\\/|\\;?\\s|)([a-z0-9\\.\\+]*?)(\\;|dev|rel|\\)|\\s|$)', names = ['camino', 'chrome', 'firefox', 'iceweasel', 'netscape', 'konqueror', 'lynx', 'msie', 'opera', 'safari', 'ipod', 'iphone', 'blackberry', 'ps3', 'rekonq', 'android'], nameTranslations = [], layouts = ['gecko', 'konqueror', 'msie', 'trident', 'edge', 'opera', 'webkit'], layoutTranslations = [['konqueror', 'khtml'], ['msie', 'trident'], ['opera', 'presto']], layoutVersions = ['applewebkit', 'gecko', 'trident', 'edge'], platforms = ['win', 'wow64', 'mac', 'linux', 'sunos', 'solaris', 'iphone'], platformTranslations = [['sunos', 'solaris'], ['wow64', 'win']], translate = function(source, translations) {
                    var i;
                    for (i = 0; i < translations.length; i++) {
                        source = source.replace(translations[i][0], translations[i][1]);
                    }
                    return source;
                }, ua = nav.userAgent, match, name = uk, layout = uk, layoutversion = uk, platform = uk, version = x;
                if ((match = new RegExp('(' + wildUserAgents.join('|') + ')').exec(ua))) {
                    ua = translate(ua, userAgentTranslations);
                }
                ua = ua.toLowerCase();
                if (ua.match(/android/) && ua.match(/firefox/)) {
                    ua = ua.replace(new RegExp('android' + versionSuffix), '');
                }
                if ((match = new RegExp('(' + names.join('|') + ')').exec(ua))) {
                    name = translate(match[1], nameTranslations);
                }
                if ((match = new RegExp('(' + layouts.join('|') + ')').exec(ua))) {
                    layout = translate(match[1], layoutTranslations);
                }
                if ((match = new RegExp('(' + layoutVersions.join('|') + ')\\/(\\d+)').exec(ua))) {
                    layoutversion = parseInt(match[2], 10);
                }
                if ((match = new RegExp('(' + platforms.join('|') + ')').exec(nav.platform.toLowerCase()))) {
                    platform = translate(match[1], platformTranslations);
                }
                if ((match = new RegExp('(' + versionPrefixes.join('|') + ')' + versionSuffix).exec(ua))) {
                    version = match[3];
                }
                if (name === 'safari' && version > 400) {
                    version = '2.0';
                }
                if (name === 'opera' && version >= 9.8) {
                    match = ua.match(/\bversion\/([0-9.]*)/);
                    if (match && match[1]) {
                        version = match[1];
                    } else {
                        version = '10';
                    }
                }
                if (name === 'chrome' && (match = ua.match(/\bopr\/([0-9.]*)/))) {
                    if (match[1]) {
                        name = 'opera';
                        version = match[1];
                    }
                }
                if (layout === 'trident' && layoutversion >= 7 && (match = ua.match(/\brv[ :/]([0-9.]*)/))) {
                    if (match[1]) {
                        name = 'msie';
                        version = match[1];
                    }
                }
                if (name === 'chrome' && (match = ua.match(/\bedge\/([0-9.]*)/))) {
                    name = 'edge';
                    version = match[1];
                    layout = 'edge';
                    layoutversion = parseInt(match[1], 10);
                }
                if ((match = ua.match(/\bsilk\/([0-9.\-_]*)/))) {
                    if (match[1]) {
                        name = 'silk';
                        version = match[1];
                    }
                }
                versionNumber = parseFloat(version, 10) || 0.0;
                profileCache[key] = {
                    name: name,
                    layout: layout,
                    layoutVersion: layoutversion,
                    platform: platform,
                    version: version,
                    versionBase: (version !== x ? Math.floor(versionNumber).toString() : x),
                    versionNumber: versionNumber
                };
                return profileCache[key];
            },
            test: function(map, profile, exactMatchOnly) {
                var conditions, dir, i, op, val, j, pieceVersion, pieceVal, compare;
                profile = $.isPlainObject(profile) ? profile : $.client.profile();
                if (map.ltr && map.rtl) {
                    dir = $(document.body).is('.rtl') ? 'rtl' : 'ltr';
                    map = map[dir];
                }
                if (typeof map !== 'object' || map[profile.name] === undefined) {
                    return !exactMatchOnly;
                }
                conditions = map[profile.name];
                if (conditions === false) {
                    return false;
                }
                if (conditions === null) {
                    return true;
                }
                for (i = 0; i < conditions.length; i++) {
                    op = conditions[i][0];
                    val = conditions[i][1];
                    if (typeof val === 'string') {
                        pieceVersion = profile.version.toString().split('.');
                        pieceVal = val.split('.');
                        while (pieceVersion.length < pieceVal.length) {
                            pieceVersion.push('0');
                        }
                        while (pieceVal.length < pieceVersion.length) {
                            pieceVal.push('0');
                        }
                        compare = 0;
                        for (j = 0; j < pieceVersion.length; j++) {
                            if (Number(pieceVersion[j]) < Number(pieceVal[j])) {
                                compare = -1;
                                break;
                            } else if (Number(pieceVersion[j]) > Number(pieceVal[j])) {
                                compare = 1;
                                break;
                            }
                        }
                        if (!(eval(String(compare + op + '0')))) {
                            return false;
                        }
                    } else if (typeof val === 'number') {
                        if (!(eval('profile.versionNumber' + op + val))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        };
    }());
