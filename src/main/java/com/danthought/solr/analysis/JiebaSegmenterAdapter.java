package com.danthought.solr.analysis;

import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

public class JiebaSegmenterAdapter {

	// 结巴分词器
	private JiebaSegmenter segmenter;
	// 分词模式
	private final SegMode segMode;
	// 词元列表的迭代器
	private Iterator<SegToken> iterator;

	public JiebaSegmenterAdapter(String segMode, String userDict) {
		segmenter = new JiebaSegmenter();

		this.segMode = SegMode.valueOf(segMode);
		
		// 加载自定义的词典
		if (userDict != null) {
			// 从 Solr 解压文件目录下 server 目录开始
			// 如，解压目录为 /Users/danjiang/webapps/solr-5.5.2，userDict 配置为 solr/chinese/conf/jieba.txt
			// 下面 Path 绝对路径为 /Users/danjiang/webapps/solr-5.5.2/server/solr/chinese/conf/jieba.txt
			// 当然 userDict 也可以配置绝对路径
			Path path = FileSystems.getDefault().getPath(userDict);
			WordDictionary wordDict = WordDictionary.getInstance();
			wordDict.loadUserDict(path);
		}
	}

	public synchronized SegToken next() { 
		// 获取下一个词元
		return iterator.next();
	}

	public synchronized boolean hasNext() {
		// 是否还有下一个词元
		return iterator.hasNext();
	}

	public synchronized void reset(Reader input) {
		// 重置分词器到初始状态
		// 从输入中读取句子
		String sentence = "";
		try {
			StringBuilder sb = new StringBuilder();
			char[] buffer = new char[1024];
			int size = 0;
			while ((size = input.read(buffer, 0, buffer.length)) != -1) {
				String string = new String(buffer, 0, size);
				sb.append(string);
			}
			sentence = sb.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("reading sentence from input failed");
		}

		// 分词后的每一个词元
		List<SegToken> tokens = segmenter.process(sentence, segMode);
		// 词元列表的迭代器
		iterator = tokens.iterator();
	}

}
