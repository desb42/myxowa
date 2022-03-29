/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2017 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.htmls.core.htmls.tidy; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.htmls.core.htmls.tidy.vnus.*;
import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.sax.HtmlParser;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import gplx.core.envs.*;
class Xoh_tidy_wkr__vnu implements Xoh_tidy_wkr {
		private byte[] depurate(Bry_bfr tidy_bfr, boolean compat) throws SAXException, IOException {
/*
                    private byte[] depurate(Bry_bfr tidy_bfr, byte[] prefix, byte[] suffix, boolean compat) throws SAXException, IOException {
                    int tidy_bfr_len = tidy_bfr.Len();
                    int prefix_len = prefix.length;
                    int suffix_len = suffix.length;
                    int size = prefix_len + tidy_bfr_len + suffix_len;
                    byte[] input = new byte[size];
                    int i;
                    for (i = 0; i < prefix_len; i++)
                        input[i] = prefix[i];
                    for (i = 0; i < tidy_bfr_len; i++)
                        input[prefix_len++] = tidy_bfr.Bfr()[i];
                    for (i = 0; i < suffix_len; i++)
                        input[prefix_len++] = suffix[i];
                    tidy_bfr.Clear();
*/                    
		byte[] input = tidy_bfr.To_bry_and_clear();
		int pos = 0;
		int len = input.length;
		while (pos < len) {
			byte b = input[pos++];
			if (b == '&') {
				if (input[pos] == '#') {
                                    if ((input[pos+1] == '3' && input[pos+2] == '2' && input[pos+3] == ';')
                                            || (input[pos+1] == 'x'))
					input[pos-1] = '@'; // replace &#32; with @#32;
                                }
			}
		}
		InputStream stream = new ByteArrayInputStream(input);
		InputSource source = new InputSource(stream);
		ByteArrayOutputStream sink = new ByteArrayOutputStream();
		ContentHandler serializer;
		serializer = new CompatibilitySerializer(sink);
		HtmlParser parser = new HtmlParser(XmlViolationPolicy.ALLOW);
		parser.setContentHandler(serializer);
		source.setEncoding("UTF-8");
		parser.setProperty("http://xml.org/sax/properties/lexical-handler", serializer);
		parser.parse(source);
		byte[] output = sink.toByteArray();
		pos = 0;
		len = output.length;
		while (pos < len) {
			byte b = output[pos++];
			if (b == '@') {
				if (output[pos] == '#') {
                                    if ((output[pos+1] == '3' && output[pos+2] == '2' && output[pos+3] == ';')
                                            || (output[pos+1] == 'x'))
					output[pos-1] = '&'; // replace @#32; with &#32;
                                }
			}
		}
		return output;
	}
	public byte Tid() {return Xoh_tidy_wkr_.Tid_vnu;}
	public void Init_by_app(Xoae_app app) {
	}
	public void Indent_(boolean v) {
	}
	public void Exec_tidy(Bry_bfr bfr, byte[] page_url) {
		try {
			bfr.Add(depurate(bfr, true));
		} 
		catch (SAXException e) { }
		catch (IOException e) {}
	}
}
