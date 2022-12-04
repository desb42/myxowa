(function(){function r(e,n,t){function o(i,f){if(!n[i]){if(!e[i]){var c="function"==typeof require&&require;if(!f&&c)return c(i,!0);if(u)return u(i,!0);var a=new Error("Cannot find module '"+i+"'");throw a.code="MODULE_NOT_FOUND",a}var p=n[i]={exports:{}};e[i][0].call(p.exports,function(r){var n=e[i][1][r];return o(n||r)},p,p.exports,r,e,n,t)}return n[i].exports}for(var u="function"==typeof require&&require,i=0;i<t.length;i++)o(t[i]);return o}return r})()({1:[function(require,module,exports){
const texvcjs = require('./');
const result = texvcjs.check("\\widehat \\mathbf{r}_{12}=\\frac{\\mathbf{r}_{12}}{|\\mathbf{r}_{12}|}", {debug: false, usemathrm: false, usemhchem: false});
if (result.status === '+') {
    console.log(result.status + (result.output || ''));
} else if (result.status === 'F' || program.verbose) {
    console.log(result.status + (result.details || ''));
} else {
    console.log(result.status);
}

},{"./":4}],2:[function(require,module,exports){
// AST type declarations
'use strict';

const typecheck = module.exports.typecheck = function (val, type, self) {
    switch (type) {
        case 'string':
            return typeof (val) === type;
        case 'self':
            return self && self.contains(val);
    }
    if (Array.isArray(type)) {
        return Array.isArray(val) && val.every(function (elem) {
            return typecheck(elem, type[0], self);
        });
    }
    return type.contains(val);
};
const type2str = function (type) {
    if (typeof (type) === 'string') {
        return type;
    }
    if (Array.isArray(type)) {
        return '[' + type2str(type[0]) + ']';
    }
    return type.name;
};

// "Enum" helper
// vaguely based on:
// https://github.com/rauschma/enums/blob/master/enums.js
const Enum = function (name, fields, proto) {
    proto = proto || {};
    // Non-enumerable properties 'name' and 'prototype'
    Object.defineProperty(this, 'name', { value: name });
    Object.defineProperty(this, 'prototype', { value: proto });
    Object.keys(fields).forEach(function (fname) {
        const args = fields[fname].args || [];
        const self = this;
        this[fname] = function EnumField() {
            if (!(this instanceof EnumField)) {
                const o = Object.create(EnumField.prototype);
                o.constructor = EnumField;
                EnumField.apply(o, arguments);
                return o;
            }
            this.name = fname;
            console.assert(arguments.length === args.length,
                'Wrong # of args for ' + name + '.' + fname);
            for (let i = 0; i < args.length; i++) {
                console.assert(typecheck(arguments[i], args[i], self),
                    'Argument ' + i + ' of ' + name + '.' + fname +
                    ' should be ' + type2str(args[i]));
                this[i] = arguments[i];
            }
            this.length = args.length;
        };
        this[fname].prototype = Object.create(proto);
        this[fname].prototype.toString = function () {
            const stringify = function (type, val) {
                if (type === 'string') {
                    return JSON.stringify(val);
                } else if (Array.isArray(type)) {
                    return '[' + val.map(function (v) {
                        return stringify(type[0], v);
                    }).join(',') + ']';
                }
                return val.toString();
            };
            return fname + '(' + args.map(function (type, i) {
                return stringify(type, this[i]);
            }.bind(this)).join(',') + ')';
        };
    }.bind(this));
};
Enum.prototype.contains = function (sym) {
    // eslint-disable-next-line no-prototype-builtins
    return sym.name && this.hasOwnProperty(sym.name) &&
        sym instanceof this[sym.name];
};
Enum.prototype.defineVisitor = function (visitorName, o, numArgs) {
    const self = this;
    numArgs = numArgs || 0;
    console.assert(Object.keys(o).length === Object.keys(self).length,
        'Missing cases in ' + self.name + '. Expected:\n' +
        Object.keys(self).sort() + ' but got: \n' +
        Object.keys(o).sort());
    Object.keys(o).forEach(function (fname) {
        self[fname].prototype[visitorName] = function () {
            const args = [];
            for (let i = 0; i < numArgs; i++) {
                args.push(arguments[i]);
            }
            args.push.apply(args, this);
            return o[fname].apply(this, args);
        };
    });
};

// Actual AST starts here.

const FontForce = module.exports.FontForce = new Enum('FontForce', {
    IT: {},
    RM: {}
});

const RenderT = module.exports.RenderT = new Enum('RenderT', {
    TEX_ONLY: { args: ['string'] }
});

const Tex = module.exports.Tex = new Enum('Tex', {
    LITERAL: { args: [RenderT] }, // contents
    CURLY: { args: [['self']] }, // expr
    DOLLAR: { args: [['self']] }, // expr
    FQ: { args: ['self', 'self', 'self'] }, // base, down, up
    DQ: { args: ['self', 'self'] }, // base, down
    UQ: { args: ['self', 'self'] }, // base, up
    FQN: { args: ['self', 'self'] }, // down, up (no base)
    DQN: { args: ['self'] }, // down (no base)
    UQN: { args: ['self'] }, // up (no base)
    LR: { args: [RenderT, RenderT, ['self']] }, // left, right, expr
    BOX: { args: ['string', 'string'] }, // name, contents
    BIG: { args: ['string', RenderT] }, // name, contents
    FUN1: { args: ['string', 'self'] }, // name, expr
    MHCHEM: { args: ['string', 'self'] }, // name, expr
    CHEM_WORD: { args: ['self', 'self'] }, // name, expr
    CHEM_FUN2u: { args: ['string', 'self', 'self'] }, // name, expr
    FUN1nb: { args: ['string', 'self'] }, // name, expr
    FUN2: { args: ['string', 'self', 'self'] },
    FUN2nb: { args: ['string', 'self', 'self'] },
    INFIX: { args: ['string', ['self'], ['self']] },
    FUN2sq: { args: ['string', 'self', 'self'] },
    MATRIX: { args: ['string', [[['self']]]] },
    DECLh: { args: ['string', FontForce, ['self']] }
});

// Linked List of Tex, useful for efficient "append to head" operations.
const nil = {};
const LList = module.exports.LList = function LList(head, tail) {
    if (!(this instanceof LList)) {
        return new LList(head, tail);
    }
    if (head === nil && tail === nil) {
        return; /* empty list singleton */
    }
    if (!tail) {
        tail = LList.EMPTY;
    }
    console.assert(typecheck(head, Tex));
    console.assert(tail instanceof LList);
    this.head = head;
    this.tail = tail;
};
LList.EMPTY = new LList(nil, nil);
LList.prototype.toArray = function () {
    console.assert(this instanceof LList, 'not a LList');
    const arr = [];
    for (let l = this; l !== LList.EMPTY; l = l.tail) {
        arr.push(l.head);
    }
    return arr;
};

},{}],3:[function(require,module,exports){
// Useful AST methods.
// "contains_func": returns true iff the given AST contains a reference to
//                  the specified function(s).
'use strict';

const ast = require('./ast');

// like Array#some, but returns the non-falsy value, rather than the
// boolean constant `true`.
const some = function (array, testfunc) {
    let i, b;
    for (i = 0; i < array.length; i++) {
        b = testfunc(array[i], i, array);
        if (b) {
            return b;
        } // return the non-falsy value
    }
    return false;
};

// Matches a string against an string, array, or set target.
// Returns the matching value, or `false`.
const match = function (target, str) {
    if (Array.isArray(target)) {
        return some(target, function (t) {
            return match(t, str);
        });
    }
    if (typeof (target) === 'string') {
        return target === str ? str : false;
    }
    return target[str] ? str : false;
};

// Check if any of the array of AST nodes contains `target`.
const arrContainsFunc = function (array, target) {
    return some(array, function (t) {
        return t.contains_func(target);
    });
};

/**
 * RenderT nodes can contain function references only in a few specific
 * forms, which we test for here.
 *
 * @param {Object} target any
 * @return {string} rendered LaTeX string
 */
ast.RenderT.prototype.tex_contains_func = function (target) {
    let t = this.tex_part(), m;
    // may have trailing '(', '[', '\\{' or " "
    t = t.replace(/(\(|\[|\\{| )$/, '');
    // special case #1: \\operatorname {someword}
    m = /^\\operatorname \{([^\\]*)\}$/.exec(t);
    if (m) {
        return match(target, '\\operatorname');
    }
    // special case #2: \\mbox{\\somefunc}
    m = /^\\mbox\{(\\.*)\}$/.exec(t);
    if (m) {
        return match(target, '\\mbox') || match(target, m[1]);
    }
    // special case #3: \\color, \\pagecolor, \\definecolor
    m = /^(\\(color|pagecolor|definecolor)) /.exec(t);
    if (m) {
        return match(target, m[1]);
    }
    // special case #4: \\mathbb, \\mathrm
    m = /^(\\math..) \{(\\.*)\}$/.exec(t);
    if (m) {
        return match(target, m[1]) || match(target, m[2]);
    }
    // protect against using random strings as keys in target
    return t.charAt(0) === '\\' && match(target, t);
};

// This defines a function of one argument, which becomes the first argument
// in the visitor functions.  The subsequent arguments in the definition
// are the fields of that particular AST class.
//
// The `target` provided can be a string, array, or object (which is used
// as a hash table).  It returns true if any of the array elements or
// object keys names a function referenced in the AST.
ast.Tex.defineVisitor('contains_func', {
    FQ: function (target, base, down, up) {
        // base_down^up
        return base.contains_func(target) ||
            down.contains_func(target) || up.contains_func(target);
    },
    DQ: function (target, base, down) {
        // base_down
        return base.contains_func(target) || down.contains_func(target);
    },
    UQ: function (target, base, up) {
        // base^up
        return base.contains_func(target) || up.contains_func(target);
    },
    FQN: function (target, down, up) {
        // _down^up (no base)
        return down.contains_func(target) || up.contains_func(target);
    },
    DQN: function (target, down) {
        // _down (no base)
        return down.contains_func(target);
    },
    UQN: function (target, up) {
        // ^up (no base)
        return up.contains_func(target);
    },
    LITERAL: function (target, r) {
        // a TeX literal.  It may contain function invocations in
        // certain specific forms; see the tex_contains method above.
        return r.tex_contains_func(target);
    },
    FUN1: function (target, f, a) {
        // {\f{a}}  (function of one argument)
        return match(target, f) || a.contains_func(target);
    },
    MHCHEM: function (target, f, a) {
        // {\f{a}}  (function of one argument)
        return match(target, f) || a.contains_func(target);
    },
    CHEM_WORD: function (target, r, s) {
        // chem word
        return match(target, s) || r.contains_func(target);
    },
    CHEM_FUN2u: function (target, f, a, b) {
        // {\underbrace{a}_{b}}
        return match(target, f) ||
            a.contains_func(target) || b.contains_func(target);
    },
    FUN1nb: function (target, f, a) {
        // \f{a}  (function of one argument, "no braces" around outside)
        return match(target, f) || a.contains_func(target);
    },
    DECLh: function (target, f, _, a) {
        // {\rm a1 a2 a3 a4 ...}  where f is \rm, \it, \cal, or \bf
        return match(target, f) || arrContainsFunc(a, target);
    },
    FUN2: function (target, f, a, b) {
        // {\f{a}{b}}  (function of two arguments)
        return match(target, f) ||
            a.contains_func(target) || b.contains_func(target);
    },
    FUN2nb: function (target, f, a, b) {
        // \f{a}{b}  (function of two arguments, "no braces" around outside)
        return match(target, f) ||
            a.contains_func(target) || b.contains_func(target);
    },
    FUN2sq: function (target, f, a, b) {
        // {\f[a]{b}}  (function of two arguments, first is optional)
        return match(target, f) ||
            a.contains_func(target) || b.contains_func(target);
    },
    CURLY: function (target, tl) {
        // { tl1 tl2 tl3 ... }
        return arrContainsFunc(tl, target);
    },
    DOLLAR: function (target, tl) {
        // { tl1 tl2 tl3 ... }
        return arrContainsFunc(tl, target);
    },
    INFIX: function (target, s, ll, rl) {
        // { ll1 ll2 ... \s rl1 rl2 ... } (infix function)
        return match(target, s) ||
            arrContainsFunc(ll, target) || arrContainsFunc(rl, target);
    },
    BOX: function (target, box, s) {
        // \box{s} where box is \text, \mbox, \hbox, or \vbox
        //         and s is a string not containing special characters
        return match(target, box);
    },
    BIG: function (target, big, d) {
        // \big\d where big is \big, \Big, \bigg, \Bigg, \biggl, etc
        return match(target, big) || d.tex_contains_func(target);
    },
    MATRIX: function (target, t, m) {
        // \begin{env} .. & .. \\ .. & .. \\ .. & .. \end{env}
        // t is the environment name.
        // m is a doubly-nested array
        const exprHas = function (e) {
            return arrContainsFunc(e, target);
        };
        const lineHas = function (l) {
            return some(l, exprHas);
        };
        const matrixHas = function (matrix) {
            return some(matrix, lineHas);
        };
        return match(target, '\\begin{' + t + '}') ||
            match(target, '\\end{' + t + '}') ||
            matrixHas(m);
    },
    LR: function (target, l, r, tl) {
        // \left\l tl1 tl2 tl3 ... \right\r  (a balanced pair of delimiters)
        return match(target, '\\left') || match(target, '\\right') ||
            l.tex_contains_func(target) || r.tex_contains_func(target) ||
            arrContainsFunc(tl, target);
    }
}, 1);

// allow user to pass an unparsed TeX string, or a parsed AST (which will
// usually be an array of `ast.Tex`), or a low-level `ast.Tex` node.
module.exports.contains_func = function (t, target) {
    if (typeof (t) === 'string') {
        t = require('./parser').parse(t);
    }
    if (Array.isArray(t)) {
        return arrContainsFunc(t, target);
    }
    return t.contains_func(target);
};

},{"./ast":2,"./parser":5}],4:[function(require,module,exports){
'use strict';

const json = require('../package.json');

module.exports = {
    name: json.name, // package name
    version: json.version // version # for this package
};

const Parser = require('./parser');
const render = module.exports.render = require('./render');
const tu = require('./texutil');

module.exports.ast = require('./ast');
module.exports.parse = Parser.parse.bind(Parser);
module.exports.SyntaxError = Parser.SyntaxError;

const astutil = require('./astutil');
module.exports.contains_func = astutil.contains_func;

/*
  Gets the location information of an error object, or returns default error
   location if no location information was specified.
 */
function getLocationInfo(e) {
    try {
        return {
            offset: e.location.start.offset,
            line: e.location.start.line,
            column: e.location.start.column
        };
    } catch (err) {
        return { offset: 0, line: 0, column: 0 };
    }
}

function handleTexError(e, options) {
    if (options && options.debug) {
        throw e;
    }
    let report = { error: e, success: false, warnings: [] };
    if (e instanceof Parser.SyntaxError) {
        if (e.message === 'Illegal TeX function') {
            report = Object.assign(report, { status: 'F', details: e.found }, getLocationInfo(e));
        } else {
            report = Object.assign(report, { status: 'S', details: e.toString() }, getLocationInfo(e));
        }
    } else { // this else statement is superfluous and was inserted for the coverage reporter
        report = Object.assign(report, { status: '-', details: e.toString() });
    }
    return report;
}

const check = module.exports.check = function (input, options, warnings) {
    /* status is one character:
     *  + : success! result is in 'output'
     *  E : Lexer exception raised
     *  F : TeX function not recognized
     *  S : Parsing error
     *  - : Generic/Default failure code. Might be an invalid argument,
     *      output file already exist, a problem with an external
     *      command ...
     */
    if (typeof options === 'undefined') {
        options = {};
        options.usemathrm = false;
        options.usemhchem = false;
    }
    if (typeof warnings === 'undefined') {
        warnings = [];
    }
    try {
        // allow user to pass a parsed AST as input, as well as a string
        if (typeof (input) === 'string') {
            input = Parser.parse(input, options);
        }
        const output = render(input);
        const result = { status: '+', output: output, warnings: warnings, input: input };
        ['ams', 'cancel', 'color', 'euro', 'teubner', 'mhchem', 'mathoid'].forEach(function (pkg) {
            pkg = pkg + '_required';
            result[pkg] = astutil.contains_func(input, tu[pkg]);
        });
        if (!options.usemhchem) {
            if (result.mhchem_required) {
                return {
                    status: 'C', details: 'mhchem package required.'
                };
            }
        }
        return result;
    } catch (e) {
        if (e instanceof Parser.SyntaxError && !options.oldtexvc && e.message.startsWith('Deprecation')) {
            warnings.push({ type: 'texvc-deprecation', details: handleTexError(e, options) });
            options.oldtexvc = true;
            return check(input, options, warnings);
        }
        if (e instanceof Parser.SyntaxError && options.usemhchem && !options.oldmhchem) {
            warnings.push({ type: 'mhchem-deprecation', details: handleTexError(e, options) });
            options.oldmhchem = true;
            return check(input, options, warnings);
        }
        return handleTexError(e, options);
    }
};

},{"../package.json":8,"./ast":2,"./astutil":3,"./parser":5,"./render":6,"./texutil":7}],5:[function(require,module,exports){
module.exports = /*
 * Generated by PEG.js 0.10.0.
 *
 * http://pegjs.org/
 */
(function() {
  "use strict";

  function peg$subclass(child, parent) {
    function ctor() { this.constructor = child; }
    ctor.prototype = parent.prototype;
    child.prototype = new ctor();
  }

  function peg$SyntaxError(message, expected, found, location) {
    this.message  = message;
    this.expected = expected;
    this.found    = found;
    this.location = location;
    this.name     = "SyntaxError";

    if (typeof Error.captureStackTrace === "function") {
      Error.captureStackTrace(this, peg$SyntaxError);
    }
  }

  peg$subclass(peg$SyntaxError, Error);

  peg$SyntaxError.buildMessage = function(expected, found) {
    var DESCRIBE_EXPECTATION_FNS = {
          literal: function(expectation) {
            return "\"" + literalEscape(expectation.text) + "\"";
          },

          "class": function(expectation) {
            var escapedParts = "",
                i;

            for (i = 0; i < expectation.parts.length; i++) {
              escapedParts += expectation.parts[i] instanceof Array
                ? classEscape(expectation.parts[i][0]) + "-" + classEscape(expectation.parts[i][1])
                : classEscape(expectation.parts[i]);
            }

            return "[" + (expectation.inverted ? "^" : "") + escapedParts + "]";
          },

          any: function(expectation) {
            return "any character";
          },

          end: function(expectation) {
            return "end of input";
          },

          other: function(expectation) {
            return expectation.description;
          }
        };

    function hex(ch) {
      return ch.charCodeAt(0).toString(16).toUpperCase();
    }

    function literalEscape(s) {
      return s
        .replace(/\\/g, '\\\\')
        .replace(/"/g,  '\\"')
        .replace(/\0/g, '\\0')
        .replace(/\t/g, '\\t')
        .replace(/\n/g, '\\n')
        .replace(/\r/g, '\\r')
        .replace(/[\x00-\x0F]/g,          function(ch) { return '\\x0' + hex(ch); })
        .replace(/[\x10-\x1F\x7F-\x9F]/g, function(ch) { return '\\x'  + hex(ch); });
    }

    function classEscape(s) {
      return s
        .replace(/\\/g, '\\\\')
        .replace(/\]/g, '\\]')
        .replace(/\^/g, '\\^')
        .replace(/-/g,  '\\-')
        .replace(/\0/g, '\\0')
        .replace(/\t/g, '\\t')
        .replace(/\n/g, '\\n')
        .replace(/\r/g, '\\r')
        .replace(/[\x00-\x0F]/g,          function(ch) { return '\\x0' + hex(ch); })
        .replace(/[\x10-\x1F\x7F-\x9F]/g, function(ch) { return '\\x'  + hex(ch); });
    }

    function describeExpectation(expectation) {
      return DESCRIBE_EXPECTATION_FNS[expectation.type](expectation);
    }

    function describeExpected(expected) {
      var descriptions = new Array(expected.length),
          i, j;

      for (i = 0; i < expected.length; i++) {
        descriptions[i] = describeExpectation(expected[i]);
      }

      descriptions.sort();

      if (descriptions.length > 0) {
        for (i = 1, j = 1; i < descriptions.length; i++) {
          if (descriptions[i - 1] !== descriptions[i]) {
            descriptions[j] = descriptions[i];
            j++;
          }
        }
        descriptions.length = j;
      }

      switch (descriptions.length) {
        case 1:
          return descriptions[0];

        case 2:
          return descriptions[0] + " or " + descriptions[1];

        default:
          return descriptions.slice(0, -1).join(", ")
            + ", or "
            + descriptions[descriptions.length - 1];
      }
    }

    function describeFound(found) {
      return found ? "\"" + literalEscape(found) + "\"" : "end of input";
    }

    return "Expected " + describeExpected(expected) + " but " + describeFound(found) + " found.";
  };

  function peg$parse(input, options) {
    options = options !== void 0 ? options : {};

    var peg$FAILED = {},

        peg$startRuleFunctions = { start: peg$parsestart },
        peg$startRuleFunction  = peg$parsestart,

        peg$c0 = function(t) { console.assert(t instanceof ast.LList); return t.toArray(); },
        peg$c1 = /^[ \t\n\r]/,
        peg$c2 = peg$classExpectation([" ", "\t", "\n", "\r"], false, false),
        peg$c3 = function(e) { return e; },
        peg$c4 = function(e1, name, e2) { return ast.LList(ast.Tex.INFIX(name, e1.toArray(), e2.toArray())); },
        peg$c5 = function(e1, f, e2) { return ast.LList(ast.Tex.INFIXh(f[0], f[1], e1.toArray(), e2.toArray()));},
        peg$c6 = "",
        peg$c7 = function() { return ast.LList.EMPTY; },
        peg$c8 = function(h, t) { return ast.LList(h, t); },
        peg$c9 = function(d, e) { return ast.LList(ast.Tex.DECLh(d[0], d[1], e.toArray())); },
        peg$c10 = function(l1, l2) { return ast.Tex.FQ(l1[0], l1[1], l2); },
        peg$c11 = function(l1, l2) { return ast.Tex.FQ(l1[0], l2, l1[1]); },
        peg$c12 = function(base, upi) { return ast.Tex.UQ(base, upi); },
        peg$c13 = function(base, downi) { return ast.Tex.DQ(base, downi); },
        peg$c14 = function() { return ast.Tex.LITERAL(ast.RenderT.TEX_ONLY( "]")); },
        peg$c15 = function(l, e) { return ast.LList(l, e); },
        peg$c16 = function(l1, l2) { return ast.Tex.FQN(l1[0], l2); },
        peg$c17 = function(l) { return ast.Tex.UQN(l); },
        peg$c18 = function(l) { return ast.Tex.DQN(l); },
        peg$c19 = function(d) { return d; },
        peg$c20 = function() { return ast.RenderT.TEX_ONLY( "]"); },
        peg$c21 = function(r) { return ast.Tex.LITERAL(r); },
        peg$c22 = function(f) { return tu.nullary_macro_aliase[f]; },
        peg$c23 = function(f) {
             var ast = peg$parse(tu.nullary_macro_aliase[f]);
             console.assert(Array.isArray(ast) && ast.length === 1);
             return ast[0];
           },
        peg$c24 = function(f) { return tu.deprecated_nullary_macro_aliase[f]; },
        peg$c25 = function(f) {
             var ast = peg$parse(tu.deprecated_nullary_macro_aliase[f]);
             console.assert(Array.isArray(ast) && ast.length === 1);
             if (options.oldtexvc){
               return ast[0];
             } else {
                  throw new peg$SyntaxError("Deprecation: Alias no longer supported.", [], text(), location());
             }
           },
        peg$c26 = function(b, r) { return ast.Tex.BIG(b, r); },
        peg$c27 = function(b) { return ast.Tex.BIG(b, ast.RenderT.TEX_ONLY( "]")); },
        peg$c28 = function(l, e, r) { return ast.Tex.LR(l, r, e.toArray()); },
        peg$c29 = function(name, e, l) { return ast.Tex.FUN2sq(name, ast.Tex.CURLY(e.toArray()), l); },
        peg$c30 = function(name, l) { return ast.Tex.FUN1(name, l); },
        peg$c31 = function(name, l) { return ast.Tex.FUN1nb(name, l); },
        peg$c32 = function(name, l) { return ast.Tex.MHCHEM(name, l); },
        peg$c33 = function(name, l1, l2) { return ast.Tex.FUN2(name, l1, l2); },
        peg$c34 = function(name, l1, l2) { return ast.Tex.FUN2nb(name, l1, l2); },
        peg$c35 = function(e) { return ast.Tex.CURLY(e.toArray()); },
        peg$c36 = function(e1, name, e2) { return ast.Tex.INFIX(name, e1.toArray(), e2.toArray()); },
        peg$c37 = function(e1, f, e2) { return ast.Tex.INFIXh(f[0], f[1], e1.toArray(), e2.toArray()); },
        peg$c38 = function(m) { return ast.Tex.MATRIX("matrix", lst2arr(m)); },
        peg$c39 = function(m) { return ast.Tex.MATRIX("pmatrix", lst2arr(m)); },
        peg$c40 = function(m) { return ast.Tex.MATRIX("bmatrix", lst2arr(m)); },
        peg$c41 = function(m) { return ast.Tex.MATRIX("Bmatrix", lst2arr(m)); },
        peg$c42 = function(m) { return ast.Tex.MATRIX("vmatrix", lst2arr(m)); },
        peg$c43 = function(m) { return ast.Tex.MATRIX("Vmatrix", lst2arr(m)); },
        peg$c44 = function(m) { return ast.Tex.MATRIX("array", lst2arr(m)); },
        peg$c45 = function(m) { return ast.Tex.MATRIX("aligned", lst2arr(m)); },
        peg$c46 = function(m) { return ast.Tex.MATRIX("alignedat", lst2arr(m)); },
        peg$c47 = function(m) { return ast.Tex.MATRIX("smallmatrix", lst2arr(m)); },
        peg$c48 = function(m) { return ast.Tex.MATRIX("cases", lst2arr(m)); },
        peg$c49 = "\\begin{",
        peg$c50 = peg$literalExpectation("\\begin{", false),
        peg$c51 = "}",
        peg$c52 = peg$literalExpectation("}", false),
        peg$c53 = function() { throw new peg$SyntaxError("Illegal TeX function", [], text(), location()); },
        peg$c54 = function(f) { return !tu.all_functions[f]; },
        peg$c55 = function(f) { throw new peg$SyntaxError("Illegal TeX function", [], f, location()); },
        peg$c56 = function(cs, m) { m.head[0].unshift(cs); return m; },
        peg$c57 = function(as, m) { m.head[0].unshift(as); return m; },
        peg$c58 = function(l, m) { return m; },
        peg$c59 = function(l, tail) { return { head: lst2arr(l), tail: tail }; },
        peg$c60 = function(f, l) { l.head.unshift(ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(f + " "))); return l;},
        peg$c61 = function(e, l) { return l; },
        peg$c62 = function(e, tail) { return { head: e.toArray(), tail: tail }; },
        peg$c63 = function() { return text(); },
        peg$c64 = function(cs) { return ast.Tex.CURLY([ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(cs))]); },
        peg$c65 = /^[lrc]/,
        peg$c66 = peg$classExpectation(["l", "r", "c"], false, false),
        peg$c67 = "p",
        peg$c68 = peg$literalExpectation("p", false),
        peg$c69 = "*",
        peg$c70 = peg$literalExpectation("*", false),
        peg$c71 = /^[0-9]/,
        peg$c72 = peg$classExpectation([["0", "9"]], false, false),
        peg$c73 = "||",
        peg$c74 = peg$literalExpectation("||", false),
        peg$c75 = "|",
        peg$c76 = peg$literalExpectation("|", false),
        peg$c77 = "@",
        peg$c78 = peg$literalExpectation("@", false),
        peg$c79 = function(num) { return ast.Tex.CURLY([ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(num))]); },
        peg$c80 = "[",
        peg$c81 = peg$literalExpectation("[", false),
        peg$c82 = /^[tcb]/,
        peg$c83 = peg$classExpectation(["t", "c", "b"], false, false),
        peg$c84 = "]",
        peg$c85 = peg$literalExpectation("]", false),
        peg$c86 = " ",
        peg$c87 = peg$literalExpectation(" ", false),
        peg$c88 = function(p, s) { return ast.LList(p,ast.LList(ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(" ")),s)); },
        peg$c89 = function(p) { return ast.LList(p,ast.LList.EMPTY); },
        peg$c90 = "(^)",
        peg$c91 = peg$literalExpectation("(^)", false),
        peg$c92 = function(m) { return ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(m)); },
        peg$c93 = function(m, n) { return ast.Tex.CHEM_WORD(m, ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(n))); },
        peg$c94 = function(m) { return m; },
        peg$c95 = "^",
        peg$c96 = peg$literalExpectation("^", false),
        peg$c97 = function(m, n) { return ast.Tex.CHEM_WORD(m, n); },
        peg$c98 = function(m, n, o) { return ast.Tex.CHEM_WORD(ast.Tex.CHEM_WORD(ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(m)), n), o); },
        peg$c99 = function() { return ast.Tex.LITERAL(ast.RenderT.TEX_ONLY("")); },
        peg$c100 = function(m) { return m;},
        peg$c101 = function(c) { return ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(c)) },
        peg$c102 = function(c) { return ast.Tex.CURLY([c]); },
        peg$c103 = function(c) { return ast.Tex.DOLLAR(c.toArray()); },
        peg$c104 = function(e) { return ast.Tex.CURLY([ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(e))]); },
        peg$c105 = function(a, b) { return ast.Tex.CHEM_WORD(ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(a)), ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(b))); },
        peg$c106 = function(a, b) { return ast.Tex.CHEM_WORD(ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(a)), b); },
        peg$c107 = function(a, b) { return ast.Tex.CHEM_WORD(ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(a)), ast.Tex.DOLLAR(b.toArray())); },
        peg$c108 = "_",
        peg$c109 = peg$literalExpectation("_", false),
        peg$c110 = function(name, l1, l2) { return ast.Tex.CHEM_FUN2u(name, l1, l2); },
        peg$c111 = function(cs) { return ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(cs.join(''))); },
        peg$c112 = "{",
        peg$c113 = peg$literalExpectation("{", false),
        peg$c114 = function(name) { return ast.Tex.LITERAL(ast.RenderT.TEX_ONLY(name.join(''))); },
        peg$c115 = /^[a-zA-Z]/,
        peg$c116 = peg$classExpectation([["a", "z"], ["A", "Z"]], false, false),
        peg$c117 = /^[,:;?!']/,
        peg$c118 = peg$classExpectation([",", ":", ";", "?", "!", "'"], false, false),
        peg$c119 = /^[().]/,
        peg$c120 = peg$classExpectation(["(", ")", "."], false, false),
        peg$c121 = /^[\-+*=]/,
        peg$c122 = peg$classExpectation(["-", "+", "*", "="], false, false),
        peg$c123 = /^[\/|]/,
        peg$c124 = peg$classExpectation(["/", "|"], false, false),
        peg$c125 = /^[\-0-9a-zA-Z+*,=():\/;?.!'` [\]\x80-\uD7FF\uE000-\uFFFF]/,
        peg$c126 = peg$classExpectation(["-", ["0", "9"], ["a", "z"], ["A", "Z"], "+", "*", ",", "=", "(", ")", ":", "/", ";", "?", ".", "!", "'", "`", " ", "[", "]", ["\x80", "\uD7FF"], ["\uE000", "\uFFFF"]], false, false),
        peg$c127 = /^[\uD800-\uDBFF]/,
        peg$c128 = peg$classExpectation([["\uD800", "\uDBFF"]], false, false),
        peg$c129 = /^[\uDC00-\uDFFF]/,
        peg$c130 = peg$classExpectation([["\uDC00", "\uDFFF"]], false, false),
        peg$c131 = function(l, h) { return text(); },
        peg$c132 = function(b) { return tu.box_functions[b]; },
        peg$c133 = function(b, cs) { return ast.Tex.BOX(b, cs.join('')); },
        peg$c134 = "-",
        peg$c135 = peg$literalExpectation("-", false),
        peg$c136 = function(c) { return ast.RenderT.TEX_ONLY(c); },
        peg$c137 = function(f) { return tu.latex_function_names[f]; },
        peg$c138 = "(",
        peg$c139 = peg$literalExpectation("(", false),
        peg$c140 = "\\{",
        peg$c141 = peg$literalExpectation("\\{", false),
        peg$c142 = function(f) { return " ";},
        peg$c143 = function(f, c) { return ast.RenderT.TEX_ONLY(f + c); },
        peg$c144 = function(f) { return tu.mediawiki_function_names[f]; },
        peg$c145 = function(f, c) { return ast.RenderT.TEX_ONLY("\\operatorname {" + f.slice(1) + "}" + c); },
        peg$c146 = function(f) { return tu.nullary_macro[f]; },
        peg$c147 = function(f) { return ast.RenderT.TEX_ONLY(f + " "); },
        peg$c148 = function(f) { return options.usemathrm && tu.nullary_macro_in_mbox[f]; },
        peg$c149 = function(f) { return ast.RenderT.TEX_ONLY("\\mathrm {" + f + "} "); },
        peg$c150 = function(mathrm) { return options.usemathrm && mathrm === "\\mathrm"; },
        peg$c151 = function(mathrm, f) { return options.usemathrm && tu.nullary_macro_in_mbox[f]; },
        peg$c152 = function(mathrm, f) { return options.usemathrm && ast.RenderT.TEX_ONLY("\\mathrm {" + f + "} "); },
        peg$c153 = function(f) { return tu.nullary_macro_in_mbox[f]; },
        peg$c154 = function(f) { return ast.RenderT.TEX_ONLY("\\mbox{" + f + "} "); },
        peg$c155 = function(mbox) { return mbox === "\\mbox"; },
        peg$c156 = function(mbox, f) { return tu.nullary_macro_in_mbox[f]; },
        peg$c157 = function(mbox, f) { return ast.RenderT.TEX_ONLY("\\mbox{" + f + "} "); },
        peg$c158 = function(f) { return ast.RenderT.TEX_ONLY(f); },
        peg$c159 = "\\",
        peg$c160 = peg$literalExpectation("\\", false),
        peg$c161 = /^[, ;!_#%$&]/,
        peg$c162 = peg$classExpectation([",", " ", ";", "!", "_", "#", "%", "$", "&"], false, false),
        peg$c163 = function(c) { return ast.RenderT.TEX_ONLY("\\" + c); },
        peg$c164 = /^[><~]/,
        peg$c165 = peg$classExpectation([">", "<", "~"], false, false),
        peg$c166 = /^[%$]/,
        peg$c167 = peg$classExpectation(["%", "$"], false, false),
        peg$c168 = function(c) { if(options.oldtexvc) {
            return ast.RenderT.TEX_ONLY("\\" + c); /* escape dangerous chars */
            } else {
             throw new peg$SyntaxError("Deprecation: % and $ need to be escaped.", [], text(), location());
            }},
        peg$c169 = /^[{}|]/,
        peg$c170 = peg$classExpectation(["{", "}", "|"], false, false),
        peg$c171 = function(f) { return tu.other_delimiters1[f]; },
        peg$c172 = function(f) { return tu.other_delimiters2[f]; },
        peg$c173 = function(f) { var p = peg$parse(tu.other_delimiters2[f]);
             console.assert(Array.isArray(p) && p.length === 1);
             console.assert(p[0].constructor === ast.Tex.LITERAL);
             console.assert(p[0][0].constructor === ast.RenderT.TEX_ONLY);
             return p[0][0];
           },
        peg$c174 = function(f) { return tu.fun_ar1nb[f]; },
        peg$c175 = function(f) { return f; },
        peg$c176 = function(f) { return tu.fun_ar1opt[f]; },
        peg$c177 = "&",
        peg$c178 = peg$literalExpectation("&", false),
        peg$c179 = "\\\\",
        peg$c180 = peg$literalExpectation("\\\\", false),
        peg$c181 = "\\begin",
        peg$c182 = peg$literalExpectation("\\begin", false),
        peg$c183 = "\\end",
        peg$c184 = peg$literalExpectation("\\end", false),
        peg$c185 = "{matrix}",
        peg$c186 = peg$literalExpectation("{matrix}", false),
        peg$c187 = "{pmatrix}",
        peg$c188 = peg$literalExpectation("{pmatrix}", false),
        peg$c189 = "{bmatrix}",
        peg$c190 = peg$literalExpectation("{bmatrix}", false),
        peg$c191 = "{Bmatrix}",
        peg$c192 = peg$literalExpectation("{Bmatrix}", false),
        peg$c193 = "{vmatrix}",
        peg$c194 = peg$literalExpectation("{vmatrix}", false),
        peg$c195 = "{Vmatrix}",
        peg$c196 = peg$literalExpectation("{Vmatrix}", false),
        peg$c197 = "{array}",
        peg$c198 = peg$literalExpectation("{array}", false),
        peg$c199 = "{align}",
        peg$c200 = peg$literalExpectation("{align}", false),
        peg$c201 = "{aligned}",
        peg$c202 = peg$literalExpectation("{aligned}", false),
        peg$c203 = "{alignat}",
        peg$c204 = peg$literalExpectation("{alignat}", false),
        peg$c205 = "{alignedat}",
        peg$c206 = peg$literalExpectation("{alignedat}", false),
        peg$c207 = "{smallmatrix}",
        peg$c208 = peg$literalExpectation("{smallmatrix}", false),
        peg$c209 = "{cases}",
        peg$c210 = peg$literalExpectation("{cases}", false),
        peg$c211 = function(f) { return tu.big_literals[f]; },
        peg$c212 = function(f) { return tu.fun_ar1[f]; },
        peg$c213 = function(f) { return options.oldmhchem && tu.fun_mhchem[f]},
        peg$c214 = function(f) { return tu.other_fun_ar1[f]; },
        peg$c215 = function(f) { if (options.oldtexvc) {
                return tu.other_fun_ar1[f];
             } else {
                throw new peg$SyntaxError("Deprecation: \\Bbb and \\bold are not allowed in math mode.", [], text(), location());
               }},
        peg$c216 = function(f) { return tu.fun_mhchem[f]; },
        peg$c217 = function(f) { return tu.fun_ar2[f]; },
        peg$c218 = function(f) { return tu.fun_infix[f]; },
        peg$c219 = function(f) { return tu.declh_function[f]; },
        peg$c220 = function(f) { return ast.Tex.DECLh(f, ast.FontForce.RM(), []); /*see bug 54818*/ },
        peg$c221 = function(f) { return tu.fun_ar2nb[f]; },
        peg$c222 = function(f) { return tu.left_function[f]; },
        peg$c223 = function(f) { return tu.right_function[f]; },
        peg$c224 = function(f) { return tu.hline_function[f]; },
        peg$c225 = function(f) { return tu.color_function[f]; },
        peg$c226 = function(f, cs) { return f + " " + cs; },
        peg$c227 = function(f) { return tu.definecolor_function[f]; },
        peg$c228 = "named",
        peg$c229 = peg$literalExpectation("named", true),
        peg$c230 = function(f, name, cs) { return "{named}" + cs; },
        peg$c231 = "gray",
        peg$c232 = peg$literalExpectation("gray", true),
        peg$c233 = function(f, name, cs) { return "{gray}" + cs; },
        peg$c234 = "rgb",
        peg$c235 = peg$literalExpectation("rgb", false),
        peg$c236 = function(f, name, cs) { return "{rgb}" + cs; },
        peg$c237 = "RGB",
        peg$c238 = peg$literalExpectation("RGB", false),
        peg$c239 = "cmyk",
        peg$c240 = peg$literalExpectation("cmyk", true),
        peg$c241 = function(f, name, cs) { return "{cmyk}" + cs; },
        peg$c242 = function(f, name, a) { return f + " {" + name.join('') + "}" + a; },
        peg$c243 = function(cs) { return "[named]" + cs; },
        peg$c244 = function(cs) { return "[gray]" + cs; },
        peg$c245 = function(cs) { return "[rgb]" + cs; },
        peg$c246 = function(cs) { return "[cmyk]" + cs; },
        peg$c247 = function(name) { return "{" + name.join('') + "}"; },
        peg$c248 = function(k) { return "{"+k+"}"; },
        peg$c249 = ",",
        peg$c250 = peg$literalExpectation(",", false),
        peg$c251 = function(r, g, b) { return "{"+r+","+g+","+b+"}"; },
        peg$c252 = function(c, m, y, k) { return "{"+c+","+m+","+y+","+k+"}"; },
        peg$c253 = "0",
        peg$c254 = peg$literalExpectation("0", false),
        peg$c255 = /^[1-9]/,
        peg$c256 = peg$classExpectation([["1", "9"]], false, false),
        peg$c257 = function(n) { return parseInt(n, 10) <= 255; },
        peg$c258 = function(n) { return n / 255; },
        peg$c259 = ".",
        peg$c260 = peg$literalExpectation(".", false),
        peg$c261 = function(n) { return n; },
        peg$c262 = /^[01]/,
        peg$c263 = peg$classExpectation(["0", "1"], false, false),
        peg$c264 = function(f) { return tu.mhchem_single_macro[f]; },
        peg$c265 = function(c) { return "\\" + c; },
        peg$c266 = function(f) { return tu.mhchem_bond[f]; },
        peg$c267 = function(f) { return tu.mhchem_macro_1p[f]; },
        peg$c268 = function(f) { return tu.mhchem_macro_2p[f]; },
        peg$c269 = function(f) { return tu.mhchem_macro_2pu[f]; },
        peg$c270 = function(f) { return tu.mhchem_macro_2pc[f]; },
        peg$c271 = /^[+-.*']/,
        peg$c272 = peg$classExpectation([["+", "."], "*", "'"], false, false),
        peg$c273 = "=",
        peg$c274 = peg$literalExpectation("=", false),
        peg$c275 = "#",
        peg$c276 = peg$literalExpectation("#", false),
        peg$c277 = "~--",
        peg$c278 = peg$literalExpectation("~--", false),
        peg$c279 = "~-",
        peg$c280 = peg$literalExpectation("~-", false),
        peg$c281 = "~=",
        peg$c282 = peg$literalExpectation("~=", false),
        peg$c283 = "~",
        peg$c284 = peg$literalExpectation("~", false),
        peg$c285 = "-~-",
        peg$c286 = peg$literalExpectation("-~-", false),
        peg$c287 = "....",
        peg$c288 = peg$literalExpectation("....", false),
        peg$c289 = "...",
        peg$c290 = peg$literalExpectation("...", false),
        peg$c291 = "<-",
        peg$c292 = peg$literalExpectation("<-", false),
        peg$c293 = "->",
        peg$c294 = peg$literalExpectation("->", false),
        peg$c295 = "1",
        peg$c296 = peg$literalExpectation("1", false),
        peg$c297 = "2",
        peg$c298 = peg$literalExpectation("2", false),
        peg$c299 = "3",
        peg$c300 = peg$literalExpectation("3", false),
        peg$c301 = "{math}",
        peg$c302 = peg$literalExpectation("{math}", false),
        peg$c303 = function(c) { return c; },
        peg$c304 = "\\}",
        peg$c305 = peg$literalExpectation("\\}", false),
        peg$c306 = /^[+-=#().,;\/*<>|@&'[\]]/,
        peg$c307 = peg$classExpectation([["+", "="], "#", "(", ")", ".", ",", ";", "/", "*", "<", ">", "|", "@", "&", "'", "[", "]"], false, false),
        peg$c308 = function() { return "{}"; },
        peg$c309 = function() { return false; },
        peg$c310 = function() { return peg$currPos === input.length; },

        peg$currPos          = 0,
        peg$savedPos         = 0,
        peg$posDetailsCache  = [{ line: 1, column: 1 }],
        peg$maxFailPos       = 0,
        peg$maxFailExpected  = [],
        peg$silentFails      = 0,

        peg$resultsCache = {},

        peg$result;

    if ("startRule" in options) {
      if (!(options.startRule in peg$startRuleFunctions)) {
        throw new Error("Can't start parsing from rule \"" + options.startRule + "\".");
      }

      peg$startRuleFunction = peg$startRuleFunctions[options.startRule];
    }

    function text() {
      return input.substring(peg$savedPos, peg$currPos);
    }

    function location() {
      return peg$computeLocation(peg$savedPos, peg$currPos);
    }

    function expected(description, location) {
      location = location !== void 0 ? location : peg$computeLocation(peg$savedPos, peg$currPos)

      throw peg$buildStructuredError(
        [peg$otherExpectation(description)],
        input.substring(peg$savedPos, peg$currPos),
        location
      );
    }

    function error(message, location) {
      location = location !== void 0 ? location : peg$computeLocation(peg$savedPos, peg$currPos)

      throw peg$buildSimpleError(message, location);
    }

    function peg$literalExpectation(text, ignoreCase) {
      return { type: "literal", text: text, ignoreCase: ignoreCase };
    }

    function peg$classExpectation(parts, inverted, ignoreCase) {
      return { type: "class", parts: parts, inverted: inverted, ignoreCase: ignoreCase };
    }

    function peg$anyExpectation() {
      return { type: "any" };
    }

    function peg$endExpectation() {
      return { type: "end" };
    }

    function peg$otherExpectation(description) {
      return { type: "other", description: description };
    }

    function peg$computePosDetails(pos) {
      var details = peg$posDetailsCache[pos], p;

      if (details) {
        return details;
      } else {
        p = pos - 1;
        while (!peg$posDetailsCache[p]) {
          p--;
        }

        details = peg$posDetailsCache[p];
        details = {
          line:   details.line,
          column: details.column
        };

        while (p < pos) {
          if (input.charCodeAt(p) === 10) {
            details.line++;
            details.column = 1;
          } else {
            details.column++;
          }

          p++;
        }

        peg$posDetailsCache[pos] = details;
        return details;
      }
    }

    function peg$computeLocation(startPos, endPos) {
      var startPosDetails = peg$computePosDetails(startPos),
          endPosDetails   = peg$computePosDetails(endPos);

      return {
        start: {
          offset: startPos,
          line:   startPosDetails.line,
          column: startPosDetails.column
        },
        end: {
          offset: endPos,
          line:   endPosDetails.line,
          column: endPosDetails.column
        }
      };
    }

    function peg$fail(expected) {
      if (peg$currPos < peg$maxFailPos) { return; }

      if (peg$currPos > peg$maxFailPos) {
        peg$maxFailPos = peg$currPos;
        peg$maxFailExpected = [];
      }

      peg$maxFailExpected.push(expected);
    }

    function peg$buildSimpleError(message, location) {
      return new peg$SyntaxError(message, null, null, location);
    }

    function peg$buildStructuredError(expected, found, location) {
      return new peg$SyntaxError(
        peg$SyntaxError.buildMessage(expected, found),
        expected,
        found,
        location
      );
    }

    function peg$parsestart() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 0,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parse_();
      if (s1 !== peg$FAILED) {
        s2 = peg$parsetex_expr();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c0(s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parse_() {
      var s0, s1;

      var key    = peg$currPos * 125 + 1,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = [];
      if (peg$c1.test(input.charAt(peg$currPos))) {
        s1 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c2); }
      }
      while (s1 !== peg$FAILED) {
        s0.push(s1);
        if (peg$c1.test(input.charAt(peg$currPos))) {
          s1 = input.charAt(peg$currPos);
          peg$currPos++;
        } else {
          s1 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c2); }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsetex_expr() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 2,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseexpr();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseEOF();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c3(s1);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parsene_expr();
        if (s1 !== peg$FAILED) {
          s2 = peg$parseFUN_INFIX();
          if (s2 !== peg$FAILED) {
            s3 = peg$parsene_expr();
            if (s3 !== peg$FAILED) {
              s4 = peg$parseEOF();
              if (s4 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c4(s1, s2, s3);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parsene_expr();
          if (s1 !== peg$FAILED) {
            s2 = peg$parseimpossible();
            if (s2 !== peg$FAILED) {
              s3 = peg$parsene_expr();
              if (s3 !== peg$FAILED) {
                s4 = peg$parseEOF();
                if (s4 !== peg$FAILED) {
                  peg$savedPos = s0;
                  s1 = peg$c5(s1, s2, s3);
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseexpr() {
      var s0, s1;

      var key    = peg$currPos * 125 + 3,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$parsene_expr();
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$c6;
        if (s1 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c7();
        }
        s0 = s1;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsene_expr() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 4,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parselit_aq();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseexpr();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c8(s1, s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parselitsq_aq();
        if (s1 !== peg$FAILED) {
          s2 = peg$parseexpr();
          if (s2 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c8(s1, s2);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parseDECLh();
          if (s1 !== peg$FAILED) {
            s2 = peg$parseexpr();
            if (s2 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c9(s1, s2);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselitsq_aq() {
      var s0;

      var key    = peg$currPos * 125 + 5,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$parselitsq_fq();
      if (s0 === peg$FAILED) {
        s0 = peg$parselitsq_dq();
        if (s0 === peg$FAILED) {
          s0 = peg$parselitsq_uq();
          if (s0 === peg$FAILED) {
            s0 = peg$parselitsq_zq();
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselitsq_fq() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 6,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parselitsq_dq();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseSUP();
        if (s2 !== peg$FAILED) {
          s3 = peg$parselit();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c10(s1, s3);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parselitsq_uq();
        if (s1 !== peg$FAILED) {
          s2 = peg$parseSUB();
          if (s2 !== peg$FAILED) {
            s3 = peg$parselit();
            if (s3 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c11(s1, s3);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselitsq_uq() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 7,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parselitsq_zq();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseSUP();
        if (s2 !== peg$FAILED) {
          s3 = peg$parselit();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c12(s1, s3);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselitsq_dq() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 8,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parselitsq_zq();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseSUB();
        if (s2 !== peg$FAILED) {
          s3 = peg$parselit();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c13(s1, s3);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselitsq_zq() {
      var s0, s1;

      var key    = peg$currPos * 125 + 9,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseSQ_CLOSE();
      if (s1 !== peg$FAILED) {
        peg$savedPos = s0;
        s1 = peg$c14();
      }
      s0 = s1;

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseexpr_nosqc() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 10,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parselit_aq();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseexpr_nosqc();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c15(s1, s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$c6;
        if (s1 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c7();
        }
        s0 = s1;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselit_aq() {
      var s0;

      var key    = peg$currPos * 125 + 11,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$parselit_fq();
      if (s0 === peg$FAILED) {
        s0 = peg$parselit_dq();
        if (s0 === peg$FAILED) {
          s0 = peg$parselit_uq();
          if (s0 === peg$FAILED) {
            s0 = peg$parselit_dqn();
            if (s0 === peg$FAILED) {
              s0 = peg$parselit_uqn();
              if (s0 === peg$FAILED) {
                s0 = peg$parselit();
              }
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselit_fq() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 12,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parselit_dq();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseSUP();
        if (s2 !== peg$FAILED) {
          s3 = peg$parselit();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c10(s1, s3);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parselit_uq();
        if (s1 !== peg$FAILED) {
          s2 = peg$parseSUB();
          if (s2 !== peg$FAILED) {
            s3 = peg$parselit();
            if (s3 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c11(s1, s3);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parselit_dqn();
          if (s1 !== peg$FAILED) {
            s2 = peg$parseSUP();
            if (s2 !== peg$FAILED) {
              s3 = peg$parselit();
              if (s3 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c16(s1, s3);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselit_uq() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 13,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parselit();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseSUP();
        if (s2 !== peg$FAILED) {
          s3 = peg$parselit();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c12(s1, s3);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselit_dq() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 14,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parselit();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseSUB();
        if (s2 !== peg$FAILED) {
          s3 = peg$parselit();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c13(s1, s3);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselit_uqn() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 15,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseSUP();
      if (s1 !== peg$FAILED) {
        s2 = peg$parselit();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c17(s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselit_dqn() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 16,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseSUB();
      if (s1 !== peg$FAILED) {
        s2 = peg$parselit();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c18(s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseleft() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 17,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseLEFT();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseDELIMITER();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c19(s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parseLEFT();
        if (s1 !== peg$FAILED) {
          s2 = peg$parseSQ_CLOSE();
          if (s2 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c20();
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseright() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 18,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseRIGHT();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseDELIMITER();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c19(s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parseRIGHT();
        if (s1 !== peg$FAILED) {
          s2 = peg$parseSQ_CLOSE();
          if (s2 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c20();
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parselit() {
      var s0, s1, s2, s3, s4, s5;

      var key    = peg$currPos * 125 + 19,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseLITERAL();
      if (s1 !== peg$FAILED) {
        peg$savedPos = s0;
        s1 = peg$c21(s1);
      }
      s0 = s1;
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parsegeneric_func();
        if (s1 !== peg$FAILED) {
          peg$savedPos = peg$currPos;
          s2 = peg$c22(s1);
          if (s2) {
            s2 = void 0;
          } else {
            s2 = peg$FAILED;
          }
          if (s2 !== peg$FAILED) {
            s3 = peg$parse_();
            if (s3 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c23(s1);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parsegeneric_func();
          if (s1 !== peg$FAILED) {
            peg$savedPos = peg$currPos;
            s2 = peg$c24(s1);
            if (s2) {
              s2 = void 0;
            } else {
              s2 = peg$FAILED;
            }
            if (s2 !== peg$FAILED) {
              s3 = peg$parse_();
              if (s3 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c25(s1);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
          if (s0 === peg$FAILED) {
            s0 = peg$currPos;
            s1 = peg$parseDELIMITER();
            if (s1 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c21(s1);
            }
            s0 = s1;
            if (s0 === peg$FAILED) {
              s0 = peg$currPos;
              s1 = peg$parseBIG();
              if (s1 !== peg$FAILED) {
                s2 = peg$parseDELIMITER();
                if (s2 !== peg$FAILED) {
                  peg$savedPos = s0;
                  s1 = peg$c26(s1, s2);
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
              if (s0 === peg$FAILED) {
                s0 = peg$currPos;
                s1 = peg$parseBIG();
                if (s1 !== peg$FAILED) {
                  s2 = peg$parseSQ_CLOSE();
                  if (s2 !== peg$FAILED) {
                    peg$savedPos = s0;
                    s1 = peg$c27(s1);
                    s0 = s1;
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
                if (s0 === peg$FAILED) {
                  s0 = peg$currPos;
                  s1 = peg$parseleft();
                  if (s1 !== peg$FAILED) {
                    s2 = peg$parseexpr();
                    if (s2 !== peg$FAILED) {
                      s3 = peg$parseright();
                      if (s3 !== peg$FAILED) {
                        peg$savedPos = s0;
                        s1 = peg$c28(s1, s2, s3);
                        s0 = s1;
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                  if (s0 === peg$FAILED) {
                    s0 = peg$currPos;
                    s1 = peg$parseFUN_AR1opt();
                    if (s1 !== peg$FAILED) {
                      s2 = peg$parseexpr_nosqc();
                      if (s2 !== peg$FAILED) {
                        s3 = peg$parseSQ_CLOSE();
                        if (s3 !== peg$FAILED) {
                          s4 = peg$parselit();
                          if (s4 !== peg$FAILED) {
                            peg$savedPos = s0;
                            s1 = peg$c29(s1, s2, s4);
                            s0 = s1;
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                    if (s0 === peg$FAILED) {
                      s0 = peg$currPos;
                      s1 = peg$parseFUN_AR1();
                      if (s1 !== peg$FAILED) {
                        s2 = peg$parselit();
                        if (s2 !== peg$FAILED) {
                          peg$savedPos = s0;
                          s1 = peg$c30(s1, s2);
                          s0 = s1;
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                      if (s0 === peg$FAILED) {
                        s0 = peg$currPos;
                        s1 = peg$parseFUN_AR1nb();
                        if (s1 !== peg$FAILED) {
                          s2 = peg$parselit();
                          if (s2 !== peg$FAILED) {
                            peg$savedPos = s0;
                            s1 = peg$c31(s1, s2);
                            s0 = s1;
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                        if (s0 === peg$FAILED) {
                          s0 = peg$currPos;
                          s1 = peg$parseFUN_MHCHEM();
                          if (s1 !== peg$FAILED) {
                            s2 = peg$parsechem_lit();
                            if (s2 !== peg$FAILED) {
                              peg$savedPos = s0;
                              s1 = peg$c32(s1, s2);
                              s0 = s1;
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                          if (s0 === peg$FAILED) {
                            s0 = peg$currPos;
                            s1 = peg$parseFUN_AR2();
                            if (s1 !== peg$FAILED) {
                              s2 = peg$parselit();
                              if (s2 !== peg$FAILED) {
                                s3 = peg$parselit();
                                if (s3 !== peg$FAILED) {
                                  peg$savedPos = s0;
                                  s1 = peg$c33(s1, s2, s3);
                                  s0 = s1;
                                } else {
                                  peg$currPos = s0;
                                  s0 = peg$FAILED;
                                }
                              } else {
                                peg$currPos = s0;
                                s0 = peg$FAILED;
                              }
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                            if (s0 === peg$FAILED) {
                              s0 = peg$currPos;
                              s1 = peg$parseFUN_AR2nb();
                              if (s1 !== peg$FAILED) {
                                s2 = peg$parselit();
                                if (s2 !== peg$FAILED) {
                                  s3 = peg$parselit();
                                  if (s3 !== peg$FAILED) {
                                    peg$savedPos = s0;
                                    s1 = peg$c34(s1, s2, s3);
                                    s0 = s1;
                                  } else {
                                    peg$currPos = s0;
                                    s0 = peg$FAILED;
                                  }
                                } else {
                                  peg$currPos = s0;
                                  s0 = peg$FAILED;
                                }
                              } else {
                                peg$currPos = s0;
                                s0 = peg$FAILED;
                              }
                              if (s0 === peg$FAILED) {
                                s0 = peg$parseBOX();
                                if (s0 === peg$FAILED) {
                                  s0 = peg$currPos;
                                  s1 = peg$parseCURLY_OPEN();
                                  if (s1 !== peg$FAILED) {
                                    s2 = peg$parseexpr();
                                    if (s2 !== peg$FAILED) {
                                      s3 = peg$parseCURLY_CLOSE();
                                      if (s3 !== peg$FAILED) {
                                        peg$savedPos = s0;
                                        s1 = peg$c35(s2);
                                        s0 = s1;
                                      } else {
                                        peg$currPos = s0;
                                        s0 = peg$FAILED;
                                      }
                                    } else {
                                      peg$currPos = s0;
                                      s0 = peg$FAILED;
                                    }
                                  } else {
                                    peg$currPos = s0;
                                    s0 = peg$FAILED;
                                  }
                                  if (s0 === peg$FAILED) {
                                    s0 = peg$currPos;
                                    s1 = peg$parseCURLY_OPEN();
                                    if (s1 !== peg$FAILED) {
                                      s2 = peg$parsene_expr();
                                      if (s2 !== peg$FAILED) {
                                        s3 = peg$parseFUN_INFIX();
                                        if (s3 !== peg$FAILED) {
                                          s4 = peg$parsene_expr();
                                          if (s4 !== peg$FAILED) {
                                            s5 = peg$parseCURLY_CLOSE();
                                            if (s5 !== peg$FAILED) {
                                              peg$savedPos = s0;
                                              s1 = peg$c36(s2, s3, s4);
                                              s0 = s1;
                                            } else {
                                              peg$currPos = s0;
                                              s0 = peg$FAILED;
                                            }
                                          } else {
                                            peg$currPos = s0;
                                            s0 = peg$FAILED;
                                          }
                                        } else {
                                          peg$currPos = s0;
                                          s0 = peg$FAILED;
                                        }
                                      } else {
                                        peg$currPos = s0;
                                        s0 = peg$FAILED;
                                      }
                                    } else {
                                      peg$currPos = s0;
                                      s0 = peg$FAILED;
                                    }
                                    if (s0 === peg$FAILED) {
                                      s0 = peg$currPos;
                                      s1 = peg$parseCURLY_OPEN();
                                      if (s1 !== peg$FAILED) {
                                        s2 = peg$parsene_expr();
                                        if (s2 !== peg$FAILED) {
                                          s3 = peg$parseimpossible();
                                          if (s3 !== peg$FAILED) {
                                            s4 = peg$parsene_expr();
                                            if (s4 !== peg$FAILED) {
                                              s5 = peg$parseCURLY_CLOSE();
                                              if (s5 !== peg$FAILED) {
                                                peg$savedPos = s0;
                                                s1 = peg$c37(s2, s3, s4);
                                                s0 = s1;
                                              } else {
                                                peg$currPos = s0;
                                                s0 = peg$FAILED;
                                              }
                                            } else {
                                              peg$currPos = s0;
                                              s0 = peg$FAILED;
                                            }
                                          } else {
                                            peg$currPos = s0;
                                            s0 = peg$FAILED;
                                          }
                                        } else {
                                          peg$currPos = s0;
                                          s0 = peg$FAILED;
                                        }
                                      } else {
                                        peg$currPos = s0;
                                        s0 = peg$FAILED;
                                      }
                                      if (s0 === peg$FAILED) {
                                        s0 = peg$currPos;
                                        s1 = peg$parseBEGIN_MATRIX();
                                        if (s1 !== peg$FAILED) {
                                          s2 = peg$parsearray();
                                          if (s2 === peg$FAILED) {
                                            s2 = peg$parsematrix();
                                          }
                                          if (s2 !== peg$FAILED) {
                                            s3 = peg$parseEND_MATRIX();
                                            if (s3 !== peg$FAILED) {
                                              peg$savedPos = s0;
                                              s1 = peg$c38(s2);
                                              s0 = s1;
                                            } else {
                                              peg$currPos = s0;
                                              s0 = peg$FAILED;
                                            }
                                          } else {
                                            peg$currPos = s0;
                                            s0 = peg$FAILED;
                                          }
                                        } else {
                                          peg$currPos = s0;
                                          s0 = peg$FAILED;
                                        }
                                        if (s0 === peg$FAILED) {
                                          s0 = peg$currPos;
                                          s1 = peg$parseBEGIN_PMATRIX();
                                          if (s1 !== peg$FAILED) {
                                            s2 = peg$parsearray();
                                            if (s2 === peg$FAILED) {
                                              s2 = peg$parsematrix();
                                            }
                                            if (s2 !== peg$FAILED) {
                                              s3 = peg$parseEND_PMATRIX();
                                              if (s3 !== peg$FAILED) {
                                                peg$savedPos = s0;
                                                s1 = peg$c39(s2);
                                                s0 = s1;
                                              } else {
                                                peg$currPos = s0;
                                                s0 = peg$FAILED;
                                              }
                                            } else {
                                              peg$currPos = s0;
                                              s0 = peg$FAILED;
                                            }
                                          } else {
                                            peg$currPos = s0;
                                            s0 = peg$FAILED;
                                          }
                                          if (s0 === peg$FAILED) {
                                            s0 = peg$currPos;
                                            s1 = peg$parseBEGIN_BMATRIX();
                                            if (s1 !== peg$FAILED) {
                                              s2 = peg$parsearray();
                                              if (s2 === peg$FAILED) {
                                                s2 = peg$parsematrix();
                                              }
                                              if (s2 !== peg$FAILED) {
                                                s3 = peg$parseEND_BMATRIX();
                                                if (s3 !== peg$FAILED) {
                                                  peg$savedPos = s0;
                                                  s1 = peg$c40(s2);
                                                  s0 = s1;
                                                } else {
                                                  peg$currPos = s0;
                                                  s0 = peg$FAILED;
                                                }
                                              } else {
                                                peg$currPos = s0;
                                                s0 = peg$FAILED;
                                              }
                                            } else {
                                              peg$currPos = s0;
                                              s0 = peg$FAILED;
                                            }
                                            if (s0 === peg$FAILED) {
                                              s0 = peg$currPos;
                                              s1 = peg$parseBEGIN_BBMATRIX();
                                              if (s1 !== peg$FAILED) {
                                                s2 = peg$parsearray();
                                                if (s2 === peg$FAILED) {
                                                  s2 = peg$parsematrix();
                                                }
                                                if (s2 !== peg$FAILED) {
                                                  s3 = peg$parseEND_BBMATRIX();
                                                  if (s3 !== peg$FAILED) {
                                                    peg$savedPos = s0;
                                                    s1 = peg$c41(s2);
                                                    s0 = s1;
                                                  } else {
                                                    peg$currPos = s0;
                                                    s0 = peg$FAILED;
                                                  }
                                                } else {
                                                  peg$currPos = s0;
                                                  s0 = peg$FAILED;
                                                }
                                              } else {
                                                peg$currPos = s0;
                                                s0 = peg$FAILED;
                                              }
                                              if (s0 === peg$FAILED) {
                                                s0 = peg$currPos;
                                                s1 = peg$parseBEGIN_VMATRIX();
                                                if (s1 !== peg$FAILED) {
                                                  s2 = peg$parsearray();
                                                  if (s2 === peg$FAILED) {
                                                    s2 = peg$parsematrix();
                                                  }
                                                  if (s2 !== peg$FAILED) {
                                                    s3 = peg$parseEND_VMATRIX();
                                                    if (s3 !== peg$FAILED) {
                                                      peg$savedPos = s0;
                                                      s1 = peg$c42(s2);
                                                      s0 = s1;
                                                    } else {
                                                      peg$currPos = s0;
                                                      s0 = peg$FAILED;
                                                    }
                                                  } else {
                                                    peg$currPos = s0;
                                                    s0 = peg$FAILED;
                                                  }
                                                } else {
                                                  peg$currPos = s0;
                                                  s0 = peg$FAILED;
                                                }
                                                if (s0 === peg$FAILED) {
                                                  s0 = peg$currPos;
                                                  s1 = peg$parseBEGIN_VVMATRIX();
                                                  if (s1 !== peg$FAILED) {
                                                    s2 = peg$parsearray();
                                                    if (s2 === peg$FAILED) {
                                                      s2 = peg$parsematrix();
                                                    }
                                                    if (s2 !== peg$FAILED) {
                                                      s3 = peg$parseEND_VVMATRIX();
                                                      if (s3 !== peg$FAILED) {
                                                        peg$savedPos = s0;
                                                        s1 = peg$c43(s2);
                                                        s0 = s1;
                                                      } else {
                                                        peg$currPos = s0;
                                                        s0 = peg$FAILED;
                                                      }
                                                    } else {
                                                      peg$currPos = s0;
                                                      s0 = peg$FAILED;
                                                    }
                                                  } else {
                                                    peg$currPos = s0;
                                                    s0 = peg$FAILED;
                                                  }
                                                  if (s0 === peg$FAILED) {
                                                    s0 = peg$currPos;
                                                    s1 = peg$parseBEGIN_ARRAY();
                                                    if (s1 !== peg$FAILED) {
                                                      s2 = peg$parseopt_pos();
                                                      if (s2 !== peg$FAILED) {
                                                        s3 = peg$parsearray();
                                                        if (s3 !== peg$FAILED) {
                                                          s4 = peg$parseEND_ARRAY();
                                                          if (s4 !== peg$FAILED) {
                                                            peg$savedPos = s0;
                                                            s1 = peg$c44(s3);
                                                            s0 = s1;
                                                          } else {
                                                            peg$currPos = s0;
                                                            s0 = peg$FAILED;
                                                          }
                                                        } else {
                                                          peg$currPos = s0;
                                                          s0 = peg$FAILED;
                                                        }
                                                      } else {
                                                        peg$currPos = s0;
                                                        s0 = peg$FAILED;
                                                      }
                                                    } else {
                                                      peg$currPos = s0;
                                                      s0 = peg$FAILED;
                                                    }
                                                    if (s0 === peg$FAILED) {
                                                      s0 = peg$currPos;
                                                      s1 = peg$parseBEGIN_ALIGN();
                                                      if (s1 !== peg$FAILED) {
                                                        s2 = peg$parseopt_pos();
                                                        if (s2 !== peg$FAILED) {
                                                          s3 = peg$parsematrix();
                                                          if (s3 !== peg$FAILED) {
                                                            s4 = peg$parseEND_ALIGN();
                                                            if (s4 !== peg$FAILED) {
                                                              peg$savedPos = s0;
                                                              s1 = peg$c45(s3);
                                                              s0 = s1;
                                                            } else {
                                                              peg$currPos = s0;
                                                              s0 = peg$FAILED;
                                                            }
                                                          } else {
                                                            peg$currPos = s0;
                                                            s0 = peg$FAILED;
                                                          }
                                                        } else {
                                                          peg$currPos = s0;
                                                          s0 = peg$FAILED;
                                                        }
                                                      } else {
                                                        peg$currPos = s0;
                                                        s0 = peg$FAILED;
                                                      }
                                                      if (s0 === peg$FAILED) {
                                                        s0 = peg$currPos;
                                                        s1 = peg$parseBEGIN_ALIGNED();
                                                        if (s1 !== peg$FAILED) {
                                                          s2 = peg$parseopt_pos();
                                                          if (s2 !== peg$FAILED) {
                                                            s3 = peg$parsematrix();
                                                            if (s3 !== peg$FAILED) {
                                                              s4 = peg$parseEND_ALIGNED();
                                                              if (s4 !== peg$FAILED) {
                                                                peg$savedPos = s0;
                                                                s1 = peg$c45(s3);
                                                                s0 = s1;
                                                              } else {
                                                                peg$currPos = s0;
                                                                s0 = peg$FAILED;
                                                              }
                                                            } else {
                                                              peg$currPos = s0;
                                                              s0 = peg$FAILED;
                                                            }
                                                          } else {
                                                            peg$currPos = s0;
                                                            s0 = peg$FAILED;
                                                          }
                                                        } else {
                                                          peg$currPos = s0;
                                                          s0 = peg$FAILED;
                                                        }
                                                        if (s0 === peg$FAILED) {
                                                          s0 = peg$currPos;
                                                          s1 = peg$parseBEGIN_ALIGNAT();
                                                          if (s1 !== peg$FAILED) {
                                                            s2 = peg$parsealignat();
                                                            if (s2 !== peg$FAILED) {
                                                              s3 = peg$parseEND_ALIGNAT();
                                                              if (s3 !== peg$FAILED) {
                                                                peg$savedPos = s0;
                                                                s1 = peg$c46(s2);
                                                                s0 = s1;
                                                              } else {
                                                                peg$currPos = s0;
                                                                s0 = peg$FAILED;
                                                              }
                                                            } else {
                                                              peg$currPos = s0;
                                                              s0 = peg$FAILED;
                                                            }
                                                          } else {
                                                            peg$currPos = s0;
                                                            s0 = peg$FAILED;
                                                          }
                                                          if (s0 === peg$FAILED) {
                                                            s0 = peg$currPos;
                                                            s1 = peg$parseBEGIN_ALIGNEDAT();
                                                            if (s1 !== peg$FAILED) {
                                                              s2 = peg$parsealignat();
                                                              if (s2 !== peg$FAILED) {
                                                                s3 = peg$parseEND_ALIGNEDAT();
                                                                if (s3 !== peg$FAILED) {
                                                                  peg$savedPos = s0;
                                                                  s1 = peg$c46(s2);
                                                                  s0 = s1;
                                                                } else {
                                                                  peg$currPos = s0;
                                                                  s0 = peg$FAILED;
                                                                }
                                                              } else {
                                                                peg$currPos = s0;
                                                                s0 = peg$FAILED;
                                                              }
                                                            } else {
                                                              peg$currPos = s0;
                                                              s0 = peg$FAILED;
                                                            }
                                                            if (s0 === peg$FAILED) {
                                                              s0 = peg$currPos;
                                                              s1 = peg$parseBEGIN_SMALLMATRIX();
                                                              if (s1 !== peg$FAILED) {
                                                                s2 = peg$parsearray();
                                                                if (s2 === peg$FAILED) {
                                                                  s2 = peg$parsematrix();
                                                                }
                                                                if (s2 !== peg$FAILED) {
                                                                  s3 = peg$parseEND_SMALLMATRIX();
                                                                  if (s3 !== peg$FAILED) {
                                                                    peg$savedPos = s0;
                                                                    s1 = peg$c47(s2);
                                                                    s0 = s1;
                                                                  } else {
                                                                    peg$currPos = s0;
                                                                    s0 = peg$FAILED;
                                                                  }
                                                                } else {
                                                                  peg$currPos = s0;
                                                                  s0 = peg$FAILED;
                                                                }
                                                              } else {
                                                                peg$currPos = s0;
                                                                s0 = peg$FAILED;
                                                              }
                                                              if (s0 === peg$FAILED) {
                                                                s0 = peg$currPos;
                                                                s1 = peg$parseBEGIN_CASES();
                                                                if (s1 !== peg$FAILED) {
                                                                  s2 = peg$parsematrix();
                                                                  if (s2 !== peg$FAILED) {
                                                                    s3 = peg$parseEND_CASES();
                                                                    if (s3 !== peg$FAILED) {
                                                                      peg$savedPos = s0;
                                                                      s1 = peg$c48(s2);
                                                                      s0 = s1;
                                                                    } else {
                                                                      peg$currPos = s0;
                                                                      s0 = peg$FAILED;
                                                                    }
                                                                  } else {
                                                                    peg$currPos = s0;
                                                                    s0 = peg$FAILED;
                                                                  }
                                                                } else {
                                                                  peg$currPos = s0;
                                                                  s0 = peg$FAILED;
                                                                }
                                                                if (s0 === peg$FAILED) {
                                                                  s0 = peg$currPos;
                                                                  if (input.substr(peg$currPos, 7) === peg$c49) {
                                                                    s1 = peg$c49;
                                                                    peg$currPos += 7;
                                                                  } else {
                                                                    s1 = peg$FAILED;
                                                                    if (peg$silentFails === 0) { peg$fail(peg$c50); }
                                                                  }
                                                                  if (s1 !== peg$FAILED) {
                                                                    s2 = [];
                                                                    s3 = peg$parsealpha();
                                                                    if (s3 !== peg$FAILED) {
                                                                      while (s3 !== peg$FAILED) {
                                                                        s2.push(s3);
                                                                        s3 = peg$parsealpha();
                                                                      }
                                                                    } else {
                                                                      s2 = peg$FAILED;
                                                                    }
                                                                    if (s2 !== peg$FAILED) {
                                                                      if (input.charCodeAt(peg$currPos) === 125) {
                                                                        s3 = peg$c51;
                                                                        peg$currPos++;
                                                                      } else {
                                                                        s3 = peg$FAILED;
                                                                        if (peg$silentFails === 0) { peg$fail(peg$c52); }
                                                                      }
                                                                      if (s3 !== peg$FAILED) {
                                                                        peg$savedPos = s0;
                                                                        s1 = peg$c53();
                                                                        s0 = s1;
                                                                      } else {
                                                                        peg$currPos = s0;
                                                                        s0 = peg$FAILED;
                                                                      }
                                                                    } else {
                                                                      peg$currPos = s0;
                                                                      s0 = peg$FAILED;
                                                                    }
                                                                  } else {
                                                                    peg$currPos = s0;
                                                                    s0 = peg$FAILED;
                                                                  }
                                                                  if (s0 === peg$FAILED) {
                                                                    s0 = peg$currPos;
                                                                    s1 = peg$parsegeneric_func();
                                                                    if (s1 !== peg$FAILED) {
                                                                      peg$savedPos = peg$currPos;
                                                                      s2 = peg$c54(s1);
                                                                      if (s2) {
                                                                        s2 = void 0;
                                                                      } else {
                                                                        s2 = peg$FAILED;
                                                                      }
                                                                      if (s2 !== peg$FAILED) {
                                                                        peg$savedPos = s0;
                                                                        s1 = peg$c55(s1);
                                                                        s0 = s1;
                                                                      } else {
                                                                        peg$currPos = s0;
                                                                        s0 = peg$FAILED;
                                                                      }
                                                                    } else {
                                                                      peg$currPos = s0;
                                                                      s0 = peg$FAILED;
                                                                    }
                                                                  }
                                                                }
                                                              }
                                                            }
                                                          }
                                                        }
                                                      }
                                                    }
                                                  }
                                                }
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsearray() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 20,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsecolumn_spec();
      if (s1 !== peg$FAILED) {
        s2 = peg$parsematrix();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c56(s1, s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsealignat() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 21,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsealignat_spec();
      if (s1 !== peg$FAILED) {
        s2 = peg$parsematrix();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c57(s1, s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsematrix() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 22,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseline_start();
      if (s1 !== peg$FAILED) {
        s2 = peg$currPos;
        s3 = peg$parseNEXT_ROW();
        if (s3 !== peg$FAILED) {
          s4 = peg$parsematrix();
          if (s4 !== peg$FAILED) {
            peg$savedPos = s2;
            s3 = peg$c58(s1, s4);
            s2 = s3;
          } else {
            peg$currPos = s2;
            s2 = peg$FAILED;
          }
        } else {
          peg$currPos = s2;
          s2 = peg$FAILED;
        }
        if (s2 === peg$FAILED) {
          s2 = null;
        }
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c59(s1, s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseline_start() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 23,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseHLINE();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseline_start();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c60(s1, s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$parseline();
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseline() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 24,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseexpr();
      if (s1 !== peg$FAILED) {
        s2 = peg$currPos;
        s3 = peg$parseNEXT_CELL();
        if (s3 !== peg$FAILED) {
          s4 = peg$parseline();
          if (s4 !== peg$FAILED) {
            peg$savedPos = s2;
            s3 = peg$c61(s1, s4);
            s2 = s3;
          } else {
            peg$currPos = s2;
            s2 = peg$FAILED;
          }
        } else {
          peg$currPos = s2;
          s2 = peg$FAILED;
        }
        if (s2 === peg$FAILED) {
          s2 = null;
        }
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c62(s1, s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsecolumn_spec() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 25,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseCURLY_OPEN();
      if (s1 !== peg$FAILED) {
        s2 = peg$currPos;
        s3 = [];
        s4 = peg$parseone_col();
        if (s4 !== peg$FAILED) {
          while (s4 !== peg$FAILED) {
            s3.push(s4);
            s4 = peg$parseone_col();
          }
        } else {
          s3 = peg$FAILED;
        }
        if (s3 !== peg$FAILED) {
          peg$savedPos = s2;
          s3 = peg$c63();
        }
        s2 = s3;
        if (s2 !== peg$FAILED) {
          s3 = peg$parseCURLY_CLOSE();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c64(s2);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseone_col() {
      var s0, s1, s2, s3, s4, s5, s6, s7, s8, s9;

      var key    = peg$currPos * 125 + 26,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (peg$c65.test(input.charAt(peg$currPos))) {
        s1 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c66); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        if (input.charCodeAt(peg$currPos) === 112) {
          s1 = peg$c67;
          peg$currPos++;
        } else {
          s1 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c68); }
        }
        if (s1 !== peg$FAILED) {
          s2 = peg$parseCURLY_OPEN();
          if (s2 !== peg$FAILED) {
            s3 = [];
            s4 = peg$parseboxchars();
            if (s4 !== peg$FAILED) {
              while (s4 !== peg$FAILED) {
                s3.push(s4);
                s4 = peg$parseboxchars();
              }
            } else {
              s3 = peg$FAILED;
            }
            if (s3 !== peg$FAILED) {
              s4 = peg$parseCURLY_CLOSE();
              if (s4 !== peg$FAILED) {
                s1 = [s1, s2, s3, s4];
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          if (input.charCodeAt(peg$currPos) === 42) {
            s1 = peg$c69;
            peg$currPos++;
          } else {
            s1 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c70); }
          }
          if (s1 !== peg$FAILED) {
            s2 = peg$parseCURLY_OPEN();
            if (s2 !== peg$FAILED) {
              s3 = [];
              if (peg$c71.test(input.charAt(peg$currPos))) {
                s4 = input.charAt(peg$currPos);
                peg$currPos++;
              } else {
                s4 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c72); }
              }
              if (s4 !== peg$FAILED) {
                while (s4 !== peg$FAILED) {
                  s3.push(s4);
                  if (peg$c71.test(input.charAt(peg$currPos))) {
                    s4 = input.charAt(peg$currPos);
                    peg$currPos++;
                  } else {
                    s4 = peg$FAILED;
                    if (peg$silentFails === 0) { peg$fail(peg$c72); }
                  }
                }
              } else {
                s3 = peg$FAILED;
              }
              if (s3 !== peg$FAILED) {
                s4 = peg$parse_();
                if (s4 !== peg$FAILED) {
                  s5 = peg$parseCURLY_CLOSE();
                  if (s5 !== peg$FAILED) {
                    s6 = peg$parseone_col();
                    if (s6 === peg$FAILED) {
                      s6 = peg$currPos;
                      s7 = peg$parseCURLY_OPEN();
                      if (s7 !== peg$FAILED) {
                        s8 = [];
                        s9 = peg$parseone_col();
                        if (s9 !== peg$FAILED) {
                          while (s9 !== peg$FAILED) {
                            s8.push(s9);
                            s9 = peg$parseone_col();
                          }
                        } else {
                          s8 = peg$FAILED;
                        }
                        if (s8 !== peg$FAILED) {
                          s9 = peg$parseCURLY_CLOSE();
                          if (s9 !== peg$FAILED) {
                            s7 = [s7, s8, s9];
                            s6 = s7;
                          } else {
                            peg$currPos = s6;
                            s6 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s6;
                          s6 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s6;
                        s6 = peg$FAILED;
                      }
                    }
                    if (s6 !== peg$FAILED) {
                      s1 = [s1, s2, s3, s4, s5, s6];
                      s0 = s1;
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
          if (s0 === peg$FAILED) {
            s0 = peg$currPos;
            if (input.substr(peg$currPos, 2) === peg$c73) {
              s1 = peg$c73;
              peg$currPos += 2;
            } else {
              s1 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c74); }
            }
            if (s1 !== peg$FAILED) {
              s2 = peg$parse_();
              if (s2 !== peg$FAILED) {
                s1 = [s1, s2];
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
            if (s0 === peg$FAILED) {
              s0 = peg$currPos;
              if (input.charCodeAt(peg$currPos) === 124) {
                s1 = peg$c75;
                peg$currPos++;
              } else {
                s1 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c76); }
              }
              if (s1 !== peg$FAILED) {
                s2 = peg$parse_();
                if (s2 !== peg$FAILED) {
                  s1 = [s1, s2];
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
              if (s0 === peg$FAILED) {
                s0 = peg$currPos;
                if (input.charCodeAt(peg$currPos) === 64) {
                  s1 = peg$c77;
                  peg$currPos++;
                } else {
                  s1 = peg$FAILED;
                  if (peg$silentFails === 0) { peg$fail(peg$c78); }
                }
                if (s1 !== peg$FAILED) {
                  s2 = peg$parse_();
                  if (s2 !== peg$FAILED) {
                    s3 = peg$parseCURLY_OPEN();
                    if (s3 !== peg$FAILED) {
                      s4 = [];
                      s5 = peg$parseboxchars();
                      if (s5 !== peg$FAILED) {
                        while (s5 !== peg$FAILED) {
                          s4.push(s5);
                          s5 = peg$parseboxchars();
                        }
                      } else {
                        s4 = peg$FAILED;
                      }
                      if (s4 !== peg$FAILED) {
                        s5 = peg$parseCURLY_CLOSE();
                        if (s5 !== peg$FAILED) {
                          s1 = [s1, s2, s3, s4, s5];
                          s0 = s1;
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              }
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsealignat_spec() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 27,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseCURLY_OPEN();
      if (s1 !== peg$FAILED) {
        s2 = peg$currPos;
        s3 = [];
        if (peg$c71.test(input.charAt(peg$currPos))) {
          s4 = input.charAt(peg$currPos);
          peg$currPos++;
        } else {
          s4 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c72); }
        }
        if (s4 !== peg$FAILED) {
          while (s4 !== peg$FAILED) {
            s3.push(s4);
            if (peg$c71.test(input.charAt(peg$currPos))) {
              s4 = input.charAt(peg$currPos);
              peg$currPos++;
            } else {
              s4 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c72); }
            }
          }
        } else {
          s3 = peg$FAILED;
        }
        if (s3 !== peg$FAILED) {
          peg$savedPos = s2;
          s3 = peg$c63();
        }
        s2 = s3;
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s4 = peg$parseCURLY_CLOSE();
            if (s4 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c79(s2);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseopt_pos() {
      var s0, s1, s2, s3, s4, s5, s6;

      var key    = peg$currPos * 125 + 28,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 91) {
        s1 = peg$c80;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c81); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          if (peg$c82.test(input.charAt(peg$currPos))) {
            s3 = input.charAt(peg$currPos);
            peg$currPos++;
          } else {
            s3 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c83); }
          }
          if (s3 !== peg$FAILED) {
            s4 = peg$parse_();
            if (s4 !== peg$FAILED) {
              if (input.charCodeAt(peg$currPos) === 93) {
                s5 = peg$c84;
                peg$currPos++;
              } else {
                s5 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c85); }
              }
              if (s5 !== peg$FAILED) {
                s6 = peg$parse_();
                if (s6 !== peg$FAILED) {
                  s1 = [s1, s2, s3, s4, s5, s6];
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$c6;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_lit() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 29,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseCURLY_OPEN();
      if (s1 !== peg$FAILED) {
        s2 = peg$parsechem_sentence();
        if (s2 !== peg$FAILED) {
          s3 = peg$parseCURLY_CLOSE();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c35(s2);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_sentence() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 30,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parse_();
      if (s1 !== peg$FAILED) {
        s2 = peg$parsechem_phrase();
        if (s2 !== peg$FAILED) {
          if (input.charCodeAt(peg$currPos) === 32) {
            s3 = peg$c86;
            peg$currPos++;
          } else {
            s3 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c87); }
          }
          if (s3 !== peg$FAILED) {
            s4 = peg$parsechem_sentence();
            if (s4 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c88(s2, s4);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parse_();
        if (s1 !== peg$FAILED) {
          s2 = peg$parsechem_phrase();
          if (s2 !== peg$FAILED) {
            s3 = peg$parse_();
            if (s3 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c89(s2);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_phrase() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 31,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.substr(peg$currPos, 3) === peg$c90) {
        s1 = peg$c90;
        peg$currPos += 3;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c91); }
      }
      if (s1 !== peg$FAILED) {
        peg$savedPos = s0;
        s1 = peg$c92(s1);
      }
      s0 = s1;
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parsechem_word();
        if (s1 !== peg$FAILED) {
          s2 = peg$parseCHEM_SINGLE_MACRO();
          if (s2 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c93(s1, s2);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parsechem_word();
          if (s1 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c94(s1);
          }
          s0 = s1;
          if (s0 === peg$FAILED) {
            s0 = peg$currPos;
            s1 = peg$parseCHEM_SINGLE_MACRO();
            if (s1 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c92(s1);
            }
            s0 = s1;
            if (s0 === peg$FAILED) {
              s0 = peg$currPos;
              if (input.charCodeAt(peg$currPos) === 94) {
                s1 = peg$c95;
                peg$currPos++;
              } else {
                s1 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c96); }
              }
              if (s1 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c92(s1);
              }
              s0 = s1;
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_word() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 32,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsechem_char();
      if (s1 !== peg$FAILED) {
        s2 = peg$parsechem_word_nt();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c97(s1, s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parseCHEM_SINGLE_MACRO();
        if (s1 !== peg$FAILED) {
          s2 = peg$parsechem_char_nl();
          if (s2 !== peg$FAILED) {
            s3 = peg$parsechem_word_nt();
            if (s3 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c98(s1, s2, s3);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_word_nt() {
      var s0, s1;

      var key    = peg$currPos * 125 + 33,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsechem_word();
      if (s1 !== peg$FAILED) {
        peg$savedPos = s0;
        s1 = peg$c94(s1);
      }
      s0 = s1;
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$c6;
        if (s1 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c99();
        }
        s0 = s1;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_char() {
      var s0, s1;

      var key    = peg$currPos * 125 + 34,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsechem_char_nl();
      if (s1 !== peg$FAILED) {
        peg$savedPos = s0;
        s1 = peg$c100(s1);
      }
      s0 = s1;
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parseCHEM_LETTER();
        if (s1 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c101(s1);
        }
        s0 = s1;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_char_nl() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 35,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsechem_script();
      if (s1 !== peg$FAILED) {
        peg$savedPos = s0;
        s1 = peg$c100(s1);
      }
      s0 = s1;
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parseCURLY_OPEN();
        if (s1 !== peg$FAILED) {
          s2 = peg$parsechem_text();
          if (s2 !== peg$FAILED) {
            s3 = peg$parseCURLY_CLOSE();
            if (s3 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c102(s2);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parseBEGIN_MATH();
          if (s1 !== peg$FAILED) {
            s2 = peg$parseexpr();
            if (s2 !== peg$FAILED) {
              s3 = peg$parseEND_MATH();
              if (s3 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c103(s2);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
          if (s0 === peg$FAILED) {
            s0 = peg$currPos;
            s1 = peg$parseCHEM_BOND();
            if (s1 !== peg$FAILED) {
              s2 = peg$parsechem_bond();
              if (s2 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c30(s1, s2);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
            if (s0 === peg$FAILED) {
              s0 = peg$currPos;
              s1 = peg$parsechem_macro();
              if (s1 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c94(s1);
              }
              s0 = s1;
              if (s0 === peg$FAILED) {
                s0 = peg$currPos;
                s1 = peg$parseCHEM_NONLETTER();
                if (s1 !== peg$FAILED) {
                  peg$savedPos = s0;
                  s1 = peg$c101(s1);
                }
                s0 = s1;
              }
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_bond() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 36,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseCURLY_OPEN();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseCHEM_BOND_TYPE();
        if (s2 !== peg$FAILED) {
          s3 = peg$parseCURLY_CLOSE();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c104(s2);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_script() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 37,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseCHEM_SUPERSUB();
      if (s1 !== peg$FAILED) {
        s2 = peg$parseCHEM_SCRIPT_FOLLOW();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c105(s1, s2);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parseCHEM_SUPERSUB();
        if (s1 !== peg$FAILED) {
          s2 = peg$parsechem_lit();
          if (s2 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c106(s1, s2);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parseCHEM_SUPERSUB();
          if (s1 !== peg$FAILED) {
            s2 = peg$parseBEGIN_MATH();
            if (s2 !== peg$FAILED) {
              s3 = peg$parseexpr();
              if (s3 !== peg$FAILED) {
                s4 = peg$parseEND_MATH();
                if (s4 !== peg$FAILED) {
                  peg$savedPos = s0;
                  s1 = peg$c107(s1, s3);
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_macro() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 38,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseCHEM_MACRO_2PU();
      if (s1 !== peg$FAILED) {
        s2 = peg$parsechem_lit();
        if (s2 !== peg$FAILED) {
          if (input.charCodeAt(peg$currPos) === 95) {
            s3 = peg$c108;
            peg$currPos++;
          } else {
            s3 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c109); }
          }
          if (s3 !== peg$FAILED) {
            s4 = peg$parsechem_lit();
            if (s4 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c110(s1, s2, s4);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parseCHEM_MACRO_2PC();
        if (s1 !== peg$FAILED) {
          s2 = peg$parseCHEM_COLOR();
          if (s2 !== peg$FAILED) {
            s3 = peg$parsechem_lit();
            if (s3 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c33(s1, s2, s3);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parseCHEM_MACRO_2P();
          if (s1 !== peg$FAILED) {
            s2 = peg$parsechem_lit();
            if (s2 !== peg$FAILED) {
              s3 = peg$parsechem_lit();
              if (s3 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c33(s1, s2, s3);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
          if (s0 === peg$FAILED) {
            s0 = peg$currPos;
            s1 = peg$parseCHEM_MACRO_1P();
            if (s1 !== peg$FAILED) {
              s2 = peg$parsechem_lit();
              if (s2 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c30(s1, s2);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsechem_text() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 39,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = [];
      s2 = peg$parseboxchars();
      if (s2 !== peg$FAILED) {
        while (s2 !== peg$FAILED) {
          s1.push(s2);
          s2 = peg$parseboxchars();
        }
      } else {
        s1 = peg$FAILED;
      }
      if (s1 !== peg$FAILED) {
        peg$savedPos = s0;
        s1 = peg$c111(s1);
      }
      s0 = s1;

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_COLOR() {
      var s0, s1, s2, s3, s4, s5, s6;

      var key    = peg$currPos * 125 + 40,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 123) {
        s1 = peg$c112;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c113); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s3 = [];
          s4 = peg$parsealpha();
          if (s4 !== peg$FAILED) {
            while (s4 !== peg$FAILED) {
              s3.push(s4);
              s4 = peg$parsealpha();
            }
          } else {
            s3 = peg$FAILED;
          }
          if (s3 !== peg$FAILED) {
            s4 = peg$parse_();
            if (s4 !== peg$FAILED) {
              if (input.charCodeAt(peg$currPos) === 125) {
                s5 = peg$c51;
                peg$currPos++;
              } else {
                s5 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c52); }
              }
              if (s5 !== peg$FAILED) {
                s6 = peg$parse_();
                if (s6 !== peg$FAILED) {
                  peg$savedPos = s0;
                  s1 = peg$c114(s3);
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsealpha() {
      var s0;

      var key    = peg$currPos * 125 + 41,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (peg$c115.test(input.charAt(peg$currPos))) {
        s0 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c116); }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseliteral_id() {
      var s0;

      var key    = peg$currPos * 125 + 42,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (peg$c115.test(input.charAt(peg$currPos))) {
        s0 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c116); }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseliteral_mn() {
      var s0;

      var key    = peg$currPos * 125 + 43,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (peg$c71.test(input.charAt(peg$currPos))) {
        s0 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c72); }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseliteral_uf_lt() {
      var s0;

      var key    = peg$currPos * 125 + 44,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (peg$c117.test(input.charAt(peg$currPos))) {
        s0 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c118); }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsedelimiter_uf_lt() {
      var s0;

      var key    = peg$currPos * 125 + 45,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (peg$c119.test(input.charAt(peg$currPos))) {
        s0 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c120); }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseliteral_uf_op() {
      var s0;

      var key    = peg$currPos * 125 + 46,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (peg$c121.test(input.charAt(peg$currPos))) {
        s0 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c122); }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsedelimiter_uf_op() {
      var s0;

      var key    = peg$currPos * 125 + 47,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (peg$c123.test(input.charAt(peg$currPos))) {
        s0 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c124); }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseboxchars() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 48,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (peg$c125.test(input.charAt(peg$currPos))) {
        s0 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c126); }
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        if (peg$c127.test(input.charAt(peg$currPos))) {
          s1 = input.charAt(peg$currPos);
          peg$currPos++;
        } else {
          s1 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c128); }
        }
        if (s1 !== peg$FAILED) {
          if (peg$c129.test(input.charAt(peg$currPos))) {
            s2 = input.charAt(peg$currPos);
            peg$currPos++;
          } else {
            s2 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c130); }
          }
          if (s2 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c131(s1, s2);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBOX() {
      var s0, s1, s2, s3, s4, s5, s6, s7;

      var key    = peg$currPos * 125 + 49,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c132(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            if (input.charCodeAt(peg$currPos) === 123) {
              s4 = peg$c112;
              peg$currPos++;
            } else {
              s4 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c113); }
            }
            if (s4 !== peg$FAILED) {
              s5 = [];
              s6 = peg$parseboxchars();
              if (s6 !== peg$FAILED) {
                while (s6 !== peg$FAILED) {
                  s5.push(s6);
                  s6 = peg$parseboxchars();
                }
              } else {
                s5 = peg$FAILED;
              }
              if (s5 !== peg$FAILED) {
                if (input.charCodeAt(peg$currPos) === 125) {
                  s6 = peg$c51;
                  peg$currPos++;
                } else {
                  s6 = peg$FAILED;
                  if (peg$silentFails === 0) { peg$fail(peg$c52); }
                }
                if (s6 !== peg$FAILED) {
                  s7 = peg$parse_();
                  if (s7 !== peg$FAILED) {
                    peg$savedPos = s0;
                    s1 = peg$c133(s1, s5);
                    s0 = s1;
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseLITERAL() {
      var s0, s1, s2, s3, s4, s5, s6, s7, s8, s9;

      var key    = peg$currPos * 125 + 50,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseliteral_id();
      if (s1 === peg$FAILED) {
        s1 = peg$parseliteral_mn();
        if (s1 === peg$FAILED) {
          s1 = peg$parseliteral_uf_lt();
          if (s1 === peg$FAILED) {
            if (input.charCodeAt(peg$currPos) === 45) {
              s1 = peg$c134;
              peg$currPos++;
            } else {
              s1 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c135); }
            }
            if (s1 === peg$FAILED) {
              s1 = peg$parseliteral_uf_op();
            }
          }
        }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c136(s1);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parsegeneric_func();
        if (s1 !== peg$FAILED) {
          peg$savedPos = peg$currPos;
          s2 = peg$c137(s1);
          if (s2) {
            s2 = void 0;
          } else {
            s2 = peg$FAILED;
          }
          if (s2 !== peg$FAILED) {
            s3 = peg$parse_();
            if (s3 !== peg$FAILED) {
              if (input.charCodeAt(peg$currPos) === 40) {
                s4 = peg$c138;
                peg$currPos++;
              } else {
                s4 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c139); }
              }
              if (s4 === peg$FAILED) {
                if (input.charCodeAt(peg$currPos) === 91) {
                  s4 = peg$c80;
                  peg$currPos++;
                } else {
                  s4 = peg$FAILED;
                  if (peg$silentFails === 0) { peg$fail(peg$c81); }
                }
                if (s4 === peg$FAILED) {
                  if (input.substr(peg$currPos, 2) === peg$c140) {
                    s4 = peg$c140;
                    peg$currPos += 2;
                  } else {
                    s4 = peg$FAILED;
                    if (peg$silentFails === 0) { peg$fail(peg$c141); }
                  }
                  if (s4 === peg$FAILED) {
                    s4 = peg$currPos;
                    s5 = peg$c6;
                    if (s5 !== peg$FAILED) {
                      peg$savedPos = s4;
                      s5 = peg$c142(s1);
                    }
                    s4 = s5;
                  }
                }
              }
              if (s4 !== peg$FAILED) {
                s5 = peg$parse_();
                if (s5 !== peg$FAILED) {
                  peg$savedPos = s0;
                  s1 = peg$c143(s1, s4);
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parsegeneric_func();
          if (s1 !== peg$FAILED) {
            peg$savedPos = peg$currPos;
            s2 = peg$c144(s1);
            if (s2) {
              s2 = void 0;
            } else {
              s2 = peg$FAILED;
            }
            if (s2 !== peg$FAILED) {
              s3 = peg$parse_();
              if (s3 !== peg$FAILED) {
                if (input.charCodeAt(peg$currPos) === 40) {
                  s4 = peg$c138;
                  peg$currPos++;
                } else {
                  s4 = peg$FAILED;
                  if (peg$silentFails === 0) { peg$fail(peg$c139); }
                }
                if (s4 === peg$FAILED) {
                  if (input.charCodeAt(peg$currPos) === 91) {
                    s4 = peg$c80;
                    peg$currPos++;
                  } else {
                    s4 = peg$FAILED;
                    if (peg$silentFails === 0) { peg$fail(peg$c81); }
                  }
                  if (s4 === peg$FAILED) {
                    if (input.substr(peg$currPos, 2) === peg$c140) {
                      s4 = peg$c140;
                      peg$currPos += 2;
                    } else {
                      s4 = peg$FAILED;
                      if (peg$silentFails === 0) { peg$fail(peg$c141); }
                    }
                    if (s4 === peg$FAILED) {
                      s4 = peg$currPos;
                      s5 = peg$c6;
                      if (s5 !== peg$FAILED) {
                        peg$savedPos = s4;
                        s5 = peg$c142(s1);
                      }
                      s4 = s5;
                    }
                  }
                }
                if (s4 !== peg$FAILED) {
                  s5 = peg$parse_();
                  if (s5 !== peg$FAILED) {
                    peg$savedPos = s0;
                    s1 = peg$c145(s1, s4);
                    s0 = s1;
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
          if (s0 === peg$FAILED) {
            s0 = peg$currPos;
            s1 = peg$parsegeneric_func();
            if (s1 !== peg$FAILED) {
              peg$savedPos = peg$currPos;
              s2 = peg$c146(s1);
              if (s2) {
                s2 = void 0;
              } else {
                s2 = peg$FAILED;
              }
              if (s2 !== peg$FAILED) {
                s3 = peg$parse_();
                if (s3 !== peg$FAILED) {
                  peg$savedPos = s0;
                  s1 = peg$c147(s1);
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
            if (s0 === peg$FAILED) {
              s0 = peg$currPos;
              s1 = peg$parsegeneric_func();
              if (s1 !== peg$FAILED) {
                peg$savedPos = peg$currPos;
                s2 = peg$c148(s1);
                if (s2) {
                  s2 = void 0;
                } else {
                  s2 = peg$FAILED;
                }
                if (s2 !== peg$FAILED) {
                  s3 = peg$parse_();
                  if (s3 !== peg$FAILED) {
                    peg$savedPos = s0;
                    s1 = peg$c149(s1);
                    s0 = s1;
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
              if (s0 === peg$FAILED) {
                s0 = peg$currPos;
                s1 = peg$parsegeneric_func();
                if (s1 !== peg$FAILED) {
                  peg$savedPos = peg$currPos;
                  s2 = peg$c150(s1);
                  if (s2) {
                    s2 = void 0;
                  } else {
                    s2 = peg$FAILED;
                  }
                  if (s2 !== peg$FAILED) {
                    s3 = peg$parse_();
                    if (s3 !== peg$FAILED) {
                      if (input.charCodeAt(peg$currPos) === 123) {
                        s4 = peg$c112;
                        peg$currPos++;
                      } else {
                        s4 = peg$FAILED;
                        if (peg$silentFails === 0) { peg$fail(peg$c113); }
                      }
                      if (s4 !== peg$FAILED) {
                        s5 = peg$parsegeneric_func();
                        if (s5 !== peg$FAILED) {
                          peg$savedPos = peg$currPos;
                          s6 = peg$c151(s1, s5);
                          if (s6) {
                            s6 = void 0;
                          } else {
                            s6 = peg$FAILED;
                          }
                          if (s6 !== peg$FAILED) {
                            s7 = peg$parse_();
                            if (s7 !== peg$FAILED) {
                              if (input.charCodeAt(peg$currPos) === 125) {
                                s8 = peg$c51;
                                peg$currPos++;
                              } else {
                                s8 = peg$FAILED;
                                if (peg$silentFails === 0) { peg$fail(peg$c52); }
                              }
                              if (s8 !== peg$FAILED) {
                                s9 = peg$parse_();
                                if (s9 !== peg$FAILED) {
                                  peg$savedPos = s0;
                                  s1 = peg$c152(s1, s5);
                                  s0 = s1;
                                } else {
                                  peg$currPos = s0;
                                  s0 = peg$FAILED;
                                }
                              } else {
                                peg$currPos = s0;
                                s0 = peg$FAILED;
                              }
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
                if (s0 === peg$FAILED) {
                  s0 = peg$currPos;
                  s1 = peg$parsegeneric_func();
                  if (s1 !== peg$FAILED) {
                    peg$savedPos = peg$currPos;
                    s2 = peg$c153(s1);
                    if (s2) {
                      s2 = void 0;
                    } else {
                      s2 = peg$FAILED;
                    }
                    if (s2 !== peg$FAILED) {
                      s3 = peg$parse_();
                      if (s3 !== peg$FAILED) {
                        peg$savedPos = s0;
                        s1 = peg$c154(s1);
                        s0 = s1;
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                  if (s0 === peg$FAILED) {
                    s0 = peg$currPos;
                    s1 = peg$parsegeneric_func();
                    if (s1 !== peg$FAILED) {
                      peg$savedPos = peg$currPos;
                      s2 = peg$c155(s1);
                      if (s2) {
                        s2 = void 0;
                      } else {
                        s2 = peg$FAILED;
                      }
                      if (s2 !== peg$FAILED) {
                        s3 = peg$parse_();
                        if (s3 !== peg$FAILED) {
                          if (input.charCodeAt(peg$currPos) === 123) {
                            s4 = peg$c112;
                            peg$currPos++;
                          } else {
                            s4 = peg$FAILED;
                            if (peg$silentFails === 0) { peg$fail(peg$c113); }
                          }
                          if (s4 !== peg$FAILED) {
                            s5 = peg$parsegeneric_func();
                            if (s5 !== peg$FAILED) {
                              peg$savedPos = peg$currPos;
                              s6 = peg$c156(s1, s5);
                              if (s6) {
                                s6 = void 0;
                              } else {
                                s6 = peg$FAILED;
                              }
                              if (s6 !== peg$FAILED) {
                                s7 = peg$parse_();
                                if (s7 !== peg$FAILED) {
                                  if (input.charCodeAt(peg$currPos) === 125) {
                                    s8 = peg$c51;
                                    peg$currPos++;
                                  } else {
                                    s8 = peg$FAILED;
                                    if (peg$silentFails === 0) { peg$fail(peg$c52); }
                                  }
                                  if (s8 !== peg$FAILED) {
                                    s9 = peg$parse_();
                                    if (s9 !== peg$FAILED) {
                                      peg$savedPos = s0;
                                      s1 = peg$c157(s1, s5);
                                      s0 = s1;
                                    } else {
                                      peg$currPos = s0;
                                      s0 = peg$FAILED;
                                    }
                                  } else {
                                    peg$currPos = s0;
                                    s0 = peg$FAILED;
                                  }
                                } else {
                                  peg$currPos = s0;
                                  s0 = peg$FAILED;
                                }
                              } else {
                                peg$currPos = s0;
                                s0 = peg$FAILED;
                              }
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                    if (s0 === peg$FAILED) {
                      s0 = peg$currPos;
                      s1 = peg$parseCOLOR();
                      if (s1 === peg$FAILED) {
                        s1 = peg$parseDEFINECOLOR();
                      }
                      if (s1 !== peg$FAILED) {
                        peg$savedPos = s0;
                        s1 = peg$c158(s1);
                      }
                      s0 = s1;
                      if (s0 === peg$FAILED) {
                        s0 = peg$currPos;
                        if (input.charCodeAt(peg$currPos) === 92) {
                          s1 = peg$c159;
                          peg$currPos++;
                        } else {
                          s1 = peg$FAILED;
                          if (peg$silentFails === 0) { peg$fail(peg$c160); }
                        }
                        if (s1 !== peg$FAILED) {
                          if (peg$c161.test(input.charAt(peg$currPos))) {
                            s2 = input.charAt(peg$currPos);
                            peg$currPos++;
                          } else {
                            s2 = peg$FAILED;
                            if (peg$silentFails === 0) { peg$fail(peg$c162); }
                          }
                          if (s2 !== peg$FAILED) {
                            s3 = peg$parse_();
                            if (s3 !== peg$FAILED) {
                              peg$savedPos = s0;
                              s1 = peg$c163(s2);
                              s0 = s1;
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                        if (s0 === peg$FAILED) {
                          s0 = peg$currPos;
                          if (peg$c164.test(input.charAt(peg$currPos))) {
                            s1 = input.charAt(peg$currPos);
                            peg$currPos++;
                          } else {
                            s1 = peg$FAILED;
                            if (peg$silentFails === 0) { peg$fail(peg$c165); }
                          }
                          if (s1 !== peg$FAILED) {
                            s2 = peg$parse_();
                            if (s2 !== peg$FAILED) {
                              peg$savedPos = s0;
                              s1 = peg$c136(s1);
                              s0 = s1;
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                          if (s0 === peg$FAILED) {
                            s0 = peg$currPos;
                            if (peg$c166.test(input.charAt(peg$currPos))) {
                              s1 = input.charAt(peg$currPos);
                              peg$currPos++;
                            } else {
                              s1 = peg$FAILED;
                              if (peg$silentFails === 0) { peg$fail(peg$c167); }
                            }
                            if (s1 !== peg$FAILED) {
                              s2 = peg$parse_();
                              if (s2 !== peg$FAILED) {
                                peg$savedPos = s0;
                                s1 = peg$c168(s1);
                                s0 = s1;
                              } else {
                                peg$currPos = s0;
                                s0 = peg$FAILED;
                              }
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseDELIMITER() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 51,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsedelimiter_uf_lt();
      if (s1 === peg$FAILED) {
        s1 = peg$parsedelimiter_uf_op();
        if (s1 === peg$FAILED) {
          if (input.charCodeAt(peg$currPos) === 91) {
            s1 = peg$c80;
            peg$currPos++;
          } else {
            s1 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c81); }
          }
        }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c136(s1);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        if (input.charCodeAt(peg$currPos) === 92) {
          s1 = peg$c159;
          peg$currPos++;
        } else {
          s1 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c160); }
        }
        if (s1 !== peg$FAILED) {
          if (peg$c169.test(input.charAt(peg$currPos))) {
            s2 = input.charAt(peg$currPos);
            peg$currPos++;
          } else {
            s2 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c170); }
          }
          if (s2 !== peg$FAILED) {
            s3 = peg$parse_();
            if (s3 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c163(s2);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parsegeneric_func();
          if (s1 !== peg$FAILED) {
            peg$savedPos = peg$currPos;
            s2 = peg$c171(s1);
            if (s2) {
              s2 = void 0;
            } else {
              s2 = peg$FAILED;
            }
            if (s2 !== peg$FAILED) {
              s3 = peg$parse_();
              if (s3 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c147(s1);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
          if (s0 === peg$FAILED) {
            s0 = peg$currPos;
            s1 = peg$parsegeneric_func();
            if (s1 !== peg$FAILED) {
              peg$savedPos = peg$currPos;
              s2 = peg$c172(s1);
              if (s2) {
                s2 = void 0;
              } else {
                s2 = peg$FAILED;
              }
              if (s2 !== peg$FAILED) {
                s3 = peg$parse_();
                if (s3 !== peg$FAILED) {
                  peg$savedPos = s0;
                  s1 = peg$c173(s1);
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseFUN_AR1nb() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 52,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c174(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseFUN_AR1opt() {
      var s0, s1, s2, s3, s4, s5;

      var key    = peg$currPos * 125 + 53,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c176(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            if (input.charCodeAt(peg$currPos) === 91) {
              s4 = peg$c80;
              peg$currPos++;
            } else {
              s4 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c81); }
            }
            if (s4 !== peg$FAILED) {
              s5 = peg$parse_();
              if (s5 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c175(s1);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseNEXT_CELL() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 54,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 38) {
        s1 = peg$c177;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c178); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseNEXT_ROW() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 55,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.substr(peg$currPos, 2) === peg$c179) {
        s1 = peg$c179;
        peg$currPos += 2;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c180); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 56,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.substr(peg$currPos, 6) === peg$c181) {
        s1 = peg$c181;
        peg$currPos += 6;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c182); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 57,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.substr(peg$currPos, 4) === peg$c183) {
        s1 = peg$c183;
        peg$currPos += 4;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c184); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_MATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 58,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 8) === peg$c185) {
          s2 = peg$c185;
          peg$currPos += 8;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c186); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_MATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 59,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 8) === peg$c185) {
          s2 = peg$c185;
          peg$currPos += 8;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c186); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_PMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 60,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c187) {
          s2 = peg$c187;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c188); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_PMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 61,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c187) {
          s2 = peg$c187;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c188); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_BMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 62,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c189) {
          s2 = peg$c189;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c190); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_BMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 63,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c189) {
          s2 = peg$c189;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c190); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_BBMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 64,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c191) {
          s2 = peg$c191;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c192); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_BBMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 65,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c191) {
          s2 = peg$c191;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c192); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_VMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 66,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c193) {
          s2 = peg$c193;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c194); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_VMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 67,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c193) {
          s2 = peg$c193;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c194); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_VVMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 68,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c195) {
          s2 = peg$c195;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c196); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_VVMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 69,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c195) {
          s2 = peg$c195;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c196); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_ARRAY() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 70,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 7) === peg$c197) {
          s2 = peg$c197;
          peg$currPos += 7;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c198); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_ARRAY() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 71,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 7) === peg$c197) {
          s2 = peg$c197;
          peg$currPos += 7;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c198); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_ALIGN() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 72,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 7) === peg$c199) {
          s2 = peg$c199;
          peg$currPos += 7;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c200); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_ALIGN() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 73,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 7) === peg$c199) {
          s2 = peg$c199;
          peg$currPos += 7;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c200); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_ALIGNED() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 74,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c201) {
          s2 = peg$c201;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c202); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_ALIGNED() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 75,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c201) {
          s2 = peg$c201;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c202); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_ALIGNAT() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 76,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c203) {
          s2 = peg$c203;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c204); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_ALIGNAT() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 77,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 9) === peg$c203) {
          s2 = peg$c203;
          peg$currPos += 9;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c204); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_ALIGNEDAT() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 78,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 11) === peg$c205) {
          s2 = peg$c205;
          peg$currPos += 11;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c206); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_ALIGNEDAT() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 79,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 11) === peg$c205) {
          s2 = peg$c205;
          peg$currPos += 11;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c206); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_SMALLMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 80,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 13) === peg$c207) {
          s2 = peg$c207;
          peg$currPos += 13;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c208); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_SMALLMATRIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 81,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 13) === peg$c207) {
          s2 = peg$c207;
          peg$currPos += 13;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c208); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_CASES() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 82,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 7) === peg$c209) {
          s2 = peg$c209;
          peg$currPos += 7;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c210); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_CASES() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 83,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 7) === peg$c209) {
          s2 = peg$c209;
          peg$currPos += 7;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c210); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseSQ_CLOSE() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 84,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 93) {
        s1 = peg$c84;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c85); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCURLY_OPEN() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 85,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 123) {
        s1 = peg$c112;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c113); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCURLY_CLOSE() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 86,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 125) {
        s1 = peg$c51;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c52); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseSUP() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 87,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 94) {
        s1 = peg$c95;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c96); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseSUB() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 88,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 95) {
        s1 = peg$c108;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c109); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s1 = [s1, s2];
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parsegeneric_func() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 89,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 92) {
        s1 = peg$c159;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c160); }
      }
      if (s1 !== peg$FAILED) {
        s2 = [];
        s3 = peg$parsealpha();
        if (s3 !== peg$FAILED) {
          while (s3 !== peg$FAILED) {
            s2.push(s3);
            s3 = peg$parsealpha();
          }
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c63();
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBIG() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 90,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c211(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseFUN_AR1() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 91,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c212(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$parsegeneric_func();
        if (s1 !== peg$FAILED) {
          peg$savedPos = peg$currPos;
          s2 = peg$c213(s1);
          if (s2) {
            s2 = void 0;
          } else {
            s2 = peg$FAILED;
          }
          if (s2 !== peg$FAILED) {
            s3 = peg$parse_();
            if (s3 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c175(s1);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          s1 = peg$parsegeneric_func();
          if (s1 !== peg$FAILED) {
            peg$savedPos = peg$currPos;
            s2 = peg$c214(s1);
            if (s2) {
              s2 = void 0;
            } else {
              s2 = peg$FAILED;
            }
            if (s2 !== peg$FAILED) {
              s3 = peg$parse_();
              if (s3 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c215(s1);
                s0 = s1;
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseFUN_MHCHEM() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 92,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c216(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseFUN_AR2() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 93,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c217(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseFUN_INFIX() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 94,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c218(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseDECLh() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 95,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c219(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c220(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseFUN_AR2nb() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 96,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c221(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseLEFT() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 97,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c222(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseRIGHT() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 98,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c223(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseHLINE() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 99,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c224(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCOLOR() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 100,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c225(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s4 = peg$parseCOLOR_SPEC();
            if (s4 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c226(s1, s4);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseDEFINECOLOR() {
      var s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17;

      var key    = peg$currPos * 125 + 101,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c227(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            if (input.charCodeAt(peg$currPos) === 123) {
              s4 = peg$c112;
              peg$currPos++;
            } else {
              s4 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c113); }
            }
            if (s4 !== peg$FAILED) {
              s5 = peg$parse_();
              if (s5 !== peg$FAILED) {
                s6 = [];
                s7 = peg$parsealpha();
                if (s7 !== peg$FAILED) {
                  while (s7 !== peg$FAILED) {
                    s6.push(s7);
                    s7 = peg$parsealpha();
                  }
                } else {
                  s6 = peg$FAILED;
                }
                if (s6 !== peg$FAILED) {
                  s7 = peg$parse_();
                  if (s7 !== peg$FAILED) {
                    if (input.charCodeAt(peg$currPos) === 125) {
                      s8 = peg$c51;
                      peg$currPos++;
                    } else {
                      s8 = peg$FAILED;
                      if (peg$silentFails === 0) { peg$fail(peg$c52); }
                    }
                    if (s8 !== peg$FAILED) {
                      s9 = peg$parse_();
                      if (s9 !== peg$FAILED) {
                        if (input.charCodeAt(peg$currPos) === 123) {
                          s10 = peg$c112;
                          peg$currPos++;
                        } else {
                          s10 = peg$FAILED;
                          if (peg$silentFails === 0) { peg$fail(peg$c113); }
                        }
                        if (s10 !== peg$FAILED) {
                          s11 = peg$parse_();
                          if (s11 !== peg$FAILED) {
                            s12 = peg$currPos;
                            if (input.substr(peg$currPos, 5).toLowerCase() === peg$c228) {
                              s13 = input.substr(peg$currPos, 5);
                              peg$currPos += 5;
                            } else {
                              s13 = peg$FAILED;
                              if (peg$silentFails === 0) { peg$fail(peg$c229); }
                            }
                            if (s13 !== peg$FAILED) {
                              s14 = peg$parse_();
                              if (s14 !== peg$FAILED) {
                                if (input.charCodeAt(peg$currPos) === 125) {
                                  s15 = peg$c51;
                                  peg$currPos++;
                                } else {
                                  s15 = peg$FAILED;
                                  if (peg$silentFails === 0) { peg$fail(peg$c52); }
                                }
                                if (s15 !== peg$FAILED) {
                                  s16 = peg$parse_();
                                  if (s16 !== peg$FAILED) {
                                    s17 = peg$parseCOLOR_SPEC_NAMED();
                                    if (s17 !== peg$FAILED) {
                                      peg$savedPos = s12;
                                      s13 = peg$c230(s1, s6, s17);
                                      s12 = s13;
                                    } else {
                                      peg$currPos = s12;
                                      s12 = peg$FAILED;
                                    }
                                  } else {
                                    peg$currPos = s12;
                                    s12 = peg$FAILED;
                                  }
                                } else {
                                  peg$currPos = s12;
                                  s12 = peg$FAILED;
                                }
                              } else {
                                peg$currPos = s12;
                                s12 = peg$FAILED;
                              }
                            } else {
                              peg$currPos = s12;
                              s12 = peg$FAILED;
                            }
                            if (s12 === peg$FAILED) {
                              s12 = peg$currPos;
                              if (input.substr(peg$currPos, 4).toLowerCase() === peg$c231) {
                                s13 = input.substr(peg$currPos, 4);
                                peg$currPos += 4;
                              } else {
                                s13 = peg$FAILED;
                                if (peg$silentFails === 0) { peg$fail(peg$c232); }
                              }
                              if (s13 !== peg$FAILED) {
                                s14 = peg$parse_();
                                if (s14 !== peg$FAILED) {
                                  if (input.charCodeAt(peg$currPos) === 125) {
                                    s15 = peg$c51;
                                    peg$currPos++;
                                  } else {
                                    s15 = peg$FAILED;
                                    if (peg$silentFails === 0) { peg$fail(peg$c52); }
                                  }
                                  if (s15 !== peg$FAILED) {
                                    s16 = peg$parse_();
                                    if (s16 !== peg$FAILED) {
                                      s17 = peg$parseCOLOR_SPEC_GRAY();
                                      if (s17 !== peg$FAILED) {
                                        peg$savedPos = s12;
                                        s13 = peg$c233(s1, s6, s17);
                                        s12 = s13;
                                      } else {
                                        peg$currPos = s12;
                                        s12 = peg$FAILED;
                                      }
                                    } else {
                                      peg$currPos = s12;
                                      s12 = peg$FAILED;
                                    }
                                  } else {
                                    peg$currPos = s12;
                                    s12 = peg$FAILED;
                                  }
                                } else {
                                  peg$currPos = s12;
                                  s12 = peg$FAILED;
                                }
                              } else {
                                peg$currPos = s12;
                                s12 = peg$FAILED;
                              }
                              if (s12 === peg$FAILED) {
                                s12 = peg$currPos;
                                if (input.substr(peg$currPos, 3) === peg$c234) {
                                  s13 = peg$c234;
                                  peg$currPos += 3;
                                } else {
                                  s13 = peg$FAILED;
                                  if (peg$silentFails === 0) { peg$fail(peg$c235); }
                                }
                                if (s13 !== peg$FAILED) {
                                  s14 = peg$parse_();
                                  if (s14 !== peg$FAILED) {
                                    if (input.charCodeAt(peg$currPos) === 125) {
                                      s15 = peg$c51;
                                      peg$currPos++;
                                    } else {
                                      s15 = peg$FAILED;
                                      if (peg$silentFails === 0) { peg$fail(peg$c52); }
                                    }
                                    if (s15 !== peg$FAILED) {
                                      s16 = peg$parse_();
                                      if (s16 !== peg$FAILED) {
                                        s17 = peg$parseCOLOR_SPEC_rgb();
                                        if (s17 !== peg$FAILED) {
                                          peg$savedPos = s12;
                                          s13 = peg$c236(s1, s6, s17);
                                          s12 = s13;
                                        } else {
                                          peg$currPos = s12;
                                          s12 = peg$FAILED;
                                        }
                                      } else {
                                        peg$currPos = s12;
                                        s12 = peg$FAILED;
                                      }
                                    } else {
                                      peg$currPos = s12;
                                      s12 = peg$FAILED;
                                    }
                                  } else {
                                    peg$currPos = s12;
                                    s12 = peg$FAILED;
                                  }
                                } else {
                                  peg$currPos = s12;
                                  s12 = peg$FAILED;
                                }
                                if (s12 === peg$FAILED) {
                                  s12 = peg$currPos;
                                  if (input.substr(peg$currPos, 3) === peg$c237) {
                                    s13 = peg$c237;
                                    peg$currPos += 3;
                                  } else {
                                    s13 = peg$FAILED;
                                    if (peg$silentFails === 0) { peg$fail(peg$c238); }
                                  }
                                  if (s13 !== peg$FAILED) {
                                    s14 = peg$parse_();
                                    if (s14 !== peg$FAILED) {
                                      if (input.charCodeAt(peg$currPos) === 125) {
                                        s15 = peg$c51;
                                        peg$currPos++;
                                      } else {
                                        s15 = peg$FAILED;
                                        if (peg$silentFails === 0) { peg$fail(peg$c52); }
                                      }
                                      if (s15 !== peg$FAILED) {
                                        s16 = peg$parse_();
                                        if (s16 !== peg$FAILED) {
                                          s17 = peg$parseCOLOR_SPEC_RGB();
                                          if (s17 !== peg$FAILED) {
                                            peg$savedPos = s12;
                                            s13 = peg$c236(s1, s6, s17);
                                            s12 = s13;
                                          } else {
                                            peg$currPos = s12;
                                            s12 = peg$FAILED;
                                          }
                                        } else {
                                          peg$currPos = s12;
                                          s12 = peg$FAILED;
                                        }
                                      } else {
                                        peg$currPos = s12;
                                        s12 = peg$FAILED;
                                      }
                                    } else {
                                      peg$currPos = s12;
                                      s12 = peg$FAILED;
                                    }
                                  } else {
                                    peg$currPos = s12;
                                    s12 = peg$FAILED;
                                  }
                                  if (s12 === peg$FAILED) {
                                    s12 = peg$currPos;
                                    if (input.substr(peg$currPos, 4).toLowerCase() === peg$c239) {
                                      s13 = input.substr(peg$currPos, 4);
                                      peg$currPos += 4;
                                    } else {
                                      s13 = peg$FAILED;
                                      if (peg$silentFails === 0) { peg$fail(peg$c240); }
                                    }
                                    if (s13 !== peg$FAILED) {
                                      s14 = peg$parse_();
                                      if (s14 !== peg$FAILED) {
                                        if (input.charCodeAt(peg$currPos) === 125) {
                                          s15 = peg$c51;
                                          peg$currPos++;
                                        } else {
                                          s15 = peg$FAILED;
                                          if (peg$silentFails === 0) { peg$fail(peg$c52); }
                                        }
                                        if (s15 !== peg$FAILED) {
                                          s16 = peg$parse_();
                                          if (s16 !== peg$FAILED) {
                                            s17 = peg$parseCOLOR_SPEC_CMYK();
                                            if (s17 !== peg$FAILED) {
                                              peg$savedPos = s12;
                                              s13 = peg$c241(s1, s6, s17);
                                              s12 = s13;
                                            } else {
                                              peg$currPos = s12;
                                              s12 = peg$FAILED;
                                            }
                                          } else {
                                            peg$currPos = s12;
                                            s12 = peg$FAILED;
                                          }
                                        } else {
                                          peg$currPos = s12;
                                          s12 = peg$FAILED;
                                        }
                                      } else {
                                        peg$currPos = s12;
                                        s12 = peg$FAILED;
                                      }
                                    } else {
                                      peg$currPos = s12;
                                      s12 = peg$FAILED;
                                    }
                                  }
                                }
                              }
                            }
                            if (s12 !== peg$FAILED) {
                              peg$savedPos = s0;
                              s1 = peg$c242(s1, s6, s12);
                              s0 = s1;
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCOLOR_SPEC() {
      var s0, s1, s2, s3, s4, s5, s6, s7;

      var key    = peg$currPos * 125 + 102,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$parseCOLOR_SPEC_NAMED();
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        if (input.charCodeAt(peg$currPos) === 91) {
          s1 = peg$c80;
          peg$currPos++;
        } else {
          s1 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c81); }
        }
        if (s1 !== peg$FAILED) {
          s2 = peg$parse_();
          if (s2 !== peg$FAILED) {
            if (input.substr(peg$currPos, 5).toLowerCase() === peg$c228) {
              s3 = input.substr(peg$currPos, 5);
              peg$currPos += 5;
            } else {
              s3 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c229); }
            }
            if (s3 !== peg$FAILED) {
              s4 = peg$parse_();
              if (s4 !== peg$FAILED) {
                if (input.charCodeAt(peg$currPos) === 93) {
                  s5 = peg$c84;
                  peg$currPos++;
                } else {
                  s5 = peg$FAILED;
                  if (peg$silentFails === 0) { peg$fail(peg$c85); }
                }
                if (s5 !== peg$FAILED) {
                  s6 = peg$parse_();
                  if (s6 !== peg$FAILED) {
                    s7 = peg$parseCOLOR_SPEC_NAMED();
                    if (s7 !== peg$FAILED) {
                      peg$savedPos = s0;
                      s1 = peg$c243(s7);
                      s0 = s1;
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          if (input.charCodeAt(peg$currPos) === 91) {
            s1 = peg$c80;
            peg$currPos++;
          } else {
            s1 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c81); }
          }
          if (s1 !== peg$FAILED) {
            s2 = peg$parse_();
            if (s2 !== peg$FAILED) {
              if (input.substr(peg$currPos, 4).toLowerCase() === peg$c231) {
                s3 = input.substr(peg$currPos, 4);
                peg$currPos += 4;
              } else {
                s3 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c232); }
              }
              if (s3 !== peg$FAILED) {
                s4 = peg$parse_();
                if (s4 !== peg$FAILED) {
                  if (input.charCodeAt(peg$currPos) === 93) {
                    s5 = peg$c84;
                    peg$currPos++;
                  } else {
                    s5 = peg$FAILED;
                    if (peg$silentFails === 0) { peg$fail(peg$c85); }
                  }
                  if (s5 !== peg$FAILED) {
                    s6 = peg$parse_();
                    if (s6 !== peg$FAILED) {
                      s7 = peg$parseCOLOR_SPEC_GRAY();
                      if (s7 !== peg$FAILED) {
                        peg$savedPos = s0;
                        s1 = peg$c244(s7);
                        s0 = s1;
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
          if (s0 === peg$FAILED) {
            s0 = peg$currPos;
            if (input.charCodeAt(peg$currPos) === 91) {
              s1 = peg$c80;
              peg$currPos++;
            } else {
              s1 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c81); }
            }
            if (s1 !== peg$FAILED) {
              s2 = peg$parse_();
              if (s2 !== peg$FAILED) {
                if (input.substr(peg$currPos, 3) === peg$c234) {
                  s3 = peg$c234;
                  peg$currPos += 3;
                } else {
                  s3 = peg$FAILED;
                  if (peg$silentFails === 0) { peg$fail(peg$c235); }
                }
                if (s3 !== peg$FAILED) {
                  s4 = peg$parse_();
                  if (s4 !== peg$FAILED) {
                    if (input.charCodeAt(peg$currPos) === 93) {
                      s5 = peg$c84;
                      peg$currPos++;
                    } else {
                      s5 = peg$FAILED;
                      if (peg$silentFails === 0) { peg$fail(peg$c85); }
                    }
                    if (s5 !== peg$FAILED) {
                      s6 = peg$parse_();
                      if (s6 !== peg$FAILED) {
                        s7 = peg$parseCOLOR_SPEC_rgb();
                        if (s7 !== peg$FAILED) {
                          peg$savedPos = s0;
                          s1 = peg$c245(s7);
                          s0 = s1;
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
            if (s0 === peg$FAILED) {
              s0 = peg$currPos;
              if (input.charCodeAt(peg$currPos) === 91) {
                s1 = peg$c80;
                peg$currPos++;
              } else {
                s1 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c81); }
              }
              if (s1 !== peg$FAILED) {
                s2 = peg$parse_();
                if (s2 !== peg$FAILED) {
                  if (input.substr(peg$currPos, 3) === peg$c237) {
                    s3 = peg$c237;
                    peg$currPos += 3;
                  } else {
                    s3 = peg$FAILED;
                    if (peg$silentFails === 0) { peg$fail(peg$c238); }
                  }
                  if (s3 !== peg$FAILED) {
                    s4 = peg$parse_();
                    if (s4 !== peg$FAILED) {
                      if (input.charCodeAt(peg$currPos) === 93) {
                        s5 = peg$c84;
                        peg$currPos++;
                      } else {
                        s5 = peg$FAILED;
                        if (peg$silentFails === 0) { peg$fail(peg$c85); }
                      }
                      if (s5 !== peg$FAILED) {
                        s6 = peg$parse_();
                        if (s6 !== peg$FAILED) {
                          s7 = peg$parseCOLOR_SPEC_RGB();
                          if (s7 !== peg$FAILED) {
                            peg$savedPos = s0;
                            s1 = peg$c245(s7);
                            s0 = s1;
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
              if (s0 === peg$FAILED) {
                s0 = peg$currPos;
                if (input.charCodeAt(peg$currPos) === 91) {
                  s1 = peg$c80;
                  peg$currPos++;
                } else {
                  s1 = peg$FAILED;
                  if (peg$silentFails === 0) { peg$fail(peg$c81); }
                }
                if (s1 !== peg$FAILED) {
                  s2 = peg$parse_();
                  if (s2 !== peg$FAILED) {
                    if (input.substr(peg$currPos, 4).toLowerCase() === peg$c239) {
                      s3 = input.substr(peg$currPos, 4);
                      peg$currPos += 4;
                    } else {
                      s3 = peg$FAILED;
                      if (peg$silentFails === 0) { peg$fail(peg$c240); }
                    }
                    if (s3 !== peg$FAILED) {
                      s4 = peg$parse_();
                      if (s4 !== peg$FAILED) {
                        if (input.charCodeAt(peg$currPos) === 93) {
                          s5 = peg$c84;
                          peg$currPos++;
                        } else {
                          s5 = peg$FAILED;
                          if (peg$silentFails === 0) { peg$fail(peg$c85); }
                        }
                        if (s5 !== peg$FAILED) {
                          s6 = peg$parse_();
                          if (s6 !== peg$FAILED) {
                            s7 = peg$parseCOLOR_SPEC_CMYK();
                            if (s7 !== peg$FAILED) {
                              peg$savedPos = s0;
                              s1 = peg$c246(s7);
                              s0 = s1;
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              }
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCOLOR_SPEC_NAMED() {
      var s0, s1, s2, s3, s4, s5, s6;

      var key    = peg$currPos * 125 + 103,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 123) {
        s1 = peg$c112;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c113); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s3 = [];
          s4 = peg$parsealpha();
          if (s4 !== peg$FAILED) {
            while (s4 !== peg$FAILED) {
              s3.push(s4);
              s4 = peg$parsealpha();
            }
          } else {
            s3 = peg$FAILED;
          }
          if (s3 !== peg$FAILED) {
            s4 = peg$parse_();
            if (s4 !== peg$FAILED) {
              if (input.charCodeAt(peg$currPos) === 125) {
                s5 = peg$c51;
                peg$currPos++;
              } else {
                s5 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c52); }
              }
              if (s5 !== peg$FAILED) {
                s6 = peg$parse_();
                if (s6 !== peg$FAILED) {
                  peg$savedPos = s0;
                  s1 = peg$c247(s3);
                  s0 = s1;
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCOLOR_SPEC_GRAY() {
      var s0, s1, s2, s3, s4;

      var key    = peg$currPos * 125 + 104,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 123) {
        s1 = peg$c112;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c113); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s3 = [];
          s4 = peg$parseCNUM();
          if (s4 !== peg$FAILED) {
            while (s4 !== peg$FAILED) {
              s3.push(s4);
              s4 = peg$parseCNUM();
            }
          } else {
            s3 = peg$FAILED;
          }
          if (s3 !== peg$FAILED) {
            if (input.charCodeAt(peg$currPos) === 125) {
              s4 = peg$c51;
              peg$currPos++;
            } else {
              s4 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c52); }
            }
            if (s4 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c248(s3);
              s0 = s1;
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCOLOR_SPEC_rgb() {
      var s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11;

      var key    = peg$currPos * 125 + 105,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 123) {
        s1 = peg$c112;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c113); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s3 = peg$parseCNUM();
          if (s3 !== peg$FAILED) {
            if (input.charCodeAt(peg$currPos) === 44) {
              s4 = peg$c249;
              peg$currPos++;
            } else {
              s4 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c250); }
            }
            if (s4 !== peg$FAILED) {
              s5 = peg$parse_();
              if (s5 !== peg$FAILED) {
                s6 = peg$parseCNUM();
                if (s6 !== peg$FAILED) {
                  if (input.charCodeAt(peg$currPos) === 44) {
                    s7 = peg$c249;
                    peg$currPos++;
                  } else {
                    s7 = peg$FAILED;
                    if (peg$silentFails === 0) { peg$fail(peg$c250); }
                  }
                  if (s7 !== peg$FAILED) {
                    s8 = peg$parse_();
                    if (s8 !== peg$FAILED) {
                      s9 = peg$parseCNUM();
                      if (s9 !== peg$FAILED) {
                        if (input.charCodeAt(peg$currPos) === 125) {
                          s10 = peg$c51;
                          peg$currPos++;
                        } else {
                          s10 = peg$FAILED;
                          if (peg$silentFails === 0) { peg$fail(peg$c52); }
                        }
                        if (s10 !== peg$FAILED) {
                          s11 = peg$parse_();
                          if (s11 !== peg$FAILED) {
                            peg$savedPos = s0;
                            s1 = peg$c251(s3, s6, s9);
                            s0 = s1;
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCOLOR_SPEC_RGB() {
      var s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11;

      var key    = peg$currPos * 125 + 106,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 123) {
        s1 = peg$c112;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c113); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s3 = peg$parseCNUM255();
          if (s3 !== peg$FAILED) {
            if (input.charCodeAt(peg$currPos) === 44) {
              s4 = peg$c249;
              peg$currPos++;
            } else {
              s4 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c250); }
            }
            if (s4 !== peg$FAILED) {
              s5 = peg$parse_();
              if (s5 !== peg$FAILED) {
                s6 = peg$parseCNUM255();
                if (s6 !== peg$FAILED) {
                  if (input.charCodeAt(peg$currPos) === 44) {
                    s7 = peg$c249;
                    peg$currPos++;
                  } else {
                    s7 = peg$FAILED;
                    if (peg$silentFails === 0) { peg$fail(peg$c250); }
                  }
                  if (s7 !== peg$FAILED) {
                    s8 = peg$parse_();
                    if (s8 !== peg$FAILED) {
                      s9 = peg$parseCNUM255();
                      if (s9 !== peg$FAILED) {
                        if (input.charCodeAt(peg$currPos) === 125) {
                          s10 = peg$c51;
                          peg$currPos++;
                        } else {
                          s10 = peg$FAILED;
                          if (peg$silentFails === 0) { peg$fail(peg$c52); }
                        }
                        if (s10 !== peg$FAILED) {
                          s11 = peg$parse_();
                          if (s11 !== peg$FAILED) {
                            peg$savedPos = s0;
                            s1 = peg$c251(s3, s6, s9);
                            s0 = s1;
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCOLOR_SPEC_CMYK() {
      var s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14;

      var key    = peg$currPos * 125 + 107,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 123) {
        s1 = peg$c112;
        peg$currPos++;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c113); }
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          s3 = peg$parseCNUM();
          if (s3 !== peg$FAILED) {
            if (input.charCodeAt(peg$currPos) === 44) {
              s4 = peg$c249;
              peg$currPos++;
            } else {
              s4 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c250); }
            }
            if (s4 !== peg$FAILED) {
              s5 = peg$parse_();
              if (s5 !== peg$FAILED) {
                s6 = peg$parseCNUM();
                if (s6 !== peg$FAILED) {
                  if (input.charCodeAt(peg$currPos) === 44) {
                    s7 = peg$c249;
                    peg$currPos++;
                  } else {
                    s7 = peg$FAILED;
                    if (peg$silentFails === 0) { peg$fail(peg$c250); }
                  }
                  if (s7 !== peg$FAILED) {
                    s8 = peg$parse_();
                    if (s8 !== peg$FAILED) {
                      s9 = peg$parseCNUM();
                      if (s9 !== peg$FAILED) {
                        if (input.charCodeAt(peg$currPos) === 44) {
                          s10 = peg$c249;
                          peg$currPos++;
                        } else {
                          s10 = peg$FAILED;
                          if (peg$silentFails === 0) { peg$fail(peg$c250); }
                        }
                        if (s10 !== peg$FAILED) {
                          s11 = peg$parse_();
                          if (s11 !== peg$FAILED) {
                            s12 = peg$parseCNUM();
                            if (s12 !== peg$FAILED) {
                              if (input.charCodeAt(peg$currPos) === 125) {
                                s13 = peg$c51;
                                peg$currPos++;
                              } else {
                                s13 = peg$FAILED;
                                if (peg$silentFails === 0) { peg$fail(peg$c52); }
                              }
                              if (s13 !== peg$FAILED) {
                                s14 = peg$parse_();
                                if (s14 !== peg$FAILED) {
                                  peg$savedPos = s0;
                                  s1 = peg$c252(s3, s6, s9, s12);
                                  s0 = s1;
                                } else {
                                  peg$currPos = s0;
                                  s0 = peg$FAILED;
                                }
                              } else {
                                peg$currPos = s0;
                                s0 = peg$FAILED;
                              }
                            } else {
                              peg$currPos = s0;
                              s0 = peg$FAILED;
                            }
                          } else {
                            peg$currPos = s0;
                            s0 = peg$FAILED;
                          }
                        } else {
                          peg$currPos = s0;
                          s0 = peg$FAILED;
                        }
                      } else {
                        peg$currPos = s0;
                        s0 = peg$FAILED;
                      }
                    } else {
                      peg$currPos = s0;
                      s0 = peg$FAILED;
                    }
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              } else {
                peg$currPos = s0;
                s0 = peg$FAILED;
              }
            } else {
              peg$currPos = s0;
              s0 = peg$FAILED;
            }
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCNUM255() {
      var s0, s1, s2, s3, s4, s5, s6;

      var key    = peg$currPos * 125 + 108,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 48) {
        s2 = peg$c253;
        peg$currPos++;
      } else {
        s2 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c254); }
      }
      if (s2 === peg$FAILED) {
        s2 = peg$currPos;
        if (peg$c255.test(input.charAt(peg$currPos))) {
          s3 = input.charAt(peg$currPos);
          peg$currPos++;
        } else {
          s3 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c256); }
        }
        if (s3 !== peg$FAILED) {
          s4 = peg$currPos;
          if (peg$c71.test(input.charAt(peg$currPos))) {
            s5 = input.charAt(peg$currPos);
            peg$currPos++;
          } else {
            s5 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c72); }
          }
          if (s5 !== peg$FAILED) {
            if (peg$c71.test(input.charAt(peg$currPos))) {
              s6 = input.charAt(peg$currPos);
              peg$currPos++;
            } else {
              s6 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c72); }
            }
            if (s6 === peg$FAILED) {
              s6 = null;
            }
            if (s6 !== peg$FAILED) {
              s5 = [s5, s6];
              s4 = s5;
            } else {
              peg$currPos = s4;
              s4 = peg$FAILED;
            }
          } else {
            peg$currPos = s4;
            s4 = peg$FAILED;
          }
          if (s4 === peg$FAILED) {
            s4 = null;
          }
          if (s4 !== peg$FAILED) {
            s3 = [s3, s4];
            s2 = s3;
          } else {
            peg$currPos = s2;
            s2 = peg$FAILED;
          }
        } else {
          peg$currPos = s2;
          s2 = peg$FAILED;
        }
      }
      if (s2 !== peg$FAILED) {
        s1 = input.substring(s1, peg$currPos);
      } else {
        s1 = s2;
      }
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c257(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c258(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCNUM() {
      var s0, s1, s2, s3, s4, s5, s6;

      var key    = peg$currPos * 125 + 109,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$currPos;
      s2 = peg$currPos;
      if (input.charCodeAt(peg$currPos) === 48) {
        s3 = peg$c253;
        peg$currPos++;
      } else {
        s3 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c254); }
      }
      if (s3 === peg$FAILED) {
        s3 = null;
      }
      if (s3 !== peg$FAILED) {
        if (input.charCodeAt(peg$currPos) === 46) {
          s4 = peg$c259;
          peg$currPos++;
        } else {
          s4 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c260); }
        }
        if (s4 !== peg$FAILED) {
          s5 = [];
          if (peg$c71.test(input.charAt(peg$currPos))) {
            s6 = input.charAt(peg$currPos);
            peg$currPos++;
          } else {
            s6 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c72); }
          }
          if (s6 !== peg$FAILED) {
            while (s6 !== peg$FAILED) {
              s5.push(s6);
              if (peg$c71.test(input.charAt(peg$currPos))) {
                s6 = input.charAt(peg$currPos);
                peg$currPos++;
              } else {
                s6 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c72); }
              }
            }
          } else {
            s5 = peg$FAILED;
          }
          if (s5 !== peg$FAILED) {
            s3 = [s3, s4, s5];
            s2 = s3;
          } else {
            peg$currPos = s2;
            s2 = peg$FAILED;
          }
        } else {
          peg$currPos = s2;
          s2 = peg$FAILED;
        }
      } else {
        peg$currPos = s2;
        s2 = peg$FAILED;
      }
      if (s2 !== peg$FAILED) {
        s1 = input.substring(s1, peg$currPos);
      } else {
        s1 = s2;
      }
      if (s1 !== peg$FAILED) {
        s2 = peg$parse_();
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c261(s1);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        s1 = peg$currPos;
        s2 = peg$currPos;
        if (peg$c262.test(input.charAt(peg$currPos))) {
          s3 = input.charAt(peg$currPos);
          peg$currPos++;
        } else {
          s3 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c263); }
        }
        if (s3 !== peg$FAILED) {
          if (input.charCodeAt(peg$currPos) === 46) {
            s4 = peg$c259;
            peg$currPos++;
          } else {
            s4 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c260); }
          }
          if (s4 === peg$FAILED) {
            s4 = null;
          }
          if (s4 !== peg$FAILED) {
            s3 = [s3, s4];
            s2 = s3;
          } else {
            peg$currPos = s2;
            s2 = peg$FAILED;
          }
        } else {
          peg$currPos = s2;
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s1 = input.substring(s1, peg$currPos);
        } else {
          s1 = s2;
        }
        if (s1 !== peg$FAILED) {
          s2 = peg$parse_();
          if (s2 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c261(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_SINGLE_MACRO() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 110,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c264(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c175(s1);
          s0 = s1;
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        if (input.charCodeAt(peg$currPos) === 92) {
          s1 = peg$c159;
          peg$currPos++;
        } else {
          s1 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c160); }
        }
        if (s1 !== peg$FAILED) {
          if (peg$c161.test(input.charAt(peg$currPos))) {
            s2 = input.charAt(peg$currPos);
            peg$currPos++;
          } else {
            s2 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c162); }
          }
          if (s2 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c265(s2);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_BOND() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 111,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c266(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_MACRO_1P() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 112,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c267(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_MACRO_2P() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 113,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c268(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_MACRO_2PU() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 114,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c269(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_MACRO_2PC() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 115,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parsegeneric_func();
      if (s1 !== peg$FAILED) {
        peg$savedPos = peg$currPos;
        s2 = peg$c270(s1);
        if (s2) {
          s2 = void 0;
        } else {
          s2 = peg$FAILED;
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c175(s1);
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_SCRIPT_FOLLOW() {
      var s0;

      var key    = peg$currPos * 125 + 116,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$parseliteral_mn();
      if (s0 === peg$FAILED) {
        s0 = peg$parseliteral_id();
        if (s0 === peg$FAILED) {
          if (peg$c271.test(input.charAt(peg$currPos))) {
            s0 = input.charAt(peg$currPos);
            peg$currPos++;
          } else {
            s0 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c272); }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_SUPERSUB() {
      var s0;

      var key    = peg$currPos * 125 + 117,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (input.charCodeAt(peg$currPos) === 95) {
        s0 = peg$c108;
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c109); }
      }
      if (s0 === peg$FAILED) {
        if (input.charCodeAt(peg$currPos) === 94) {
          s0 = peg$c95;
          peg$currPos++;
        } else {
          s0 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c96); }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_BOND_TYPE() {
      var s0;

      var key    = peg$currPos * 125 + 118,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (input.charCodeAt(peg$currPos) === 61) {
        s0 = peg$c273;
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c274); }
      }
      if (s0 === peg$FAILED) {
        if (input.charCodeAt(peg$currPos) === 35) {
          s0 = peg$c275;
          peg$currPos++;
        } else {
          s0 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c276); }
        }
        if (s0 === peg$FAILED) {
          if (input.substr(peg$currPos, 3) === peg$c277) {
            s0 = peg$c277;
            peg$currPos += 3;
          } else {
            s0 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c278); }
          }
          if (s0 === peg$FAILED) {
            if (input.substr(peg$currPos, 2) === peg$c279) {
              s0 = peg$c279;
              peg$currPos += 2;
            } else {
              s0 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c280); }
            }
            if (s0 === peg$FAILED) {
              if (input.substr(peg$currPos, 2) === peg$c281) {
                s0 = peg$c281;
                peg$currPos += 2;
              } else {
                s0 = peg$FAILED;
                if (peg$silentFails === 0) { peg$fail(peg$c282); }
              }
              if (s0 === peg$FAILED) {
                if (input.charCodeAt(peg$currPos) === 126) {
                  s0 = peg$c283;
                  peg$currPos++;
                } else {
                  s0 = peg$FAILED;
                  if (peg$silentFails === 0) { peg$fail(peg$c284); }
                }
                if (s0 === peg$FAILED) {
                  if (input.substr(peg$currPos, 3) === peg$c285) {
                    s0 = peg$c285;
                    peg$currPos += 3;
                  } else {
                    s0 = peg$FAILED;
                    if (peg$silentFails === 0) { peg$fail(peg$c286); }
                  }
                  if (s0 === peg$FAILED) {
                    if (input.substr(peg$currPos, 4) === peg$c287) {
                      s0 = peg$c287;
                      peg$currPos += 4;
                    } else {
                      s0 = peg$FAILED;
                      if (peg$silentFails === 0) { peg$fail(peg$c288); }
                    }
                    if (s0 === peg$FAILED) {
                      if (input.substr(peg$currPos, 3) === peg$c289) {
                        s0 = peg$c289;
                        peg$currPos += 3;
                      } else {
                        s0 = peg$FAILED;
                        if (peg$silentFails === 0) { peg$fail(peg$c290); }
                      }
                      if (s0 === peg$FAILED) {
                        if (input.substr(peg$currPos, 2) === peg$c291) {
                          s0 = peg$c291;
                          peg$currPos += 2;
                        } else {
                          s0 = peg$FAILED;
                          if (peg$silentFails === 0) { peg$fail(peg$c292); }
                        }
                        if (s0 === peg$FAILED) {
                          if (input.substr(peg$currPos, 2) === peg$c293) {
                            s0 = peg$c293;
                            peg$currPos += 2;
                          } else {
                            s0 = peg$FAILED;
                            if (peg$silentFails === 0) { peg$fail(peg$c294); }
                          }
                          if (s0 === peg$FAILED) {
                            if (input.charCodeAt(peg$currPos) === 45) {
                              s0 = peg$c134;
                              peg$currPos++;
                            } else {
                              s0 = peg$FAILED;
                              if (peg$silentFails === 0) { peg$fail(peg$c135); }
                            }
                            if (s0 === peg$FAILED) {
                              if (input.charCodeAt(peg$currPos) === 49) {
                                s0 = peg$c295;
                                peg$currPos++;
                              } else {
                                s0 = peg$FAILED;
                                if (peg$silentFails === 0) { peg$fail(peg$c296); }
                              }
                              if (s0 === peg$FAILED) {
                                if (input.charCodeAt(peg$currPos) === 50) {
                                  s0 = peg$c297;
                                  peg$currPos++;
                                } else {
                                  s0 = peg$FAILED;
                                  if (peg$silentFails === 0) { peg$fail(peg$c298); }
                                }
                                if (s0 === peg$FAILED) {
                                  if (input.charCodeAt(peg$currPos) === 51) {
                                    s0 = peg$c299;
                                    peg$currPos++;
                                  } else {
                                    s0 = peg$FAILED;
                                    if (peg$silentFails === 0) { peg$fail(peg$c300); }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseBEGIN_MATH() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 119,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseBEGIN();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 6) === peg$c301) {
          s2 = peg$c301;
          peg$currPos += 6;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c302); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEND_MATH() {
      var s0, s1, s2, s3;

      var key    = peg$currPos * 125 + 120,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      s1 = peg$parseEND();
      if (s1 !== peg$FAILED) {
        if (input.substr(peg$currPos, 6) === peg$c301) {
          s2 = peg$c301;
          peg$currPos += 6;
        } else {
          s2 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c302); }
        }
        if (s2 !== peg$FAILED) {
          s3 = peg$parse_();
          if (s3 !== peg$FAILED) {
            s1 = [s1, s2, s3];
            s0 = s1;
          } else {
            peg$currPos = s0;
            s0 = peg$FAILED;
          }
        } else {
          peg$currPos = s0;
          s0 = peg$FAILED;
        }
      } else {
        peg$currPos = s0;
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_LETTER() {
      var s0;

      var key    = peg$currPos * 125 + 121,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      if (peg$c115.test(input.charAt(peg$currPos))) {
        s0 = input.charAt(peg$currPos);
        peg$currPos++;
      } else {
        s0 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c116); }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseCHEM_NONLETTER() {
      var s0, s1, s2;

      var key    = peg$currPos * 125 + 122,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      s0 = peg$currPos;
      if (input.substr(peg$currPos, 2) === peg$c140) {
        s1 = peg$c140;
        peg$currPos += 2;
      } else {
        s1 = peg$FAILED;
        if (peg$silentFails === 0) { peg$fail(peg$c141); }
      }
      if (s1 !== peg$FAILED) {
        peg$savedPos = s0;
        s1 = peg$c303(s1);
      }
      s0 = s1;
      if (s0 === peg$FAILED) {
        s0 = peg$currPos;
        if (input.substr(peg$currPos, 2) === peg$c304) {
          s1 = peg$c304;
          peg$currPos += 2;
        } else {
          s1 = peg$FAILED;
          if (peg$silentFails === 0) { peg$fail(peg$c305); }
        }
        if (s1 !== peg$FAILED) {
          peg$savedPos = s0;
          s1 = peg$c303(s1);
        }
        s0 = s1;
        if (s0 === peg$FAILED) {
          s0 = peg$currPos;
          if (input.substr(peg$currPos, 2) === peg$c179) {
            s1 = peg$c179;
            peg$currPos += 2;
          } else {
            s1 = peg$FAILED;
            if (peg$silentFails === 0) { peg$fail(peg$c180); }
          }
          if (s1 !== peg$FAILED) {
            peg$savedPos = s0;
            s1 = peg$c303(s1);
          }
          s0 = s1;
          if (s0 === peg$FAILED) {
            s0 = peg$currPos;
            if (peg$c306.test(input.charAt(peg$currPos))) {
              s1 = input.charAt(peg$currPos);
              peg$currPos++;
            } else {
              s1 = peg$FAILED;
              if (peg$silentFails === 0) { peg$fail(peg$c307); }
            }
            if (s1 !== peg$FAILED) {
              peg$savedPos = s0;
              s1 = peg$c303(s1);
            }
            s0 = s1;
            if (s0 === peg$FAILED) {
              s0 = peg$currPos;
              s1 = peg$parseliteral_mn();
              if (s1 !== peg$FAILED) {
                peg$savedPos = s0;
                s1 = peg$c303(s1);
              }
              s0 = s1;
              if (s0 === peg$FAILED) {
                s0 = peg$currPos;
                s1 = peg$parseCURLY_OPEN();
                if (s1 !== peg$FAILED) {
                  s2 = peg$parseCURLY_CLOSE();
                  if (s2 !== peg$FAILED) {
                    peg$savedPos = s0;
                    s1 = peg$c308();
                    s0 = s1;
                  } else {
                    peg$currPos = s0;
                    s0 = peg$FAILED;
                  }
                } else {
                  peg$currPos = s0;
                  s0 = peg$FAILED;
                }
              }
            }
          }
        }
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseimpossible() {
      var s0;

      var key    = peg$currPos * 125 + 123,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      peg$savedPos = peg$currPos;
      s0 = peg$c309();
      if (s0) {
        s0 = void 0;
      } else {
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }

    function peg$parseEOF() {
      var s0;

      var key    = peg$currPos * 125 + 124,
          cached = peg$resultsCache[key];

      if (cached) {
        peg$currPos = cached.nextPos;

        return cached.result;
      }

      peg$savedPos = peg$currPos;
      s0 = peg$c310();
      if (s0) {
        s0 = void 0;
      } else {
        s0 = peg$FAILED;
      }

      peg$resultsCache[key] = { nextPos: peg$currPos, result: s0 };

      return s0;
    }


      var ast = require('./ast');
      var tu = require('./texutil');

      var lst2arr = function(l) {
        var arr = [];
        while (l !== null) {
          arr.push(l.head);
          l = l.tail;
        }
        return arr;
      };


    peg$result = peg$startRuleFunction();

    if (peg$result !== peg$FAILED && peg$currPos === input.length) {
      return peg$result;
    } else {
      if (peg$result !== peg$FAILED && peg$currPos < input.length) {
        peg$fail(peg$endExpectation());
      }

      throw peg$buildStructuredError(
        peg$maxFailExpected,
        peg$maxFailPos < input.length ? input.charAt(peg$maxFailPos) : null,
        peg$maxFailPos < input.length
          ? peg$computeLocation(peg$maxFailPos, peg$maxFailPos + 1)
          : peg$computeLocation(peg$maxFailPos, peg$maxFailPos)
      );
    }
  }

  return {
    SyntaxError: peg$SyntaxError,
    parse:       peg$parse
  };
})();
},{"./ast":2,"./texutil":7}],6:[function(require,module,exports){
// Render an AST.
'use strict';

const ast = require('./ast');

ast.RenderT.defineVisitor('tex_part', {
    TEX_ONLY: function (t) { return t; }
});

const render = module.exports = function render(e) {
    if (Array.isArray(e)) {
        return e.map(render).join('');
    }
    return e.render_tex();
};

const curlies = function (t) {
    switch (t.constructor) {
        // constructs which are surrounded by curlies
        case ast.Tex.FUN1:
        case ast.Tex.FUN1hl:
        case ast.Tex.FUN1hf:
        case ast.Tex.DECLh:
        case ast.Tex.FUN2:
        case ast.Tex.FUN2h:
        case ast.Tex.FUN2sq:
        case ast.Tex.CURLY:
        case ast.Tex.INFIX:
        case ast.Tex.INFIXh:
        case ast.Tex.BOX:
        case ast.Tex.BIG:
        case ast.Tex.MATRIX:
            return t.render_tex();
        case String:
            break;
        default:
            t = t.render_tex();
    }
    return '{' + t + '}';
};

const renderCurlies = function (a) {
    if (a.length === 1) {
        return curlies(a[0]);
    }
    return curlies(render(a));
};

const dollar = function (t) {
    switch (t.constructor) {
        case String:
            break;
        default:
            t = t.render_tex();
    }
    return '$' + t + '$';
};

const renderDollar = function (a) {
    if (a.length === 1) {
        return dollar(a[0]);
    }
    return dollar(render(a));
};

ast.Tex.defineVisitor('render_tex', {
    FQ: function (base, down, up) {
        return base.render_tex() + '_' + curlies(down) + '^' + curlies(up);
    },
    DQ: function (base, down) {
        return base.render_tex() + '_' + curlies(down);
    },
    UQ: function (base, up) {
        return base.render_tex() + '^' + curlies(up);
    },
    FQN: function (down, up) {
        return '_' + curlies(down) + '^' + curlies(up);
    },
    DQN: function (down) {
        return '_' + curlies(down);
    },
    UQN: function (up) {
        return '^' + curlies(up);
    },
    LITERAL: function (r) {
        return r.tex_part();
    },
    FUN1: function (f, a) {
        return curlies(f + ' ' + curlies(a));
    },
    MHCHEM: function (f, a) {
        return curlies(f + ' ' + curlies(a));
    },
    CHEM_WORD: function (l, w) {
        return l.render_tex() + w.render_tex();
    },
    CHEM_FUN2u: function (f, a, b) {
        return f + curlies(a) + '_' + curlies(b);
    },
    FUN1nb: function (f, a) {
        return f + ' ' + curlies(a) + ' ';
    },
    DECLh: function (f, _, a) {
        return curlies(f + ' ' + renderCurlies(a));
    },
    FUN2: function (f, a, b) {
        return curlies(f + ' ' + curlies(a) + curlies(b));
    },
    FUN2nb: function (f, a, b) {
        return f + ' ' + curlies(a) + curlies(b);
    },
    FUN2sq: function (f, a, b) {
        return curlies(f + '[' + a.render_tex() + ']' + curlies(b));
    },
    CURLY: function (tl) {
        return renderCurlies(tl);
    },
    DOLLAR: function (tl) {
        return renderDollar(tl);
    },
    INFIX: function (s, ll, rl) {
        return curlies(render(ll) + ' ' + s + ' ' + render(rl));
    },
    BOX: function (bt, s) {
        return curlies(bt + curlies(s));
    },
    BIG: function (bt, d) {
        return curlies(bt + ' ' + d.tex_part());
    },
    MATRIX: function (t, m) {
        const renderLine = function (l) {
            return l.map(render).join('&');
        };
        const renderMatrix = function (matrix) {
            return matrix.map(renderLine).join('\\\\');
        };
        return curlies('\\begin{' + t + '}' + renderMatrix(m) + '\\end{' + t + '}');
    },
    LR: function (l, r, tl) {
        return '\\left' + l.tex_part() + render(tl) + '\\right' + r.tex_part();
    }
});

},{"./ast":2}],7:[function(require,module,exports){
// Information about TeX functions.
// In its own module so that the sets aren't recreated from scratch
// every time that parse() is called.
'use strict';

// track all known function names, so we can give good errors for unknown
// functions.
const allFunctions = module.exports.all_functions = Object.create(null);
allFunctions['\\begin'] = allFunctions['\\end'] = true;

const arr2set = function (a) {
    // note that the fact that all keys in the set are prefixed with '\\'
    // helps avoid accidental name conflicts.  But use Object.create(null)
    // to be extra safe.
    const set = Object.create(null);
    a.forEach(function (v) {
        console.assert(!set[v], v);
        set[v] = allFunctions[v] = true;
    });
    return set;
};
const obj2map = function (o) {
    // this just recreates the argument, but with `null` as prototype.
    const map = Object.create(null);
    Object.keys(o).forEach(function (f) {
        console.assert(!map[f]);
        map[f] = o[f];
        allFunctions[f] = true;
    });
    return map;
};

// Sets of function names
module.exports.box_functions = arr2set([
    '\\text', '\\mbox', '\\hbox', '\\vbox'
]);

module.exports.latex_function_names = arr2set([
    '\\arccos', '\\arcsin', '\\arctan', '\\arg', '\\cosh', '\\cos',
    '\\cot', '\\coth', '\\csc', '\\deg', '\\det', '\\dim', '\\exp',
    '\\gcd', '\\hom', '\\inf', '\\ker', '\\lg', '\\lim', '\\liminf',
    '\\limsup', '\\ln', '\\log', '\\max', '\\min', '\\Pr', '\\sec',
    '\\sin', '\\sinh', '\\sup', '\\tan', '\\tanh'
]);

module.exports.mediawiki_function_names = arr2set([
    '\\arccot', '\\arcsec', '\\arccsc', '\\sgn', '\\sen'
]);

module.exports.nullary_macro = arr2set([
    '\\aleph',
    '\\alpha',
    '\\amalg',
    '\\And',
    '\\angle',
    '\\approx',
    '\\approxeq',
    '\\ast',
    '\\asymp',
    '\\backepsilon',
    '\\backprime',
    '\\backsim',
    '\\backsimeq',
    '\\barwedge',
    '\\Bbbk',
    '\\because',
    '\\beta',
    '\\beth',
    '\\between',
    '\\bigcap',
    '\\bigcirc',
    '\\bigcup',
    '\\bigodot',
    '\\bigoplus',
    '\\bigotimes',
    '\\bigsqcup',
    '\\bigstar',
    '\\bigtriangledown',
    '\\bigtriangleup',
    '\\biguplus',
    '\\bigvee',
    '\\bigwedge',
    '\\blacklozenge',
    '\\blacksquare',
    '\\blacktriangle',
    '\\blacktriangledown',
    '\\blacktriangleleft',
    '\\blacktriangleright',
    '\\bot',
    '\\bowtie',
    '\\Box',
    '\\boxdot',
    '\\boxminus',
    '\\boxplus',
    '\\boxtimes',
    '\\bullet',
    '\\bumpeq',
    '\\Bumpeq',
    '\\cap',
    '\\Cap',
    '\\cdot',
    '\\cdots',
    '\\centerdot',
    '\\checkmark',
    '\\chi',
    '\\circ',
    '\\circeq',
    '\\circlearrowleft',
    '\\circlearrowright',
    '\\circledast',
    '\\circledcirc',
    '\\circleddash',
    '\\circledS',
    '\\clubsuit',
    '\\colon',
    '\\complement',
    '\\cong',
    '\\coprod',
    '\\cup',
    '\\Cup',
    '\\curlyeqprec',
    '\\curlyeqsucc',
    '\\curlyvee',
    '\\curlywedge',
    '\\curvearrowleft',
    '\\curvearrowright',
    '\\dagger',
    '\\daleth',
    '\\dashv',
    '\\ddagger',
    '\\ddots',
    '\\delta',
    '\\Delta',
    '\\diagdown',
    '\\diagup',
    '\\diamond',
    '\\Diamond',
    '\\diamondsuit',
    '\\digamma',
    '\\displaystyle',
    '\\div',
    '\\divideontimes',
    '\\doteq',
    '\\doteqdot',
    '\\dotplus',
    '\\dots',
    '\\dotsb',
    '\\dotsc',
    '\\dotsi',
    '\\dotsm',
    '\\dotso',
    '\\doublebarwedge',
    '\\downdownarrows',
    '\\downharpoonleft',
    '\\downharpoonright',
    '\\ell',
    '\\emptyset',
    '\\epsilon',
    '\\eqcirc',
    '\\eqsim',
    '\\eqslantgtr',
    '\\eqslantless',
    '\\equiv',
    '\\eta',
    '\\eth',
    '\\exists',
    '\\fallingdotseq',
    '\\Finv',
    '\\flat',
    '\\forall',
    '\\frown',
    '\\Game',
    '\\gamma',
    '\\Gamma',
    '\\geq',
    '\\geqq',
    '\\geqslant',
    '\\gets',
    '\\gg',
    '\\ggg',
    '\\gimel',
    '\\gnapprox',
    '\\gneq',
    '\\gneqq',
    '\\gnsim',
    '\\gtrapprox',
    '\\gtrdot',
    '\\gtreqless',
    '\\gtreqqless',
    '\\gtrless',
    '\\gtrsim',
    '\\gvertneqq',
    '\\hbar',
    '\\heartsuit',
    // "\\hline", // moved to hline_function
    '\\hookleftarrow',
    '\\hookrightarrow',
    '\\hslash',
    '\\iff',
    '\\iiiint',
    '\\iiint',
    '\\iint',
    '\\Im',
    '\\imath',
    '\\implies',
    '\\in',
    '\\infty',
    '\\injlim',
    '\\int',
    '\\intbar',
    '\\intercal',
    '\\iota',
    '\\jmath',
    '\\kappa',
    '\\lambda',
    '\\Lambda',
    '\\land',
    '\\ldots',
    '\\leftarrow',
    '\\Leftarrow',
    '\\leftarrowtail',
    '\\leftharpoondown',
    '\\leftharpoonup',
    '\\leftleftarrows',
    '\\leftrightarrow',
    '\\Leftrightarrow',
    '\\leftrightarrows',
    '\\leftrightharpoons',
    '\\leftrightsquigarrow',
    '\\leftthreetimes',
    '\\leq',
    '\\leqq',
    '\\leqslant',
    '\\lessapprox',
    '\\lessdot',
    '\\lesseqgtr',
    '\\lesseqqgtr',
    '\\lessgtr',
    '\\lesssim',
    '\\limits', // XXX only valid in certain contexts
    '\\ll',
    '\\Lleftarrow',
    '\\lll',
    '\\lnapprox',
    '\\lneq',
    '\\lneqq',
    '\\lnot',
    '\\lnsim',
    '\\longleftarrow',
    '\\Longleftarrow',
    '\\longleftrightarrow',
    '\\Longleftrightarrow',
    '\\longmapsto',
    '\\longrightarrow',
    '\\Longrightarrow',
    '\\looparrowleft',
    '\\looparrowright',
    '\\lor',
    '\\lozenge',
    '\\Lsh',
    '\\ltimes',
    '\\lVert',
    '\\lvertneqq',
    '\\mapsto',
    '\\measuredangle',
    '\\mho',
    '\\mid',
    '\\mod',
    '\\models',
    '\\mp',
    '\\mu',
    '\\multimap',
    '\\nabla',
    '\\natural',
    '\\ncong',
    '\\nearrow',
    '\\neg',
    '\\neq',
    '\\nexists',
    '\\ngeq',
    '\\ngeqq',
    '\\ngeqslant',
    '\\ngtr',
    '\\ni',
    '\\nleftarrow',
    '\\nLeftarrow',
    '\\nleftrightarrow',
    '\\nLeftrightarrow',
    '\\nleq',
    '\\nleqq',
    '\\nleqslant',
    '\\nless',
    '\\nmid',
    '\\nolimits', // XXX see \limits
    '\\not',
    '\\notin',
    '\\nparallel',
    '\\nprec',
    '\\npreceq',
    '\\nrightarrow',
    '\\nRightarrow',
    '\\nshortmid',
    '\\nshortparallel',
    '\\nsim',
    '\\nsubseteq',
    '\\nsubseteqq',
    '\\nsucc',
    '\\nsucceq',
    '\\nsupseteq',
    '\\nsupseteqq',
    '\\ntriangleleft',
    '\\ntrianglelefteq',
    '\\ntriangleright',
    '\\ntrianglerighteq',
    '\\nu',
    '\\nvdash',
    '\\nVdash',
    '\\nvDash',
    '\\nVDash',
    '\\nwarrow',
    '\\odot',
    '\\oint',
    '\\oiint',
    '\\oiiint',
    '\\ointctrclockwise',
    '\\omega',
    '\\Omega',
    '\\ominus',
    '\\oplus',
    '\\oslash',
    '\\otimes',
    // "\\overbrace", // moved to ar1nb (grabs trailing sub/superscript)
    '\\P',
    '\\parallel',
    '\\partial',
    '\\perp',
    '\\phi',
    '\\Phi',
    '\\pi',
    '\\Pi',
    '\\pitchfork',
    '\\pm',
    '\\prec',
    '\\precapprox',
    '\\preccurlyeq',
    '\\preceq',
    '\\precnapprox',
    '\\precneqq',
    '\\precnsim',
    '\\precsim',
    '\\prime',
    '\\prod',
    '\\projlim',
    '\\propto',
    '\\psi',
    '\\Psi',
    '\\qquad',
    '\\quad',
    '\\Re',
    '\\rho',
    '\\rightarrow',
    '\\Rightarrow',
    '\\rightarrowtail',
    '\\rightharpoondown',
    '\\rightharpoonup',
    '\\rightleftarrows',
    '\\rightrightarrows',
    '\\rightsquigarrow',
    '\\rightthreetimes',
    '\\risingdotseq',
    '\\Rrightarrow',
    '\\Rsh',
    '\\rtimes',
    '\\rVert',
    '\\S',
    '\\scriptscriptstyle',
    '\\scriptstyle',
    '\\searrow',
    '\\setminus',
    '\\sharp',
    '\\shortmid',
    '\\shortparallel',
    '\\sigma',
    '\\Sigma',
    '\\sim',
    '\\simeq',
    '\\smallfrown',
    '\\smallsetminus',
    '\\smallsmile',
    '\\smile',
    '\\spadesuit',
    '\\sphericalangle',
    '\\sqcap',
    '\\sqcup',
    '\\sqsubset',
    '\\sqsubseteq',
    '\\sqsupset',
    '\\sqsupseteq',
    '\\square',
    '\\star',
    '\\strokeint',
    '\\subset',
    '\\Subset',
    '\\subseteq',
    '\\subseteqq',
    '\\subsetneq',
    '\\subsetneqq',
    '\\succ',
    '\\succapprox',
    '\\succcurlyeq',
    '\\succeq',
    '\\succnapprox',
    '\\succneqq',
    '\\succnsim',
    '\\succsim',
    '\\sum',
    '\\supset',
    '\\Supset',
    '\\supseteq',
    '\\supseteqq',
    '\\supsetneq',
    '\\supsetneqq',
    '\\surd',
    '\\swarrow',
    '\\tau',
    '\\textstyle',
    '\\therefore',
    '\\theta',
    '\\Theta',
    '\\thickapprox',
    '\\thicksim',
    '\\times',
    '\\to',
    '\\top',
    '\\triangle',
    '\\triangledown',
    '\\triangleleft',
    '\\trianglelefteq',
    '\\triangleq',
    '\\triangleright',
    '\\trianglerighteq',
    // "\\underbrace", // moved to ar1nb (grabs trailing sub/superscript)
    '\\upharpoonleft',
    '\\upharpoonright',
    '\\uplus',
    '\\upsilon',
    '\\Upsilon',
    '\\upuparrows',
    '\\varDelta',
    '\\varepsilon',
    '\\varGamma',
    '\\varinjlim',
    '\\varkappa',
    '\\varLambda',
    '\\varliminf',
    '\\varlimsup',
    '\\varnothing',
    '\\varOmega',
    '\\varointclockwise',
    '\\varphi',
    '\\varPhi',
    '\\varpi',
    '\\varPi',
    '\\varprojlim',
    '\\varpropto',
    '\\varrho',
    '\\varsigma',
    '\\varSigma',
    '\\varsubsetneq',
    '\\varsubsetneqq',
    '\\varsupsetneq',
    '\\varsupsetneqq',
    '\\vartheta',
    '\\varTheta',
    '\\vartriangle',
    '\\vartriangleleft',
    '\\vartriangleright',
    '\\varUpsilon',
    '\\varXi',
    '\\vdash',
    '\\Vdash',
    '\\vDash',
    '\\vdots',
    '\\vee',
    '\\veebar',
    '\\vline',
    '\\Vvdash',
    '\\wedge',
    '\\wp',
    '\\wr',
    '\\xi',
    '\\Xi',
    '\\zeta'
]);

// text-mode literals; enclose in \mbox
module.exports.nullary_macro_in_mbox = arr2set([
    '\\AA',
    '\\Coppa',
    '\\coppa',
    '\\Digamma',
    '\\euro',
    '\\geneuro',
    '\\geneuronarrow',
    '\\geneurowide',
    '\\Koppa',
    '\\koppa',
    '\\officialeuro',
    '\\Sampi',
    '\\sampi',
    '\\Stigma',
    '\\stigma',
    '\\textvisiblespace',
    '\\varstigma'
]);

module.exports.nullary_macro_aliase = obj2map({
    '\\N': '\\mathbb{N}',
    '\\Q': '\\mathbb{Q}',
    '\\R': '\\mathbb{R}',
    '\\Z': '\\mathbb{Z}',
    '\\alef': '\\aleph',
    '\\alefsym': '\\aleph',
    '\\Alpha': '\\mathrm{A}',
    '\\Beta': '\\mathrm{B}',
    '\\bull': '\\bullet',
    '\\Chi': '\\mathrm{X}',
    '\\clubs': '\\clubsuit',
    '\\cnums': '\\mathbb{C}',
    '\\Complex': '\\mathbb{C}',
    '\\Dagger': '\\ddagger',
    '\\diamonds': '\\diamondsuit',
    '\\Doteq': '\\doteqdot',
    '\\doublecap': '\\Cap',
    '\\doublecup': '\\Cup',
    '\\empty': '\\emptyset',
    '\\Epsilon': '\\mathrm{E}',
    '\\Eta': '\\mathrm{H}',
    '\\exist': '\\exists',
    '\\ge': '\\geq',
    '\\gggtr': '\\ggg',
    '\\hArr': '\\Leftrightarrow', // OCaML texvc had \hAar here.
    '\\harr': '\\leftrightarrow',
    '\\Harr': '\\Leftrightarrow',
    '\\hearts': '\\heartsuit',
    '\\image': '\\Im',
    '\\infin': '\\infty',
    '\\Iota': '\\mathrm{I}',
    '\\isin': '\\in',
    '\\Kappa': '\\mathrm{K}',
    '\\larr': '\\leftarrow',
    '\\Larr': '\\Leftarrow',
    '\\lArr': '\\Leftarrow',
    '\\le': '\\leq',
    '\\lrarr': '\\leftrightarrow',
    '\\Lrarr': '\\Leftrightarrow',
    '\\lrArr': '\\Leftrightarrow',
    '\\Mu': '\\mathrm{M}',
    '\\natnums': '\\mathbb{N}',
    '\\ne': '\\neq',
    '\\Nu': '\\mathrm{N}',
    '\\O': '\\emptyset',
    '\\omicron': '\\mathrm{o}',
    '\\Omicron': '\\mathrm{O}',
    '\\plusmn': '\\pm',
    '\\rarr': '\\rightarrow',
    '\\Rarr': '\\Rightarrow',
    '\\rArr': '\\Rightarrow',
    '\\real': '\\Re',
    '\\reals': '\\mathbb{R}',
    '\\Reals': '\\mathbb{R}',
    '\\restriction': '\\upharpoonright',
    '\\Rho': '\\mathrm{P}',
    '\\sdot': '\\cdot',
    '\\sect': '\\S',
    '\\spades': '\\spadesuit',
    '\\sub': '\\subset',
    '\\sube': '\\subseteq',
    '\\supe': '\\supseteq',
    '\\Tau': '\\mathrm{T}',
    '\\thetasym': '\\vartheta',
    '\\varcoppa': '\\mbox{\\coppa}',
    '\\weierp': '\\wp',
    '\\Zeta': '\\mathrm{Z}'
});

// Deprecated via T197842
module.exports.deprecated_nullary_macro_aliase = obj2map({
    '\\C': '\\mathbb{C}',
    '\\H': '\\mathbb{H}',
    '\\and': '\\land',
    '\\ang': '\\angle',
    '\\or': '\\lor',
    '\\part': '\\partial'
});

module.exports.big_literals = arr2set([
    '\\big',
    '\\Big',
    '\\bigg',
    '\\Bigg',
    '\\biggl',
    '\\Biggl',
    '\\biggr',
    '\\Biggr',
    '\\bigl',
    '\\Bigl',
    '\\bigr',
    '\\Bigr'
]);

module.exports.other_delimiters1 = arr2set([
    '\\backslash',
    '\\downarrow',
    '\\Downarrow',
    '\\langle',
    '\\lbrace',
    '\\lceil',
    '\\lfloor',
    '\\llcorner',
    '\\lrcorner',
    '\\rangle',
    '\\rbrace',
    '\\rceil',
    '\\rfloor',
    '\\rightleftharpoons',
    '\\twoheadleftarrow',
    '\\twoheadrightarrow',
    '\\ulcorner',
    '\\uparrow',
    '\\Uparrow',
    '\\updownarrow',
    '\\Updownarrow',
    '\\urcorner',
    '\\Vert',
    '\\vert',
    '\\lbrack',
    '\\rbrack'
]);

module.exports.other_delimiters2 = obj2map({
    '\\darr': '\\downarrow',
    '\\dArr': '\\Downarrow',
    '\\Darr': '\\Downarrow',
    '\\lang': '\\langle',
    '\\rang': '\\rangle',
    '\\uarr': '\\uparrow',
    '\\uArr': '\\Uparrow',
    '\\Uarr': '\\Uparrow'
});

module.exports.fun_ar1 = arr2set([
    '\\acute',
    '\\bar',
    '\\bcancel',
    '\\bmod',
    '\\boldsymbol',
    '\\breve',
    '\\cancel',
    // "\\ce",  // moved to fun_mhchem
    '\\check',
    '\\ddot',
    '\\dot',
    '\\emph',
    '\\grave',
    '\\hat',
    // "\\mathbb", // moved to fun_ar1nb
    // "\\mathbf", // moved to fun_ar1nb
    // '\\mathbin', // moved to fun_ar1nb
    '\\mathcal',
    '\\mathclose',
    '\\mathfrak',
    '\\mathit',
    //    "\\mathop", // moved to fun_ar1nb
    '\\mathopen',
    '\\mathord',
    '\\mathpunct',
    // '\\mathrel', // moved to fun_ar1nb
    // "\\mathrm", // moved to fun_ar1nb
    '\\mathsf',
    '\\mathtt',
    // "\\operatorname", // already exists in fun_ar1nb
    '\\overleftarrow',
    '\\overleftrightarrow',
    '\\overline',
    '\\overrightarrow',
    '\\pmod',
    '\\sqrt',
    '\\textbf',
    '\\textit',
    '\\textrm',
    '\\textsf',
    '\\texttt',
    '\\tilde',
    '\\underline',
    '\\vec',
    '\\widehat',
    '\\widetilde',
    '\\xcancel'
    // '\\xleftarrow', // moved to fun_ar1nb
    // '\\xrightarrow' // moved to fun_ar1nb
]);

module.exports.fun_mhchem = arr2set([
    '\\ce'
]);

// Deprecated via T197842
module.exports.other_fun_ar1 = obj2map({
    '\\Bbb': '\\mathbb',
    '\\bold': '\\mathbf'
});

module.exports.fun_ar1nb = arr2set([
    '\\operatorname',
    '\\mathop',
    '\\overbrace',
    '\\mathbb',
    '\\mathbf',
    '\\mathrm',
    '\\underbrace',
    '\\xleftarrow',
    '\\xrightarrow',
    '\\mathbin',
    '\\mathrel'
]);

module.exports.fun_ar1opt = arr2set([
    '\\sqrt', '\\xleftarrow', '\\xrightarrow'
]);

module.exports.fun_ar2 = arr2set([
    '\\binom',
    '\\cancelto',
    '\\cfrac',
    '\\dbinom',
    '\\dfrac',
    '\\frac',
    '\\overset',
    '\\stackrel',
    '\\tbinom',
    '\\tfrac',
    '\\underset'
]);

module.exports.fun_ar2nb = arr2set([
    '\\sideset'
]);

module.exports.fun_infix = arr2set([
    '\\atop',
    '\\choose',
    '\\over'
]);

module.exports.declh_function = arr2set([
    '\\rm',
    '\\it',
    '\\cal',
    '\\bf'
]);

module.exports.left_function = arr2set(['\\left']);
module.exports.right_function = arr2set(['\\right']);
module.exports.hline_function = arr2set(['\\hline']);
module.exports.definecolor_function = arr2set(['\\definecolor']);
module.exports.color_function = arr2set(['\\color', '\\pagecolor']);

// ------------------------------------------------------
// Package dependencies for various allowed commands.

module.exports.ams_required = arr2set([
    '\\text',
    '\\begin{matrix}',
    '\\begin{pmatrix}',
    '\\begin{bmatrix}',
    '\\begin{Bmatrix}',
    '\\begin{vmatrix}',
    '\\begin{Vmatrix}',
    '\\begin{aligned}',
    '\\begin{alignedat}',
    '\\begin{smallmatrix}',
    '\\begin{cases}',

    '\\ulcorner',
    '\\urcorner',
    '\\llcorner',
    '\\lrcorner',
    '\\twoheadleftarrow',
    '\\twoheadrightarrow',
    '\\xleftarrow',
    '\\xrightarrow',
    // "\\angle", // in texvc, but ams not actually required
    '\\sqsupset',
    '\\sqsubset',
    // "\\sqsupseteq", // in texvc, but ams not actually required
    // "\\sqsubseteq", // in texvc, but ams not actually required
    '\\smallsetminus',
    '\\And',
    // "\\sqcap", // in texvc, but ams not actually required
    // "\\sqcup", // in texvc, but ams not actually required
    '\\implies',
    '\\mod',
    '\\Diamond',
    '\\dotsb',
    '\\dotsc',
    '\\dotsi',
    '\\dotsm',
    '\\dotso',
    '\\lVert',
    '\\rVert',
    '\\nmid',
    '\\lesssim',
    '\\ngeq',
    '\\smallsmile',
    '\\smallfrown',
    '\\nleftarrow',
    '\\nrightarrow',
    '\\trianglelefteq',
    '\\trianglerighteq',
    '\\square',
    '\\checkmark',
    '\\supsetneq',
    '\\subsetneq',
    '\\Box',
    '\\nleq',
    '\\upharpoonright',
    '\\upharpoonleft',
    '\\downharpoonright',
    '\\downharpoonleft',
    // "\\rightharpoonup", // in texvc, but ams not actually required
    // "\\rightharpoondown", // in texvc, but ams not actually required
    // "\\leftharpoonup", // in texvc, but ams not actually required
    // "\\leftharpoondown", // in texvc, but ams not actually required
    '\\nless',
    '\\Vdash',
    '\\vDash',
    '\\varkappa',
    '\\digamma',
    '\\beth',
    '\\daleth',
    '\\gimel',
    '\\complement',
    '\\eth',
    '\\hslash',
    '\\mho',
    '\\Finv',
    '\\Game',
    '\\varlimsup',
    '\\varliminf',
    '\\varinjlim',
    '\\varprojlim',
    '\\injlim',
    '\\projlim',
    '\\iint',
    '\\iiint',
    '\\iiiint',
    '\\varnothing',
    '\\overleftrightarrow',
    '\\binom',
    '\\dbinom',
    '\\tbinom',
    '\\sideset',
    '\\underset',
    '\\overset',
    '\\dfrac',
    '\\tfrac',
    '\\cfrac',
    // "\\bigl", // in texvc, but ams not actually required
    // "\\bigr", // in texvc, but ams not actually required
    // "\\Bigl", // in texvc, but ams not actually required
    // "\\Bigr", // in texvc, but ams not actually required
    // "\\biggl", // in texvc, but ams not actually required
    // "\\biggr", // in texvc, but ams not actually required
    // "\\Biggl", // in texvc, but ams not actually required
    // "\\Biggr", // in texvc, but ams not actually required
    '\\vartriangle',
    '\\triangledown',
    '\\lozenge',
    '\\circledS',
    '\\measuredangle',
    '\\nexists',
    '\\Bbbk',
    '\\backprime',
    '\\blacktriangle',
    '\\blacktriangledown',
    '\\blacksquare',
    '\\blacklozenge',
    '\\bigstar',
    '\\sphericalangle',
    '\\diagup',
    '\\diagdown',
    '\\dotplus',
    '\\Cap',
    '\\Cup',
    '\\barwedge',
    '\\veebar',
    '\\doublebarwedge',
    '\\boxminus',
    '\\boxtimes',
    '\\boxdot',
    '\\boxplus',
    '\\divideontimes',
    '\\ltimes',
    '\\rtimes',
    '\\leftthreetimes',
    '\\rightthreetimes',
    '\\curlywedge',
    '\\curlyvee',
    '\\circleddash',
    '\\circledast',
    '\\circledcirc',
    '\\centerdot',
    '\\intercal',
    '\\leqq',
    '\\leqslant',
    '\\eqslantless',
    '\\lessapprox',
    '\\approxeq',
    '\\lessdot',
    '\\lll',
    '\\lessgtr',
    '\\lesseqgtr',
    '\\lesseqqgtr',
    '\\doteqdot',
    '\\risingdotseq',
    '\\fallingdotseq',
    '\\backsim',
    '\\backsimeq',
    '\\subseteqq',
    '\\Subset',
    '\\preccurlyeq',
    '\\curlyeqprec',
    '\\precsim',
    '\\precapprox',
    '\\vartriangleleft',
    '\\Vvdash',
    '\\bumpeq',
    '\\Bumpeq',
    '\\geqq',
    '\\geqslant',
    '\\eqslantgtr',
    '\\gtrsim',
    '\\gtrapprox',
    '\\eqsim',
    '\\gtrdot',
    '\\ggg',
    '\\gtrless',
    '\\gtreqless',
    '\\gtreqqless',
    '\\eqcirc',
    '\\circeq',
    '\\triangleq',
    '\\thicksim',
    '\\thickapprox',
    '\\supseteqq',
    '\\Supset',
    '\\succcurlyeq',
    '\\curlyeqsucc',
    '\\succsim',
    '\\succapprox',
    '\\vartriangleright',
    '\\shortmid',
    '\\shortparallel',
    '\\between',
    '\\pitchfork',
    '\\varpropto',
    '\\blacktriangleleft',
    '\\therefore',
    '\\backepsilon',
    '\\blacktriangleright',
    '\\because',
    '\\nleqslant',
    '\\nleqq',
    '\\lneq',
    '\\lneqq',
    '\\lvertneqq',
    '\\lnsim',
    '\\lnapprox',
    '\\nprec',
    '\\npreceq',
    '\\precneqq',
    '\\precnsim',
    '\\precnapprox',
    '\\nsim',
    '\\nshortmid',
    '\\nvdash',
    '\\nVdash',
    '\\ntriangleleft',
    '\\ntrianglelefteq',
    '\\nsubseteq',
    '\\nsubseteqq',
    '\\varsubsetneq',
    '\\subsetneqq',
    '\\varsubsetneqq',
    '\\ngtr',
    '\\ngeqslant',
    '\\ngeqq',
    '\\gneq',
    '\\gneqq',
    '\\gvertneqq',
    '\\gnsim',
    '\\gnapprox',
    '\\nsucc',
    '\\nsucceq',
    '\\succneqq',
    '\\succnsim',
    '\\succnapprox',
    '\\ncong',
    '\\nshortparallel',
    '\\nparallel',
    '\\nvDash',
    '\\nVDash',
    '\\ntriangleright',
    '\\ntrianglerighteq',
    '\\nsupseteq',
    '\\nsupseteqq',
    '\\varsupsetneq',
    '\\supsetneqq',
    '\\varsupsetneqq',
    '\\leftleftarrows',
    '\\leftrightarrows',
    '\\Lleftarrow',
    '\\leftarrowtail',
    '\\looparrowleft',
    '\\leftrightharpoons',
    '\\curvearrowleft',
    '\\circlearrowleft',
    '\\Lsh',
    '\\upuparrows',
    '\\rightrightarrows',
    '\\rightleftarrows',
    '\\Rrightarrow',
    '\\rightarrowtail',
    '\\looparrowright',
    '\\curvearrowright',
    '\\circlearrowright',
    '\\Rsh',
    '\\downdownarrows',
    '\\multimap',
    '\\leftrightsquigarrow',
    '\\rightsquigarrow',
    '\\nLeftarrow',
    '\\nleftrightarrow',
    '\\nRightarrow',
    '\\nLeftrightarrow',

    // "\\mathit", // in texvc, but ams not actually required
    // "\\mathrm", // in texvc, but ams not actually required
    // "\\mathord", // in texvc, but ams not actually required
    // "\\mathop", // in texvc, but ams not actually required
    // "\\mathbin", // in texvc, but ams not actually required
    // "\\mathrel", // in texvc, but ams not actually required
    // "\\mathopen", // in texvc, but ams not actually required
    // "\\mathclose", // in texvc, but ams not actually required
    // "\\mathpunct", // in texvc, but ams not actually required
    '\\boldsymbol',
    '\\mathbb',
    // "\\mathbf", // in texvc, but ams not actually required
    // "\\mathsf", // in texvc, but ams not actually required
    // "\\mathcal", // in texvc, but ams not actually required
    // "\\mathtt", // in texvc, but ams not actually required
    '\\mathfrak',
    '\\operatorname',
    '\\mathbb{R}'
]);

module.exports.cancel_required = arr2set([
    '\\bcancel',
    '\\cancel',
    '\\xcancel',
    '\\cancelto'
]);

module.exports.color_required = arr2set([
    '\\color',
    '\\pagecolor',
    '\\definecolor'
]);

module.exports.euro_required = arr2set([
    '\\euro',
    '\\geneuro',
    '\\geneuronarrow',
    '\\geneurowide',
    '\\officialeuro'
]);

module.exports.teubner_required = arr2set([
    '\\Coppa',
    '\\coppa',
    '\\Digamma',
    '\\Koppa',
    '\\koppa',
    '\\Sampi',
    '\\sampi',
    '\\Stigma',
    '\\stigma',
    '\\varstigma'
]);

module.exports.mhchem_required = arr2set([
    '\\ce'
]);

module.exports.mathoid_required = arr2set([
    '\\oiint',
    '\\oiiint',
    '\\ointctrclockwise',
    '\\varointclockwise'
]);

// MHCHEM functions

module.exports.mhchem_single_macro = arr2set([
    '\\Alpha',
    '\\Beta',
    '\\Gamma',
    '\\Delta',
    '\\Epsilon',
    '\\Zeta',
    '\\Eta',
    '\\Theta',
    '\\Iota',
    '\\Kappa',
    '\\Lambda',
    '\\Mu',
    '\\Nu',
    '\\Omicron',
    '\\Pi',
    '\\Rho',
    '\\Sigma',
    '\\Tau',
    '\\Upsilon',
    '\\Phi',
    '\\Chi',
    '\\Psi',
    '\\Omega',
    '\\alpha',
    '\\beta',
    '\\gamma',
    '\\delta',
    '\\epsilon',
    '\\zeta',
    '\\eta',
    '\\theta',
    '\\iota',
    '\\kappa',
    '\\lambda',
    '\\mu',
    '\\nu',
    '\\omicron',
    '\\pi',
    '\\rho',
    '\\sigma',
    '\\tau',
    '\\upsilon',
    '\\phi',
    '\\chi',
    '\\psi',
    '\\omega',
    '\\varkappa',
    '\\varepsilon',
    '\\vartheta',
    '\\varphi',
    '\\varpi',
    '\\varrho',
    '\\varsigma',
    '\\pm',
    '\\approx',
    '\\ca'
]);

module.exports.mhchem_bond = arr2set([
    '\\bond'
]);

module.exports.mhchem_macro_1p = arr2set([
    '\\ce',
    '\\mathbf'
]);

module.exports.mhchem_macro_2p = arr2set([
    '\\frac',
    '\\overset',
    '\\underset'
]);

module.exports.mhchem_macro_2pu = arr2set([
    '\\underbrace'
]);

module.exports.mhchem_macro_2pc = arr2set([
    '\\color'
]);

},{}],8:[function(require,module,exports){
module.exports={
  "name": "mathoid-texvcjs",
  "version": "0.3.10",
  "description": "A TeX/LaTeX validator for MediaWiki.",
  "main": "lib/index.js",
  "scripts": {
    "build": "node -e 'require(\"./lib/build-parser\")'",
    "cover": "nyc --reporter=lcov --exclude lib/parser.js_mocha ",
    "lint": "eslint --max-warnings 0 --ext .js .",
    "test": "node -e 'require(\"./lib/build-parser\")' && npm run lint && mocha",
    "report-coverage": "cat ./coverage/lcov.info | coveralls"
  },
  "repository": {
    "type": "git",
    "url": "git://github.com/wikimedia/texvcjs"
  },
  "keywords": [
    "tex",
    "wikitext",
    "mediawiki",
    "mathoid",
    "texvc"
  ],
  "license": "GPL-2.0",
  "bugs": {
    "url": "https://phabricator.wikimedia.org/project/profile/1771/"
  },
  "dependencies": {
    "commander": "~6.1.0"
  },
  "devDependencies": {
    "coveralls": "^3.1.0",
    "nyc": "^15.1.0",
    "eslint-config-wikimedia": "^0.17.0",
    "mocha": "~8.1.3",
    "mocha-lcov-reporter": "^1.3.0",
    "pegjs": "~0.10.0"
  },
  "bin": {
    "texvcjs": "./bin/texvcjs"
  }
}

},{}]},{},[1]);
