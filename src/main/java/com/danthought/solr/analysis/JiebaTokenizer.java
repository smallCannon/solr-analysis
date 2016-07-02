package com.danthought.solr.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import com.huaban.analysis.jieba.SegToken;

public final class JiebaTokenizer extends Tokenizer {

	// 结巴分词器 Solr 适配器
	private JiebaSegmenterAdapter segmenter;
	
	// 词元文本属性
	private final CharTermAttribute termAtt;
	// 词元位移属性
	private final OffsetAttribute offsetAtt;
	// 词元分类属性
	private final TypeAttribute typeAtt;
	// 记录最后一个词元的结束位置
	private int endPosition;
	
	public JiebaTokenizer(String segMode, String userDict) {
	    offsetAtt = addAttribute(OffsetAttribute.class);
	    termAtt = addAttribute(CharTermAttribute.class);
	    typeAtt = addAttribute(TypeAttribute.class);
	    segmenter = new JiebaSegmenterAdapter(segMode, userDict); 
	}
	
	@Override
	public boolean incrementToken() throws IOException {			
		// 清除所有的词元属性		
		clearAttributes();
		if(segmenter.hasNext()) {
			SegToken token = segmenter.next();
			// 设置词元文本
			termAtt.append(token.word);
			// 设置词元长度
			termAtt.setLength(token.word.length());
			// 设置词元位移
			offsetAtt.setOffset(token.startOffset, token.endOffset);
			// 记录分词的最后位置
			endPosition = token.endOffset;
			// 记录词元分类
			typeAtt.setType("CN_WORD");			
			// 返会 true 告知还有下个词元
			return true;
		}
		//返会 false 告知词元输出完毕
		return false;
	}
	
	@Override
	public void end() throws IOException { 
		super.end();
		int finalOffset = correctOffset(endPosition);
		offsetAtt.setOffset(finalOffset, finalOffset);
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		segmenter.reset(input);
	}

}
