
window.MathJax = {
  tex: {
    inlineMath: [['$', '$'], ['\\(', '\\)']]
  },
  svg: {
    fontCache: 'global'
  },
  startup: {
    ready: () => {
      var math = document.querySelectorAll('[id^="xowa_math_txt"]');
      var j;
      for (j = 0; j < math.length; j++) {
        var e = math[j];
        tex = e.innerHTML;
    //tex = tex.replace(/&lt;/g,"<").replace(/&gt;/g,">").replace(/&nbsp;/g," ").replace(/&amp;/g,"&").replace(/([^\\])%/g,"\\%");
    //tex = tex.replace(/(?:\\iiint|\\int\\!\\!\\!(?:\\!)+\\int\\!\\!\\!(?:\\!)+\\int)((?:[^\\!]|\\(?!!))*)\\!\\!\\!\\!\\![^sb]*(?:\\subset\\!\\supset|\\bigcirc(?:\\,)*)/g,"\\oiiint$1").replace(/(?:\\iint|\\int\\!\\!\\!(?:\\!)+\\int)((?:[^\\!]|\\(?!!))*)\\!\\!\\!\\!\\![^sb]*(?:\\subset\\!\\supset|\\bigcirc(?:\\,)*)/g,"\\oiint$1");
    console.log('tex:' + tex);
        var d = document.createTextNode('$' + tex + '$');
        e.parentNode.replaceChild(d, e);
      }
      MathJax.startup.defaultReady();
      MathJax.startup.promise.then(() => {
        console.log('MathJax initial typesetting complete');
      });
    }
  }
};

(function () {
  var script = document.createElement('script');
  script.src = xowa_root_dir + 'bin/any/xowa/xtns/Math/modules/MathJax-3.1.2/es5/tex-svg.js';
  script.async = true;
  document.head.appendChild(script);
  var script = document.createElement('script');
  script.src = xowa_root_dir + 'bin/any/xowa/xtns/Math/modules/mathjax/bundle.js';
  script.async = true;
  document.head.appendChild(script);
})();
