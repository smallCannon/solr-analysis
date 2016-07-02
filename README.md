## 简介

针对[结巴分词 Java 版](https://github.com/huaban/jieba-analysis)能够在 Solr 中使用，编写的 [Solr](http://lucene.apache.org/solr/) 插件，只实现了 [Tokenizer](https://cwiki.apache.org/confluence/display/solr/About+Tokenizers)，没有实现 [Analyzer](https://cwiki.apache.org/confluence/display/solr/Analyzers)，前者符合我的使用情况。

## 安装

### 系统要求

* Maven
* Java 8
* Solr 5.5.2

### 克隆项目

```
git clone https://github.com/danjiang/solr-analysis
```

### 打包 Jar

```
cd solr-analysis
mvn package
```

### 拷贝 Jar

`solr_unzip_directory` 是 Solr 的解压目录，`solr_instance_directory` 是自定义 Solr Core Instance 的目录，后面多处用到，请脑补。

```
cp target/solr-analysis-1.0.0.jar {solr_unzip_directory}/server/solr/{solr_instance_directory}/lib/
cp ~/.m2/repository/com/huaban/jieba-analysis/1.0.2/jieba-analysis-1.0.2.jar  {solr_unzip_directory}/server/solr/{solr_instance_directory}/lib/
```

## 使用

### solrconfig.xml

```
vim {solr_unzip_directory}/server/solr/{solr_instance_directory}/conf/solrconfig.xml
```

添加如下配置 Solr 才能加载添加的 Jar

```xml
<lib dir="./lib" regex=".*\.jar" />
```

### schema.xml

```
vim {solr_unzip_directory}/server/solr/{solr_instance_directory}/conf/schema.xml
```

配置自定义的 Tokenizer，segMode 只有两种值，针对索引是 INDEX，针对搜索是 SEARCH，userDict 是用户词典的路径。

```xml
<fieldType name="text_general" class="solr.TextField">
    <analyzer type="index">
        <tokenizer class="com.danthought.solr.analysis.JiebaTokenizerFactory" segMode="INDEX" userDict="solr/{solr_instance_directory}/conf/jieba.txt" />
    </analyzer>
    <analyzer type="query">
        <tokenizer class="com.danthought.solr.analysis.JiebaTokenizerFactory" segMode="SEARCH" userDict="solr/{solr_instance_directory}/conf/jieba.txt" />
        <filter class="solr.SynonymFilterFactory" synonyms="synonyms.txt" ignoreCase="true" expand="true"/>
    </analyzer>
</fieldType>
```

### 用户词典

如果需要额外添加用户词典，就需要填写 userDict，不需要就不用填写 userDict，说一下 userDict 的相对路径方式，相对路径都是开始于 `{solr_unzip_directory}/server`，针对上面 userDict 的值是 `solr/{solr_instance_directory}/conf/jieba.txt`，绝对路径就是 `{solr_unzip_directory}/server/solr/{solr_instance_directory}/conf/jieba.txt`。

```
vim {solr_unzip_directory}/server/solr/{solr_instance_directory}/conf/jieba.txt
```

添加需要的词，每一行一个，第一列为词语，然后空格，第二列为词频，词语和词频都是必填项。

```
湾仔码头 3
```

### 测试

没有使用用户词典

![结巴分词没有使用用户词典](jieba-without-userdict.jpg?raw=true "结巴分词没有使用用户词典")

使用用户词典

![结巴分词使用用户词典](jieba-with-userdict.jpg?raw=true "结巴分词使用用户词典")