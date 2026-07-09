package com.hei.school.service.event;

import com.hei.school.endpoint.event.model.SaleConfirmedEvent;
import com.hei.school.entity.Sale;
import com.hei.school.entity.SaleItem;
import com.hei.school.file.bucket.BucketComponent;
import com.hei.school.mail.Email;
import com.hei.school.mail.Mailer;
import com.hei.school.mail.PdfGenerator;
import com.hei.school.repository.SaleRepository;
import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SaleConfirmedEventService implements Consumer<SaleConfirmedEvent> {
  private final Mailer mailer;
  private final SaleRepository saleRepository;
  private final PdfGenerator pdfGenerator;
  private final BucketComponent bucketComponent;

  @SneakyThrows
  @Transactional(readOnly = true)
  @Override
  public void accept(SaleConfirmedEvent event) {
    Sale sale = saleRepository.findById(UUID.fromString(event.getSaleId())).orElseThrow();

    StringBuilder itemsHtml = new StringBuilder();
    for (SaleItem item : sale.getItems()) {
      itemsHtml
          .append("<tr>")
          .append("<td style=\"padding:8px;border:1px solid #ddd;\">")
          .append(item.getEdition().getBook().getTitle())
          .append("</td>")
          .append("<td style=\"padding:8px;border:1px solid #ddd;text-align:center;\">")
          .append(item.getQuantity())
          .append("</td>")
          .append("<td style=\"padding:8px;border:1px solid #ddd;text-align:right;\">")
          .append(String.format("%.2f", item.getUnitPrice()))
          .append("</td>")
          .append("<td style=\"padding:8px;border:1px solid #ddd;text-align:right;\">")
          .append(String.format("%.2f", item.getLineTotal()))
          .append("</td>")
          .append("</tr>");
    }

    String tableHtml =
        "<table style=\"width:100%;border-collapse:collapse;\"><thead><tr><th"
            + " style=\"padding:8px;border:1px solid"
            + " #ddd;background-color:#f4f4f4;text-align:left;\">Book</th><th"
            + " style=\"padding:8px;border:1px solid"
            + " #ddd;background-color:#f4f4f4;text-align:center;\">Qty</th><th"
            + " style=\"padding:8px;border:1px solid"
            + " #ddd;background-color:#f4f4f4;text-align:right;\">Price</th><th"
            + " style=\"padding:8px;border:1px solid"
            + " #ddd;background-color:#f4f4f4;text-align:right;\">Total</th></tr></thead><tbody>"
            + itemsHtml
            + "</tbody></table>"
            + "<h3 style=\"text-align:right;\">Total: "
            + String.format("%.2f", sale.getTotalAmount())
            + " Ar"
            + "</h3>";

    File receiptPdf = pdfGenerator.generate(buildReceiptHtml(sale, tableHtml), "sale-receipt-");

    String bucketKey = "receipts/" + sale.getId() + ".pdf";
    bucketComponent.upload(receiptPdf, bucketKey);
    URL downloadUrl = bucketComponent.presign(bucketKey, Duration.ofDays(7));

    String htmlBody =
        "<html><body><h2>Sale Confirmed</h2><p>Thank you for your purchase!</p>"
            + tableHtml
            + "<p><a href=\""
            + downloadUrl
            + "\">Download your receipt</a></p>"
            + "</body></html>";

    InternetAddress recipient = new InternetAddress(event.getEmail());
    mailer.accept(
        new Email(recipient, List.of(), List.of(), "Sale Confirmed", htmlBody, List.of()));

    receiptPdf.delete();
  }

  private String buildReceiptHtml(Sale sale, String tableHtml) {
    String saleDate =
        sale.getSaleDate() == null
            ? ""
            : sale.getSaleDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    return "<html><body>"
        + "<h1>Receipt</h1>"
        + "<p>Sale ID: "
        + sale.getId()
        + "</p>"
        + "<p>Date: "
        + saleDate
        + "</p>"
        + "<p>Payment method: "
        + sale.getPaymentMethod()
        + "</p>"
        + tableHtml
        + "</body></html>";
  }
}
