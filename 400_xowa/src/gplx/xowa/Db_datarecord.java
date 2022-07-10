package gplx.xowa;
import gplx.*;
import java.io.*;
public class Db_datarecord {
	private String root;
	private int page_text_id;
	private RandomAccessFile txt_f;

	public Db_datarecord(String root) {
		this.root = root;
		page_text_id = 0;
	}
	public byte[] Textfind(int text_file_id, long text_offset, int text_len) {
		byte[] data = null;
		try {
			if (this.page_text_id != text_file_id) {
				if (this.page_text_id != 0) {
					txt_f.close();
				}
				File file = new File(root + Integer.toString(text_file_id) + ".dat");
				txt_f = new RandomAccessFile(file, "r");
				this.page_text_id = text_file_id;
			}
			txt_f.seek(text_offset);
			data = new byte[text_len];
			txt_f.read(data);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return data;
	}
}
