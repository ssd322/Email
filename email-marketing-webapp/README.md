# Email Marketing Web App (Spring Boot)

Migrasi dari **Java Console Email Marketing (Maven)** menjadi aplikasi web
berbasis **Spring Boot 3.x + Thymeleaf + Bootstrap 5**, dengan arsitektur MVC.

## Teknologi
- Java 25
- Spring Boot 3.3.x
- Maven
- Thymeleaf
- Bootstrap 5 + Bootstrap Icons
- Jakarta Mail (JavaMail)
- Spring Validation

## Cara Menjalankan

```bash
mvn spring-boot:run
```

Lalu buka browser: **http://localhost:8080**

Login default (bisa diubah di `application.properties`):
- Username: `admin`
- Password: `admin123`

## Konfigurasi SMTP Default

Sebelum mengirim email sungguhan, ubah pengaturan default di
`src/main/resources/application.properties`:

```properties
smtp.host=smtp.gmail.com
smtp.port=587
smtp.username=youremail@gmail.com
smtp.password=your-app-password
smtp.tls=true
```

Atau ubah langsung dari halaman **SMTP** setelah login (berlaku selama
aplikasi berjalan / runtime).

> Jika menggunakan Gmail, gunakan **App Password**, bukan password akun biasa
> (aktifkan 2-Step Verification lalu buat App Password di myaccount.google.com/apppasswords).

## Struktur Project

```
src/main/java/com/emailmarketing/
├── EmailMarketingApplication.java
├── config/          -> AppConfig, LoginInterceptor, WebMvcConfig, AsyncConfig
├── controller/       -> Auth, Dashboard, Smtp, Csv, Template, SendEmail, History
├── model/             -> Recipient, SmtpConfig, EmailTemplate, SendHistory, SendProgress
├── service/            -> CsvReaderService, EmailSenderService, SmtpConfigService,
│                          TemplateService, RecipientService, HistoryService, SendProgressService
└── repository/          -> disediakan untuk pengembangan DB di masa depan (belum dipakai)

src/main/resources/
├── application.properties
├── templates/     -> login, dashboard, smtp, upload-csv, template, send-email, history
│   └── fragments/ -> head, sidebar, navbar, scripts
└── static/
    ├── css/style.css
    └── js/ (main.js, template.js, send-email.js)
```

## Fitur

1. **Login Admin** - session-based, kredensial dari `application.properties`
2. **Dashboard** - card jumlah terkirim/gagal/penerima + tombol kirim email
3. **SMTP** - form pengaturan host/port/username/password/TLS
4. **Upload CSV** - upload `recipients.csv`, preview, validasi format email
5. **Template Email** - subject + editor HTML + live preview (`{{nama}}`, `{{email}}`)
6. **Kirim Email** - tombol kirim, progress bar realtime (AJAX polling),
   status berhasil/gagal per penerima, delay antar email dapat diatur
7. **Riwayat Pengiriman** - tanggal, email tujuan, status, pesan error

## Catatan Migrasi Logika Lama

Logika dari project Java Console lama dipindahkan ke layer **service**
tanpa menghilangkan fungsinya, hanya diadaptasi menjadi Spring Bean (DI):

| Class Lama   | Menjadi                                             |
|--------------|------------------------------------------------------|
| `Config`     | `config/AppConfig.java` + `service/SmtpConfigService.java` |
| `CsvReader`  | `service/CsvReaderService.java`                       |
| `EmailSender`| `service/EmailSenderService.java` (pakai Jakarta Mail)|
| `Recipient`  | `model/Recipient.java`                                |

## Format CSV

```
nama,email
John Doe,john@example.com
Jane Smith,jane@example.com
```

Baris dengan format email tidak valid akan tetap ditampilkan pada preview
(ditandai merah) tetapi otomatis dilewati saat proses pengiriman.
