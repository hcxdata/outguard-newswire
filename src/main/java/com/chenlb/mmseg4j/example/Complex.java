package com.chenlb.mmseg4j.example;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

public class Complex {

	protected Dictionary dic;
	
	public Complex() {
		dic = Dictionary.getInstance();
	}

	protected Seg getSeg() {
		return new ComplexSeg(dic);
	}
	
	public String segWords(Reader input, String wordSpilt) throws IOException {
		StringBuilder sb = new StringBuilder();
		Seg seg = getSeg();	
		MMSeg mmSeg = new MMSeg(input, seg);
		Word word = null;
		boolean first = true;
		while((word=mmSeg.next())!=null) {
			if(!first) {
				sb.append(wordSpilt);
			}
			String w = word.getString();
			sb.append(w);
			first = false;
			
		}
		return sb.toString();
	}
	
	public String segWords(String txt, String wordSpilt) throws IOException {
		return segWords(new StringReader(txt), wordSpilt);
	}
	

}
