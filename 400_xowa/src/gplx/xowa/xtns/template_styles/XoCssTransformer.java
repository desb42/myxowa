package gplx.xowa.xtns.template_styles;

import gplx.Bry_;
import gplx.langs.javascripts.JsString_;
import gplx.xowa.htmls.minifys.XoCssMin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class XoCssTransformer {
    private final static ConcurrentHashMap<String, Pattern> patterns = new ConcurrentHashMap<>();
    private String css;
    public XoCssTransformer(String css) {
        this.css = css;
    }

    public XoCssTransformer Minify() {
        XoCssMin minifier = new XoCssMin();
        this.css = minifier.cssmin(css, -1);
        return this;
    }
    public XoCssTransformer Prepend(String prepend) {
        // prepend any classes to all declarations; primarily for '.mw-parser-output ' selector
        css = my_addselector(css, "\\}([^\\{\\}]*)\\{", prepend, "}", "{");
        //css = JsString_.replace(css, patterns, "\\}([^@}].{2})", "} " + prepend + " $1");
        css = my_addselector(css, "\\{([^\\{\\}]*)\\{", prepend, "{", "{");
        //css = JsString_.replace(css, patterns, "(@media[^\\{]*\\{)", "$1" + prepend + " ");
        if (css.charAt(0) != '@')
            css = my_addselector(css, "^([^\\{]*)\\{", prepend, "", "{");
            //css = prepend + " " + css;
        return this;
    }
	String my_addselector(String base, String regex, String prepend, String before, String after) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(base);
		StringBuffer sb = new StringBuffer();
		boolean found = false;
		
		while (m.find()) {
			found = true;
                        String replacement = before;
                        String grp = m.group(1);
                        if (grp.length() > 0) {
                            if (grp.charAt(0) != '@') {
                                String[] st = grp.split(",");
                                for (int i = 0; i < st.length; i++) {
                                    if (i > 0)
                                        replacement += ",";
                                    replacement += prepend + " " + st[i] + " ";
                                }
                            }
                            else
                                replacement += grp;
                        }
			m.appendReplacement(sb, replacement.replace("$", "\\$") + after);
		}
		if (found) {
			m.appendTail(sb);
			return sb.toString();
		}
		else
			return base;
	}
    public XoCssTransformer Url(String src, String trg) {
        // change some url(...) entries
        css = css.replace("//" + src, "//" + trg);
        return this;
    }
    public byte[] ToBry() {return Bry_.new_u8(css);}
    public String ToStr() {return css;}
}
