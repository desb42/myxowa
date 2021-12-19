/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2021 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa; import gplx.*;
public class Db_index_stats {
	private int[] quality = new int[5];
	private int numberOfPages;
	public Db_index_stats(int count, int q0, int q1, int q2, int q3, int q4) {
		this.quality[0] = q0;
		this.quality[1] = q1;
		this.quality[2] = q2;
		this.quality[3] = q3;
		this.quality[4] = q4;
		this.numberOfPages = count;
	}
	public int getNumberOfPagesForQualityLevel( int level ) {
		// should check the bounds
		return quality[level];
	}
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public int getNumberOfPagesWithAnyQualityLevel() {
		int val = 0;
		for (int i = 0; i < 5; i++)
			val += quality[i];
		return val;
	}
	public int getNumberOfPagesWithoutQualityLevel() {
		return numberOfPages - getNumberOfPagesWithAnyQualityLevel();
	}
	public static Db_index_stats Null = new Db_index_stats(0, 0, 0, 0, 0, 0);
}
