package com.BloodBank.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class CertificateService {

    public byte[] generateCertificate(String name, String bloodGroup, LocalDate date) {

        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // ================= FONTS =================
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL);

            // ================= TITLE =================
            Paragraph title = new Paragraph("BLOOD DONATION CERTIFICATE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // ================= SUBTITLE =================
            Paragraph subtitle = new Paragraph("This certificate is proudly presented to", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(20);
            document.add(subtitle);

            // ================= NAME =================
            Paragraph namePara = new Paragraph(name.toUpperCase(), boldFont);
            namePara.setAlignment(Element.ALIGN_CENTER);
            namePara.setSpacingAfter(20);
            document.add(namePara);

            // ================= DETAILS =================
            Paragraph details = new Paragraph(
                    "For generously donating blood and helping save lives.\n\n" +
                    "Blood Group: " + bloodGroup + "\n" +
                    "Donation Date: " + date,
                    normalFont
            );
            details.setAlignment(Element.ALIGN_CENTER);
            details.setSpacingAfter(30);
            document.add(details);

            // ================= THANK YOU =================
            Paragraph thankYou = new Paragraph("❤️ Thank you for your life-saving contribution ❤️", boldFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating certificate: " + e.getMessage());
        }
    }
}