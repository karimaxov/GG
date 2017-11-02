package sample.Controllers;

import java.awt.*;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.io.File;

public class DocumentGenerator{


    public static void RemplacerLesChamps(String modelName, String destination, String[] keys,
                                          String[] values, boolean openFile)
    {
        final String folderLocation = "src\\sample\\Models\\";
        try{
            XWPFDocument doc = new XWPFDocument(new FileInputStream(folderLocation+modelName+".docx"));

            int curKey = 0;
            for (XWPFParagraph p : doc.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                System.out.println(p.getText());
                //System.out.println(p.getText());

                if (runs != null) {
                    //System.out.println("not null");
                    for (XWPFRun r : runs) {
                        //System.out.println(r.getText(0));
                        String text = r.getText(0);
                        if (text != null && curKey < keys.length && text.contains("{$"+keys[curKey]+"}")) {
                            text = text.replace("{$"+keys[curKey]+"}", values[curKey]);
                            r.setText(text, 0);
                            System.out.println("found key : "+curKey);
                            curKey++;
                        }
                        //System.out.println(text);
                    }
                }
            }
            doc.write(new FileOutputStream(folderLocation+destination+".docx"));
            doc.close();
            if(openFile){
                File f = new File(folderLocation+destination+".docx");
                Desktop.getDesktop().open(f);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void RemplirTableu(String modelName, String destination, String[][] values,
                                     boolean openFile)
    {
        /*values = new String[][]{//testing
            new String[]{"cont1","cont2","cont3","cont4","cont5"},
            new String[]{"chauf1","chauf2","chauf3","chauf3","chauf3"},
            new String[]{"cam1","cam2","cam3","cam3","cam3"}
        };*/
        final String folderLocation = "src\\sample\\Models\\";
        try{
            XWPFDocument doc = new XWPFDocument(new FileInputStream(folderLocation+modelName+".docx"));

            List<XWPFTable> tables = doc.getTables();
            if(tables == null || tables.size() == 0)
            {
                System.out.println("Pas de tableau dans le fichier "+modelName);
                return;
            }
            List<XWPFTableRow> rows = tables.get(0).getRows();
            if(rows == null || rows.size() < 1)
            {
                System.out.println("Le tableau du fichier "+modelName+" ne peut pas etre rempli");
                return;
            }

            int fontSize = rows.get(0).getCell(0).getParagraphs().get(0).getRuns().get(0).getFontSize();
            boolean fontBold = rows.get(0).getCell(0).getParagraphs().get(0).getRuns().get(0).isBold();

            for(int i = 0;i<values.length;i++){
                String text = values[i][0];
                XWPFParagraph p = rows.get(1).getCell(i).addParagraph();
                for(int j = 0;j<values[0].length;j++){
                    XWPFRun r = p.createRun();
                    r.setText(values[i][j],0);
                    r.setBold(fontBold);
                    r.setFontSize(fontSize);
                    r.addBreak();
                }
            }
            doc.write(new FileOutputStream(folderLocation+destination+".docx"));
            doc.close();
            if(openFile){
                File f = new File(folderLocation+destination+".docx");
                Desktop.getDesktop().open(f);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }




}
