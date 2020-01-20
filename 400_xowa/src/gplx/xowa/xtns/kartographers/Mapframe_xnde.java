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
package gplx.xowa.xtns.kartographers; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.logs.*; import gplx.xowa.parsers.xndes.*; import gplx.xowa.parsers.htmls.*;
import gplx.xowa.xtns.proofreadPage.*;
public class Mapframe_xnde implements Xox_xnde, Mwh_atr_itm_owner2 {
	private byte[] lat, lon, zoom, show, group, mapstyle, width, height, align, lang, text, frameless;
	private int json_bgn, json_end;
	public void Xatr__set(Xowe_wiki wiki, byte[] src, Mwh_atr_itm xatr, byte xatr_id) {
		switch (xatr_id) {
			case Map_atrs.Tid__latitude:   lat = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__longitude:  lon = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__zoom:       zoom = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__show:       show = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__group:      group = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__mapstyle:   mapstyle = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__width:      width = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__height:     height = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__align:      align = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__lang:       lang = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__text:       text = xatr.Val_as_bry(); break;
			case Map_atrs.Tid__frameless:  frameless = xatr.Val_as_bry(); break;
		default:
			break;
		}
	}
	public void Xtn_parse(Xowe_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		ctx.Para().Process_block__xnde(xnde.Tag(), Xop_xnde_tag.Block_bgn);
		Xox_xnde_.Parse_xatrs(wiki, this, Map_atrs.Key_hash, src, xnde);
		// should validate width and height (assume correct)
		if (this.align == null) {
			if (wiki.Lang().Dir_ltr())
				this.align = Bry_.new_a7("right");
			else
				this.align = Bry_.new_a7("left");
		}
		if (this.lang == null) {
			this.lang = wiki.Lang().Key_bry();
		}
		if (this.mapstyle == null) {
			this.mapstyle = Bry_.new_a7("osm-intl"); // osm-intl or osm
		}
		json_bgn = xnde.Tag_open_end();
		json_end = xnde.Tag_close_bgn();
		//System.out.println("mapframe " + String_.new_u8(src, xnde.Tag_open_bgn(), xnde.Tag_close_end()));
//		ctx.Para().Process_block__xnde(xnde.Tag(), Xop_xnde_tag.Block_bgn);
//		boolean log_wkr_enabled = Log_wkr != Xop_log_basic_wkr.Null; if (log_wkr_enabled) Log_wkr.Log_end_xnde(ctx.Page(), Xop_log_basic_wkr.Tid_mapframe, src, xnde);
		ctx.Para().Process_block__xnde(xnde.Tag(), Xop_xnde_tag.Block_end);
	}
	public void Xtn_write(Bry_bfr bfr, Xoae_app app, Xop_ctx ctx, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xoae_page wpg, Xop_xnde_tkn xnde, byte[] src) {
		Render(bfr, ctx);
	}
	public static Xop_log_basic_wkr Log_wkr = Xop_log_basic_wkr.Null;

	private boolean is_numeric(byte[] src) {
		int len = src.length;
		for (int i = 0; i < len; i++) {
			byte b = src[i];
			if (b < '0' || b > '9')
				return false;
		}
		return true;
	}
	private void Render(Bry_bfr bfr, Xop_ctx ctx) {
		byte[] caption = this.text;
		if (caption != null) {
			caption = Pp_pages_nde.Decode_as_dot_bry(caption);
			caption = Bry_.Replace(caption, Byte_ascii.Underline, Byte_ascii.Space);
			caption = ctx.Wiki().Parser_mgr().Uniq_mgr().Parse(caption);
			Bry_bfr tmp_bfr = Bry_bfr_.New();
			ctx.Wiki().Parser_mgr().Main().Parse_text_to_html(tmp_bfr, ctx, ctx.Page(), false, caption);
			caption = tmp_bfr.To_bry();
		}
		boolean framed = false;
		if (caption != null || this.frameless == null)
			framed = true;

		// for the moment always 'interactive'
		boolean fullWidth = false;

		byte[] width;
		byte[] height;
		if (is_numeric(this.width))
			width = Bry_.Add(this.width, Bry_.new_a7("px"));
		else
			width = this.width;
		//$width = is_numeric( $this->width ) ? "{$this->width}px" : $this->width;

		//if ( preg_match( '/^\d+%$/', $width ) ) {
		int wlen = width.length;
		byte[] staticWidth;
		if ( wlen > 0 && width[wlen-1] == '%') {
			if ( wlen == 4 ) { // assuming $width === '100%' ) {
				fullWidth = true;
				staticWidth = Bry_.new_a7("800");
			} else {
				width = Bry_.new_a7("300px"); // @todo: deprecate old syntax completely
				staticWidth = Bry_.new_a7("300");
			}
		} else if ( wlen == 4 && width[0] == 'f' ) { // 'full'
			width = Bry_.new_a7("100%");
			fullWidth = true;
			staticWidth = Bry_.new_a7("800");
		} else {
			staticWidth = this.width;
		}

		height = Bry_.Add(this.height, Bry_.new_a7("px"));

		byte[] attrs = Bry_.Add(
		Bry_.new_a7("class=\"mw-kartographer-map\""),
		Bry_.new_a7(" mw-data=\"interface\""),
		Bry_.new_a7(" data-style=\""), this.mapstyle,
		Bry_.new_a7("\" data-width=\""), this.width,
		Bry_.new_a7("\" data-height=\""), this.height,
		Bry_.new_a7("\" data-lang=\""), this.lang,
		Bry_.new_a7("\""));

		byte[] staticZoom;
		if ( this.zoom != null ) {
			staticZoom = this.zoom;
			attrs = Bry_.Add(attrs, Bry_.new_a7(" data-zoom=\""), this.zoom, Bry_.new_a7("\""));
		} else {
			staticZoom = Bry_.new_a7("2");
		}

		byte[] staticLat, staticLon;
		if ( this.lat != null && this.lon != null ) {
			attrs = Bry_.Add(attrs, 
				Bry_.new_a7(" data-lat=\""), this.lat,
				Bry_.new_a7("\" data-lon=\""), this.lon, Bry_.new_a7("\""));
			staticLat = this.lat;
			staticLon = this.lon;
		} else {
			staticLat = Bry_.new_a7("30");
			staticLon = Bry_.new_a7("0");
		}
/*
				if ( $this->showGroups ) {
					$attrs['data-overlays'] = FormatJson::encode( $this->showGroups, false,
						FormatJson::ALL_OK );
					$this->state->addInteractiveGroups( $this->showGroups );
				}
				break;
*/
		byte[] style = Bry_.Empty;
/*
		$containerClass = 'mw-kartographer-container';
		if ( $fullWidth ) {
			$containerClass .= ' mw-kartographer-full';
		}

		$params = [
			'lang' => $this->langCode,
		];
		$bgUrl = "{$wgKartographerMapServer}/img/{$this->mapStyle},{$staticZoom},{$staticLat}," .
			"{$staticLon},{$staticWidth}x{$this->height}.png";
		if ( $this->showGroups ) {
			$params += [
				'domain' => $wgServerName,
				'title' => $this->parser->getTitle()->getPrefixedText(),
				'groups' => implode( ',', $this->showGroups ),
			];
		}
		$bgUrl .= '?' . wfArrayToCgi( $params );

		$attrs['style'] = "background-image: url({$bgUrl});";
		$attrs['href'] = SpecialMap::link( $staticLat, $staticLon, $staticZoom )->getLocalURL();

		if ( !$framed ) {
			$attrs['style'] .= " width: {$width}; height: {$height};";
			$attrs['class'] .= " {$containerClass} {$alignClasses[$this->align]}";

			return Html::rawElement( 'a', $attrs );
		}

		$attrs['style'] .= " height: {$height};";
		$containerClass .= " thumb {$thumbAlignClasses[$this->align]}";

		$captionFrame = Html::rawElement( 'div', [ 'class' => 'thumbcaption' ],
			$this->parser->recursiveTagParse( $caption ) );

		$mapDiv = Html::rawElement( 'a', $attrs );
*/
		byte[] containerClass = Bry_.new_a7("mw-kartographer-container");
		if ( fullWidth ) {
			containerClass = Bry_.Add(containerClass, Bry_.new_a7(" mw-kartographer-full"));
		}

		style = Bry_.Add(style, Bry_.new_a7(" height: "), height, Bry_.new_a7(";"));
		if (this.align[0] != 'c')
			this.align = Bry_.Add(Bry_.new_a7("t"), this.align);
		containerClass = Bry_.Add(containerClass, Bry_.new_a7(" thumb "), this.align);

		Fmt__frame.Bld_many(bfr, containerClass, width, attrs, style, caption);
	}
	private static final	Bry_fmt
	  Fmt__frame = Bry_fmt.Auto_nl_skip_last
	( ""
	, "<div class=\"~{conatinerClass}\">"
	, " <div class=\"thumbinner\" style=\"width: ~{width};\">"
	, "  <a ~{attrs} style=\"~{style}\"></a>"
	, "  <div class=\"thumbcaption\">"
	, "   ~{caption}"
	, "  </div>"
	, " </div>"
	, "</div>"
	);
}
