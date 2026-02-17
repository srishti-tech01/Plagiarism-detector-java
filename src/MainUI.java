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
        setSize(950,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30,30,40));
        add(mainPanel);

        // TITLE
        JLabel title = new JLabel("PLAGIARISM DETECTOR", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        mainPanel.add(title, BorderLayout.NORTH);

        // TEXT AREAS STYLE
        styleTextArea(areaA);
        styleTextArea(areaB);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(areaA), new JScrollPane(areaB));
        sp.setDividerLocation(450);
        mainPanel.add(sp, BorderLayout.CENTER);

        // BUTTON PANEL
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(30,30,40));

        JButton upA = styledButton("Upload A", new Color(70,70,90));
        JButton upB = styledButton("Upload B", new Color(70,70,90));
        JButton check = styledButton("Check", new Color(0,153,255));
        JButton pdf = styledButton("Export PDF", new Color(0,200,120));

        result.setForeground(Color.YELLOW);
        result.setFont(new Font("Arial", Font.BOLD, 18));

        bottom.add(upA);
        bottom.add(upB);
        bottom.add(check);
        bottom.add(pdf);
        bottom.add(result);

      

        // FOOTER
        JLabel footer = new JLabel("Developed by Srishti", SwingConstants.CENTER);
        footer.setForeground(Color.LIGHT_GRAY);
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
       JPanel southPanel = new JPanel(new BorderLayout());
southPanel.setBackground(new Color(30,30,40));

southPanel.add(bottom, BorderLayout.CENTER);
southPanel.add(footer, BorderLayout.SOUTH);

mainPanel.add(southPanel, BorderLayout.SOUTH);


        // UPLOAD A
        upA.addActionListener(e -> {
            fileA = load(areaA);
            if(fileA != null){
                upA.setText("A: " + fileA.getName());
                JOptionPane.showMessageDialog(this,"File A Uploaded!");
            }
        });

        // UPLOAD B
        upB.addActionListener(e -> {
            fileB = load(areaB);
            if(fileB != null){
                upB.setText("B: " + fileB.getName());
                JOptionPane.showMessageDialog(this,"File B Uploaded!");
            }
        });

        // CHECK SIMILARITY
        check.addActionListener(e -> runSimilarity());

        // EXPORT PDF
        pdf.addActionListener(e ->
                PDFUtil.export(result.getText(), areaA.getText(), areaB.getText()));

        setVisible(true);
    }

    void styleTextArea(JTextArea area){
        area.setBackground(new Color(45,45,60));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
    }

    JButton styledButton(String text, Color bg){
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        return btn;
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
