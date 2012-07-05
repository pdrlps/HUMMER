package pt.ua.bioinformatics.hummer.services;

/**
 *
 * @author pedrolopes
 */
public class Loader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // ensembl information
        //Import ensembl = new Import("ensembl","hgnc","ensembl2hgnc",',');
       //ensembl.load();
        // mesh
       // Import uniprot = new Import("entrez", "pubmed", "entrez2pubmed",'\t');
        //uniprot.load("entrez2pubmed");
//        Import omim = new Import("omim","entrez","omim2entrez",'\t');
//       omim.load();
        //Import uniprot = new Import("uniprot","mesh","uniprot2mesh",'\t');
       //uniprot.load();
       // Import hpo = new Import("hpo","omim","hpo2omim",'\t');
       // hpo.load("hpo2omim");
        //Import hpo = new Import("hpo","hgnc","hpo2hgnc",'\t');
        //hpo.load("hpo2hgnc");
        //Import uni = new Import("pdb","uniprot","pdb2uniprot",'\t');
        //uni.load("pdb2uniprot");
        //Import pharm = new Import("pharmgkb","hgnc","pharmgkb2hgnc",'\t');
        //pharm.load("pharmgkb2hgnc");
        //Import kegg = new Import("kegg","hgnc","kegg2hgnc",'\t');
        //kegg.load("kegg2hgnc");
        Import icd = new Import("hgncid","hgncsymbol","hgncid2hgncsymbol",'\t');
        icd.load("hgncid2hgncsymbol");
    }
}
