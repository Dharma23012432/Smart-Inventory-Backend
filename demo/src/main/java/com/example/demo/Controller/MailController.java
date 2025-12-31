package com.example.demo.Controller;

import com.example.demo.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class MailController {

    @Autowired
    private MailService mailService;

    // ---------- TEST ----------
    @GetMapping("/test")
    public String testMail() {
        boolean success = mailService.testMailConnection();
        return success ? "Mail working" : "Mail failed";
    }

    // ---------- DTO ----------
    public static class InvoiceEmailRequest {
        public String to;
        public String invoiceNumber;
        public String date;
        public Double subtotal;
        public Double cgst;
        public Double sgst;
        public Double total;
        public java.util.List<InvoiceItem> items;

        public static class InvoiceItem {
            public String name;
            public int quantity;
            public double price;
        }
    }

    // ---------- SEND INVOICE (NON BLOCKING) ----------
    @PostMapping("/invoice")
    public String sendInvoiceEmail(@RequestBody InvoiceEmailRequest req) {

        if (req == null || req.to == null || req.to.isEmpty()) {
            return "Recipient email required";
        }

        StringBuilder body = new StringBuilder();
        body.append("Dear Customer,\n\n");
        body.append("Thank you for your purchase.\n\n");
        body.append("Invoice No: ").append(req.invoiceNumber).append("\n");
        body.append("Date: ").append(req.date).append("\n\n");

        if (req.items != null) {
            body.append("Items:\n");
            for (InvoiceEmailRequest.InvoiceItem it : req.items) {
                body.append(
                        String.format(
                                " - %s x%d @ %.2f = %.2f\n",
                                it.name,
                                it.quantity,
                                it.price,
                                it.price * it.quantity
                        )
                );
            }
        }

        body.append("\n");
        body.append(String.format("Subtotal: ₹%.2f\n", req.subtotal != null ? req.subtotal : 0));
        body.append(String.format("CGST: ₹%.2f\n", req.cgst != null ? req.cgst : 0));
        body.append(String.format("SGST: ₹%.2f\n", req.sgst != null ? req.sgst : 0));
        body.append(String.format("Grand Total: ₹%.2f\n", req.total != null ? req.total : 0));
        body.append("\n\nRegards,\nSmart Inventory System");

        // 🔥 ASYNC CALL
        mailService.sendInvoiceAsync(
                req.to,
                "Your Invoice " + req.invoiceNumber,
                body.toString()
        );

        // 🚀 RETURN IMMEDIATELY
        return "Invoice email queued";
    }
}
