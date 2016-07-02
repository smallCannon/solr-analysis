package com.danthought.solr.analysis;

import junit.framework.TestCase;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;

public class JiebaAnalysisTest extends TestCase {
	
	private String[] sentences;
	private JiebaSegmenter segmenter;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		sentences = new String[] { "三全水果汤圆（椰果）", "板栗肉粽子", "北菇虾球粥", "小米玉米粥", "湾仔码头水晶汤圆（紫薯）" };
		segmenter = new JiebaSegmenter();
	}

	public void testIndex() {
	    for (String sentence : sentences) {
	        System.out.println(segmenter.process(sentence, SegMode.INDEX).toString());
	    }
	}
	
	public void testSearch() {
		for (String sentence : sentences) {
	        System.out.println(segmenter.process(sentence, SegMode.SEARCH).toString());
	    }
	}
	
}
