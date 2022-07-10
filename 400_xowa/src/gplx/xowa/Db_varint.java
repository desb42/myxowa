package gplx.xowa;
public class Db_varint {
	public static Db_varval getVarint(byte[] pagedata, int ofs) {
		long iKey = pagedata[ofs++] & 0xff;
		if( iKey >= 0x80 ){
			byte x;
			iKey = ((iKey&0x7f)<<7) | ((x = pagedata[ofs++]) & 0x7f);
			//if( (x & 0x80) > 0 ){
			if( x < 0 ){
				iKey = (iKey<<7) | ((x = pagedata[ofs++]) & 0x7f);
				//if( (x & 0x80) > 0 ){
				if( x < 0 ){
					iKey = (iKey<<7) | ((x = pagedata[ofs++]) & 0x7f);
					//if( (x & 0x80) > 0 ){
					if( x < 0 ){
						iKey = (iKey<<7) | ((x = pagedata[ofs++]) & 0x7f);
						//if( (x & 0x80) > 0 ){
						if( x < 0 ){
							iKey = (iKey<<7) | ((x = pagedata[ofs++]) & 0x7f);
							//if( (x & 0x80) > 0 ){
							if( x < 0 ){
								iKey = (iKey<<7) | ((x = pagedata[ofs++]) & 0x7f);
								//if( (x & 0x80) > 0 ){
								if( x < 0 ){
									iKey = (iKey<<7) | ((x = pagedata[ofs++]) & 0x7f);
									//if( (x & 0x80) > 0 ){
									if( x < 0 ){
										iKey = (iKey<<8) | (pagedata[ofs++]);
									}
								}
							}
						}
					}
				}
			}
		}
		return new Db_varval(iKey, ofs);
	}
}	
