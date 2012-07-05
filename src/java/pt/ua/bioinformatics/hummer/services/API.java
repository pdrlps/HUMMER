package pt.ua.bioinformatics.hummer.services;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author pedrolopes
 */
public class API {

    static DB db = new DB();
    static HashMap<String, Integer> relList = new HashMap<String, Integer>();
    static {
        API.relList.put("pubmed2entrez", 1);
        API.relList.put("entrez2pubmed", 2);
        API.relList.put("omim2entrez", 3);
        API.relList.put("entrez2omim", 4);
        API.relList.put("uniprot2mesh", 5);
        API.relList.put("mesh2uniprot", 6);
        API.relList.put("ensembl2hgnc", 7);
        API.relList.put("hgnc2ensembl", 8);
        API.relList.put("hpo2omim", 9);
        API.relList.put("omim2hpo", 10);
        API.relList.put("hgnc2hpo", 11);
        API.relList.put("hpo2hgnc", 12);
        API.relList.put("hpo2umls", 13);
        API.relList.put("umls2hpo", 14);
        API.relList.put("uniprot2mesh", 15);
        API.relList.put("mesh2uniprot", 16);
        API.relList.put("pdb2uniprot", 17);
        API.relList.put("uniprot2pdb", 18);
        API.relList.put("pharmgkb2entrez", 19);
        API.relList.put("entrez2pharmgkb", 20);
        API.relList.put("pharmgkb2ensembl", 21);
        API.relList.put("ensembl2pharmgkb", 22);
        API.relList.put("pharmgkb2uniprot", 23);
        API.relList.put("uniprot2pharmgkb", 24);
        API.relList.put("pharmgkb2hgnc", 25);
        API.relList.put("hgnc2pharmgkb", 26);
        API.relList.put("kegg2hgnc", 27);
        API.relList.put("hgnc2kegg", 28);
        API.relList.put("uniprot2icd10", 29);
        API.relList.put("icd102uniprot", 30);
        API.relList.put("hgncid2hgncsymbol", 31);
        API.relList.put("hgncsymbol2hgncid", 32);
    }
    
    static public String list(String rel, String id, String format) {
        String response = "";
        if (!id.equals("")) {
            if (format.equals("js")) {
                JSONObject obj = new JSONObject();
                JSONArray entries = new JSONArray();
                try {
                    db.connect();
                    String query = "SELECT b AS entry FROM hummer WHERE rel =" + API.relList.get(rel) + " AND a LIKE '" + id + "'";
                    ResultSet rs = db.getData(query);
                    while (rs.next()) {
                        entries.add(rs.getString("entry"));
                    }
                    db.close();
                } catch (Exception ex) {
                    Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    obj.put("entries", entries);
                    response = obj.toJSONString();
                }
            } else if (format.equals("xml")) {
                try {
                    response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entries>";
                    db.connect();
                    String query = "SELECT b AS entry FROM hummer WHERE rel =" + API.relList.get(rel) + " AND a LIKE '" + id + "'";
                    ResultSet rs = db.getData(query);
                    while (rs.next()) {
                        response += "\n\t<entry>" + rs.getString("entry") + "</entry>";
                    }
                } catch (Exception ex) {
                    Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    db.close();
                    response += "\n</entries>";
                }
            } else if (format.equals("html")) {
                try {
                    response = "<!DOCTYPE html><html><head><title>HUMMER | " + id + " - " + rel + "</title></head><body><div align=\"center\"><h1>HUman Multi Mapper for Entities & Relationships</h1>"
                            + "<p>More information <a href=\"mailto:pedrolopes@ua.pt\">pedrolopes@ua.pt</a></p></div>"
                            + "<table><tr><td><b>" + id + " - " + rel + "</td></tr>";
                    db.connect();
                    String query = "SELECT b AS entry FROM hummer WHERE rel =" + API.relList.get(rel) + " AND a LIKE '" + id + "'";
                    ResultSet rs = db.getData(query);
                    while (rs.next()) {
                        response += "\t<tr><td>" + rs.getString("entry") + "</td></tr>";
                    }
                } catch (Exception ex) {
                    Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    db.close();
                    response += "</table></body></html>";
                }
            } else if (format.equals("csv")) {
                try {
                    db.connect();
                    String query = "SELECT b AS entry FROM hummer WHERE rel =" + API.relList.get(rel) + " AND a LIKE '" + id + "'";
                    ResultSet rs = db.getData(query);
                while (rs.next()) {
                        response += rs.getString("entry") + "\n";
                    }
                    db.close();
                } catch (Exception ex) {
                    Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return response;
    }
}
