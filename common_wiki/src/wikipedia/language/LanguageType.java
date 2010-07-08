// current: Taita dav
// http://en.wiktionary.org/wiki/Wiktionary:Index_to_templates/languages#Template_table
/*
 * LanguageType.java - code of languages in wiki.
 * 
 * Copyright (c) 2008-2010 Andrew Krizhanovsky <andrew.krizhanovsky at gmail.com>
 * Distributed under GNU General Public License.
 */

//http://ru.wiktionary.org/w/index.php?title=%D0%A8%D0%B0%D0%B1%D0%BB%D0%BE%D0%BD:%D0%BF%D0%B5%D1%80%D0%B5%D0%B2-%D0%B1%D0%BB%D0%BE%D0%BA&diff=next&oldid=1243557

package wikipedia.language;

import wikipedia.language.local.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;


/** Languages of wiki: code and name, e.g. ru and Russian. 
 *
 * Source of data: 
 * mediawiki-1.10.1/languages/Names.php
 *
 * Russian Wikipedia: http://ru.wikipedia.org/wiki/%D0%9A%D0%BE%D0%B4%D1%8B_%D1%8F%D0%B7%D1%8B%D0%BA%D0%BE%D0%B2
 *
 * Russian Wiktionary:
 *   Шаблон:перев-блок  or http://ru.wiktionary.org/wiki/%D0%A8%D0%B0%D0%B1%D0%BB%D0%BE%D0%BD:%D0%BF%D0%B5%D1%80%D0%B5%D0%B2-%D0%B1%D0%BB%D0%BE%D0%BA
 *   Шаблон:lang        or http://ru.wiktionary.org/wiki/%D0%A8%D0%B0%D0%B1%D0%BB%D0%BE%D0%BD:lang
 *
 * English Wiktionary
 *  http://en.wiktionary.org/wiki/Wiktionary:Index_to_templates/languages
 *  http://en.wiktionary.org/wiki/Wiktionary:LANGCODE
 *  http://en.wiktionary.org/wiki/Wiktionary:Translations/Wikification
 *
 * English Wikipedia
 *  http://en.wikipedia.org/wiki/ISO_639
 *  http://meta.wikimedia.org/wiki/List_of_Wikipedias
 *  http://en.wikipedia.org/wiki/List_of_languages_by_number_of_native_speakers
 */
public class LanguageType {
    
    /** Two (or more) letter language code, e.g. 'en', 'ru'. */
    private final String code;
    
    /** Language name, e.g. 'English', 'Русский'. */
    private final String name;

    /** Language name in English (ASCII), e.g. 'English', 'Russian'. 
     * See http://en.wiktionary.org/wiki/Wiktionary:Language_names
     */
    private final String english_name;
    
    private static Map<String, String>       code2name = new HashMap<String, String>();
    private static Map<String, LanguageType> code2lang = new HashMap<String, LanguageType>();

    /** It is need for English Wiktionary */
    private static Map<String, LanguageType> english2lang = new HashMap<String, LanguageType>();

    /** If there are more than one English names for the language */
    private static Map<String, LanguageType> multiple_names2type = new HashMap<String, LanguageType>();
 
    /** If there are more than one language code for the language */
    private static Map<String, LanguageType> multiple_codes2type = new HashMap<String, LanguageType>();


    private LanguageType(String code,String name,String english_name) {
        this.code = code;

        if(code.length() == 0 || name.length() == 0 || english_name.length() == 0)
            System.out.println("Error in LanguageType.LanguageType(): one of parameters is empty! The language code="+code+"; name=\'"+name+"\'; english_name=\'"+english_name+"\'.");

        //this.name         = name;
        //this.english_name = english_name;
        
        // only name in English is used now, name is skipped
        this.name           = english_name;
        this.english_name   = english_name;

        // check the uniqueness of the language code and the english_name
        String name_prev = code2name.get(code);
        LanguageType english_name_prev = english2lang.get(english_name);
        
        if(null != name_prev)
            System.out.println("Error in LanguageType.LanguageType(): duplication of code! The language code="+code+
                    " language='"+english_name+
                    "'. Check the maps code2name and english2lang.");

        if(null != english_name_prev)
            System.out.println("Error in LanguageType.LanguageType(): duplication of language! The language code="+code+
                    " language='"+english_name+
                    "'. Check the maps code2name and english2lang.");

        code2name.put(code, name);
        code2lang.put(code, this);
        english2lang.put(english_name, this);
    }

    /** Gets language code. */
    public String getCode() {
        return code;
    }

    /** Gets language name. */
    public String getName() {
        return name;
    }
    
    /** Checks weather exists the language code 'code'. */
    public static boolean has(String code) {
        return code2name.containsKey(code) ||
               multiple_codes2type.containsKey(code);
    }
    
    public String toString() { return code; }

    /** Gets language code in English (ASCII). */
    public String toStringASCII() {
        
        if(code.equalsIgnoreCase("Буква"))
            return "letter_ru";
            
        return code;
    }
    
    /** Returns true if the language has this 'code'. */
    public boolean equals(String code) {
        return code.equalsIgnoreCase(this.code);
    }
    
    /** Gets LanguageType by language code */
    public static LanguageType get(String code) throws NullPointerException
    {
        LanguageType lt;

        if(null != (lt = code2lang.get(code)))
            return  lt;

        if(null != (lt = multiple_codes2type.get(code)))
            return  lt;

        throw new NullPointerException("Null LanguageType (get)");
    }

    /** Checks weather exists the language name in English. */
    public static boolean hasEnglishName(String english) {
        return english2lang.containsKey(english) ||
        multiple_names2type.containsKey(english);
    }
    
    /** Gets LanguageType by language name in English.
     * 
     *  @return null if there is no corresponded language
     */
    public static LanguageType getByEnglishName (String english) // throws NullPointerException
    {
        LanguageType lt;

        if(null != (lt = english2lang.get(english)))
            return  lt;


        if(null != (lt = multiple_names2type.get(english)))
            return  lt;

        return null; // throw new NullPointerException("Null LanguageType (getByEnglishName)");
    }

    /** Counts number of languages. */
    public static int size() {
        return code2name.size();
    }
    
    /** Gets all languages. */
    public static Map<String, LanguageType> getAllLanguages() {
        return code2lang;
    }

    /////////////////////////////////////

    /** Adds one more language code and language name for the same language.
     */
    public static LanguageType addNonUnique(LanguageType lt,
                                             String code, String english_name) {
        addNonUniqueName(lt, english_name);
        return addNonUniqueCode(lt, code);
    }

    /** Adds one more language name for this language.
     */
    public static LanguageType addNonUniqueName(LanguageType lt, String english_name) {

        if(english_name.length() == 0) {
            System.out.println("Error in LanguageType.addNonUniqueName(): empty language name! The language code="+lt+".");
            return null;
        }

        if(english2lang.containsKey(english_name)) {
            System.out.println("Error in LanguageType.addNonUniqueName(): the language '"+english_name+
                    "' is already presented in the map english2lang!");
            return null;
        }

        if(multiple_names2type.containsKey(english_name)) {
            System.out.println("Error in LanguageType.addNonUniqueName(): the language '"+english_name+
                    "' is already presented in the map multiple_names2type!");
            return null;
        }

        multiple_names2type.put(english_name, lt);
        return lt;
    }
    
    /** Adds one more language code for this language.
     */
    public static LanguageType addNonUniqueCode(LanguageType lt, String code) {

        if(code.length() > 12) {
            System.out.println("Error in LanguageType.addNonUniqueCode(): the language code '"+code+
                    "' is too long (.length() > 12)!");// zh-classical
            return null;
        }


        if(code2lang.containsKey(code)) {
            System.out.println("Error in LanguageType.addNonUniqueCode(): the language code '"+code+
                    "' is already presented in the map code2lang!");
            return null;
        }
        
        if(multiple_codes2type.containsKey(code)) {
            System.out.println("Error in LanguageType.addNonUniqueName(): the language '"+code+
                    "' is already presented in the map multiple_codes2type!");
            return null;
        }
        
        multiple_codes2type.put(code, lt);
        return lt;
    }

    /////////////////////////////////////
    
    /** The set of unknown language codes, which were found during parsing.
     * There is only one message for one uknown language code (for concise logging).
     */
    private static Set<String> unknown_lang_code = new HashSet<String>();
    private static Set<String> unknown_lang_name = new HashSet<String>();

    /** Checks weather exists the unknown language code 'code'. */
    public static boolean hasUnknownLangCode(String code) {
        return unknown_lang_code.contains(code);
    }

    /** Adds unknown language code 'code'. */
    public static boolean addUnknownLangCode(String code) {
        return unknown_lang_code.add(code);
    }

    /** Checks weather exists the unknown language name. */
    public static boolean hasUnknownLangName(String name) {
        return unknown_lang_name.contains(name);
    }

    /** Adds unknown language name. */
    public static boolean addUnknownLangName(String code) {
        return unknown_lang_name.add(code);
    }

    /////////////////////////////////////

    // List of localizations / translations

    // private static final LanguageTypeLocal local_ru = new LanguageTypeRu();
    //                                     local_de
    //                                     local_fr etc...
    
    /** Translates the language name into the target language.
     * 
     *  @return Language name in English if there is no a translation
     */
    public String translateTo (LanguageType target)
    {
        if(null == target || LanguageType.en == target)
            return english_name;

        if(LanguageType.ru == target) {
            return LanguageTypeRu.get(this);
        //} else if(l == LanguageType.de) { // todo some day
        }
        
        return english_name; // if there is no translation into local language, then English name
    }

    /** Check wheather exists the translation of the language into the target
     * language. */
    public boolean hasTranslation (LanguageType target)
    {
        if(LanguageType.en == target)
            return true;

        if(null == target)
            return false;

        if(LanguageType.ru == target) {
            return LanguageTypeRu.has(this);
        //} else if(l == LanguageType.de) { // todo some day
        }

        return false; // if there is no translation into local language, then English name
    }


    
    // English Wiktionary specific codes
    public static final LanguageType translingual = new LanguageType("translingual", "Translingual", "Translingual");


    // more than one language code (or language name) for one language
    public static final LanguageType aar = new LanguageType("aar", "Afar", "Afar");
    public static final LanguageType aa = LanguageType.addNonUniqueCode(aar, "aa");

    public static final LanguageType abk = new LanguageType("abk", "РђТ§СЃСѓР°", "Abkhaz");
    public static final LanguageType ab  = LanguageType.addNonUniqueCode(abk, "ab");
    
    public static final LanguageType acr = new LanguageType("acr", "Achi", "Achi");
    public static final LanguageType acc = LanguageType.addNonUnique(acr, "acc", "Cubulco");

    public static final LanguageType ace = new LanguageType("ace", "AchГЁh", "Acehnese");
    public static final LanguageType ace2 = LanguageType.addNonUniqueName(ace, "Aceh");
    
    public static final LanguageType acv = new LanguageType("acv", "Achumawi", "Achumawi");
    public static final LanguageType acv2 = LanguageType.addNonUniqueName(acv, "Achomawi");
    
    public static final LanguageType ada = new LanguageType("ada", "Adangme", "Adangme");
    public static final LanguageType ada2 = LanguageType.addNonUniqueName(ada, "Dangme");
    
    public static final LanguageType adz = new LanguageType("adz", "Adzera", "Adzera");
    public static final LanguageType zsu = LanguageType.addNonUnique(adz, "zsu", "Sukurum");
    public static final LanguageType zsa = LanguageType.addNonUnique(adz, "zsa", "Sarasira");

    public static final LanguageType afr = new LanguageType("afr", "Afrikaans", "Afrikaans");
    public static final LanguageType af  = LanguageType.addNonUniqueCode(afr, "af");

    public static final LanguageType agx    = new LanguageType("agx", "Aghul", "Aghul");
    public static final LanguageType agx2 = LanguageType.addNonUniqueName(agx, "Agul");
    
    public static final LanguageType ain    = new LanguageType("ain", "Ainu", "Ainu");
    public static final LanguageType ain_lat = LanguageType.addNonUnique(ain, "ain.lat", "Ainu (Latin)");//In Russian Wiktionary
    public static final LanguageType ain_kana = LanguageType.addNonUnique(ain, "ain.kana", "Ainu (Kana)");//In Russian Wiktionary

    public static final LanguageType aka = new LanguageType("aka", "Akan", "Akan");
    public static final LanguageType ak  = LanguageType.addNonUniqueCode(aka, "ak");
    public static final LanguageType fat = new LanguageType("fat", "Fante", "Fante");
    public static final LanguageType fat2 = LanguageType.addNonUniqueName(fat, "Fanti");
    public static final LanguageType twi = new LanguageType("twi", "Twi", "Twi");
    public static final LanguageType tw = LanguageType.addNonUniqueCode(twi, "tw");

    public static final LanguageType alr = new LanguageType("alr", "Alyutor", "Alyutor");
    public static final LanguageType alr2 = LanguageType.addNonUniqueName(alr, "Alutor");

    public static final LanguageType alt = new LanguageType("alt", "Altai", "Altai");
    public static final LanguageType alt2 = LanguageType.addNonUniqueName(alt, "Southern Altai");
    public static final LanguageType atv = new LanguageType("atv", "Northern Altai", "Northern Altai");

    public static final LanguageType am = new LanguageType("am", "бЉ б€›б€­бЉ›", "Amharic");
    public static final LanguageType amh = LanguageType.addNonUniqueCode(am, "amh");
    
    public static final LanguageType apk = new LanguageType("apk", "Plains Apache", "Plains Apache");
    public static final LanguageType apk2 = LanguageType.addNonUniqueName(apk, "Kiowa Apache");
    public static final LanguageType apw = new LanguageType("apw", "Western Apache", "Western Apache");
    
    
    // Arabic ------------
    public static final LanguageType ara = new LanguageType("ara", "Arabic", "Arabic");
    public static final LanguageType ar = LanguageType.addNonUniqueCode(ara, "ar");

    public static final LanguageType arb = new LanguageType("arb", "Standard Arabic", "Standard Arabic");
    public static final LanguageType arb2 = LanguageType.addNonUniqueName(arb, "Modern Standard Arabic");
    public static final LanguageType arb3 = LanguageType.addNonUniqueName(arb, "Literary Arabic");
    
    public static final LanguageType aao = new LanguageType("aao", "Saharan Arabic", "Saharan Arabic");
    public static final LanguageType aao2 = LanguageType.addNonUniqueName(aao, "Algerian Saharan Arabic");
    
    public static final LanguageType acx = new LanguageType("acx", "Omani Arabic", "Omani Arabic");
    public static final LanguageType aeb = new LanguageType("aeb", "Tunisian Arabic", "Tunisian Arabic");
    public static final LanguageType afb = new LanguageType("afb", "Gulf Arabic", "Gulf Arabic");

    public static final LanguageType ajp = new LanguageType("ajp", "South Levantine Arabic", "South Levantine Arabic");
    public static final LanguageType apc = new LanguageType("apc", "North Levantine Arabic", "North Levantine Arabic");

    public static final LanguageType apd = new LanguageType("apd", "Sudanese Arabic", "Sudanese Arabic");

    public static final LanguageType arq = new LanguageType("arq", "Algerian Arabic", "Algerian Arabic");
    public static final LanguageType ars = new LanguageType("ars", "Najdi Arabic", "Najdi Arabic");
    public static final LanguageType ary = new LanguageType("ary", "Moroccan Arabic", "Moroccan Arabic");
    public static final LanguageType arz = new LanguageType("arz", "Щ…ШµШ±Щ‰", "Egyptian Arabic");
    
    public static final LanguageType ayl = new LanguageType("ayl", "Libyan Arabic", "Libyan Arabic");
    public static final LanguageType ayl2 = LanguageType.addNonUniqueName(ayl, "Sulaimitian Arabic");
    
    public static final LanguageType ayp = new LanguageType("ayp", "North Mesopotamian Arabic", "North Mesopotamian Arabic");

    public static final LanguageType pga = new LanguageType("pga", "Juba Arabic", "Juba Arabic");
    
    public static final LanguageType shu = new LanguageType("shu", "Chadian Arabic", "Chadian Arabic");
    public static final LanguageType shu2 = LanguageType.addNonUniqueName(shu, "Shuwa Arabic");
    // ------------ eo Arabic


    // Aramaic ------------
    public static final LanguageType arc = new LanguageType("arc", "ЬђЬЄЬЎЬќЬђ", "Aramaic");
    public static final LanguageType arc2 = LanguageType.addNonUniqueName(arc, "Official Aramaic");
    public static final LanguageType tmr = LanguageType.addNonUnique(arc, "tmr", "Jewish Babylonian Aramaic");

    public static final LanguageType arc_jud = LanguageType.addNonUniqueCode(arc, "arc.jud");// Russian Wiktionary

    public static final LanguageType aii = new LanguageType("aii", "Assyrian Neo-Aramaic", "Assyrian Neo-Aramaic");
    
    public static final LanguageType sam = new LanguageType("sam", "Samaritan Aramaic", "Samaritan Aramaic");
    
    public static final LanguageType syc = new LanguageType("syc", "Syriac", "Syriac");
    public static final LanguageType syr = LanguageType.addNonUniqueCode(syc, "syr");
    public static final LanguageType arc_syr = LanguageType.addNonUniqueCode(syc, "arc.syr");// Russian Wiktionary

    public static final LanguageType tru = new LanguageType("tru", "Turoyo", "Turoyo");
    public static final LanguageType tru2 = LanguageType.addNonUniqueName(tru, "Surayt");
    // not yet in English Wiktionary:
    //
    // oar – Old Aramaic    Ancient Aramaic
    // aij – Lishanid Noshan, Neo-Aramaic or Judeo-Aramaic
    // amw – Western Neo-Aramaic
    // bhn – Bohtan Neo-Aramaic
    // bjf – Barzani Jewish Neo-Aramaic
    // cld – Chaldean Neo-Aramaic
    // hrt – Hértevin
    // huy – Hulaulá
    // jpa – Jewish Palestinian Aramaic
    // kqd – Koy Sanjaq Surat
    // lhs – Mlahsô
    // lsd – Lishana Deni
    // mid – Modern Mandaic
    // myz – Classical Mandaic
    // syn – Senaya
    // trg – Lishán Didán
    // ------------ eo Aramaic


    public static final LanguageType are = new LanguageType("are", "Arrernte", "Arrernte");
    public static final LanguageType are2 = LanguageType.addNonUniqueName(are, "Western Arrernte");
    public static final LanguageType aer = LanguageType.addNonUnique(are, "aer", "Eastern Arrernte");
    public static final LanguageType amx = LanguageType.addNonUnique(are, "amx", "Anmatjirra");
    public static final LanguageType aly = LanguageType.addNonUnique(are, "aly", "Alyawarr");
    public static final LanguageType adg = LanguageType.addNonUnique(are, "adg", "Antekerrepenhe");

    public static final LanguageType arg = new LanguageType("arg", "AragonГ©s", "Aragonese");
    public static final LanguageType an = LanguageType.addNonUniqueCode(arg, "an");
    
    public static final LanguageType aru = new LanguageType("aru", "Aruá", "Arua");
    public static final LanguageType aru2 = LanguageType.addNonUniqueName(aru, "Aruá");
    
    public static final LanguageType av = new LanguageType("av", "РђРІР°СЂ", "Avar");
    public static final LanguageType ava = LanguageType.addNonUniqueCode(av, "ava");
    
    public static final LanguageType ave = new LanguageType("ave", "Avestan", "Avestan");
    public static final LanguageType ae = LanguageType.addNonUniqueCode(ave, "ae");

    public static final LanguageType awk = new LanguageType("awk", "Awabakal", "Awabakal");
    public static final LanguageType awk2 = LanguageType.addNonUniqueName(awk, "Awabagal");
    
    public static final LanguageType ay = new LanguageType("ay", "Aymar aru", "Aymara");
    public static final LanguageType aym = LanguageType.addNonUniqueCode(ay, "aym");
    public static final LanguageType ayr = LanguageType.addNonUnique(ay, "ayr", "Central Aymara");
    public static final LanguageType ayc = LanguageType.addNonUnique(ay, "ayc", "Southern Aymara");

    
    // Azerbaijani ------------
    public static final LanguageType az = new LanguageType("az", "AzЙ™rbaycan", "Azerbaijani");
    public static final LanguageType aze = LanguageType.addNonUnique(az, "aze", "Azeri");

    public static final LanguageType azj = LanguageType.addNonUnique(az, "azj", "North Azeri");
    public static final LanguageType azj2 = LanguageType.addNonUniqueName(az, "North Azerbaijani");

    public static final LanguageType azb = LanguageType.addNonUnique(az, "azb", "South Azeri");
    public static final LanguageType azb2 = LanguageType.addNonUniqueName(az, "South Azerbaijani");
    // ------------ eo Azerbaijani


    public static final LanguageType bal = new LanguageType("bal", "Balochi", "Balochi");
    public static final LanguageType bal2 = LanguageType.addNonUniqueName(bal, "Baluchi");

    public static final LanguageType ban    = new LanguageType("ban", "Bali", "Bali");// Balinese or Bali language (Nigeria)?
    public static final LanguageType ban2 = LanguageType.addNonUniqueName(ban, "Balinese");

    public static final LanguageType bas = new LanguageType("bas", "Basaa", "Basaa");
    public static final LanguageType bas2 = LanguageType.addNonUniqueName(bas, "Bissa");
    // 3 Basa and 4 Bassa: see the problem at http://en.wikipedia.org/wiki/Basaa_language
    public static final LanguageType bas3 = LanguageType.addNonUniqueName(bas, "Basa");// Bassa==Kainji language
    public static final LanguageType bas4 = LanguageType.addNonUniqueName(bas, "Bassa");// Problem: Bassa==Kru language


    // Bikol ------------
    public static final LanguageType bik = new LanguageType("bik", "Bikol", "Bikol");

    public static final LanguageType bcl = new LanguageType("bcl", "Bikol Central", "Bikol Central");
    public static final LanguageType bcl2 = LanguageType.addNonUniqueName(bcl, "Central Bikolano");
    // ------------ eo Bikol


    public static final LanguageType be = new LanguageType("be", "Р‘РµР»Р°СЂСѓСЃРєР°СЏ", "Belarusian");// Belarusian normative
    public static final LanguageType bel = LanguageType.addNonUniqueCode(be, "bel");
    public static final LanguageType be_tarask = LanguageType.addNonUniqueCode(be, "be-tarask");// Belarusian (Taraškievica)
    public static final LanguageType be_x_old = LanguageType.addNonUniqueCode(be, "be-x-old");

    public static final LanguageType bg = new LanguageType("bg", "Р‘СЉР»РіР°СЂСЃРєРё", "Bulgarian");
    public static final LanguageType bul = LanguageType.addNonUniqueCode(bg, "bul");

    
    // Bihari ------------
    public static final LanguageType bh = new LanguageType("bh", "Bihari", "Bihari");

    public static final LanguageType anp = new LanguageType("anp", "Angika", "Angika");
    public static final LanguageType bho = new LanguageType("bho", "а¤­аҐ‹а¤ња¤ЄаҐЃа¤°аҐЂ", "Bhojpuri");

    public static final LanguageType hif = new LanguageType("hif", "Fiji Hindi", "Fiji Hindi");
    public static final LanguageType hif2 = LanguageType.addNonUniqueName(hif, "Fijian Hindi");
    public static final LanguageType hif_deva = LanguageType.addNonUniqueCode(hif, "hif-deva");// Fiji Hindi (devangari)
    public static final LanguageType hif_latn = LanguageType.addNonUniqueCode(hif, "hif-latn");// Fiji Hindi (latin)

    public static final LanguageType mag = new LanguageType("mag", "Magahi", "Magahi");

    public static final LanguageType mai = new LanguageType("mai", "а¤®аҐ€а¤Ґа¤їа¤ІаҐЂ", "Maithili");
    // not yet in English Wiktionary:
    //
    //  Kudmali (kyw)
    //  Majhi (mjz)
    //  Musasa (smm)
    //  Panchpargania (tdb) // Панчпарганья
    //  Sadri (sck) // Садри
    //  Sadri, Oraon (sdr)
    //  Sarnami Hindustani (hns)
    //  Surajpuri (sjp)
    //  Vajjika
    // ------------ eo Bihari


    public static final LanguageType bm = new LanguageType("bm", "Bamanankan", "Bambara");
    public static final LanguageType bam = LanguageType.addNonUnique(bm, "bam", "Bamanankan");

    public static final LanguageType bn = new LanguageType("bn", "Bengali", "Bengali");
    public static final LanguageType ben = LanguageType.addNonUnique(bn, "ben", "Bangla");

    public static final LanguageType bo = new LanguageType("bo", "аЅ–аЅјаЅ‘ај‹аЅЎаЅІаЅ‚", "Tibetan");
    public static final LanguageType bo2 = LanguageType.addNonUniqueName(bo, "Standard Tibetan");
    public static final LanguageType bo3 = LanguageType.addNonUniqueName(bo, "Central Tibetan");

    public static final LanguageType bs = new LanguageType("bs", "Bosanski", "Bosnian");
    public static final LanguageType bos = LanguageType.addNonUniqueCode(bs, "bos");

    public static final LanguageType br = new LanguageType("br", "Brezhoneg", "Breton");
    public static final LanguageType bre = LanguageType.addNonUniqueCode(br, "bre");
    public static final LanguageType obt = new LanguageType("obt", "Old Breton", "Old Breton");
    public static final LanguageType xbm = new LanguageType("xbm", "Middle Breton", "Middle Breton");

    public static final LanguageType bra = new LanguageType("bra", "Braj", "Braj");
    public static final LanguageType bra2 = LanguageType.addNonUniqueName(bra, "Braj Bhasha");
    
    public static final LanguageType btk = new LanguageType("btk", "Batak", "Batak");
    public static final LanguageType bya = LanguageType.addNonUniqueCode(btk, "bya");

    public static final LanguageType bua = new LanguageType("bua", "Buryat", "Buryat");
    public static final LanguageType bxr = LanguageType.addNonUnique(bua, "bxr", "Russia Buriat");
    public static final LanguageType bxu = LanguageType.addNonUnique(bua, "bxu", "China Buriat");
    public static final LanguageType bxm = LanguageType.addNonUnique(bua, "bxm", "Mongolia Buriat");

    public static final LanguageType bug = new LanguageType("bug", "бЁ…бЁ” бЁ•бЁбЁЃбЁ—", "Buginese");
    public static final LanguageType bug2 = LanguageType.addNonUniqueName(bug, "Bugi");

    public static final LanguageType ca = new LanguageType("ca", "CatalГ ", "Catalan");
    public static final LanguageType cat = LanguageType.addNonUniqueCode(ca, "cat");

    public static final LanguageType caa = new LanguageType("caa", "Ch'orti'", "Ch'orti'");
    public static final LanguageType caa2 = LanguageType.addNonUniqueName(caa, "Chorti");

    public static final LanguageType car = new LanguageType("car", "Carib", "Carib");
    public static final LanguageType crb = LanguageType.addNonUnique(car, "crb", "Galibi Carib");

    public static final LanguageType ce = new LanguageType("ce", "РќРѕС…С‡РёР№РЅ", "Chechen");
    public static final LanguageType che = LanguageType.addNonUniqueCode(ce, "che");

    public static final LanguageType ch = new LanguageType("ch", "Chamoru", "Chamorro");
    public static final LanguageType cha = LanguageType.addNonUniqueCode(ch, "cha");

    public static final LanguageType chk = new LanguageType("chk", "Chuukese", "Chuukese");
    public static final LanguageType chk2 = LanguageType.addNonUniqueName(chk, "Trukese");

    public static final LanguageType chp = new LanguageType("chp", "Chipewyan", "Chipewyan");
    public static final LanguageType chp2 = LanguageType.addNonUniqueName(chp, "Dene Suline");
    
    public static final LanguageType co = new LanguageType("co", "Corsu", "Corsican");
    public static final LanguageType cos = LanguageType.addNonUniqueCode(co, "cos");

    public static final LanguageType cpx = new LanguageType("cpx", "Pu-Xian", "Pu-Xian");
    public static final LanguageType cpx2 = LanguageType.addNonUniqueName(cpx, "Puxian Min");
    public static final LanguageType cpx3 = LanguageType.addNonUniqueName(cpx, "Puxian");
    public static final LanguageType cpx4 = LanguageType.addNonUniqueName(cpx, "Xinghua");


    // Cree ------------
    public static final LanguageType cr = new LanguageType("cr", "Cree", "Cree");
    public static final LanguageType cre = LanguageType.addNonUniqueCode(cr, "cre");
    
    public static final LanguageType moe = new LanguageType("moe", "Montagnais", "Montagnais");
    public static final LanguageType moe2 = LanguageType.addNonUniqueName(moe, "Innu-aimun");

    public static final LanguageType crk = new LanguageType("crk", "Plains Cree", "Plains Cree");
    // not yet in English Wiktionary:
    //
    // nsk – Naskapi
    // atj – Atikamekw
    // crm – Moose Cree
    // crl – Northern East Cree
    // crj – Southern East Cree
    // crw – Swampy Cree
    // cwd – Woods Cree
    // ------------ eo Cree


    public static final LanguageType crh = new LanguageType("crh", "Crimean Tatar", "Crimean Tatar");
    public static final LanguageType crh_latn = LanguageType.addNonUnique(crh, "crh-latn", "Crimean Tatar (Latin)");
    public static final LanguageType crh_cyrl = LanguageType.addNonUnique(crh, "crh-cyrl", "Crimean Tatar (Cyrillic)");

    public static final LanguageType cs = new LanguageType("cs", "ДЊesky", "Czech");
    public static final LanguageType ces = LanguageType.addNonUniqueCode(cs, "ces");
    public static final LanguageType cze = LanguageType.addNonUniqueCode(cs, "cze");

    public static final LanguageType csb = new LanguageType("csb", "KaszГ«bsczi", "Cassubian");
    public static final LanguageType csb2 = LanguageType.addNonUniqueName(csb, "Kashubian");
    
    public static final LanguageType cu = new LanguageType("cu", "Old Church Slavonic", "Old Church Slavonic");
    public static final LanguageType chu = LanguageType.addNonUniqueCode(cu, "chu");
    public static final LanguageType chu_cyr = LanguageType.addNonUnique(cu, "chu.cyr", "Old Church Slavonic (Cyrillic)");// in Russian Wiktionary
    public static final LanguageType chu_glag = LanguageType.addNonUnique(cu, "chu.glag", "Old Church Slavonic (Glagolitic)");// in Russian Wiktionary
    public static final LanguageType chu_ru = LanguageType.addNonUnique(cu, "chu-ru", "Church Slavonic");// Russian Wiktionary

    public static final LanguageType cuh = new LanguageType("cuh", "Chuka", "Chuka");
    public static final LanguageType cuh2 = LanguageType.addNonUniqueName(cuh, "Gichuka");

    public static final LanguageType cuk = new LanguageType("cuk", "Kuna", "Kuna");
    public static final LanguageType kvn = LanguageType.addNonUniqueCode(cuk, "kvn");

    public static final LanguageType cv = new LanguageType("cv", "Р§ДѓРІР°С€Р»Р°", "Chuvash");
    public static final LanguageType chv = LanguageType.addNonUniqueCode(cv, "chv");

    public static final LanguageType da = new LanguageType("da", "Dansk", "Danish");
    public static final LanguageType dan = LanguageType.addNonUniqueCode(da, "dan");

    public static final LanguageType dar = new LanguageType("dar", "Dargwa", "Dargwa");
    public static final LanguageType dar2 = LanguageType.addNonUniqueName(dar, "Dargin");

    

    // German ------------
    public static final LanguageType de = new LanguageType("de", "Deutsch", "German");
    public static final LanguageType de2 = LanguageType.addNonUniqueName(de, "Ripuarian");
    public static final LanguageType deu = LanguageType.addNonUnique(de, "deu", "New High German");
    public static final LanguageType pfl = LanguageType.addNonUnique(de, "pfl", "Palatinate German");

    public static final LanguageType bar = new LanguageType("bar", "Boarisch", "Bavarian");
    public static final LanguageType bar2 = LanguageType.addNonUniqueName(bar, "Austro-Bavarian");

    public static final LanguageType gmh = new LanguageType("gmh", "Middle High German", "Middle High German");
    public static final LanguageType goh = new LanguageType("goh", "Old High German", "Old High German");
    public static final LanguageType pdc = new LanguageType("pdc", "Deitsch", "Pennsylvania German");
    public static final LanguageType ksh = new LanguageType("ksh", "Kölsch", "Kölsch");

    public static final LanguageType gsw = new LanguageType("gsw", "Schwyzerdütsch", "Swiss German");
    public static final LanguageType gsw2 = LanguageType.addNonUniqueName(gsw, "Alemannic German");
    
    public static final LanguageType nds = new LanguageType("nds", "Plattdüütsch", "Low Saxon");
    public static final LanguageType nds2 = LanguageType.addNonUniqueName(nds, "Low German");
    public static final LanguageType pdt = LanguageType.addNonUnique(nds, "pdt", "Plautdietsch");

    public static final LanguageType nds_nl = new LanguageType("nds-nl", "Nedersaksisch", "Dutch Low Saxon");

    public static final LanguageType uln = new LanguageType("uln", "Unserdeutsch", "Unserdeutsch");
    // not yet in English Wiktionary: (from WP German)
    // gct – Alemán Coloniero
    // cim – Cimbrian
    // geh – Hutterite German
    // sli – Lower Silesian
    // ltz – Luxembourgish
    // vmf – Main-Franconian
    // mhn – Mócheno
    // swg – Swabian German
    // sxu – Upper Saxon
    // wae – Walser German
    // wep – Westphalian
    // ------------ eo German



    public static final LanguageType ewe    = new LanguageType("ewe", "Ewe", "Ewe");
    public static final LanguageType ee     = LanguageType.addNonUniqueCode(ewe, "ee");


    // Greek ------------
    public static final LanguageType el = new LanguageType("el", "ελληνικά", "Greek");
    public static final LanguageType ell = LanguageType.addNonUniqueCode(el, "ell");
    public static final LanguageType el_dhi = LanguageType.addNonUniqueCode(el, "el.dhi");// ruwikt Греческий демот.
    public static final LanguageType el_kat = LanguageType.addNonUniqueCode(el, "el.kat");// ruwikt Греческий кафар.
    
    public static final LanguageType cpg = new LanguageType("cpg", "Cappadocian Greek", "Cappadocian Greek");

    public static final LanguageType pnt = new LanguageType("pnt", "Ποντιακή διάλεκτος", "Pontic Greek");

    public static final LanguageType gmy = new LanguageType("gmy", "Mycenaean Greek", "Mycenaean Greek");

    public static final LanguageType rge = new LanguageType("rge", "Ελληνο-ρομανική", "Romano-Greek");
    
    public static final LanguageType tsd = new LanguageType("tsd", "Τσακώνικα", "Tsakonian");

    public static final LanguageType grc = new LanguageType("grc", "ἡ Ἑλληνικὴ γλῶσσα", "Ancient Greek");
    public static final LanguageType grc_att = LanguageType.addNonUnique(grc, "grc-att", "Attic Greek");
    public static final LanguageType grc_ion = LanguageType.addNonUnique(grc, "grc-ion", "Ionic Greek");
    // ------------ eo Greek


    public static final LanguageType et = new LanguageType("et", "Eesti", "Estonian");
    public static final LanguageType est = LanguageType.addNonUniqueCode(et, "est");

    public static final LanguageType eu = new LanguageType("eu", "Euskara", "Basque");
    public static final LanguageType eus = LanguageType.addNonUniqueCode(eu, "eus");


    // Persian ------------
    public static final LanguageType fa = new LanguageType("fa", "ЩЃШ§Ш±ШіЫЊ", "Persian");
    
    public static final LanguageType jpr = new LanguageType("jpr", "Judeo-Persian", "Judeo-Persian");
    public static final LanguageType jpr2 = LanguageType.addNonUniqueName(jpr, "Dzhidi");
    public static final LanguageType jpr3 = LanguageType.addNonUniqueName(jpr, "Judæo-Persian");
    public static final LanguageType jpr4 = LanguageType.addNonUniqueName(jpr, "Jidi");

    public static final LanguageType drw = new LanguageType("drw", "Darwazi", "Darwazi");
    public static final LanguageType pal = new LanguageType("pal", "Middle Persian", "Middle Persian");
    public static final LanguageType peo = new LanguageType("peo", "Old Persian", "Old Persian");
    public static final LanguageType prs = new LanguageType("prs", "Eastern Persian", "Eastern Persian");
    // not yet in English Wiktionary:
    // fas – Persian
    // pes – Western Persian
    // tgk – Tajik
    // aiq – Aimaq
    // bhh – Bukharic
    // haz – Hazaragi
    // phv – Pahlavani
    // ------------ eo Persian
    

    
    public static final LanguageType fi = new LanguageType("fi", "Suomi", "Finnish");
    public static final LanguageType fin = LanguageType.addNonUniqueCode(fi, "fin");

    public static final LanguageType fo = new LanguageType("fo", "Føroyskt", "Faroese");
    public static final LanguageType fao = LanguageType.addNonUniqueCode(fo, "fao");

    public static final LanguageType frr = new LanguageType("frr", "North Frisian", "North Frisian");
    public static final LanguageType frs = new LanguageType("frs", "Eastern Frisian", "Eastern Frisian");
    public static final LanguageType fy = new LanguageType("fy", "Frysk", "West Frisian");
    public static final LanguageType fry = LanguageType.addNonUniqueCode(fy, "fry");
    public static final LanguageType stq = new LanguageType("stq", "Seeltersk", "Saterland Frisian");
    public static final LanguageType ofs    = new LanguageType("ofs", "Old Frisian", "Old Frisian");

    public static final LanguageType gd = new LanguageType("gd", "Gàidhlig", "Scottish Gaelic");
    public static final LanguageType gla = LanguageType.addNonUniqueCode(gd, "gla");

    public static final LanguageType gl = new LanguageType("gl", "Galego", "Galician");
    public static final LanguageType glg = LanguageType.addNonUniqueCode(gl, "glg");

    public static final LanguageType grn = new LanguageType("grn", "Guaraní", "Guaraní");
    public static final LanguageType gn  = LanguageType.addNonUnique(grn, "gn", "Guarani");
    public static final LanguageType tup = new LanguageType("tup", "Tupí-Guaraní", "Tupí-Guaraní");

    public static final LanguageType he = new LanguageType("he", "Hebrew", "Hebrew");
    public static final LanguageType heb = LanguageType.addNonUniqueCode(he, "heb");
    public static final LanguageType hbo = LanguageType.addNonUnique(he, "hbo", "Ancient Hebrew");

    public static final LanguageType hi = new LanguageType("hi", "Hindī", "Hindi");
    public static final LanguageType hin = LanguageType.addNonUniqueCode(hi, "hin");

    public static final LanguageType hr = new LanguageType("hr", "Hrvatski", "Croatian");
    public static final LanguageType hrv = LanguageType.addNonUniqueCode(hr, "hrv");
    
    public static final LanguageType hu = new LanguageType("hu", "Magyar", "Hungarian");
    public static final LanguageType hun = LanguageType.addNonUniqueCode(hu, "hun");


    // Armenian ------------
    public static final LanguageType hy = new LanguageType("hy", "ХЂХЎХµХҐЦЂХҐХ¶", "Armenian");
    public static final LanguageType hye = LanguageType.addNonUnique(hy, "hye", "Modern Armenian");

    public static final LanguageType axm = new LanguageType("axm", "Middle Armenian", "Middle Armenian");

    public static final LanguageType xcl    = new LanguageType("xcl", "Classical Armenian", "Classical Armenian");
    public static final LanguageType xcl2 = LanguageType.addNonUniqueName(xcl, "Old Armenian");
    public static final LanguageType xcl3 = LanguageType.addNonUniqueName(xcl, "Liturgical Armenian");
    // ------------ eo Armenian


    public static final LanguageType ibo = new LanguageType("ibo","Igbo", "Igbo");
    public static final LanguageType ig  = LanguageType.addNonUniqueCode(ibo, "ig");

    // id_ (instead of "id") because of the problems with names in SQLite
    public static final LanguageType id_ = new LanguageType("id_", "Bahasa Indonesia", "Indonesian");
    public static final LanguageType id  = LanguageType.addNonUniqueCode(id_, "id");
    public static final LanguageType ind = LanguageType.addNonUniqueCode(id_, "ind");

    public static final LanguageType ilo = new LanguageType("ilo", "Ilokano", "Ilokano");
    public static final LanguageType ilo2 = LanguageType.addNonUniqueName(ilo, "Ilocano");

    public static final LanguageType ina = new LanguageType("ina", "Interlingua", "Interlingua");
    public static final LanguageType ia  = LanguageType.addNonUniqueCode(ina, "ia");

    public static final LanguageType is = new LanguageType("is", "ГЌslenska", "Icelandic");
    public static final LanguageType isl = LanguageType.addNonUniqueCode(is, "isl");

    public static final LanguageType iu = new LanguageType("iu", "бђѓб“„б’ѓб‘Ћб‘ђб‘¦", "Inuktitut");
    public static final LanguageType ike_cans = new LanguageType("ike-cans", "бђѓб“„б’ѓб‘Ћб‘ђб‘¦", "Inuktitut, Eastern Canadian");
    public static final LanguageType ike_latn = new LanguageType("ike-latn", "inuktitut", "Inuktitut, Eastern Canadian (Latin)");// Latin script
    
    public static final LanguageType ja = new LanguageType("ja", "Japanese", "Japanese");
    public static final LanguageType jpn = LanguageType.addNonUniqueCode(ja, "jpn");
    public static final LanguageType ojp = LanguageType.addNonUnique(ja, "ojp", "Old Japanese");

    public static final LanguageType ka = new LanguageType("ka", "Georgian", "Georgian");
    public static final LanguageType kat = LanguageType.addNonUniqueCode(ka, "kat");

    public static final LanguageType kal    = new LanguageType("kal", "Kalaallisut", "Greenlandic");
    public static final LanguageType kl     = LanguageType.addNonUniqueCode(kal, "kl");// add lang names? : Inuktitut, Greenlandic/Kalaallisut as addNonUniqueName(kal, "");


    // Kazakh ------------
    public static final LanguageType kaz    = new LanguageType("kaz", "ТљР°Р·Р°Т›С€Р°", "Kazakh");
    public static final LanguageType kk     = LanguageType.addNonUniqueCode(kaz, "kk");

    public static final LanguageType kk_arab = LanguageType.addNonUniqueCode(kaz, "kk-arab");// Kazakh Arabic
    public static final LanguageType kk_cyrl = LanguageType.addNonUniqueCode(kaz, "kk-cyrl");// Kazakh Cyrillic
    public static final LanguageType kk_latn = LanguageType.addNonUniqueCode(kaz, "kk-latn");// Kazakh Latin

    public static final LanguageType kk_cn   = LanguageType.addNonUniqueCode(kaz, "kk-cn");// Kazakh (China)
    public static final LanguageType kk_kz   = LanguageType.addNonUniqueCode(kaz, "kk-kz");// Kazakh (Kazakhstan)
    public static final LanguageType kk_tr   = LanguageType.addNonUniqueCode(kaz, "kk-tr");// Kazakh (Turkey)

                                     // Russian Wiktionary
    public static final LanguageType kk_arab2 = LanguageType.addNonUniqueCode(kaz, "kk.arab");// Kazakh Arabic
    public static final LanguageType kk_cyr = LanguageType.addNonUniqueCode(kaz, "kk.cyr");// Kazakh Cyrillic
    public static final LanguageType kk_lat = LanguageType.addNonUniqueCode(kaz, "kk.lat");// Kazakh Latin
    // ------------ eo Kazakh


    public static final LanguageType ki = new LanguageType("ki", "Gĩkũyũ", "Gikuyu");
    public static final LanguageType kik = LanguageType.addNonUnique(ki, "kik", "Kikuyu");

    public static final LanguageType knw    = new LanguageType("knw", "!Kung", "!Kung");
    public static final LanguageType knw2   = LanguageType.addNonUniqueName(knw, "Kung-Ekoka");
    public static final LanguageType oun    = new LanguageType("oun", "ǃOǃung", "!O!ung");
    public static final LanguageType mwj    = new LanguageType("mwj", "Maligo", "Maligo");

    public static final LanguageType ko = new LanguageType("ko", "Korean", "Korean");
    public static final LanguageType kor = LanguageType.addNonUniqueCode(ko, "kor");
    public static final LanguageType oko = LanguageType.addNonUnique(ko, "oko", "Old Korean");
    public static final LanguageType okm = LanguageType.addNonUnique(ko, "okm", "Middle Korean");

    public static final LanguageType kom    = new LanguageType("kom", "Komi", "Komi");
    public static final LanguageType koi    = LanguageType.addNonUnique(kom, "koi", "Komi-Permyak");
    public static final LanguageType kv     = LanguageType.addNonUnique(kom, "kv", "Komi-Zyrian");// cyrillic is common script but also written in latin script
    public static final LanguageType kpv    = LanguageType.addNonUniqueCode(kom, "kpv");
    
    public static final LanguageType ks = new LanguageType("ks", "Kashmiri", "Kashmiri");
    public static final LanguageType kas = LanguageType.addNonUniqueCode(ks, "kas");

    
    // Kurdish ------------
    public static final LanguageType ku = new LanguageType("ku", "Kurdish", "Kurdish");
    public static final LanguageType kur = LanguageType.addNonUniqueCode(ku, "kur");
    
    public static final LanguageType ckb = LanguageType.addNonUnique(ku, "ckb", "Soranî");
    public static final LanguageType ckb2 = LanguageType.addNonUniqueName(ku, "Central Kurdish");

    public static final LanguageType sdh = LanguageType.addNonUnique(ku, "sdh", "Southern Kurdish");

    public static final LanguageType kmr = LanguageType.addNonUnique(ku, "kmr", "Kurmanji");
    public static final LanguageType kmr2 = LanguageType.addNonUniqueName(ku, "Northern Kurdish");
    
    public static final LanguageType ku_latn = LanguageType.addNonUniqueCode(ku, "ku-latn");// "Northern Kurdish Latin script"
    public static final LanguageType ku_arab = LanguageType.addNonUniqueCode(ku, "ku-arab");// "Northern Kurdish Arabic script"

                                     // Russian Wiktionary
    public static final LanguageType ku_cyr = LanguageType.addNonUniqueCode(ku, "ku.cyr");// Kurdish Cyrillic
    public static final LanguageType ku_lat2 = LanguageType.addNonUniqueCode(ku, "ku.lat");// Kurdish Latin script
    public static final LanguageType ku_arab2 = LanguageType.addNonUniqueCode(ku, "ku.arab");// Northern Kurdish Arabic script
    // ------------ eo Kurdish

    
    public static final LanguageType ky = new LanguageType("ky", "Kyrgyz", "Kyrgyz");
    public static final LanguageType kir = LanguageType.addNonUnique(ky, "kir", "Kirghiz");

    public static final LanguageType lg = new LanguageType("lg", "Luganda", "Luganda");
    public static final LanguageType lug = LanguageType.addNonUnique(lg, "lug", "Ganda");

    public static final LanguageType li = new LanguageType("li", "Limburgs", "Limburgish");
    public static final LanguageType lim = LanguageType.addNonUnique(li, "lim", "Limburgian");
    public static final LanguageType li2   = LanguageType.addNonUniqueName(li, "Limburgic");
    
    public static final LanguageType lt = new LanguageType("lt", "LietuviЕі", "Lithuanian");
    public static final LanguageType lit = LanguageType.addNonUniqueCode(lt, "lit");

    public static final LanguageType ltg    = new LanguageType("ltg", "Latgalian", "Latgalian");
    public static final LanguageType bat_ltg = LanguageType.addNonUniqueCode( ltg, "bat-ltg");

    public static final LanguageType luo = new LanguageType("luo", "Dholuo", "Dholuo");
    public static final LanguageType luo2   = LanguageType.addNonUniqueName(luo, "Luo");
    
    public static final LanguageType lv = new LanguageType("lv", "LatvieЕЎu", "Latvian");
    public static final LanguageType lav = LanguageType.addNonUniqueCode(lv, "lav");


    // Mandingo ------------
    public static final LanguageType man = new LanguageType("man", "Mandingo", "Mandingo");
    public static final LanguageType man_arab = LanguageType.addNonUniqueCode(man, "man.arab");// in Russian Wiktionary
    public static final LanguageType man_lat = LanguageType.addNonUniqueCode(man, "man.lat");// in Russian Wiktionary

    public static final LanguageType mnk = new LanguageType("mnk", "Mandinka", "Mandinka");
    // ------------ eo Mandingo
    
    
    public static final LanguageType mr = new LanguageType("mr", "Marathi", "Marathi");
    public static final LanguageType mar = LanguageType.addNonUniqueCode(mr, "mar");

    public static final LanguageType mn = new LanguageType("mn", "Mongolian", "Mongolian");
    public static final LanguageType khk = new LanguageType("khk", "Khalkha Mongolian", "Khalkha Mongolian");// Halh Mongolian (Cyrillic) (ISO 639-3: khk)
    public static final LanguageType xal = new LanguageType("xal", "Kalmyk", "Kalmyk");

    public static final LanguageType mi = new LanguageType("mi", "MДЃori", "Maori");
    public static final LanguageType mri = LanguageType.addNonUnique(mi, "mri", "Māori");

    public static final LanguageType ml = new LanguageType("ml", "Malayāḷam", "Malayalam");
    public static final LanguageType mal = LanguageType.addNonUniqueCode(ml, "mal");

    public static final LanguageType mrv = new LanguageType("mrv", "Mangareva", "Mangareva");
    public static final LanguageType mrv2   = LanguageType.addNonUniqueName(mrv, "Mangarevan");

    public static final LanguageType my = new LanguageType("my", "Myanmasa", "Burmese");
    public static final LanguageType mya = LanguageType.addNonUniqueCode(my, "mya");

    public static final LanguageType nah = new LanguageType("nah", "Nāhuatl", "Nahuatl");
    public static final LanguageType nci = LanguageType.addNonUnique(nah, "nci", "Classical Nahuatl");

    public static final LanguageType ne = new LanguageType("ne", "Nepālī", "Nepali");
    public static final LanguageType nep = LanguageType.addNonUnique(ne, "nep", "Nepalese");

    public static final LanguageType nv = new LanguageType("nv", "Diné bizaad", "Navajo");
    public static final LanguageType nav = LanguageType.addNonUniqueCode(nv, "nav");
    
    public static final LanguageType new_ = new LanguageType("new", "Newari", "Newari");
    public static final LanguageType new2 = LanguageType.addNonUniqueName(new_, "Nepal Bhasa");
    public static final LanguageType new3 = LanguageType.addNonUniqueName(new_, "Newah Bhaye");

    public static final LanguageType nl = new LanguageType("nl", "Nederlands", "Dutch");
    public static final LanguageType nld = LanguageType.addNonUniqueCode(nl, "nld");


    // Norwegian ------------
    public static final LanguageType no = new LanguageType("no", "Norwegian", "Norwegian");
    public static final LanguageType nor = LanguageType.addNonUniqueCode(no, "nor");
    
    public static final LanguageType nn = new LanguageType("nn", "Norwegian Nynorsk", "Norwegian Nynorsk");
    public static final LanguageType nno = LanguageType.addNonUnique(nn, "nno", "Nynorsk");
    
    public static final LanguageType nb = new LanguageType("nb", "Bokmål", "Bokmål");
    public static final LanguageType nob = LanguageType.addNonUnique(nb, "nob", "Norwegian Bokmål");
    // ------------ eo Norwegian


    public static final LanguageType nrm = new LanguageType("nrm", "Narom", "Narom");
    public static final LanguageType nrm2 = LanguageType.addNonUniqueName(nrm, "Narum");

    public static final LanguageType oc = new LanguageType("oc", "Occitan", "Occitan");
    public static final LanguageType oci = LanguageType.addNonUniqueCode(oc, "oci");

    
    // Ojibwe ------------
    public static final LanguageType oj = new LanguageType("oj", "Ojibwe", "Ojibwe");
    public static final LanguageType oji = LanguageType.addNonUniqueCode(oj, "oji");

    public static final LanguageType ciw = LanguageType.addNonUnique(oj, "ciw", "Chippewa");
    public static final LanguageType otw = LanguageType.addNonUnique(oj, "otw", "Ottawa");

    public static final LanguageType alq = new LanguageType("alq", "Algonquin", "Algonquin");
    public static final LanguageType alg = LanguageType.addNonUniqueCode(alq, "alg");// old: alg ISO 639-2
    // not yet in English Wiktionary:
    //
    // ojs – Severn Ojibwa
    // ojg – Eastern Ojibwa
    // ojc – Central Ojibwa
    // ojb – Northwestern Ojibwa
    // ojw – Western Ojibwa
    // ------------ eo Ojibwe


    public static final LanguageType os = new LanguageType("os", "Иронау", "Ossetian");
    public static final LanguageType os2 = LanguageType.addNonUniqueName(os, "Ossetic");


    // Punjabi ------------
    public static final LanguageType pa = new LanguageType("pa", "аЁЄа©°аЁњаЁѕаЁ¬а©Ђ", "Punjabi");
    public static final LanguageType pa2 = LanguageType.addNonUniqueName(pa, "Panjabi");
    
    public static final LanguageType lah = new LanguageType("lah", "Lahnda", "Lahnda");

    // not yet in English Wiktionary:
    // * pnb – Western Panjabi
    // * pmu – Mirpur Punjabi
    // ------------ eo Punjabi
    

    public static final LanguageType pam = new LanguageType("pam", "Kapampangan", "Kapampangan");
    public static final LanguageType pam2 = LanguageType.addNonUniqueName(pam, "Pampanga");

    public static final LanguageType pcm    = new LanguageType("pcm", "Nigerian Pidgin", "Nigerian Pidgin");
    public static final LanguageType pcm2   = LanguageType.addNonUniqueName(pcm, "Naija");

    public static final LanguageType ro = new LanguageType("ro", "RomГўnДѓ", "Romanian");
    public static final LanguageType ron = LanguageType.addNonUniqueCode(ro, "ron");


    // Creole ------------
    public static final LanguageType rop = new LanguageType("rop", "Kriol", "Kriol");
    public static final LanguageType rop2 = LanguageType.addNonUniqueName(rop, "Australian Kriol");

    public static final LanguageType brc = new LanguageType("brc", "Berbice Creole Dutch", "Berbice Creole Dutch");
    public static final LanguageType brc2 = LanguageType.addNonUniqueName(brc, "Berbice Dutch Creole");

    public static final LanguageType ht = new LanguageType("ht", "Haitian Creole", "Haitian Creole");
    public static final LanguageType hat = LanguageType.addNonUniqueCode(ht, "hat");

    public static final LanguageType jam = new LanguageType("jam", "Jamaican Creole", "Jamaican Creole");
    public static final LanguageType jam2 = LanguageType.addNonUniqueName(jam, "Jamaican Patois");
    public static final LanguageType jam3 = LanguageType.addNonUniqueName(jam, "Patois");
    public static final LanguageType jam4 = LanguageType.addNonUniqueName(jam, "Jamaican");

    public static final LanguageType tcs = new LanguageType("tcs", "Torres Strait Creole", "Torres Strait Creole");
    // not yet in English Wiktionary:
    //
    // * Belize Kriol, bzj - Belizean Creole, Belizean Creole English
    // * Guinea-Bissau Creole pov
    // ------------ eo Creole


    public static final LanguageType rup = new LanguageType("rup", "ArmГЈneashce", "Aromanian");
    public static final LanguageType roa_rup = LanguageType.addNonUniqueCode(rup, "roa-rup");

    public static final LanguageType run = new LanguageType("run", "Kirundi", "Kirundi");
    public static final LanguageType rn  = LanguageType.addNonUnique(run, "rn", "Rundi");
    
    public static final LanguageType ryn = new LanguageType("ryn", "Northern Amami", "Northern Amami");
    public static final LanguageType ams = new LanguageType("ams", "Southern Amami", "Southern Amami");
    public static final LanguageType ams2 = LanguageType.addNonUniqueName(pcm, "Southern Amami-Oshima");

    public static final LanguageType sah = new LanguageType("sah", "Sakha", "Sakha");
    public static final LanguageType sah2 = LanguageType.addNonUniqueName(sah, "Yakut");

    public static final LanguageType sk = new LanguageType("sk", "SlovenДЌina", "Slovak");
    public static final LanguageType slk = LanguageType.addNonUniqueCode(sk, "slk");

    public static final LanguageType sl = new LanguageType("sl", "Slovenščina", "Slovene");
    public static final LanguageType slv = LanguageType.addNonUnique(sl, "slv", "Slovenian");

    public static final LanguageType slovio = new LanguageType("slovio", "Slovio", "Slovio");// ruwikt
    public static final LanguageType slovio_la = LanguageType.addNonUniqueCode(slovio, "slovio-la");// the longest language code in ruwikt
    public static final LanguageType slovio_c = LanguageType.addNonUnique(slovio, "slovio-c", "Slovio (Cyrillic)");// ruwikt
    public static final LanguageType slovio_l = LanguageType.addNonUnique(slovio, "slovio-l", "Slovio (Latin)");// ruwikt

    public static final LanguageType smo    = new LanguageType("smo", "Gagana Samoa", "Samoan");
    public static final LanguageType sm     = LanguageType.addNonUniqueCode(smo, "sm");

    
    // Albanian ------------
    public static final LanguageType sqi = new LanguageType("sqi", "Shqip", "Albanian");
    public static final LanguageType sq  = LanguageType.addNonUniqueCode(sqi, "sq");

    public static final LanguageType aln  = new LanguageType("aln", "Gege", "Gheg");
    public static final LanguageType aln2 = LanguageType.addNonUniqueName(aln, "Gheg Albanian");

    public static final LanguageType aae  = new LanguageType("aae", "Arbëreshë Albanian", "Arbëreshë");
    public static final LanguageType aae2 = LanguageType.addNonUniqueName(aae, "Arbëreshë Albanian");

    public static final LanguageType aat  = new LanguageType("aat", "Arvanitika Albanian", "Arvanitika");
    public static final LanguageType aat2 = LanguageType.addNonUniqueName(aat, "Arvanitika Albanian");

    public static final LanguageType als = new LanguageType("als", "Tosk", "Tosk");
    public static final LanguageType als2 = LanguageType.addNonUniqueName(als, "Tosk Albanian");
    // ------------ eo Albanian


    public static final LanguageType sr = new LanguageType("sr", "Srpski", "Serbian");
    public static final LanguageType srp = LanguageType.addNonUniqueCode(sr, "srp");
    public static final LanguageType sr_c = LanguageType.addNonUnique(sr, "sr-c", "Serbian (Cyrillic)");// Russian Wiktionary
    public static final LanguageType sr_l = LanguageType.addNonUnique(sr, "sr-l", "Serbian (Latin)");// Russian Wiktionary

    public static final LanguageType tet    = new LanguageType("tet", "Tetun", "Tetun");
    public static final LanguageType tet2   = LanguageType.addNonUniqueName(tet, "Tetum");

    public static final LanguageType tgl    = new LanguageType("tgl", "Tagalog", "Tagalog");
    public static final LanguageType tl     = LanguageType.addNonUniqueCode(tgl, "tl");
    public static final LanguageType fil    = new LanguageType("fil", "Filipino", "Filipino");

    public static final LanguageType tix    = new LanguageType("tix", "Southern Tiwa", "Southern Tiwa");
    public static final LanguageType tix2   = LanguageType.addNonUniqueName(tix, "Tiwa");
    public static final LanguageType twf    = new LanguageType("twf", "Taos", "Taos");
    public static final LanguageType twf2   = LanguageType.addNonUniqueName(twf, "Northern Tiwa");
    
    public static final LanguageType tgk    = new LanguageType("tgk", "Tajik", "Tajik");
    public static final LanguageType tg     = LanguageType.addNonUnique(tgk, "tg", "Tajiki");
    public static final LanguageType tg_cyrl = LanguageType.addNonUniqueName(tgk, "Tajiki (Cyrllic script)");
    public static final LanguageType tg_latn = LanguageType.addNonUniqueName(tgk, "Tajiki (Latin script)");

    public static final LanguageType tir    = new LanguageType("tir", "б‰µбЊЌб€­бЉ›", "Tigrinya");
    public static final LanguageType ti     = LanguageType.addNonUniqueCode(tir, "ti");

    public static final LanguageType tt = new LanguageType("tt", "Татарча", "Tatar");
    public static final LanguageType tat = LanguageType.addNonUniqueCode(tt, "tat");
    public static final LanguageType tt_cyr = LanguageType.addNonUnique(tt, "tt.cyr", "Tatar (Cyrillic)");
    public static final LanguageType tt_lat = LanguageType.addNonUnique(tt, "tt.lat", "Tatar (Latin)");

    public static final LanguageType tk = new LanguageType("tk", "Türkmençe", "Turkmen");
    public static final LanguageType tuk = LanguageType.addNonUniqueCode(tk, "tuk");

    public static final LanguageType tokipona = new LanguageType("tokipona", "Toki Pona", "Toki Pona");
    public static final LanguageType art = LanguageType.addNonUniqueCode(tokipona, "art");// ruwikt

    public static final LanguageType tso    = new LanguageType("tso", "Xitsonga", "Tsonga");
    public static final LanguageType ts     = LanguageType.addNonUniqueCode(tso, "ts");

    public static final LanguageType tsn = new LanguageType("tsn", "Setswana", "Tswana");
    public static final LanguageType tn       = LanguageType.addNonUnique(tsn, "tn", "Setswana");
    public static final LanguageType tsn2 = LanguageType.addNonUniqueName(tsn, "Sitswana");

    public static final LanguageType tah    = new LanguageType("tah", "Reo MДЃ`ohi", "Tahitian");
    public static final LanguageType ty     = LanguageType.addNonUniqueCode(tah, "ty");

    public static final LanguageType th = new LanguageType("th", "Phasa Thai", "Thai");
    public static final LanguageType tha = LanguageType.addNonUniqueCode(th, "tha");
    
    public static final LanguageType tr = new LanguageType("tr", "TГјrkГ§e", "Turkish");
    public static final LanguageType tur = LanguageType.addNonUniqueCode(tr, "tur");

    public static final LanguageType tzj    = new LanguageType("tzj", "Tz'utujil", "Tz'utujil");
    public static final LanguageType tzt    = LanguageType.addNonUniqueCode(tzj, "tzt");

    public static final LanguageType uk = new LanguageType("uk", "Українська мова", "Ukrainian");
    public static final LanguageType ukr = LanguageType.addNonUniqueCode(uk, "ukr");


    // Utian ------------
    public static final LanguageType csm = new LanguageType("csm", "Central Sierra Miwok", "Central Sierra Miwok");

    public static final LanguageType css = new LanguageType("css", "Southern Ohlone", "Southern Ohlone");
    public static final LanguageType cst = new LanguageType("cst", "Northern Ohlone", "Northern Ohlone");
    // ------------ eo Utian


    public static final LanguageType uz = new LanguageType("uz", "Ўзбекча", "Uzbek");
    public static final LanguageType uzb = LanguageType.addNonUniqueCode(uz, "uzb");

    public static final LanguageType ve = new LanguageType("ve", "Venda", "Venda");
    public static final LanguageType ven = LanguageType.addNonUnique(ve, "ven", "Tshivenda");
    public static final LanguageType ve2 = LanguageType.addNonUniqueName(ve, "Luvenda");

    public static final LanguageType vi = new LanguageType("vi", "Tiếng Việt", "Vietnamese");
    public static final LanguageType vie = LanguageType.addNonUniqueCode(vi, "vie");


    // Visayan ------------
    public static final LanguageType akl = new LanguageType("akl", "Aklanon", "Aklanon");
    public static final LanguageType ceb = new LanguageType("ceb", "Cebuano", "Cebuano");
    public static final LanguageType hil = new LanguageType("hil", "Ilonggo", "Hiligaynon");
    public static final LanguageType krj = new LanguageType("krj", "Kinaray-a", "Kinaray-a");
    public static final LanguageType tsg = new LanguageType("tsg", "Tausug", "Tausug");
    
    public static final LanguageType war = new LanguageType("war", "Winaray", "Waray");
    public static final LanguageType war2 = LanguageType.addNonUniqueName(war, "Waray-Waray");
    // not yet in English Wiktionary:
    // Ati 	ati
    // ------------ eo Visayan

    
    // Welsh ------------
    public static final LanguageType cy = new LanguageType("cy", "Cymraeg", "Welsh");
    public static final LanguageType cym = LanguageType.addNonUniqueCode(cy, "cym");
    public static final LanguageType wel = LanguageType.addNonUniqueCode(cy, "wel");

    public static final LanguageType owl = new LanguageType("owl", "Hen Gymraeg", "Old Welsh");

    public static final LanguageType wlm = new LanguageType("wlm", "Middle Welsh", "Middle Welsh");// cel
    // ------------ eo Welsh


    public static final LanguageType wo = new LanguageType("wo", "Wolof", "Wolof");
    public static final LanguageType wol = LanguageType.addNonUniqueCode(wo, "wol");

    public static final LanguageType wrh = new LanguageType("wrh", "Wiradhuri", "Wiradhuri");
    public static final LanguageType wrh2 = LanguageType.addNonUniqueName(wrh, "Wiradjuri");
    
    public static final LanguageType xho    = new LanguageType("xho", "isiXhosa", "Xhosa");
    public static final LanguageType xh     = LanguageType.addNonUniqueCode(xho, "xh");
    public static final LanguageType xho_хhosan = LanguageType.addNonUniqueName(xho, "Xhosan");
    
    public static final LanguageType xno = new LanguageType("xno", "Anglo-Norman", "Anglo-Norman");
    public static final LanguageType roa_nor = LanguageType.addNonUniqueCode(xno, "roa-nor");// Russian Wiktionary
    public static final LanguageType roa = LanguageType.addNonUnique(xno, "roa", "Jèrriais");

    public static final LanguageType xto    = new LanguageType("xto", "Tocharian",  "Tocharian");
    public static final LanguageType xto2   = LanguageType.addNonUniqueName(xto,    "Tocharian A");
    public static final LanguageType txb    = LanguageType.addNonUnique(xto, "txb", "Tocharian B");

    public static final LanguageType yi = new LanguageType("yi", "Yiddish", "Yiddish");
    public static final LanguageType yid = LanguageType.addNonUniqueCode(yi, "yid");
    public static final LanguageType ydd = LanguageType.addNonUnique(yi, "ydd", "Eastern Yiddish");

    public static final LanguageType yo = new LanguageType("yo", "Yorùbá", "Yoruba");
    public static final LanguageType yor = LanguageType.addNonUniqueCode(yo, "yor");
    

    // Chinese ------------
    public static final LanguageType zh = new LanguageType("zh", "Chinese", "Chinese");
    public static final LanguageType cmn = new LanguageType("cmn", "Mandarin", "Mandarin");

    public static final LanguageType czh = LanguageType.addNonUnique(zh, "czh", "Huizhou");

    public static final LanguageType cjy = new LanguageType("cjy", "Jinyu", "Jinyu");
    public static final LanguageType cjy2 = LanguageType.addNonUniqueName(cjy, "Jin Chinese");
    public static final LanguageType cjy3 = LanguageType.addNonUniqueName(cjy, "Jin-yu");
    
    public static final LanguageType ltc = new LanguageType("ltc", "Middle Chinese", "Middle Chinese");
    public static final LanguageType ltc2 = LanguageType.addNonUniqueName(ltc, "Ancient Chinese");
    public static final LanguageType ltc3 = LanguageType.addNonUniqueName(ltc, "Late Middle Chinese");

    public static final LanguageType wuu    = new LanguageType("wuu", "Wu", "Wu");

            // todo : enwikt:template:zh-ts -> trad. (zh-tw), simpl. (zh-cn)
    public static final LanguageType zh_tw = new LanguageType("zh-tw", "Traditional Chinese", "Traditional Chinese");
    public static final LanguageType zh_hant = LanguageType.addNonUniqueCode(zh_tw, "zh-hant");// Chinese written using the Traditional Chinese script

    public static final LanguageType zh_cn = new LanguageType("zh-cn", "Simplified Chinese", "Simplified Chinese");
    public static final LanguageType zh_hans = LanguageType.addNonUnique(zh_cn, "zh-hans", "Chinese (PRC)");// Chinese written using the Simplified Chinese script

    public static final LanguageType lzh = new LanguageType("lzh", "ж–‡иЁЂ", "Classical Chinese");
    public static final LanguageType och = LanguageType.addNonUnique(lzh, "och", "Old Chinese");
    public static final LanguageType zh_classical = LanguageType.addNonUnique(lzh, "zh-classical", "Literary Chinese");

    public static final LanguageType nan = new LanguageType("nan", "BГўn-lГўm-gГє", "Min Nan");// Min Nan, Minnan, or Min-nan, Southern Min
    public static final LanguageType zh_min_nan = LanguageType.addNonUniqueCode(nan, "zh-min-nan");
    public static final LanguageType zh_nan = LanguageType.addNonUniqueCode(nan, "zh-nan");

    public static final LanguageType czo = new LanguageType("czo", "Min Zhong", "Min Zhong");
    public static final LanguageType czo2 = LanguageType.addNonUniqueName(czo, "Central Min");

    public static final LanguageType yue = new LanguageType("yue", "зІµиЄћ", "Cantonese");
    public static final LanguageType zh_yue = LanguageType.addNonUniqueCode(yue, "zh-yue");
    // ------------ eo Chinese
    

    public static final LanguageType zu = new LanguageType("zu", "isiZulu", "Zulu");
    public static final LanguageType zul = LanguageType.addNonUniqueCode(zu, "zul");
    

    // Zazaki ------------
    public static final LanguageType zza = new LanguageType("zza", "Zazaki", "Zazaki");
    
    public static final LanguageType kiu = new LanguageType("kiu", "Kirmanjki", "Kirmanjki");
    public static final LanguageType kiu2 = LanguageType.addNonUniqueName(kiu, "Northern Zazaki");

    public static final LanguageType diq = new LanguageType("diq", "Dimli", "Dimli");
    public static final LanguageType diq2 = LanguageType.addNonUniqueName(diq, "Southern Zazaki");
    // ------------ eo Zazaki






    // Russian Wiktionary specific codes
    // 1. todo check errors:  has too long unknown language code
    // 2. todo error:  unknown language code
    
    public static final LanguageType letter_ru = new LanguageType("Буква", "Letter", "Letter");
    public static final LanguageType bagua  = new LanguageType("bagua", "Ba gua", "Ba gua");
    public static final LanguageType hanzi  = new LanguageType("hanzi", "Chinese character", "Chinese character");

    public static final LanguageType abq    = new LanguageType("abq", "Abaza", "Abaza");
    public static final LanguageType abs    = new LanguageType("abs", "abs", "Ambonese");
    public static final LanguageType ady    = new LanguageType("ady", "Adyghe", "Adyghe");
    
    public static final LanguageType agf    = new LanguageType("agf", "Arguni", "Arguni");

    public static final LanguageType aie    = new LanguageType("aie", "Amara", "Amara");

    public static final LanguageType aja    = new LanguageType("aja", "Aja (Sudan)", "Aja (Sudan)");
    public static final LanguageType ajg    = new LanguageType("ajg", "Aja (Benin)", "Aja (Benin)");

    public static final LanguageType ale    = new LanguageType("ale", "Aleut", "Aleut");
    public static final LanguageType alp    = new LanguageType("alp", "Alune", "Alune");
    
    public static final LanguageType ang    = new LanguageType("ang", "Anglo-Saxon", "Old English");
    public static final LanguageType oen    = LanguageType.addNonUnique(ang, "oen", "Anglo-Saxon");

    public static final LanguageType aqc    = new LanguageType("aqc", "Archi", "Archi");
    
    public static final LanguageType art_oou= new LanguageType("art-oou", "oou", "oou");

    public static final LanguageType asm = new LanguageType("asm", "Assamese", "Assamese");
    public static final LanguageType as = LanguageType.addNonUniqueCode(asm, "as");
    
    public static final LanguageType bdk    = new LanguageType("bdk", "Budukh", "Budukh");
    public static final LanguageType bib    = new LanguageType("bib", "Bissa", "Bissa");
    public static final LanguageType bph    = new LanguageType("bph", "Botlikh", "Botlikh");
    public static final LanguageType byn    = new LanguageType("byn", "Blin", "Blin");

    public static final LanguageType cel = new LanguageType("cel", "Tselinsky", "Tselinsky");// Целинский - in English?
    public static final LanguageType chg    = new LanguageType("chg", "Chagatai", "Chagatai");

    public static final LanguageType chm = new LanguageType("chm", "Mari", "Mari");
    public static final LanguageType mhr = new LanguageType("mhr", "РћР»С‹Рє РњР°СЂРёР№", "Eastern Mari");
    public static final LanguageType mrj = new LanguageType("mrj", "Western Mari", "Western Mari");

    public static final LanguageType cjs    = new LanguageType("cjs", "Shor", "Shor");
    public static final LanguageType ckt = new LanguageType("ckt", "Chukchi", "Chukchi");

    public static final LanguageType de_a   = new LanguageType("de-a", "de-a", "de-a");
    public static final LanguageType dlg    = new LanguageType("dlg", "Dolgan", "Dolgan");
    public static final LanguageType dng    = new LanguageType("dng", "Dungan", "Dungan");

    public static final LanguageType dum    = new LanguageType("dum", "Middle Dutch", "Middle Dutch");

    public static final LanguageType egy    = new LanguageType("egy", "Egyptian", "Egyptian");
    public static final LanguageType en_au  = new LanguageType("en-au", "Australian English", "Australian English");
    public static final LanguageType en_nz  = new LanguageType("en-nz", "New Zealand English", "New Zealand English");
    public static final LanguageType en_us  = new LanguageType("en-us", "American English", "American English");
    public static final LanguageType enm    = new LanguageType("enm", "Middle English", "Middle English");

    public static final LanguageType eve    = new LanguageType("eve", "Even", "Even");
    public static final LanguageType evn    = new LanguageType("evn", "Evenki", "Evenki");

    public static final LanguageType fic_drw = new LanguageType("fic-drw", "Drow (Dungeons & Dragons)", "Drow (Dungeons & Dragons)");// Russian Wiktionary
    public static final LanguageType fon    = new LanguageType("fon", "Fon", "Fon");

    public static final LanguageType fr_be  = new LanguageType("fr-be", "Belgian French", "Belgian French");
    public static final LanguageType fr_ch  = new LanguageType("fr-ch", "Swiss French", "Swiss French");

    public static final LanguageType gld    = new LanguageType("gld", "Nanai", "Nanai");
    public static final LanguageType gni    = new LanguageType("gni", "Gooniyandi", "Gooniyandi");

    public static final LanguageType ha_lat = new LanguageType("ha.lat", "Hausa (lat)", "Hausa (lat)");// ruwikt
    public static final LanguageType ha_arab = new LanguageType("ha.arab", "Hausa (arab)", "Hausa (arab)");// ruwikt
    public static final LanguageType hit    = new LanguageType("hit", "Hittite", "Hittite");

    public static final LanguageType ium    = new LanguageType("ium", "Iu Mien", "Iu Mien");
    public static final LanguageType itl    = new LanguageType("itl", "Itelmen", "Itelmen");
    public static final LanguageType izh    = new LanguageType("izh", "Ingrian", "Ingrian");

    public static final LanguageType jct    = new LanguageType("jct", "Krymchak", "Krymchak");
    
    public static final LanguageType kbd    = new LanguageType("kbd", "Kabardian", "Kabardian");
    public static final LanguageType kca    = new LanguageType("kca", "Khanty", "Khanty");
    public static final LanguageType kdr    = new LanguageType("kdr", "Karaim", "Karaim");
    public static final LanguageType ket    = new LanguageType("ket", "Ket", "Ket");

    public static final LanguageType kim    = new LanguageType("kim", "Tofa", "Tofa");
    public static final LanguageType kjh    = new LanguageType("kjh", "Khakas", "Khakas");
    public static final LanguageType kpy    = new LanguageType("kpy", "Koryak", "Koryak");
    public static final LanguageType krc    = new LanguageType("krc", "Karachay-Balkar", "Karachay-Balkar");
    public static final LanguageType krl    = new LanguageType("krl", "Karjalan kieli", "Karelian");
    public static final LanguageType kum    = new LanguageType("kum", "Kumyk", "Kumyk");

    public static final LanguageType liv    = new LanguageType("liv", "Livonian ", "Livonian");

    public static final LanguageType mаs    = new LanguageType("mas", "Maasai", "Maasai");
    public static final LanguageType mgm    = new LanguageType("mgm", "Mambae", "Mambae");

    public static final LanguageType mns    = new LanguageType("mns", "Mansi", "Mansi");
    public static final LanguageType mos    = new LanguageType("mos", "Mossi", "Mossi");
    
    public static final LanguageType nio    = new LanguageType("nio", "Nganasan", "Nganasan");
    public static final LanguageType niv    = new LanguageType("niv", "Nivkh", "Nivkh");
    public static final LanguageType nog = new LanguageType("nog", "Nogai", "Nogai");
    public static final LanguageType non    = new LanguageType("non", "Old Norse", "Old Norse");
    public static final LanguageType num    = new LanguageType("num", "Niuafo'ou", "Niuafo'ou");
    
    public static final LanguageType odt    = new LanguageType("odt", "Old Dutch", "Old Dutch");// ?
    public static final LanguageType orv    = new LanguageType("orv", "Old East Slavic", "Old East Slavic");

    public static final LanguageType pau    = new LanguageType("pau", "Palau", "Palau");
    public static final LanguageType PIE    = new LanguageType("PIE", "Proto-Indo-European", "Proto-Indo-European");
    public static final LanguageType pinyin = new LanguageType("pinyin", "Pinyin", "Pinyin");
    public static final LanguageType pmt    = new LanguageType("pmt", "Tuamotuan", "Tuamotuan");
    public static final LanguageType pox    = new LanguageType("pox", "Polabian", "Polabian");
    public static final LanguageType ppol   = new LanguageType("ppol", "Proto-Polynesian", "Proto-Polynesian");
    public static final LanguageType prg    = new LanguageType("prg", "Old Prussian", "Old Prussian");
    public static final LanguageType psl    = new LanguageType("psl", "Proto-Slavic", "Proto-Slavic");

    public static final LanguageType qya    = new LanguageType("qya", "Quenya", "Quenya");

    public static final LanguageType rmr    = new LanguageType("rmr", "Calo", "Calo");
    public static final LanguageType rom    = new LanguageType("rom", "Romani", "Romani");
    public static final LanguageType romaji = new LanguageType("romaji", "Romaji", "Romaji");
    public static final LanguageType ru_old = new LanguageType("ru-old", "Russian (before 1917)", "Russian (before 1917)");
    
    public static final LanguageType sel    = new LanguageType("sel", "Selkup", "Selkup");
    public static final LanguageType sjd    = new LanguageType("sjd", "Kildin Sami", "Kildin Sami");
    public static final LanguageType sjn    = new LanguageType("sjn", "Sindarin", "Sindarin");
    
    public static final LanguageType sms    = new LanguageType("sms", "Skolt Sami", "Skolt Sami");
    public static final LanguageType sot    = new LanguageType("sot", "Sesotho", "Sesotho");
    
    public static final LanguageType solresol = new LanguageType("solresol", "Solresol", "Solresol");
    public static final LanguageType sol = LanguageType.addNonUniqueCode(solresol, "sol");

    public static final LanguageType sux    = new LanguageType("sux", "Sumerian", "Sumerian");
    public static final LanguageType sva    = new LanguageType("sva", "Svan", "Svan");

    public static final LanguageType tab    = new LanguageType("tab", "Tabasaran", "Tabasaranagx");
    public static final LanguageType tkl    = new LanguageType("tkl", "Tokelau", "Tokelauan");
    public static final LanguageType tly    = new LanguageType("tly", "Talysh", "Tokelau");
    public static final LanguageType translingual_ru = new LanguageType("INT", "Translingual", "Translingual (INT)");

    public static final LanguageType ttt    = new LanguageType("ttt", "Tat", "Tat");

    public static final LanguageType uby    = new LanguageType("uby", "Ubykh", "Ubykh");
    public static final LanguageType udi    = new LanguageType("udi", "Udi", "Udi");
    public static final LanguageType ulc    = new LanguageType("ulc", "Ulch", "Ulch");
    public static final LanguageType ulk    = new LanguageType("ulk", "Meriam", "Meriam");

    public static final LanguageType vep    = new LanguageType("vep", "Veps", "Veps");
    public static final LanguageType vot    = new LanguageType("vot", "Votic", "Votic");

    public static final LanguageType vro    = new LanguageType("vro", "Võro", "Võro");
    public static final LanguageType fiu    = LanguageType.addNonUniqueCode(vro, "fiu");

    public static final LanguageType xrn    = new LanguageType("xrn", "Arin", "Arin");

    public static final LanguageType ykg    = new LanguageType("ykg", "Northern Yukaghir", "Northern Yukaghir");
    public static final LanguageType yux    = new LanguageType("yux", "Southern Yukaghir", "Southern dYukaghir");
    public static final LanguageType yrk    = new LanguageType("yrk", "Nenets", "Nenets");
    
    public static final LanguageType zko    = new LanguageType("zko", "Kott", "Kott");








    
    // manually added languages:
    public static final LanguageType aaa = new LanguageType("aaa", "Ghotuo", "Ghotuo");
    public static final LanguageType aab = new LanguageType("aab", "Arum-Tesu", "Arum-Tesu");
    public static final LanguageType aak = new LanguageType("aak", "Ankave", "Ankave");

    public static final LanguageType abe = new LanguageType("abe", "Abenaki", "Abenaki");
    public static final LanguageType abl = new LanguageType("abl", "Abung", "Abung");
    public static final LanguageType abm = new LanguageType("abm", "Abanyom", "Abanyom");

    public static final LanguageType ach = new LanguageType("ach", "Acholi", "Acholi");
    
    public static final LanguageType ade = new LanguageType("ade", "Adele", "Adele");
    public static final LanguageType adj = new LanguageType("adj", "Adioukrou", "Adioukrou");
    public static final LanguageType adt = new LanguageType("adt", "Adnyamathanha", "Adnyamathanha");
    
    public static final LanguageType agj = new LanguageType("agj", "Argobba", "Argobba");
    public static final LanguageType ahs = new LanguageType("ahs", "Ashe", "Ashe");
    public static final LanguageType aiw = new LanguageType("aiw", "Aari", "Aari");
    public static final LanguageType aji = new LanguageType("aji", "Ajië", "Ajië");

    public static final LanguageType ake = new LanguageType("ake", "Akawaio", "Akawaio");
    public static final LanguageType akg = new LanguageType("akg", "Anakalangu", "Anakalangu");
    public static final LanguageType akk = new LanguageType("akk", "Akkadian", "Akkadian");
    
    public static final LanguageType akz = new LanguageType("akz", "Alabama", "Alabama");
    public static final LanguageType alc = new LanguageType("alc", "Qawasqar", "Qawasqar");
    public static final LanguageType ali = new LanguageType("ali", "Amaimon", "Amaimon");
    public static final LanguageType alu = new LanguageType("alu", "'Are'are", "'Are'are");
    public static final LanguageType amk = new LanguageType("amk", "Ambai", "Ambai");
    public static final LanguageType amn = new LanguageType("amn", "Amanab", "Amanab");
    public static final LanguageType amt = new LanguageType("amt", "Amto", "Amto");
    public static final LanguageType amu = new LanguageType("amu", "Amuzgo", "Amuzgo");
    public static final LanguageType and = new LanguageType("and", "Ansus", "Ansus");
    public static final LanguageType ant = new LanguageType("ant", "Antakarinya", "Antakarinya");
    
    public static final LanguageType apj = new LanguageType("apj", "Jicarilla", "Jicarilla");
    public static final LanguageType apl = new LanguageType("apl", "Lipan", "Lipan");
    public static final LanguageType apm = new LanguageType("apm", "Chiricahua", "Chiricahua");
    public static final LanguageType apy = new LanguageType("apy", "Apalaí", "Apalaí");
    public static final LanguageType arn = new LanguageType("arn", "Mapudungun", "Mapudungun");
    public static final LanguageType arp = new LanguageType("arp", "Arapaho", "Arapaho");
    public static final LanguageType arw = new LanguageType("arw", "Arawak", "Arawak");
    public static final LanguageType ase = new LanguageType("ase", "American Sign Language", "American Sign Language");
    public static final LanguageType aty = new LanguageType("aty", "Aneityum", "Aneityum");
    public static final LanguageType awa = new LanguageType("awa", "Awadhi", "Awadhi");

    public static final LanguageType ba = new LanguageType("ba", "Р‘Р°С€ТЎРѕСЂС‚", "Bashkir");
    public static final LanguageType bat_smg = new LanguageType("bat-smg", "ЕЅemaitД—ЕЎka", "Samogitian");
    public static final LanguageType bdp = new LanguageType("bdp", "Bende", "Bende");
    public static final LanguageType bdy = new LanguageType("bdy", "Bandjalang", "Bandjalang");
    public static final LanguageType bej = new LanguageType("bej", "Beja", "Beja");
    public static final LanguageType bem = new LanguageType("bem", "Bemba", "Bemba");
    public static final LanguageType bew = new LanguageType("bew", "Betawi", "Betawi");
    public static final LanguageType bft = new LanguageType("bft", "Balti", "Balti");
    public static final LanguageType bgc = new LanguageType("bgc", "Haryanvi", "Haryanvi");
    public static final LanguageType bhw = new LanguageType("bhw", "Biak", "Biak");
    public static final LanguageType bi = new LanguageType("bi", "Bislama", "Bislama");
    public static final LanguageType bin = new LanguageType("bin", "Bini", "Bini");
    public static final LanguageType bjz = new LanguageType("bjz", "Baruga", "Baruga");
    public static final LanguageType bku = new LanguageType("bku", "Buhid", "Buhid");
    public static final LanguageType bla = new LanguageType("bla", "Blackfoot", "Blackfoot");
    public static final LanguageType bns = new LanguageType("bns", "Bundeli", "Bundeli");
    public static final LanguageType bot = new LanguageType("bot", "Bongo", "Bongo");
    public static final LanguageType bou = new LanguageType("bou", "Bondei", "Bondei");
    public static final LanguageType bpl = new LanguageType("bpl", "Broome Pearling Lugger Pidgin", "Broome Pearling Lugger Pidgin");
    public static final LanguageType bpy = new LanguageType("bpy", "Bishnupriya Manipuri", "Bishnupriya Manipuri");
    public static final LanguageType brh = new LanguageType("brh", "Brahui", "Brahui");
    public static final LanguageType brt = new LanguageType("brt", "Bitare", "Bitare");
    public static final LanguageType bsb = new LanguageType("bsb", "Brunei Bisaya", "Brunei Bisaya");
    public static final LanguageType bvb = new LanguageType("bvb", "Bube", "Bube");

    public static final LanguageType cab = new LanguageType("cab", "Garifuna", "Garifuna");
    public static final LanguageType cad = new LanguageType("cad", "Caddo", "Caddo");
    public static final LanguageType cbk_zam = new LanguageType("cbk-zam", "Chavacano de Zamboanga", "Zamboanga Chavacano");
    public static final LanguageType ccc    = new LanguageType("ccc", "Chamicuro", "Chamicuro");
    public static final LanguageType cdo = new LanguageType("cdo", "Min Dong", "Min Dong");
    public static final LanguageType cgg = new LanguageType("cgg", "Rukiga", "Rukiga");
    public static final LanguageType chb = new LanguageType("chb", "Chibcha", "Chibcha");
    public static final LanguageType chc = new LanguageType("chc", "Catawba", "Catawba");
    public static final LanguageType chl = new LanguageType("chl", "Cahuilla", "Cahuilla");
    public static final LanguageType chn = new LanguageType("chn", "Chinook Jargon", "Chinook Jargon");
    public static final LanguageType cho = new LanguageType("cho", "Choctaw", "Choctaw");
    public static final LanguageType chr = new LanguageType("chr", "бЏЈбЋібЋ©", "Cherokee");
    public static final LanguageType chy = new LanguageType("chy", "TsetsГЄhestГўhese", "Cheyenne");
    public static final LanguageType cic = new LanguageType("cic", "Chickasaw", "Chickasaw");
    public static final LanguageType com = new LanguageType("com", "Comanche", "Comanche");
    public static final LanguageType cop = new LanguageType("cop", "Coptic", "Coptic");
    public static final LanguageType ctg = new LanguageType("ctg", "Chittagonian", "Chittagonian");
    public static final LanguageType ctu = new LanguageType("ctu", "Chol", "Chol");
    public static final LanguageType cui = new LanguageType("cui", "Cuiba", "Cuiba");
    public static final LanguageType cwe = new LanguageType("cwe", "Kwere", "Kwere");

    public static final LanguageType dak = new LanguageType("dak", "Dakota", "Dakota");
    public static final LanguageType dav = new LanguageType("dav", "Taita", "Taita");
    public static final LanguageType dbj = new LanguageType("dbj", "Ida'an", "Ida'an");
    public static final LanguageType dbl = new LanguageType("dbl", "Dyirbal", "Dyirbal");

    public static final LanguageType dif    = new LanguageType("dif", "Dieri", "Dieri");
    
    public static final LanguageType duj    = new LanguageType("duj", "Datiwuy", "Datiwuy");

    public static final LanguageType en_gb  = new LanguageType("en-gb", "British English", "British English");
    public static final LanguageType fiu_vro = new LanguageType("fiu-vro", "VГµro", "VГµro");

    public static final LanguageType fkv    = new LanguageType("fkv", "Kven", "Kven");
    
    public static final LanguageType frm    = new LanguageType("frm", "Middle French", "Middle French"); 
    public static final LanguageType fro    = new LanguageType("fro", "Old French", "Old French");

    public static final LanguageType ga = new LanguageType("ga", "Gaeilge", "Irish");
    public static final LanguageType sga = new LanguageType("sga", "Old Irish", "Old Irish");

    public static final LanguageType got = new LanguageType("got", "Gothic", "Gothic");

    public static final LanguageType hmn    = new LanguageType("hmn", "Hmong", "Hmong");
    
    public static final LanguageType ith_lat = new LanguageType("ith.lat", "Ithkuil", "Ithkuil");

    public static final LanguageType km = new LanguageType("km", "Khmer", "Khmer");
    public static final LanguageType krh    = new LanguageType("krh", "Kurama", "Kurama");
    public static final LanguageType kok    = new LanguageType("kok", "Konkani", "Konkani");

    public static final LanguageType ksh_c_a = new LanguageType("ksh-c-a", "Ripoarisch c a", "Ripuarian c a");
    public static final LanguageType ksh_p_b = new LanguageType("ksh-p-b", "Ripoarisch p b", "Ripuarian p b");
    
    public static final LanguageType ksi    = new LanguageType("ksi", "Krisa", "Krisa");
    public static final LanguageType kyi    = new LanguageType("kyi", "Kiput", "Kiput");

    public static final LanguageType lb = new LanguageType("lb", "Lëtzebuergesch", "Luxembourgish");

    public static final LanguageType luy    = new LanguageType("luy", "Luhya", "Luhya");

    public static final LanguageType map_bms = new LanguageType("map-bms", "Basa Banyumasan", "Banyumasan");
    public static final LanguageType miq    = new LanguageType("miq", "Mískitu", "Miskito");
    public static final LanguageType mk = new LanguageType("mk", "Македонски", "Macedonian");
    public static final LanguageType mnc    = new LanguageType("mnc", "Manchu", "Manchu");
    public static final LanguageType moh = new LanguageType("moh", "Mohawk", "Mohawk");
    public static final LanguageType mwf    = new LanguageType("mwf", "Murrinh-Patha", "Murrinh-Patha");

    public static final LanguageType ood    = new LanguageType("ood", "O'odham", "O'odham");
    public static final LanguageType ota = new LanguageType("ota", "Ottoman Turkish", "Ottoman Turkish");

    public static final LanguageType pcd    = new LanguageType("pcd", "Picard", "Picard");
    public static final LanguageType pjt    = new LanguageType("pjt", "Pitjantjatjara", "Pitjantjatjara");// aus
    public static final LanguageType kdd    = new LanguageType("kdd", "Yankunytjatjara", "Yankunytjatjara");// aus
    public static final LanguageType pt_br  = new LanguageType("pt-br", "PortuguГЄs do Brasil", "Brazilian Portuguese");

    public static final LanguageType roa_tara = new LanguageType("roa-tara", "TarandГ­ne", "Tarantino");

    public static final LanguageType rm = new LanguageType("rm", "Rumantsch", "Raeto-Romance");
    public static final LanguageType roh = LanguageType.addNonUnique(rm, "roh", "Rumantsch");
    public static final LanguageType rm_Romansch = LanguageType.addNonUniqueName(rm, "Romansch");

    public static final LanguageType ru = new LanguageType("ru", "Русский", "Russian");

    public static final LanguageType ruq = new LanguageType("ruq", "VlДѓheЕџte", "Megleno-Romanian");
    // to delete if there are no one entry:
    public static final LanguageType ruq_cyrl = new LanguageType("ruq-cyrl", "Р’Р»Р°С…РµСЃС‚Рµ", "Megleno-Romanian (Cyrillic script)");
    public static final LanguageType ruq_grek = new LanguageType("ruq-grek", "О’О»О±ОµПѓП„Оµ", "Megleno-Romanian (Greek script)");
    public static final LanguageType ruq_latn = new LanguageType("ruq-latn", "VlДѓheЕџte", "Megleno-Romanian (Latin script)");

    public static final LanguageType rap    = new LanguageType("rap", "Rapa Nui", "Rapa Nui");
    public static final LanguageType rar    = new LanguageType("rar", "Rarotongan", "Rarotongan");
    public static final LanguageType rhg    = new LanguageType("rhg", "Rohingya", "Rohingya");
    public static final LanguageType ryu    = new LanguageType("ryu", "Okinawan", "Okinawan");
    
    public static final LanguageType sat    = new LanguageType("sat", "Santali", "Santali");
    public static final LanguageType seu    = new LanguageType("seu", "Serui-Laut", "Serui-Laut");
    public static final LanguageType shp = new LanguageType("shp", "Shipibo", "Shipibo");
    public static final LanguageType smn    = new LanguageType("smn", "Inari Sami", "Inari Sami");
    public static final LanguageType sw = new LanguageType("sw", "Kiswahili", "Swahili");
    
    public static final LanguageType tig    = new LanguageType("tig", "Tigre", "Tigre");

    public static final LanguageType ton    = new LanguageType("ton", "Tongan", "Tongan");
    public static final LanguageType tpc    = new LanguageType("tpc", "Tlapanec", "Tlapanec");// ?
    
    public static final LanguageType tvl    = new LanguageType("tvl", "Tuvalu", "Tuvalu");
    public static final LanguageType tvl2   = LanguageType.addNonUniqueName(tvl, "Tuvaluan");

    public static final LanguageType tlh    = new LanguageType("tlh", "tlhIngan-Hol", "Klingon");// - no interlanguage links
    public static final LanguageType tpn    = new LanguageType("tpn", "Tupinambá", "Tupinambá");

    public static final LanguageType udm = new LanguageType("udm", "Udmurt", "Udmurt");
    public static final LanguageType uga    = new LanguageType("uga", "Ugaritic", "Ugaritic");
    
    public static final LanguageType val    = new LanguageType("val", "Vehes", "Vehes");// ?
    public static final LanguageType vma    = new LanguageType("vma", "Martuthunira", "Martuthunira");

    public static final LanguageType vol = new LanguageType("vol", "VolapГјk", "Volapük");
    public static final LanguageType vo = LanguageType.addNonUnique(vol, "vo", "VolapГјk");

    public static final LanguageType wad    = new LanguageType("wad", "Wandamen", "Wandamen");

    public static final LanguageType wim = new LanguageType("wim", "Wik-Mungkan", "Wik-Mungkan");
    
    public static final LanguageType wyb = new LanguageType("wyb", "Wangaaybuwan-Ngiyambaa", "Wangaaybuwan-Ngiyambaa");

    public static final LanguageType xbc    = new LanguageType("xbc", "Bactrian", "Bactrian");
    public static final LanguageType xmk = new LanguageType("xmk", "Ancient Macedonian", "Ancient Macedonian");
    public static final LanguageType xsm = new LanguageType("xsm", "Kasem", "Kasem");
    public static final LanguageType xsr    = new LanguageType("xsr", "Sherpa", "Sherpa");
    public static final LanguageType xvn    = new LanguageType("xvn", "Vandalic", "Vandalic");

    public static final LanguageType yua    = new LanguageType("yua", "Yucatec Maya", "Yucatec Maya");

    public static final LanguageType zai    = new LanguageType("zai", "Isthmus Zapotec", "Isthmus Zapotec");
    public static final LanguageType ze = new LanguageType("ze", "Zeneize", "Zeneize");
    
    
    // automatically
    /** Vim commands to convert mediawiki/languages/Names.php to the following
     * lines:
     * skip: 0. e ++enc=utf8 (skip this step)
     * skip: 1. %s/#/\/\//g               PHP to Java comments*/
     // 1. %s/\s*#\s*/ '/               PHP comments to 3rd parameter
     /*
     * 2. code to underscore, e.g. bat-smg -> bat_smg (PHP to Java variable names)
     *    %s/\(\t'[^'-]\+\)-\([^'-]\+' => \)/\1_\2/g
     * (44 languages, exception: zh-min-nan, be-x-old) */
    // 3. %s/\t'\([^']\+\)' => ['"]\([^']\+\)['"],[ \t]*/    public static final LanguageType \1 = new LanguageType("\1", "\2");/
    
    public static final LanguageType ast = new LanguageType("ast", "Asturianu", "Asturian");
    public static final LanguageType avk = new LanguageType("avk", "Kotava", "Kotava");

    public static final LanguageType bcc = new LanguageType("bcc", "ШЁЩ„Щ€Ъ†ЫЊ Щ…Ъ©Ш±Ш§Щ†ЫЊ", "Southern Balochi");
    public static final LanguageType bqi = new LanguageType("bqi", "ШЁШ®ШЄЩЉШ§Ш±ЩЉ", "Bakthiari");
    public static final LanguageType bto = new LanguageType("bto", "Iriga Bicolano", "Iriga Bicolano/Rinconada Bikol");
    
    public static final LanguageType dsb = new LanguageType("dsb", "Dolnoserbski", "Lower Sorbian");
    public static final LanguageType dv = new LanguageType("dv", "Ю‹ЮЁЮ€Ю¬ЮЂЮЁЮ„Ю¦ЮђЮ°", "Dhivehi");
    public static final LanguageType dz = new LanguageType("dz", "аЅ‡аЅјаЅ„ај‹аЅЃ", "Bhutani");
    
    public static final LanguageType eml = new LanguageType("eml", "EmiliГ n e rumagnГІl", "Emiliano-Romagnolo / Sammarinese");
    public static final LanguageType en = new LanguageType("en", "English", "English");
    
    public static final LanguageType eo = new LanguageType("eo", "Esperanto", "Esperanto");
    public static final LanguageType es = new LanguageType("es", "EspaГ±ol", "Spanish");
    public static final LanguageType ext = new LanguageType("ext", "EstremeГ±u", "Extremaduran");

    public static final LanguageType ff = new LanguageType("ff", "Fulfulde", "Fulfulde, Maasina");
    public static final LanguageType fj = new LanguageType("fj", "Na Vosa Vakaviti", "Fijian");
    public static final LanguageType fr = new LanguageType("fr", "FranГ§ais", "French");
    public static final LanguageType frc = new LanguageType("frc", "FranГ§ais cadien", "Cajun French");
    public static final LanguageType frp = new LanguageType("frp", "Arpetan", "Franco-ProvenГ§al/Arpitan");
    public static final LanguageType fur = new LanguageType("fur", "Furlan", "Friulian");

    public static final LanguageType gag = new LanguageType("gag", "Gagauz", "Gagauz");
    public static final LanguageType gan = new LanguageType("gan", "иґ›иЄћ", "Gan");
    public static final LanguageType glk = new LanguageType("glk", "ЪЇЫЊЩ„Ъ©ЫЊ", "Gilaki");
    public static final LanguageType gu = new LanguageType("gu", "аЄ—а«ЃаЄњаЄ°аЄѕаЄ¤а«Ђ", "Gujarati");
    public static final LanguageType gv = new LanguageType("gv", "Gaelg", "Manx");
    public static final LanguageType ha = new LanguageType("ha", "Щ‡ЩЋЩ€ЩЏШіЩЋ", "Hausa");
    public static final LanguageType hak = new LanguageType("hak", "Hak-kГў-fa", "Hakka");
    public static final LanguageType haw = new LanguageType("haw", "Hawai`i", "Hawaiian");
    
    public static final LanguageType ho = new LanguageType("ho", "Hiri Motu", "Hiri Motu");
    public static final LanguageType hsb = new LanguageType("hsb", "Hornjoserbsce", "Upper Sorbian");
    public static final LanguageType hz = new LanguageType("hz", "Otsiherero", "Herero");
    
    public static final LanguageType ie = new LanguageType("ie", "Interlingue", "Interlingue (Occidental)");
    public static final LanguageType ii = new LanguageType("ii", "к†‡к‰™", "Sichuan Yi");
    public static final LanguageType ik = new LanguageType("ik", "IГ±upiak", "Inupiak");
    
    public static final LanguageType inh = new LanguageType("inh", "Р“Р†Р°Р»РіР†Р°Р№ ДћalДџaj", "Ingush");
    public static final LanguageType io = new LanguageType("io", "Ido", "Ido");
    public static final LanguageType it = new LanguageType("it", "Italiano", "Italian");
    
    public static final LanguageType jbo = new LanguageType("jbo", "Lojban", "Lojban");
    public static final LanguageType jut = new LanguageType("jut", "Jysk", "Jutish / Jutlandic");
    public static final LanguageType jv = new LanguageType("jv", "Basa Jawa", "Javanese");
    
    public static final LanguageType kaa = new LanguageType("kaa", "Qaraqalpaqsha", "Karakalpak");
    public static final LanguageType kab = new LanguageType("kab", "Taqbaylit", "Kabyle");
    public static final LanguageType kg = new LanguageType("kg", "Kongo", "Kongo");
    
    public static final LanguageType kj = new LanguageType("kj", "Kwanyama", "Kwanyama");
    public static final LanguageType kn = new LanguageType("kn", "аІ•аІЁаіЌаІЁаІЎ", "Kannada");
    
    public static final LanguageType kr = new LanguageType("kr", "Kanuri", "Kanuri, Central");
    public static final LanguageType kri = new LanguageType("kri", "Krio", "Krio");
    public static final LanguageType kw = new LanguageType("kw", "Kernewek", "Cornish");

    public static final LanguageType la = new LanguageType("la", "Latina", "Latin");
    public static final LanguageType lad = new LanguageType("lad", "Ladino", "Ladino");
    public static final LanguageType lbe = new LanguageType("lbe", "Р›Р°РєРєСѓ", "Lak");
    public static final LanguageType lez = new LanguageType("lez", "Р›РµР·РіРё", "Lezgi");
    public static final LanguageType lfn = new LanguageType("lfn", "Lingua Franca Nova", "Lingua Franca Nova");
    public static final LanguageType lij = new LanguageType("lij", "LГ­guru", "Ligurian");
    public static final LanguageType lld = new LanguageType("lld", "Ladin", "Ladin");
    public static final LanguageType lmo = new LanguageType("lmo", "Lumbaart", "Lombard");
    public static final LanguageType ln = new LanguageType("ln", "LingГЎla", "Lingala");
    public static final LanguageType lo = new LanguageType("lo", "аєҐаєІає§", "Laotian");
    public static final LanguageType loz = new LanguageType("loz", "Silozi", "Lozi");
    public static final LanguageType lzz = new LanguageType("lzz", "Lazuri Nena", "Laz");
    
    public static final LanguageType mdf = new LanguageType("mdf", "РњРѕРєС€РµРЅСЊ", "Moksha");
    public static final LanguageType mg = new LanguageType("mg", "Malagasy", "Malagasy");
    public static final LanguageType mh = new LanguageType("mh", "Ebon", "Marshallese");

    public static final LanguageType mo = new LanguageType("mo", "РњРѕР»РґРѕРІРµРЅСЏСЃРєСЌ", "Moldovan");
    public static final LanguageType ms = new LanguageType("ms", "Bahasa Melayu", "Malay");
    public static final LanguageType mt = new LanguageType("mt", "Malti", "Maltese");
    public static final LanguageType mus = new LanguageType("mus", "Mvskoke", "Muskogee/Creek");
    public static final LanguageType mwl = new LanguageType("mwl", "MirandГ©s", "Mirandese");
    public static final LanguageType myv = new LanguageType("myv", "Р­СЂР·СЏРЅСЊ", "Erzya");
    public static final LanguageType mzn = new LanguageType("mzn", "Щ…ЩЋШІЩђШ±Щ€Щ†ЩЉ", "Mazanderani");
    public static final LanguageType na = new LanguageType("na", "Dorerin Naoero", "Nauruan");
    
    public static final LanguageType nap = new LanguageType("nap", "Nnapulitano", "Neapolitan");
    public static final LanguageType ng = new LanguageType("ng", "Oshiwambo", "Ndonga");
    public static final LanguageType niu = new LanguageType("niu", "NiuД“", "Niuean");
    
    public static final LanguageType nov = new LanguageType("nov", "Novial", "Novial");
    public static final LanguageType nso = new LanguageType("nso", "Sesotho sa Leboa", "Northern Sotho");
    public static final LanguageType ny = new LanguageType("ny", "Chi-Chewa", "Chichewa");
    
    public static final LanguageType om = new LanguageType("om", "Oromoo", "Oromo");
    public static final LanguageType or = new LanguageType("or", "а¬“а­ња¬їа¬†", "Oriya");
    
    public static final LanguageType pag = new LanguageType("pag", "Pangasinan", "Pangasinan");

    public static final LanguageType pap = new LanguageType("pap", "Papiamentu", "Papiamentu");
    public static final LanguageType pi = new LanguageType("pi", "а¤Єа¤ѕа¤їа¤ґ", "Pali");
    public static final LanguageType pih = new LanguageType("pih", "Norfuk / Pitkern", "Norfuk/Pitcairn/Norfolk");
    public static final LanguageType pl = new LanguageType("pl", "Polski", "Polish");
    public static final LanguageType plm = new LanguageType("plm", "Palembang", "Palembang");
    public static final LanguageType pms = new LanguageType("pms", "PiemontГЁis", "Piedmontese");
    public static final LanguageType pnb = new LanguageType("pnb", "ЩѕЩ†Ш¬Ш§ШЁЫЊ", "Western Punjabi");
    public static final LanguageType ps = new LanguageType("ps", "ЩѕЪљШЄЩ€", "Pashto");// Northern/Paktu/Pakhtu/Pakhtoo/Afghan/Pakhto/Pashtu/Pushto/Yusufzai Pashto
    public static final LanguageType pt = new LanguageType("pt", "PortuguГЄs", "Portuguese");
    
    public static final LanguageType qu = new LanguageType("qu", "Runa Simi", "Quechua");
    public static final LanguageType rif = new LanguageType("rif", "Tarifit", "Tarifit");
    public static final LanguageType rmy = new LanguageType("rmy", "Romani", "Vlax Romany");
    public static final LanguageType rw = new LanguageType("rw", "Kinyarwanda", "Kinyarwanda");
    
    public static final LanguageType sa = new LanguageType("sa", "а¤ёа¤‚а¤ёаҐЌа¤•аҐѓа¤¤", "Sanskrit");
    public static final LanguageType sc = new LanguageType("sc", "Sardu", "Sardinian");
    public static final LanguageType scn = new LanguageType("scn", "Sicilianu", "Sicilian");
    public static final LanguageType sco = new LanguageType("sco", "Scots", "Scots");
    public static final LanguageType sd = new LanguageType("sd", "ШіЩ†ЪЊЩЉ", "Sindhi");
    public static final LanguageType sdc = new LanguageType("sdc", "Sassaresu", "Sassarese");
    public static final LanguageType se = new LanguageType("se", "SГЎmegiella", "Northern Sami");
    public static final LanguageType sei = new LanguageType("sei", "Cmique Itom", "Seri");
    public static final LanguageType sg = new LanguageType("sg", "SГ¤ngГ¶", "Sango/Sangho");
    public static final LanguageType sh = new LanguageType("sh", "Srpskohrvatski / РЎСЂРїСЃРєРѕС…СЂРІР°С‚СЃРєРё", "Serbo-Croatian");
    public static final LanguageType shi = new LanguageType("shi", "TaЕЎlбёҐiyt", "Tachelhit");
    public static final LanguageType si = new LanguageType("si", "а·ѓа·’а¶‚а·„а¶Ѕ", "Sinhalese");
    public static final LanguageType simple = new LanguageType("simple", "Simple English", "Simple English");
    public static final LanguageType sma = new LanguageType("sma", "Г…arjelsaemien", "Southern Sami");
    public static final LanguageType sn = new LanguageType("sn", "chiShona", "Shona");
    public static final LanguageType so = new LanguageType("so", "Soomaaliga", "Somali");
    
    public static final LanguageType srn = new LanguageType("srn", "Sranantongo", "Sranan Tongo");
    public static final LanguageType ss = new LanguageType("ss", "SiSwati", "Swati");
    public static final LanguageType st = new LanguageType("st", "Sesotho", "Southern Sotho");
    
    public static final LanguageType su = new LanguageType("su", "Basa Sunda", "Sundanese");
    public static final LanguageType sv = new LanguageType("sv", "Svenska", "Swedish");
    public static final LanguageType szl = new LanguageType("szl", "ЕљlЕЇnski", "Silesian");
    public static final LanguageType ta = new LanguageType("ta", "а®¤а®®а®їа®ґаЇЌ", "Tamil");
    public static final LanguageType tcy = new LanguageType("tcy", "аІ¤аіЃаІіаіЃ", "Tulu");
    public static final LanguageType te = new LanguageType("te", "а°¤а±†а°Іа±Ѓа°—а±Ѓ", "Telugu");
    
    public static final LanguageType to = new LanguageType("to", "faka-Tonga", "Tonga (Tonga Islands)");
    public static final LanguageType tpi = new LanguageType("tpi", "Tok Pisin", "Tok Pisin");
    public static final LanguageType tum = new LanguageType("tum", "chiTumbuka", "Tumbuka");
    public static final LanguageType tyv = new LanguageType("tyv", "РўС‹РІР° РґС‹Р»", "Tyvan");
    public static final LanguageType tzm = new LanguageType("tzm", "вµњвґ°вµЋвґ°вµЈвµ‰вµ–вµњ", "(Central Morocco) Tamazight");
    
    public static final LanguageType ug = new LanguageType("ug", "UyghurcheвЂЋ / Ш¦Ы‡ЩЉШєЫ‡Ш±Ъ†Ы•", "Uyghur");
    public static final LanguageType ur = new LanguageType("ur", "Ш§Ш±ШЇЩ€", "Urdu");

    public static final LanguageType vec = new LanguageType("vec", "VГЁneto", "Venetian");
    public static final LanguageType vls = new LanguageType("vls", "West-Vlams", "West Flemish");
    public static final LanguageType wa = new LanguageType("wa", "Walon", "Walloon");
    
    public static final LanguageType xmf = new LanguageType("xmf", "бѓ›бѓђбѓ бѓ’бѓђбѓљбѓЈбѓ бѓ", "Mingrelian");
    
    public static final LanguageType za = new LanguageType("za", "(Cuengh)", "Zhuang");
    public static final LanguageType zea = new LanguageType("zea", "ZeГЄuws", "Zeeuws/Zeaws");
}
