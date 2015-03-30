

# Introduction #

This page describes:
  1. Tables in the machine-readable dictionary (MRD) database) containing quotations from the Wiktionaries (English and Russian).
  1. SPARQL and SQL queries which show how to work with quotes

Very important note:
  * Now quotes are extracted only from the Russian Wiktionary;
  * English Wiktionary quotes will be extracted after "[context labels](http://en.wiktionary.org/wiki/Category:Context_labels)" extraction (in 2012, my optimistic plan :)

## Examples from English Wiktionary ##

Example from English Wiktionary:
```
 TEMPLATE REFERENCE:
#* {{RQ:Twain Huckleberry}}
#*: “Looky here — mind how you talk to me; I’m a-standing about all I can stand now — so don’t gimme no '''sass'''.”

 SIMPLE TEXT (WITHOUT REFERENCE)
#: ''The player made an impressive '''catch'''.''
#: ''Nice '''catch'''!''

 TEXT REFERENCE
#* '''2010''' October 27, [[w:The Sydney Morning Herald|The Sydney Morning Herald]], ''Howard says leader[s]hip row 'done '''to death'''' '',
#*: "I don't want to revisit his view and my view about whether I should have retired and who said what then," he told ABC Radio.
#*:"That's been done '''to death'''."
```

## Examples from Russian Wiktionary ##

There is a special template
```
{{пример|текст|автор|титул|дата|}}
{{пример|текст=|перевод=|автор=|титул=|издание=|перев=|дата=|источник=}}
```

## Quotations - quote table ##

See the structure (tables and relations) of the Wiktionary parsed database here: [How to create, edit and load empty Wiktionary parsed database into MySQL](File_wikt_parsed_empty_sql.md)

Set of tables related to quotations (fragment of the Wiktionary parsed database) at Figure:

![http://wikokit.googlecode.com/svn/trunk/wiki/wiwordik.attach/db_scheme/quote_tables.png](http://wikokit.googlecode.com/svn/trunk/wiki/wiwordik.attach/db_scheme/quote_tables.png)

fields in **quote** table:
  * **id** (quote.id == quote\_id in other tables, PRIMARY key)
  * **meaning\_id** - link to the appropriated definition in the table meaning (== meaning.id)
  * **lang\_id** - language of the quote _text_ (duplication of lang\_pos.lang\_id)
  * **text** (not UNIQUE!) - quotation sentence text
  * **ref\_id** (==quot\_ref.id, may be NULL)

+ tables:
  * **quot\_translation** (quote in native language without translation, but the transcription can be presented)
    * quote\_id (== quote.id, PRIMARY, reference to the table `quote`)
    * text (not UNIQUE)

  * **quot\_transcription**
    * quote\_id (reference to the table `quote`))
    * text (not UNIQUE)

  * **quot\_ref** - links to tables with reference information
    * id (PRIMARY key)
    * year\_id - year (== quot\_year.id),
    * author\_id - author (== quot\_author.id),
    * title - source title
    * title\_wikilink - a wikilink to Wikisource (format: [[s:title|]]) for the work
    * publisher\_id - publisher (== quot\_publisher.id)
      * enwikt: text after 'Source title' and before page numbers
      * ruwikt: пример|..|издание=|
    * source\_id - source (== quot\_source.id)
> > Remark: _quot\_ref_ has:
      * _UNIQUE compound KEY_ (year\_id, author\_id, title\_id, publisher\_id, source\_id), and any of the fields could be NULL.

  * **quot\_author**
    * id
    * name (Not NULL)
    * wikilink (may be NULL) - a wikilink to author's name in Wikipedia (format: [[w:name|]])


> Remark: _quot\_author_ has:
    * _UNIQUE compound KEY_ (name, wikilink), and _wikilink_ field could be NULL.

> Todo test: (before INSERT) SELECT FROM quot\_author BY name OR wikilink;


  * **quot\_year**
    * id
    * from (Not NULL) - start date of a writing book with the quote
    * to (Not NULL) - finish date of a writing book with the quote
    * todo: comment (abbreviation [c., a., p.](http://en.wiktionary.org/wiki/Wiktionary:Quotations#The_use_of_abbreviations))
    * UNIQUE INDEX: (from, to)
> > Remark: if quote contains only one date, then _to_ = _from_


> Todo test: INSERTs twice (from, NULL), it should generate errors, so only unique (from, NULL) are allowed

  * **quot\_publisher** (_издание_ in ruwikt)
    * id
    * text (UNIQUE) - publisher’s name

  * **quot\_source** = НКРЯ = [Wiktionary:Quotations/Templates](http://en.wiktionary.org/wiki/Wiktionary:Quotations/Templates)
    * id
    * text (UNIQUE) - quotation templates in enwikt, источник in ruwikt

# Parsing rules #

todo


# SPARQL examples #

See more information about SPARQL queries to Wiktionary data here: [d2rqMappingSPARQL](d2rqMappingSPARQL.md).

## Get text of quotes by word and language ##

Let's find text of all quotations for the English word "airplane":

Open URL: http://localhost:2020/snorql/

Paste SPARQL request:
```
SELECT ?langId ?pageId ?meaningId ?quoteId ?quoteText ?quoteRefId
WHERE {
    ?lang wikpa:lang_code "en";
          wikpa:lang_id ?langId.

    ?page wikpa:page_page_title "airplane";
          wikpa:page_id ?pageId.

    ?lang_pos wikpa:lang_pos_page_id ?pageId;
              wikpa:lang_pos_lang_id ?langId;
              wikpa:lang_pos_id ?langPosId.

    ?meaning wikpa:meaning_id ?meaningId;
             wikpa:meaning_lang_pos_id ?langPosId.

    ?quote wikpa:quote_id ?quoteId;
           wikpa:quote_meaning_id ?meaningId;
           wikpa:quote_lang_id ?langId;
           wikpa:quote_text ?quoteText;
           wikpa:quote_ref_id ?quoteRefId.
}
```

## Get name of the author of quotes by word and language ##

Let's find author name for the quote of the Russian word "го":

SPARQL request:
```
SELECT ?langId ?pageId ?meaningId ?quoteId ?quoteText ?quoteRefId ?authorId ?authorName ?authorWikilink
WHERE {
    ?lang wikpa:lang_code "ru";
          wikpa:lang_id ?langId.

    ?page wikpa:page_page_title "го";
          wikpa:page_id ?pageId.

    ?lang_pos wikpa:lang_pos_page_id ?pageId;
              wikpa:lang_pos_lang_id ?langId;
              wikpa:lang_pos_id ?langPosId.

    ?meaning wikpa:meaning_id ?meaningId;
             wikpa:meaning_lang_pos_id ?langPosId.

    ?quote wikpa:quote_id ?quoteId;
           wikpa:quote_meaning_id ?meaningId;
           wikpa:quote_lang_id ?langId;
           wikpa:quote_text ?quoteText;
           wikpa:quote_ref_id ?quoteRefId.

    ?quot_ref wikpa:quot_ref_id ?quoteRefId;
               wikpa:quot_ref_author_id ?authorId. 

    ?quot_author wikpa:quot_author_id ?authorId;
                 wikpa:quot_author_name ?authorName;
                 wikpa:quot_author_wikilink ?authorWikilink.

}
```

# SQL queries #

This section contains examples of SQL queries related to quotes. See also [SQLExamples](SQLExamples.md).

## Get all quotes by language ##

Let's get all quotes in English language with non-empty date (years) (we are working with the Russian Wiktionary now). Let's the data will be in the range _'from'-'to'_, i.e. the quotes from the books written during more than in one year, e.g. 1926-1927.

1.
```
mysql> SELECT id FROM lang WHERE code="en";
```

| **id** |
|:-------|
| 285 |

The language code "en" (English) has **lang\_id** = 285.

2.
```
mysql> SELECT COUNT(*) from quote WHERE lang_id=285;
```

| **COUNT(`*`)** |
|:---------------|
| 1355 |

There are 1355 quotes for English words in the Russian Wiktionary (dump as of March 25, 2012)

See examples of several quotes in English:
```
mysql> SELECT * FROM lang, quote WHERE code="en" AND lang.id=quote.lang_id LIMIT 33;
```

3.
```
mysql> SELECT COUNT(*) FROM quote WHERE lang_id=285 AND ref_id is not NULL;
```

| **COUNT(`*`)** |
|:---------------|
| 222 |

There are 222 quotes in English with non-empty reference (i.e. there are an author, a title or date of quote).

4.
```
mysql> SELECT COUNT(*) FROM quote, quot_ref WHERE lang_id=285 AND ref_id=quot_ref.id AND year_id IS NOT NULL;
```

| **COUNT(`*`)** |
|:---------------|
| 123 |

There are 123 quotes in English with non-empty years in the reference.

See examples of several quotes in English with years:
```
mysql> SELECT * FROM quote, quot_ref, quot_year WHERE lang_id=285 AND ref_id=quot_ref.id AND year_id=quot_year.id LIMIT 33;
```

5.
```
mysql> SELECT COUNT(*) FROM quote, quot_ref, quot_year WHERE lang_id=285 AND ref_id=quot_ref.id AND year_id=quot_year.id AND `to`-`from`>0;
```

| **COUNT(`*`)** |
|:---------------|
| 7 |

There are 7 quotes in English from books written during more than in one year. These are the quotes from entries: [Moscow](http://ru.wiktionary.org/wiki/Moscow), [cacophony](http://ru.wiktionary.org/wiki/cacophony), [hoarder](http://ru.wiktionary.org/wiki/hoarder) (two), [order](http://ru.wiktionary.org/wiki/order) (two), [practitioner](http://ru.wiktionary.org/wiki/practitioner).

## Further reading ##
  * Retrieval and analysis of quotes’ dates in corpus of quotes of online dictionary. 2012. (todo link)

# Links #
## Russian Wiktionary ##
  * [Quotes statistics](http://ru.wiktionary.org/wiki/%D0%A3%D1%87%D0%B0%D1%81%D1%82%D0%BD%D0%B8%D0%BA:AKA_MBG/%D0%A1%D1%82%D0%B0%D1%82%D0%B8%D1%81%D1%82%D0%B8%D0%BA%D0%B0:%D0%A6%D0%B8%D1%82%D0%B0%D1%82%D1%8B)
  * [Шаблон:пример](http://ru.wiktionary.org/wiki/%D0%A8%D0%B0%D0%B1%D0%BB%D0%BE%D0%BD:%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80)
  * [Викисловарь:Рассчитывайте на непонятливого](http://ru.wiktionary.org/wiki/%D0%92%D0%A1:%D0%A0%D0%9D%D0%9D)
  * [Parameters of the database (with quotes) created by the Wiktionary parser](http://ru.wiktionary.org/wiki/%D0%A3%D1%87%D0%B0%D1%81%D1%82%D0%BD%D0%B8%D0%BA:AKA_MBG/%D0%A1%D1%82%D0%B0%D1%82%D0%B8%D1%81%D1%82%D0%B8%D0%BA%D0%B0:%D0%A0%D0%B0%D0%B7%D0%BC%D0%B5%D1%80%D1%8B_%D0%B1%D0%B0%D0%B7%D1%8B_%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85,_%D1%81%D0%BE%D0%B7%D0%B4%D0%B0%D0%BD%D0%BD%D0%BE%D0%B9_%D0%BF%D0%B0%D1%80%D1%81%D0%B5%D1%80%D0%BE%D0%BC_%D0%92%D0%B8%D0%BA%D0%B8%D1%81%D0%BB%D0%BE%D0%B2%D0%B0%D1%80%D1%8F)

## English Wiktionary ##
  * [Wiktionary:Quotations](http://en.wiktionary.org/wiki/Wiktionary:Quotations)
  * [Help:Example\_sentences](http://en.wiktionary.org/wiki/Help:Example_sentences)
  * [Template:usex](http://en.wiktionary.org/wiki/Template:usex)