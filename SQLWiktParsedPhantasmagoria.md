How to extract some information from the database **wikt\_parsed** of the parsed Wiktionary.

# wikt\_parsed #

The number of entries and selected types of lexical semantic information available from the Russian edition of Wiktionary as of January 2009 (see [ruwikt20090122\_parsed\_with\_translation](http://code.google.com/p/wikokit/downloads/detail?name=ruwikt20090122_parsed_with_translation.zip)).

| **Question** | **SQL** | **Result** | **Comment** | en | de | ru | uk | all languages |
|:-------------|:--------|:-----------|:------------|:---|:---|:---|:---|:--------------|
| English id by language code | SELECT `*` FROM lang WHERE code="en"; | id = 85 |  | 85 | 74 | 276 | 352 |
| Entries | SELECT COUNT(`*`) FROM lang\_pos WHERE lang\_id=85; | 2'813 |  | 2813 | 13'072 | 124'301 | 88'575 |
| noun POS id | SELECT `*` FROM part\_of\_speech WHERE name="noun"; | 11 |  |  |  |  |  |
|  | noun=11; verb=21; adjective=3; unknown=20 |  |  |  |  |  |  |
| English nouns | SELECT COUNT(`*`) FROM lang\_pos WHERE lang\_id=85 AND pos\_id=11; | 935 |  |  |  |  |  | all POS = 247'580 |
| nouns |  |  |  | 935 | 336 | 58'843 | 40'607 | 108'448 |
| verb | Parser is unfinished :) |  | Too little verbs!? | 342 | 49 | 356 | 24'096 | 26'290 |
| adjective |  |  |  | 184 | 18 | 2'168 | 23'536 | 26'864 |
| unknown | Parts of speech which were not recognized by the parser |  |  | 1'321 | 12'648 | 57'573 | 331 | 80'293 |
| synonymy relation id | SELECT `*` FROM relation\_type WHERE name="synonymy" | 26 |  |  |  |  |  |
|  | synonymy=26; antonymy=19; hypernymy=22; hyponymy=23; holonymy=21; meronymy=24 |  |  |  |  |  |  |
| total synonyms | SELECT COUNT(`*`) FROM relation WHERE relation\_type\_id=26; | 28'718 |  |  |  |  |  |
| total antonyms |  | 10'480 |  |  |  |  |  |
| total hypernyms |  | 18'975 |  |  |  |  |  |
| total hyponyms |  | 8'585 |  |  |  |  |  |
| total holonyms |  | 216 |  |  |  |  |  |
| total meronyms |  | 322 |  |  |  |  |  |
| total relations |  | 67'296 |  |  |  |  |  |

|  | SQL |  | | en | de | ru | uk |
|:-|:----|:-|:|:---|:---|:---|:---|


## Number of semantic relations for each language ##

| Language code | Language  | synonymy | antonymy | hypernymy | hyponymy | holonymy | meronymy | total |
|:--------------|:----------|:---------|:---------|:----------|:---------|:---------|:---------|:------|
| zh-tw | Chinese (Taiwan) | 0 | 0 | 1 | 0 | 0 | 0 | 1 |
| slovio | Slovio | 4 | 1 | 0 | 0 | 0 | 0 | 5 |
| pl | Polish | 42 | 8 | 11 | 2 | 0 | 0 | 63 |
| yo | Yoruba | 0 | 0 | 1 | 0 | 0 | 0 | 1 |
| udm | Udmurt | 22 | 37 | 14 | 6 | 0 | 0 | 79 |
| en | English | 1345 | 238 | 444 | 176 | 1 | 8 | 2212 |
| yi | Yiddish | 1 | 0 | 0 | 0 | 0 | 0 | 1 |
| sr | Serbian | 17 | 1 | 14 | 2 | 0 | 0 | 34 |
| uk | Ukrainian | 310 | 54 | 115 | 12 | 0 | 0 | 491 |
| vi | Vietnamese | 0 | 0 | 1 | 0 | 0 | 0 | 1 |
| chu | Old Church Slavonic | 8 | 0 | 0 | 0 | 0 | 0 | 8 |
| ang | Old English | 0 | 0 | 1 | 0 | 0 | 0 | 1 |
| de | German ("Du") | 665 | 234 | 474 | 473 | 0 | 2 | 1848 |
| lt | Lithuanian | 0 | 0 | 7 | 0 | 0 | 0 | 7 |
| sw | Swahili | 0 | 0 | 0 | 4 | 0 | 0 | 4 |
| is | Icelandic | 7 | 3 | 2 | 0 | 0 | 0 | 12 |
| nah | Nahuatl | 1 | 0 | 1 | 0 | 0 | 0 | 2 |
| fi | Finnish | 37 | 11 | 22 | 20 | 0 | 0 | 90 |
| tg | Tajiki (falls back to tg-cyrl) | 2 | 0 | 13 | 0 | 0 | 0 | 15 |
| la | Latin | 279 | 174 | 162 | 74 | 0 | 0 | 689 |
| ja | Japanese | 8 | 0 | 2 | 0 | 0 | 0 | 10 |
| fo | Faroese | 74 | 24 | 14 | 8 | 0 | 0 | 120 |
| eo | Esperanto | 20 | 2 | 34 | 10 | 0 | 0 | 66 |
| nl | Dutch | 43 | 9 | 14 | 12 | 0 | 0 | 78 |
| he | Hebrew | 0 | 4 | 2 | 0 | 0 | 0 | 6 |
| fa | Persian | 3 | 0 | 5 | 0 | 0 | 0 | 8 |
| ce | Chechen | 1 | 0 | 0 | 0 | 0 | 0 | 1 |
| crh | Crimean Tatar | 26 | 21 | 0 | 0 | 0 | 0 | 47 |
| xal | Kalmyk-Oirat (Kalmuk, Kalmuck, Kalmack, Qalmaq, Kalmytskii Jazyk, Khal:mag, Oirat, Volga Oirat, European Oirat, Western Mongolian) | 1 | 8 | 0 | 0 | 0 | 0 | 9 |
| af | Afrikaans | 4 | 2 | 1 | 0 | 0 | 0 | 7 |
| ru | Russian | 24338 | 9062 | 17033 | 7574 | 215 | 306 | 58528 |
| vep | Veps | 0 | 0 | 1 | 0 | 0 | 0 | 1 |
| el | Greek | 28 | 8 | 17 | 0 | 0 | 0 | 53 |
| es | Spanish | 122 | 18 | 78 | 7 | 0 | 6 | 231 |
| sk | Slovak | 6 | 10 | 10 | 0 | 0 | 0 | 26 |
| id | Indonesian | 2 | 0 | 1 | 0 | 0 | 0 | 3 |
| hi | Hindi | 0 | 0 | 1 | 0 | 0 | 0 | 1 |
| io | Ido | 0 | 1 | 0 | 0 | 0 | 0 | 1 |
| cs | Czech | 313 | 79 | 111 | 24 | 0 | 0 | 527 |
| ca | Catalan | 4 | 5 | 2 | 0 | 0 | 0 | 11 |
| art | Toki pona (art) | 0 | 18 | 30 | 7 | 0 | 0 | 55 |
| et | Estonian | 3 | 0 | 1 | 0 | 0 | 0 | 4 |
| bg | Bulgarian | 251 | 11 | 106 | 74 | 0 | 0 | 442 |
| grc | Ancient Greece | 11 | 2 | 0 | 8 | 0 | 0 | 21 |
| lv | Latvian | 0 | 0 | 4 | 2 | 0 | 0 | 6 |
| tt | Tatar (multiple scripts - defaults to Latin) | 139 | 81 | 53 | 5 | 0 | 0 | 278 |
| bs | Bosnian | 3 | 0 | 4 | 1 | 0 | 0 | 8 |
| ro | Romanian | 2 | 2 | 0 | 0 | 0 | 0 | 4 |
| eu | Basque | 0 | 0 | 1 | 0 | 0 | 0 | 1 |
| kk | Kazakh | 0 | 0 | 2 | 0 | 0 | 0 | 2 |
| os | Ossetic | 1 | 1 | 3 | 1 | 0 | 0 | 6 |
| hu | Hungarian | 3 | 0 | 1 | 0 | 0 | 0 | 4 |
| km | Khmer, Central | 0 | 0 | 1 | 0 | 0 | 0 | 1 |
| sv | Swedish | 11 | 2 | 7 | 4 | 0 | 0 | 24 |
| it | Italian | 214 | 43 | 25 | 33 | 0 | 0 | 315 |
| ky | Kirghiz | 2 | 4 | 0 | 0 | 0 | 0 | 6 |
| sl | Slovenian | 0 | 0 | 2 | 0 | 0 | 0 | 2 |
| ain | Ainu | 5 | 0 | 0 | 0 | 0 | 0 | 5 |
| an | Aragonese | 1 | 0 | 0 | 0 | 0 | 0 | 1 |
| hr | Croatian | 10 | 0 | 5 | 1 | 0 | 0 | 16 |
| oc | Occitan | 3 | 0 | 0 | 0 | 0 | 0 | 3 |
| tr | Turkish | 4 | 1 | 3 | 0 | 0 | 0 | 8 |
| no | Norwegian | 15 | 1 | 7 | 8 | 0 | 0 | 31 |
| fy | Frisian | 2 | 0 | 0 | 0 | 0 | 0 | 2 |
| be | Belarusian normative | 58 | 234 | 15 | 3 | 0 | 0 | 310 |
| ko | Korean | 41 | 9 | 0 | 1 | 0 | 0 | 51 |
| fr | French | 134 | 41 | 76 | 31 | 0 | 0 | 282 |
| INT | Translingual (INT) | 1 | 0 | 0 | 0 | 0 | 0 | 1 |
| pt | Portuguese | 10 | 3 | 2 | 0 | 0 | 0 | 15 |
| ab | Abkhaz | 2 | 0 | 4 | 0 | 0 | 0 | 6 |
| kom | Komi | 0 | 1 | 0 | 0 | 0 | 0 | 1 |
| cv | Chuvash | 2 | 0 | 1 | 0 | 0 | 0 | 3 |
| ia | Interlingua (IALA) | 5 | 0 | 2 | 0 | 0 | 0 | 7 |
| da | Danish | 18 | 10 | 5 | 0 | 0 | 0 | 33 |
| uz | Uzbek | 1 | 0 | 0 | 0 | 0 | 0 | 1 |
| mk | Macedonian | 33 | 2 | 6 | 2 | 0 | 0 | 43 |

Languages with relations:76

# See also #
  * [Database\_statistics](Database_statistics.md)
  * [SQLExamples](SQLExamples.md)

# References #
  * Zesch, T.; Mueller, C. & Gurevych, I. [Extracting Lexical Semantic Knowledge from Wikipedia and Wiktionary.](http://elara.tk.informatik.tu-darmstadt.de/publications/2008/lrec08_camera_ready.pdf) (see page 2, table 2)