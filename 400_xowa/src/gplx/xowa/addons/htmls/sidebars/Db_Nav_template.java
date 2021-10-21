package gplx.xowa.addons.htmls.sidebars;

import gplx.Bry_;
import gplx.Bry_bfr;
import gplx.Bry_bfr_;
import gplx.Io_url;
import gplx.String_;
import gplx.langs.jsons.Json_nde;
import gplx.langs.mustaches.JsonMustacheNde;
import gplx.langs.mustaches.Mustache_bfr;
import gplx.langs.mustaches.Mustache_render_ctx;
import gplx.langs.mustaches.Mustache_tkn_itm;
import gplx.langs.mustaches.Mustache_tkn_parser;
import gplx.xowa.Xowe_wiki;
import gplx.xowa.langs.msgs.Xol_msg_mgr;

// incorporating 20201001 changes in SkinVector.php
public class Db_Nav_template {
	public Mustache_tkn_itm Navigation_root() {return navigation_root;} private Mustache_tkn_itm navigation_root;
	private Xol_msg_mgr msg_mgr;
	private Json_nde msgdata;
	private Xowe_wiki wiki;
	private static boolean once = true;
	private static Mustache_tkn_itm menu_root;

	public static Json_nde Build_Sidebar_json(Xowe_wiki wiki, byte[] id, byte[] text, byte[] itms, int iter_count) {
		return s_getMenuData(wiki, id, text, itms, MENU_TYPE_PORTAL, iter_count);
	}
	public static Json_nde Build_Menu(Xowe_wiki wiki, byte[] label, byte[] text, byte[] itms) {
		byte[] id = Bry_.Add(Bry_.new_a7("p-"), label);
		int type;
/* from SkinVector.php 20210908 :592
		switch ( $label ) {
			case 'user-menu':
			case 'actions':
			case 'variants':
				$type = self::MENU_TYPE_DROPDOWN;
				break;
			case 'views':
			case 'namespaces':
				$type = self::MENU_TYPE_TABS;
				break;
			case 'notifications':
			case 'personal':
			case 'user-page':
				$type = self::MENU_TYPE_DEFAULT;
				break;
			case 'lang':
				$type = $this->isLanguagesInHeader() ?
					self::MENU_TYPE_DROPDOWN : self::MENU_TYPE_PORTAL;
				break;
			default:
				$type = self::MENU_TYPE_PORTAL;
				break;
		}
*/
		if ((label[0] == 'u' && label[5] == 'm') || label[0] == 'a' || (label[0] == 'v' && label[1] == 'a')) // 'user-menu', 'actions' or 'variants'
			type = MENU_TYPE_DROPDOWN;
		else if ((label[0] == 'v' && label[1] == 'i') || (label[0] == 'n' && label[1] == 'a')) // 'views' or 'namespaces'
			type = MENU_TYPE_TABS;
		else if ((label[0] == 'n' && label[1] == 'o') || (label[0] == 'p' && label[1] == 'e') || (label[0] == 'u' && label[5] == 'p')) // 'notifications' or 'personal' or 'user-page'
			type = MENU_TYPE_DEFAULT;
		else if (label[0] == 'l') // 'lang'
//				$type = $this->isLanguagesInHeader() ?
//					self::MENU_TYPE_DROPDOWN : self::MENU_TYPE_PORTAL;
			type = MENU_TYPE_PORTAL;
		else
			type = MENU_TYPE_PORTAL;
		return s_getMenuData(wiki, id, text, itms, type, 1);
	}
	public static Json_nde Build_Menu_json(Xowe_wiki wiki, byte[] id, byte[] text, byte[] itms) {
		return s_getMenuData(wiki, id, text, itms, MENU_TYPE_TABS, 1);
	}
	public static Json_nde Build_Menu_Default_json(Xowe_wiki wiki, byte[] id, byte[] text, byte[] itms) {
		return s_getMenuData(wiki, id, text, itms, MENU_TYPE_DEFAULT, 1);
	}
	public static void Render_Sidebar(Xowe_wiki wiki, Bry_bfr bfr, Json_nde data) {
		if (once) {
			once = false;
			Io_url template_root = wiki.Appe().Fsys_mgr().Bin_any_dir().GenSubDir_nest("xowa", "xtns", "Skin-Vector", "templates");
			Mustache_tkn_parser parser = new Mustache_tkn_parser(template_root);
			menu_root = parser.Parse("top-test");
		}
		Mustache_render_ctx mctx = new Mustache_render_ctx().Init(new JsonMustacheNde(data));
		Mustache_bfr mbfr = Mustache_bfr.New_bfr(bfr);
		menu_root.Render(mbfr, mctx);
	}
	public static void Render_Content(Xowe_wiki wiki, Bry_bfr bfr, Json_nde data) {
		if (once) {
			once = false;
			Io_url template_root = wiki.Appe().Fsys_mgr().Bin_any_dir().GenSubDir_nest("xowa", "xtns", "Skin-Vector", "templates");
			Mustache_tkn_parser parser = new Mustache_tkn_parser(template_root);
			menu_root = parser.Parse("content-test");
		}
		Mustache_render_ctx mctx = new Mustache_render_ctx().Init(new JsonMustacheNde(data));
		Mustache_bfr mbfr = Mustache_bfr.New_bfr(bfr);
		menu_root.Render(mbfr, mctx);
	}
	public static void Build_Sidebar(Xowe_wiki wiki, Bry_bfr bfr, byte[] id, byte[] text, byte[] itms, int iter_count) {
		if (once) {
			once = false;
			Io_url template_root = wiki.Appe().Fsys_mgr().Bin_any_dir().GenSubDir_nest("xowa", "xtns", "Skin-Vector", "templates");
			Mustache_tkn_parser parser = new Mustache_tkn_parser(template_root);
			menu_root = parser.Parse("Menu");
		}
		Json_nde data = s_getMenuData(wiki, id, text, itms, MENU_TYPE_PORTAL, iter_count);

		// Bry_bfr tmp_bfr = Bry_bfr_.New();
		Mustache_render_ctx mctx = new Mustache_render_ctx().Init(new JsonMustacheNde(data));
		Mustache_bfr mbfr = Mustache_bfr.New_bfr(bfr);
		menu_root.Render(mbfr, mctx);
		// byte[] result = mbfr.To_bry_and_clear();
		// System.out.println(String_.new_u8(result));
	}
	/* Vector/SkinVector.php */
	private static int MENU_TYPE_DROPDOWN = 0, MENU_TYPE_TABS = 1, MENU_TYPE_PORTAL = 2, MENU_TYPE_DEFAULT = 3;
	private static byte[][] extraClasses = new byte[][] {
		Bry_.new_a7("vector-menu vector-menu-dropdown"),
		Bry_.new_a7("vector-menu vector-menu-tabs"),
		Bry_.new_a7("vector-menu vector-menu-portal portal"),
		Bry_.new_a7("vector-menu")
	};
	private Json_nde getMenuData(byte[] label, Json_nde urls, int type) { return getMenuData(label, urls, type, false); }
	private Json_nde getMenuData(byte[] label_bry, Json_nde urls, int type, boolean setLabelToSelected) {
//private function getMenuData(
//		string $label,
//		array $urls = [],
//		int $type = self::MENU_TYPE_DEFAULT,
//		bool $setLabelToSelected = false
//	) : array {
//		$skin = $this->getSkin();
//		$extraClasses = [
//			self::MENU_TYPE_DROPDOWN => 'vector-menu vector-menu-dropdown',
//			self::MENU_TYPE_TABS => 'vector-menu vector-menu-tabs',
//			self::MENU_TYPE_PORTAL => 'vector-menu vector-menu-portal portal',
//			self::MENU_TYPE_DEFAULT => 'vector-menu',
//		];
//		$isPortal = $type === self::MENU_TYPE_PORTAL;

		boolean isPortal = type == MENU_TYPE_PORTAL;

//		// For some menu items, there is no language key corresponding with its menu key.
//		// These inconsitencies are captured in MENU_LABEL_KEYS
//		$msgObj = $skin->msg( self::MENU_LABEL_KEYS[ $label ] ?? $label );
//
//		$props = [
//			'id' => "p-$label",

		String label = String_.new_u8(label_bry);
		String msg = label; // for now
		String linkertooltip = String_.Empty;
		String plabel = "p-" + label;

		Json_nde props = Json_nde.NewByVal();
		props.AddKvStr("id", plabel);

		// 'label-id' => "p-{$label}-label",
		props.AddKvStr("label-id", plabel +"-label");

		// If no message exists fallback to plain text (T252727)
		// 'label' => $msgObj->exists() ? $msgObj->text() : $label,
		props.AddKvStr("label", msg);

		// 'html-items' => '',

		// 'is-dropdown' => $type === self::MENU_TYPE_DROPDOWN,
		props.AddKvBool("is-dropdown", type == MENU_TYPE_DROPDOWN);
		props.AddKvStr("html-tooltip", wiki.Msg_mgr().Val_html_accesskey_and_title(plabel));

		// 'html-tooltip' => Linker::tooltip( 'p-' . $label ),
		//props.AddKvStr("html-tooltip", linkertooltip); // already set above!!!!!!!

//		foreach ( $urls as $key => $item ) {
//			$props['html-items'] .= $this->getSkin()->makeListItem( $key, $item );
//			// Check the class of the item for a `selected` class and if so, propagate the items
//			// label to the main label.
//			if ( $setLabelToSelected ) {
//				if ( isset( $item['class'] ) && stripos( $item['class'], 'selected' ) !== false ) {
//					$props['label'] = $item['text'];
//				}
//			}
//		}

    	props.AddKvStr("html-items", "<li id=\"ca-nstab-main\" class=\"selected\"><a href=\"/wiki/Main_Page\" title=\"View the content page [c]\" accesskey=\"c\">Main Page</a></li>");

//		$afterPortal = '';
//		if ( $isPortal ) {
//			// The BaseTemplate::getAfterPortlet method ran the SkinAfterPortlet
//			// hook and if content is added appends it to the html-after-portal method.
//			// This replicates that historic behaviour.
//			// This code should eventually be upstreamed to SkinMustache in core.
//			// Currently in production this supports the Wikibase 'edit' link.
//			$content = $this->getAfterPortlet( $label );
//			if ( $content !== '' ) {
//				$afterPortal = Html::rawElement(
//					'div',
//					[ 'class' => [ 'after-portlet', 'after-portlet-' . $label ] ],
//					$content
//				);
//			}
//		}
//		$props['html-after-portal'] = $afterPortal;
//
//		// Mark the portal as empty if it has no content
//		$class = ( count( $urls ) == 0 && !$props['html-after-portal'] )
//			? 'vector-menu-empty emptyPortlet' : '';
//		$props['class'] = trim( "$class $extraClasses[$type]" );
//		return $props;

		props.AddKvStr("class", extraClasses[type]);
		return props;
	}

	private static Json_nde s_getMenuData(Xowe_wiki wiki, byte[] id, byte[] label, byte[] urls, int type, int iter_count) { return s_getMenuData(wiki, id, label, urls, type, false, iter_count); }
	private static Json_nde s_getMenuData(Xowe_wiki wiki, byte[] id, byte[] label_bry, byte[] urls, int type, boolean setLabelToSelected, int iter_count) {
		boolean isPortal = type == MENU_TYPE_PORTAL;

		String plabel = String_.new_u8(id);
		String label = plabel.substring(2);
		String msg = String_.new_u8(label_bry); // for now
		//String msg = msg_mgr.Val_by_str_or_empty(label_bry);
		//if (msg == "")
		//	msg = String_.new_u8(label_bry);
		//String linkertooltip = String_.Empty;

		Json_nde props = Json_nde.NewByVal();
		props.AddKvStr("id", plabel);
		props.AddKvStr("label-id", plabel +"-label");
		props.AddKvStr("label", msg);
		props.AddKvBool("is-dropdown", type == MENU_TYPE_DROPDOWN);
		props.AddKvStr("html-tooltip", wiki.Msg_mgr().Val_html_accesskey_and_title(plabel));
		//props.AddKvStr("html-tooltip", linkertooltip); // already set above

//		foreach ( $urls as $key => $item ) {
//			$props['html-items'] .= $this->getSkin()->makeListItem( $key, $item );
//			// Check the class of the item for a `selected` class and if so, propagate the items
//			// label to the main label.
//			if ( $setLabelToSelected ) {
//				if ( isset( $item['class'] ) && stripos( $item['class'], 'selected' ) !== false ) {
//					$props['label'] = $item['text'];
//				}
//			}
//		}
		props.AddKvStr("html-items", urls);

//		$afterPortal = '';
//		if ( $isPortal ) {
//			// The BaseTemplate::getAfterPortlet method ran the SkinAfterPortlet
//			// hook and if content is added appends it to the html-after-portal method.
//			// This replicates that historic behaviour.
//			// This code should eventually be upstreamed to SkinMustache in core.
//			// Currently in production this supports the Wikibase 'edit' link.
//			$content = $this->getAfterPortlet( $label );
//			if ( $content !== '' ) {
//				$afterPortal = Html::rawElement(
//					'div',
//					[ 'class' => [ 'after-portlet', 'after-portlet-' . $label ] ],
//					$content
//				);
//			}
//		}
//		$props['html-after-portal'] = $afterPortal;
//
//		// Mark the portal as empty if it has no content
//		$class = ( count( $urls ) == 0 && !$props['html-after-portal'] )
//			? 'vector-menu-empty emptyPortlet' : '';
//		$props['class'] = trim( "$class $extraClasses[$type]" );
//		return $props;

// need to add 'mw-portlet mw-portlet-${name}' - cant find where in mediawiki this comes from
		byte[] classes = Bry_.Add(
		Bry_.new_a7("mw-portlet mw-portlet-"
		        + label
		        + " "
//		        + (iter_count == 0 ? "portal-first ":"")
		        + (urls == Bry_.Empty ? "emptyPortlet ":"")
		        + ((id[2] == 'p' && id[3] == 'e') ? "vector-user-menu-legacy ":"") //'p-personal'?
		        )
			, extraClasses[type]);
		props.AddKvStr("class", classes);
		//props.AddKvStr("class", extraClasses[type]);
		return props;
	}
}