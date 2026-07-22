/**
 * Package repository.
 *
 * Pada versi ini seluruh data (recipients, riwayat pengiriman, konfigurasi SMTP,
 * dan template email) disimpan secara in-memory melalui layer service
 * (RecipientService, HistoryService, SmtpConfigService, TemplateService),
 * sesuai kebutuhan migrasi dari aplikasi Java Console yang juga tidak
 * menggunakan database.
 *
 * Package ini disediakan sebagai tempat untuk menambahkan implementasi
 * Spring Data JPA (misalnya JpaRepository) apabila di kemudian hari
 * aplikasi ingin menyimpan data secara persisten ke database
 * (MySQL/PostgreSQL/H2), tanpa perlu mengubah struktur project.
 */
package com.emailmarketing.repository;
