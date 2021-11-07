
from pygments import highlight
from pygments.lexers import get_lexer_by_name
from pygments.formatters import HtmlFormatter

import logging
import re
import base64

def checkval(key, val):
    global dir, lang, showLines, style, klass, htmlAttribs, opts
    #print(key, val)
    key = key.lower()
    if key == 'lang':
        lang = val.lower()
        try:
            lang = compat[lang]
        except:
            pass
    elif key == 'inline':
        opts["nowrap"] = True
    elif key == 'enclose':
        if val != "none":
            logging.info("not none")
        else:
            opts["nowrap"] = True
    elif key == 'style':
        htmlAttribs['style'] = val
    elif key == 'id':
        htmlAttribs['id'] = val
    elif key == 'highlight':
        opts["hl_lines"] = parseHighlightLines( val )
    elif key == 'line':
        opts["linenos"] = 'inline'
        showLines = True
    elif key == 'start':
        try:
            opts["linenostart"] = int(val) # ignore invalid integers
        except:
            pass
    elif key == 'linelinks':
        opts["linespans"] = val
##    else:
##        zzz()

def parseHighlightLines( lineSpec ):
    lines = []
    values = lineSpec.split(",")
    for val in values:
        vals = val.strip().split("-")
        v = int(vals[0])
        lines.append(str(v))
        if len(vals) > 1:
            for i in range(v + 1, int(vals[1]) + 1):
                lines.append(str(i))
    return ' '.join(lines)

def state0():
    global c, key, i, ofs, state, delim, xatrs
    if c == '=':
        key = xatrs[ofs:i]
        ofs = i + 1
        state = state1
    elif c == ' ':
        checkval(xatrs[ofs:i], "")
        ofs = i + 1
        state = state4
def state1():
    global c, key, i, ofs, state, delim, xatrs
    if c == '"' or c == "'":
        delim = c
        ofs += 1
        state = state2
    else:
        state = state3
def state2():
    global c, key, i, ofs, state, delim, xatrs
    if c == delim:
        checkval(key, xatrs[ofs:i])
        state = state4
def state3():
    global c, key, i, ofs, state, delim, xatrs
    if c == ' ':
        checkval(key, xatrs[ofs:i])
        state = state4
def state4():
    global c, key, i, ofs, state, delim, xatrs
    if c != ' ':
        state = state0
        ofs = i

def syntaxhighlight(atrs, code):
    global c, key, i, ofs, state, delim, xatrs
    xatrs = atrs
    global dir, lang, showLines, style, klass, htmlAttribs, opts
    #logging.info(atrs)
    opts = {"encoding":"utf-8", "cssclass":"mw-highlight", "nowrap":False}
    dir = None
    lang = None
    showLines = False
    style = None
    klass = None
    htmlAttribs = {}
    ofs = 0
    state = state0
    key = ""
    delim = ""
    for i, c in enumerate(atrs):
        state()
    if state != state4:
        if state == state0:
            checkval(atrs[ofs:], "")
        elif state == state2 or state == state3:
            checkval(key, atrs[ofs:])
        else:
            checkval(key, "")

    lopts = {"stripall":True}
    if lang == 'php' and code.find('<?php') < 0:
        lopts['startinline'] = True

    #print(lang, opts)

    try:
        lexer = get_lexer_by_name(lang, **lopts)
    except:
        lexer = None
    if lexer == None:
        lang = "text" # not sure what the default is - check
        lexer = get_lexer_by_name(lang, **lopts)
    formatter = HtmlFormatter(**opts)
    output = highlight(code, lexer, formatter)

    if showLines:
        output = re.sub(rb'<span class="linenos">\s*([^<]*)\s*</span>', rb'<span class="linenos" data-line="\1"></span>', output)
    if dir == None or dir != "rtl":
        dir = "ltr"

    # Build class list
    classList = []
    if klass:
        classList.append(klass)

    classList.append("mw-highlight")
    if lang:
        classList.append("mw-highlight-lang-%s" % lang)
    classList.append("mw-content-%s" % dir)
    if showLines:
        classList.append("mw-highlight-lines")

    htmlAttribs['class'] = ' '.join(classList)
    htmlAttribs['dir'] = dir

    if opts["nowrap"]:
        output = output.strip()
        output = output.replace(b"\n", b" ")
        output = rawElement( b'code', htmlAttribs, output );
    else:
        output = output[26:-7]
        output = rawElement( b'div', htmlAttribs, output );
    return output

def rawElement(tag, atrs, output):
    html = [b'<%s ' % tag]
    for key, val in atrs.items():
        txt = '%s="%s" ' % (key, val)
        html.append(txt.encode("utf-8"))
    html.append(b'>')
    html.append(output)
    html.append(b'</%s>' % tag)
    return b''.join(html)

compat = {
    'arm'       : 'asm',
    '6502acme'  : 'asm',
    '6502tasm'  : 'asm',
    '6502kickass' : 'asm',
    '68000devpac' : 'asm',
    'dcpu16'    : 'asm',
    'm68k'      : 'asm',
    'mmix'      : 'nasm',
    'mpasm'     : 'asm',
    'pic16'     : 'asm',
    'z80'       : 'asm',

    # BASIC
    'xbasic'    : 'basic',
    'thinbasic' : 'basic',
    'sdlbasic'  : 'basic',
    'purebasic' : 'basic',
    'mapbasic'  : 'basic',
    'locobasic' : 'basic',
    'gwbasic'   : 'basic',
    'freebasic' : 'basic',
    'basic4gl'  : 'basic',
    'zxbasic'   : 'basic',
    'gambas'    : 'basic',
    'oobas'     : 'basic',
    'bascomavr' : 'basic',

    # C / C++
    'c_loadrunner' : 'c',
    'c_mac'        : 'c',
    'c_winapi'     : 'c',
    'upc'          : 'c',
    'cpp-qt'       : 'cpp',
    'cpp-winapi'   : 'cpp',
    'urbi'         : 'cpp',

    # HTML
    'html4strict' : 'html',
    'html5'       : 'html',

    # JavaScript
    'jquery'     : 'javascript',
    'ecmascript' : 'javascript',

    # Microsoft
    'vb'           : 'vbnet',
    'asp'          : 'aspx-vb',
    'visualfoxpro' : 'foxpro',
    'dos'          : 'bat',
    'visualprolog' : 'prolog',
    'reg'          : 'registry',

    # Miscellaneous
    'cadlisp'   : 'lisp',
    'java5'     : 'java',
    'php-brief' : 'php',
    'povray'    : 'pov',
    'pys60'     : 'python',
    'rails'     : 'ruby',
    'rpmspec'   : 'spec',
    'rsplus'    : 'splus',
    'gettext'   : 'pot',

    # ML
    'ocaml-brief' : 'ocaml',
    'standardml'  : 'sml',

    # Modula 2
    'modula3' : 'modula2',
    'oberon2' : 'modula2',

    # SQL
    'dcl'      : 'sql',
    'plsql'    : 'sql',
    'oracle11' : 'sql',
    'oracle8'  : 'sql',

    # REXX
    'oorexx'  : 'rexx',
    'netrexx' : 'rexx',

    # xpp is basically Java
    'xpp' : 'java',

    # apt
    'apt_sources' : 'debsources'
}

sizes = {b'PHN5bnRheGhpZ2hsaWdo':16,
         b'PHNvdXJjZ':7}

def syntaxlight(data):
    ofs = 0
    html = []
    startre = re.compile(rb"(PHN5bnRheGhpZ2hsaWdo|PHNvdXJjZ)") # <syntaxhighlight | <source
    while 1:
        mt = startre.search(data, ofs)
        if mt == None:
            break
        s1 = mt.start()
        html.append(data[ofs:s1])
        s3 = data.find(b'$$', s1)
        s4 = data.find(b'%24%24', s1)
        if s4 > 0 and s4 < s3:
            # eg en.wikibooks.org/wiki/Pascal_Programming/Records
            ofs = s4 + 6
            continue
        tag64 = data[s1:s3]
        if tag64[-2] == 0x2e: # ".."?
            tag64 = tag64[:-2] + b"=="
        elif tag64[-1] == 0x2e: # '.'?
            tag64 = tag64[:-1] + b"="
        tag = base64.b64decode(tag64)
        size = sizes[mt.group(1)]
        s2 = tag.find(b'>')
        if tag[size] == 32: # ' '
            atrs = tag[size+1:s2].decode("utf-8")
        else:
            atrs = ''
        code = tag[s2+1:-size - 2].decode("utf-8")
        code = code.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&")
        html.append(syntaxhighlight(atrs, code))
        ofs = s3 + 2
        
    html.append(data[ofs:])
    
    return b''.join(html)

###syntaxhighlight('lang="asm" inline=""', "check")
##syntaxhighlight('lang="java" line="" start="1" highlight="24-34"', "check")
##syntaxhighlight('lang="asm', "check")
##syntaxhighlight('lang="asm', "check")
##print(syntaxhighlight('lang="html4strict" line="" start="1"', "check").decode("utf-8"))
##zzz()
if __name__ == '__main__':
    import sys
    input = "d:/des/xowa_x/html.htm"
    output = "d:/des/xowa_x/html_syn.htm"
    input = "d:/des/xowa_x/tmp/src.file0"
    output = "d:/des/xowa_x/tmp/trg.file0"
    domain = 'nodom'
    page_name = '*n/a*'

    if len(sys.argv) == 5:
        input = sys.argv[1]
        output = sys.argv[2]
        domain = sys.argv[3]
        page_name = sys.argv[4]
    
    logging.basicConfig(filename=input+'.log', level=logging.INFO, format='%(asctime)s %(levelname)s:%(message)s')
    logging.info("%s/wiki/%s" % (domain, page_name))

    f = open(input, "rb")
    data = f.read()
    f.close()
    result = syntaxlight(data)
    if result.find(b'$$') > 0:
        logging.info("possible problem")
    f = open(output, "wb")
    f.write(result)
    f.close()
