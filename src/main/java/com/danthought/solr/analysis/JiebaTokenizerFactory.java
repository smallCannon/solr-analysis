package com.danthought.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;

public class JiebaTokenizerFactory extends TokenizerFactory {
	
	// 分词模式
	private final String segMode;
	// 用户词典
	private final String userDict;

	public JiebaTokenizerFactory(Map<String, String> args) {
		super(args);
		segMode = get(args, "segMode", SegMode.SEARCH.toString());
		userDict = get(args, "userDict");
	    if (!args.isEmpty()) {
	      throw new IllegalArgumentException("Unknown parameters: " + args);
	    }
	}

	@Override
	public Tokenizer create(AttributeFactory factory) {
		return new JiebaTokenizer(segMode, userDict);
	}

}
