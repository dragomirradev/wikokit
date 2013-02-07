/* Label.java - contexual information for definitions, or Synonyms,
 *                     or Translations.
 * 
 * Copyright (c) 2013 Andrew Krizhanovsky <andrew.krizhanovsky at gmail.com>
 * Distributed under EPL/LGPL/GPL/AL/BSD multi-license.
 */
package wikokit.base.wikt.constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import wikokit.base.wikipedia.language.LanguageType;
import wikokit.base.wikt.multi.en.name.LabelEn;
import wikokit.base.wikt.multi.ru.LabelRu;

/** Contextual information for definitions, such as archaic, by analogy, 
 * chemistry, etc.
 * 
 * This contextual information is located in the sections: semantic relations 
 * and translations in Russian Wiktionary.
 * 
 * This class describes context labels of English Wiktionary. Context labels 
 * of other wiktionaries (e.g. Russian Wiktionary) are described in 
 * LabelRu, German Wikt in LabelDe (todo), French Wikt in LabelFr (todo).
 *
 * @see (Scheme of Label abstract classes hierarchy - Wiktionary parser) wikokit\wikt_parser\doc\screenshots\Label_abstract_classes_hierarchy_IMG_20130205.jpg  
 * @see http://en.wiktionary.org/wiki/Template_talk:context
 * @see http://en.wiktionary.org/wiki/Wiktionary:Entry_layout_explained
 * @see http://en.wiktionary.org/wiki/Template:context
 */
public abstract class Label {
    
    /** Label itself, e.g. 'устар.', 'п.'. */
    protected String short_name;
    
    /** Label name, e.g. 'устарелое', 'переносное значение'. */
    protected String name;
    
    protected final static Map<String, Label> short_name2label = new HashMap<String, Label>();
    protected final static Map<Label, String> label2short_name = new HashMap<Label, String>();
    
    protected final static Map<String, Label> name2label = new HashMap<String, Label>();
    protected final static Map<Label, String> label2name = new HashMap<Label, String>();
    
    /** If there are more than one context label (synonyms,  short name label): <synonymic_label, source_main_unique_label> */
    private static Map<String, Label> multiple_synonym2label = new HashMap<String, Label>();
    
    protected Label(String short_name, String name) {
    
        if(short_name.length() == 0 || name.length() == 0)
            System.out.println("Error in Label.Label(): one of parameters is empty! label="+short_name+"; name=\'"+name+"\'.");
        
        this.short_name  = short_name; 
        this.name       = name; 
    
    };
    
    protected void initLabel(Label label) {
    
        if(null == label)
            System.out.println("Error in Label.initLabel(): label is null, short_name="+short_name+"; name=\'"+name+"\'.");
        
        checksPrefixSuffixSpace(short_name);
        checksPrefixSuffixSpace(name);
        
        // check the uniqueness of the label short name and full name
        Label label_prev_by_short_name = short_name2label.get(short_name);
        Label label_prev_by_name       =       name2label.get(      name);
        
        if(null != label_prev_by_short_name)
            System.out.println("Error in Label.initLabel(): duplication of label (short name)! short name='"+short_name+
                    "' name='"+name+"'. Check the maps short_name2label and name2label.");

        if(null != label_prev_by_name)
            System.out.println("Error in Label.initLabel(): duplication of label (full name)! short_name='"+short_name+
                    "' name='"+name+ "'. Check the maps short_name2label and name2label.");
        
        short_name2label.put(short_name, label);
        label2short_name.put(label, short_name);
        
        name2label.put(name, label);
        label2name.put(label, name);
    };
    
    /** Checks whitespace characters in the prefix or suffix of a string.
     * Prints "error" message if there is any.
     */
    protected static void checksPrefixSuffixSpace(String s) {

        if(s.charAt(0) == ' ' || s.charAt(s.length()-1) == ' ')
            System.out.println("Error in Label.checksPrefixSuffixSpace(): there are leading spaces, string='"+s+"'.");
    }
    
    
    /** Gets English Wiktionary context label associated with this label. 
     * This function is needed for compatibility with LabelLocal.java (other child class of Label.java). */
    protected abstract Label getLinkedLabelEn();
    
    @Override
    public String toString() { return short_name; }
    
    /** Checks weather exists the Label (short name) by its name, checks synonyms also. */
    public static boolean hasShortName(String short_name) {
        return short_name2label.containsKey(short_name) || 
         multiple_synonym2label.containsKey(short_name);
    }
    
    /** Gets label itself (short name). */
    public String getShortName() {
        return short_name;
    }
    
    /** Gets label by short name of the label. */
    public static Label getByShortName(String short_name) throws NullPointerException
    {
        Label label;

        if(null != (label = short_name2label.get(short_name)))
            return  label;

        if(null != (label = multiple_synonym2label.get(short_name)))
            return  label;

        throw new NullPointerException("Null Label.getByShortName(), label short_name="+ short_name);
    }
    
    public static boolean hasName(String name) {
        return name2label.containsKey(name);
    }

    /** Gets label full name. */
    public String getName() {
        return name;
    }
    
    /** Gets (full) name of context label by label object.
     * 
     * It is supposed that (1) number of context labels in enwikt > in ruwikt,
     * (2) context labels in enwikt cover (include) labels in all other wiktionaries.
     * So, first 
     * 
     * @param label
     * @return 
     */
    /*public static String getName (Label label) {

        String s = label2name.get(label);
        if(null == s) // e.g. LabelRu don't has "dated_sense" label
            return label.getName(); // if there is no translation into local language, then English name

        return s;
    }*/
    
    
    /** Counts number of labels. */
    public static int size() {
        return short_name2label.size();
    }

    
    
    /** Adds synonymic context label for the main (source) label.
     * @param label source main unique label
     * @param synonymic_label synonym of label (short name)
     */
    public static Label addNonUniqueShortName(Label label, String synonymic_short_name) {

        checksPrefixSuffixSpace(synonymic_short_name);
        if(synonymic_short_name.length() > 255) {
            System.out.println("Error in Label.addNonUniqueShortName(): the synonymic label='"+synonymic_short_name+
                    "' is too long (.length() > 255)!");
            return null;
        }

        if(short_name2label.containsKey(synonymic_short_name)) {
            System.out.println("Error in Label.addNonUniqueShortName(): the synonymic label '"+synonymic_short_name+
                    "' is already presented in the map label2name!");
            return null;
        }
        
        if(multiple_synonym2label.containsKey(synonymic_short_name)) {
            System.out.println("Error in Label.addNonUniqueShortName(): the synonymic label '"+synonymic_short_name+
                    "' is already presented in the map multiple_synonym2label!");
            return null;
        }
        
        multiple_synonym2label.put(synonymic_short_name, label);
        return label;
    }
    
    
    
    /** The set of unknown labels, which were found during parsing.
     * It should be only one message for one unknown label (for concise logging).
     */
    private static Set<String> unknown_label = new HashSet<String>();

    /** Checks weather exists the unknown label 'label'. */
    public static boolean hasUnknownLabel(String label) {
        return unknown_label.contains(label);
    }

    /** Adds unknown language code 'code'. */
    public static boolean addUnknownLabel(String label) {
        return unknown_label.add(label);
    }
    
}
