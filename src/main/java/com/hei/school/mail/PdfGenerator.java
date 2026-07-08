package com.hei.school.mail;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class PdfGenerator {

  @SneakyThrows
  public File generate(String html, String fileNamePrefix) {
    File pdfFile = File.createTempFile(fileNamePrefix, ".pdf");
    try (OutputStream os = new FileOutputStream(pdfFile)) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.withHtmlContent(html, null);
      builder.toStream(os);
      builder.run();
    }
    return pdfFile;
  }
}
