/* TLabelCategory.java - SQL operations with the table 'label_category' 
 * in Wiktionary parsed database.
 *
 * Copyright (c) 2013 Andrew Krizhanovsky <andrew.krizhanovsky at gmail.com>
 * Distributed under EPL/LGPL/GPL/AL/BSD multi-license.
 */
package wikokit.base.wikt.sql.label;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import wikokit.base.wikipedia.sql.Connect;
import wikokit.base.wikipedia.sql.Statistics;
import wikokit.base.wikipedia.sql.UtilSQL;
import wikokit.base.wikt.constant.LabelCategory;

/** An operations with the table 'label_category' in MySQL Wiktionary_parsed database.
 * label_category - categories of context labels (templates).
 * 
 * This class defines also special types of context labels, e.g.
 * id == 0 - labels found by parser, else labels were entered by this software developers. 
 */
public class TLabelCategory {
    
    /** Unique identifier in the table 'label_category'. */
    private int id;
    
    /** Category of the label. */
    private LabelCategory label_category;
    
                /** Context label category name (i.e. type of context label). */
                //private String name;
    
    /** Parent category of context label category (parent_category_id), 
     * e.g. label category "Computing context labels‎" 
     * has parent category "Topical context labels‎". */
    // private LabelCategory parent_category;
    
    
    /** Map from label category to ID.*/
    private static Map<LabelCategory, Integer> category2id;
    
    /** Map from ID to label category.*/
    private static Map<Integer, LabelCategory> id2category;

    private final static TLabelCategory[] NULL_TLABEL_CATEGORY_ARRAY = new TLabelCategory[0];
    
    public TLabelCategory(int _id, LabelCategory _label_category) {
        id      = _id;
        label_category  = _label_category;
    }
    
    public static Map<LabelCategory, Integer> getMapCategory2ID() {
        return category2id;
    }
    
    //public static Map<Integer, LabelCategory> getMapID2Category() {
    //    return id2category;
    //}
    
    
    /** Gets label category ID from the table 'label_category'.<br><br>
     * 
     * REM: the function 'createFastMaps()' should be run at least once,
     * before this function execution.
     */
    public static int getIDFast(LabelCategory lc) {
        if(null == category2id || 0 == category2id.size()) {
            System.out.println("Error (wikt_parsed TLabelCategory.getIDFast()):: What about calling 'createFastMaps()' before?");
            return -1;
        }
        if(null == lc) {
            System.out.println("Error (wikt_parsed TLabelCategory.getIDFast()):: argument LanguageCategory is null");
            return -1;
        }
        
        Integer result = category2id.get(lc);
        if(null == result) {
            System.out.println("Warning (wikt_parsed TLabelCategory.getIDFast()):: map category2id don't have this id.");
            return -1;
        }
        return result;
    }
    
    /** Gets label category by ID from the table 'label_category'.<br><br>
     *
     * REM: the functions createFastMaps() should be run at least once,
     * before this function execution.
     */  
    public static LabelCategory getLabelCategoryFast(int id) {
        if(null == id2category) {
            System.out.println("Error (wikt_parsed TLabelCategory.getLabelCategoryFast()):: What about calling 'createFastMaps()' before?");
            return null;
        }
        if(id <= 0) {
            // System.out.println("Error (wikt_parsed TLabelCategory.getLabelCategoryFast()):: argument id <=0, id = "+id);
            return null;
        }
        return id2category.get(id);
    }
    
    /** Read all records from the table 'label_category',
     * fills the internal map from a table ID to a label category .<br><br>
     *
     * REM: during a creation of Wiktionary parsed database
     * the functions recreateTable() should be called (before this function).
     */
    public static void createFastMaps(Connect connect) {

        System.out.println("Loading table `label_category`...");

        int size = Statistics.Count(connect, "label_category");
        if(0==size) {
            System.out.println("Error (wikt_parsed TLabelCategory.createFastMaps()):: The table `label_category` is empty!");
            return;
        }
        
        if(null != category2id && category2id.size() > 0)
            category2id.clear();
        
        if(null != id2category && id2category.size() > 0)
            id2category.clear();
        
        category2id = new LinkedHashMap<>(size);
        id2category = new LinkedHashMap<>(size);
        
        Collection<LabelCategory> labs = LabelCategory.getAllLabelCats();
        for(LabelCategory lc : labs) {
            
            String name   = lc.getName();
            int id        = getIDByName(connect, name);
            
            if (0 == id) {
                System.out.println("Error (wikt_parsed TLabelCategory.createFastMaps()):: There is an empty label category name, check the table `label_category`!");
                continue;
            }
        
            category2id.put(lc, id);
            id2category.put(id, lc);
        }
        
        if(size != LabelCategory.size())
            System.out.println("Warning (wikt_parsed TLabelCategory.createFastMaps()):: LabelCategory.size (" + LabelCategory.size()
                    + ") is not equal to size of table 'label_category'("+ size +"). Is the database outdated?");
    }
    
    /** Deletes all records from the table 'label_category',
     * loads names of labels categories from LabelCategory.java,
     * sorts by name,
     * fills the table.
     */
    public static void recreateTable(Connect connect) {

        System.out.println("Recreating the table `label_category`...");
        Map<Integer, LabelCategory> _id2category = fillLocalMaps();
        UtilSQL.deleteAllRecordsResetAutoIncrement(connect, "label_category");
        fillDB(connect, _id2category);
        {
            int db_current_size = wikokit.base.wikipedia.sql.Statistics.Count(connect, "label_category");
            assert(db_current_size == LabelCategory.size()); // ~ NNN label categories entered by hand
        }
    }

    /** Load data from a LabelCategory class, sorts,
     * and fills the local map 'id2category'. */
    public static Map<Integer, LabelCategory> fillLocalMaps() {

        int size = LabelCategory.size();
        List<String>list_category = new ArrayList<>(size);
        list_category.addAll(LabelCategory.getAllNames());
        Collections.sort(list_category);

        // OK, we have list of category labels (names). Sorted list 'list_category'

        category2id = new LinkedHashMap<>(size);
        
        // Local map from id to LabelCategory. It is created from data in LabelCategory.java.
        // It is used to fill the table 'label' in right sequence.
        Map<Integer, LabelCategory> _id2category = new LinkedHashMap<>(size);
        for(int id=0; id<size; id++) {
            String s = list_category.get(id);           // s - name of category of labels
            assert(LabelCategory.hasName(s));           //System.out.println("fillLocalMaps---id="+id+"; s="+s);
            
            LabelCategory lc = LabelCategory.getByName(s);
            _id2category.put(id+1, lc); // +1 since MySQL autoincrement starts from 1, not zero
            category2id.put(lc, id+1);
        }
        return _id2category;
    }

    /** Fills database table 'label_category' by data from LabelCategory class. */
    public static void fillDB(Connect connect,Map<Integer, LabelCategory> id2category) {

        for(int id : id2category.keySet()) {
            LabelCategory lc = id2category.get(id);
            if(null == lc)
                continue;
            
            LabelCategory parent = lc.getParent();
            int parent_id;
            if(null == parent)
                parent_id = 0;
            else 
                parent_id = category2id.get(parent);
                    
            insert (connect, lc.getName(), parent_id);
        }
    }
    
    /** Inserts record into the table 'relation_type'.<br><br>
     * INSERT INTO relation_type (name) VALUES ("synonymy");
     * @param name  category label name
     * @param parent_category_id ID of parent category, id=0 corresponds to NULL in the database 
     */
    public static void insert (Connect connect,String name,int parent_category_id) {

        StringBuilder str_sql = new StringBuilder();
        try
        {
            Statement s = connect.conn.createStatement ();
            try {
                if(0 == parent_category_id) {
                    str_sql.append("INSERT INTO label_category (name) VALUES (\"");
                    str_sql.append(name);
                    str_sql.append("\")");
                } else {
                    str_sql.append("INSERT INTO label_category (name, parent_category_id) VALUES (\"");

                    str_sql.append(name);
                    str_sql.append("\",");
                    str_sql.append(parent_category_id);
                    str_sql.append(")");
                }

                s.executeUpdate (str_sql.toString());
            } finally {
                s.close();
            }
        }catch(SQLException ex) {
            System.out.println("SQLException (wikt_parsed TLabelCategory.java insert):: sql='" + str_sql.toString() + "' " + ex.getMessage());
        }
    }
    
    /** Selects ID from the table 'label_category' by a label category name.<br><br>
     *  SELECT id FROM label_category WHERE name="Usage";
     * @param  label_category_name    name of label category
     * @return 0 if a label category name is empty in the table 'label_category'
     */
    public static int getIDByName (Connect connect,String label_category_name) {

        if(null == label_category_name
                || label_category_name.isEmpty()) return 0;

        int result_id = 0;
        StringBuilder str_sql = new StringBuilder();
        try {
            Statement s = connect.conn.createStatement ();
            try {
                str_sql.append("SELECT id FROM label_category WHERE name=\"");
                str_sql.append(label_category_name);
                str_sql.append("\"");
                try (ResultSet rs = s.executeQuery (str_sql.toString())) {
                    if (rs.next ())
                        result_id = rs.getInt("id");
                    else
                        System.out.println("Warning: (wikt_parsed LabelCategory.getIDByName()):: name (" + label_category_name + ") is absent in the table 'label_category'.");
                }
            } finally {
                s.close();
            }
        } catch(SQLException ex) {
            System.out.println("SQLException (LabelCategory.getIDByName()):: sql='" + str_sql.toString() + "' " + ex.getMessage());
        }
        return result_id;
    }

}
