package pt.ua.bioinformatics.hummer.services;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author pedrolopes
 */
public class Import {

    private CSVReader reader;
    private String filename = "/Users/pedrolopes/Development/Software/HUMMER/HUMMER/src/java/";
    private String from;
    private String to;
    private char delimiter;

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public CSVReader getReader() {
        return reader;
    }

    public void setReader(CSVReader reader) {
        this.reader = reader;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Import(String from, String to, String file, char delimiter) {
        this.from = from;
        this.to = to;
        this.filename += file;
        this.delimiter = delimiter;
    }

    public void load() {
        String[] nextLine;
        DB db = new DB();
        try {
            reader = new CSVReader(new FileReader(filename), delimiter);
            db.connect();
            while ((nextLine = reader.readNext()) != null) {
                if (!nextLine[1].equals("")) {
                    db.insert(nextLine[0], "INSERT INTO hummer (a, b, rel) values('" + nextLine[0] + "','" + nextLine[1] + "'," + API.relList.get(from + "2" + to) + ");");
                    db.insert(nextLine[1], "INSERT INTO hummer (a, b, rel) values('" + nextLine[1] + "','" + nextLine[0] + "'," + API.relList.get(to + "2" + from) + ");");
                }
            }
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void load(String what) {
        if (what.equals("uniprot2icd10")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/ICD10/uniprot2icd10"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    String[] cache = nextLine[2].split(" / ");
                    for (String icd : cache) {
                        db.insert(nextLine[0], "INSERT INTO hummer (a, b, rel) values('" + nextLine[0] + "','" + icd + "'," + API.relList.get(from + "2" + to) + ");");
                        db.insert(icd, "INSERT INTO hummer (a, b, rel) values('" + icd + "','" + nextLine[0] + "'," + API.relList.get(to + "2" + from) + ");");
                    }
                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("hgncid2hgncsymbol")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/HGNC/hgnc2symbol"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    String hgnc = nextLine[0].replace("HGNC:", "");
                    db.insert(hgnc, "INSERT INTO hummer (a, b, rel) values('" + hgnc + "','" + nextLine[1] + "'," + API.relList.get(from + "2" + to) + ");");
                    db.insert(nextLine[1], "INSERT INTO hummer (a, b, rel) values('" + nextLine[1] + "','" + hgnc + "'," + API.relList.get(to + "2" + from) + ");");
                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("kegg2hgnc")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/KEGG/kegg2hgnc"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    String[] cache = nextLine[1].split(":");
                    db.insert(nextLine[0], "INSERT INTO hummer (a, b, rel) values('" + nextLine[0] + "','" + cache[1] + "'," + API.relList.get(from + "2" + to) + ");");
                    db.insert(cache[1], "INSERT INTO hummer (a, b, rel) values('" + cache[1] + "','" + nextLine[0] + "'," + API.relList.get(to + "2" + from) + ");");

                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("pharmgkb2entrez")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/PharmGKB/pharmgkb2entrez"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    db.insert(nextLine[0], "INSERT INTO hummer (a, b, rel) values('" + nextLine[0] + "','" + nextLine[1] + "'," + API.relList.get(from + "2" + to) + ");");
                    db.insert(nextLine[2], "INSERT INTO hummer (a, b, rel) values('" + nextLine[1] + "','" + nextLine[0] + "'," + API.relList.get(to + "2" + from) + ");");

                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("pharmgkb2ensembl")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/PharmGKB/pharmgkb2ensembl"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    if (!nextLine[2].equals("")) {
                        db.insert(nextLine[0], "INSERT INTO hummer (a, b, rel) values('" + nextLine[0] + "','" + nextLine[2] + "'," + API.relList.get(from + "2" + to) + ");");
                        db.insert(nextLine[2], "INSERT INTO hummer (a, b, rel) values('" + nextLine[2] + "','" + nextLine[0] + "'," + API.relList.get(to + "2" + from) + ");");
                    }
                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("pharmgkb2uniprot")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/PharmGKB/pharmgkb2uniprot"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    if (!nextLine[3].equals("")) {
                        db.insert(nextLine[0], "INSERT INTO hummer (a, b, rel) values('" + nextLine[0] + "','" + nextLine[3] + "'," + API.relList.get(from + "2" + to) + ");");
                        db.insert(nextLine[3], "INSERT INTO hummer (a, b, rel) values('" + nextLine[3] + "','" + nextLine[0] + "'," + API.relList.get(to + "2" + from) + ");");
                    }
                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("pharmgkb2hgnc")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/PharmGKB/pharmgkb2hgnc"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    if (!nextLine[5].equals("")) {
                        db.insert(nextLine[0], "INSERT INTO hummer (a, b, rel) values('" + nextLine[0] + "','" + nextLine[5] + "'," + API.relList.get(from + "2" + to) + ");");
                        db.insert(nextLine[5], "INSERT INTO hummer (a, b, rel) values('" + nextLine[5] + "','" + nextLine[0] + "'," + API.relList.get(to + "2" + from) + ");");
                    }
                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (what.equals("pdb2uniprot")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/PDB/pdb2uniprot"), delimiter);
                Pattern p = Pattern.compile("\\((.*?)\\)");
                ArrayList<String> list = new ArrayList<String>();
                db.connect();
                String temp = "";
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine[0].endsWith(",")) {
                        temp += nextLine[0];
                    } else {
                        String line = temp + nextLine[0];
                        Matcher m = p.matcher(line);
                        String pdb = line.substring(0, 4);
                        while (m.find()) {
                            String uniprot = m.group().replace("(", "").replace(")", "");
                            db.insert(pdb, "INSERT INTO hummer (a, b, rel) values('" + pdb + "','" + uniprot + "'," + API.relList.get(from + "2" + to) + ");");
                            db.insert(uniprot, "INSERT INTO hummer (a, b, rel) values('" + uniprot + "','" + pdb + "'," + API.relList.get(to + "2" + from) + ");");
                        }
                        temp = "";
                    }
                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (what.equals("hpo2umls")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/HPO/hpo2umls"), delimiter);
                Pattern p = Pattern.compile("\\(HP:(.*?)\\)");
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    Matcher m = p.matcher(nextLine[0]);
                    if (m.find()) {
                        String hpo = m.group().replace("(", "").replace(")", "");
                        String umls = nextLine[1].substring(0, 8);

                        db.insert(hpo, "INSERT INTO hummer (a, b, rel) values('" + hpo + "','" + umls + "'," + API.relList.get(from + "2" + to) + ");");
                        db.insert(umls, "INSERT INTO hummer (a, b, rel) values('" + umls + "','" + hpo + "'," + API.relList.get(to + "2" + from) + ");");
                    }
                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("hpo2hgnc")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/HPO/hpo2hgnc.txt"), delimiter);
                Pattern p = Pattern.compile("\\(HP:(.*?)\\)");


                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    System.out.println(nextLine[1]);
                    Matcher m = p.matcher(nextLine[2]);
                    while (m.find()) {
                        String hpo = m.group().replace("(", "").replace(")", "");
                        db.insert(hpo, "INSERT INTO hummer (a, b, rel) values('" + hpo + "','" + nextLine[1] + "'," + API.relList.get(from + "2" + to) + ");");
                        db.insert(nextLine[1], "INSERT INTO hummer (a, b, rel) values('" + nextLine[1] + "','" + hpo + "'," + API.relList.get(to + "2" + from) + ");");
                    }
                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("hpo2omim")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/HPO/hpo2omim"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    //System.out.println(nextLine[1] +" - " + nextLine[4]);
                    db.insert(nextLine[4], "INSERT INTO hummer (a, b, rel) values('" + nextLine[4] + "','" + nextLine[1] + "'," + API.relList.get(from + "2" + to) + ");");
                    db.insert(nextLine[1], "INSERT INTO hummer (a, b, rel) values('" + nextLine[1] + "','" + nextLine[4] + "'," + API.relList.get(to + "2" + from) + ");");

                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("uniprot2mesh")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/MeSH/uniprot2mesh"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    //System.out.println(nextLine[1] +" - " + nextLine[4]);
                    db.insert(nextLine[0], "INSERT INTO hummer (a, b, rel) values('" + nextLine[0] + "','" + nextLine[1] + "'," + API.relList.get(from + "2" + to) + ");");
                    db.insert(nextLine[1], "INSERT INTO hummer (a, b, rel) values('" + nextLine[1] + "','" + nextLine[0] + "'," + API.relList.get(to + "2" + from) + ");");

                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (what.equals("entrez2pubmed")) {
            String[] nextLine;
            DB db = new DB();
            try {
                reader = new CSVReader(new FileReader("/Users//pedrolopes/Development/Software/DC4/Sources/EntrezGene/entrez2pubmed"), delimiter);
                db.connect();
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine[0].equals("9606")) {
                        db.insert(nextLine[1], "INSERT INTO hummer (a, b, rel) values('" + nextLine[1] + "','" + nextLine[2] + "'," + API.relList.get(from + "2" + to) + ");");
                        db.insert(nextLine[2], "INSERT INTO hummer (a, b, rel) values('" + nextLine[2] + "','" + nextLine[1] + "'," + API.relList.get(to + "2" + from) + ");");
                    }
                }
                db.close();
            } catch (Exception ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (what.equals("omim2orphanet")) {
            DB db = new DB();
            try {
                db.connect();
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true);
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                Document doc = builder.parse("src/orphanet.xml");
                XPath disorders = XPathFactory.newInstance().newXPath();
                XPathExpression exprDisorders = disorders.compile("//Disorder");
                NodeList nodes = (NodeList) exprDisorders.evaluate(doc, XPathConstants.NODESET);
                    Pattern p = Pattern.compile("[0-9]{6}");

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node disorder = nodes.item(i);
                    XPath ids = XPathFactory.newInstance().newXPath();
                    XPathExpression exprIds = ids.compile("@id");
                    Node id = (Node) exprIds.evaluate(disorder, XPathConstants.NODE);
                    String orphanet = id.getNodeValue();
                    try {
                        XPath omims = XPathFactory.newInstance().newXPath();
                        XPathExpression exprOmim = omims.compile("ExternalReferenceList/ExternalReference/Reference");
                        NodeList resultOmim = (NodeList) exprOmim.evaluate(disorder, XPathConstants.NODESET);

                        for (int j = 0; j < resultOmim.getLength(); j++) {

                            Matcher m = p.matcher(resultOmim.item(j).getTextContent());
                            while (m.find()) {
                                String omim = m.group();
                                db.insert(orphanet, "INSERT INTO hummer (a, b, rel) values('" + omim + "','" + orphanet + "'," + API.relList.get("omim2orphanet") + ");");
                                db.insert(orphanet, "INSERT INTO hummer (a, b, rel) values('" + orphanet + "','" + omim + "'," + API.relList.get("orphanet2omim") + ");");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

         if (what.equals("orphanet2icd10")) {
            DB db = new DB();
            try {
                db.connect();
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true);
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                Document doc = builder.parse("src/orphanet.xml");
                XPath disorders = XPathFactory.newInstance().newXPath();
                XPathExpression exprDisorders = disorders.compile("//Disorder");
                NodeList nodes = (NodeList) exprDisorders.evaluate(doc, XPathConstants.NODESET);
                    Pattern p = Pattern.compile("^[A-Z]\\d{2}(\\.\\d){0,1}$");

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node disorder = nodes.item(i);
                    XPath ids = XPathFactory.newInstance().newXPath();
                    XPathExpression exprIds = ids.compile("@id");
                    Node id = (Node) exprIds.evaluate(disorder, XPathConstants.NODE);
                    String orphanet = id.getNodeValue();
                    try {
                        XPath omims = XPathFactory.newInstance().newXPath();
                        XPathExpression exprOmim = omims.compile("ExternalReferenceList/ExternalReference/Reference");
                        NodeList resultOmim = (NodeList) exprOmim.evaluate(disorder, XPathConstants.NODESET);

                        for (int j = 0; j < resultOmim.getLength(); j++) {

                            Matcher m = p.matcher(resultOmim.item(j).getTextContent());
                            while (m.find()) {
                                String icd10 = m.group();
                                db.insert(orphanet, "INSERT INTO hummer (a, b, rel) values('" + orphanet + "','" + icd10  + "'," + API.relList.get("orphanet2icd10") + ");");
                                db.insert(orphanet, "INSERT INTO hummer (a, b, rel) values('" + icd10 + "','" + orphanet + "'," + API.relList.get("icd102orphanet") + ");");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
