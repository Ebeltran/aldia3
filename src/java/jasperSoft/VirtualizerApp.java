/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasperSoft;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class VirtualizerApp extends AbstractSampleApp {

//    /**
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
//        main(new VirtualizerApp(), args);
//    }

    /**
     * metodo test
     * @throws net.sf.jasperreports.engine.JRException
     */
    @Override
    public void test() throws JRException {
        export();
    }

    /**
     *
     * @throws net.sf.jasperreports.engine.JRException
     */
    public void view() throws JRException {
        JasperPrint jasperPrint = fillReport();

        JasperViewer.viewReport(jasperPrint, true);
    }

    /**
     *
     * @throws net.sf.jasperreports.engine.JRException
     */
    public void print() throws JRException {
        JasperPrint jasperPrint = fillReport();

        JasperPrintManager.printReport(jasperPrint, true);
    }

    /**
     *
     * @throws net.sf.jasperreports.engine.JRException
     */
    public void pdf() throws JRException {
        JasperPrint jasperPrint = fillReport();

        exportPdf(jasperPrint);
    }

    /**
     *
     * @throws net.sf.jasperreports.engine.JRException
     */
    public void xml() throws JRException {
        JasperPrint jasperPrint = fillReport();

        exportXml(jasperPrint, false);
    }

    /**
     *
     * @throws net.sf.jasperreports.engine.JRException
     */
    public void xmlEmbed() throws JRException {
        JasperPrint jasperPrint = fillReport();

        exportXml(jasperPrint, true);
    }

    /**
     *
     * @throws net.sf.jasperreports.engine.JRException
     */
    public void csv() throws JRException {
        JasperPrint jasperPrint = fillReport();

        exportCsv(jasperPrint);
    }

    /**
     *
     * @throws net.sf.jasperreports.engine.JRException
     */
    public void export() throws JRException {
        // creating the virtualizer
        JRFileVirtualizer virtualizer = new JRFileVirtualizer(2, "tmp");

        JasperPrint jasperPrint = fillReport(virtualizer);

        exportPdf(jasperPrint);
        exportXml(jasperPrint, false);
        exportHtml(jasperPrint);
        exportCsv(jasperPrint);

        // manually cleaning up
        virtualizer.cleanup();
    }

    private static JasperPrint fillReport() throws JRException {
        // creating the virtualizer
        JRFileVirtualizer virtualizer = new JRFileVirtualizer(2, "tmp");

        return fillReport(virtualizer);
    }

    private static JasperPrint fillReport(JRFileVirtualizer virtualizer) throws JRException {
        long start = System.currentTimeMillis();

		// Virtualization works only with in memory JasperPrint objects.
        // All the operations will first fill the report and then export
        // the filled object.
        // creating the data source
        JRDataSource dataSource = new JREmptyDataSource(1000);

        // Preparing parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

        // filling the report
        JasperPrint jasperPrint = JasperFillManager.fillReport("build/reports/VirtualizerReport.jasper", parameters, dataSource);

        virtualizer.setReadOnly(true);

        System.err.println("Filling time : " + (System.currentTimeMillis() - start));
        return jasperPrint;
    }

    private static void exportCsv(JasperPrint jasperPrint) throws JRException {
        long start = System.currentTimeMillis();
        JRCsvExporter exporter = new JRCsvExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput("build/reports/" + jasperPrint.getName() + ".csv"));

        exporter.exportReport();

        System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));
    }

    private static void exportHtml(JasperPrint jasperPrint) throws JRException {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToHtmlFile(jasperPrint, "build/reports/" + jasperPrint.getName() + ".html");
        System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
    }

    private static void exportXml(JasperPrint jasperPrint, boolean embedded) throws JRException {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToXmlFile(jasperPrint, "build/reports/" + jasperPrint.getName() + ".jrpxml", embedded);
        System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
    }

    private static void exportPdf(JasperPrint jasperPrint) throws JRException {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToPdfFile(jasperPrint, "build/reports/" + jasperPrint.getName() + ".pdf");
        System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
    }

}
