import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class MainUI extends JFrame {

    JTextArea areaA = new JTextArea();
    JTextArea areaB = new JTextArea();
    JLabel result = new JLabel("Similarity: 0%");
    File fileA, fileB;

    public MainUI() {
        DBUtil.createTable();

        setTitle("Plagiarism Detector");
        setSize(900,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton upA = new JButton("Upload A");
        JButton upB = new JButton("Upload B");
        JButton check = new JButton("Check");
        JButton pdf = new JButton("Export PDF");

        JPanel top = new JPanel();
        top.add(upA); top.add(upB); top.add(check); top.add(pdf); top.add(result);
        add(top,BorderLayout.NORTH);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(areaA), new JScrollPane(areaB));
        add(sp);

        upA.addActionListener(e -> fileA = load(areaA));
        upB.addActionListener(e -> fileB = load(areaB));

        check.addActionListener(e -> runSimilarity());
        pdf.addActionListener(e ->
                PDFUtil.export(result.getText(), areaA.getText(), areaB.getText()));

        setVisible(true);
    }

    File load(JTextArea area){
        JFileChooser fc = new JFileChooser();
        if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            File f=fc.getSelectedFile();
            try{
                area.setText(new String(Files.readAllBytes(f.toPath())));
            }catch(Exception ex){ex.printStackTrace();}
            return f;
        }
        return null;
    }

    void runSimilarity(){
        String a=SimilarityUtil.clean(areaA.getText());
        String b=SimilarityUtil.clean(areaB.getText());

        double sim=SimilarityUtil.similarity(
                SimilarityUtil.freqMap(SimilarityUtil.tokenize(a)),
                SimilarityUtil.freqMap(SimilarityUtil.tokenize(b)));

        result.setText("Similarity: "+String.format("%.2f",sim)+"%");

        if(fileA!=null && fileB!=null)
            DBUtil.saveReport(fileA.getName(), fileB.getName(), sim);
    }

    public static void main(String[] args){
        new MainUI();
    }
}
